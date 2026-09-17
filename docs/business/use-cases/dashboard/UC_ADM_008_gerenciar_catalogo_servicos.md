# UC_ADM_008 - Gerenciar Catálogo de Serviços

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_ADM_008` (ref. `UC17` em [`general_dashboard.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/general_dashboard.puml)) |
| **Nome** | Gerenciar Catálogo de Serviços da Barbearia |
| **Módulo** | Módulos de Gestão Estratégica & Configurações |
| **Atores Primários** | Administrador Geral (*Perfil 1*) |
| **Atores Secundários** | Motor de Grade de Horários (`AgendamentoService`) |
| **Tipo** | Estratégico / Concreto |
| **Frequência de Uso** | Moderada (ao cadastrar novos procedimentos, reajustar tabelas de preços ou tempos de atendimento) |
| **Rastreabilidade** | [`AdminServicoController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminServicoController.java), [`Servico.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/model/domain/entities/Servico.java), [`RN-AGE-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_agendamento.md#rn-age-01-ocupa%C3%A7%C3%A3o-de-m%C3%BAltiplos-blocos-consecutivos), [`RN-SEG-03`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-03-autoriza%C3%A7%C3%A3o-granular-por-entidade-no-crud-din%C3%A2mico) |

---

## 1. 🎯 Descrição Sumária

Permite ao Administrador gerenciar a tabela de serviços oferecidos pela barbearia (`/MRYnZpAsC9sp/servicos`), incluindo cortes tradicionais, barboterapia, pigmentação, química e combos promocionais. O gestor pode cadastrar novos serviços, definir nomes comerciais, descrições detalhadas, valores em reais e o tempo estimado de execução em minutos (`duracao`). A duração informada afeta diretamente o cálculo de blocos de 20 minutos na grade de horários da agenda.

---

## 2. ⚡ Pré-Condições

1. Administrador autenticado no sistema (`UC_ADM_001`).
2. Permissão de gestão de serviços autorizada pelo `AdminInterceptor` (`UC_ADM_002`).

---

## 3. ✅ Pós-Condições

1. O serviço é inserido ou atualizado na base de dados (`tab_servico`).
2. O catálogo público e o formulário de agendamento passam a refletir instantaneamente a nova lista de procedimentos, seus preços e durações atualizadas.

---

## 4. 🚀 Gatilho (Trigger)

* O Administrador clica em "Serviços" no menu de gestão do painel administrativo.

---

## 5. 🔄 Fluxo Principal (Cadastrar e Gerenciar Serviços)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Administrador | Acessa `/MRYnZpAsC9sp/servicos`. |
| **2** | Sistema | [`AdminServicoController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminServicoController.java) busca os registros com `ServicoService.read(new Servico())` e exibe na view `admin/catalog/service/listar`. |
| **3** | Administrador | Clica em "Novo Serviço" (`/novo`). |
| **4** | Sistema | Inicializa uma instância padrão de `Servico` com `ativo = true` e `duracao = 30` minutos, exibindo a view `admin/catalog/service/form`. |
| **5** | Administrador | Preenche o título do serviço, descrição, preço (R$) e a duração em minutos. |
| **6** | Administrador | Submete o formulário via `POST /salvar`. |
| **7** | Sistema | Persiste o serviço via `create` (se novo) ou `update` (se já existente) e redireciona para a listagem. |
| **8** | Administrador | Visualiza o serviço disponível no catálogo corporativo. |

---

## 6. 🔀 Fluxos Alternativos

### **FA01 - Inativação ou Alteração de Preço (`/editar/{id}`)**
1. O Administrador acessa a edição de um serviço existente.
2. Altera o preço ou desmarca o campo "Ativo" para suspendê-lo temporariamente da vitrine sem perder o histórico de agendamentos.
3. Salva as alterações com sucesso.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Exclusão de Serviço com Agendamentos Vinculados**
* Se o gestor tentar excluir um serviço que já possui histórico de agendamentos na tabela `tab_agendamento`, o sistema previne a quebra de integridade referencial mantendo a consistência dos relatórios.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-AGE-01** | Ocupação de Blocos Consecutivos | O campo `duracao` determina quantos slots consecutivos de 20 minutos serão exigidos na grade de horários pública. |
| **RN-SEG-03** | Autorização Granular | Módulo restrito a quem possui perfil/permissão `GERENCIAR_SERVICOS`. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas:
* `nome` *(String, Obrigatório)*: Título do serviço (ex: "Corte Degradê Navalhado").
* `descricao` *(String, Opcional)*: Detalhes do atendimento.
* `preco` *(Double/BigDecimal, Obrigatório)*: Valor cobrado pelo procedimento.
* `duracao` *(Integer, Obrigatório)*: Tempo de atendimento em minutos (ex: 20, 40, 60).
* `ativo` *(Boolean)*: Flag de visibilidade e disponibilidade para agendamento.

### Saídas:
* Listagem de serviços ativos/inativos.
* Formulário de cadastro/edição com campos populados.
