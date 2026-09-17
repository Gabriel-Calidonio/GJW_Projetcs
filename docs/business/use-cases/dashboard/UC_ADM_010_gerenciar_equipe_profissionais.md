# UC_ADM_010 - Gerenciar Equipe de Profissionais

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_ADM_010` (ref. `UC19` em [`general_dashboard.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/general_dashboard.puml)) |
| **Nome** | Gerenciar Equipe de Profissionais e Barbeiros |
| **Módulo** | Módulos de Gestão Estratégica & Configurações |
| **Atores Primários** | Administrador Geral (*Perfil 1*) |
| **Atores Secundários** | Motor de Agendamento (`AgendamentoService`) |
| **Tipo** | Estratégico / Concreto |
| **Frequência de Uso** | Moderada (contratações, desligamentos ou alterações de especialidade) |
| **Rastreabilidade** | [`AdminProfissionalController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminProfissionalController.java), [`Profissional.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/model/domain/entities/Profissional.java), [`RN-AGE-04`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_agendamento.md#rn-age-04-atribui%C3%A7%C3%A3o-autom%C3%A1tica-qualquer-profissional) |

---

## 1. 🎯 Descrição Sumária

Permite ao Administrador gerenciar a equipe de colaboradores e barbeiros da barbearia (`/MRYnZpAsC9sp/profissionais`). O gestor cadastra novos profissionais, define suas especialidades (ex: Master Barber, Especialista em Barboterapia, Colorimetria), gerencia dados de contato e controla o status operacional (`status = true / false`). O status ativo do profissional é essencial para que sua grade de horários seja disponibilizada para reservas públicas e para a funcionalidade de atribuição automática ("Qualquer Profissional").

---

## 2. ⚡ Pré-Condições

1. Administrador autenticado no sistema (`UC_ADM_001`).
2. Requisição autorizada pelo `AdminInterceptor` (`UC_ADM_002`).

---

## 3. ✅ Pós-Condições

1. O profissional é cadastrado, atualizado ou inativado na tabela `tab_profissional`.
2. Apenas profissionais ativos figuram na lista de seleção da tela de agendamento online e na rotina de balanceamento de horários livres (`RN-AGE-04`).

---

## 4. 🚀 Gatilho (Trigger)

* O Administrador clica em "Profissionais" no menu administrativo.

---

## 5. 🔄 Fluxo Principal (Cadastrar e Gerenciar Barbeiros)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Administrador | Acessa `/MRYnZpAsC9sp/profissionais`. |
| **2** | Sistema | [`AdminProfissionalController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminProfissionalController.java) invoca `ProfissionalService.read(new Profissional())` e renderiza `admin/staff/listar`. |
| **3** | Administrador | Clica em "Novo Profissional" (`/novo`). |
| **4** | Sistema | Apresenta a view `admin/staff/create` com o campo `status` pré-selecionado como ativo (`true`). |
| **5** | Administrador | Informa nome completo, telefone/contato, especialidade e status. |
| **6** | Administrador | Submete o formulário via `POST /salvar`. |
| **7** | Sistema | Persiste via `create` (se novo) ou `update` (se edição) e retorna à listagem. |
| **8** | Administrador | Visualiza o profissional ativo na lista da equipe. |

---

## 6. 🔀 Fluxos Alternativos

### **FA01 - Inativação Temporária de Barbeiro (Férias / Licença)**
1. O Administrador acessa a edição do profissional (`/editar/{id}`).
2. Desmarca a flag "Ativo" (`status = false`).
3. Ao salvar, o barbeiro deixa de receber novos agendamentos na vitrine pública sem apagar seus agendamentos já concluídos.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Exclusão de Profissional com Agendamentos Existentes**
* Se o gestor tentar remover um profissional que possui histórico em `tab_agendamento`, o sistema previne a exclusão para preservar o histórico financeiro e relatórios de comissões, recomendando a inativação do status.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-AGE-04** | Atribuição Automática ("Qualquer Profissional") | O motor de agendamento consulta exclusivamente profissionais com `status = true` para compor as vagas disponíveis. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas:
* `nome` *(String, Obrigatório)*: Nome profissional do barbeiro.
* `especialidade` *(String, Opcional)*: Principais habilidades técnicas.
* `telefone` *(String, Opcional)*: Contato direto.
* `status` *(Boolean)*: `true` para ativo e elegível a agendamentos, `false` para inativo.

### Saídas:
* Tabela da equipe com status operacional e opções de edição/exclusão.
