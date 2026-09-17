# UC_STR_006 - Cadastrar Nova Conta e Autenticar Cliente

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_STR_006` (engloba `UC19`, `UC20` e `UC22` em [`storefront.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/storefront.puml)) |
| **Nome** | Cadastrar Nova Conta, Autenticar e Encerrar Sessão do Cliente |
| **Módulo** | Área e Conta do Cliente |
| **Atores Primários** | Visitante (*Público Geral*), Cliente Registrado (*Perfil 4*) |
| **Atores Secundários** | Serviço de Usuários (`UsuarioService`), Serviço de Clientes (`ClienteService`) |
| **Tipo** | Essencial / Concreto |
| **Frequência de Uso** | Alta (acesso inicial e identificação em compras e agendamentos) |
| **Rastreabilidade** | [`LoginController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/LoginController.java), [`PasswordUtil.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/PasswordUtil.java), [`RN-SEG-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-01-hashing-criptogr%C3%A1fico-mandat%C3%B3rio-de-senhas), [`RN-SEG-02`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-02-bloqueio-de-clientes-nas-rotas-administrativas), [`RN-SEG-04`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-04-encerramento-de-sess%C3%A3o-logout) |

---

## 1. 🎯 Descrição Sumária

Permite que novos clientes criem uma conta pessoal no sistema (`/cadastro`), efetuem login seguro na área pública (`/login`) e encerrem suas sessões (`/logout`). Todas as senhas cadastradas são processadas criptograficamente com o algoritmo SHA-256 e salvas sob o padrão `{sha256}` (`RN-SEG-01`). Ao autenticar com sucesso, clientes (Perfil 4) são automaticamente direcionados para a Home da barbearia (`/`), mantendo sua sessão ativa para agilizar checkouts de produtos e agendamentos online, com bloqueio automático a rotas restritas do painel administrativo (`RN-SEG-02`).

---

## 2. ⚡ Pré-Condições

1. Para cadastro: O visitante deve fornecer e-mail e telefone válidos não cadastrados anteriormente.
2. Para login: O usuário deve possuir cadastro ativo na base de dados (`status = true`).

---

## 3. ✅ Pós-Condições

1. Criação do registro em `tab_cliente` e `tab_usuario` com perfil associado (`perfil_id = 4`).
2. Criação da sessão `HttpSession` com o atributo `usuarioLogado`.
3. Redirecionamento do cliente para a página inicial (`/`).

---

## 4. 🚀 Gatilhos (Triggers)

* O visitante clica em "Entrar" ou "Cadastrar-se" no menu superior do site; OU
* O usuário clica em "Sair" na sua área logada.

---

## 5. 🔄 Fluxo Principal (Cadastro de Novo Cliente e Login)

### **Parte A: Cadastro de Conta (`/cadastro`)**
| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Visitante | Acessa `/cadastro`. |
| **2** | Sistema | Exibe a tela de cadastro `site/auth/cadastro.html`. |
| **3** | Visitante | Preenche nome, sobrenome, e-mail, telefone, define uma senha e clica em "Cadastrar". |
| **4** | Sistema | [`LoginController.processRegister`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/LoginController.java) instancia `Cliente`. |
| **5** | Sistema (*RN-SEG-01*) | Aplica o hash criptográfico através de `novoCliente.criptografarSenha()`, prefixando `{sha256}`. |
| **6** | Sistema | Persiste a conta via `ClienteService.create(novoCliente)`. |
| **7** | Sistema | Redireciona para `/login?sucesso=true` com notificação de cadastro realizado. |

### **Parte B: Autenticação de Cliente (`/login`)**
| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **8** | Cliente | Insere e-mail e senha no formulário de login público (`/login`) e clica em "Entrar". |
| **9** | Sistema | Aplica hash SHA-256 na senha digitada (`PasswordUtil.hash(senha)`). |
| **10** | Sistema | Consulta a conta de `Usuario` pelo e-mail e compara a senha criptografada. |
| **11** | Sistema | Registra o usuário na sessão HTTP: `session.setAttribute("usuarioLogado", usuarioBanco)`. |
| **12** | Sistema (*RN-SEG-02*) | Identifica que o usuário possui perfil Cliente (`perfil_id == 4L`) e **redireciona para a Home (`/`)**. |

---

## 6. 🔀 Extensões e Ações Complementares

### **UC22 - Encerrar Sessão do Cliente (`/logout`)**
1. O cliente clica no botão "Sair" no cabeçalho.
2. A requisição atinge `GET /logout`.
3. O controlador obtém a sessão e invoca `session.invalidate()` (`RN-SEG-04`).
4. O cliente é redirecionado para a tela de login desautenticado.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Credenciais Inválidas no Login**
* **No Passo 10:** Senha incorreta ou e-mail não encontrado.
* **Ação do Sistema:** Adiciona mensagem `"E-mail ou senha inválidos."` e renderiza novamente `site/auth/login.html`.

### **FE02 - E-mail Já Cadastrado**
* **No Passo 6:** Tentativa de cadastro com e-mail já existente.
* **Ação do Sistema:** O sistema captura a exceção de violação de unicidade e retorna ao formulário com alerta explicativo.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-SEG-01** | Hashing Criptográfico Mandatório de Senhas | Todas as senhas de clientes são gravadas sob o algoritmo SHA-256 com prefixo `{sha256}`. |
| **RN-SEG-02** | Bloqueio de Clientes nas Rotas Administrativas | Usuários do perfil Cliente (ID 4) são mantidos estritamente na interface pública (`/`). |
| **RN-SEG-04** | Encerramento de Sessão (Logout) | A chamada a `/logout` destrói completamente o estado da sessão via `session.invalidate()`. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas (Cadastro):
* `nome` *(String, Obrigatório)*.
* `sobrenome` *(String, Obrigatório)*.
* `email` *(String, Obrigatório)*.
* `telefone` *(String, Obrigatório)*.
* `senha` *(String, Obrigatório, tipo Password)*.

### Saídas:
* Redirecionamento HTTP para `/login` (cadastro) ou `/` (login bem-sucedido).
* Mensagens de alerta amigáveis em caso de erro de credenciais.
