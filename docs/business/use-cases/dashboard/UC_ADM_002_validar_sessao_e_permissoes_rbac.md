# UC_ADM_002 - Validar Sessão e Permissões RBAC

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_ADM_002` (ref. `UC02` em [`general_dashboard.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/general_dashboard.puml)) |
| **Nome** | Validar Sessão e Permissões RBAC |
| **Módulo** | Controle de Acesso & Sessão |
| **Atores Primários** | `AdminInterceptor` (*Sistema de Segurança / Interceptador Spring*) |
| **Atores Secundários** | Usuário Requisitante (*Administrador, Recepcionista, Barbeiro ou Cliente não autorizado*) |
| **Tipo** | Essencial / Abstrato (Incluso ou Executado Automaticamente) |
| **Frequência de Uso** | A cada requisição HTTP para endpoints administrativos (`/MRYnZpAsC9sp/*`) |
| **Rastreabilidade** | [`RF004`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/requirements/functional/RF004_autenticacao_e_permissoes.md), [`RN-SEG-02`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-02-bloqueio-de-clientes-nas-rotas-administrativas), [`RN-SEG-03`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-03-autoriza%C3%A7%C3%A3o-granular-por-entidade-no-crud-din%C3%A2mico) |

---

## 1. 🎯 Descrição Sumária

Atua como barreira de segurança perimetral inspecionando toda e qualquer requisição HTTP direcionada ao painel administrativo ofuscado (`/MRYnZpAsC9sp/**`), exceto a própria tela de login. O caso de uso valida se existe uma sessão válida ativa no servidor (`usuarioLogado`). Em seguida, executa o controle de acesso baseado em papéis (*RBAC*) e permissões granulares:
1. Concede acesso irrestrito ao Administrador Geral (*Perfil 1*);
2. Bloqueia sumariamente usuários com perfil Cliente (*Perfil 4*), redirecionando-os para a Home (`/`);
3. Para perfis operacionais (Recepcionistas e Barbeiros), inspeciona o módulo/URI solicitado e valida se o usuário possui a permissão requerida em sua lista de permissões antes de liberar a execução do Controller de destino.

---

## 2. ⚡ Pré-Condições

1. O cliente HTTP (navegador) dispara uma requisição para uma URI que coincide com o padrão de interceptação configurado em `WebConfig` (`/MRYnZpAsC9sp/**`).
2. O framework Spring MVC repassa o fluxo de processamento para o método `preHandle` do [`AdminInterceptor`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminInterceptor.java).

---

## 3. ✅ Pós-Condições

* **Acesso Permitido:** O método `preHandle` retorna `true` e a execução prossegue normalmente para o `Controller` responsável pela ação solicitada.
* **Acesso Negado por Falta de Sessão:** O usuário é redirecionado para a tela de autenticação administrativa (`/MRYnZpAsC9sp/login`).
* **Acesso Negado por Perfil Inválido (Cliente):** O usuário é expulso do painel e redirecionado para a Home pública (`/`).
* **Acesso Negado por Falta de Permissão Granular:** O usuário é redirecionado para o Dashboard principal (`/MRYnZpAsC9sp`) com mensagem flash de erro explicativa.

---

## 4. 🚀 Gatilho (Trigger)

Disparado automaticamente pelo contêiner Spring MVC para qualquer requisição HTTP direcionada a endpoints sob `/MRYnZpAsC9sp/*`.

---

## 5. 🔄 Fluxo Principal (Validação e Liberação de Acesso)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Ator Requisitante | Dispara requisição HTTP (ex: `GET /MRYnZpAsC9sp/clientes`). |
| **2** | `AdminInterceptor` | Captura a requisição antes do encaminhamento ao Controller (`preHandle`). |
| **3** | `AdminInterceptor` | Verifica a existência de `session` e a presença do atributo `usuarioLogado`. |
| **4** | `AdminInterceptor` | Obtém a instância de `Usuario` da sessão e examina seu perfil associado (`usuarioLogado.getPerfil()`). |
| **5** | `AdminInterceptor` | Identifica se o perfil é Administrador Geral (`perfil_id == 1L`). Em caso afirmativo, **autoriza imediatamente** (`return true`). |
| **6** | `AdminInterceptor` | Se não for Administrador, mapeia a URI da requisição para determinar a permissão requerida: <br>• `/clientes` -> `GERENCIAR_CLIENTES`<br>• `/servicos` -> `GERENCIAR_SERVICOS`<br>• `/produtos` -> `GERENCIAR_ESTOQUE`<br>• `/agendamentos` -> `GERENCIAR_TODAS_AGENDAS` / `AGENDAR_HORARIO` / `VISUALIZAR_PROPRIA_AGENDA`<br>• `/profissionais` ou `/configuracoes` -> Exclusivo Administrador (`ADMIN_ONLY`). |
| **7** | `AdminInterceptor` | Executa o método `usuarioLogado.hasPermissao(permissaoNecessaria)`. |
| **8** | `AdminInterceptor` | Confirmando a titularidade da permissão pelo colaborador, retorna `true` e a requisição atinge o Controller de destino. |

---

## 6. 🔀 Fluxos Alternativos

### **FA01 - Acesso pelo Administrador Geral (Bypass Pleno)**
* **No Passo 5:** O usuário logado possui `perfil_id == 1L`.
* **Ação do Sistema:** O sistema não necessita verificar permissões granulares individuais, pois o Administrador possui acesso irrestrito a todos os módulos, configurações e relatórios. Retorna `true` diretamente.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Sessão Inexistente ou Expirada**
* **No Passo 3:** Não há sessão HTTP ativa (`session == null`) ou o atributo `usuarioLogado` é nulo.
* **Ação do Sistema:**
  1. O interceptador bloqueia a continuação do fluxo (`return false`).
  2. Executa redirecionamento HTTP compulsório: `response.sendRedirect(contextPath + "/MRYnZpAsC9sp/login")`.

### **FE02 - Tentativa de Acesso por Usuário com Perfil de Cliente (RN-SEG-02)**
* **No Passo 4:** O usuário autenticado possui perfil de Cliente (`perfil_id == 4L`).
* **Ação do Sistema:**
  1. O interceptador impede o acesso ao painel de administração (`return false`).
  2. Executa redirecionamento HTTP imediato para a raiz pública do sistema: `response.sendRedirect(contextPath + "/")`.

### **FE03 - Ausência da Permissão Granular Requerida (RN-SEG-03)**
* **No Passo 7:** O usuário é um colaborador da barbearia (ex: Barbeiro tentando acessar `/MRYnZpAsC9sp/servicos` ou `/MRYnZpAsC9sp/configuracoes`), mas não possui a permissão requerida cadastrada.
* **Ação do Sistema:**
  1. O interceptador instancia um objeto `FlashMap` contendo a mensagem:  
     `"Acesso Negado: Você não possui permissão para gerenciar [Módulo]."`
  2. Salva o flash map através de `RequestContextUtils.getFlashMapManager(request)`.
  3. Redireciona o usuário para o Dashboard Geral: `response.sendRedirect(contextPath + "/MRYnZpAsC9sp")`.
  4. Retorna `false` para abortar a chamada ao Controller protegido.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-SEG-02** | Bloqueio de Clientes nas Rotas Administrativas | Impede clientes de acessar qualquer área interna sob `/MRYnZpAsC9sp/*`, expulsando-os para `/`. |
| **RN-SEG-03** | Autorização Granular por Entidade no CRUD Dinâmico | Verifica permissões específicas por entidade na tabela `permissoes` para colaboradores não-administradores. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas Inspecionadas:
* `HttpServletRequest.getRequestURI()` *(String)*: Caminho solicitado pelo usuário.
* `HttpSession.getAttribute("usuarioLogado")` *(Object/Usuario)*: Dados do usuário em memória.
* Coleção de Permissões: `Usuario.getPerfil().getPermissoes()`.

### Saídas:
* Retorno Booleano (`true` para autorizar, `false` para interceptar).
* Redirecionamentos HTTP (`/MRYnZpAsC9sp/login`, `/`, ou `/MRYnZpAsC9sp`).
* Atributo Flash: `mensagemErro` com descrição da permissão negada.
