# Walkthrough: Especificação Completa dos Casos de Uso do Dashboard

Este documento consolida o trabalho realizado na pasta [`docs/business/use-cases/dashboard/`](/docs/business/use-cases/dashboard/), onde foram especificadas formalmente todas as operações administrativas e gerenciais do sistema **GWJ Barbearia**.

---

## 📋 Resumo das Mudanças

### 1. Correção e Especificação do UC01
* **Arquivo:** [`UC_ADM_001_autenticar_no_painel_administrativo.md`](/docs/business/use-cases/dashboard/UC_ADM_001_autenticar_no_painel_administrativo.md)
* **Ajuste:** Substituição do esboço genérico que continha referências a "produtos" pela especificação integral de autenticação corporativa, validação de hash SHA-256 (`PasswordUtil`), isolamento de perfil Cliente (`RN-SEG-02`) e criação de sessão HTTP.

---

### 2. Criação dos Documentos do Dashboard e Módulos Operacionais

Foram criados 11 documentos de especificação alinhados aos 20 casos de uso modelados no diagrama [`general_dashboard.puml`](/docs/business/use-cases/general_dashboard.puml):

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

### 3. Atualização do README Central de Casos de Uso
* **Arquivo:** [`docs/business/use-cases/README.md`](/docs/business/use-cases/README.md)
* Adicionada a seção **"Especificações Detalhadas de Casos de Uso"** contendo a tabela de índices com links diretos para cada arquivo `.md`.

---

## 🔍 Validação

1. **Compilação do Diagrama PlantUML:**
   - Comando executado: `plantuml -tsvg docs/business/use-cases/general_dashboard.puml`
   - Resultado: Sucesso sem erros sintáticos.
2. **Rastreabilidade e Links:**
   - Todos os controladores citados (`AdminDashboardController`, `AdminAgendamentoController`, `AdminPedidoController`, `AdminClienteController`, `AdminServicoController`, `AdminProdutoController`, `AdminProfissionalController`, `AdminSettingController`, `LoginController`, `AdminInterceptor`) e entidades foram validados no código-fonte Java real do projeto.
   - Todas as regras de negócio (`RN-SEG`, `RN-AGE`, `RN-EST`, `RN-ATE`, `RN-CAN`) foram citadas com referências corretas aos seus respectivos arquivos de documentação.
