# DP-2: Implementação do Mini-Cart e Carrinho de Produtos

- **Tipo:** Deployment plan
- **Status:** closed
- **Autor:** GWJ
- **Criado em:** 2026-06-16 18:50:53
- **Labels:** Nenhuma
- **Responsáveis:** Nenhum

## Descrição

# Implementação do Mini-Cart e Carrinho de Produtos

Implementar o mini-cart (carrinho rápido) de produtos para a loja da barbearia, armazenando a seleção em uma variável de sessão HTTP. Isso permitirá que tanto clientes logados quanto visitantes montem seu carrinho de produtos. Também integraremos o carrinho real na página de visualização detalhada do carrinho e criaremos uma página de confirmação de compra simplificada.

## User Review Required

> [!IMPORTANT]
> **Fluxo de Checkout de Produtos:**
> Como o sistema atual só possui checkout para agendamento de serviços (com data/hora e profissional), implementaremos uma rota de finalização de compra simplificada (`/carrinho/finalizar`) que limpa o carrinho e redireciona para uma tela de sucesso de compra de produtos (`/compra-confirmada`), indicando que os produtos estarão reservados para retirada e pagamento no balcão.

> [!TIP]
> **Interface Premium do Mini-Cart:**
> Adicionaremos um botão flutuante/dropdown na barra de navegação principal com um badge animado que indica a quantidade de itens. Ao adicionar um produto, o mini-cart abrirá como um painel lateral/dropdown interativo com micro-animações, permitindo que o usuário veja itens, subtotal e possa remover produtos sem precisar recarregar a página.

## Proposed Changes

### Backend

#### [NEW] [CarrinhoItem.java](/src/main/java/com/gwj/model/domain/CarrinhoItem.java)
Representa um item do carrinho, contendo o objeto `Produto` e a quantidade. Possui método para cálculo do subtotal do item.

#### [NEW] [Carrinho.java](/src/main/java/com/gwj/model/domain/Carrinho.java)
Gerencia a lista de itens, contendo métodos utilitários para adicionar, remover, atualizar quantidade, limpar carrinho, calcular a quantidade total de itens e obter o valor total.

#### [MODIFY] [GlobalAttributesAdvice.java](/src/main/java/com/gwj/controller/GlobalAttributesAdvice.java)
Adicionar um método `@ModelAttribute("carrinho")` que injeta o carrinho da sessão HTTP em todas as views do Thymeleaf, garantindo acesso instantâneo ao `${carrinho}` em qualquer página.

#### [NEW] [CarrinhoController.java](/src/main/java/com/gwj/controller/CarrinhoController.java)
Controlador REST para operações do carrinho por AJAX:
- `POST /carrinho/adicionar`: adiciona produto e quantidade ao carrinho.
- `POST /carrinho/atualizar`: atualiza a quantidade de um item.
- `POST /carrinho/remover`: remove um item do carrinho.
- `POST /carrinho/limpar`: limpa todo o carrinho.
- `POST /carrinho/finalizar`: finaliza o pedido de produtos limpando o carrinho de sessão.

#### [MODIFY] [Router.java](/src/main/java/com/gwj/controller/Router.java)
Adicionar rota `/compra-confirmada` para renderizar a página de agradecimento e retirada dos produtos no balcão.

---

### Frontend

#### [MODIFY] [header.html](/src/main/resources/templates/parts/header.html)
- Incluir folha de estilos do `bootstrap-icons` via CDN no `<head>` para termos ícones de carrinho modernos.
- Adicionar o botão do mini-cart com o badge indicador no header.
- Implementar o painel flutuante (dropdown/drawer) do mini-cart que exibe a lista atual de produtos, quantidade, total e botões de ação ("Ver Carrinho" e "Finalizar Compra").
- Inserir a lógica JavaScript para atualizar a interface do mini-cart via Fetch API de forma dinâmica e interativa (adicionar, remover, animações).

#### [MODIFY] [product-card.html](/src/main/resources/templates/parts/product-card.html)
- Adicionar um botão discreto de "Adicionar" rápido ao card do produto que adiciona 1 unidade do produto ao carrinho via Fetch/AJAX instantâneo.

#### [MODIFY] [single-product.html](/src/main/resources/templates/single-product.html)
- Modificar o botão "Adicionar ao Carrinho" para usar Fetch/AJAX com a quantidade selecionada no input, abrindo o mini-cart após o sucesso.

#### [MODIFY] [carrinho.html](/src/main/resources/templates/carrinho.html)
- Substituir a lista de serviços mockada pelo loop dinâmico dos produtos reais contidos no `${carrinho}`.
- Adicionar inputs de alteração de quantidade que atualizam via Fetch, modificando o subtotal da linha e o total do carrinho na hora.
- Alterar o botão de checkout para chamar `/carrinho/finalizar`.
- Tratar estado de carrinho vazio com layout elegante e botão de retornar à loja.

#### [NEW] [compra-confirmada.html](/src/main/resources/templates/compra-confirmada.html)
- Tela moderna de confirmação de compra de produtos com mensagem de sucesso, instruções de retirada na barbearia e botão de voltar para a loja.

#### [MODIFY] [style.css](/src/main/resources/static/css/style.css)
- Adicionar estilos para o mini-carrinho (toggle, badge com animação pulse, painel flutuante, scrollbars finos, botões, animação slide-in).
- Customizar layout da página do carrinho principal e do estado vazio.

## Verification Plan

### Automated Tests
Não há testes automatizados específicos para o carrinho no momento. Faremos build/compilação e validação do Maven para garantir que a aplicação compila sem erros.
- `mvn clean compile`

### Manual Verification
1. Acessar a página `/loja` e validar se os produtos dinâmicos carregam.
2. Clicar no botão "Adicionar" de um produto na listagem e verificar se o badge do mini-cart no topo incrementa e abre o mini-cart exibindo o item adicionado (sem recarregar a página).
3. Acessar a página de detalhes de um produto (`/single-product?id=X`), escolher uma quantidade e clicar em "Adicionar ao Carrinho". Validar o comportamento dinâmico.
4. No mini-cart, clicar no "X" (remover) e verificar se o item desaparece e o total do carrinho é atualizado via Fetch.
5. Ir para a página `/carrinho` e verificar se os itens adicionados estão listados. Mudar a quantidade em um input e ver se o subtotal e o total geral atualizam.
6. Clicar em "Finalizar Compra" na página do carrinho e validar o redirecionamento para a página `/compra-confirmada` e se o carrinho foi limpo.

# Tarefas para Implementação do Mini-Cart e Carrinho de Produtos

- `[x]` Criar as classes de domínio `CarrinhoItem.java` e `Carrinho.java`
- `[x]` Atualizar `GlobalAttributesAdvice.java` para injetar o carrinho dinamicamente na sessão
- `[x]` Criar o controlador REST `CarrinhoController.java` para chamadas AJAX do carrinho
- `[x]` Atualizar `Router.java` com a rota `/compra-confirmada`
- `[x]` Modificar `parts/header.html` para incluir Bootstrap Icons, o botão do mini-carrinho, dropdown flutuante e funções JS AJAX
- `[x]` Modificar `parts/product-card.html` adicionando o botão de adicionar rápido
- `[x]` Modificar `single-product.html` ajustando o botão de adicionar produto
- `[x]` Criar o template da página de sucesso `compra-confirmada.html`
- `[x]` Modificar a página de carrinho real `carrinho.html` para funcionar com os produtos da sessão
- `[x]` Adicionar estilos do mini-carrinho e aprimorar a folha de estilos em `style.css`
- `[x]` Compilar e validar a aplicação com Maven (`mvn clean compile`)
- `[x]` Validar manualmente as interações e o fluxo completo do carrinho
