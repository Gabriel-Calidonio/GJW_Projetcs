# UC_ADM_011 - Gerenciar Configurações da Barbearia

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_ADM_011` (ref. `UC20` em [`general_dashboard.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/general_dashboard.puml)) |
| **Nome** | Gerenciar Configurações Gerais e Parâmetros da Barbearia |
| **Módulo** | Módulos de Gestão Estratégica & Configurações |
| **Atores Primários** | Administrador Geral (*Perfil 1*) |
| **Atores Secundários** | Serviço de Configurações (`SettingService`), Mecanismo de Flash Messages |
| **Tipo** | Estratégico / Concreto |
| **Frequência de Uso** | Eventual (ajustes de horários de funcionamento, dados de contato ou políticas institucionais) |
| **Rastreabilidade** | [`AdminSettingController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminSettingController.java), [`SettingService.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/service/SettingService.java), [`RN-AGE-02`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_agendamento.md#rn-age-02-respeito-ao-hor%C3%A1rio-de-encerramento-do-expediente), [`RN-CAN-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_cancelamento_reagendamento.md#rn-can-01-prazo-limite-de-cancelamento-online-anteced%C3%AAncia-m%C3%ADnima) |

---

## 1. 🎯 Descrição Sumária

Permite exclusivamente ao Administrador Geral configurar os parâmetros operacionais e institucionais da barbearia (`/MRYnZpAsC9sp/configuracoes`). O sistema carrega as configurações persistidas sob o padrão chave-valor através do `SettingService`, viabilizando o ajuste de horários de abertura e fechamento semanal do salão, limites de tolerância de atraso, telefone de contato para WhatsApp, endereço físico e regras globais do estabelecimento. Os horários aqui configurados delimitam diretamente a geração e o encerramento da grade pública de agendamento (`RN-AGE-02`).

---

## 2. ⚡ Pré-Condições

1. Administrador autenticado no sistema (`UC_ADM_001`).
2. Requisição autorizada pelo `AdminInterceptor` com perfil exclusivo de Administrador (`perfil_id = 1L`, `ADMIN_ONLY`) (`UC_ADM_002`).

---

## 3. ✅ Pós-Condições

1. Os parâmetros chave-valor são atualizados na base de dados (`tab_setting`).
2. As novas configurações institucionais e limites de horário entram em vigor imediatamente para todos os módulos e clientes da vitrine online.

---

## 4. 🚀 Gatilho (Trigger)

* O Administrador clica em "Configurações da Barbearia" no menu do painel.

---

## 5. 🔄 Fluxo Principal (Consultar e Salvar Configurações)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Administrador | Acessa `/MRYnZpAsC9sp/configuracoes`. |
| **2** | Sistema | [`AdminSettingController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminSettingController.java) invoca `SettingService.getAllAsMap()`. |
| **3** | Sistema | Carrega a view `admin/setting/store-setting/form` populando o mapa de parâmetros nos inputs da tela. |
| **4** | Administrador | Edita parâmetros institucionais (nome, telefone, WhatsApp, horários de expediente, tolerâncias). |
| **5** | Administrador | Submete o formulário via `POST /salvar`. |
| **6** | Sistema | O controlador recebe o `Map<String, String>` com os parâmetros e invoca `SettingService.updateSettings(params)`. |
| **7** | Sistema | Adiciona flash attribute `"Configurações da barbearia atualizadas com sucesso!"`. |
| **8** | Sistema | Redireciona o navegador de volta para `/MRYnZpAsC9sp/configuracoes` exibindo mensagem de sucesso. |

---

## 6. 🔀 Fluxos Alternativos

### **FA01 - Configuração de Expediente Diferenciado**
* O Administrador altera o horário de encerramento do sábado (ex: de 19:00 para 16:00). Ao salvar, os novos agendamentos já são validados contra a nova regra de término (`RN-AGE-02`).

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Erro ao Persistir Parâmetros**
* Em caso de falha na escrita dos parâmetros na tabela `tab_setting`, o sistema captura a exceção, adiciona um flash attribute `"Erro ao salvar configurações: [mensagem]"` e retorna ao formulário preservando os dados informados.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-AGE-02** | Horário de Encerramento do Expediente | Os limites diários de funcionamento configurados aqui bloqueiam agendamentos que extrapolam o expediente. |
| **RN-CAN-01** | Prazo Limite de Cancelamento Online | Define a margem de tolerância e antecedência mínima de cancelamento permitida aos clientes. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Campos Chave-Valor Gerenciados:
* `nome_barbearia`: Nome oficial da unidade.
* `telefone_contato` / `whatsapp_atendimento`: Telefones para contato e suporte ao cliente.
* `horario_abertura` / `horario_fechamento`: Faixas padrão de expediente.
* `tempo_tolerancia_atraso`: Minutos tolerados antes de considerar o agendamento como no-show.

### Saídas:
* Mensagens flash de sucesso (`sucesso`) ou de falha (`erro`).
