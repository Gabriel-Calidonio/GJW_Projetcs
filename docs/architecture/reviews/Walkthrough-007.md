# Walkthrough-007: Criação de artefatos

## Descrição

Criamos uma suíte completa de **Diagramas de Atividades (Workflows)** no formato `.puml` em [`docs/workflows/activity/`], cobrindo as principais atividades de negócio e fluxos operacionais da barbearia.
---

### 📂 Diagramas Criados

| Arquivo | Atividade / Escopo | Destaques do Fluxo |
| :--- | :--- | :--- |
| [**`agendamento_online.puml`**](/docs/workflows/activity/agendamento_online.puml) | **Agendamento Online Autônomo** ([US01](/docs/UserStory/US01-Agendamento%20de%20Hor%C3%A1rio%20Aut%C3%B4nomo.md) / RF03) | Cruzamento dinâmico de slots por duração de serviço (`tab_servico`), limite de expediente (`tab_dias_funcionamento`), e prevenção de *double-booking* com transação atômica (`UnitOfWork`). |
| [**`cancelamento_reagendamento.puml`**](/docs/workflows/activity/cancelamento_reagendamento.puml) | **Cancelamento e Reagendamento** | Validação de prazo mínimo de antecedência, liberação imediata dos slots na agenda e atualização atômica no banco. |
| [**`atendimento_execucao_servico.puml`**](/docs/workflows/activity/atendimento_execucao_servico.puml) | **Atendimento Presencial e Caixa** | Rotina do barbeiro e recepção: check-in, execução do serviço, adição de extras/produtos na comanda e fechamento financeiro. |
| [**`compra_produtos_loja.puml`**](/docs/workflows/activity/compra_produtos_loja.puml) | **Compra de Cosméticos e Kits (E-Commerce)** | Seleção de produtos, gestão do carrinho em sessão (`CarrinhoController`), verificação de estoque físico e geração de pedidos (`tab_pedidos`). |
| [**`envio_lembretes_automaticos.puml`**](/docs/workflows/activity/envio_lembretes_automaticos.puml) | **Lembretes Automáticos (RF05)** | Job de varredura periódica (`@Scheduled`), identificação de horários próximos e disparo de lembretes com links de ação via WhatsApp/E-mail. |
| [**`README.md`**](/docs/workflows/activity/README.md) | **Índice e Documentação Geral** | Sumário explicativo relacionando cada diagrama com suas respectivas User Stories e tabelas do banco de dados `gwj5`. |

---

### 🛠️ Características dos Diagramas:
- **Swimlanes (Partições):** Divididos entre **Cliente**, **Frontend (Thymeleaf/JS)**, **Backend (Spring Boot/Services)** e **Banco de Dados (MySQL)**.
- **Sintaxe Validada:** Todos os arquivos foram testados e compilam sem erros no PlantUML.

Criamos os novos **Diagramas de Sequência** em [`docs/workflows/sequence/`](/docs/workflows/sequence/), cobrindo as interações técnicas, chamadas de métodos entre camadas, transações `UnitOfWork` e persistência JDBC.

---

### 📂 Diagramas de Sequência Criados

| Arquivo | Escopo / Fluxo Técnico | Camadas e Classes Principais |
| :--- | :--- | :--- |
| [**`sequenceDiagramEcommerce.puml`**](/docs/workflows/sequence/sequenceDiagramEcommerce.puml) | **Compra de Produtos & Checkout (Loja)** | `CarrinhoController`, `HttpSession` (`Carrinho`/`CarrinhoItem`), `GenericService<Pedido>`, `UnitOfWork`, `GenericRepository`, `MySQL` (`tab_pedidos`, `tab_itens_pedido`, `tab_produto`). |
| [**`sequenceDiagramAuthSecurity.puml`**](/docs/workflows/sequence/sequenceDiagramAuthSecurity.puml) | **Autenticação, Login e Interceptor de Segurança** | `LoginController`, `PasswordUtil` (SHA-256), `UsuarioService`, `AdminInterceptor` (validação de rotas `/MRYnZpAsC9sp/*` e permissões granulares), `MySQL`. |
| [**`sequenceDiagramCancelReschedule.puml`**](/docs/workflows/sequence/sequenceDiagramCancelReschedule.puml) | **Cancelamento e Reagendamento de Horário** | `AgendaController`, `AgendamentoService`, `UnitOfWork` (rollback e commit), `GenericRepository`, `MySQL` (`tab_agendamento`). |
| [**`sequenceDiagramNotificationJob.puml`**](/docs/workflows/sequence/sequenceDiagramNotificationJob.puml) | **Lembretes Automáticos (@Scheduled)** | `TaskScheduler`, `NotificationService`, `AgendamentoService`, `WhatsApp/Email Gateway`, `MySQL`. |
| [**`README.md`**](/docs/workflows/sequence/README.md) | **Índice e Documentação de Sequência** | Guia completo catalogando todos os diagramas de sequência existentes na aplicação. |

---

### 🔍 Destaques dos Fluxos:
1. **Padrão Arquitetural Estrito:** Mostra a integração entre Controllers Spring, `ServiceRegistry`, transações gerenciadas por `UnitOfWork` com `ThreadLocal`, `GenericRepository` e `DataMapper`.
2. **Tratamento de Exceções e Concorrência:** Demonstra os caminhos alternativos (`alt / else`), bloqueio e rollbacks em casos de indisponibilidade de horário ou produtos esgotados.
3. **Validação PlantUML:** Todos os diagramas foram validados e compilam perfeitamente.



Estruturamos e criamos as especificações completas de **Requisitos Funcionais (RF)** e **Requisitos Não-Funcionais (RNF)** dentro de [`docs/requirements/`](/docs/requirements/):

---

### 🎯 1. Requisitos Funcionais ([`docs/requirements/functional/`](/docs/requirements/functional/))

| Documento | Escopo do Requisito | Regras Chave & Detalhes |
| :--- | :--- | :--- |
| [**`RF001_kits.md`**](/docs/requirements/functional/RF001_kits.md) | **Kits Promocionais, Serviços e Produtos** | Exibição de catálogo ativo, kits com produtos agrupados (`tab_produto`), validação de estoque e preços em BRL. |
| [**`RF002_grade_horarios_disponibilidade.md`**](/docs/requirements/functional/RF002_grade_horarios_disponibilidade.md) | **Motor de Busca e Grade de Horários** | Cruzamento dinâmico de slots por duração de serviço (`tab_servico.duracao`), blocos consecutivos e horário de fechamento (`tab_dias_funcionamento`). |
| [**`RF003_agendamento_online_autonomo.md`**](/docs/requirements/functional/RF003_agendamento_online_autonomo.md) | **Agendamento Autônomo e Anti Double-Booking** | Transação atômica no `AgendamentoService` com `UnitOfWork` e trava de concorrência impedindo reservas simultâneas do mesmo barbeiro. |
| [**`RF004_autenticacao_e_permissoes.md`**](/docs/requirements/functional/RF004_autenticacao_e_permissoes.md) | **Autenticação e Permissões Granulares (RBAC)** | Criptografia SHA-256 via `PasswordUtil`, interceptação de rotas administrativas (`/MRYnZpAsC9sp/*`) por `AdminInterceptor`. |
| [**`RF005_lembretes_automaticos.md`**](/docs/requirements/functional/RF005_lembretes_automaticos.md) | **Módulo de Lembretes Automáticos** | Rotina em background com `@Scheduled` buscando horários das próximas 24h/2h para envio de lembretes com links de ação via WhatsApp/E-mail. |
| [**`RF006_ecommerce_e_controle_estoque.md`**](/docs/requirements/functional/RF006_ecommerce_e_controle_estoque.md) | **E-Commerce e Baixa de Estoque** | Manipulação do carrinho (`HttpSession`), checkout com clientes ou visitantes, gravação de `tab_pedidos` e decremento atômico de estoque. |

---

### ⚡ 2. Requisitos Não-Funcionais ([`docs/requirements/non_functional/`](/docs/requirements/non_functional/))

| Documento | Foco / Categoria | Diretrizes Técnicas |
| :--- | :--- | :--- |
| [**`RNF001_performance.md`**](/docs/requirements/non_functional/RNF001_performance.md) | **Desempenho e Eficiência** | SLAs rigorosos (busca de slots em < 1.2s), acesso direto JDBC sem sobrecarga de ORM e índices no MySQL. |
| [**`RNF002_consistencia_e_concorrencia.md`**](/docs/requirements/non_functional/RNF002_consistencia_e_concorrencia.md) | **Consistência e Concorrência ACID** | Padrão `UnitOfWork` com `ThreadLocal`, InnoDB e isolamento de transações para evitar *race conditions*. |
| [**`RNF003_seguranca_e_autorizacao.md`**](/docs/requirements/non_functional/RNF003_seguranca_e_autorizacao.md) | **Segurança e Criptografia (OWASP)** | Hashing SHA-256 (`{sha256}`), PreparedStatement contra SQL Injection, Honeypot anti-spam e mascaramento de rotas. |
| [**`RNF004_usabilidade_e_responsividade.md`**](/docs/requirements/non_functional/RNF004_usabilidade_e_responsividade.md) | **Usabilidade e Mobile-First** | CSS Vanilla otimizado, áreas de clique mínimas (44x44px), máscaras automáticas de entrada e design escuro premium. |
| [**`RNF005_arquitetura_e_manutenibilidade.md`**](/docs/requirements/non_functional/RNF005_arquitetura_e_manutenibilidade.md) | **Arquitetura e Design Patterns** | Java 21, Spring Boot 3.2.5, Service Layer, Service Registry, Generic Repository e Data Mapper. |

---

Estruturamos e criamos as especificações completas de **Requisitos Funcionais (RF)** e **Requisitos Não-Funcionais (RNF)** dentro de [`docs/requirements/`](/docs/requirements/):

---

### 🎯 1. Requisitos Funcionais ([`docs/requirements/functional/`](/docs/requirements/functional/))

| Documento | Escopo do Requisito | Regras Chave & Detalhes |
| :--- | :--- | :--- |
| [**`RF001_kits.md`**](/docs/requirements/functional/RF001_kits.md) | **Kits Promocionais, Serviços e Produtos** | Exibição de catálogo ativo, kits com produtos agrupados (`tab_produto`), validação de estoque e preços em BRL. |
| [**`RF002_grade_horarios_disponibilidade.md`**](/docs/requirements/functional/RF002_grade_horarios_disponibilidade.md) | **Motor de Busca e Grade de Horários** | Cruzamento dinâmico de slots por duração de serviço (`tab_servico.duracao`), blocos consecutivos e horário de fechamento (`tab_dias_funcionamento`). |
| [**`RF003_agendamento_online_autonomo.md`**](/docs/requirements/functional/RF003_agendamento_online_autonomo.md) | **Agendamento Autônomo e Anti Double-Booking** | Transação atômica no `AgendamentoService` com `UnitOfWork` e trava de concorrência impedindo reservas simultâneas do mesmo barbeiro. |
| [**`RF004_autenticacao_e_permissoes.md`**](/docs/requirements/functional/RF004_autenticacao_e_permissoes.md) | **Autenticação e Permissões Granulares (RBAC)** | Criptografia SHA-256 via `PasswordUtil`, interceptação de rotas administrativas (`/MRYnZpAsC9sp/*`) por `AdminInterceptor`. |
| [**`RF005_lembretes_automaticos.md`**](/docs/requirements/functional/RF005_lembretes_automaticos.md) | **Módulo de Lembretes Automáticos** | Rotina em background com `@Scheduled` buscando horários das próximas 24h/2h para envio de lembretes com links de ação via WhatsApp/E-mail. |
| [**`RF006_ecommerce_e_controle_estoque.md`**](/docs/requirements/functional/RF006_ecommerce_e_controle_estoque.md) | **E-Commerce e Baixa de Estoque** | Manipulação do carrinho (`HttpSession`), checkout com clientes ou visitantes, gravação de `tab_pedidos` e decremento atômico de estoque. |

---

### ⚡ 2. Requisitos Não-Funcionais ([`docs/requirements/non_functional/`](/docs/requirements/non_functional/))

| Documento | Foco / Categoria | Diretrizes Técnicas |
| :--- | :--- | :--- |
| [**`RNF001_performance.md`**](/docs/requirements/non_functional/RNF001_performance.md) | **Desempenho e Eficiência** | SLAs rigorosos (busca de slots em < 1.2s), acesso direto JDBC sem sobrecarga de ORM e índices no MySQL. |
| [**`RNF002_consistencia_e_concorrencia.md`**](/docs/requirements/non_functional/RNF002_consistencia_e_concorrencia.md) | **Consistência e Concorrência ACID** | Padrão `UnitOfWork` com `ThreadLocal`, InnoDB e isolamento de transações para evitar *race conditions*. |
| [**`RNF003_seguranca_e_autorizacao.md`**](/docs/requirements/non_functional/RNF003_seguranca_e_autorizacao.md) | **Segurança e Criptografia (OWASP)** | Hashing SHA-256 (`{sha256}`), PreparedStatement contra SQL Injection, Honeypot anti-spam e mascaramento de rotas. |
| [**`RNF004_usabilidade_e_responsividade.md`**](/docs/requirements/non_functional/RNF004_usabilidade_e_responsividade.md) | **Usabilidade e Mobile-First** | CSS Vanilla otimizado, áreas de clique mínimas (44x44px), máscaras automáticas de entrada e design escuro premium. |
| [**`RNF005_arquitetura_e_manutenibilidade.md`**](/docs/requirements/non_functional/RNF005_arquitetura_e_manutenibilidade.md) | **Arquitetura e Design Patterns** | Java 21, Spring Boot 3.2.5, Service Layer, Service Registry, Generic Repository e Data Mapper. |

---
Estruturamos e criamos as especificações completas de Requisitos Funcionais (RF) e Requisitos Não-Funcionais (RNF) dentro de `docs/requirements/`:

### 🎯 1. Requisitos Funcionais (`docs/requirements/functional/`)

| Documento | Escopo do Requisito | Regras Chave & Detalhes |
| :--- | :--- | :--- |
| **RF001_kits.md** | Kits Promocionais, Serviços e Produtos | Exibição de catálogo ativo, kits com produtos agrupados (`tab_produto`), validação de estoque e preços em BRL. |
| **RF002_grade_horarios_disponibilidade.md** | Motor de Busca e Grade de Horários | Cruzamento dinâmico de slots por duração de serviço (`tab_servico.duracao`), blocos consecutivos e horário de fechamento (`tab_dias_funcionamento`). |
| **RF003_agendamento_online_autonomo.md** | Agendamento Autônomo e Anti Double-Booking | Transação atômica no `AgendamentoService` com `UnitOfWork` e trava de concorrência impedindo reservas simultâneas do mesmo barbeiro. |
| **RF004_autenticacao_e_permissoes.md** | Autenticação e Permissões Granulares (RBAC) | Criptografia SHA-256 via `PasswordUtil`, interceptação de rotas administrativas (`/MRYnZpAsC9sp/*`) por `AdminInterceptor`. |
| **RF005_lembretes_automaticos.md** | Módulo de Lembretes Automáticos | Rotina em background com `@Scheduled` buscando horários das próximas 24h/2h para envio de lembretes com links de ação via WhatsApp/E-mail. |
| **RF006_ecommerce_e_controle_estoque.md** | E-Commerce e Baixa de Estoque | Manipulação do carrinho (`HttpSession`), checkout com clientes ou visitantes, gravação de `tab_pedidos` e decremento atômico de estoque. |

### ⚡ 2. Requisitos Não-Funcionais (`docs/requirements/non_functional/`)

| Documento | Foco / Categoria | Diretrizes Técnicas |
| :--- | :--- | :--- |
| **RNF001_performance.md** | Desempenho e Eficiência | SLAs rigorosos (busca de slots em < 1.2s), acesso direto JDBC sem sobrecarga de ORM e índices no MySQL. |
| **RNF002_consistencia_e_concorrencia.md** | Consistência e Concorrência ACID | Padrão `UnitOfWork` com `ThreadLocal`, InnoDB e isolamento de transações para evitar race conditions. |
| **RNF003_seguranca_e_autorizacao.md** | Segurança e Criptografia (OWASP) | Hashing SHA-256 (`{sha256}`), `PreparedStatement` contra SQL Injection, Honeypot anti-spam e mascaramento de rotas. |
| **RNF004_usabilidade_e_responsividade.md** | Usabilidade e Mobile-First | CSS Vanilla otimizado, áreas de clique mínimas (44x44px), máscaras automáticas de entrada e design escuro premium. |
| **RNF005_arquitetura_e_manutenibilidade.md** | Arquitetura e Design Patterns | Java 21, Spring Boot 3.2.5, Service Layer, Service Registry, Generic Repository e Data Mapper. |

### 📑 Sumário Central
* Criado o arquivo [**`docs/requirements/README.md`**](/docs/requirements/README.md) contendo o índice geral mapeando todos os requisitos com suas prioridades e módulos.

Listed directory business
Viewed glossary.md
Created glossary.md
Created regras_negocio_agendamento.md
Created regras_negocio_cancelamento_reagendamento.md
Created regras_negocio_atendimento_comandas.md
Created regras_negocio_loja_estoque.md
Created regras_negocio_seguranca_perfis.md
Created README.md
Listed directory business

Criamos a documentação completa de **Regras de Negócio e Glossário de Domínio** em [`docs/business/`](/docs/business/):

---

### 🏢 Documentos de Negócio Criados

| Arquivo | Módulo / Escopo | Principais Regras & Políticas |
| :--- | :--- | :--- |
| [**`glossary.md`**](/docs/business/glossary.md) | **Glossário Ubíquo (DDD)** | Definições formais da barbearia: Slots de 20 min, Blocos Consecutivos, Double-Booking, No-Show, Comanda Dinâmica, Kits Promocionais e RBAC. |
| [**`regras_negocio_agendamento.md`**](/docs/business/regras_negocio_agendamento.md) | **Agendamento (RN-AGE)** | - **RN-AGE-01:** Ocupação de múltiplos blocos contínuos por duração do serviço.<br>- **RN-AGE-02:** Respeito rigoroso ao limite de horário do expediente (`tab_dias_funcionamento`).<br>- **RN-AGE-03:** Bloqueio de horários no passado.<br>- **RN-AGE-04:** Alocação de profissional livre.<br>- **RN-AGE-05:** Prevenção atômica de *double-booking* com trava transacional. |
| [**`regras_negocio_cancelamento_reagendamento.md`**](/docs/business/regras_negocio_cancelamento_reagendamento.md) | **Cancelamento & Reagendamento (RN-CAN)** | - **RN-CAN-01:** Antecedência mínima de 2 horas para cancelamento online.<br>- **RN-CAN-02:** Liberação instantânea de slots na grade pública.<br>- **RN-CAN-03:** Reagendamento com preservação do ID e histórico. |
| [**`regras_negocio_atendimento_comandas.md`**](/docs/business/regras_negocio_atendimento_comandas.md) | **Atendimento & Caixa (RN-ATE)** | - **RN-ATE-01:** Ciclo de vida do atendimento (`Confirmado` $\rightarrow$ `Em Atendimento` $\rightarrow$ `Concluído`).<br>- **RN-ATE-02:** Inclusão dinâmica de serviços e produtos extras na comanda.<br>- **RN-ATE-03:** Baixa de estoque no balcão e apuração de comissões. |
| [**`regras_negocio_loja_estoque.md`**](/docs/business/regras_negocio_loja_estoque.md) | **Loja & Estoque (RN-EST)** | - **RN-EST-01:** Validação e trava atômica de estoque no checkout.<br>- **RN-EST-02:** Composição e precificação diferenciada de Kits Promocionais.<br>- **RN-EST-03:** Diferenciação entre pedidos de clientes logados vs visitantes. |
| [**`regras_negocio_seguranca_perfis.md`**](/docs/business/regras_negocio_seguranca_perfis.md) | **Segurança & Perfis (RN-SEG)** | - **RN-SEG-01:** Hashing SHA-256 obrigatório em senhas (`{sha256}`).<br>- **RN-SEG-02:** Bloqueio mandatório de clientes no painel `/MRYnZpAsC9sp/*`.<br>- **RN-SEG-03:** Mapeamento granular de permissões por entidade (`AdminInterceptor`). |
| [**`README.md`**](/docs/business/README.md) | **Índice Geral de Negócio** | Mapa consolidado correlacionando termos, regras de negócio e arquivos do projeto. |

