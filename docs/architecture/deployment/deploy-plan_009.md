# DP-9: Reorganização de Pastas Thymeleaf e Telas Explícitas

- **Tipo:** Deployment plan
- **Status:** closed
- **Autor:** GWJ
- **Criado em:** 2026-08-19 14:47:26
- **Labels:** Nenhuma
- **Responsáveis:** Nenhum

## Descrição

# Plano de Reorganização de Pastas Thymeleaf e Telas Explícitas

Este plano estabelece a estratégia completa para:
1. **Refinar e padronizar o diagrama de pastas** (`docs/architecture/pastas_estrutura.puml`), garantindo consistência em *lowercase*, singular/plural e separadores.
2. **Reorganizar todas as páginas do Site (público)** em submódulos claros (`auth`, `booking`, `cart`, `catalog`, `pages`, `user`, `errors`, `fragments`, `layouts`).
3. **Reorganizar e especializar as páginas do Admin**, criando páginas e formulários explícitos para cada entidade (Produtos, Serviços, Categorias, Clientes, Endereços, Agendamentos, Funcionários e Configurações), substituindo a dependência do CRUD reflexivo genérico para fins didáticos/acadêmicos.
4. **Atualizar todos os Controllers e referências do Thymeleaf** (`th:replace`, `layout:decorate`, rotas de redirecionamento).

---

## Decisões Arquiteturais e Padronização

> [!NOTE]
> **Convenções Adotadas:**
> - **Nomenclatura**: Pastas em *lowercase* e *kebab-case* para evitar inconsistências no Linux (`address`, `store-setting`, `service`).
> - **Singularidade**: Subpastas de entidades no singular (`catalog/product`, `catalog/service`, `customer/customer`, `order/booking`).
> - **Layouts & Fragmentos**: Isolamento de componentes em `site/fragments/`, `site/layouts/`, `admin/fragments/` e `admin/layouts/`.

---

## User Review Required

> [!IMPORTANT]
> **Abordagem de Migração do Admin (CRUD Genérico -> Explícito):**
> 1. As telas públicas do site serão migradas imediatamente para suas respectivas pastas em `templates/site/`.
> 2. No Admin, criaremos as páginas HTML dedicadas (`listar.html`, `form.html` ou `detalhe.html`) para cada domínio sob `templates/admin/`.
> 3. Os novos controladores Spring MVC do Admin serão criados com rotas semânticas claras (ex.: `/admin/produtos`, `/admin/servicos`, `/admin/profissionais`, etc.), simplificando o fluxo didático em relação ao `GenericViewController`.

---

## Proposed Changes

### 1. Documentação de Arquitetura

#### [MODIFY] [pastas_estrutura.puml](/docs/architecture/pastas_estrutura.puml)
- Corrigir `Address` -> `address`, `services` -> `service`, `store_setting` -> `store-setting`.
- Adicionar `dashboard/`, `fragments/` e `layouts/` tanto no `admin/` quanto no `site/`.
- Adicionar pastas explícitas para as páginas de conteúdo do site (`pages/` ou `information/`).

---

### 2. Templates do Site Público (`src/main/resources/templates/site/`)

#### [NEW / MOVE] `templates/site/layouts/main-layout.html`
- Mover de `templates/layouts/main-layout.html` e ajustar referências para `~{site/fragments/header :: ...}`.

#### [NEW / MOVE] `templates/site/fragments/`
- Mover `header.html`, `footer.html`, `product-card.html` de `templates/parts/` para `templates/site/fragments/`.

#### [NEW / MOVE] `templates/site/pages/`
- Mover `home.html`, `sobre.html`, `contato.html` de `templates/` para `templates/site/pages/`.
- Atualizar referências de layout para `layout:decorate="~{site/layouts/main-layout}"` e fragmentos `~{site/fragments/...}`.

#### [NEW / MOVE] `templates/site/auth/`
- Mover `login.html`, `cadastro.html` para `templates/site/auth/`.

#### [NEW / MOVE] `templates/site/booking/`
- Mover `servicos.html` e `meus-agendamentos.html` para `templates/site/booking/`.

#### [NEW / MOVE] `templates/site/cart/`
- Mover `carrinho.html`, `carrinho-checkout.html`, `checkout.html`, `order-confirmation.html`, `compra-confirmada.html` para `templates/site/cart/`.

#### [NEW / MOVE] `templates/site/catalog/`
- Mover `loja.html` e `single-product.html` para `templates/site/catalog/`.

#### [NEW / MOVE] `templates/site/errors/`
- Mover `error.html` para `templates/site/errors/error.html`.

---

### 3. Templates do Painel Admin (`src/main/resources/templates/admin/`)

#### [NEW / MOVE] `templates/admin/layouts/` & `templates/admin/fragments/`
- Manter/ajustar `admin/layouts/main-layout.html` e `admin/fragments/` (`header.html`, `sidebar.html`, `footer.html`, `aside.html`) com os links atualizados para os novos módulos.

#### [NEW / MOVE] `templates/admin/auth/`
- Mover `admin/login.html` para `templates/admin/auth/login.html`.

#### [NEW / MOVE] `templates/admin/dashboard/`
- Mover `admin/dashboard.html` para `templates/admin/dashboard/index.html`.

#### [NEW / MOVE] `templates/admin/order/`
- `admin/order/order/listar.html` e `admin/order/order/detalhe.html` (migrados de `admin/pedidos/`).
- `admin/order/booking/listar.html` e `admin/order/booking/detalhe.html` (gestão dedicada de agendamentos).

#### [NEW] `templates/admin/catalog/`
- `admin/catalog/product/listar.html` e `admin/catalog/product/form.html` (Gestão de Produtos).
- `admin/catalog/service/listar.html` e `admin/catalog/service/form.html` (Gestão de Serviços).
- `admin/catalog/category/listar.html` e `admin/catalog/category/form.html` (Gestão de Categorias).

#### [NEW] `templates/admin/customer/`
- `admin/customer/customer/listar.html` e `admin/customer/customer/form.html` (Gestão de Clientes).
- `admin/customer/address/listar.html` e `admin/customer/address/form.html` (Gestão de Endereços).

#### [NEW] `templates/admin/staff/`
- `admin/staff/listar.html` e `admin/staff/form.html` (Gestão de Barbeiros / Funcionários).

#### [NEW] `templates/admin/setting/`
- `admin/setting/store/form.html` (Configurações da Loja, Horários e Dias de Funcionamento).

---

### 4. Controladores Java (`src/main/java/com/gwj/controller/`)

#### [MODIFY] [Router.java](/src/main/java/com/gwj/controller/Router.java)
- Atualizar retornos para apontar para `site/pages/home`, `site/pages/sobre`, `site/pages/contato`, `site/booking/servicos`, `site/booking/meus-agendamentos`, `site/cart/carrinho`, `site/cart/checkout`, `site/cart/order-confirmation`, `site/cart/compra-confirmada`, `site/catalog/loja`, `site/catalog/single-product`.

#### [MODIFY] [LoginController.java](/src/main/java/com/gwj/controller/LoginController.java)
- Atualizar retornos de login e cadastro para `site/auth/login`, `site/auth/cadastro` e `admin/auth/login`.

#### [MODIFY] [CarrinhoController.java](/src/main/java/com/gwj/controller/CarrinhoController.java)
- Atualizar retorno para `site/cart/carrinho-checkout`.

#### [MODIFY] [AdminDashboardController.java](/src/main/java/com/gwj/controller/AdminDashboardController.java)
- Atualizar retorno para `admin/dashboard/index`.

#### [MODIFY] [AdminPedidoController.java](/src/main/java/com/gwj/controller/AdminPedidoController.java)
- Atualizar retorno para `admin/order/order/listar` e `admin/order/order/detalhe`.

#### [MODIFY] [GlobalExceptionHandler.java](/src/main/java/com/gwj/controller/GlobalExceptionHandler.java)
- Atualizar retorno para `site/errors/error`.

#### [NEW] Controladores Administrativos Dedicados (Spring MVC Direto)
- `AdminProdutoController.java` (`/admin/produtos` -> `admin/catalog/product/...`)
- `AdminServicoController.java` (`/admin/servicos` -> `admin/catalog/service/...`)
- `AdminProfissionalController.java` (`/admin/profissionais` -> `admin/staff/...`)
- `AdminClienteController.java` (`/admin/clientes` -> `admin/customer/customer/...`)
- `AdminAgendamentoController.java` (`/admin/agendamentos` -> `admin/order/booking/...`)
- `AdminSettingController.java` (`/admin/configuracoes` -> `admin/setting/store/...`)

---

## Verification Plan

### Automated Tests
- Executar compilação completa do Maven:
  ```bash
  mvn clean compile test-compile
  ```
- Executar a suíte de testes unitários existentes:
  ```bash
  mvn test
  ```

### Manual Verification
- Iniciar a aplicação Spring Boot e verificar a renderização de:
  - Home: `http://localhost:8080/`
  - Serviços: `http://localhost:8080/servicos`
  - Loja & Produto: `http://localhost:8080/loja`
  - Carrinho & Checkout: `http://localhost:8080/carrinho`
  - Login & Cadastro Site: `http://localhost:8080/login`, `http://localhost:8080/cadastro`
  - Login Admin: `http://localhost:8080/MRYnZpAsC9sp/login`
  - Dashboard Admin: `http://localhost:8080/MRYnZpAsC9sp/dashboard`
  - Navegação do Sidebar do Admin nos novos módulos dedicados.
