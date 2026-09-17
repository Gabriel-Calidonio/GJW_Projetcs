# 📜 Regras de Negócio: Loja Virtual e Controle de Estoque

Este documento define as regras aplicáveis ao módulo de compras e-commerce, precificação de cosméticos, formação de kits e controle de estoque de produtos.

---

## 🎯 Lista de Regras de Negócio (RN-EST)

### **RN-EST-01: Validação e Trava de Estoque Disponível**
* **Contexto:** Adição de itens ao carrinho e fechamento de compras.
* **Regra:**
  1. Nenhum cliente pode adicionar ao carrinho uma quantidade superior ao saldo atual em `tab_produto.estoque`.
  2. No momento da submissão do checkout (`/carrinho/checkout/confirmar`), o sistema deve revalidar o saldo com trava atômica. Se algum item do carrinho estiver com estoque inferior à quantidade comprada, a transação inteira é abortada com Rollback.

---

### **RN-EST-02: Estrutura de Kits Promocionais**
* **Contexto:** Venda de conjuntos promocionais de produtos (ex: Kit Cabelo + Barba).
* **Regra:** Os kits são cadastrados como produtos no catálogo (`tab_produto`) com precificação promocional própria (inferior à soma dos produtos avulsos). O estoque do kit reflete a disponibilidade do lote promocional montado.

---

### **RN-EST-03: Identificação de Comprador (Cliente vs Visitante)**
* **Contexto:** Processamento do formulário de checkout.
* **Regra:**
  * Se houver um usuário autenticado na sessão (`session.getAttribute("usuarioLogado") != null`), o pedido em `tab_pedidos` deve ser obrigatoriamente associado ao ID do cliente (`tab_pedidos.cliente_id`).
  * Se o comprador não estiver autenticado (checkout como visitante), os campos `nome_visitante` e `telefone_visitante` tornam-se de preenchimento obrigatório no pedido, e `cliente_id` permanece nulo.
* **Impacto no Código:** [`CarrinhoController.java:L172-L185`](/src/main/java/com/gwj/controller/CarrinhoController.java#L172-L185).

---

### **RN-EST-04: Atomicidade e Limpeza de Sessão**
* **Contexto:** Conclusão bem-sucedida do pedido.
* **Regra:** A inserção do registro mestre em `tab_pedidos`, dos itens em `tab_itens_pedido` e o decremento em `tab_produto.estoque` devem ocorrer dentro da mesma transação JDBC. Somente após o commit da transação, o carrinho da sessão (`carrinho.limpar()`) deve ser esvaziado.

---

### **RN-EST-05: Precificação Mandatória e Desconto Mínimo de Kits Promocionais**
* **Contexto:** Cadastro e precificação de kits promocionais no catálogo.
* **Regra:** O preço final do kit deve ser estritamente inferior à soma dos preços regulares dos cosméticos avulsos que o compõem, com desconto mínimo garantido de **10%**. Se o valor for igual ou superior à soma, o cadastro é bloqueado.
* **Especificação Detalhada:** [`RN-EST-005_precificacao_kits_promocionais.md`](/docs/requirements/business_rules/RN-EST-005_precificacao_kits_promocionais.md).
* **Impacto no Código:** [`AdminProdutoController.java`](/src/main/java/com/gwj/controller/AdminProdutoController.java), [`Produto.java`](/src/main/java/com/gwj/model/domain/entities/Produto.java).

