# Plano de Implementação: Suporte à Instalação e Execução com PostgreSQL

Este plano detalha as alterações necessárias para permitir que o cliente escolha instalar e utilizar o **PostgreSQL** ou o **MariaDB/MySQL**, integrando a classe `ConnectionPostGreSQL.java`, o assistente de instalação web (`/setup`), os scripts de banco de dados e a camada de acesso a dados.

---

## Revisão do Usuário Necessária

> [!IMPORTANT]
> A inclusão do suporte a PostgreSQL envolve:
> 1. Adicionar o driver JDBC oficial do PostgreSQL no `pom.xml`.
> 2. Compatibilizar o gerador de queries SQL (`QueryBuilder`), o `GenericRepository` e o `SchemaValidator` para não utilizarem exclusivamente a sintaxe do MySQL (como crases `` ` `` e `AUTO_INCREMENT`/`ENGINE=InnoDB`).
> 3. Atualizar o Assistente de Instalação (`Setup Wizard`) na interface web para oferecer a opção de selecionar entre MariaDB/MySQL e PostgreSQL com suas respectivas portas padrão (3306 e 5432).

---

## Mudanças Propostas

### 1. Dependências do Projeto

#### [MODIFY] [pom.xml](/pom.xml)
- Adicionar a dependência do driver `org.postgresql:postgresql` (gerenciada pelo Spring Boot Starter Parent) para permitir que a JVM e a classe `ConnectionPostGreSQL` carreguem o driver em tempo de execução.

---

### 2. Configurações e Gerenciador de Conexão

#### [MODIFY] [AppConfig.java](/src/main/java/com/gwj/AppConfig.java)
- Adicionar leitura da propriedade `DB_DRIVER`.
- Implementar o método utilitário `isPostgreSql()` para permitir que o sistema detecte se a URL ou driver configurado aponta para PostgreSQL (`jdbc:postgresql:`).

#### [MODIFY] [ConnectionPostGreSQL.java](/src/main/java/com/gwj/model/dataAccessObject/ConnectionPostGreSQL.java)
- Aprimorar o tratamento de exceções e garantir que a obtenção da conexão use as credenciais resolvidas pelo `AppConfig`.

#### [MODIFY] [ConnectionDB.java](/src/main/java/com/gwj/model/dataAccessObject/ConnectionDB.java)
- Integrar com `ConnectionPostGreSQL`: se `AppConfig.isPostgreSql()` for verdadeiro, delegar a conexão para `ConnectionPostGreSQL.getInstance().getConnection()`.
- Dessa forma, todas as chamadas existentes em `UnitOfWork` continuam funcionando de forma 100% transparente para qualquer um dos bancos.

---

### 3. Dialeto SQL e Validação de Schema

#### [MODIFY] [QueryBuilder.java](/src/main/java/com/gwj/model/dataAccessObject/QueryBuilder.java)
- Ajustar a interpolação de nomes de tabelas e colunas para usar sintaxe compatível com ambos os bancos (evitando crases em PostgreSQL).

#### [MODIFY] [GenericRepository.java](/src/main/java/com/gwj/model/repository/GenericRepository.java)
- Ajustar cláusulas `WHERE` e consultas `SELECT` para compatibilidade ANSI.
- Adequar inserções M2M (`INSERT IGNORE` no MySQL/MariaDB vs `ON CONFLICT DO NOTHING` no PostgreSQL).

#### [MODIFY] [SettingService.java](/src/main/java/com/gwj/service/SettingService.java)
- Adequar o comando `INSERT ... ON DUPLICATE KEY UPDATE` para suportar `ON CONFLICT (chave) DO UPDATE` quando rodando em PostgreSQL.

#### [MODIFY] [SchemaValidator.java](/src/main/java/com/gwj/model/domain/factory/SchemaValidator.java)
- Tornar os métodos de criação de índices e tabelas (`ensureSettingsTableExists`, `ensureOrderTablesExist`, etc.) compatíveis com a sintaxe do PostgreSQL (`BIGSERIAL PRIMARY KEY` em vez de `BIGINT AUTO_INCREMENT`, remoção de `ENGINE=InnoDB`).

---

### 4. Scripts de Inicialização do Banco

#### [NEW] [gwj5_postgres.sql](/src/main/resources/db/gwj5_postgres.sql)
- Criar a versão PostgreSQL do script de banco de dados com:
  - Tipos de dados PostgreSQL (`SERIAL`/`BIGSERIAL`, `TIMESTAMP`, `BOOLEAN`, etc.).
  - Sem especificadores de engine MySQL (`ENGINE=InnoDB`).
  - Carga dos dados iniciais (permissões, agendamentos, serviços padrão).

---

### 5. Assistente de Instalação (Setup Wizard)

#### [MODIFY] [SetupDTO.java](/src/main/java/com/gwj/model/dataTransferObject/SetupDTO.java)
- Adicionar o campo `dbType` (valores: `"mariadb"` ou `"postgres"`, padrão `"mariadb"`).

#### [MODIFY] [SetupService.java](/src/main/java/com/gwj/service/SetupService.java)
- Em `testConnection`:
  - Se for PostgreSQL, testar a URL `jdbc:postgresql://host:port/dbName` com o driver do Postgres. Caso o banco não exista, testar conexão com o banco de manutenção padrão `postgres`.
- Em `completeSetup`:
  - Se for PostgreSQL, criar o banco de dados via SQL do PostgreSQL (`CREATE DATABASE`).
  - Gravar no `.env` o `DB_DRIVER=org.postgresql.Driver` e a URL `jdbc:postgresql://...`.
  - Executar o script `gwj5_postgres.sql`.
  - Criar o usuário administrador usando sintaxe compatível com PostgreSQL (`ON CONFLICT (email) DO UPDATE`).

#### [MODIFY] [wizard.html](/src/main/resources/templates/setup/wizard.html)
- Adicionar seleção visual no Passo 2 com botões/cards modernos:
  - 🐬 **MariaDB 10 LTS** (Recomendado - Porta 3306)
  - 🐘 **PostgreSQL 16** (Suportado - Porta 5432)
- Ao selecionar a opção de banco, atualizar dinamicamente a porta padrão no formulário e no resumo.

---

## Plano de Verificação

### Testes Automatizados
- Executar compilação do Maven para validar todas as dependências:
  ```bash
  mvn clean test-compile
  ```
- Executar os testes unitários do SetupService:
  ```bash
  mvn test -Dtest=SetupServiceTest
  ```

### Verificação Manual
- Testar a chamada do endpoint `/setup/test-db` com simulação de payloads MariaDB e PostgreSQL.
- Validar se o arquivo `.env` é gerado corretamente para ambos os tipos de banco.
- Conferir a compatibilidade do `docker-compose.postgres.yml` com as novas configurações.
