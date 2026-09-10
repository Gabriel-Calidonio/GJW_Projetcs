# Resumo da Implementação: Suporte à Instalação com PostgreSQL

Implementamos o suporte completo à instalação e execução do sistema com **PostgreSQL**, integrando a classe [ConnectionPostGreSQL.java](/src/main/java/com/gwj/model/dataAccessObject/ConnectionPostGreSQL.java), o assistente de instalação web (`/setup`), o mapeamento de entidades e dialetos SQL.

---

## Modificações Realizadas

### 1. Dependência do Driver JDBC Oficial
- **[pom.xml](/pom.xml)**:
  - Adicionada a dependência oficial `org.postgresql:postgresql` gerenciada pelo Spring Boot.

### 2. Camada de Conexão e Configuração Dinâmica
- **[AppConfig.java](/src/main/java/com/gwj/AppConfig.java)**:
  - Incluída a propriedade dinâmica `DB_DRIVER`.
  - Criado o método utilitário `AppConfig.isPostgreSql()`, que detecta automaticamente se a URL ou driver ativo correspondem ao PostgreSQL.
- **[ConnectionDB.java](/src/main/java/com/gwj/model/dataAccessObject/ConnectionDB.java)**:
  - Quando `AppConfig.isPostgreSql()` for verdadeiro, delega a obtenção de conexão para `ConnectionPostGreSQL.getInstance().getConnection()`.
  - Mantém 100% de retrocompatibilidade com chamadas existentes no `UnitOfWork`.
- **[ConnectionPostGreSQL.java](/src/main/java/com/gwj/model/dataAccessObject/ConnectionPostGreSQL.java)**:
  - Integrado como o singleton dedicado para carregamento e conexões via `org.postgresql.Driver`.

### 3. Dialeto SQL e Gerador de Queries
- **[QueryBuilder.java](/src/main/java/com/gwj/model/dataAccessObject/QueryBuilder.java)**:
  - Sanitização de crases específicas do MySQL, tornando as cláusulas de `INSERT`, `UPDATE`, `DELETE` e `SELECT` 100% compatíveis com a sintaxe ANSI e PostgreSQL.
- **[GenericRepository.java](/src/main/java/com/gwj/model/repository/GenericRepository.java)**:
  - Ajuste nas cláusulas de inserção muitos-para-muitos (`INSERT IGNORE` no MySQL vs `ON CONFLICT DO NOTHING` no PostgreSQL).
  - Remoção de crases nas condições da cláusula `WHERE`.
- **[SettingService.java](/src/main/java/com/gwj/service/SettingService.java)**:
  - Implementado suporte à sintaxe de Upsert do PostgreSQL (`ON CONFLICT (chave) DO UPDATE SET valor = EXCLUDED.valor`).
- **[SchemaValidator.java](/src/main/java/com/gwj/model/domain/factory/SchemaValidator.java)**:
  - Métodos `ensureUniqueEmailIndex`, `ensureSettingsTableExists`, `ensureProductImageColumnAndDataExist` e `ensureOrderTablesExist` atualizados com variantes nativas do PostgreSQL (`BIGSERIAL PRIMARY KEY`, sem `ENGINE=InnoDB`).

### 4. Scripts DDL e Carga Inicial
- **[gwj5_postgres.sql](/src/main/resources/db/gwj5_postgres.sql)** e **[gwj5_postgres.sql (raiz)](/gwj5_postgres.sql)**:
  - Criado o schema completo equivalente ao `gwj5.sql`, ajustado para PostgreSQL (tipos `SERIAL`, `BIGSERIAL`, `TIMESTAMP`, `BOOLEAN`, chaves estrangeiras e sincronização de sequências pós-carga com `setval`).

### 5. Assistente de Instalação (Setup Wizard)
- **[SetupDTO.java](/src/main/java/com/gwj/model/dataTransferObject/SetupDTO.java)**:
  - Adicionado o atributo `dbType` (`mariadb` ou `postgres`).
- **[SetupService.java](/src/main/java/com/gwj/service/SetupService.java)**:
  - `testConnection`: testa a conexão PostgreSQL contra o banco alvo ou banco raiz `postgres`.
  - `completeSetup`: cria o banco de dados no PostgreSQL, grava o arquivo `.env` com `DB_DRIVER=org.postgresql.Driver` e `jdbc:postgresql://...`, executa o `gwj5_postgres.sql` e cadastra o administrador com `ON CONFLICT`.
  - `isConfigured`: verificação agnóstica de dialeto sem crases.
- **[SetupController.java](/src/main/java/com/gwj/controller/SetupController.java)**:
  - Repassa o `dbType` e seleciona as portas padrão corretas (5432 vs 3306).
- **[wizard.html](/src/main/resources/templates/setup/wizard.html)**:
  - Interface moderna com cards selecionáveis:
    - 🐬 **MariaDB / MySQL** (Recomendado - Porta 3306)
    - 🐘 **PostgreSQL** (Suportado - Porta 5432)
  - Alternância dinâmica de porta, usuário e atualização em tempo real no resumo da instalação.

---

## Verificação dos Testes

- **Compilação do Projeto**:
  - `mvn clean test-compile` finalizado com sucesso (`BUILD SUCCESS`).
- **Testes Unitários**:
  - `mvn test -Dtest=SetupServiceTest` executado com 5 testes aprovados (`BUILD SUCCESS`):
    - Validação de senha curta
    - Validação de confirmação de senha divergente
    - Validação de formato de e-mail inválido
    - Configuração de `SetupDTO` em modo PostgreSQL
    - Detecção dinâmica de PostgreSQL no `AppConfig`
