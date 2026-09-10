# DP-3: Implementação do Checkout do Mini-Shopping

- **Tipo:** Deployment plan
- **Status:** closed
- **Autor:** GWJ
- **Criado em:** 2026-06-16 18:53:24
- **Labels:** Nenhuma
- **Responsáveis:** Nenhum

## Descrição

# Implementação do Checkout do Mini-Shopping

Implementar o fluxo completo de checkout para os produtos da loja do mini-shopping, permitindo que tanto clientes logados quanto visitantes finalizem suas compras. O pedido e seus respectivos itens serão gravados no banco de dados nas tabelas `tab_pedidos` e `tab_itens_pedido`.

## User Review Required

> [!IMPORTANT]
> **Criação de Tabelas Automática:**
> Para garantir a autonomia da aplicação e facilitar a execução local do projeto, adicionaremos a lógica de criação das tabelas `tab_pedidos` e `tab_itens_pedido` diretamente na inicialização do sistema via `SchemaValidator`.
>
> **Fluxo de Usuário (Cliente vs. Visitante):**
> - Se o usuário estiver **logado**, utilizaremos seus dados de `Cliente` cadastrado (ID) para associar o pedido.
> - Se for um **visitante** (não logado), solicitaremos o preenchimento de um formulário de identificação rápido contendo: **Nome**, **Telefone** e a escolha da **Forma de Pagamento**.

## Proposed Changes

### Database & Bootstrapping

#### [MODIFY] [SchemaValidator.java](/src/main/java/com/gwj/model/domain/factory/SchemaValidator.java)
Adicionar os métodos para garantir que as tabelas `tab_pedidos` e `tab_itens_pedido` sejam criadas no MySQL na inicialização do sistema se não existirem.

#### [MODIFY] [StartApplication.java](/src/main/java/com/gwj/StartApplication.java)
Invocar os novos métodos de verificação de tabelas de pedidos criados no `SchemaValidator`.

---

### Backend Domain Entities

#### [MODIFY] [Pedido.java](/src/main/java/com/gwj/model/domain/entities/Pedido.java)
Ajustar o rascunho de `Pedido.java` para:
- Implementar `IEntity` (ID do tipo `Long`).
- Adicionar anotações JPA para tabela (`@Entity`, `@Table(name = "tab_pedidos")`).
- Mapear a relação `@ManyToOne` com a entidade `Cliente` e a relação `@OneToMany(mappedBy = "pedido")` com a lista de `ItemPedido`.

#### [MODIFY] [ItemPedido.java](/src/main/java/com/gwj/model/domain/entities/ItemPedido.java)
Ajustar o rascunho de `ItemPedido.java` para:
- Implementar `IEntity`.
- Adicionar anotações JPA (`@Entity`, `@Table(name = "tab_itens_pedido")`).
- Mapear a relação `@ManyToOne` com `Pedido` e `Produto`.

---

### Checkout Business Logic & Routes

#### [MODIFY] [CarrinhoController.java](/src/main/java/com/gwj/controller/CarrinhoController.java)
- Adicionar rota `GET /carrinho/checkout`: renderiza a página de finalização de compras (`carrinho-checkout.html`). Exibe o resumo do carrinho e o formulário necessário. Preenche automaticamente os dados do cliente se estiver autenticado.
- Adicionar rota `POST /carrinho/checkout/confirmar`: processa a finalização da compra. Cria o objeto `Pedido` e seus `ItemPedido` associados, salva no banco através do repositório genérico, limpa o carrinho de compras na sessão e redireciona para a página `/compra-confirmada?id=PEDIDO_ID`.

#### [MODIFY] [Router.java](/src/main/java/com/gwj/controller/Router.java)
- Ajustar a rota `GET /compra-confirmada` para aceitar opcionalmente o parâmetro `id` do pedido. Se fornecido, carrega os detalhes do pedido do banco de dados e repassa ao template para exibir o resumo completo da compra (número do pedido, itens, total, forma de pagamento).

---

### Frontend Checkout Interface

#### [NEW] [carrinho-checkout.html](/src/main/resources/templates/carrinho-checkout.html)
Criar uma página de checkout premium para produtos com:
- Grid responsivo dividindo:
  1. Formulário de identificação e pagamento (Nome e Telefone para visitantes, ou aviso de cliente logado; opções de pagamento como PIX, Cartão ou Dinheiro na retirada).
  2. Resumo lateral contendo a lista compacta de produtos comprados e o valor total do carrinho.
- Validação no submit para garantir o preenchimento de campos obrigatórios.

#### [MODIFY] [carrinho.html](/src/main/resources/templates/carrinho.html)
- Modificar o botão "Finalizar Compra" para apontar para a rota de checkout: `GET /carrinho/checkout` ao invés de submeter diretamente o formulário POST que limpava o carrinho.

#### [MODIFY] [compra-confirmada.html](/src/main/resources/templates/compra-confirmada.html)
- Adaptar o layout para renderizar dinamicamente os detalhes do pedido salvos no banco de dados (número do pedido formatado, lista de itens e total) caso o objeto `pedido` seja passado pelo controller.

## Verification Plan

### Automated Tests
Validação de integridade e compilação do projeto:
- `mvn clean compile`

### Manual Verification
1. Acessar a loja, adicionar produtos ao carrinho e clicar em "Finalizar Compra" a partir da página `/carrinho`.
2. Validar que é redirecionado para `/carrinho/checkout`.
3. **Fluxo Visitante:**
   - Preencher o formulário com Nome fictício, Telefone e escolher Forma de Pagamento.
   - Clicar em "Confirmar Pedido".
   - Validar que o pedido é salvo no banco, o carrinho é esvaziado na sessão, e o usuário é redirecionado para a tela `/compra-confirmada` mostrando os dados reais do pedido gravado (ex: `#PED-X`, itens, valor correto).
4. **Fluxo Logado:**
   - Fazer login com um usuário cliente cadastrado.
   - Adicionar produtos ao carrinho e prosseguir para `/carrinho/checkout`.
   - Validar que os campos de identificação de Nome/Telefone não aparecem ou vêm bloqueados/identificados como cliente logado.
   - Confirmar o pedido e validar a gravação vinculando o `cliente_id` no banco de dados.

# Tarefas para Implementação do Checkout do Mini-Shopping

- `[x]` Atualizar `SchemaValidator.java` com a inicialização automática das tabelas `tab_pedidos` e `tab_itens_pedido`
- `[x]` Atualizar `StartApplication.java` para chamar a validação das tabelas de pedidos
- `[x]` Ajustar a classe de entidade `Pedido.java`
- `[x]` Ajustar a classe de entidade `ItemPedido.java`
- `[x]` Criar o template do formulário de finalização de compras `carrinho-checkout.html`
- `[x]` Modificar `carrinho.html` para direcionar o botão "Finalizar Compra" para a tela de checkout
- `[x]` Atualizar `CarrinhoController.java` com as rotas de checkout (GET `/carrinho/checkout` e POST `/carrinho/checkout/confirmar`)
- `[x]` Atualizar `Router.java` com a rota GET `/compra-confirmada` aceitando opcionalmente o ID do pedido
- `[x]` Modificar `compra-confirmada.html` para renderizar os detalhes reais do pedido salvo
- `[x]` Adicionar estilos adicionais necessários de checkout em `style.css`
- `[x]` Compilar a aplicação com Maven (`mvn clean compile`)
- `[x]` Testar manualmente o fluxo completo de compra e persistência (Logado e Visitante)
