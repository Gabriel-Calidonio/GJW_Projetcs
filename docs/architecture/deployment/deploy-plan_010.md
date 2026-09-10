# DP-10: Implementação de Script de Diagnóstico e Checagem de Ambiente

- **Tipo:** Deployment plan
- **Status:** closed
- **Autor:** GWJ
- **Criado em:** 2026-08-31 12:33:57
- **Labels:** Nenhuma
- **Responsáveis:** Nenhum

## Descrição

# Plano de Implementação: Script de Diagnóstico e Checagem de Ambiente

Criar uma ferramenta de diagnóstico automatizado (pré-voo / health-check) para que alunos e desenvolvedores possam verificar se todos os pré-requisitos (Java 21, Maven, MySQL/MariaDB, portas de rede, usuário do banco, tabelas essenciais e entidades) estão devidamente configurados antes de rodar a aplicação Spring Boot.

## User Review Required

> [!NOTE]
> Os scripts e a classe de diagnóstico foram projetados para serem executados de forma independente antes de subir o servidor, emitindo relatórios coloridos, objetivos e com instruções passo a passo para corrigir qualquer pendência encontrada.

## Proposed Changes

### Ferramenta de Diagnóstico em Java

#### [NEW] [CheckEnvironment.java](/src/main/java/com/gwj/tools/CheckEnvironment.java)
- Classe executável autônoma (`main`) que executa as seguintes checagens:
  1. **Java Runtime:** Valida versão do JDK ($\ge 21$), arquitetura e vendor.
  2. **Configuração (`application.properties`):** Valida leitura das propriedades do DataSource, porta HTTP e caminhos de entidades.
  3. **Porta do Servidor Web:** Testa se a porta configurada (ex: `8089` ou `8080`) está livre ou em conflito.
  4. **Porta do Banco de Dados:** Testa conectividade de socket na porta do banco (`3306`).
  5. **Conexão JDBC & Autenticação:** Conecta usando `AppConfig` / `ConnectionDB` com o usuário `desenvolvedor` e senha configurada.
  6. **Banco de Dados & Tabelas:** Verifica se o banco `gwj2` existe e se as tabelas principais das entidades (`tab_usuario`, `tab_cliente`, `tab_produto`, `tab_setting`, `tab_pedidos`, etc.) foram criadas.
  7. **Validação Arquitetural de Entidades:** Executa `SchemaValidator.validateAllEntities()`.
  8. **Relatório Visual:** Apresenta resumo com status (✅ SUCESSO, ⚠️ AVISO, ❌ ERRO) e comandos de correção recomendados (ex: importação do SQL, criação do usuário).

---

### Scripts Multiplataforma para Execução com 1 Comando

#### [NEW] [check.sh](/check.sh)
- Script Bash para Linux, macOS e WSL com permissão de execução.
- Checa rapidamente a versão do Java no terminal e dispara a execução do `CheckEnvironment.java` via Maven Wrapper (`./mvnw`).

#### [NEW] [check.bat](/check.bat)
- Script em lote para Windows (compatível com CMD e duplo-clique no Windows Explorer).
- Pausa ao final para que a janela não feche sozinha em caso de duplo-clique.

#### [NEW] [check.ps1](/check.ps1)
- Script PowerShell para usuários de terminal moderno no Windows.

---

### Documentação e Instruções

#### [MODIFY] [README.md](/README.md)
- Adicionar uma seção de destaque no topo explicando como os alunos podem rodar o teste de ambiente antes de iniciar o projeto.

## Verification Plan

### Automated Tests & Execuções de Validação
- Executar `./check.sh` no ambiente Linux e verificar a saída de diagnóstico.
- Simular cenários de sucesso e verificar a integridade da conexão com MariaDB/MySQL.
- Testar execução direta via Maven: `./mvnw compile exec:java -Dexec.mainClass="com.gwj.tools.CheckEnvironment"`.

### Manual Verification
- Testar se os comandos e mensagens de orientação são amigáveis e de fácil entendimento para os alunos.
