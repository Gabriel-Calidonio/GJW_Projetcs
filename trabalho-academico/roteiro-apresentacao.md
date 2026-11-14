# Roteiro de Apresentação do Projeto Integrador (1ª e 2ª Fases)

**Projeto:** Tgo's Barbearia (`GWJ Java Spring Boot Dinâmico`)  
**Datas:** 17 e 24 de novembro de 2026  
**Tempo Total:** Máximo **15 minutos** (Roteiro planejado para **12 min 30 s** + **2 min 30 s** para questionamentos da banca)  
**Integrantes:** Gabriel Calidonio André, Josias da Conceição Sobrinho e Wallace Francis Miranda

---

## Divisão Equilibrada por Blocos (Cronograma)

| Bloco | Tempo | Tema (Critérios da Banca) | Responsável Sugerido |
| :---: | :---: | :--- | :--- |
| **1** | 00:00 – 02:00 (2 min) | Contexto, Problema, Objetivo e Stack Tecnológica | **Gabriel** |
| **2** | 02:00 – 04:30 (2m30s) | Arquitetura do Back-end, Banco Relacional e API REST | **Josias** |
| **3** | 04:30 – 07:00 (2m30s) | Demonstração do Front-end (Vitrine, Agendamento e E-commerce) | **Wallace** |
| **4** | 07:00 – 10:00 (3 min) | Autenticação, Segurança (RBAC) e CRUD Completo no Painel Admin | **Gabriel & Wallace** |
| **5** | 10:00 – 12:00 (2 min) | Integração API REST + Banco ao Vivo, Validações e Deploy | **Josias** |
| **6** | 12:00 – 12:30 (30 s) | Testes Automatizados, GitHub e Encerramento | **Todos (Gabriel fecha)** |
| **7** | 12:30 – 15:00 (2m30s) | Perguntas e Respostas da Banca | **Todos** |

---

## Roteiro Detalhado Minuto a Minuto

### Bloco 1: Contexto, Objetivo e Visão Geral (00:00 – 02:00) | **Gabriel**
* **O que mostrar na tela:** Página inicial publicada (`/` ou `/home`) + Diagrama de Arquitetura ([Diagrama de Arquitetura](../docs/architecture/Diagrama%20de%20Arquitetura%20-%20GWJ%20Dinâmico%20(Proposta).svg)).
* **Pontos-chave da fala:**
  1. Apresentar o grupo (**GJW Projects**) e o cliente real do estudo de caso (**Tgo's Barbearia**).
  2. **O Problema:** Barbearias que dependem de agendamentos manuais via WhatsApp/papel sofrem com respostas lentas, choques de horários (*double-booking*), falta de controle de estoque e ausência de histórico financeiro.
  3. **O Objetivo:** Reunir na mesma plataforma web o **agendamento autônomo em tempo real**, **loja virtual de produtos masculinos (e-commerce)** e **painel administrativo com controle de acesso por perfil (RBAC)**.
  4. **Resumo da Stack:** Back-end em **Java 21 + Spring Boot 3.2.5**, Banco **MariaDB/MySQL** (com compatibilidade PostgreSQL), e Front-end integrado com **Thymeleaf (Decorator/Fragments) + HTML5/CSS3 + JavaScript (Fetch API)**.

---

### Bloco 2: Back-end, Banco de Dados e API REST (02:00 – 04:30) | **Josias**
* **O que mostrar na tela:** Estrutura de pacotes na IDE (`controller/`, `service/`, `model/dataAccessObject/`, `model/domain/entities/`) + Diagrama EER ([EER Diagram](../docs/architecture/EER%20Diagram%20-%20GWJ%20Tgo's%20Barbearia.svg)) + `JsonController.java` / `AgendamentoController.java`.
* **Pontos-chave da fala:**
  1. **Arquitetura em Camadas:**
     * **Controller:** Recebe requisições HTTP/REST (`JsonController`, `AgendamentoController`, `CarrinhoController`, `Admin*Controller`).
     * **Service:** Regras de negócio, cálculo de slots de 20 minutos e controle transacional (`AgendamentoService`, `SetupService`, `UnitOfWork`).
     * **Repository / DAO (`dataAccessObject`):** Persistência relacional via **JDBC puro + Reflection (`DataMapper`, `QueryBuilder`)**, evitando *overhead* de ORM e prevenindo SQL Injection com `PreparedStatement`.
     * **Model / Entity & DTO:** 14 entidades que implementam `IEntity` e DTOs de mapeamento (`EntityMapper`, `DtoMapper`, `SetupDTO`).
  2. **Banco de Dados Relacional:** Mostrar o modelo EER e destacar os relacionamentos principais (`tab_usuario` -> `tab_perfil`/`tab_permissao`, `tab_agendamento` vinculando cliente/profissional/serviço, `tab_pedido` e `tab_item_pedido`).
  3. **API REST:** Explicar o `JsonController` (`/create-json`, `/read-json`, `/update-json`, `/delete-json`) e o endpoint de domínio `/api/agenda/horarios-disponiveis`, destacando o uso de `ResponseEntity` e códigos HTTP (`200`, `400`, `403`, `500`).

---

### Bloco 3: Interface Front-end, Responsividade e Integração com API (04:30 – 07:00) | **Wallace**
* **O que mostrar na tela:** Navegação no sistema publicado (desktop e modo responsivo / F12 celular): `/home` -> `/servicos` -> `/checkout` -> `/loja` -> `/carrinho`.
* **Pontos-chave da fala:**
  1. **Organização do Front-end:** Templates modulares com **Thymeleaf Layout Dialect** (layouts base e fragmentos reutilizáveis de header, footer e sidebar) e scripts JS separados por função (`validation.js`, `masks.js`, `automatic-address.js`, `create.js`, `update.js`, `delete.js`).
  2. **Demonstração Prática (Agendamento + API REST):**
     * Acessar `/servicos`, selecionar um serviço e um barbeiro/data.
     * *(Dica visual)* Abrir o **DevTools (aba Network)** para mostrar a chamada `GET /api/agenda/horarios-disponiveis` retornando o **JSON** em tempo real.
     * Mostrar como horários ocupados já aparecem bloqueados visualmente na interface (prevenção de *double-booking*).
     * Preencher o checkout mostrando **máscaras automáticas** e consulta de CEP (ViaCEP) quando aplicável, concluir a reserva e confirmar a tela de sucesso (`/order-confirmation`).
  3. **E-commerce:** Adicionar rapidamente um produto na `/loja` via `POST /carrinho/adicionar` (atualizando o mini-cart assincronamente).

---

### Bloco 4: Autenticação, Segurança e CRUD Completo no Painel Admin (07:00 – 10:00) | **Gabriel & Wallace**
* **O que mostrar na tela:** `/login`, tentativa em `/admin` (Honeypot 403), acesso ao `/<rota-ofuscada>/login` e módulos do Painel Administrativo.
* **Pontos-chave da fala:**
  1. **Segurança e Autenticação (Gabriel):**
     * Demonstrar a rota armadilha `/admin` retornando **`403 Forbidden`** (`HoneypotController`), explicando a proteção contra bots.
     * Explicar a rota administrativa ofuscada configurável via `.env` (`ADMIN_ROUTE_PREFIX`) e o bloqueio de sessão pelo `AdminInterceptor` (se um cliente Perfil 4 tentar acessar o painel, é barrado; se for Barbeiro/Recepcionista, só acessa os módulos permitidos pelo RBAC).
     * Fazer login como Administrador no painel (`PasswordUtil` com SHA-256).
  2. **Operações CRUD Completas pela Interface (Wallace):**
     * Escolher um módulo (ex.: **Produtos** ou **Clientes** ou **Serviços**).
     * **Create:** Clicar em *Novo*, testar enviar campo inválido/vazio para mostrar a **validação**, depois preencher corretamente e salvar.
     * **Read:** Mostrar o registro recém-criado aparecendo na listagem.
     * **Update:** Editar o registro (ex.: alterar preço ou nome) e salvar, comprovando a atualização na lista.
     * **Delete:** Excluir o registro de teste mostrando o modal/confirmação e remoção da tabela.
     * Mostrar também o agendamento criado no Bloco 3 aparecendo na listagem de **Agendamentos** do painel.

---

### Bloco 5: Persistência Real, Tratamento de Erros e Deploy (10:00 – 12:00) | **Josias**
* **O que mostrar na tela:**
  1. **Persistência no Banco:** Consulta simples no MySQL/MariaDB (`SELECT` na tabela correspondente ao CRUD feito no Bloco 4 e `tab_agendamento`) provando que não há dados simulados.
  2. **Consumo Direto da API REST:** Uma chamada rápida via **Postman / Insomnia / curl** em `GET /read-json?entity=Produto` (ou `Cliente`) retornando JSON real do banco publicado.
  3. **Tratamento de Erros:** Acessar uma URL inexistente (ex.: `/pagina-que-nao-existe`) para exibir a página amigável tratada pelo `GlobalExceptionHandler` (`error.html`).
  4. **Deploy:** Mostrar a URL pública no navegador explicando brevemente a configuração com `.env`, variáveis de ambiente e banco em produção.

---

### Bloco 6: Qualidade, GitHub e Encerramento (12:00 – 12:30) | **Gabriel**
* **O que mostrar na tela:** Repositório no GitHub (`Gabriel-Calidonio/GJW_Projetcs`) + print/saída do `mvn test`.
* **Pontos-chave da fala:**
  1. Destacar o versionamento no GitHub e a documentação técnica produzida (`README.md`, diagramas UML/EER, scripts SQL).
  2. Citar a cobertura de qualidade: **33 testes automatizados passando** (JUnit 5, MockMvc, ArchUnit e teste E2E com Playwright) + SpotBugs e Spotless.
  3. Agradecer à banca e abrir para perguntas.

---

## Banco de Perguntas e Respostas (Preparação para a Banca)

Use este guia rápido para responder com segurança ao critério **"Domínio técnico"** e **"Respostas aos questionamentos"**:

1. **"Por que vocês usaram JDBC puro (`DataMapper`) em vez de Spring Data JPA / Hibernate?"**
   * *Resposta:* Para ter controle total das queries SQL geradas, evitar problemas clássicos de ORM como consultas *N+1*, reduzir o consumo de memória da JVM em hospedagens de baixo custo e aplicar na prática padrões clássicos da Engenharia de Software (*Data Mapper*, *Query Builder*, *Unit of Work* com `ThreadLocal` e *Reflection*).

2. **"Por que o front-end usa Thymeleaf + JavaScript (`fetch`) em vez de React/Angular separado?"**
   * *Resposta:* A arquitetura utiliza Thymeleaf com *Layout Dialect* (padrão Decorator/Fragments) para componentização e SEO da vitrine pública, combinado com **JavaScript ES6+ (`fetch()`)** consumindo endpoints JSON REST (`/api/agenda/horarios-disponiveis`, `/create-json`, `/update-json`, `/delete-json`, `/carrinho/*`) para atualização dinâmica sem recarregamento de página.

3. **"Como vocês garantem que dois clientes não agendem o mesmo horário ao mesmo tempo?"**
   * *Resposta:* O `AgendamentoService` divide o expediente em blocos de 20 minutos, verifica intervalos contínuos conforme a duração do serviço (20, 40 ou 60 min) e executa a validação + inserção dentro de uma transação ACID via `UnitOfWork`, bloqueando colisões no banco.

4. **"Como funciona a segurança e o controle de acesso do sistema?"**
   * *Resposta:* As senhas são gravadas com hash SHA-256 (`PasswordUtil`), as consultas usam `PreparedStatement` contra SQL Injection, e todas as rotas administrativas passam pelo `AdminInterceptor`, que verifica sessão ativa e permissões RBAC do perfil. Além disso, a URL do painel é ofuscada via `.env` (`ADMIN_ROUTE_PREFIX`) e `/admin/**` funciona como *Honeypot* devolvendo `403 Forbidden`.

5. **"Como funciona o Setup Wizard (`/setup`)?"**
   * *Resposta:* No primeiro boot sem `.env` configurado, o `SetupInterceptor` redireciona o acesso para `/setup`, permitindo configurar e testar a conexão com MariaDB/MySQL ou PostgreSQL em tempo real, criar tabelas, definir a rota administrativa ofuscada e cadastrar o superusuário. Após finalizar, a rota `/setup` é travada permanentemente por segurança.

---

## Checklist Técnico 30 Minutos Antes da Apresentação

- [ ] Sistema publicado e acessível pela URL pública (testar no navegador fora da rede local, se possível no 4G).
- [ ] Banco de dados populado com serviços, profissionais, produtos e um usuário Admin + um usuário Cliente prontos para login.
- [ ] Aba do Postman/Insomnia aberta com 1 requisição `GET`, `POST`, `PUT/PATCH` (ou `POST /update-json`) e `DELETE` testada na URL de produção.
- [ ] Console do banco ou ferramenta (DBeaver/phpMyAdmin/MySQL Workbench) aberta para mostrar a persistência real.
- [ ] IDE aberta nos pacotes `controller`, `service`, `model` e `templates` para mostrar organização ao vivo.
- [ ] Áudio, câmera e compartilhamento de tela testados por todos os integrantes.

