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

## 6. 🔀 Casos de Uso Complementares e Extensões do Carrinho

### **UC05 - Visualizar Carrinho de Compras (`/carrinho`)**
* **Objetivo:** Exibir ao usuário o extrato analítico dos itens adicionados, subtotais individuais, valor total consolidado e opções de navegação para continuidade das compras ou avanço ao checkout.
* **Fluxo Operacional:**
  1. O usuário clica no ícone de sacola/carrinho no cabeçalho ou navega diretamente para `/carrinho` (ou `/cart`).
  2. O controlador [`CarrinhoController.carrinho`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/CarrinhoController.java) recupera a instância de `Carrinho` da `HttpSession`.
  3. Injeta o objeto `carrinho` no modelo Thymeleaf.
  4. Renderiza a view `site/cart/carrinho.html`. Se o carrinho estiver vazio, exibe o estado vazio (*empty state*) com botão convidando o cliente a explorar a `/loja`.
* **Saída:** Tabela detalhada de itens contendo: foto miniatura, nome do produto/kit, valor unitário, seletor de quantidade `[-] Qtd [+]`, subtotal em reais e botão de remoção.

### **UC06 - Alterar Quantidade do Item (`POST /carrinho/atualizar`) (`<<extend>>`)**
* **Objetivo:** Ajustar o quantitativo desejado de uma unidade específica sem necessidade de recarregar a página web.
* **Fluxo Operacional:**
  1. Na visualização do carrinho, o usuário interage com os botões de incremento/decremento ou altera o valor numérico.
  2. O script cliente JavaScript dispara requisição assíncrona (Fetch/AJAX):
     * **Método:** `POST`
     * **Endpoint:** `/carrinho/atualizar`
     * **Payload (Form Data):** `produtoId={id}&quantidade={novaQtd}`
  3. O controlador valida se `novaQtd > 0` e se `novaQtd <= produto.getEstoque()` (`RN-EST-01`).
  4. Executa `carrinho.atualizarQuantidade(produtoId, quantidade)`.
  5. Retorna resposta JSON: `{"sucesso": true, "quantidadeTotal": N, "valorTotal": "R$ X,XX", "subtotalItem": "R$ Y,YY"}`.
  6. A interface atualiza os valores na linha da tabela e no totalizador geral.

### **UC07 - Remover Item do Carrinho (`POST /carrinho/remover`) (`<<extend>>`)**
* **Objetivo:** Retirar uma linha de produto específica do carrinho da sessão.
* **Fluxo Operacional:**
  1. O usuário clica no ícone de exclusão / lixeira referente ao item indesejado.
  2. A interface dispara requisição AJAX:
     * **Método:** `POST`
     * **Endpoint:** `/carrinho/remover`
     * **Parâmetro:** `produtoId={id}`
  3. O controlador executa `carrinho.removerItem(produtoId)`.
  4. Retorna resposta JSON: `{"sucesso": true, "quantidadeTotal": N, "valorTotal": "R$ X,XX"}`.
  5. O JavaScript remove a linha do produto do DOM com animação suave e recalcula o contador do mini-cart.

### **UC08 - Esvaziar Carrinho (`POST /carrinho/limpar`) (`<<extend>>`)**
* **Objetivo:** Descartar todos os itens da sacola de compras de uma só vez.
* **Fluxo Operacional:**
  1. O usuário clica na ação "Limpar Carrinho".
  2. Dispara requisição `POST /carrinho/limpar`.
  3. O controlador executa `carrinho.limpar()`, esvaziando a coleção de itens na sessão HTTP.
  4. Retorna resposta JSON: `{"sucesso": true, "quantidadeTotal": 0, "valorTotal": "R$ 0,00"}`.
  5. A interface transiciona para o estado vazio convidativo.

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
