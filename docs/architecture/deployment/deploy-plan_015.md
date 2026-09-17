# Plano de Implementação: Especificação dos Casos de Uso do Dashboard Administrativo

Este documento detalha o plano para preencher e estruturar os Casos de Uso da pasta `docs/business/use-cases/dashboard/`, garantindo rastreabilidade com o diagrama de casos de uso [`general_dashboard.puml`](/docs/business/use-cases/general_dashboard.puml), os requisitos funcionais e as regras de negócio do sistema GWJ.

---

## Contexto e Diagnóstico Atual

1. O arquivo existente [`UC_ADM_001_autenticar_no_painel_administrativo.md`](/docs/business/use-cases/dashboard/UC_ADM_001_autenticar_no_painel_administrativo.md) possui o nome de autenticação administrativa, porém seu conteúdo interno é um template não preenchido com título "Gerenciar Catálogo de Produtos".
2. O diagrama [`general_dashboard.puml`](/docs/business/use-cases/general_dashboard.puml) define **4 pacotes** e **20 casos de uso**:
   - **Controle de Acesso & Sessão**: UC01 (Login), UC02 (Validação RBAC), UC03 (Logout).
   - **Dashboard & Indicadores**: UC04 (Visualizar Dashboard), UC05 a UC10 (6 KPIs de Agendamentos, Pedidos, Serviços, Produtos, Clientes e Profissionais).
   - **Módulos de Gestão Operacional**: UC11 (Agendamentos), UC12 (Filtro por Data/Profissional), UC13 (Status Agendamento), UC14 (Pedidos), UC15 (Status Pedido), UC16 (Clientes).
   - **Módulos Estratégicos & Configurações**: UC17 (Serviços), UC18 (Produtos/Estoque), UC19 (Profissionais), UC20 (Configurações da Barbearia).

---

## User Review Required

> [!IMPORTANT]
> **Estratégia de Organização dos Arquivos**:
> Propõe-se documentar os casos de uso agrupando as extensões (`<<extend>>`) e inclusões (`<<include>>`) em casos de uso principais coesos, totalizando **11 documentos centrais** (ou alternativamente 20 arquivos individuais se preferir 1:1 estrito).
> 
> A abordagem recomendada (11 documentos estruturados) mantém cada arquivo rico, completo e com fluxos alternativos/extensões integrados:
> 1. `UC_ADM_001_autenticar_no_painel_administrativo.md` (UC01)
> 2. `UC_ADM_002_validar_sessao_e_permissoes_rbac.md` (UC02)
> 3. `UC_ADM_003_encerrar_sessao_logout.md` (UC03)
> 4. `UC_ADM_004_visualizar_dashboard_geral_e_kpis.md` (UC04 + UC05 a UC10)
> 5. `UC_ADM_005_gerenciar_agendamentos.md` (UC11 + UC12 + UC13)
> 6. `UC_ADM_006_gerenciar_pedidos_loja.md` (UC14 + UC15)
> 7. `UC_ADM_007_gerenciar_base_clientes.md` (UC16)
> 8. `UC_ADM_008_gerenciar_catalogo_servicos.md` (UC17)
> 9. `UC_ADM_009_gerenciar_produtos_estoque.md` (UC18)
> 10. `UC_ADM_010_gerenciar_equipe_profissionais.md` (UC19)
> 11. `UC_ADM_011_gerenciar_configuracoes_barbearia.md` (UC20)

---

## Proposed Changes

### 1. Correção Imediata do `UC_ADM_001`

#### [MODIFY] [UC_ADM_001_autenticar_no_painel_administrativo.md](/docs/business/use-cases/dashboard/UC_ADM_001_autenticar_no_painel_administrativo.md)
Preencher completamente a especificação formal de autenticação:
- **Atributos:** Identificador `UC_ADM_001`, Atores (Administrador, Recepcionista, Barbeiro), Rastreabilidade (`RF004`, `RN-SEG-01`, `RN-SEG-02`).
- **Descrição Sumária:** Autenticação dos colaboradores no painel administrativo seguro em `/MRYnZpAsC9sp/login`.
- **Pré-Condições:** Usuário cadastrado e ativo (`status = true`), perfil não-cliente (`perfil_id != 4`).
- **Pós-Condições:** Sessão HTTP instanciada com atributo `usuarioLogado`, redirecionamento para `/MRYnZpAsC9sp`.
- **Gatilho:** Colaborador acessa `/MRYnZpAsC9sp/login` ou tenta acessar rota protegida sem sessão.
- **Fluxo Principal:** 
  1. Sistema apresenta formulário com e-mail e senha.
  2. Operador insere credenciais e clica em "Entrar".
  3. Sistema aplica hash SHA-256 via `PasswordUtil.hash(senha)`.
  4. Sistema busca usuário pelo e-mail e compara a senha criptografada.
  5. Sistema valida se o perfil é diferente de Cliente (`perfil_id != 4`).
  6. Sistema armazena objeto `Usuario` na `HttpSession`.
  7. Sistema redireciona para o Dashboard Geral `/MRYnZpAsC9sp`.
- **Fluxos Alternativos e de Exceção:**
  - *FA01: Usuário já autenticado* -> Redirecionamento imediato para `/MRYnZpAsC9sp`.
  - *FE01: Credenciais inválidas (e-mail ou senha incorretos)* -> Retorna para tela com mensagem "E-mail ou senha inválidos."
  - *FE02: Tentativa de login por cliente (Perfil 4)* -> Bloqueia e exibe "Acesso negado: esta área é restrita a administradores."
  - *FE03: Usuário inativo / bloqueado* -> Impede acesso com mensagem explicativa.
- **Regras de Negócio Aplicadas:** `RN-SEG-01`, `RN-SEG-02`.
- **Interface & Campos de Entrada/Saída:** Mapeamento de campos do formulário HTML e atributos de sessão.

---

### 2. Criação dos Demais Casos de Uso do Módulo Administrativo

Criar os arquivos padronizados com o mesmo rigor de especificação:
- `UC_ADM_002_validar_sessao_e_permissoes_rbac.md`: Interceptor, proteção de rotas `/MRYnZpAsC9sp/*`, verificação de perfis e permissões granulares (`GERENCIAR_CLIENTES`, `GERENCIAR_SERVICOS`, etc.).
- `UC_ADM_003_encerrar_sessao_logout.md`: Rota `/MRYnZpAsC9sp/logout`, invalidação de sessão (`session.invalidate()`) e redirecionamento.
- `UC_ADM_004_visualizar_dashboard_geral_e_kpis.md`: Rota `/MRYnZpAsC9sp`, cálculo e renderização das 6 métricas em tempo real (`totalAgendamentos`, `totalPedidos`, `totalServicos`, `totalProdutos`, `totalClientes`, `totalProfissionais`).
- `UC_ADM_005_gerenciar_agendamentos.md`: Listagem, agendamento de horário, filtros por data e barbeiro, transição de status (Confirmado, Finalizado, Cancelado), bloqueio de horários no passado e respeito a blocos de 20 min.
- `UC_ADM_006_gerenciar_pedidos_loja.md`: Listagem de pedidos, tela de detalhes com itens e comprador (cliente cadastrado ou visitante), alteração de status (Aguardando Retirada, Retirado, Cancelado).
- `UC_ADM_007_gerenciar_base_clientes.md`: Gestão de cadastros de clientes da barbearia, histórico de contatos e integridade com agendamentos.
- `UC_ADM_008_gerenciar_catalogo_servicos.md`: Cadastro, edição e ativação de serviços, definição de preço e duração em minutos para a grade.
- `UC_ADM_009_gerenciar_produtos_estoque.md`: Gestão de cosméticos e kits, controle de saldo em estoque e precificação.
- `UC_ADM_010_gerenciar_equipe_profissionais.md`: Cadastro e gestão de barbeiros e colaboradores, vinculação a agendamentos.
- `UC_ADM_011_gerenciar_configuracoes_barbearia.md`: Parâmetros institucionais, horários de funcionamento semanais e limites de tolerância.

---

### 3. Atualização do README de Casos de Uso

#### [MODIFY] [docs/business/use-cases/README.md](/docs/business/use-cases/README.md)
Atualizar a tabela e índice de documentos adicionando a relação detalhada dos casos de uso do Dashboard com links diretos para cada arquivo `.md`.

---

## Verification Plan

### Validação Manual e Estrutural
- Verificar a integridade das referências cruzadas: checar se todos os links para controllers (`AdminDashboardController`, `LoginController`, `AdminInterceptor`, etc.) e documentos de regras de negócio (`regras_negocio_*.md`) estão corretos.
- Verificar consistência de atores com a herança modelada em `general_dashboard.puml` (Administrador herda Recepcionista e Barbeiro).
- Validar se a formatação Markdown de tabelas, alertas e listas está em conformidade com o padrão do repositório.
