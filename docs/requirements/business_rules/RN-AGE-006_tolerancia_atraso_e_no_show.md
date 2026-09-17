# RN-AGE-006: Tolerância de Atraso, Caracterização de No-Show e Liberação para Encaixe

## 📋 Metadados

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `RN-AGE-006` |
| **Módulo** | Agendamento & Atendimento Presencial |
| **Status** | 🟢 Ativo |
| **Versão** | 1.0.0 |
| **Criticidade** | Alta |
| **Autor** | Equipe de Engenharia de Requisitos GWJ |
| **Data de Criação** | 17/09/2026 |
| **Última Atualização** | 17/09/2026 |

---

## 1. 🎯 Descrição Comercial (O Quê e Por Quê)

O atraso de clientes sem aviso prévio compromete a grade de atendimentos dos barbeiros, causando efeito cascata em todos os agendamentos seguintes do dia ou deixando a cadeira ociosa com prejuízo financeiro. Esta regra estabelece a tolerância máxima permitida para comparecimento ao salão, define o momento em que o compromisso é classificado formalmente como **No-Show (Falta)** e autoriza a recepção a liberar o horário vago para atendimento presencial por **encaixe de balcão**.

---

## 2. 📜 Enunciado Formal e Especificações

### 2.1 Limite de Tolerância:
O tempo limite de tolerância para check-in do cliente presencialmente no salão é fixado em **10 minutos** a contar do horário previsto para início do agendamento:

$$\text{Limite de Tolerância} = \text{tab-agendamento.hora-inicio} + 10\text{ minutos}$$

### 2.2 Caracterização de No-Show e Liberação de Encaixe:
1. **Até o Limite de Tolerância:** O profissional permanece reservado e bloqueado exclusivamente para o cliente agendado.
2. **Decorrido o Limite de Tolerância ($t > 10\text{ min}$) sem Check-in:**
   - O agendamento tem seu status alterado para `'No-Show'`.
   - O profissional responsável fica automaticamente liberado para atender clientes da fila de espera presencial (encaixe rápido).
   - Caso o cliente faltoso compareça após a tolerância, o atendimento não é garantido e fica condicionado à existência de nova janela livre na grade.

---

## 3. ⚖️ Escopo e Condições de Borda

* **Aplica-se a:** Todos os agendamentos confirmados originados pelo site ou pela recepção.
* **Não se aplica a:** Clientes que entraram em contato prévio com a barbearia informando imprevisto e cuja prorrogação de tolerância foi aprovada pelo barbeiro.
* **Condição de Contorno:** Se o atraso for gerado por culpa exclusiva da barbearia (atendimento anterior atrasou), a tolerância não penaliza o cliente e seu horário é integralmente honrado.

---

## 4. 🧪 Cenários de Teste / BDD (Gherkin)

### Cenário 1: Cliente comparece dentro do prazo de tolerância
  Dado que o cliente possui agendamento confirmado para as 14:00
  Quando o cliente realiza o check-in presencial às 14:08 (8 min de atraso)
  Então o sistema deve autorizar o início do atendimento
  E o status do agendamento deve transitar para 'Em Atendimento'

### Cenário 2: Cliente excede a tolerância de 10 minutos (Caracterização de No-Show)
  Dado que o cliente possui agendamento confirmado para as 14:00
  Quando o relógio atinge 14:11 sem que o check-in tenha sido realizado
  Então o sistema deve classificar o atendimento como 'No-Show'
  E o profissional deve ficar disponível para receber um cliente por encaixe

---

## 5. ⚙️ Notas Técnicas e Impacto na Arquitetura

* **Rotina Automática:** Um job agendado ou verificação sob demanda em `AdminAgendamentoController` pode sinalizar visualmente com alerta amarelo (em atraso) e vermelho (no-show).
* **Parâmetro Configurável:** O tempo de tolerância (10 min) é lido preferencialmente da tabela `tab_setting` (`tempo_tolerancia_atraso`), permitindo ajuste flexível pelo Administrador via `SettingService`.
* **Histórico do Cliente:** No-shows recorrentes (ex: 3 faltas em 60 dias) devem ser computados na ficha do cliente para orientar políticas de pagamento antecipado.

---

## 6. 🔗 Matriz de Rastreabilidade

* **Requisito Funcional (RF):** [`RF003_agendamento_online_autonomo.md`](/docs/requirements/functional/RF003_agendamento_online_autonomo.md)
* **Casos de Uso Afetados:** [`UC_ADM_005_gerenciar_agendamentos.md`](/docs/business/use-cases/dashboard/UC_ADM_005_gerenciar_agendamentos.md), [`UC_STR_007_historico_meus_agendamentos.md`](/docs/business/use-cases/storefront/UC_STR_007_historico_meus_agendamentos.md)
* **Classes de Implementação:** [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java), [`AdminAgendamentoController.java`](/src/main/java/com/gwj/controller/AdminAgendamentoController.java)
* **Tabelas do Banco de Dados:** `tab_agendamento`, `tab_setting`
