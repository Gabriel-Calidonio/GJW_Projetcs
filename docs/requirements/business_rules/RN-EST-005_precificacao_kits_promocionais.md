# RN-EST-005: Precificação Mandatória e Desconto Mínimo de Kits Promocionais

## 📋 Metadados

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `RN-EST-005` |
| **Módulo** | Loja & Estoque de Cosméticos |
| **Status** | 🟢 Ativo |
| **Versão** | 1.0.0 |
| **Criticidade** | Média-Alta |
| **Autor** | Equipe de Engenharia de Requisitos GWJ |
| **Data de Criação** | 17/09/2026 |
| **Última Atualização** | 17/09/2026 |

---

## 1. 🎯 Descrição Comercial (O Quê e Por Quê)

Os **Kits Promocionais** são ferramentas estratégicas para elevar o ticket médio de vendas de cosméticos na barbearia, incentivando o cliente a adquirir múltiplos produtos complementares (ex: Pomada Modeladora + Óleo para Barba + Pente de Madeira). Para garantir conformidade com o Código de Defesa do Consumidor e preservar o apelo promocional real, esta regra determina que o preço de venda do kit deve ser obrigatoriamente inferior à soma dos preços avulsos de seus itens, com uma margem de desconto mínima de **10%**, impedindo configurações comerciais errôneas no painel administrativo.

---

## 2. 📜 Enunciado Formal e Fórmulas de Precificação

### 2.1 Regra de Vantagem Financeira Mandatória:
O preço final cadastrado para qualquer Kit Promocional ($\text{Preço}_{\text{kit}}$) deve ser estritamente inferior à somatória dos preços regulares de venda de todos os seus itens avulsos:

$$\text{Preço}_{\text{kit}} < \sum_{i=1}^{n} \text{Preço Avulso}_i$$

### 2.2 Margem de Desconto Mínima Parametrizada (10%):
A economia percentual concedida ao cliente na compra do kit não pode ser inferior a **10%**:

$$\text{Desconto do Kit} = \left( 1 - \frac{\text{Preço}_{\text{kit}}}{\sum_{i=1}^{n} \text{Preço Avulso}_i} \right) \times 100 \ge 10\%$$

### 2.3 Bloqueio de Validação no Cadastro:
Ao salvar um kit em `/MRYnZpAsC9sp/produtos/salvar`, o sistema deve calcular a soma dos itens componentes:
* Se $\text{Preço}_{\text{kit}} \ge \sum \text{Preço Avulso}$, o formulário deve ser rejeitado com mensagem de erro impeditiva.
* O sistema deve exibir na tela a economia em reais (R$) e a porcentagem de desconto que será destacada no selo promocional da vitrine (`/loja`).

---

## 3. ⚖️ Escopo e Condições de Borda

* **Aplica-se a:** Todos os produtos classificados como kits compostos na tabela `tab_produto`.
* **Não se aplica a:** Produtos avulsos individuais ou itens promocionais de liquidação de item único.
* **Condição de Contorno:** Caso o preço de tabela de um dos cosméticos avulsos seja reajustado para baixo, o sistema deve emitir um alerta ao Administrador caso a margem do kit caia abaixo de 10%.

---

## 4. 🧪 Cenários de Teste / BDD (Gherkin)

### Cenário 1: Cadastro de kit com desconto promocional válido
  Dado que os produtos avulsos são "Pomada Matte" (R$ 50,00) e "Óleo para Barba" (R$ 40,00)
  E a soma dos preços individuais é R$ 90,00
  Quando o administrador cadastra o "Kit Barba & Cabelo" pelo preço de R$ 75,00 (16,6% de desconto)
  Então o sistema deve autorizar o cadastro do kit
  E deve exibir na vitrine o selo "Economize R$ 15,00 (16% OFF)"

### Cenário 2: Bloqueio de kit sem vantagem de preço (Preço igual ou superior)
  Dado que os produtos avulsos somam R$ 90,00
  Quando o administrador tenta cadastrar o kit pelo preço de R$ 90,00 ou R$ 95,00
  Então o sistema deve abortar a persistência
  E deve apresentar a mensagem: "O valor do kit promocional deve oferecer desconto real sobre a soma dos produtos avulsos (mínimo de 10%)."

---

## 5. ⚙️ Notas Técnicas e Impacto na Arquitetura

* **Camada de Validação:** Executada no `AdminProdutoController` ou serviço de validação antes da chamada ao `ProdutoService.create/update`.
* **Exibição na Loja:** A view `site/catalog/loja.html` e `site/catalog/single-product.html` utilizam a diferença entre o preço nominal e a soma avulsa para renderizar badges de desconto dinâmicos (`strike-through` do preço cheio e destaque do preço do kit).

---

## 6. 🔗 Matriz de Rastreabilidade

* **Requisito Funcional (RF):** [`RF001_kits.md`](/docs/requirements/functional/RF001_kits.md), [`RF006_ecommerce_e_controle_estoque.md`](/docs/requirements/functional/RF006_ecommerce_e_controle_estoque.md)
* **Casos de Uso Afetados:** [`UC_ADM_009_gerenciar_produtos_estoque.md`](/docs/business/use-cases/dashboard/UC_ADM_009_gerenciar_produtos_estoque.md), [`UC_STR_001_catalogo_produtos_e_detalhes.md`](/docs/business/use-cases/storefront/UC_STR_001_catalogo_produtos_e_detalhes.md)
* **Classes de Implementação:** [`Produto.java`](/src/main/java/com/gwj/model/domain/entities/Produto.java), [`AdminProdutoController.java`](/src/main/java/com/gwj/controller/AdminProdutoController.java)
* **Tabelas do Banco de Dados:** `tab_produto`
