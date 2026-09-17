# RN-FIN-001: Rateio e Apuração de Comissões de Serviços e Produtos

## 📋 Metadados

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `RN-FIN-001` |
| **Módulo** | Financeiro & Atendimento |
| **Status** | 🟢 Ativo |
| **Versão** | 1.0.0 |
| **Criticidade** | Alta |
| **Autor** | Equipe de Engenharia de Requisitos GWJ |
| **Data de Criação** | 17/09/2026 |
| **Última Atualização** | 17/09/2026 |

---

## 1. 🎯 Descrição Comercial (O Quê e Por Quê)

O modelo de parceria da barbearia remunera os barbeiros comissionados através de rateio percentual sobre a receita gerada em seus atendimentos. Esta regra disciplina a fórmula de cálculo da comissão de serviços prestados na cadeira e de produtos cosméticos vendidos pelo profissional no balcão, estipulando que somente atendimentos efetivamente concluídos e pagos dão direito ao rateio, blindando o caixa da barbearia contra comissionamentos indevidos em cancelamentos ou faltas.

---

## 2. 📜 Enunciado Formal e Fórmulas de Rateio

### 2.1 Percentuais Padrão de Comissionamento:
Salvo estipulação contratual individual em `tab_profissional.percentual_comissao`, os parâmetros de comissionamento padrão são:
* **Serviços Prestados:** **50%** sobre o valor líquido faturado.
* **Produtos Cosméticos Vendidos:** **10%** sobre o valor de tabela dos produtos recomendados e adicionados à comanda pelo barbeiro.

### 2.2 Fórmulas Matemáticas:

$$\text{Comissão Serviços} = \sum_{i=1}^{n} (\text{Valor Líquido do Serviço}_i) \times 0.50$$

$$\text{Comissão Produtos} = \sum_{j=1}^{m} (\text{Valor de Venda do Produto}_j) \times 0.10$$

$$\mathbf{\text{Total da Comissão}} = \text{Comissão Serviços} + \text{Comissão Produtos}$$

### 2.3 Condição Mandatória de Pagamento:
1. O comissionamento é gerado **exclusivamente** quando o agendamento atinge o status formal `'Concluído'` e o pagamento é liquidado no caixa.
2. Agendamentos com status `'Cancelado'` ou `'No-Show'` geram **R$ 0,00** de comissão.
3. Descontos autorizados concedidos na comanda reduzem a base de cálculo da comissão na mesma proporção.

---

## 3. ⚖️ Escopo e Condições de Borda

* **Aplica-se a:** Todos os atendimentos presenciais concluídos por barbeiros cadastrados em `tab_profissional`.
* **Não se aplica a:** Pedidos avulsos efetuados na loja virtual (`e-commerce`) entregues sem indicação direta de barbeiro (receita 100% institucional).
* **Condição de Contorno:** Se o barbeiro for contratado sob regime fixo (comissão zerada), o percentual cadastrado na ficha do profissional deve ser `0.00`.

---

## 4. 🧪 Cenários de Teste / BDD (Gherkin)

### Cenário 1: Fechamento de atendimento com corte e venda de pomada
  Dado que o barbeiro realizou um "Corte Degradê" no valor de R$ 50,00
  E adicionou uma "Pomada Modeladora Matte" no valor de R$ 40,00 à comanda do cliente
  Quando o atendimento for finalizado com status 'Concluído'
  Então a comissão de serviços deve ser de R$ 25,00 (50% de R$ 50,00)
  E a comissão de produtos deve ser de R$ 4,00 (10% de R$ 40,00)
  E a comissão total apurada para o profissional deve ser de R$ 29,00

### Cenário 2: Atendimento cancelado não deve gerar comissão
  Dado que havia um agendamento de "Barba Terapia" (R$ 45,00)
  Quando o agendamento for cancelado pelo cliente ou registrado como 'No-Show'
  Então o sistema não deve computar nenhuma comissão para o barbeiro (R$ 0,00)

---

## 5. ⚙️ Notas Técnicas e Impacto na Arquitetura

* **Arredondamento:** O cálculo financeiro deve utilizar a classe `java.math.BigDecimal` com precisão de 2 casas decimais e modo de arredondamento `RoundingMode.HALF_EVEN` (arredondamento bancário).
* **Persistência do Rateio:** Os valores de comissão apurados no momento do fechamento devem ser congelados no registro financeiro da comanda, garantindo que reajustes futuros nas tabelas de preços não alterem o histórico de comissões passadas.

---

## 6. 🔗 Matriz de Rastreabilidade

* **Requisito Funcional (RF):** [`RF003_agendamento_online_autonomo.md`](/docs/requirements/functional/RF003_agendamento_online_autonomo.md)
* **Casos de Uso Afetados:** [`UC_ADM_005_gerenciar_agendamentos.md`](/docs/business/use-cases/dashboard/UC_ADM_005_gerenciar_agendamentos.md)
* **Classes de Implementação:** [`Agendamento.java`](/src/main/java/com/gwj/model/domain/entities/Agendamento.java), [`Profissional.java`](/src/main/java/com/gwj/model/domain/entities/Profissional.java)
* **Tabelas do Banco de Dados:** `tab_agendamento`, `tab_profissional`, `tab_servico`, `tab_produto`
