# UC_STR_002 - Adicionar Produtos e Gerenciar Carrinho de Compras

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_STR_002` (engloba `UC03` a `UC08` em [`storefront.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/storefront.puml)) |
| **Nome** | Adicionar Produtos ao Carrinho e Gerenciar Itens na Sessão HTTP |
| **Módulo** | Carrinho de Compras (`HttpSession`) |
| **Atores Primários** | Visitante (*Público Geral*), Cliente Registrado (*Perfil 4*) |
| **Atores Secundários** | Gerenciador de Sessão HTTP (`HttpSession`), Controlador do Carrinho (`CarrinhoController`) |
| **Tipo** | Essencial / Concreto (com Extensões de Manipulação) |
| **Frequência de Uso** | Muito Alta (a cada iteração de compra na vitrine) |
| **Rastreabilidade** | [`CarrinhoController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/CarrinhoController.java), [`Carrinho.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/model/domain/Carrinho.java), [`RN-EST-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_loja_estoque.md#rn-est-01-valida%C3%A7%C3%A3o-e-trava-de-estoque-dispon%C3%ADvel) |

---

## 1. 🎯 Descrição Sumária

Permite que o usuário monte sua sacola de compras de cosméticos e kits promocionais. O carrinho de compras é mantido na sessão HTTP do cliente (`session.getAttribute("carrinho")`), permitindo adicionar produtos via requisições assíncronas AJAX sem recarregar a página. O caso de uso valida imediatamente o saldo físico em estoque (`RN-EST-01`), atualiza os contadores e totais no cabeçalho (*mini-cart*) e disponibiliza a página `/carrinho` para alterar quantidades, excluir itens individuais ou esvaziar todo o pedido antes do checkout.

---

## 2. ⚡ Pré-Condições

1. O produto desejado deve existir no catálogo e possuir saldo físico positivo (`tab_produto.estoque > 0`).
2. O usuário deve possuir uma sessão HTTP ativa no navegador.

---

## 3. ✅ Pós-Condições

1. A instância de `Carrinho` é atualizada na `HttpSession`.
2. A quantidade total de itens e o valor total acumulado em reais são recalculados dinamicamente.
3. A resposta JSON devolve a confirmação da operação para atualização reativa da interface.

---

## 4. 🚀 Gatilhos (Triggers)

* O usuário clica em "Adicionar ao Carrinho" em um card da loja ou na página `/single-product`; OU
* O usuário acessa a página do carrinho `/carrinho` e altera quantidades ou remove itens.

---

## 5. 🔄 Fluxo Principal (Adicionar Produto ao Carrinho com Validação de Estoque)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Usuário | Clica em "Adicionar ao Carrinho" em um produto ou kit. |
| **2** | Interface (JS) | Dispara requisição HTTP POST assíncrona (AJAX) para `/carrinho/adicionar` enviando `produtoId` e `quantidade`. |
| **3** | Sistema | [`CarrinhoController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/CarrinhoController.java) localiza o produto no banco via `ProdutoService`. |
| **4** | Sistema (*UC04*) | Valida o saldo em estoque (`RN-EST-01`): a quantidade requisitada somada à já existente no carrinho não pode exceder `produto.getEstoque()`. |
| **5** | Sistema | Obtém o carrinho da sessão (`getCarrinhoFromSession`) e adiciona o item (`carrinho.adicionarItem(produto, quantidade)`). |
| **6** | Sistema | Devolve payload JSON contendo: `sucesso = true`, `quantidadeTotal` e `valorTotal`. |
| **7** | Interface (JS) | Exibe notificação visual de sucesso (*toast*) e atualiza o contador do mini-cart no cabeçalho. |

---

## 6. 🔀 Extensões Operacionais do Carrinho

### **UC05 - Visualizar Carrinho de Compras (`/carrinho`)**
* O usuário acessa a rota `/carrinho` (ou `/cart`). O sistema renderiza `site/cart/carrinho.html` exibindo a tabela completa com fotos, nomes, preços unitários, seletores numéricos de quantidade, subtotal por linha e valor total geral.

### **UC06 - Alterar Quantidade do Item (`POST /carrinho/atualizar`) (`<<extend>>`)**
1. Na tela do carrinho, o usuário clica nos botões `+` ou `-` ou digita nova quantidade.
2. Requisição AJAX submete `produtoId` e nova `quantidade`.
3. O sistema valida se a nova quantidade respeita o saldo em estoque (`RN-EST-01`).
4. Invoca `carrinho.atualizarQuantidade(produtoId, quantidade)` e devolve o novo valor total formatado.

### **UC07 - Remover Item do Carrinho (`POST /carrinho/remover`) (`<<extend>>`)**
1. O usuário clica no ícone de lixeira ao lado do item.
2. Requisição AJAX submete `/carrinho/remover?produtoId={id}`.
3. O controlador execute `carrinho.removerItem(produtoId)` e remove o produto da coleção da sessão.

### **UC08 - Esvaziar Carrinho (`POST /carrinho/limpar`) (`<<extend>>`)**
1. O usuário clica em "Limpar Carrinho".
2. O sistema executa `carrinho.limpar()`, zerando a lista de itens, o contador e o montante financeiro.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Saldo Insuficiente em Estoque (RN-EST-01)**
* **No Passo 4:** A quantidade pretendida é superior ao estoque físico disponível em `tab_produto.estoque`.
* **Ação do Sistema:** O sistema retorna JSON com `sucesso = false` e mensagem: *"Quantidade indisponível em estoque. Saldo disponível: X"*. O item não é adicionado além do limite.

### **FE02 - Produto Desativado ou Inexistente**
* Caso o produto tenha sido inativado pelo administrador durante a navegação do cliente, o sistema impede a inclusão e exibe alerta visual.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-EST-01** | Validação e Trava de Estoque Disponível | Nenhum cliente pode adicionar quantidade maior do que o saldo físico real do produto. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas (Rotas `/carrinho/*`):
* `produtoId` *(Long, Obrigatório)*: Identificador do produto no banco.
* `quantidade` *(Integer, Padrão: 1)*: Quantidade de unidades desejadas.

### Saídas (Payload JSON):
* `sucesso` *(Boolean)*: Confirmação da operação.
* `mensagem` *(String)*: Notificação ao usuário.
* `quantidadeTotal` *(Integer)*: Total de itens acumulados no carrinho.
* `valorTotal` *(Double/BigDecimal)*: Valor monetário consolidado da compra.
