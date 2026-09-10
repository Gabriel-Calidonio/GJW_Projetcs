# DP-5: Separação das Telas de Login (Cliente vs Dashboard Administrativo)

- **Tipo:** Deployment plan
- **Status:** closed
- **Autor:** GWJ
- **Criado em:** 2026-06-16 19:18:59
- **Labels:** Nenhuma
- **Responsáveis:** Nenhum

## Descrição

# Separação das Telas de Login (Cliente vs Dashboard Administrativo)

Separar a experiência de login do cliente da área administrativa do dashboard para melhor segurança e clareza de uso. O login do cliente continuará na rota pública `/login` utilizando a view estilizada `login.html`, enquanto o login administrativo será movido para `/MRYnZpAsC9sp/login` utilizando a view dedicada `admin/login.html`.

## User Review Required

> [!IMPORTANT]
> **Fluxo de Autenticação Admin:**
> - A rota `/MRYnZpAsC9sp/login` será pública para permitir a inserção de credenciais de administradores, recepcionistas e barbeiros, sendo expressamente excluída da interceptação padrão do `AdminInterceptor`.
> - Clientes normais (Perfil ID 4) que tentarem fazer login na área administrativa receberão uma mensagem de erro indicando que a área é restrita a administradores.
> - Se um usuário administrativo tentar acessar qualquer rota sob `/MRYnZpAsC9sp/**` sem estar logado, ele será redirecionado para `/MRYnZpAsC9sp/login` (em vez do login de cliente público).
> - Se um cliente normal (Perfil ID 4) tentar acessar o painel administrativo, ele será redirecionado para a home pública `/`.

## Proposed Changes

### Backend Controllers & Interceptors

#### [MODIFY] [LoginController.java](/src/main/java/com/gwj/controller/LoginController.java)
- Adaptar o `GET /login` e `POST /login` para direcionar clientes logados para `/` e administradores/funcionários logados para `/MRYnZpAsC9sp`.
- Adicionar a rota `GET /MRYnZpAsC9sp/login` para exibir a tela de login do painel.
- Adicionar a rota `POST /MRYnZpAsC9sp/login` para processar a autenticação de administrador, garantindo que perfis de clientes (Perfil ID 4) sejam bloqueados com um aviso.
- Adicionar a rota `GET /MRYnZpAsC9sp/logout` para deslogar a sessão e redirecionar para a tela de login administrativa.

#### [MODIFY] [AdminInterceptor.java](/src/main/java/com/gwj/controller/AdminInterceptor.java)
- Alterar o redirecionamento de usuários não autenticados para `/MRYnZpAsC9sp/login` em vez do público `/login`.
- Adicionar um bloqueio explícito para clientes logados (Perfil ID 4), redirecionando-os de volta para a Home `/`.

#### [MODIFY] [WebConfig.java](/src/main/java/com/gwj/controller/WebConfig.java)
- Adicionar exclusão para a rota `/MRYnZpAsC9sp/login` no registro do `AdminInterceptor`.

---

### Frontend Views & Layouts

#### [MODIFY] [login.html](/src/main/resources/templates/login.html)
- Corrigir a tag `<form>` para submeter via `POST` à rota `/login`.
- Mapear o campo de senha com `name="senha"` para bater com os parâmetros do Controller.
- Inserir blocos de notificação para erros (`erro`) e cadastro efetuado com sucesso (`sucesso`).

#### [MODIFY] [login.html](/src/main/resources/templates/admin/login.html)
- Apontar o formulário de login para `th:action="@{/MRYnZpAsC9sp/login}"`.
- Remover a opção de cadastro público (clientes não devem se registrar como administradores).
- Garantir a exibição correta dos alertas de erros enviados pelo controller de login administrativo.

## Verification Plan

### Automated Tests
- Compilar o projeto e rodar testes de integridade: `mvn clean test`

### Manual Verification
1. Tentar acessar `/MRYnZpAsC9sp` sem estar logado. Validar redirecionamento automático para `/MRYnZpAsC9sp/login`.
2. Fazer login como cliente normal (Perfil ID 4) em `/MRYnZpAsC9sp/login`. Garantir que o acesso seja negado com o erro "Acesso negado: esta área é restrita a administradores."
3. Fazer login como administrador (Perfil ID 1) em `/MRYnZpAsC9sp/login`. Garantir o sucesso do login e redirecionamento para o dashboard administrativo.
4. Tentar acessar o painel administrativo `/MRYnZpAsC9sp` após logado como cliente via `/login`. Validar redirecionamento para `/` impedindo o acesso.
5. Clicar em "Sair" a partir do painel e verificar o redirecionamento de volta para `/MRYnZpAsC9sp/login`.

# Tarefas para Separação das Telas de Login

- `[x]` Configurar exclusão da rota de login administrativo no `WebConfig.java`
- `[x]` Ajustar rotas de redirecionamento no `AdminInterceptor.java`
- `[x]` Implementar rotas e validações em `LoginController.java`
- `[x]` Corrigir formulário e avisos de erro em `login.html` (Cliente)
- `[x]` Ajustar formulário de login em `admin/login.html` (Administrador)
- `[x]` Compilar a aplicação e rodar testes unitários/integração (`mvn clean test`)
