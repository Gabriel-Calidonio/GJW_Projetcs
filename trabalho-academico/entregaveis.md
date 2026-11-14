# Entregáveis Finais, Projeto Integrador (1ª e 2ª Fases)

**Projeto:** Tgo's Barbearia, Sistema Integrado de Agendamento Autônomo, E-Commerce e Gestão Empresarial Dinâmica (`GWJ Java Spring Boot Dinâmico`)  
**Equipe:** GJW Projects  
**Datas de Apresentação:** 17 e 24 de novembro de 2026 (duração máxima: 15 minutos por grupo)

---

## 1. Identificação do Grupo e Links Oficiais

| Campo | Informação |
| :--- | :--- |
| **Instituição / Disciplina** | Fatec Ferraz de Vasconcelos / Programação Web / Projeto Integrador *(confirmar se há alteração)* |
| **Integrantes** | • **Gabriel Calidonio André** *(Product Owner / Desenvolvimento)*<br>• **Josias da Conceição Sobrinho** *(Scrum Master / Arquitetura e Desenvolvimento)*<br>• **Wallace Francis Miranda** *(Desenvolvimento)*<br>• *(Adicionar 4º/5º integrante caso exigido pela turma)* |
| **Repositório GitHub** | https://github.com/Gabriel-Calidonio/GJW_Projetcs |
| **URL do Sistema Publicado (Front-end + Back-end)** | `[PREENCHER URL PÚBLICA DE PRODUÇÃO, ex.: https://seu-dominio.com]` |
| **URL do Protótipo de Validação UX/UI (Referência)** | https://tgos-barbearia.lovestoblog.com/templates/home.php *(protótipo inicial citado na documentação FETEPS)* |
| **README Principal** | [README.md](../README.md) |
| **Scripts de Banco de Dados** | • MariaDB / MySQL: [gwj5.sql](../gwj5.sql) (ou [src/main/resources/db/gwj5.sql](../src/main/resources/db/gwj5.sql))<br>• PostgreSQL: [gwj5_postgres.sql](../gwj5_postgres.sql) (ou [src/main/resources/db/gwj5_postgres.sql](../src/main/resources/db/gwj5_postgres.sql))<br>• Usuário SQL: [gwj_criacao_usuario.sql](../gwj_criacao_usuario.sql) |

---

## 2. Mapeamento dos Requisitos e Critérios de Avaliação

Abaixo está o cruzamento completo entre os requisitos exigidos em [descricao-atividade.md](./descricao-atividade.md) e o que foi implementado no projeto:

### 2.1. Back-end e Arquitetura em Camadas
* **Stack:** **Java 21 LTS** + **Spring Boot 3.2.5** + **Maven** ([pom.xml](../pom.xml)) + Banco Relacional (**MariaDB / MySQL 8+** via `mariadb-java-client 3.3.3` e suporte a **PostgreSQL** via driver `postgresql`).
* **Organização das Camadas (`src/main/java/com/gwj/`):**
  * **Controller (`controller/`):** Camada de entrada HTTP/REST e rotas MVC (`JsonController`, `AgendamentoController`, `CarrinhoController`, `SetupController`, `LoginController`, `Router`, `AdminDashboardController`, `AdminAgendamentoController`, `AdminClienteController`, `AdminPedidoController`, `AdminProdutoController`, `AdminProfissionalController`, `AdminServicoController`, `AdminSettingController`, `HoneypotController`).
  * **Service (`service/`):** Regras de negócio, cálculo de slots sem colisão e transações ACID (`AgendamentoService`, `GenericService`, `SettingService`, `SetupService`, `UsuarioService`, `ServiceRegistry`, `UnitOfWork`).
  * **Repository / DAO (`model/dataAccessObject/`):** Persistência relacional desacoplada de ORM pesado, implementada via **JDBC puro + Data Mapper + Reflection** (`ConnectionDB`, `ConnectionPostGreSQL`, `DataMapper`, `QueryBuilder`).
  * **Model / Entity & DTO (`model/domain/entities/` e `model/dataTransferObject/`):** 14 entidades que implementam `IEntity` (`Agendamento`, `Cliente`, `DiasFuncionamento`, `Endereco`, `GradeHorarios`, `ItemPedido`, `Pedido`, `Perfil`, `Permissao`, `Produto`, `Profissional`, `Servico`, `Setting`, `Usuario`), além de DTOs de entrada/saída (`SetupDTO`, `UsuarioDTO`, `ClienteDTO`, `EntityMapper`, `DtoMapper`).
* **Padrões de Projeto Aplicados:** Singleton (conexão JDBC e `ServiceRegistry`), Factory (`SimpleObjectFactory`), Data Mapper, Query Builder, Unit of Work (`ThreadLocal`), Decorator/Template (Thymeleaf Layout Dialect) e Interceptor (`SetupInterceptor`, `AdminInterceptor`, `GlobalExceptionHandler`).

### 2.2. Front-end e Integração com a API
* **Tecnologias Front-end:** **Thymeleaf + Thymeleaf Layout Dialect 3.0.0** (templates modulares reutilizáveis em `src/main/resources/templates/site/` e `admin/`), **HTML5**, **CSS3** (`style.css`, `styles-admin.css`, Bootstrap 5 no Setup Wizard) e **JavaScript ES6+ (`fetch()` API)** em `src/main/resources/static/js/`.
  > *Observação para a banca:* Por ser uma aplicação Spring Boot MVC integrada, o front-end utiliza renderização estruturada em layouts/fragmentos Thymeleaf somada ao consumo assíncrono via `fetch()` em JSON contra os endpoints REST do back-end (como alternativa equivalente aprovada ou arquitetura full-stack integrada).
* **Componentização e Reaproveitamento:**
  * Layouts base (`site/layouts/`, `admin/layouts/`) e fragmentos isolados (`site/fragments/header.html`, `footer.html`, `admin/fragments/sidebar.html`, `header.html`).
  * Scripts JS desacoplados por responsabilidade: [create.js](../src/main/resources/static/js/create.js), [update.js](../src/main/resources/static/js/update.js), [delete.js](../src/main/resources/static/js/delete.js), [validation.js](../src/main/resources/static/js/validation.js), [masks.js](../src/main/resources/static/js/masks.js) e [automatic-address.js](../src/main/resources/static/js/automatic-address.js) (integração com API externa ViaCEP).
* **Responsividade e Usabilidade:** Design system escuro com destaques dourados adaptado para desktop e dispositivos móveis, feedback visual de horários disponíveis/ocupados em [servicos.html](../src/main/resources/templates/site/booking/servicos.html), modais de confirmação de exclusão e máscaras automáticas de telefone/CEP.

### 2.3. Validações e Tratamento de Erros
* **Front-end:** Validação de formulários HTML5 + classes Bootstrap (`was-validated`, `is-invalid`) e conferência de confirmação de senha em [validation.js](../src/main/resources/static/js/validation.js); bloqueio de envio de formulários inválidos e tratamento de respostas `!response.ok` nas chamadas `fetch()` com mensagens de sucesso/erro ao usuário.
* **Back-end:**
  * [GlobalExceptionHandler.java](../src/main/java/com/gwj/controller/GlobalExceptionHandler.java) (`@ControllerAdvice`) captura `NoHandlerFoundException` (404), `NoResourceFoundException` (404 de assets) e `Exception` genérica (500), renderizando página amigável em [error.html](../src/main/resources/templates/site/errors/error.html).
  * Validações de regras de negócio no `AgendamentoService` (impedindo agendamentos no passado, fora do horário de funcionamento ou com sobreposição de intervalos) e no `SetupService` (validação de e-mail, tamanho/confirmação de senha e teste de conexão JDBC).

### 2.4. Autenticação, Segurança e Controle de Acesso (RBAC)
* **Login e Cadastro:** [LoginController.java](../src/main/java/com/gwj/controller/LoginController.java) com rotas públicas (`/login`, `/cadastro`, `/logout`) e administrativas separadas (`/${admin.route.prefix}/login`, `/${admin.route.prefix}/logout`), utilizando hash **SHA-256** em [PasswordUtil.java](../src/main/java/com/gwj/controller/PasswordUtil.java).
* **Proteção de Rotas e Permissões (RBAC):** [AdminInterceptor.java](../src/main/java/com/gwj/controller/AdminInterceptor.java) e [WebConfig.java](../src/main/java/com/gwj/controller/WebConfig.java) protegem todas as rotas administrativas contra usuários não autenticados, bloqueiam acesso de clientes (`Perfil 4`) ao painel e validam permissões granulares por módulo (`GERENCIAR_CLIENTES`, `GERENCIAR_SERVICOS`, `GERENCIAR_ESTOQUE`, `GERENCIAR_TODAS_AGENDAS`, etc.).
* **Segurança Ativa e Ofuscamento:**
  * Rota administrativa ofuscada dinamicamente via `.env` (`ADMIN_ROUTE_PREFIX`, padrão `MRYnZpAsC9sp`, customizável no Setup Wizard).
  * **Honeypot:** [HoneypotController.java](../src/main/java/com/gwj/controller/HoneypotController.java) responde `403 Forbidden` imediato em `/admin` e `/admin/**`.
  * **Trava de Instalação:** [SetupInterceptor.java](../src/main/java/com/gwj/controller/SetupInterceptor.java) bloqueia `/setup/**` após a instalação concluída.
  * Prevenção contra **SQL Injection** com uso de `PreparedStatement` parametrizado no `DataMapper` e `QueryBuilder`.

---

## 3. Documentação dos Endpoints (API REST e Rotas Principais)

### 3.1. API REST Genérica de CRUD ([JsonController.java](../src/main/java/com/gwj/controller/JsonController.java))
> Opera dinamicamente sobre entidades do domínio utilizando `SimpleObjectFactory`, `EntityMapper`, `DtoMapper` e `ServiceRegistry`.

| Método HTTP | Endpoint | Parâmetros | Descrição | Códigos HTTP |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/create-json` | `entity` (query/form) + campos da entidade no corpo | Cadastra novo registro no banco | `200 OK`, `400 Bad Request`, `500 Internal Server Error` |
| `GET` | `/read-json` | `entity` (obrigatório) + filtros opcionais (ex.: `id`, `nome`) | Lista todos ou consulta por identificador/filtro | `200 OK`, `400 Bad Request`, `500 Internal Server Error` |
| `POST` | `/update-json` | `entity` e `id` + campos atualizados | Atualiza registro existente *(ver item 6 do checklist quanto a PUT/PATCH)* | `200 OK`, `400 Bad Request`, `500 Internal Server Error` |
| `DELETE` | `/delete-json` | `entity` e `id` | Exclui registro pelo identificador | `200 OK`, `400 Bad Request`, `500 Internal Server Error` |

### 3.2. Endpoints REST/JSON de Domínio e Utilitários

| Método HTTP | Endpoint | Controller | Descrição |
| :--- | :--- | :--- | :--- |
| `GET` | `/api/agenda/horarios-disponiveis` *(aliases: `/api/agendamento/...`, `/api/agendamentos/...`)* | `AgendamentoController` | Retorna lista JSON de slots de 20 min com status de disponibilidade (`servicoId`, `profissionalId`, `data`) |
| `POST` | `/carrinho/adicionar` | `CarrinhoController` | Adiciona produto ao carrinho na sessão e retorna JSON com totais (`sucesso`, `quantidadeTotal`, `valorTotal`) |
| `POST` | `/carrinho/atualizar` | `CarrinhoController` | Atualiza quantidade de item no carrinho e retorna JSON atualizado |
| `POST` | `/carrinho/remover` | `CarrinhoController` | Remove item do carrinho e retorna JSON atualizado |
| `POST` | `/carrinho/limpar` | `CarrinhoController` | Esvazia o carrinho da sessão |
| `POST` | `/setup/test-db` | `SetupController` | Recebe JSON (`SetupDTO`), testa conexão JDBC (MariaDB/MySQL/PostgreSQL) e retorna JSON |
| `POST` | `/setup/finish` | `SetupController` | Recebe JSON (`SetupDTO`), cria banco/tabelas, grava `.env`, cria superusuário e retorna `redirectUrl` |
| `ANY` | `/admin`, `/admin/**` | `HoneypotController` | Armadilha de segurança que retorna `403 Forbidden` |

### 3.3. Principais Rotas MVC (Públicas e Painel Administrativo)
* **Vitrine Pública (`Router`, `AgendamentoController`, `CarrinhoController`, `LoginController`):** `/`, `/home`, `/servicos`, `/loja`, `/single-product`, `/sobre-nos`, `/contato`, `/carrinho`, `/checkout`, `/checkout/confirmar`, `/carrinho/checkout/confirmar`, `/pedidos`, `/meus-agendamentos`, `/login`, `/cadastro`, `/logout`.
* **Setup Inicial:** `/setup`.
* **Painel Administrativo (`/${ADMIN_ROUTE_PREFIX}`):** `/login`, `/logout`, `/` (Dashboard), `/clientes/**`, `/produtos/**`, `/servicos/**`, `/profissionais/**`, `/agendamentos/**`, `/pedidos/**`, `/configuracoes/**`.

---

## 4. Banco de Dados e Documentação Visual

* **Principais Tabelas (`tab_*`):** `tab_usuario`, `tab_perfil`, `tab_permissao`, `tab_perfil_permissao`, `tab_cliente`, `tab_profissional`, `tab_endereco`, `tab_servico`, `tab_produto`, `tab_agendamento`, `tab_pedido`, `tab_item_pedido`, `tab_dias_funcionamento`, `tab_grade_horarios`, `tab_setting`.
* **Diagramas Disponíveis no Repositório:**
  * Modelo Entidade-Relacionamento Estendido (EER): [EER Diagram - GWJ Tgo's Barbearia.svg](../docs/architecture/EER%20Diagram%20-%20GWJ%20Tgo's%20Barbearia.svg)
  * Arquitetura do Sistema: [Diagrama de Arquitetura - GWJ Dinâmico (Proposta).svg](../docs/architecture/Diagrama%20de%20Arquitetura%20-%20GWJ%20Dinâmico%20(Proposta).svg)
  * Diagrama de Classes: [diagramasClasse.svg](../docs/diagramas/diagramasClasse.svg) e [diagramasClassesDominio.svg](../docs/diagramas/diagramasClassesDominio.svg)

---

## 5. Qualidade, Testes Automatizados e Execução

### 5.1. Suíte de Testes (33 testes passando via `mvn test`)
* `UsuarioServiceTest` (3 testes): regras de serviço de usuários.
* `SetupServiceTest` (9 testes): validações do Setup Wizard, detecção PostgreSQL/MariaDB, persistência de superusuário e sanitização da rota administrativa.
* `AgendamentoServiceTest` (5 testes + validação arquitetural ArchUnit das entidades `IEntity`): cálculo de slots de 20 min e prevenção de colisão.
* `LoginControllerTest` (15 testes com `MockMvc`): fluxo de login cliente/admin, bloqueio de acesso indevido, anti-enumeração e logout.
* `AgendamentoVideoE2ETest` (1 teste E2E com Playwright): navegação ponta a ponta gravando vídeo do fluxo de agendamento.
* Ferramentas adicionais no `pom.xml`: **Spotless** (formatação Google Java Format) e **SpotBugs** (análise estática).

### 5.2. Instruções Rápidas de Execução Local
1. **Validar ambiente (Java 21, portas e banco):**
   ```bash
   ./check.sh   # Linux/macOS (ou check.bat no Windows)
   ```
2. **Executar a aplicação (porta padrão `8089`):**
   ```bash
   ./mvnw spring-boot:run
   ```
3. **Instalação guiada:** No primeiro acesso sem `.env` configurado, acessar `http://localhost:8089/setup`, testar a conexão com o banco (MariaDB/MySQL ou PostgreSQL), definir administrador e rota ofuscada do painel.
4. **Executar testes automatizados:**
   ```bash
   ./mvnw test
   ```

---

## 6. Checklist Pré-Apresentação (Pendências a Conferir pelo Grupo)

Antes dos dias **17 e 24 de novembro**, verifiquem os pontos abaixo para garantir nota máxima nos critérios de avaliação:

- [ ] **Deploy em Produção (`0,5 pt`):** Subir a aplicação em servidor/VPS ou nuvem acessível externamente (ex.: conforme [estudo_hospedagem_viabilidade.md](../docs/documentos_para_faculdade.md/estudo_hospedagem_viabilidade.md)) e substituir o campo `[PREENCHER URL PÚBLICA DE PRODUÇÃO]` na Seção 1 e no `README.md`.
- [ ] **Verbos `PUT`/`PATCH` na API REST:** Na descrição da atividade consta *"Uso adequado de GET, POST, PUT/PATCH e DELETE"*. Hoje o `JsonController.java` mapeia atualização via `@PostMapping("/update-json")`. Caso o professor teste literalmente `PUT` ou `PATCH` no Postman/Insomnia, recomenda-se adicionar `@RequestMapping(value = "/update-json", method = {RequestMethod.POST, RequestMethod.PUT, RequestMethod.PATCH})` (ou criar endpoints RESTful padrão `/api/...`) antes da apresentação.
- [ ] **Commits Individuais no GitHub (`0,5 pt`):** Pelo histórico local (`git shortlog -sn`), quase todos os commits estão atribuídos a uma única conta (`JosiasWattrelos`). Recomenda-se que **Gabriel** e **Wallace** realizem commits/PRs identificáveis com suas contas do GitHub (ex.: documentação, ajustes de front-end, testes ou deploy) para comprovar a participação colaborativa no repositório.
- [ ] **Atualização do `README.md` (`0,25 pt`):** Incluir no [README.md](../README.md) a tabela com nomes completos dos integrantes, link da aplicação publicada, resumo dos endpoints da API REST e arquitetura do projeto.
- [ ] **Documentação da API (Postman / Swagger):** Preparar uma coleção do Postman/Insomnia exportada (ou adicionar Swagger/OpenAPI) para demonstrar os endpoints ao vivo durante a apresentação.

