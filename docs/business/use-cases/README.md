# 📊 Diagramas de Casos de Uso (Use Cases)

Este diretório contém a modelagem em **PlantUML** dos Casos de Uso do sistema **GWJ (Tgo's Barbearia)**, abrangendo o painel administrativo/gerencial e o storefront público/atendimento ao cliente.

---

## 📂 Diagramas Disponíveis

| Módulo / Escopo | Arquivo PlantUML | Diagrama Vetorial (SVG) | Imagem (PNG) | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| **Painel Administrativo & Dashboard** | [`general_dashboard.puml`](./general_dashboard.puml) | [`general_dashboard.svg`](./general_dashboard.svg) | [`general_dashboard.png`](./general_dashboard.png) | Casos de uso do Dashboard central (`/MRYnZpAsC9sp`), 6 KPIs em tempo real, navegação para módulos operacionais e de gestão estratégica, com proteção RBAC (`AdminInterceptor`). |
| **Storefront & Loja Virtual** | [`storefront.puml`](./storefront.puml) | [`storefront.svg`](./storefront.svg) | [`storefront.png`](./storefront.png) | Casos de uso da vitrine pública: e-commerce de cosméticos e kits, manipulação do carrinho em sessão HTTP, checkout atômico com baixa de estoque, agendamento de serviços e área do cliente. |

---

## 📑 Especificações Detalhadas de Casos de Uso

### 🖥️ Painel Administrativo & Dashboard (`dashboard/`)

| Identificador | Título do Caso de Uso | Diagrama (PUML) | Atores Principais | Regras e Rastreabilidade |
| :--- | :--- | :--- | :--- | :--- |
| [`UC_ADM_001`](./dashboard/UC_ADM_001_autenticar_no_painel_administrativo.md) | **Autenticar no Painel Administrativo** | `UC01` | Administrador, Recepcionista, Barbeiro | `RF004`, `RN-SEG-01`, `RN-SEG-02` |
| [`UC_ADM_002`](./dashboard/UC_ADM_002_validar_sessao_e_permissoes_rbac.md) | **Validar Sessão e Permissões RBAC** | `UC02` | `AdminInterceptor` (Segurança) | `RF004`, `RN-SEG-02`, `RN-SEG-03` |
| [`UC_ADM_003`](./dashboard/UC_ADM_003_encerrar_sessao_logout.md) | **Encerrar Sessão (Logout)** | `UC03` | Administrador, Recepcionista, Barbeiro | `RF004`, `RN-SEG-04` |
| [`UC_ADM_004`](./dashboard/UC_ADM_004_visualizar_dashboard_geral_e_kpis.md) | **Visualizar Dashboard Geral e KPIs** | `UC04` a `UC10` | Administrador, Recepcionista | 6 KPIs em tempo real, `AdminDashboardController` |
| [`UC_ADM_005`](./dashboard/UC_ADM_005_gerenciar_agendamentos.md) | **Gerenciar Agendamentos** | `UC11`, `UC12`, `UC13` | Administrador, Recepcionista, Barbeiro | `RN-AGE-01` a `RN-AGE-05`, `RN-CAN-02` |
| [`UC_ADM_006`](./dashboard/UC_ADM_006_gerenciar_pedidos_loja.md) | **Gerenciar Pedidos da Loja** | `UC14`, `UC15` | Administrador, Recepcionista | `RN-EST-01`, `RN-EST-03`, `RN-EST-04` |
| [`UC_ADM_007`](./dashboard/UC_ADM_007_gerenciar_base_clientes.md) | **Gerenciar Base de Clientes** | `UC16` | Administrador, Recepcionista | `RN-SEG-03`, `ClienteService` |
| [`UC_ADM_008`](./dashboard/UC_ADM_008_gerenciar_catalogo_servicos.md) | **Gerenciar Catálogo de Serviços** | `UC17` | Administrador | `RN-AGE-01`, `RN-SEG-03` |
| [`UC_ADM_009`](./dashboard/UC_ADM_009_gerenciar_produtos_estoque.md) | **Gerenciar Produtos e Estoque** | `UC18` | Administrador | `RN-EST-01`, `RN-EST-02`, `RN-SEG-03` |
| [`UC_ADM_010`](./dashboard/UC_ADM_010_gerenciar_equipe_profissionais.md) | **Gerenciar Equipe de Profissionais** | `UC19` | Administrador | `RN-AGE-04`, `ProfissionalService` |
| [`UC_ADM_011`](./dashboard/UC_ADM_011_gerenciar_configuracoes_barbearia.md) | **Gerenciar Configurações da Barbearia** | `UC20` | Administrador | `RN-AGE-02`, `RN-CAN-01`, `SettingService` |

---

### 🛍️ Vitrine Pública & Storefront (`storefront/`)

| Identificador | Título do Caso de Uso | Diagrama (PUML) | Atores Principais | Regras e Rastreabilidade |
| :--- | :--- | :--- | :--- | :--- |
| [`UC_STR_001`](./storefront/UC_STR_001_catalogo_produtos_e_detalhes.md) | **Navegar no Catálogo de Produtos e Kits** | `UC01`, `UC02` | Visitante, Cliente Registrado | `RN-EST-01`, `RN-EST-02`, `Router.java` |
| [`UC_STR_002`](./storefront/UC_STR_002_adicionar_e_gerenciar_carrinho.md) | **Adicionar e Gerenciar Carrinho de Compras** | `UC03` a `UC08` | Visitante, Cliente Registrado | `RN-EST-01`, `CarrinhoController.java` |
| [`UC_STR_003`](./storefront/UC_STR_003_checkout_e_pedido_produtos.md) | **Realizar Checkout e Concluir Pedido** | `UC09` a `UC14` | Visitante, Cliente, UnitOfWork | `RN-EST-01`, `RN-EST-03`, `RN-EST-04` |
| [`UC_STR_004`](./storefront/UC_STR_004_catalogo_servicos_e_disponibilidade.md) | **Consultar Serviços e Horários Disponíveis** | `UC15`, `UC16` | Visitante, Cliente Registrado | `RN-AGE-01` a `RN-AGE-04`, `AgendamentoService` |
| [`UC_STR_005`](./storefront/UC_STR_005_realizar_agendamento_e_comprovante.md) | **Checkout de Agendamento e Comprovante** | `UC17`, `UC18` | Visitante, Cliente, UnitOfWork | `RN-AGE-03`, `RN-AGE-04`, `RN-AGE-05`, `RN-ATE-01` |
| [`UC_STR_006`](./storefront/UC_STR_006_cadastro_e_autenticacao_cliente.md) | **Cadastrar Conta e Autenticar Cliente** | `UC19`, `UC20`, `UC22` | Visitante, Cliente Registrado | `RN-SEG-01`, `RN-SEG-02`, `RN-SEG-04` |
| [`UC_STR_007`](./storefront/UC_STR_007_historico_meus_agendamentos.md) | **Consultar Meus Agendamentos e Pedidos** | `UC21` | Cliente Registrado (Autenticado) | `RN-CAN-01`, `RN-ATE-01`, `Router.java` |

---

## 🛠️ Como Regenerar os Diagramas

Caso realize alterações nos arquivos `.puml`, execute o comando abaixo no terminal da raiz do projeto:

```bash
plantuml -tsvg docs/business/use-cases/*.puml
plantuml -tpng docs/business/use-cases/*.puml
```


