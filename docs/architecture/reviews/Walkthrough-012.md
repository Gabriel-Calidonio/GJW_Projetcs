# Walkthrough-12: Setup Wizard para Primeiro Boot

Foi implementado um **Assistente de Instalação Web (Setup Wizard)** para a primeira execução do sistema. Quando o aplicativo é iniciado sem um arquivo `.env` configurado ou sem conexão/usuário administrador no banco de dados, qualquer rota requisitada é redirecionada automaticamente para a interface amigável do assistente.

---

## 🚀 Componentes Implementados

### 1. DTO e Serviço do Assistente
- **[SetupDTO.java](/src/main/java/com/gwj/model/dataTransferObject/SetupDTO.java)**: Modela os dados transferidos nas requisições do wizard (parâmetros do MariaDB/MySQL, porta do servidor e credenciais do administrador master).
- **[SetupService.java](/src/main/java/com/gwj/service/SetupService.java)**:
  - `isConfigured()`: Detecta dinamicamente se o sistema já foi inicializado (existência do `.env`, teste de conexão e verificação de administrador na tabela `tab_usuario`).
  - `testConnection(...)`: Executa teste rápido de conexão com o banco e suporte a servidores onde o banco ainda precisa ser criado.
  - `completeSetup(...)`: Cria o banco (`CREATE DATABASE IF NOT EXISTS`), grava o arquivo físico `.env`, recarrega as configurações em tempo de execução via `AppConfig.reload()`, aplica as migrações/criação de tabelas e insere o administrador master criptografado em SHA-256.

### 2. Interceptor e Segurança de Rota
- **[SetupInterceptor.java](/src/main/java/com/gwj/controller/SetupInterceptor.java)**:
  - Se o sistema **não** estiver configurado: redireciona qualquer requisição HTTP (exceto assets estáticos e `/setup/**`) para `/setup`.
  - Se o sistema **já** estiver configurado: bloqueia o acesso a `/setup` redirecionando para a tela de login (`/MRYnZpAsC9sp/login`), agindo como trava de segurança (*Setup Lock*).
- **[WebConfig.java](/src/main/java/com/gwj/controller/WebConfig.java)**: Registra o `SetupInterceptor` com prioridade máxima (`order(1)`).
- **[GlobalAttributesAdvice.java](/src/main/java/com/gwj/controller/GlobalAttributesAdvice.java)**: Protegido para ignorar leitura de `Setting` do banco durante a rota do setup.

### 3. Tolerância a Falhas no Boot
- **[StartApplication.java](/src/main/java/com/gwj/StartApplication.java)**: As validações que tocam o banco agora rodam com tratamento resiliente. Se o banco não estiver acessível na inicialização, o Tomcat sobe normalmente para permitir que o usuário use o Setup Wizard.
- **[AppConfig.java](/src/main/java/com/gwj/AppConfig.java)**: Suporte a `AppConfig.reload()` e propriedades `volatile` para troca de credenciais a quente em tempo de execução sem reiniciar a JVM.

### 4. Controller e Interface Web
- **[SetupController.java](/src/main/java/com/gwj/controller/SetupController.java)**:
  - `GET /setup`: Renderiza a tela do assistente com diagnóstico de versão Java e permissão de escrita.
  - `POST /setup/test-db`: Endpoint AJAX para o botão "Testar Conexão".
  - `POST /setup/finish`: Endpoint AJAX para gravação e inicialização completa.
- **[wizard.html](/src/main/resources/templates/setup/wizard.html)**:
  - Interface rica com Bootstrap 5, Glassmorphism e identidade visual dourada (`#e5c07b`) consistente com o login da barbearia.
  - Wizard em 4 etapas (Início -> Banco de Dados -> Administrador -> Instalação com barra de progresso).

---

## 🧪 Testes e Validação

- Executado teste automatizado completo com Maven:
  ```bash
  ./mvnw test
  ```
  - `AgendamentoServiceTest`: 5 testes passaram.
  - `UsuarioServiceTest`: 3 testes passaram.
  - `SetupServiceTest`: 3 novos testes passaram (validações de senha e e-mail).
  - **Total:** 11 testes executados, 0 falhas, `BUILD SUCCESS`.
