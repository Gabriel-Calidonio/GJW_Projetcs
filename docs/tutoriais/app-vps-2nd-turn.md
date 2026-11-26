# Atualização do Projeto na VPS, pular para o passo 3?

**Sim.** Com a máquina local (Passo 1) e a VPS já configuradas na primeira implantação (MariaDB, `.env`, `tgos-barbearia.service`, Nginx, UFW e Certbot dos Passos 4, 5.2 e 6), nas atualizações seguintes você pode ir direto para o **Passo 3** e depois apenas substituir o binário na VPS e reiniciar o serviço.

Na prática, o fluxo de atualização vira:

1. **Passo 3** ([`app-vpn.md`](/docs/tutoriais/app-vpn.md#L121-L150)): rodar os testes e compilar o binário nativo localmente (`./mvnw -Pnative native:compile -DskipTests`).
2. **Substituir o binário e reiniciar** (como no **Passo 5.1**, ou automatizado pelo **Passo 7 [`deploy.sh`](/docs/tutoriais/app-vpn.md#L389-L447)**, que já inclui backup `.bak` e rollback automático).

---

### Por que você não precisa refazer o Passo 2 na maioria das mudanças

Como o projeto agora usa [`AppRuntimeHints`](/src/main/java/com/gwj/AppRuntimeHints.java) (em vez de depender só da gravação manual com `native-image-agent` descrita no Passo 2):
- **Novas entidades** em `com.gwj.model.domain.entities.*` e **novos DTOs** em `com.gwj.model.dataTransferObject.*` são descobertos e registrados automaticamente durante o `native:compile`.
- Alterações em controllers, services, regras de negócio, CSS/JS e templates Thymeleaf nativos também vão direto no Passo 3.

---

### Exceções: quando apenas pular para o Passo 3 não basta

1. **Mudança de estrutura no banco (novas tabelas/colunas)**: os scripts `db/gwj5.sql` só são executados pelo `/setup` inicial. Como [`SchemaValidator`](/src/main/java/com/gwj/model/domain/factory/SchemaValidator.java) garante apenas alguns índices/tabelas específicos no boot, novas colunas ou tabelas precisam de `ALTER TABLE`/`CREATE TABLE` no MariaDB da VPS.
2. **Novas variáveis no `.env`**: se o código novo ler alguma chave nova via [`AppConfig`](/src/main/java/com/gwj/AppConfig.java), adicione-a em `/opt/tgos-barbearia/.env` na VPS antes de reiniciar.
3. **Novas dependências/tipos acessados por reflexão**: se adicionar uma biblioteca nova sem metadata GraalVM, uma nova classe fora dos pacotes escaneados chamada via SpEL, um novo `@Autowired` de interface request/session-scoped ou um recurso fora de `templates/`, `static/` e `db/*.sql`, atualize [`AppRuntimeHints`](/src/main/java/com/gwj/AppRuntimeHints.java) antes de rodar o Passo 3.
4. **Primeira troca de rotas do Nginx ou domínio**: só exige mexer no Passo 6 se mudar domínio, portas ou limites como `client_max_body_size`.