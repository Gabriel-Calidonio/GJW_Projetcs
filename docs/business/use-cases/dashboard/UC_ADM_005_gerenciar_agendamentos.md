# UC_ADM_005 - Gerenciar Agendamentos da Barbearia

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_ADM_005` (engloba `UC11`, `UC12` e `UC13` em [`general_dashboard.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/general_dashboard.puml)) |
| **Nome** | Gerenciar Agendamentos, Filtragem de Grade e Transição de Status |
| **Módulo** | Módulos de Gestão Operacional |
| **Atores Primários** | Administrador Geral (*Perfil 1*), Recepcionista (*Perfil 3*), Barbeiro (*Perfil 2 - agenda individual*) |
| **Atores Secundários** | Motor de Disponibilidade (`AgendamentoService`), Grade de Horários (`GradeHorarios`) |
| **Tipo** | Essencial / Concreto (com Extensões Operacionais) |
| **Frequência de Uso** | Muito Alta (uso constante ao longo do expediente da barbearia) |
| **Rastreabilidade** | [`AdminAgendamentoController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminAgendamentoController.java), [`AgendamentoService.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/service/AgendamentoService.java), [`RN-AGE-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_agendamento.md#rn-age-01-ocupa%C3%A7%C3%A3o-de-m%C3%BAltiplos-blocos-consecutivos) a [`RN-AGE-05`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_agendamento.md#rn-age-05-preven%C3%A7%C3%A3o-at%C3%B4mica-de-double-booking), [`RN-ATE-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_atendimento_comandas.md#rn-ate-01-ciclo-de-vida-do-atendimento-e-transi%C3%A7%C3%A3o-de-status), [`RN-CAN-02`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_cancelamento_reagendamento.md#rn-can-02-libera%C3%A7%C3%A3o-imediata-de-slots-na-grade-p%C3%BAblica) |

---

## 1. 🎯 Descrição Sumária

Permite o controle completo da agenda do estabelecimento no painel administrativo (`/MRYnZpAsC9sp/agendamentos`). O operador pode consultar a lista cronológica decrescente de atendimentos, cadastrar novos agendamentos presenciais ou telefônicos feitos na recepção, editar agendamentos existentes, aplicar filtros por data/barbeiro e gerenciar o ciclo de vida da reserva (`Confirmado` -> `Em Atendimento` -> `Concluído` ou `Cancelado`).

---

## 2. ⚡ Pré-Condições

1. Operador autenticado com sessão válida (`UC_ADM_001`).
2. Usuário com permissão `GERENCIAR_TODAS_AGENDAS`, `AGENDAR_HORARIO` ou `VISUALIZAR_PROPRIA_AGENDA` validada pelo `AdminInterceptor` (`UC_ADM_002`).
3. Serviços e Profissionais previamente cadastrados e ativos no sistema.

---

## 3. ✅ Pós-Condições

1. O agendamento é inserido, atualizado ou cancelado na base de dados (`tab_agendamento`).
2. No caso de cancelamento, os slots de 20 minutos correspondentes tornam-se imediatamente liberados para novos agendamentos na grade pública (`RN-CAN-02`).
3. Em caso de criação/atualização, o horário final (`hora_fim`) é calculado automaticamente caso não tenha sido explicitamente preenchido, respeitando a duração do serviço escolhido.

---

## 4. 🚀 Gatilhos (Triggers)

* O operador clica no menu "Agendamentos" no painel administrativo; OU
* A recepção recebe um cliente presencial ou por telefone solicitando agendamento/cancelamento.

---

## 5. 🔄 Fluxo Principal (Consultar e Listar Agendamentos)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Operador | Acessa `/MRYnZpAsC9sp/agendamentos`. |
| **2** | Sistema | [`AdminAgendamentoController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminAgendamentoController.java) invoca `AgendamentoService.read(new Agendamento())`. |
| **3** | Sistema | Ordena a coleção de agendamentos cronologicamente do mais recente para o mais antigo (por data e hora de início). |
| **4** | Sistema | Encaminha a lista para a view `admin/order/booking/listar`. |
| **5** | Operador | Visualiza tabela com ID, Cliente, Telefone, Serviço, Profissional, Data, Horário e Status. |

---

## 6. 🔀 Extensões e Ações Operacionais

### **Ação: Cadastrar Novo Agendamento pela Recepção (`/novo` e `/salvar`)**
1. O operador clica em "Novo Agendamento".
2. O sistema carrega dropdowns de profissionais ativos, serviços cadastrados e grade de horários (`populateDropdowns`).
3. O operador informa o nome do cliente, telefone, seleciona o serviço, profissional, data e hora de início.
4. Ao submeter (`POST /salvar`), se a `horaFim` não for enviada, o sistema obtém a duração do serviço (ex: 40 min) e calcula: `horaFim = horaInicio.plusMinutes(duracao)`.
5. O sistema persiste a entidade via `service.create(ag)` e redireciona para a listagem.

### **UC12 - Filtrar Agenda por Data e Profissional (`<<extend>>`)**
* **Objetivo:** Permitir ao operador (especialmente Recepcionista ou Barbeiro com permissão `VISUALIZAR_PROPRIA_AGENDA`) segmentar a visualização da grade diária por colaborador e intervalo de datas.
* **Fluxo Operacional:**
  1. Na tela de agendamentos (`/MRYnZpAsC9sp/agendamentos`), o operador seleciona um filtro de data no seletor de calendário e/ou escolhe um profissional no menu suspenso.
  2. Ao submeter a filtragem, a requisição envia os parâmetros `?data={data}&profissionalId={id}` via GET.
  3. O controlador [`AdminAgendamentoController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminAgendamentoController.java) instancia `Agendamento` como critério de filtro preenchendo `dataAgendamento` e `profissionalId`.
  4. Caso o usuário logado seja um Barbeiro (*Perfil 2*), o sistema força `profissionalId` igual ao seu próprio identificador (`RN-SEG-03`), impedindo que o profissional visualize atendimentos de outras cadeiras.
  5. A lista filtrada é retornada à view mantendo a ordenação cronológica das cadeiras.
* **Campos de Entrada:** `data` *(LocalDate, formato YYYY-MM-DD)*, `profissionalId` *(Long, Opcional)*.
* **Saída:** Tabela de agendamentos restrita ao escopo solicitado.

### **UC13 - Alterar Status do Agendamento / Cancelamento (`<<extend>>`)**
* **Objetivo:** Gerenciar a transição de estado da reserva durante o atendimento presencial ou processar cancelamentos solicitados pelo cliente.
* **Estados Possíveis (`RN-ATE-01`):** `Confirmado` ➔ `Em Atendimento` ➔ `Concluído` OU `Cancelado`.
* **Fluxo de Cancelamento Operacional (`/cancelar/{id}`):**
  1. O operador localiza o agendamento na listagem e aciona a opção "Cancelar Reserva".
  2. O sistema executa requisição GET para `/MRYnZpAsC9sp/agendamentos/cancelar/{id}`.
  3. O controlador carrega a reserva pelo ID, altera seu status para `"CANCELADO"` e executa `AgendamentoService.update(agendamento)`.
  4. O sistema libera imediatamente os slots correspondentes na grade pública da barbearia (`RN-CAN-02`), permitindo que novos clientes realizem reservas no mesmo horário.
  5. Adiciona flash message `"Agendamento cancelado com sucesso. Horário liberado na grade pública."` e redireciona para a listagem.
* **Fluxo de Exclusão Física (`/excluir/{id}`):**
  1. O operador clica em "Excluir". O sistema confirma a intenção e executa `service.delete(filtro)`. Operação recomendada apenas para registros gerados indevidamente em testes.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Conflito de Horário / Double-Booking (RN-AGE-05)**
* Ao tentar salvar um agendamento cujo horário colida com outro atendimento já confirmado do mesmo profissional, o mecanismo transacional detecta o conflito e impede a sobreposição de horários.

### **FE02 - Serviço Excede Horário de Encerramento (RN-AGE-02)**
* Se o horário de início somado à duração do serviço ultrapassar o encerramento do expediente configurado para o dia (`tab_dias_funcionamento`), o sistema rejeita a operação.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-AGE-01** | Ocupação de Múltiplos Blocos | A duração do serviço define quantos blocos de 20 minutos serão bloqueados. |
| **RN-AGE-02** | Horário de Encerramento | Bloqueia reservas cujo término ultrapasse o fim do expediente. |
| **RN-AGE-05** | Prevenção de Double-Booking | Garante atomicidade e impede que dois clientes ocupem a mesma cadeira simultaneamente. |
| **RN-CAN-02** | Liberação Imediata de Slots | Status `'CANCELADO'` restaura a disponibilidade dos blocos na grade. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas do Formulário de Cadastro/Edição:
* `clienteNome` *(String, Obrigatório)*: Nome do cliente atendido.
* `clienteTelefone` *(String, Opcional)*: Telefone/WhatsApp de contato.
* `dataAgendamento` *(LocalDate, Obrigatório)*: Data no formato `YYYY-MM-DD`.
* `horaInicio` *(LocalTime, Obrigatório)*: Horário de início do atendimento.
* `horaFim` *(LocalTime, Opcional)*: Calculada automaticamente com a duração do serviço se nula.
* `servicoId` *(Long, Obrigatório)*: Chave estrangeira do serviço escolhido.
* `profissionalId` *(Long, Obrigatório)*: Chave estrangeira do barbeiro responsável.
* `status` *(String, Padrão: "Confirmado")*: Situação da reserva.

### Saídas:
* Redirecionamento para `/MRYnZpAsC9sp/agendamentos` com listagem atualizada.
