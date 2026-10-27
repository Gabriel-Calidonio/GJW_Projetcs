# 🧭 Matriz de Rastreabilidade de Requisitos (RF, RNF e Regras de Negócio)

Esta matriz formaliza a **rastreabilidade bidirecional** entre **Requisitos Funcionais (RF)**, **Requisitos Não-Funcionais (RNF)**, **Regras de Negócio (RN)**, **Casos de Uso (UC)** e os **Componentes de Software (Código/Spring Boot)** do sistema **GWJ (Tgo's Barbearia)**.

O documento segue as diretrizes das normas **ISO/IEC/IEEE 29148 (Requirements Engineering)** e as melhores práticas de auditoria de software corporativo e acadêmico (TCC / Projeto Integrador).

---

## 📊 Sumário Executivo de Cobertura

```
+----------------------------------------------------------------------------------------------------+
|                                    COBERTURA DE RASTREABILIDADE                                     |
+--------------------------+------------------------------+------------------------------------------+
| 6 Requisitos Funcionais  | 5 Requisitos Não-Funcionais  | 21 Regras de Negócio (7 Famílias)        |
| 100% Mapeados em Código  | 100% Vinculados a SLAs       | 100% Validadas em Camada de Domínio/UoW  |
+--------------------------+------------------------------+------------------------------------------+
| 42 Casos de Uso (UML)    | 18 Controladores & Serviços  | 14 Views Thymeleaf & Endpoints REST/AJAX |
+--------------------------+------------------------------+------------------------------------------+
```

---

## 🎯 1. Matriz de Requisitos Funcionais (RF)

| Código | Título do Requisito | Módulo | Regras de Negócio (RN) | Requisitos Não-Funcionais | Casos de Uso (Diagrama & Especificação) | Classes de Backend (Controller / Service) | Frontend / Views | Critérios de Aceite & Validação |
| :---: | :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **RF001** | **Catálogo de Kits Promocionais, Serviços e Produtos** | Loja & Serviços | [`RN-EST-01`](/docs/business/regras_negocio_loja_estoque.md#rn-est-01-valida%C3%A7%C3%A3o-e-trava-de-estoque-dispon%C3%ADvel), [`RN-EST-02`](/docs/business/regras_negocio_loja_estoque.md#rn-est-02-estrutura-de-kits-promocionais), [`RN-EST-005`](/docs/requirements/business_rules/RN-EST-005_precificacao_kits_promocionais.md), [`RN-AGE-01`](/docs/business/regras_negocio_agendamento.md#rn-age-01-ocupa%C3%A7%C3%A3o-de-m%C3%BAltiplos-blocos-consecutivos) | [`RNF001`](/docs/requirements/non_functional/RNF001_performance.md), [`RNF004`](/docs/requirements/non_functional/RNF004_usabilidade_e_responsividade.md) | `UC01`, `UC02`, `UC15` no Storefront ([`UC_STR_001`](/docs/business/use-cases/storefront/UC_STR_001_catalogo_produtos_e_detalhes.md), [`UC_STR_004`](/docs/business/use-cases/storefront/UC_STR_004_catalogo_servicos_e_disponibilidade.md)); `UC17`, `UC18` no Admin ([`UC_ADM_008`](/docs/business/use-cases/dashboard/UC_ADM_008_gerenciar_catalogo_servicos.md), [`UC_ADM_009`](/docs/business/use-cases/dashboard/UC_ADM_009_gerenciar_produtos_estoque.md)) | [`Router.java`](/src/main/java/com/gwj/controller/Router.java), [`ProdutoService.java`](/src/main/java/com/gwj/service/ProdutoService.java), [`ServicoService.java`](/src/main/java/com/gwj/service/ServicoService.java) | `site/catalog/loja.html`, `site/catalog/single-product.html`, `site/booking/servicos.html` | Exibição de preços formatados (BRL), durações corretas e filtragem estrita de itens com status ativo. |
| **RF002** | **Motor de Busca e Cálculo da Grade de Horários Livres** | Agenda & Horários | [`RN-AGE-01`](/docs/business/regras_negocio_agendamento.md#rn-age-01-ocupa%C3%A7%C3%A3o-de-m%C3%BAltiplos-blocos-consecutivos), [`RN-AGE-02`](/docs/business/regras_negocio_agendamento.md#rn-age-02-respeito-ao-hor%C3%A1rio-de-encerramento-do-expediente), [`RN-AGE-03`](/docs/business/regras_negocio_agendamento.md#rn-age-03-bloqueio-de-hor%C3%A1rios-no-passado), [`RN-AGE-04`](/docs/business/regras_negocio_agendamento.md#rn-age-04-atribui%C3%A7%C3%A3o-autom%C3%A1tica-qualquer-profissional) | [`RNF001`](/docs/requirements/non_functional/RNF001_performance.md) (Tempo de resposta < 1.2s), [`RNF002`](/docs/requirements/non_functional/RNF002_consistencia_e_concorrencia.md) | `UC16` no Storefront ([`UC_STR_004`](/docs/business/use-cases/storefront/UC_STR_004_catalogo_servicos_e_disponibilidade.md)); `UC12` no Admin ([`UC_ADM_005`](/docs/business/use-cases/dashboard/UC_ADM_005_gerenciar_agendamentos.md)) | [`AgendamentoController.java`](/src/main/java/com/gwj/controller/AgendamentoController.java), [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java) | `GET /api/agendamentos/disponibilidade` (REST/JSON) | Retorno em JSON de slots de 20 min com flag booleana de disponibilidade sem expor conflitos de agenda. |
| **RF003** | **Agendamento Online Autônomo e Prevenção de Concorrência** | Agendamento & Checkout | [`RN-AGE-03`](/docs/business/regras_negocio_agendamento.md#rn-age-03-bloqueio-de-hor%C3%A1rios-no-passado), [`RN-AGE-04`](/docs/business/regras_negocio_agendamento.md#rn-age-04-atribui%C3%A7%C3%A3o-autom%C3%A1tica-qualquer-profissional), [`RN-AGE-05`](/docs/business/regras_negocio_agendamento.md#rn-age-05-preven%C3%A7%C3%A3o-at%C3%B4mica-de-double-booking), [`RN-ATE-01`](/docs/business/regras_negocio_atendimento_comandas.md#rn-ate-01-ciclo-de-vida-do-atendimento-e-transi%C3%A7%C3%A3o-de-status), [`RN-AGE-006`](/docs/requirements/business_rules/RN-AGE-006_tolerancia_atraso_e_no_show.md) | [`RNF002`](/docs/requirements/non_functional/RNF002_consistencia_e_concorrencia.md) (Isolamento ACID), [`RNF003`](/docs/requirements/non_functional/RNF003_seguranca_e_autorizacao.md) | `UC17`, `UC18` no Storefront ([`UC_STR_005`](/docs/business/use-cases/storefront/UC_STR_005_realizar_agendamento_e_comprovante.md)); `UC11`, `UC13` no Admin ([`UC_ADM_005`](/docs/business/use-cases/dashboard/UC_ADM_005_gerenciar_agendamentos.md)) | [`Router.java`](/src/main/java/com/gwj/controller/Router.java), [`AgendamentoController.java`](/src/main/java/com/gwj/controller/AgendamentoController.java), [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java), [`UnitOfWork.java`](/src/main/java/com/gwj/service/transaction/UnitOfWork.java) | `site/cart/checkout.html`, `site/cart/order-confirmation.html` | Zero double-booking em testes de concorrência com submissões simultâneas; rollback seguro e comprovante formal gerado. |
| **RF004** | **Autenticação de Usuários e Controle Granular de Permissões** | Segurança & Acesso | [`RN-SEG-01`](/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-01-hashing-criptogr%C3%A1fico-mandat%C3%B3rio-de-senhas), [`RN-SEG-02`](/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-02-bloqueio-de-clientes-nas-rotas-administrativas), [`RN-SEG-03`](/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-03-autoriza%C3%A7%C3%A3o-granular-por-entidade-no-crud-din%C3%A2mico), [`RN-SEG-04`](/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-04-encerramento-de-sess%C3%A3o-logout) | [`RNF003`](/docs/requirements/non_functional/RNF003_seguranca_e_autorizacao.md) (SHA-256, RBAC), [`RNF005`](/docs/requirements/non_functional/RNF005_arquitetura_e_manutenibilidade.md) | `UC01`, `UC02`, `UC03` no Admin ([`UC_ADM_001`](/docs/business/use-cases/dashboard/UC_ADM_001_autenticar_no_painel_administrativo.md), [`UC_ADM_002`](/docs/business/use-cases/dashboard/UC_ADM_002_validar_sessao_e_permissoes_rbac.md), [`UC_ADM_003`](/docs/business/use-cases/dashboard/UC_ADM_003_encerrar_sessao_logout.md)); `UC19`, `UC20`, `UC22` no Storefront ([`UC_STR_006`](/docs/business/use-cases/storefront/UC_STR_006_cadastro_e_autenticacao_cliente.md)) | [`LoginController.java`](/src/main/java/com/gwj/controller/LoginController.java), [`AdminInterceptor.java`](/src/main/java/com/gwj/controller/AdminInterceptor.java), [`PasswordUtil.java`](/src/main/java/com/gwj/controller/PasswordUtil.java), [`UsuarioService.java`](/src/main/java/com/gwj/service/UsuarioService.java) | `admin/auth/login.html`, `site/auth/login.html`, `site/auth/cadastro.html` | Senhas salvas com hash SHA-256 (`{sha256}`); bloqueio de clientes em `/MRYnZpAsC9sp/**`; invalidação segura da sessão no logout. |
| **RF005** | **Módulo de Lembretes Automáticos e Notificações** | Background & Mensageria | [`RN-NOT-001`](/docs/requirements/business_rules/RN-NOT-001_disparo_lembretes_automaticos.md), [`RN-CAN-01`](/docs/business/regras_negocio_cancelamento_reagendamento.md#rn-can-01-prazo-limite-de-cancelamento-online-anteced%C3%AAncia-m%C3%ADnima) | [`RNF001`](/docs/requirements/non_functional/RNF001_performance.md), [`RNF005`](/docs/requirements/non_functional/RNF005_arquitetura_e_manutenibilidade.md) | Suporte aos fluxos `UC_STR_005` e `UC_STR_007` (Área do Cliente e Notificações) | `ReminderSchedulerService` (Cron `@Scheduled`), [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java) | Templates de Mensagem WhatsApp / E-mail | Disparo automático nas janelas de 24h e 2h antes do corte; sem duplicação de mensagens (idempotência). |
| **RF006** | **E-Commerce de Cosméticos Masculinos e Controle de Estoque** | Loja & Estoque | [`RN-EST-01`](/docs/business/regras_negocio_loja_estoque.md#rn-est-01-valida%C3%A7%C3%A3o-e-trava-de-estoque-dispon%C3%ADvel), [`RN-EST-02`](/docs/business/regras_negocio_loja_estoque.md#rn-est-02-estrutura-de-kits-promocionais), [`RN-EST-03`](/docs/business/regras_negocio_loja_estoque.md#rn-est-03-identifica%C3%A7%C3%A3o-de-comprador-cliente-vs-visitante), [`RN-EST-04`](/docs/business/regras_negocio_loja_estoque.md#rn-est-04-atomicidade-e-limpeza-de-sess%C3%A3o) | [`RNF001`](/docs/requirements/non_functional/RNF001_performance.md), [`RNF002`](/docs/requirements/non_functional/RNF002_consistencia_e_concorrencia.md) | `UC03` a `UC14` no Storefront ([`UC_STR_002`](/docs/business/use-cases/storefront/UC_STR_002_adicionar_e_gerenciar_carrinho.md), [`UC_STR_003`](/docs/business/use-cases/storefront/UC_STR_003_checkout_e_pedido_produtos.md)); `UC14`, `UC15`, `UC18` no Admin ([`UC_ADM_006`](/docs/business/use-cases/dashboard/UC_ADM_006_gerenciar_pedidos_loja.md), [`UC_ADM_009`](/docs/business/use-cases/dashboard/UC_ADM_009_gerenciar_produtos_estoque.md)) | [`CarrinhoController.java`](/src/main/java/com/gwj/controller/CarrinhoController.java), [`PedidoService.java`](/src/main/java/com/gwj/service/PedidoService.java), [`ProdutoService.java`](/src/main/java/com/gwj/service/ProdutoService.java), [`UnitOfWork.java`](/src/main/java/com/gwj/service/transaction/UnitOfWork.java) | `site/cart/carrinho.html`, `site/cart/carrinho-checkout.html`, `site/cart/compra-confirmada.html` | Baixa imediata em `tab_produto.estoque` sob transação ACID; bloqueio de checkout caso o saldo seja insuficiente; limpeza pós-venda. |

---

## ⚡ 2. Matriz de Requisitos Não-Funcionais (RNF)

| Código | Título | Categoria | Métrica / SLA / Critério | Mecanismo Técnico / Design Pattern Aplicado | RFs e Casos de Uso Impactados | Classes e Camadas Envolvidas | Estratégia de Verificação / Teste |
| :---: | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **RNF001** | **Desempenho e Eficiência** | Performance & Escalabilidade | Latência da busca de horários livres < **1.2s**; carregamento de página < **2s** sob 50 usuários simultâneos. | Índices em `tab_agendamento(data_agendamento, profissional_id)` e `tab_produto(status)`; consultas otimizadas no [`DataMapper`](/src/main/java/com/gwj/model/dao/DataMapper.java); cache de configurações. | `RF001`, `RF002`, `RF006`; `UC04`, `UC16`, `UC03` a `UC08`. | Controller, Repository, `DataMapper`, `ConnectionDB`. | Testes de carga (JMeter/K6) simulando consultas de horários simultâneas; profiling de query SQL. |
| **RNF002** | **Consistência Transacional e Anti Concorrência** | Integridade de Dados | **Zero tolerância** a agendamentos duplicados (*double-booking*) e **zero saldo negativo** em estoque físico. | Padrão **Unit of Work** com `ThreadLocal Connection` garantindo transações ACID (`commit` e `rollback` coordenados); travas atômicas via `SELECT FOR UPDATE` ou bloqueio concorrente pré-insert. | `RF002`, `RF003`, `RF006`; `UC12` (Loja), `UC17` (Agendamento). | [`UnitOfWork.java`](/src/main/java/com/gwj/service/transaction/UnitOfWork.java), [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java), [`PedidoService.java`](/src/main/java/com/gwj/service/PedidoService.java). | Testes unitários e de integração concorrentes com threads concorrentes tentando reservar a mesma cadeira. |
| **RNF003** | **Segurança, Autenticação e Autorização** | Segurança da Informação | Hashing unidirecional mandatória; 100% das rotas administrativas protegidas; prevenção de SQL Injection e XSS. | Algoritmo **SHA-256** com prefixo `{sha256}` via [`PasswordUtil`](/src/main/java/com/gwj/controller/PasswordUtil.java); Interceptador [`AdminInterceptor`](/src/main/java/com/gwj/controller/AdminInterceptor.java) inspecionando RBAC; uso exclusivo de `PreparedStatement` em JDBC. | `RF004`; `UC01`, `UC02`, `UC03` (Admin), `UC19`, `UC20`, `UC22` (Storefront). | [`AdminInterceptor.java`](/src/main/java/com/gwj/controller/AdminInterceptor.java), [`WebConfig.java`](/src/main/java/com/gwj/config/WebConfig.java), [`DataMapper.java`](/src/main/java/com/gwj/model/dao/DataMapper.java). | Testes de invasão em rotas restritas `/MRYnZpAsC9sp/*` sem sessão; verificação de hashes no banco de dados. |
| **RNF004** | **Usabilidade, Acessibilidade e Responsividade** | Usabilidade & UX | Interface 100% responsiva (Mobile First - 320px a 4K); tempo para completar um agendamento < **60 segundos**. | **Vanilla CSS** com variáveis de Design System, Flexbox, CSS Grid; sem dependência excessiva de bibliotecas pesadas; microinterações fluidas e toasts reativos. | `RF001`, `RF002`, `RF003`, `RF006`; `UC01`, `UC05`, `UC15`, `UC17`. | Templates Thymeleaf, CSS (`site/css/style.css`, `admin/css`), Scripts Vanilla JS. | Validação em Google Chrome DevTools em resoluções mobile (iPhone, Galaxy) e desktop; Lighthouse Score > 90. |
| **RNF005** | **Arquitetura, Manutenibilidade e Desacoplamento** | Engenharia de Software | Desacoplamento total entre controllers HTTP e camada de persistência; código modular e tipado. | **Java 21**, **Spring Boot 3.2.5**; Padrões **Data Mapper**, **Service Registry**, **Unit of Work**, **Generic Service/Repository**; arquitetura desacoplada documentada em [`diagrama.puml`](/docs/architecture/diagrama.puml). | Todos os RFs e UCs do sistema. | [`ServiceRegistry.java`](/src/main/java/com/gwj/service/registry/ServiceRegistry.java), [`IService.java`](/src/main/java/com/gwj/service/IService.java), [`DataMapper.java`](/src/main/java/com/gwj/model/dao/DataMapper.java). | Auditoria de código limpo (Clean Code), compilação Maven limpa sem warnings e aderência a contratos de interfaces. |

---

## 📜 3. Matriz de Regras de Negócio (RN)

As 21 Regras de Negócio do sistema encontram-se estruturadas por domínios operacionais e são rastreadas a seguir:

### ✂️ Família 1: Agendamento & Grade (`RN-AGE`)

| Código | Título da Regra | Descrição no Contexto do Sistema | RFs Relacionados | Casos de Uso | Camada / Classe de Aplicação | Tabela de Dados |
| :---: | :--- | :--- | :---: | :--- | :--- | :--- |
| **RN-AGE-01** | **Ocupação de Múltiplos Blocos Consecutivos** | A duração do serviço (`tab_servico.duracao`) define o número de blocos consecutivos de 20 minutos necessários: $N = \lceil \text{duracao} / 20 \rceil$. Todos os $N$ blocos devem estar desocupados para o horário ser válido. | `RF001`, `RF002` | `UC16` (Storefront), `UC11` (Admin) | [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java) | `tab_servico`, `tab_agendamento` |
| **RN-AGE-02** | **Respeito ao Horário de Encerramento** | O término do atendimento ($\text{horaInicio} + \text{duracao}$) não pode ultrapassar a hora de encerramento estipulada em `tab_dias_funcionamento` para aquele dia da semana. | `RF002` | `UC16` (Storefront), `UC20` (Admin) | [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java), [`AdminSettingController.java`](/src/main/java/com/gwj/controller/AdminSettingController.java) | `tab_dias_funcionamento`, `tab_setting` |
| **RN-AGE-03** | **Bloqueio de Horários no Passado** | Para a data atual ($D = \text{Hoje}$), slots com horário inferior à hora corrente do servidor ($\text{slot.hora} \le \text{LocalTime.now()}$) são sumariamente descartados. | `RF002`, `RF003` | `UC16`, `UC17` (Storefront) | [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java) | Validação em memória |
| **RN-AGE-04** | **Atribuição Automática ("Qualquer Profissional")** | Se o cliente não selecionar um barbeiro específico (`profissionalId = null` ou `0`), o motor avalia profissionais ativos (`status = true`) e aloca aquele com agenda livre para todos os blocos do serviço. | `RF002`, `RF003` | `UC16`, `UC17` (Storefront), `UC19` (Admin) | [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java), [`ProfissionalService.java`](/src/main/java/com/gwj/service/ProfissionalService.java) | `tab_profissional`, `tab_agendamento` |
| **RN-AGE-05** | **Prevenção Atômica de Double-Booking** | A gravação de um agendamento é protegida por transação atômica (`UnitOfWork`). Antes do `INSERT`, consulta se outro cliente confirmou a mesma cadeira no mesmo intervalo. Em caso de colisão, executa Rollback imediato. | `RF003` | `UC17` (Storefront), `UC11` (Admin) | [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java), [`UnitOfWork.java`](/src/main/java/com/gwj/service/transaction/UnitOfWork.java) | `tab_agendamento` |
| **RN-AGE-006** | **Tolerância de Atraso e No-Show** | Tolerância máxima de **15 minutos** de atraso presencial. Ultrapassado o limite, a recepção pode marcar o status como No-Show e liberar a cadeira para encaixes presenciais imediatos. | `RF003` | `UC11`, `UC13` (Admin) | [`AdminAgendamentoController.java`](/src/main/java/com/gwj/controller/AdminAgendamentoController.java), [`SettingService.java`](/src/main/java/com/gwj/service/SettingService.java) | `tab_agendamento`, `tab_setting` |

---

### 📦 Família 2: Estoque, Loja & Kits (`RN-EST`)

| Código | Título da Regra | Descrição no Contexto do Sistema | RFs Relacionados | Casos de Uso | Camada / Classe de Aplicação | Tabela de Dados |
| :---: | :--- | :--- | :---: | :--- | :--- | :--- |
| **RN-EST-01** | **Validação e Trava de Estoque Disponível** | É estritamente vedada a adição de quantidade superior ao saldo físico em `tab_produto.estoque`. No checkout, a baixa é executada sob transação ACID; se saldo < quantidade, aborta com Rollback. | `RF001`, `RF006` | `UC03`, `UC04`, `UC06`, `UC12` (Storefront) | [`CarrinhoController.java`](/src/main/java/com/gwj/controller/CarrinhoController.java), [`ProdutoService.java`](/src/main/java/com/gwj/service/ProdutoService.java), [`UnitOfWork.java`](/src/main/java/com/gwj/service/transaction/UnitOfWork.java) | `tab_produto` |
| **RN-EST-02** | **Estrutura de Kits Promocionais** | Kits combinados (ex: Pomada + Óleo) são modelados com precificação unificada e controle de saldo próprio, permitindo venda em lote sob um único ID. | `RF001`, `RF006` | `UC01`, `UC02` (Storefront), `UC18` (Admin) | [`ProdutoService.java`](/src/main/java/com/gwj/service/ProdutoService.java), [`Produto.java`](/src/main/java/com/gwj/model/domain/entities/Produto.java) | `tab_produto` |
| **RN-EST-03** | **Identificação de Comprador (Cliente vs. Visitante)** | Clientes autenticados têm o pedido associado automaticamente a `pedido.cliente_id`. Visitantes informam `nome_visitante` e `telefone_visitante` obrigatoriamente sem necessidade de cadastro prévio. | `RF006` | `UC10`, `UC12` (Storefront), `UC14` (Admin) | [`CarrinhoController.java`](/src/main/java/com/gwj/controller/CarrinhoController.java), [`PedidoService.java`](/src/main/java/com/gwj/service/PedidoService.java) | `tab_pedidos` |
| **RN-EST-04** | **Atomicidade e Limpeza de Sessão** | A criação do pedido (`tab_pedidos`), itens (`tab_itens_pedido`) e baixa física (`tab_produto.estoque`) ocorrem em transação atômica única. O carrinho da `HttpSession` só é esvaziado após o Commit bem-sucedido. | `RF006` | `UC12`, `UC14` (Storefront) | [`CarrinhoController.java`](/src/main/java/com/gwj/controller/CarrinhoController.java), [`UnitOfWork.java`](/src/main/java/com/gwj/service/transaction/UnitOfWork.java) | `tab_pedidos`, `tab_itens_pedido`, `tab_produto` |
| **RN-EST-005** | **Precificação Mandatória e Desconto Mínimo de Kits** | O preço de venda de um Kit Promocional deve oferecer economia comprovada de no mínimo **10%** em relação à soma individual dos cosméticos que o compõem: $\text{Preço Kit} \le 0.90 \times \sum (\text{Preços Individuais})$. | `RF001` | `UC01` (Storefront), `UC18` (Admin) | [`AdminProdutoController.java`](/src/main/java/com/gwj/controller/AdminProdutoController.java), [`ProdutoService.java`](/src/main/java/com/gwj/service/ProdutoService.java) | `tab_produto` |

---

### 🛡️ Família 3: Segurança & Perfis (`RN-SEG`)

| Código | Título da Regra | Descrição no Contexto do Sistema | RFs Relacionados | Casos de Uso | Camada / Classe de Aplicação | Tabela de Dados |
| :---: | :--- | :--- | :---: | :--- | :--- | :--- |
| **RN-SEG-01** | **Hashing Criptográfico Mandatório de Senhas** | Nenhuma senha trafega ou é gravada em texto puro. O sistema aplica obrigatoriamente hash SHA-256 persistindo o valor com prefixo `{sha256}` para verificação segura no login. | `RF004` | `UC01` (Admin), `UC19`, `UC20` (Storefront) | [`PasswordUtil.java`](/src/main/java/com/gwj/controller/PasswordUtil.java), [`Usuario.java`](/src/main/java/com/gwj/model/domain/entities/Usuario.java), [`UsuarioService.java`](/src/main/java/com/gwj/service/UsuarioService.java) | `tab_usuario` |
| **RN-SEG-02** | **Bloqueio de Clientes nas Rotas Administrativas** | Usuários com Perfil Cliente (`perfil_id = 4`) são sumariamente rejeitados pelo [`AdminInterceptor`](/src/main/java/com/gwj/controller/AdminInterceptor.java) em qualquer rota sob `/MRYnZpAsC9sp/**` e redirecionados para a Home pública (`/`). | `RF004` | `UC02` (Admin), `UC20` (Storefront) | [`AdminInterceptor.java`](/src/main/java/com/gwj/controller/AdminInterceptor.java) | Sessão HTTP (`usuarioLogado`) |
| **RN-SEG-03** | **Autorização Granular por Entidade no CRUD Dinâmico** | O acesso aos módulos operacionais do painel administrativo exige a posse da permissão específica associada ao perfil do usuário (`GERENCIAR_CLIENTES`, `GERENCIAR_SERVICOS`, `GERENCIAR_ESTOQUE`, `GERENCIAR_TODAS_AGENDAS`, `VISUALIZAR_PROPRIA_AGENDA`). | `RF004` | `UC02`, `UC04`, `UC11`, `UC16` a `UC20` (Admin) | [`AdminInterceptor.java`](/src/main/java/com/gwj/controller/AdminInterceptor.java), [`Usuario.java`](/src/main/java/com/gwj/model/domain/entities/Usuario.java) | `tab_permissao`, `tab_perfil_permissao` |
| **RN-SEG-04** | **Encerramento de Sessão (Logout)** | A chamada de logout destrói completamente o contexto da sessão através de `session.invalidate()`, prevenindo sequestro de sessão (*session hijacking*) em estações compartilhadas da barbearia. | `RF004` | `UC03` (Admin), `UC22` (Storefront) | [`LoginController.java`](/src/main/java/com/gwj/controller/LoginController.java) | Contêiner Servlet (`HttpSession`) |

---

### 💈 Família 4: Atendimento & Comandas (`RN-ATE`)

| Código | Título da Regra | Descrição no Contexto do Sistema | RFs Relacionados | Casos de Uso | Camada / Classe de Aplicação | Tabela de Dados |
| :---: | :--- | :--- | :---: | :--- | :--- | :--- |
| **RN-ATE-01** | **Ciclo de Vida do Atendimento e Transição de Status** | O agendamento segue a máquina de estados rigorosa: `Confirmado` ➔ `Em Atendimento` ➔ `Concluído` (ou `Cancelado`). Apenas agendamentos no estado `'Confirmado'` podem iniciar atendimento. | `RF003` | `UC13` (Admin), `UC17` (Storefront), `UC21` (Storefront) | [`AdminAgendamentoController.java`](/src/main/java/com/gwj/controller/AdminAgendamentoController.java), [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java) | `tab_agendamento` |
| **RN-ATE-02** | **Comanda Dinâmica (Itens e Serviços Extras)** | Durante a execução do serviço na cadeira, o barbeiro ou recepção pode vincular serviços adicionais (ex: Barboterapia) e cosméticos à comanda, recalculando o valor total devido. | `RF001`, `RF006` | `UC11`, `UC13` (Admin) | Camada de Atendimento Presencial / Pedidos | `tab_itens_pedido`, `tab_agendamento` |
| **RN-ATE-03** | **Baixa de Estoque de Balcão** | Vendas de produtos realizadas presencialmente na comanda abatem o saldo físico em `tab_produto.estoque` no ato do fechamento do caixa. | `RF006` | `UC11` (Admin) | [`ProdutoService.java`](/src/main/java/com/gwj/service/ProdutoService.java), [`UnitOfWork.java`](/src/main/java/com/gwj/service/transaction/UnitOfWork.java) | `tab_produto` |
| **RN-ATE-04** | **Rateio e Apuração de Comissões** | O percentual de comissão do barbeiro é apurado exclusivamente sobre os procedimentos executados e concluídos. Serviços cancelados ou No-Shows não geram comissão. | `RF003` | `UC11`, `UC13` (Admin) | Módulo Financeiro / [`RN-FIN-001`](/docs/requirements/business_rules/RN-FIN-001_rateio_comissoes_profissionais.md) | `tab_agendamento`, `tab_profissional` |

---

### ⏱️ Família 5: Cancelamento & Reagendamento (`RN-CAN`)

| Código | Título da Regra | Descrição no Contexto do Sistema | RFs Relacionados | Casos de Uso | Camada / Classe de Aplicação | Tabela de Dados |
| :---: | :--- | :--- | :---: | :--- | :--- | :--- |
| **RN-CAN-01** | **Prazo Limite de Cancelamento Online (Antecedência de 2h)** | O cliente só pode cancelar o agendamento de forma autônoma com antecedência mínima de **2 horas** (120 minutos) do horário agendado. Com menos de 2h, o cancelamento é bloqueado no site e exige contato com a barbearia. | `RF003`, `RF005` | `UC21` (Storefront), `UC20` (Admin) | [`Router.java`](/src/main/java/com/gwj/controller/Router.java), [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java) | `tab_agendamento`, `tab_setting` |
| **RN-CAN-02** | **Liberação Imediata de Slots na Grade Pública** | Ao persistir o cancelamento (`status = 'CANCELADO'`), todos os blocos de horário anteriormente ocupados por aquele atendimento voltam a ser computados como **Disponíveis** na API pública instantaneamente. | `RF002`, `RF003` | `UC13` (Admin), `UC16` (Storefront) | [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java) | `tab_agendamento` |
| **RN-CAN-03** | **Política de Reagendamento** | O reagendamento preserva o ID e o histórico do cliente, alterando data e horário de início/fim, desde que a nova janela atenda a todas as validações de disponibilidade de grade (`RN-AGE-01` a `RN-AGE-05`). | `RF002`, `RF003` | `UC11` (Admin), `UC21` (Storefront) | [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java) | `tab_agendamento` |

---

### 💰 Família 6: Financeiro (`RN-FIN`)

| Código | Título da Regra | Descrição no Contexto do Sistema | RFs Relacionados | Casos de Uso | Camada / Classe de Aplicação | Tabela de Dados |
| :---: | :--- | :--- | :---: | :--- | :--- | :--- |
| **RN-FIN-001** | **Rateio e Apuração de Comissões de Profissionais** | Parametrização percentual de repasse (ex: 50% em cortes e 10% na venda de cosméticos). O valor líquido da comissão é registrado ao mudar o status da comanda para `'Concluído'`. | `RF003`, `RF006` | `UC13` (Admin), `UC19` (Admin) | [`AdminProfissionalController.java`](/src/main/java/com/gwj/controller/AdminProfissionalController.java), Serviço de Fechamento de Comanda | `tab_profissional`, `tab_agendamento` |

---

### 📲 Família 7: Notificações & Lembretes (`RN-NOT`)

| Código | Título da Regra | Descrição no Contexto do Sistema | RFs Relacionados | Casos de Uso | Camada / Classe de Aplicação | Tabela de Dados |
| :---: | :--- | :--- | :---: | :--- | :--- | :--- |
| **RN-NOT-001** | **Política e Janelas de Disparo de Lembretes Automáticos** | Rotina em segundo plano dispara lembretes personalizados via WhatsApp/E-mail em duas janelas: **24 horas antes** (confirmação prévia) e **2 horas antes** (alerta de trânsito e pontualidade), com trava contra duplicidade. | `RF005` | Suporte a `UC_STR_005` e `UC_STR_007` | `ReminderSchedulerService` (TaskScheduler / Daemon), [`AgendamentoService.java`](/src/main/java/com/gwj/service/AgendamentoService.java) | `tab_agendamento` (`lembrete_enviado`) |

---

## 🔀 4. Matriz de Referência Cruzada Bidirecional (Cross-Reference)

A tabela abaixo sintetiza a relação de rastreabilidade direta entre os três pilares analíticos:

```
+-----------------------------------------------------------------------------------------------------------------------+
| RF    | Título do Requisito           | Regras de Negócio Associadas         | RNFs Relevantes  | Casos de Uso UML    |
+-------+-------------------------------+--------------------------------------+------------------+---------------------+
| RF001 | Catálogo de Kits e Produtos   | RN-EST-01, RN-EST-02, RN-EST-005,    | RNF001, RNF004   | UC01, UC02, UC15,   |
|       |                               | RN-AGE-01                            |                  | UC17, UC18          |
| RF002 | Cálculo da Grade de Horários  | RN-AGE-01, RN-AGE-02, RN-AGE-03,     | RNF001, RNF002   | UC16, UC12          |
|       |                               | RN-AGE-04                            |                  |                     |
| RF003 | Agendamento Online Autônomo   | RN-AGE-03, RN-AGE-04, RN-AGE-05,     | RNF002, RNF003   | UC17, UC18, UC11,   |
|       |                               | RN-ATE-01, RN-AGE-006, RN-CAN-01/02  |                  | UC13, UC21          |
| RF004 | Autenticação e Permissões     | RN-SEG-01, RN-SEG-02, RN-SEG-03,     | RNF003, RNF005   | UC01, UC02, UC03,   |
|       |                               | RN-SEG-04                            |                  | UC19, UC20, UC22    |
| RF005 | Lembretes Automáticos         | RN-NOT-001, RN-CAN-01                | RNF001, RNF005   | Transversal (Daemon)|
| RF006 | E-Commerce e Estoque          | RN-EST-01, RN-EST-02, RN-EST-03,     | RNF001, RNF002   | UC03 a UC14, UC14,  |
|       |                               | RN-EST-04, RN-ATE-03                 |                  | UC15, UC18          |
+-----------------------------------------------------------------------------------------------------------------------+
```

---

## 🔗 5. Navegação e Documentos Correlatos

* 📊 **Matriz de Casos de Uso (UML):** [`matriz_rastreabilidade_casos_de_uso.md`](/docs/business/use-cases/matriz_rastreabilidade_casos_de_uso.md)
* 🏛️ **Diagrama de Arquitetura do Sistema:** [`docs/architecture/diagrama.puml`](/docs/architecture/diagrama.puml)
* 📋 **Visão Geral dos Requisitos:** [`docs/requirements/README.md`](/docs/requirements/README.md)
* 📂 **Modelagem dos Casos de Uso:** [`docs/business/use-cases/README.md`](/docs/business/use-cases/README.md)
