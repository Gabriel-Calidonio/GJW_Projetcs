# Walkthrough: Especificação Completa dos Casos de Uso (Dashboard & Storefront)

Este documento consolida a documentação formal de Casos de Uso de todo o sistema **GWJ Barbearia**, cobrindo o painel administrativo (`docs/business/use-cases/dashboard/`) e a vitrine pública / e-commerce (`docs/business/use-cases/storefront/`).

---

## 🛍️ 1. Módulo Storefront (Frente de Loja & Vitrine Pública)

Foram criados 7 documentos de especificação em [`docs/business/use-cases/storefront/`](/docs/business/use-cases/storefront/), alinhados aos 22 casos de uso do diagrama [`storefront.puml`](/docs/business/use-cases/storefront.puml):

| Arquivo | Título | Referência PUML | Atores Principais | Regras Vinculadas |
| :--- | :--- | :--- | :--- | :--- |
| [`UC_STR_001`](/docs/business/use-cases/storefront/UC_STR_001_catalogo_produtos_e_detalhes.md) | **Navegar no Catálogo de Produtos e Kits** | `UC01`, `UC02` | Visitante, Cliente Registrado | `RN-EST-01`, `RN-EST-02`, [`Router.java`](/src/main/java/com/gwj/controller/Router.java) |
| [`UC_STR_002`](/docs/business/use-cases/storefront/UC_STR_002_adicionar_e_gerenciar_carrinho.md) | **Adicionar e Gerenciar Carrinho de Compras** | `UC03` a `UC08` | Visitante, Cliente Registrado | `RN-EST-01`, [`CarrinhoController.java`](/src/main/java/com/gwj/controller/CarrinhoController.java) |
| [`UC_STR_003`](/docs/business/use-cases/storefront/UC_STR_003_checkout_e_pedido_produtos.md) | **Realizar Checkout e Concluir Pedido** | `UC09` a `UC14` | Visitante, Cliente, UnitOfWork | `RN-EST-01`, `RN-EST-03`, `RN-EST-04` |
| [`UC_STR_004`](/docs/business/use-cases/storefront/UC_STR_004_catalogo_servicos_e_disponibilidade.md) | **Consultar Serviços e Horários Disponíveis** | `UC15`, `UC16` | Visitante, Cliente Registrado | `RN-AGE-01` a `RN-AGE-04`, [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java) |
| [`UC_STR_005`](/docs/business/use-cases/storefront/UC_STR_005_realizar_agendamento_e_comprovante.md) | **Checkout de Agendamento e Comprovante** | `UC17`, `UC18` | Visitante, Cliente, UnitOfWork | `RN-AGE-03`, `RN-AGE-04`, `RN-AGE-05`, `RN-ATE-01` |
| [`UC_STR_006`](/docs/business/use-cases/storefront/UC_STR_006_cadastro_e_autenticacao_cliente.md) | **Cadastrar Conta e Autenticar Cliente** | `UC19`, `UC20`, `UC22` | Visitante, Cliente Registrado | `RN-SEG-01`, `RN-SEG-02`, `RN-SEG-04` |
| [`UC_STR_007`](/docs/business/use-cases/storefront/UC_STR_007_historico_meus_agendamentos.md) | **Consultar Meus Agendamentos e Pedidos** | `UC21` | Cliente Registrado (Autenticado) | `RN-CAN-01`, `RN-ATE-01`, [`Router.java`](/src/main/java/com/gwj/controller/Router.java) |

---

## 🖥️ 2. Módulo Dashboard (Painel Administrativo & Gestão)

Consolidado em 11 documentos de especificação em [`docs/business/use-cases/dashboard/`](/docs/business/use-cases/dashboard/), alinhados aos 20 casos de uso do diagrama [`general_dashboard.puml`](/docs/business/use-cases/general_dashboard.puml):

| Arquivo | Título | Referência PUML | Atores Principais | Regras Vinculadas |
| :--- | :--- | :--- | :--- | :--- |
| [`UC_ADM_001`](/docs/business/use-cases/dashboard/UC_ADM_001_autenticar_no_painel_administrativo.md) | **Autenticar no Painel Administrativo** | `UC01` | Administrador, Recepcionista, Barbeiro | `RF004`, `RN-SEG-01`, `RN-SEG-02` |
| [`UC_ADM_002`](/docs/business/use-cases/dashboard/UC_ADM_002_validar_sessao_e_permissoes_rbac.md) | **Validar Sessão e Permissões RBAC** | `UC02` | `AdminInterceptor` (Segurança) | `RN-SEG-02`, `RN-SEG-03` |
| [`UC_ADM_003`](/docs/business/use-cases/dashboard/UC_ADM_003_encerrar_sessao_logout.md) | **Encerrar Sessão (Logout)** | `UC03` | Administrador, Recepcionista, Barbeiro | `RN-SEG-04` |
| [`UC_ADM_004`](/docs/business/use-cases/dashboard/UC_ADM_004_visualizar_dashboard_geral_e_kpis.md) | **Visualizar Dashboard Geral e KPIs** | `UC04` a `UC10` | Administrador, Recepcionista | 6 KPIs desacoplados (`AdminDashboardController`) |
| [`UC_ADM_005`](/docs/business/use-cases/dashboard/UC_ADM_005_gerenciar_agendamentos.md) | **Gerenciar Agendamentos** | `UC11`, `UC12`, `UC13` | Administrador, Recepcionista, Barbeiro | `RN-AGE-01` a `RN-AGE-05`, `RN-CAN-02` |
| [`UC_ADM_006`](/docs/business/use-cases/dashboard/UC_ADM_006_gerenciar_pedidos_loja.md) | **Gerenciar Pedidos da Loja** | `UC14`, `UC15` | Administrador, Recepcionista | `RN-EST-01`, `RN-EST-03`, `RN-EST-04` |
| [`UC_ADM_007`](/docs/business/use-cases/dashboard/UC_ADM_007_gerenciar_base_clientes.md) | **Gerenciar Base de Clientes** | `UC16` | Administrador, Recepcionista | `RN-SEG-03`, `ClienteService` |
| [`UC_ADM_008`](/docs/business/use-cases/dashboard/UC_ADM_008_gerenciar_catalogo_servicos.md) | **Gerenciar Catálogo de Serviços** | `UC17` | Administrador | `RN-AGE-01`, `RN-SEG-03` |
| [`UC_ADM_009`](/docs/business/use-cases/dashboard/UC_ADM_009_gerenciar_produtos_estoque.md) | **Gerenciar Produtos e Estoque** | `UC18` | Administrador | `RN-EST-01`, `RN-EST-02`, `RN-SEG-03` |
| [`UC_ADM_010`](/docs/business/use-cases/dashboard/UC_ADM_010_gerenciar_equipe_profissionais.md) | **Gerenciar Equipe de Profissionais** | `UC19` | Administrador | `RN-AGE-04`, `ProfissionalService` |
| [`UC_ADM_011`](/docs/business/use-cases/dashboard/UC_ADM_011_gerenciar_configuracoes_barbearia.md) | **Gerenciar Configurações da Barbearia** | `UC20` | Administrador | `RN-AGE-02`, `RN-CAN-01`, `SettingService` |

---

## 📑 3. Índice Centralizado
* O arquivo [`docs/business/use-cases/README.md`](/docs/business/use-cases/README.md) foi devidamente atualizado com as duas tabelas completas, permitindo navegação bidirecional e fácil leitura para desenvolvedores e stakeholders.

---

## 🔍 4. Validação

1. **Diagramas PlantUML:**
   - `plantuml -tsvg docs/business/use-cases/storefront.puml` -> Compilado com sucesso (código 0).
   - `plantuml -tsvg docs/business/use-cases/general_dashboard.puml` -> Compilado com sucesso (código 0).
2. **Rastreabilidade de Código:**
   - Todos os controladores (`Router.java`, `CarrinhoController.java`, `AgendamentoController.java`, `LoginController.java`, `AdminDashboardController.java`, etc.) e entidades citadas possuem correspondência exata com a implementação Java.
3. **Regras de Negócio:**
   - Citações das regras `RN-EST`, `RN-AGE`, `RN-CAN`, `RN-SEG` e `RN-ATE` vinculadas às suas seções nos documentos de negócio.
