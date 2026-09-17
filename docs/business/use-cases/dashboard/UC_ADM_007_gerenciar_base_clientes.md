# UC_ADM_007 - Gerenciar Base de Clientes

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_ADM_007` (ref. `UC16` em [`general_dashboard.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/general_dashboard.puml)) |
| **Nome** | Gerenciar Base de Clientes da Barbearia |
| **Módulo** | Módulos de Gestão Operacional |
| **Atores Primários** | Administrador Geral (*Perfil 1*), Recepcionista (*Perfil 3*) |
| **Atores Secundários** | Serviço de Clientes (`ClienteService`) |
| **Tipo** | Essencial / Concreto |
| **Frequência de Uso** | Alta (durante o atendimento e cadastramento de novos clientes) |
| **Rastreabilidade** | [`AdminClienteController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminClienteController.java), [`Cliente.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/model/domain/entities/Cliente.java), [`RN-SEG-03`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-03-autoriza%C3%A7%C3%A3o-granular-por-entidade-no-crud-din%C3%A2mico) |

---

## 1. 🎯 Descrição Sumária

Fornece a interface administrativa para gerenciamento do cadastro dos clientes da barbearia (`/MRYnZpAsC9sp/clientes`). O operador com permissão `GERENCIAR_CLIENTES` pode consultar a base completa de clientes, pesquisar dados de contato (e-mail, telefone), cadastrar novos clientes manualmente no balcão da recepção, atualizar informações cadastrais e inativar/excluir registros quando necessário.

---

## 2. ⚡ Pré-Condições

1. Operador autenticado com sessão válida (`UC_ADM_001`).
2. Permissão de acesso `GERENCIAR_CLIENTES` validada pelo `AdminInterceptor` (`UC_ADM_002`).

---

## 3. ✅ Pós-Condições

1. O cadastro do cliente é criado, atualizado ou removido na base de dados (`tab_cliente` / `tab_usuario`).
2. Clientes cadastrados pela recepção tornam-se imediatamente aptos para associação em novos agendamentos e pedidos.

---

## 4. 🚀 Gatilho (Trigger)

* O operador clica na opção "Clientes" no menu administrativo; OU
* Um cliente novo solicita abertura de ficha cadastral na barbearia.

---

## 5. 🔄 Fluxo Principal (Consultar e Cadastrar Cliente)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Operador | Acessa a rota `/MRYnZpAsC9sp/clientes`. |
| **2** | Sistema | [`AdminClienteController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminClienteController.java) invoca `ClienteService.read(new Cliente())` e exibe a lista na view `admin/customer/customer/listar`. |
| **3** | Operador | Clica em "Novo Cliente" (`/MRYnZpAsC9sp/clientes/novo`). |
| **4** | Sistema | Apresenta o formulário de cadastro (`admin/customer/customer/form`). |
| **5** | Operador | Preenche nome, sobrenome, e-mail, telefone e submete o formulário (`POST /salvar`). |
| **6** | Sistema | Seta `nomeUsuario` igual ao `email`, persiste via `ClienteService.create(cliente)` e redireciona para a listagem. |
| **7** | Operador | Visualiza o novo cliente na tabela com seus dados atualizados. |

---

## 6. 🔀 Fluxos Alternativos

### **FA01 - Edição de Dados do Cliente (`/editar/{id}`)**
1. O operador localiza o cliente na listagem e clica em "Editar".
2. O sistema carrega os dados atuais no formulário.
3. O operador modifica as informações de contato e confirma.
4. O sistema executa `ClienteService.update(cliente)` e retorna à listagem.

### **FA02 - Exclusão de Cadastro (`/excluir/{id}`)**
1. O operador solicita a exclusão de um registro.
2. O sistema remove o registro ou valida restrições de integridade referencial com histórico de agendamentos passados.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Tentativa de Cadastro com E-mail Já Existente**
* Caso o e-mail informado já esteja registrado em outro cliente ou usuário, o sistema impede a duplicação e retorna alerta amigável de e-mail já cadastrado.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-SEG-03** | Autorização Granular | O acesso à rota `/clientes` é condicionado à posse da permissão `GERENCIAR_CLIENTES`. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Campos do Formulário:
* `nome` *(String, Obrigatório)*: Primeiro nome do cliente.
* `sobrenome` *(String, Opcional)*: Sobrenome do cliente.
* `email` *(String, Obrigatório)*: E-mail único de contato e identificação.
* `telefone` *(String, Opcional)*: Telefone celular / WhatsApp.

### Saídas:
* Listagem tabular de clientes.
* Mensagens de confirmação de cadastro ou erro de validação.
