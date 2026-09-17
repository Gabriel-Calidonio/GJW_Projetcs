# RN-NOT-001: Política e Janelas de Disparo de Lembretes Automáticos de Agendamento

## 📋 Metadados

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `RN-NOT-001` |
| **Módulo** | Mensageria & Notificações em Segundo Plano |
| **Status** | 🟢 Ativo |
| **Versão** | 1.0.0 |
| **Criticidade** | Média-Alta |
| **Autor** | Equipe de Engenharia de Requisitos GWJ |
| **Data de Criação** | 17/09/2026 |
| **Última Atualização** | 17/09/2026 |

---

## 1. 🎯 Descrição Comercial (O Quê e Por Quê)

O esquecimento de horários por parte dos clientes é o principal catalisador de faltas (*No-Show*), gerando ociosidade nos postos de trabalho e perda financeira para os barbeiros. Esta regra disciplina a política de mensageria automática em segundo plano, determinando as janelas de antecedência para disparo de mensagens de lembrete (via WhatsApp, SMS ou E-mail), garantindo a idempotência de disparos e o cancelamento imediato de notificações caso o cliente cancele a reserva previamente.

---

## 2. 📜 Enunciado Formal e Janelas Temporais de Disparo

### 2.1 Janelas de Antecedência Parametrizadas:
Para todo agendamento com status `'Confirmado'`, o sistema deve agendar e disparar até **dois lembretes automáticos**:

1. **Lembrete de Véspera (Confirmação Prévia):**
   - Disparado **24 horas antes** do horário de início do agendamento:
     $$T_{\text{disparo-1}} = (\text{DataHora do Agendamento}) - 24\text{ horas}$$
2. **Lembrete de Proximidade (Alerta Imediato):**
   - Disparado **2 horas antes** do horário de início do agendamento:
     $$T_{\text{disparo-2}} = (\text{DataHora do Agendamento}) - 2\text{ horas}$$

### 2.2 Regras de Supressão e Idempotência:
1. **Idempotência Estrita:** Cada tipo de lembrete (24h ou 2h) só pode ser despachado **uma única vez** por agendamento. O sistema deve registrar a data/hora do disparo com flag de envio para evitar mensagens duplicadas ao cliente.
2. **Agendamento de Última Hora:** Caso o cliente marque o horário com antecedência inferior à janela do lembrete (ex: agendou hoje às 14:00 para as 15:00, ou seja, menos de 2h de antecedência), o sistema deve suprimir o lembrete intermediário e disparar apenas a notificação de confirmação imediata.
3. **Aborto Compulsório por Cancelamento:** No instante em que o agendamento transitar para `'Cancelado'`, quaisquer lembretes pendentes na fila de execução devem ser cancelados imediatamente.

---

## 3. ⚖️ Escopo e Condições de Borda

* **Aplica-se a:** Todos os agendamentos confirmados que possuam telefone celular ou e-mail válido preenchido.
* **Não se aplica a:** Atendimentos presenciais de balcão iniciados na mesma hora (check-in imediato).
* **Condição de Contorno:** Horário noturno (entre 22:00 e 07:00). Se a janela de 24h cair na madrugada, o envio deve ser postergado para as 08:00 do dia do atendimento para evitar incômodo ao cliente.

---

## 4. 🧪 Cenários de Teste / BDD (Gherkin)

### Cenário 1: Disparo bem-sucedido dos dois lembretes
  Dado que um cliente agendou um corte para "Amanhã às 15:00"
  Quando o relógio do sistema atingir "Hoje às 15:00" (24h de antecedência)
  Então o sistema deve enviar o Lembrete de Véspera e registrar 'lembrete_24h_enviado = true'
  E quando o relógio atingir "Amanhã às 13:00" (2h de antecedência)
  Então o sistema deve enviar o Lembrete de Proximidade e registrar 'lembrete_2h_enviado = true'

### Cenário 2: Cancelamento do agendamento deve impedir disparos subsequentes
  Dado que o cliente possui agendamento para as 18:00 e já recebeu o lembrete de 24h
  Quando o cliente cancelar o agendamento às 14:00
  Então o status do agendamento passa para 'Cancelado'
  E o sistema NÃO deve disparar o lembrete das 16:00 (2h antes)

---

## 5. ⚙️ Notas Técnicas e Impacto na Arquitetura

* **Mecanismo de Execução:** Worker em background ou job agendado via Spring `@Scheduled` ou Quartz Scheduler inspecionando agendamentos com janela elegível.
* **Tolerância a Falhas:** Caso o gateway de envio (ex: API do WhatsApp/Twilio) retorne erro de rede (timeout), o sistema deve realizar até 3 tentativas (*retry com backoff exponencial*) antes de registrar falha no log de mensageria.

---

## 6. 🔗 Matriz de Rastreabilidade

* **Requisito Funcional (RF):** [`RF005_lembretes_automaticos.md`](/docs/requirements/functional/RF005_lembretes_automaticos.md)
* **Casos de Uso Afetados:** [`UC_STR_005_realizar_agendamento_e_comprovante.md`](/docs/business/use-cases/storefront/UC_STR_005_realizar_agendamento_e_comprovante.md)
* **Tabelas do Banco de Dados:** `tab_agendamento`
