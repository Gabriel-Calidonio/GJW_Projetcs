# UC_STR_007 - Consultar Histórico de Agendamentos e Pedidos do Cliente

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_STR_007` (ref. `UC21` em [`storefront.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/storefront.puml)) |
| **Nome** | Consultar Meus Agendamentos e Histórico de Atendimentos |
| **Módulo** | Área e Conta do Cliente |
| **Atores Primários** | Cliente Registrado (*Perfil 4 - Autenticado*) |
| **Atores Secundários** | Serviço de Agendamentos (`AgendamentoService`), Serviço de Clientes (`ClienteService`) |
| **Tipo** | Essencial / Concreto |
| **Frequência de Uso** | Alta (para conferir horários marcados, status de atendimentos e históricos) |
| **Rastreabilidade** | [`Router.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/Router.java), [`Agendamento.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/model/domain/entities/Agendamento.java), [`RN-CAN-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_cancelamento_reagendamento.md#rn-can-01-prazo-limite-de-cancelamento-online-anteced%C3%AAncia-m%C3%ADnima), [`RN-ATE-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_atendimento_comandas.md#rn-ate-01-ciclo-de-vida-do-atendimento-e-transi%C3%A7%C3%A3o-de-status) |

---

## 1. 🎯 Descrição Sumária

Permite que o cliente autenticado visualize em uma área exclusiva (`/meus-agendamentos`) o histórico completo de seus atendimentos passados e futuros na **Tgo's Barbearia**. O sistema recupera a ficha do cliente logado e consulta todos os agendamentos registrados sob o seu número de telefone de contato, organizando-os cronologicamente dos mais recentes para os mais antigos. O cliente pode conferir o serviço agendado, o barbeiro designado, o dia, o horário e o status atual da reserva (`Confirmado`, `Em Atendimento`, `Concluído` ou `Cancelado`).

---

## 2. ⚡ Pré-Condições

1. O cliente deve estar devidamente autenticado com sessão ativa (`session.getAttribute("usuarioLogado") != null`).
2. O perfil do usuário logado deve ser de Cliente (`perfil_id = 4`).

---

## 3. ✅ Pós-Condições

1. A página `site/booking/meus-agendamentos.html` é renderizada exibindo a listagem histórica de reservas do cliente.
2. O cliente acompanha a situação de seus compromissos sem precisar acessar o painel administrativo da empresa.

---

## 4. 🚀 Gatilho (Trigger)

* O cliente autenticado clica em "Meus Agendamentos" ou "Meus Pedidos" no menu de usuário logado no site.

---

## 5. 🔄 Fluxo Principal (Consultar Histórico)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Cliente | Clica na opção "Meus Agendamentos" ou navega até `/meus-agendamentos`. |
| **2** | Sistema | [`Router.meusAgendamentos`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/Router.java) verifica se o usuário está autenticado na `HttpSession`. |
| **3** | Sistema | Busca a entidade `Cliente` completa vinculada ao `usuarioLogado.getId()`. |
| **4** | Sistema | Consulta `AgendamentoService` filtrando pelo telefone do cliente (`aFiltro.setClienteTelefone(cliente.getTelefone())`). |
| **5** | Sistema | Ordena os agendamentos por data e horário de início decrescente (do mais recente para o mais antigo). |
| **6** | Sistema | Injeta `clienteLogado` e a lista `agendamentos` no modelo. |
| **7** | Sistema | Renderiza a view Thymeleaf `site/booking/meus-agendamentos.html`. |
| **8** | Cliente | Visualiza seus agendamentos com data, hora, serviço contratado, profissional responsável e status. |

---

## 6. 🔀 Fluxos Alternativos

### **FA01 - Acesso pelo Endpoint Sinônimo `/pedidos`**
* A rota `/pedidos` direciona para a mesma área pessoal consolidada, permitindo que clientes verifiquem tanto serviços agendados quanto solicitações de retirada.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Usuário Não Autenticado**
* **No Passo 2:** O usuário tenta acessar `/meus-agendamentos` sem estar logado no sistema (`session == null` ou `usuarioLogado == null`).
* **Ação do Sistema:** O sistema redireciona o visitante diretamente para a tela de login (`redirect:/login`).

### **FE02 - Nenhum Agendamento Localizado**
* Caso o cliente seja novo e ainda não possua atendimentos associados ao seu telefone, a interface exibe um estado vazio amigável (*empty state*) com botão convidando o usuário a realizar seu primeiro agendamento (`/servicos`).

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-CAN-01** | Prazo Limite de Cancelamento Online | Orienta o cliente quanto à antecedência mínima de 2 horas caso deseje solicitar cancelamento ou remarcação. |
| **RN-ATE-01** | Transição de Status | Exibe em tempo real o ciclo de vida do atendimento (`Confirmado` -> `Em Atendimento` -> `Concluído`). |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas:
* Requisição HTTP GET para `/meus-agendamentos` ou `/pedidos`.
* Sessão HTTP contendo `usuarioLogado`.

### Saídas:
* Página `site/booking/meus-agendamentos.html` contendo a lista ordenada de agendamentos e dados do perfil.
