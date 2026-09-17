# UC_ADM_003 - Encerrar Sessão (Logout)

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_ADM_003` (ref. `UC03` em [`general_dashboard.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/general_dashboard.puml)) |
| **Nome** | Encerrar Sessão (Logout Administrativo) |
| **Módulo** | Controle de Acesso & Sessão |
| **Atores Primários** | Administrador Geral (*Perfil 1*), Recepcionista (*Perfil 3*), Barbeiro (*Perfil 2*) |
| **Atores Secundários** | Servidor de Aplicação (`HttpSession`) |
| **Tipo** | Essencial / Concreto |
| **Frequência de Uso** | Ao término de cada turno de trabalho ou saída do operador da estação de atendimento |
| **Rastreabilidade** | [`RF004`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/requirements/functional/RF004_autenticacao_e_permissoes.md), [`RN-SEG-04`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-04-encerramento-de-sess%C3%A3o-logout) |

---

## 1. 🎯 Descrição Sumária

Permite que o colaborador encerre voluntariamente sua sessão autenticada no painel administrativo. O sistema remove com segurança todos os dados do operador retidos na memória do servidor através da invalidação atômica da sessão (`session.invalidate()`), prevenindo o sequestro de sessão (*session hijacking*) ou acessos indevidos em computadores compartilhados da recepção/salão, redirecionando o usuário de volta à tela de login administrativo.

---

## 2. ⚡ Pré-Condições

1. O operador deve estar com uma sessão ativa (`usuarioLogado` presente na `HttpSession`).
2. A requisição HTTP GET deve ser direcionada para a rota `/MRYnZpAsC9sp/logout` (ou `/logout`).

---

## 3. ✅ Pós-Condições

1. A `HttpSession` associada ao identificador de sessão (JSESSIONID) é completamente destruída no contêiner web.
2. Todos os objetos cacheados vinculados ao usuário (perfil, permissões e preferências de tela) são expurgados da memória.
3. O operador é redirecionado para a tela de login do painel administrativo (`/MRYnZpAsC9sp/login`).
4. Novas tentativas de navegação no histórico do navegador ou requisições para rotas sob `/MRYnZpAsC9sp/*` são interceptadas e bloqueadas pelo `AdminInterceptor`.

---

## 4. 🚀 Gatilho (Trigger)

* O colaborador clica no botão ou ícone de "Sair" / "Logout" no cabeçalho ou menu lateral do painel administrativo; OU
* O colaborador digita diretamente a URL `/MRYnZpAsC9sp/logout`.

---

## 5. 🔄 Fluxo Principal (Encerramento de Sessão com Sucesso)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Operador | Clica na opção "Sair" no menu administrativo. |
| **2** | Navegador | Envia uma requisição HTTP GET para a rota `/MRYnZpAsC9sp/logout`. |
| **3** | Sistema | O método `adminLogout` de [`LoginController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/LoginController.java) intercepta a requisição. |
| **4** | Sistema | Obtém a sessão atual via `request.getSession(false)`. |
| **5** | Sistema | Caso a sessão exista, invoca o método `session.invalidate()`, destruindo o contexto da sessão. |
| **6** | Sistema | Retorna uma instrução de redirecionamento HTTP (302) para `/MRYnZpAsC9sp/login`. |
| **7** | Navegador | Redireciona o usuário para a tela de login administrativo com os campos em branco. |

---

## 6. 🔀 Fluxos Alternativos

### **FA01 - Logout com Sessão Já Expirada por Timeout**
* **No Passo 4:** O operador clica em "Sair" após um longo período de inatividade, quando a sessão já havia sido eliminada automaticamente pelo servidor web.
* **Ação do Sistema:** `request.getSession(false)` retorna `null`. O sistema não dispara exceção e simplesmente redireciona o usuário diretamente para `/MRYnZpAsC9sp/login`.

---

## 7. ⚠️ Fluxos de Exceção

*Não se aplicam fluxos de exceção impeditivos, pois o encerramento de sessão é idempotente e resiliente.*

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-SEG-04** | Encerramento de Sessão (Logout) | A chamada a `/logout` ou `/MRYnZpAsC9sp/logout` deve obrigatoriamente chamar `session.invalidate()`, limpando todo o cache em memória e redirecionando para a página de login. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas:
* Requisição HTTP GET para `/MRYnZpAsC9sp/logout`.

### Saídas:
* Invalidação da `HttpSession`.
* Redirecionamento HTTP para `/MRYnZpAsC9sp/login`.
