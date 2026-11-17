# Compilando

### O que foi implementado

1. **[AppRuntimeHints.java](file:///var/www/html/GJW_Projetcs/src/main/java/com/gwj/AppRuntimeHints.java)**
   - **Varredura automática no build AOT**: usa `ClassPathScanningCandidateComponentProvider` nos pacotes `com.gwj.model.domain.entities` e `com.gwj.model.dataTransferObject`, registrando reflexão completa (`PUBLIC_FIELDS`, `DECLARED_FIELDS`, `INVOKE_PUBLIC_CONSTRUCTORS`, `INVOKE_DECLARED_CONSTRUCTORS`, `INVOKE_PUBLIC_METHODS`, `INVOKE_DECLARED_METHODS`) para todas as entidades e DTOs (novas entidades adicionadas futuramente também serão descobertas automaticamente).
   - **Tipos explícitos de domínio e configuração**: registra reflexão para `IEntity`, `PerfilTipo`, `Carrinho`, `CarrinhoItem` e `AppConfig`.
   - **Drivers JDBC (`Class.forName`)**: registra `org.mariadb.jdbc.Driver`, `org.postgresql.Driver` e `com.mysql.cj.jdbc.Driver` (via `registerTypeIfPresent`).
   - **Recursos de classpath**: registra `db/*.sql` (para o `/setup` em [`SetupService`](file:///var/www/html/GJW_Projetcs/src/main/java/com/gwj/service/SetupService.java#L299-L314)), `application.properties` e `META-INF/services/java.sql.Driver`.
   - **Serialização de sessão**: registra `Carrinho`, `CarrinhoItem` e `Produto`.

2. **[StartApplication.java](file:///var/www/html/GJW_Projetcs/src/main/java/com/gwj/StartApplication.java#L8-L10)**
   - Adicionada a anotação `@ImportRuntimeHints(AppRuntimeHints.class)`.

3. **[AppRuntimeHintsTest.java](file:///var/www/html/GJW_Projetcs/src/test/java/com/gwj/AppRuntimeHintsTest.java)**
   - Valida via `RuntimeHintsPredicates` o registro de todas as 14 entidades, DTOs, objetos de domínio/sessão, drivers MariaDB/PostgreSQL e scripts `db/gwj5.sql` / `db/gwj5_postgres.sql`.

---

### Verificações executadas

- **Testes unitários (suíte sem banco), Spotless e SpotBugs** (com JDK `21.0.2-graalce`):
  ```bash
  ./mvnw clean test -Dtest="AppRuntimeHintsTest,LoginControllerTest,SetupServiceTest" spotless:check spotbugs:check
  ```
  - **29 testes executados** (`LoginControllerTest`, `AppRuntimeHintsTest`, `SetupServiceTest`), **0 falhas, 0 erros**. *(Obs.: `AgendamentoServiceTest` e `UsuarioServiceTest` não foram incluídos nesta execução filtrada porque dependem de conexão local com o MariaDB configurada com senha.)*
  - `spotless:check`: **73 arquivos limpos**.
  - `spotbugs:check`: **0 bugs / 0 erros**.
- **Processamento AOT** (`./mvnw -Pnative spring-boot:process-aot -DskipTests`, executado antes do `clean` final): concluiu com sucesso e gerou os arquivos `reflect-config.json`, `resource-config.json` e `serialization-config.json` com todas as entidades, DTOs e padrões `db/*.sql`.

---

### Pontos de atenção ao gerar e testar o binário nativo (`native:compile`)

O comando completo `./mvnw -Pnative native:compile -DskipTests` ainda não foi executado. Ao testá-lo, observe dois pontos:
1. **`thymeleaf-layout-dialect:3.0.0` ([pom.xml](file:///var/www/html/GJW_Projetcs/pom.xml#L29-L34))**: essa dependência puxa `org.codehaus.groovy:groovy:3.0.8` e não inclui metadados `META-INF/native-image`. Se o binário nativo falhar ao renderizar templates com `layout:decorate`, será necessário complementar com hints gerados pelo `native-image-agent` para as classes do Groovy/dialect ou migrar os layouts para fragmentos nativos do Thymeleaf (`th:replace`).
2. **Prefixo dinâmico do painel admin (`ADMIN_ROUTE_PREFIX`)**: confirme nos testes do binário compilado se o valor definido no `.env` em produção é aplicado corretamente às rotas anotadas com `${admin.route.prefix:...}`.