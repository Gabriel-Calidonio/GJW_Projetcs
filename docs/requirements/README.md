# 📋 Especificação de Requisitos do Sistema (GWJ - Barbearia)

Este diretório contém as especificações formais de **Requisitos Funcionais (RF)** e **Requisitos Não-Funcionais (RNF)** do **Sistema GWJ para Tgo's Barbearia**.

---

## 🎯 1. Requisitos Funcionais (RF)

Localizados em [`docs/requirements/functional/`](/docs/requirements/functional/):

| Código | Título | Módulo | Prioridade |
| :--- | :--- | :--- | :--- |
| [`RF001_kits.md`](/docs/requirements/functional/RF001_kits.md) | **Catálogo de Kits Promocionais, Serviços e Produtos** | Loja & Serviços | Alta |
| [`RF002_grade_horarios_disponibilidade.md`](/docs/requirements/functional/RF002_grade_horarios_disponibilidade.md) | **Motor de Busca e Cálculo da Grade de Horários Livres** | Agenda & Horários | Crítica |
| [`RF003_agendamento_online_autonomo.md`](/docs/requirements/functional/RF003_agendamento_online_autonomo.md) | **Agendamento Online Autônomo e Prevenção de Concorrência** | Agendamento & Checkout | Crítica |
| [`RF004_autenticacao_e_permissoes.md`](/docs/requirements/functional/RF004_autenticacao_e_permissoes.md) | **Autenticação de Usuários e Controle Granular de Permissões** | Segurança & Acesso | Alta |
| [`RF005_lembretes_automaticos.md`](/docs/requirements/functional/RF005_lembretes_automaticos.md) | **Módulo de Lembretes Automáticos e Notificações** | Background & Mensageria | Média-Alta |
| [`RF006_ecommerce_e_controle_estoque.md`](/docs/requirements/functional/RF006_ecommerce_e_controle_estoque.md) | **E-Commerce de Cosméticos Masculinos e Controle de Estoque** | Loja & Estoque | Média-Alta |

---

## ⚡ 2. Requisitos Não-Funcionais (RNF)

Localizados em [`docs/requirements/non_functional/`](/docs/requirements/non_functional/):

| Código | Título | Categoria / Foco | Prioridade |
| :--- | :--- | :--- | :--- |
| [`RNF001_performance.md`](/docs/requirements/non_functional/RNF001_performance.md) | **Desempenho, Eficiência e Baixa Latência** | SLAs de resposta (< 1.2s para busca de slots) | Alta |
| [`RNF002_consistencia_e_concorrencia.md`](/docs/requirements/non_functional/RNF002_consistencia_e_concorrencia.md) | **Consistência Transacional e Anti Double-Booking** | Transações ACID com `UnitOfWork` e `ThreadLocal` | Crítica |
| [`RNF003_seguranca_e_autorizacao.md`](/docs/requirements/non_functional/RNF003_seguranca_e_autorizacao.md) | **Segurança, Criptografia e Interceptação** | Hashing SHA-256, PreparedStatement, `AdminInterceptor` | Alta |
| [`RNF004_usabilidade_e_responsividade.md`](/docs/requirements/non_functional/RNF004_usabilidade_e_responsividade.md) | **Usabilidade, UX e Mobile-First** | CSS Vanilla, Design System Premium, Área de toque mínima | Média-Alta |
| [`RNF005_arquitetura_e_manutenibilidade.md`](/docs/requirements/non_functional/RNF005_arquitetura_e_manutenibilidade.md) | **Arquitetura Desacoplada e Design Patterns** | Java 21, Spring Boot 3.2.5, DataMapper, Service Registry | Alta |

---

## 📜 3. Regras de Negócio (RN) e Diretrizes

Localizadas em [`docs/requirements/business_rules/`](/docs/requirements/business_rules/) e [`docs/business/`](/docs/business/):

* [`guia-para-preenchimento-da-regra-de-negocio.md`](/docs/requirements/business_rules/guia-para-preenchimento-da-regra-de-negocio.md): Guia oficial e tutorial para elaboração, padronização e preenchimento de Regras de Negócio com BDD (Gherkin), Metadados e Rastreabilidade.

### 📋 Especificações Detalhadas de Regras de Negócio (`business_rules/`)

| Código | Título da Regra | Módulo | Status |
| :--- | :--- | :--- | :--- |
| [`RN-AGE-006`](/docs/requirements/business_rules/RN-AGE-006_tolerancia_atraso_e_no_show.md) | **Tolerância de Atraso, No-Show e Liberação para Encaixes** | Agendamento & Atendimento | 🟢 Ativo |
| [`RN-FIN-001`](/docs/requirements/business_rules/RN-FIN-001_rateio_comissoes_profissionais.md) | **Rateio e Apuração de Comissões de Serviços e Produtos** | Financeiro & Comandas | 🟢 Ativo |
| [`RN-NOT-001`](/docs/requirements/business_rules/RN-NOT-001_disparo_lembretes_automaticos.md) | **Política e Janelas de Disparo de Lembretes Automáticos** | Mensageria & Background | 🟢 Ativo |
| [`RN-EST-005`](/docs/requirements/business_rules/RN-EST-005_precificacao_kits_promocionais.md) | **Precificação Mandatória e Desconto Mínimo de Kits Promocionais** | Loja & Estoque | 🟢 Ativo |

### 📂 Módulos de Regras Consolidadas (`docs/business/`):
* Agendamento & Grade: [`regras_negocio_agendamento.md`](/docs/business/regras_negocio_agendamento.md) (`RN-AGE`)
* Estoque & Loja: [`regras_negocio_loja_estoque.md`](/docs/business/regras_negocio_loja_estoque.md) (`RN-EST`)
* Segurança & Perfis: [`regras_negocio_seguranca_perfis.md`](/docs/business/regras_negocio_seguranca_perfis.md) (`RN-SEG`)
* Atendimento & Comandas: [`regras_negocio_atendimento_comandas.md`](/docs/business/regras_negocio_atendimento_comandas.md) (`RN-ATE`)
* Cancelamento & Reagendamento: [`regras_negocio_cancelamento_reagendamento.md`](/docs/business/regras_negocio_cancelamento_reagendamento.md) (`RN-CAN`)


