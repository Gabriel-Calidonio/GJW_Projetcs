# Walkthrough-005: Separação das Telas de Login

Implementamos a funcionalidade completa de **Separação de Login (Cliente vs Painel Administrativo)**, melhorando a segurança geral do sistema, separando as rotas públicas de clientes das rotas privadas de administração e introduzindo uma interface de login altamente premium para os funcionários e administradores.

## Alterações Realizadas

### 1. Backend & Rotas (Java)
- **`WebConfig.java`**:
  - Adicionamos a exclusão da rota de login administrativo (`/MRYnZpAsC9sp/login`) na interceptação do `AdminInterceptor`, permitindo o acesso público a essa tela.
- **`AdminInterceptor.java`**:
  - Usuários não logados que tentarem acessar rotas restritas `/MRYnZpAsC9sp/**` agora são redirecionados automaticamente para a tela de login administrativa (`/MRYnZpAsC9sp/login`) em vez da tela pública.
  - Adicionamos um bloqueio explícito a clientes normais (Perfil ID 4). Caso um cliente logado tente acessar qualquer recurso administrativo, ele é impedido e redirecionado de volta para a Home pública `/`.
- **`LoginController.java`**:
  - Mantivemos a autenticação de clientes em `GET /login` e `POST /login` direcionando-os para `/`.
  - Criamos as rotas de login administrativo `GET /MRYnZpAsC9sp/login` e `POST /MRYnZpAsC9sp/login`.
  - Na rota administrativa, incluímos validação de perfil: clientes normais são sumariamente barrados de autenticar no painel, exibindo uma mensagem de erro dedicada.
  - Criamos a rota `GET /MRYnZpAsC9sp/logout` para que funcionários possam se deslogar especificamente no painel administrativo, sendo redirecionados para a tela de login administrativa correspondente.

### 2. Frontend & Views (HTML/CSS)
- **`login.html`** (Cliente):
  - Corrigimos a tag `<form>` para postar as credenciais para a rota correta `/login`.
  - Corrigimos o atributo `name="senha"` no input de senha.
  - Inserimos blocos de feedback visual elegantes para exibir mensagens de erro (`erro`) e mensagens de sucesso (`sucesso`).
- **`admin/login.html`** (Administrador):
  - Recriamos a view de login administrativo do zero com uma **aparência dark premium**.
  - Utilizamos sombras profundas, glassmorphism com backdrop blur, gradientes circulares no fundo e acentuações douradas (`#e5c07b`) para remeter a uma barbearia clássica e sofisticada.
  - Removemos as referências de registro público ("Cadastre-se") que eram expostas indevidamente na tela administrativa.
  - Ajustamos o form para postar na rota de mascaramento administrativo `/MRYnZpAsC9sp/login`.

---

## Como Validar Manualmente

### 1. Iniciar a Aplicação
Suba a aplicação localmente:
`mvn spring-boot:run`

### 2. Fluxo Admin Sem Autenticação
1. Tente acessar diretamente a rota do painel: `/MRYnZpAsC9sp` ou `/MRYnZpAsC9sp/pedidos`.
2. Verifique se o sistema te redireciona imediatamente para o novo formulário de login dark premium em `/MRYnZpAsC9sp/login`.

### 3. Login de Cliente no Painel Administrativo
1. Acesse `/MRYnZpAsC9sp/login`.
2. Insira as credenciais de um cliente cadastrado (Perfil ID 4).
3. Verifique se o login é negado, exibindo o alerta: *"Acesso negado: esta área é restrita a administradores."*

### 4. Login de Administrador
1. Acesse `/MRYnZpAsC9sp/login`.
2. Insira as credenciais de um administrador cadastrado (ex: Perfil ID 1).
3. Verifique se o login é bem-sucedido e se você é direcionado para a rota do dashboard `/MRYnZpAsC9sp`.

### 5. Bloqueio de Cliente no Dashboard
1. Faça login como cliente na rota pública `/login`.
2. Após o login (sendo redirecionado à Home), tente forçar a URL do painel `/MRYnZpAsC9sp`.
3. Verifique se você é redirecionado de volta para `/`, impedindo o acesso à área corporativa.

