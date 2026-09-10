# DP-8: Remoção do antigo sistema de agendamento

- **Tipo:** Deployment plan
- **Status:** closed
- **Autor:** GWJ
- **Criado em:** 2026-08-19 13:00:26
- **Labels:** Nenhuma
- **Responsáveis:** Nenhum

## Descrição

# Plano de Implementação - Remoção de "Agendas (Antigo)"

Remoção definitiva da entidade legada `Agenda` e suas tabelas (`tab_agenda` e `tab_agenda_servico`), visto que foram substituídas completamente pelo módulo atual de `Agendamento` (`tab_agendamento`).

## User Review Required

> [!IMPORTANT]
> A remoção excluirá do banco de dados e do sistema as tabelas legadas `tab_agenda` e `tab_agenda_servico` e a opção de menu "Agendas (Antigo)" do Dashboard Administrativo.
> A navegação e persistência ativas continuam operando 100% sobre `Agendamento` (`tab_agendamento`).

## Open Questions

Não há dúvidas em aberto. O escopo é estritamente limpo e delimitado à eliminação dos resíduos da antiga classe/tabela `Agenda`.

## Proposed Changes

---

### Dashboard & Views (Frontend)

#### [MODIFY] [sidebar.html](/src/main/resources/templates/admin/fragments/sidebar.html)
- Remover o item de menu "Agendas (Antigo)" que aponta para `/MRYnZpAsC9sp/listar/Agenda`.

#### [MODIFY] [listagem-dinamica.html](/src/main/resources/templates/admin/listagem-dinamica.html)
- Remover a verificação condicional legada de permissão `entidadeNome == 'Agenda' ? 'AGENDAR_HORARIO' :`.

---

### Backend (Java)

#### [DELETE] [Agenda.java](/src/main/java/com/gwj/model/domain/entities/Agenda.java)
- Deletar a entidade Java legada `Agenda`.

#### [DELETE] [AgendaService.java](/src/main/java/com/gwj/service/AgendaService.java)
- Deletar o serviço legado `AgendaService`.

#### [DELETE] [AgendaController.java](/src/main/java/com/gwj/controller/AgendaController.java)
- Deletar o controller legado `AgendaController`.

#### [DELETE] [AgendaServiceTest.java](/src/test/java/com/gwj/service/AgendaServiceTest.java)
- Deletar a classe de teste unitário legada `AgendaServiceTest`.

#### [MODIFY] [ServiceRegistry.java](/src/main/java/com/gwj/service/ServiceRegistry.java)
- Remover o registro de `registry.put("Agenda", new AgendaService());` e a importação de `AgendaService`.

#### [MODIFY] [AdminInterceptor.java](/src/main/java/com/gwj/controller/AdminInterceptor.java)
- Remover a regra especial de interceptação para a entidade legada `"Agenda"`.

---

### Banco de Dados (SQL)

#### [MODIFY] [gwj5.sql](/gwj5.sql)
- Remover definições `CREATE TABLE`, inserts de dados e `FOREIGN KEY` referentes às tabelas `tab_agenda` e `tab_agenda_servico`.

---

### Documentação & Arquitetura

#### [MODIFY] [diagramasClassesDominio.puml](/docs/diagramas/diagramasClassesDominio.puml)
- Remover a classe `Agenda` e suas associações legadas do diagrama PlantUML de classes de domínio.

---

## Verification Plan

### Automated Tests
- Executar `mvn test` para garantir que a compilação e todos os testes de serviço (como `AgendamentoServiceTest` e `SchemaValidator`) passam sem erros após a remoção.

### Manual Verification
- Acessar o Dashboard em `/MRYnZpAsC9sp/` e verificar que a sidebar não exibe mais a opção "Agendas (Antigo)".
- Tentar acessar `/MRYnZpAsC9sp/listar/Agenda` e confirmar que retorna 404 (Entidade Não Encontrada) ou 403 Forbidden.

# Checklist de Execução - Remoção de Agendas (Antigo)

- [x] Remover item "Agendas (Antigo)" da sidebar em `sidebar.html`
- [x] Ajustar verificação condicional em `listagem-dinamica.html`
- [x] Remover regra para "Agenda" em `AdminInterceptor.java`
- [x] Deletar a entidade Java `Agenda.java`
- [x] Deletar o serviço Java `AgendaService.java`
- [x] Deletar o controller Java `AgendaController.java`
- [x] Deletar o teste unitário `AgendaServiceTest.java`
- [x] Remover registro em `ServiceRegistry.java`
- [x] Remover estruturas `tab_agenda` e `tab_agenda_servico` de `gwj5.sql`
- [x] Remover classe `Agenda` do PlantUML `diagramasClassesDominio.puml`
- [x] Executar `mvn test` para validação (BUILD SUCCESS)
