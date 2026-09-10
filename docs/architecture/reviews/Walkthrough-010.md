# Walkthrough-010: Script de Diagnóstico e Checagem de Ambiente

Implementamos um sistema de checagem e diagnóstico automatizado (pré-voo / health-check) para que alunos e desenvolvedores possam verificar os pré-requisitos antes de subir a aplicação Spring Boot.

---

## 📦 Arquivos Criados e Modificados

1. **[CheckEnvironment.java](/src/main/java/com/gwj/tools/CheckEnvironment.java)**
   - Classe autônoma em Java para validação detalhada do ambiente.
   - Valida:
     1. **Versão do Java:** Verifica se o JDK é $\ge$ 21 LTS.
     2. **Configurações:** Carrega e valida [application.properties](/src/main/resources/application.properties).
     3. **Porta Web:** Verifica se a porta `8089` (ou configurada) está disponível.
     4. **Porta do Banco:** Valida conectividade de socket na porta `3306` (MariaDB/MySQL).
     5. **Autenticação JDBC:** Conecta com usuário `desenvolvedor` e senha no banco `gwj2`.
     6. **Tabelas & Schema:** Confere a existência das tabelas (`tab_usuario`, `tab_cliente`, `tab_produto`, `tab_setting`, `tab_pedidos`, etc.) e a contagem de registros iniciais.
     7. **Validação de Entidades:** Executa o `SchemaValidator.validateAllEntities()`.
   - Gera resumo colorido (ANSI) com guia de resolução de problemas passo a passo.

2. **[check.sh](/check.sh)**
   - Script Bash para Linux, macOS e WSL com permissão de execução configurada.

3. **[check.bat](/check.bat)**
   - Script em lote para Windows (Prompt de Comando ou duplo clique no Windows Explorer com `pause` ao final).

4. **[check.ps1](/check.ps1)**
   - Script em PowerShell para usuários modernos do Windows.

5. **[README.md](/README.md)**
   - Adicionada seção explicativa com comandos de diagnóstico no início do documento.

---

## 🧪 Validação dos Testes

O script `./check.sh` foi executado no ambiente e obteve o seguinte resultado:

```text
=================================================================
   🔍 TGOS / GWJ - DIAGNÓSTICO E CHECAGEM DE AMBIENTE (PRÉ-VOO)  
=================================================================
Iniciando verificação dos recursos necessários para a aplicação...

[1/6] Versão do Java (JDK): ✅ OK! Java 25 (25.0.4 - Debian)
[2/6] Arquivo de Configuração (application.properties): ✅ OK! Encontrado no classpath com 8 propriedades.
[3/6] Porta Web (8089): ✅ OK! A porta 8089 está livre para o servidor Spring Boot.
[4/6] Serviço de Banco de Dados (Host: localhost, Porta: 3306): ✅ OK! Porta 3306 respondendo ativamente.
[5/6] Conexão e Autenticação JDBC: ✅ OK! Conectado com sucesso com usuário 'desenvolvedor' no banco 'gwj2'.
[6/6] Verificação de Tabelas e Schema: ✅ OK! 20 tabelas encontradas (incluindo 8 usuário(s) cadastrado(s)).
[+] Validação Arquitetural de Entidades: ✅ Todas as classes em 'com.gwj.model.domain.entities' implementam IEntity e seguem os padrões.

=================================================================
                       RESULTADO DO DIAGNÓSTICO                  
=================================================================
  Checagens Realizadas: 6
  ✅ Sucessos: 6
  ⚠️ Avisos:   0
  ❌ Falhas:   0
-----------------------------------------------------------------
🎉 PARABÉNS! Seu ambiente está 100% pronto para rodar o projeto!
Para iniciar a aplicação, execute:
   ./mvnw spring-boot:run (Linux/macOS) ou mvnw.cmd spring-boot:run (Windows)
   Ou execute a classe com.gwj.StartApplication na sua IDE.
=================================================================
```

