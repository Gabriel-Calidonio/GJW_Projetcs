# UC_ADM_001 - Autenticar no Painel Administrativo

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_ADM_001` (ref. `UC01` em [`general_dashboard.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/general_dashboard.puml)) |
| **Nome** | Autenticar no Painel Administrativo |
| **Módulo** | Controle de Acesso & Sessão |
| **Atores Primários** | Administrador Geral (*Perfil 1*), Recepcionista (*Perfil 3*), Barbeiro (*Perfil 2*) |
| **Atores Secundários** | Sistema de Segurança (`AdminInterceptor`) |
| **Tipo** | Essencial / Concreto |
| **Frequência de Uso** | Contínua (a cada início de turno/sessão de trabalho ou expiração de cookie) |
| **Rastreabilidade** | [`RF004`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/requirements/functional/RF004_autenticacao_e_permissoes.md), [`RN-SEG-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-01-hashing-criptogr%C3%A1fico-mandat%C3%B3rio-de-senhas), [`RN-SEG-02`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-02-bloqueio-de-clientes-nas-rotas-administrativas) |

---

## 1. 🎯 Descrição Sumária

Permite que colaboradores da barbearia (Administradores, Recepcionistas e Barbeiros) realizem autenticação segura no painel administrativo ofuscado (`/MRYnZpAsC9sp/login`). O caso de uso valida a identidade do operador por meio de credenciais criptografadas, valida se o usuário possui perfil interno autorizado (rejeitando sumariamente clientes comuns) e inicializa a sessão HTTP autenticada com seus privilégios RBAC associados.

---

## 2. ⚡ Pré-Condições

1. O colaborador deve possuir uma conta ativa (`tab_usuario.status = true`) cadastrada no sistema.
2. O usuário deve estar associado a um perfil administrativo/operacional válido (`perfil_id IN (1, 2, 3)`).
3. O serviço de banco de dados deve estar acessível para verificação de credenciais e permissões.

---

## 3. ✅ Pós-Condições

1. Uma sessão HTTP (`HttpSession`) ativa é criada no servidor contendo o objeto do usuário logado (`session.setAttribute("usuarioLogado", usuarioBanco)`).
2. As permissões granulares associadas ao perfil do usuário ficam disponíveis na sessão para validação posterior por interceptadores.
3. O operador é redirecionado com sucesso para a tela principal do Dashboard Geral (`/MRYnZpAsC9sp`).

---

## 4. 🚀 Gatilho (Trigger)

* O colaborador navega diretamente até a URL do painel administrativo (`/MRYnZpAsC9sp/login`); OU
* O colaborador tenta acessar qualquer rota protegida sob `/MRYnZpAsC9sp/*` sem uma sessão válida e é interceptado e redirecionado pelo [`AdminInterceptor`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminInterceptor.java).

---

## 5. 🔄 Fluxo Principal (Autenticação com Sucesso)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Operador | Acessa a rota `/MRYnZpAsC9sp/login`. |
| **2** | Sistema | Apresenta a tela de autenticação administrativa (`admin/auth/login.html`) contendo os campos de e-mail e senha. |
| **3** | Operador | Preenche suas credenciais corporativas (`email` e `senha`) e submete o formulário via botão "Entrar". |
| **4** | Sistema | [`LoginController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/LoginController.java) recebe a requisição HTTP POST e aplica a função hash SHA-256 na senha digitada (`"{sha256}" + PasswordUtil.hash(senha)`). |
| **5** | Sistema | Consulta o serviço [`UsuarioService`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/service/UsuarioService.java) filtrando pelo e-mail informado. |
| **6** | Sistema | Compara a senha criptografada calculada com a senha armazenada na base de dados (`tab_usuario`). |
| **7** | Sistema | Valida que o perfil do usuário **não** é de "Cliente" (`perfil_id != 4`). |
| **8** | Sistema | Registra o objeto do usuário na sessão HTTP atual (`session.setAttribute("usuarioLogado", usuarioBanco)`). |
| **9** | Sistema | Redireciona o navegador do operador para a URL raiz do Dashboard Administrativo: `/MRYnZpAsC9sp`. |

---

## 6. 🔀 Fluxos Alternativos

### **FA01 - Sessão Administrativa Já Ativa**
* **Condição:** O operador já autenticado acessa a URL `/MRYnZpAsC9sp/login`.
* **Passo 1:** O controlador verifica se `session.getAttribute("usuarioLogado")` já existe e se o perfil é válido.
* **Passo 2:** O sistema não reapresenta o formulário de login e redireciona imediatamente para o dashboard (`/MRYnZpAsC9sp`).

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Credenciais Inválidas (E-mail Inexistente ou Senha Incorreta)**
* **No Passo 5 ou 6:** A busca por e-mail não retorna nenhum usuário ativo OU o hash da senha não coincide com o valor persistido.
* **Ação do Sistema:**
  1. O sistema não inicializa nenhuma sessão HTTP autenticada.
  2. Adiciona o atributo de erro `"E-mail ou senha inválidos."` ao modelo.
  3. Renderiza novamente a view `admin/auth/login` exibindo o alerta de erro visual.
  4. O caso de uso é reiniciado.

### **FE02 - Tentativa de Login por Usuário com Perfil de Cliente (RN-SEG-02)**
* **No Passo 7:** O e-mail e a senha informados são válidos, porém o perfil retornado possui `perfil_id = 4` (*Cliente*).
* **Ação do Sistema:**
  1. O sistema bloqueia a entrada no painel de administração.
  2. Adiciona o atributo de erro `"Acesso negado: esta área é restrita a administradores."` ao modelo.
  3. Retorna a view `admin/auth/login` mantendo a sessão protegida contra privilégios indevidos.

### **FE03 - Falha de Conexão com o Banco de Dados**
* **No Passo 5:** Ocorre uma falha de conexão JDBC ou indisponibilidade do banco de dados MySQL.
* **Ação do Sistema:**
  1. O erro é capturado pelo mecanismo de tratamento de exceções.
  2. O sistema exibe mensagem genérica amigável informando instabilidade momentânea e registra o erro em log técnico.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-SEG-01** | Hashing Criptográfico Mandatório de Senhas | A senha fornecida nunca é manipulada ou comparada em texto puro; aplica-se `SHA-256` antes de qualquer validação. |
| **RN-SEG-02** | Bloqueio de Clientes nas Rotas Administrativas | Usuários do perfil Cliente (ID 4) são sumariamente rejeitados no endpoint de autenticação do painel. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas (Formulário `/MRYnZpAsC9sp/login`):
* `email` *(String, Obrigatório)*: Endereço de e-mail corporativo cadastrado para o colaborador.
* `senha` *(String, Obrigatório, tipo Password)*: Senha alfanumérica de acesso.

### Saídas:
* Redirecionamento HTTP 302 para `/MRYnZpAsC9sp` em caso de sucesso.
* Mensagem de alerta na interface (`erro`):
  * `"E-mail ou senha inválidos."` (em caso de credenciais incorretas).
  * `"Acesso negado: esta área é restrita a administradores."` (em caso de perfil não autorizado).
* Atributo de Sessão:
  * `usuarioLogado` contendo a instância completa de `com.gwj.model.domain.entities.Usuario`.
