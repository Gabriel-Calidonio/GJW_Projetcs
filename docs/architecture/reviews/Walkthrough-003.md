# Walkthrough-003: Checkout do Mini-Shopping

Implementamos o fluxo completo de checkout para compras de produtos no mini-shopping. Os pedidos agora são persistidos no banco de dados nas tabelas `tab_pedidos` e `tab_itens_pedido`, vinculados a um cliente (se logado) ou com os dados informados caso seja um visitante.

## O que foi Feito

### 1. Banco de Dados & Inicialização Automática
- **`SchemaValidator.java`**: Adicionamos o método `ensureOrderTablesExist()` que cria automaticamente as tabelas `tab_pedidos` e `tab_itens_pedido` no banco de dados MySQL na inicialização caso elas não existam.
- **`StartApplication.java`**: Invoca o novo validador no método `main` para garantir que o banco esteja pronto antes da inicialização do servidor.

### 2. Classes de Entidade (Modelo ORM)
- **`Pedido.java`**:
  - Implementa `IEntity`.
  - Mapeado com `@Entity` e `@Table(name = "tab_pedidos")`.
  - Relacionamentos: `@ManyToOne` com `Cliente` (vazio para visitante) e `@OneToMany` com lista de `ItemPedido`.
- **`ItemPedido.java`**:
  - Implementa `IEntity`.
  - Mapeado com `@Entity` e `@Table(name = "tab_itens_pedido")`.
  - Relacionamentos: `@ManyToOne` com `Pedido` e `Produto`.
  - Fotografa o preço unitário e dados do produto no momento do pedido.

### 3. Rotas & Fluxo de Negócio (Controllers)
- **`CarrinhoController.java`**:
  - **`GET /carrinho/checkout`**: Carrega e exibe a tela de fechamento de compra. Verifica se o usuário está logado e, caso positivo, carrega a entidade `Cliente` correspondente para injetar na view.
  - **`POST /carrinho/checkout/confirmar`**: Cria a entidade `Pedido` com seus `ItemPedido`s. Se o cliente estiver logado, associa o pedido ao cadastro. Caso contrário, associa os dados de visitante (Nome e Telefone). O pedido é salvo usando o DAO genérico (que salva recursivamente seus itens), limpa a sessão e redireciona para a confirmação.
- **`Router.java`**:
  - **`GET /compra-confirmada`**: Atualizado para aceitar opcionalmente o parâmetro `id` do pedido. Se informado, lê os dados do pedido do banco de dados e os injeta na view para exibir a confirmação e resumo de itens real.

### 4. Interface Gráfica (Views & Styles)
- **`carrinho-checkout.html`**:
  - Nova interface premium contendo o formulário de finalização (identificação do visitante ou indicação de cliente logado, formas de pagamento) e resumo lateral dinâmico de itens e total geral.
- **`carrinho.html` e `parts/header.html`**:
  - Atualização do botão "Finalizar Compra" para apontar para a rota de checkout `GET /carrinho/checkout`.
- **`compra-confirmada.html`**:
  - Adaptado para renderizar o recibo completo do pedido (Número do Pedido, Cliente/Retirante, Forma de Pagamento, Status, itens separados e valor total a pagar).
- **`style.css`**:
  - Estilos específicos de checkout para alertas e barra de rolagem lateral de itens.

---

## Como Validar Manualmente

### 1. Iniciar a Aplicação
Execute o Maven para subir o servidor Spring Boot localmente:
`mvn spring-boot:run`

Na inicialização, observe os logs para validar que as tabelas de pedidos foram garantidas:
`✅ Tabela tab_pedidos criada ou já existente.`
`✅ Tabela tab_itens_pedido criada ou já existente.`

### 2. Testar como Visitante (Não Logado)
1. Navegue até a loja, adicione alguns produtos ao carrinho.
2. Acesse a página `/carrinho` e clique em **Finalizar Compra**.
3. Você será redirecionado para `/carrinho/checkout`.
4. Preencha o Nome e Telefone do Visitante, escolha uma forma de pagamento (ex: PIX) e clique em **Confirmar Pedido**.
5. Você será redirecionado para `/compra-confirmada?id=X`.
6. Valide que as informações reais da sua compra são exibidas: Número do Pedido (`#PED-X`), Nome do Visitante, itens comprados com quantidade e o valor total correto.

### 3. Testar como Cliente Logado
1. Faça login na barbearia usando uma conta de cliente.
2. Adicione produtos ao carrinho e prossiga até o checkout em `/carrinho/checkout`.
3. Verifique se o sistema detectou sua conta logada (exibindo o alerta informando que a compra será vinculada à sua conta de cliente) e que o formulário de visitante não é exibido.
4. Confirme o pedido.
5. Verifique na página `/compra-confirmada` que o nome impresso no recibo é o seu nome de cliente logado.
