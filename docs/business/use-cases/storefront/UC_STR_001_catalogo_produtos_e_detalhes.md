# UC_STR_001 - Navegar no Catálogo de Produtos e Visualizar Detalhes

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_STR_001` (engloba `UC01` e `UC02` em [`storefront.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/storefront.puml)) |
| **Nome** | Navegar no Catálogo de Produtos/Kits e Visualizar Detalhes |
| **Módulo** | Vitrine de Cosméticos & Kits Promocionais |
| **Atores Primários** | Visitante (*Público Geral*), Cliente Registrado (*Perfil 4*) |
| **Atores Secundários** | Sistema de Catálogo (`Router`, `ProdutoService`) |
| **Tipo** | Essencial / Concreto |
| **Frequência de Uso** | Muito Alta (porta de entrada para compras na loja virtual da barbearia) |
| **Rastreabilidade** | [`Router.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/Router.java), [`Produto.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/model/domain/entities/Produto.java), [`RN-EST-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_loja_estoque.md#rn-est-01-valida%C3%A7%C3%A3o-e-trava-de-estoque-dispon%C3%ADvel), [`RN-EST-02`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_loja_estoque.md#rn-est-02-estrutura-de-kits-promocionais) |

---

## 1. 🎯 Descrição Sumária

Permite que qualquer visitante ou cliente autenticado explore o catálogo de cosméticos masculinos, pomadas, ceras modeladoras, óleos de barba e kits promocionais da **Tgo's Barbearia** através da página `/loja`. O usuário pode visualizar a listagem completa com fotos, títulos e preços, além de acessar a página individual do produto (`/single-product?id={id}`) para obter descrições completas, orientações de uso, disponibilidade de estoque e botão para adicionar o item ao carrinho de compras.

---

## 2. ⚡ Pré-Condições

1. A aplicação web deve estar em execução e com o banco de dados acessível.
2. Devem existir registros de produtos ou kits ativos cadastrados na tabela `tab_produto`.

---

## 3. ✅ Pós-Condições

1. A vitrine `/loja` é apresentada ao usuário com todos os cosméticos e kits comercializáveis.
2. Na seleção de um item, a página `/single-product` é carregada com as informações detalhadas e dados de precificação.

---

## 4. 🚀 Gatilho (Trigger)

* O usuário clica no item "Loja" no menu de navegação superior do site; OU
* O usuário clica em um produto em destaque na página inicial (`/home` ou `/`).

---

## 5. 🔄 Fluxo Principal (Navegação na Loja e Acesso a Detalhes)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Usuário | Acessa a URL pública `/loja`. |
| **2** | Sistema | O método `shop` de [`Router.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/Router.java) consulta o serviço `ProdutoService` via `ServiceRegistry`. |
| **3** | Sistema | Recupera a lista de produtos e injeta no modelo sob o atributo `produtos`. |
| **4** | Sistema | Renderiza a view Thymeleaf `site/catalog/loja.html`. |
| **5** | Usuário | Visualiza a grade de produtos com foto, nome, preço e selo indicativo de kit (quando aplicável). |
| **6** | Usuário | Clica sobre a foto ou nome de um produto específico para conhecer seus detalhes. |
| **7** | Sistema | Dispara requisição GET para `/single-product?id={id}`. |
| **8** | Sistema | O método `singleProduct` de [`Router.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/Router.java) busca o produto pelo ID. |
| **9** | Sistema | Renderiza a view `site/catalog/single-product.html` exibindo descrição técnica, preço unitário e seletor de quantidade para o carrinho. |

---

## 6. 🔀 Fluxos Alternativos

### **FA01 - Navegação em Kits Promocionais (RN-EST-02)**
* **No Passo 5:** O usuário seleciona um conjunto promocional (ex: *Kit Cabelo & Barba Premium*).
* **Ação do Sistema:** O sistema exibe o kit como produto agregado no catálogo com preço promocional unificado, permitindo a compra em lote com um único clique.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Produto Inexistente ou ID Inválido**
* **No Passo 8:** O parâmetro `id` na URL não corresponde a nenhum produto cadastrado.
* **Ação do Sistema:** O sistema renderiza a tela `site/catalog/single-product` sem lançar erro fatal, permitindo ao usuário retornar à listagem através do menu superior.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-EST-01** | Trava de Estoque | O catálogo apresenta apenas produtos com saldo ou indica itens temporariamente esgotados. |
| **RN-EST-02** | Estrutura de Kits Promocionais | Kits são comercializados sob precificação promocional própria e saldo individualizado de lotes. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas:
* Requisição HTTP GET `/loja`.
* Parâmetro de URL `id` *(Long)* em `/single-product`.

### Saídas:
* Página de catálogo (`site/catalog/loja.html`) com coleção de `Produto`.
* Página de detalhes (`site/catalog/single-product.html`) com o objeto individual do produto selecionado.
