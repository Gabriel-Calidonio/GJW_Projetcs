# Walkthrough - Atualização do Documento Acadêmico (TCC / Projeto Integrador)

O documento acadêmico principal do projeto ([`trabalhos_conclusao_curso.md`](/apresentacao_trabalho_academico/trabalhos_conclusao_curso.md)) foi completamente reestruturado, ampliado e sincronizado com o estado real da aplicação desenvolvida.

---

## 📋 Resumo das Alterações Realizadas

### 1. **Modernização e Atualização da Stack Tecnológica**
- Atualização da versão do documento para **Versão 2.0 (Entrega Final)**.
- Substituição de termos desatualizados da fase preliminar (como "JSP" ou "PostgreSQL previsto") pela stack de produção: **Java 21 LTS**, **Spring Boot 3.2.5**, **Thymeleaf**, **MySQL 8.0 / MariaDB**, **Vanilla CSS/JS** e **Maven**.

### 2. **Saneamento e Expansão dos Requisitos**
- **Requisitos Funcionais**: Correção das numerações duplicadas da versão anterior, consolidando **15 Requisitos Funcionais (RF01 a RF15)** alinhados ao código-fonte:
  - RF01 (Cadastro e Gestão de Perfil)
  - RF02 (Autenticação SHA-256 e Sessão)
  - RF03 (Catálogo de Serviços e Kits)
  - RF04 (Motor de Grade e Slots de 20 min)
  - RF05 (Agendamento Autônomo e Anti Double-Booking)
  - RF06 (Cancelamento e Remarcação)
  - RF07 (Painel Administrativo Genérico via Reflexão)
  - RF08 (Gestão de Barbeiros e Escalas)
  - RF09 (Controle de Acesso RBAC com Interceptor)
  - RF10 (E-Commerce de Cosméticos e Estoque Atômico)
  - RF11 (Comandas e Atendimento Presencial)
  - RF12 (Lembretes Automáticos via Scheduler Job)
  - RF13 (Checkout e Pagamento)
  - RF14 (Histórico Financeiro e Relatórios)
  - RF15 (Armadilha Honeypot Controller)
- **Requisitos Não-Funcionais com SLAs Técnicos**:
  - RNF01 (Latência < 1.2s para busca de slots livres)
  - RNF02 (Integridade Transacional ACID via `UnitOfWork` e `ThreadLocal`)
  - RNF03 (Segurança OWASP Top 10 e PreparedStatement)
  - RNF04 (Usabilidade Mobile-First com áreas de toque >= 44px)
  - RNF05 (Arquitetura Desacoplada e Princípios SOLID)
  - RNF06 (Resiliência e Tratamento Centralizado com `@ControllerAdvice`)

### 3. **Engenharia de Software e Padrões de Projeto (GoF & PoEAA)**
- Seção aprofundada documentando os 9 padrões fundamentais aplicados no projeto:
  - *Singleton* (`ConnectionDB`)
  - *Factory Method* (`SimpleObjectFactory`)
  - *Data Mapper* (`DataMapper`)
  - *Query Builder* (`QueryBuilder`)
  - *Unit of Work* (`UnitOfWork`)
  - *DTO / Entity Mapper* (`EntityMapper`)
  - *Service Registry* (`ServiceRegistry`)
  - *Strategy / Interceptor* (`AdminInterceptor` e `GlobalExceptionHandler`)
  - *Decorator / Template View* (Thymeleaf layout e fragmentos)

### 4. **Integração do Catálogo de Diagramas Técnicos**
- Inclusão e mapeamento de todos os diagramas UML e EER do repositório:
  - **Diagrama de Arquitetura do Sistema** ([`architecture_diagram.puml`](/docs/architecture/architecture_diagram.puml) | SVG | PDF)
  - **Diagrama Entidade-Relacionamento Estendido** ([`EER-diagram.puml`](/docs/architecture/EER-diagram.puml) | SVG | PDF)
  - **Diagrama de Classes de Domínio** ([`diagramasClassesDominio.puml`](/docs/diagramas/diagramasClassesDominio.puml) | SVG | PDF)
  - **6 Diagramas de Sequência**: Agendamento Autônomo, Gestão Administrativa de Horários, Autenticação RBAC, E-Commerce, Cancelamento/Reagendamento e Lembretes Automáticos.
  - **5 Diagramas de Atividades**: Agendamento Online, Atendimento Presencial, Cancelamento, Compra de Produtos e Lembretes.

### 5. **Otimização de Arquivos e Capa**
- Extração do bloco base64 de 500KB para um arquivo de imagem dedicado: [`img/capa_tcc.png`](/apresentacao_trabalho_academico/img/capa_tcc.png).
- Redução do tamanho do documento de 575KB para 64KB de Markdown limpo, portátil e de rápida renderização.
- Criação de link simbólico de compatibilidade `docs/architecture/diagrama.puml` -> `architecture_diagram.puml` para suporte automático à skill `persistencia-contexto`.

### 6. **Fundamentação Teórica e Normas ABNT NBR 6023:2018**
- Inserção das referências bibliográficas obrigatórias e formatadas (Martin Fowler, Gang of Four, Robert C. Martin, Joshua Bloch, Eric Evans, Ramez Elmasri, Grady Booch, Rod Johnson, Craig Walls e OWASP).

---

## 🔍 Validação e Testes

- **Compilação do Projeto**:
  Executado `./mvnw test-compile` com **`BUILD SUCCESS`** (64 arquivos fonte e 3 arquivos de teste compilados sob Java 21).
- **Integridade de Links**:
  Todos os links para seções e arquivos do repositório foram testados e validados.
- **Rastreabilidade de Código**:
  Todas as menções a classes e padrões refletem a implementação real em `src/main/java/com/gwj/`.
