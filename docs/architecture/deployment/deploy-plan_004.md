# DP-4: Gestão de Pedidos no Dashboard Administrativo

- **Tipo:** Deployment plan
- **Status:** closed
- **Autor:** GWJ
- **Criado em:** 2026-06-16 18:56:02
- **Labels:** Nenhuma
- **Responsáveis:** Nenhum

## Descrição

# Gestão de Pedidos no Dashboard Administrativo

Implementar a gestão de pedidos no dashboard administrativo, oferecendo uma tela de listagem de pedidos com ordenação cronológica decrescente, e uma tela de detalhes com a listagem de produtos comprados, subtotal, total e a possibilidade do administrador atualizar o status do pedido (ex: de "Aguardando Retirada" para "Retirado" ou "Cancelado").

## User Review Required

> [!IMPORTANT]
> **Fluxo de Status do Pedido:**
> O administrador poderá atualizar o status do pedido a partir de um dropdown na tela de detalhes. As alterações persistirão diretamente no banco de dados.
>
> **Links de Acesso:**
> Adicionaremos o link "Pedidos" na sidebar do painel administrativo (`admin/fragments/sidebar.html`), integrando perfeitamente a gestão de pedidos de produtos junto com as demais entidades já existentes.

## Proposed Changes

### Backend Control Layer

#### [NEW] [AdminPedidoController.java](/src/main/java/com/gwj/controller/AdminPedidoController.java)
Controlador com rotas administrativas dedicadas à gestão de pedidos:
- `GET /MRYnZpAsC9sp/pedidos`: Lista todos os pedidos cadastrados, ordenados do mais recente para o mais antigo.
- `GET /MRYnZpAsC9sp/pedidos/detalhe?id=X`: Exibe a tela de detalhes do pedido correspondente com a lista de itens e formulário de status.
- `POST /MRYnZpAsC9sp/pedidos/atualizar-status`: Atualiza o status do pedido no banco de dados e redireciona de volta para os detalhes.

---

### Frontend Views & Layouts

#### [MODIFY] [sidebar.html](/src/main/resources/templates/admin/fragments/sidebar.html)
Inserir o link "Pedidos" (apontando para `/MRYnZpAsC9sp/pedidos`) na navegação administrativa, preferencialmente após o link de "Produtos".

#### [NEW] [listar.html](/src/main/resources/templates/admin/pedidos/listar.html)
Criar tela de listagem administrativa de pedidos contendo:
- Tabela com colunas: ID do Pedido, Cliente (ou Nome/Telefone de Visitante), Data do Pedido, Forma de Pagamento, Total e Status.
- Badges estilizados de acordo com o status (ex: amarelo para "Aguardando Retirada", verde para "Retirado", vermelho para "Cancelado").
- Botão "Ver Detalhes" para abrir as informações completas.

#### [NEW] [detalhe.html](/src/main/resources/templates/admin/pedidos/detalhe.html)
Criar tela detalhada do pedido em formato de fatura/recibo contendo:
- Informações gerais (ID do pedido, Data, Forma de Pagamento e Status atual).
- Dados de identificação (Cliente cadastrado ou Visitante com telefone).
- Tabela com os itens do pedido (Imagem do produto, Nome, Quantidade, Preço Unitário e Subtotal).
- Formulário para atualização do Status do Pedido com dropdown e botão de salvar.
- Links para voltar para a lista.

## Verification Plan

### Automated Tests
Validação de integridade e compilação do projeto:
- `mvn clean compile`

### Manual Verification
1. Fazer login como administrador e acessar o painel administrativo em `/MRYnZpAsC9sp`.
2. Validar que o link **Pedidos** aparece na sidebar.
3. Clicar em **Pedidos** e verificar se todos os pedidos (tanto de visitantes quanto de clientes logados) aparecem listados na tabela, ordenados do mais recente primeiro.
4. Clicar em **Ver Detalhes** de um pedido.
5. Validar se a lista de produtos, quantidades e total coincidem exatamente com o carrinho finalizado.
6. Alterar o status do pedido no dropdown (ex: selecionar "Retirado") e clicar em **Atualizar Status**.
7. Verificar se a página atualiza, mostra o novo status e a modificação foi gravada com sucesso no banco de dados.

# Tarefas para Implementação da Gestão de Pedidos no Admin

- `[x]` Criar a classe `AdminPedidoController.java`
- `[x]` Modificar `admin/fragments/sidebar.html` para incluir o link "Pedidos"
- `[x]` Criar a pasta de templates `admin/pedidos` se não existir
- `[x]` Criar o template de listagem `admin/pedidos/listar.html`
- `[x]` Criar o template de detalhes `admin/pedidos/detalhe.html`
- `[x]` Compilar a aplicação com Maven (`mvn clean compile`)
- `[x]` Validar manualmente o fluxo completo no painel administrativo
