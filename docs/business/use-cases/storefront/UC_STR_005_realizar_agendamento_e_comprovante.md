# UC_STR_005 - Realizar Checkout de Agendamento e Emitir Comprovante

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_STR_005` (engloba `UC17` e `UC18` em [`storefront.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/storefront.puml)) |
| **Nome** | Realizar Checkout de Agendamento, Prevenção de Double-Booking e Comprovante |
| **Módulo** | Vitrine de Serviços e Agendamento Online Autônomo |
| **Atores Primários** | Visitante (*Público Geral*), Cliente Registrado (*Perfil 4*) |
| **Atores Secundários** | Motor Transacional (`UnitOfWork`), Serviço de Agendamento (`AgendamentoService`) |
| **Tipo** | Essencial / Concreto (com Inclusões de Prevenção de Conflito) |
| **Frequência de Uso** | Muito Alta (conclusão do fluxo de reserva de horário de atendimento) |
| **Rastreabilidade** | [`Router.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/Router.java), [`AgendamentoController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AgendamentoController.java), [`AgendamentoService.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/service/AgendamentoService.java), [`RN-AGE-03`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_agendamento.md#rn-age-03-bloqueio-de-hor%C3%A1rios-no-passado), [`RN-AGE-04`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_agendamento.md#rn-age-04-atribui%C3%A7%C3%A3o-autom%C3%A1tica-qualquer-profissional), [`RN-AGE-05`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_agendamento.md#rn-age-05-preven%C3%A7%C3%A3o-at%C3%B4mica-de-double-booking), [`RN-ATE-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_atendimento_comandas.md#rn-ate-01-ciclo-de-vida-do-atendimento-e-transi%C3%A7%C3%A3o-de-status) |

---

## 1. 🎯 Descrição Sumária

Permite ao cliente revisar os dados do agendamento escolhido na grade (serviço, profissional, data e horário) e preencher seus dados pessoais de contato na tela `/checkout`. Ao confirmar a reserva (`POST /checkout/confirmar`), o sistema processa a criação do agendamento de forma atômica protegida por transação `UnitOfWork` contra conflitos concorrentes (*double-booking* conforme `RN-AGE-05`). Caso o cliente não tenha especificado um barbeiro, o sistema atribui automaticamente o primeiro profissional disponível (`RN-AGE-04`). Concluída a reserva com status `"Confirmado"`, o sistema redireciona para a view `/order-confirmation?id={id}`, emitindo o comprovante digital com orientações para o atendimento presencial.

---

## 2. ⚡ Pré-Condições

1. O cliente deve ter selecionado um serviço válido e um slot de horário disponível na etapa anterior (`UC_STR_004`).
2. O horário selecionado não pode pertencer ao passado no momento do carregamento da tela (`RN-AGE-03`).

---

## 3. ✅ Pós-Condições

1. Registro criado com sucesso na tabela `tab_agendamento` com status `'Confirmado'`.
2. Os blocos de 20 minutos correspondentes tornam-se imediatamente indisponíveis para outros clientes na grade pública.
3. Exibição da tela de confirmação formal `/order-confirmation?id={id}` contendo código da reserva e resumo.

---

## 4. 🚀 Gatilho (Trigger)

* O usuário seleciona um horário disponível na tela de serviços (`/servicos`) e clica para avançar para o agendamento.

---

## 5. 🔄 Fluxo Principal (Checkout e Confirmação da Reserva)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Usuário | Clica em um horário vago na grade de `/servicos`. |
| **2** | Sistema | Redireciona para `GET /checkout?servicoId={id}&profissionalId={id}&dataHora={iso}`. |
| **3** | Sistema | [`Router.checkout`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/Router.java) carrega o serviço e profissional, formata a data para padrão legível (ex: *"15 de Junho, às 14:20"*) e verifica se há cliente autenticado na sessão para pré-preencher nome, e-mail e telefone. |
| **4** | Sistema | Renderiza a view `site/cart/checkout.html`. |
| **5** | Usuário | Confere os detalhes do agendamento, preenche/revisa os campos de contato (`nome`, `sobrenome`, `email`, `telefone`) e clica em "Confirmar Agendamento". |
| **6** | Sistema | Submete requisição via `POST /checkout/confirmar`. |
| **7** | Sistema | [`AgendamentoController.confirmarReserva`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AgendamentoController.java) invoca `AgendamentoService.confirmarReserva(...)`. |
| **8** | Sistema (*RN-AGE-04*) | Se `profissionalId` for nulo ou zero, o sistema seleciona o primeiro profissional ativo livre para toda a duração do serviço. |
| **9** | Sistema (*RN-AGE-05*) | Dentro de uma transação `UnitOfWork`, executa a query de bloqueio de double-booking verificando se algum outro cliente reservou a mesma vaga. |
| **10** | Sistema | Persiste a entidade `Agendamento` com status `"Confirmado"`, data, hora de início e hora calculada de término (`RN-ATE-01`). |
| **11** | Sistema | Confirma o commit transacional e redireciona o usuário para `GET /order-confirmation?id={agendamentoId}`. |
| **12** | Sistema (*UC18*) | [`Router.orderConfirmation`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/Router.java) renderiza a view `site/cart/order-confirmation.html`. |
| **13** | Usuário | Visualiza o comprovante oficial com endereço da barbearia, horário marcado e recomendações de pontualidade. |

---

## 6. 🔀 Fluxos Alternativos

### **FA01 - Cliente Autenticado Realizando Agendamento**
* **No Passo 3:** O cliente já está logado no sistema (`session.getAttribute("usuarioLogado") != null`).
* **Ação do Sistema:** O sistema recupera a entidade `Cliente` e preenche previamente todos os inputs do formulário com seus dados cadastrais, reduzindo a fricção para apenas um clique de confirmação.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Horário Colidiu com Outra Reserva Concorrente (Double-Booking - RN-AGE-05)**
* **No Passo 9:** No intervalo de preenchimento do formulário, outro cliente confirmou a mesma cadeira no mesmo horário.
* **Ação do Sistema:**
  1. A query de validação concorrente detecta a sobreposição.
  2. A transação JDBC sofre Rollback automático.
  3. O sistema lança exceção com mensagem amigável: *"Desculpe, este horário acabou de ser reservado por outro cliente. Por favor, escolha outro horário."*
  4. Redireciona de volta para `/checkout` preservando os parâmetros e exibindo o alerta de erro flash.

### **FE02 - Horário Selecionado Ficou no Passado (RN-AGE-03)**
* Caso a dataHora enviada seja anterior ao momento atual, a tela de checkout exibe mensagem impeditiva instruindo o cliente a escolher um horário futuro.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-AGE-03** | Bloqueio de Horários no Passado | Impede submissão de reservas em datas ou horários já transcorridos. |
| **RN-AGE-04** | Atribuição Automática | Seleciona e fixa o barbeiro na confirmação quando o cliente optou por "Qualquer profissional". |
| **RN-AGE-05** | Prevenção Atômica de Double-Booking | Executa validação sob transação ACID para evitar reservas duplicadas simultâneas. |
| **RN-ATE-01** | Ciclo de Vida do Atendimento | Inicializa a reserva no estado `'Confirmado'`. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas do Formulário (`/checkout/confirmar`):
* `servicoId` *(Long, Obrigatório)*: ID do serviço.
* `profissionalId` *(Long, Opcional)*: ID do barbeiro ou zero para escolha automática.
* `dataHora` *(String ISO-8601, Obrigatório)*: Data e horário inicial (ex: `2026-06-15T14:20:00`).
* `nome` *(String, Obrigatório)*: Primeiro nome do cliente.
* `sobrenome` *(String, Obrigatório)*: Sobrenome do cliente.
* `email` *(String, Opcional)*: E-mail para envio de notificações.
* `telefone` *(String, Obrigatório)*: Celular/WhatsApp de contato.
* `pagamento` *(String, Opcional)*: Forma de acerto no salão.

### Saídas:
* Registro persistido de `Agendamento`.
* Página de comprovante (`site/cart/order-confirmation.html`).
