# UC_STR_004 - Consultar Catálogo de Serviços e Disponibilidade de Horários

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_STR_004` (engloba `UC15` e `UC16` em [`storefront.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/storefront.puml)) |
| **Nome** | Consultar Catálogo de Serviços e Disponibilidade de Horários em Tempo Real |
| **Módulo** | Vitrine de Serviços e Agendamento Online Autônomo |
| **Atores Primários** | Visitante (*Público Geral*), Cliente Registrado (*Perfil 4*) |
| **Atores Secundários** | Motor de Disponibilidade (`AgendamentoService`), API REST de Agendamento (`AgendamentoController`) |
| **Tipo** | Essencial / Concreto (com Consulta Assíncrona via API) |
| **Frequência de Uso** | Muito Alta (passo preliminar indispensável para marcação de corte ou barba) |
| **Rastreabilidade** | [`Router.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/Router.java), [`AgendamentoController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AgendamentoController.java), [`AgendamentoService.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/service/AgendamentoService.java), [`RN-AGE-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_agendamento.md#rn-age-01-ocupa%C3%A7%C3%A3o-de-m%C3%BAltiplos-blocos-consecutivos) a [`RN-AGE-04`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_agendamento.md#rn-age-04-atribui%C3%A7%C3%A3o-autom%C3%A1tica-qualquer-profissional) |

---

## 1. 🎯 Descrição Sumária

Permite que o cliente consulte os serviços oferecidos pela barbearia (`/servicos`), selecione um barbeiro de sua preferência (ou a opção flexível "Qualquer Barbeiro Livre") e interaja com um calendário dinâmico para consultar os horários vagos em tempo real. A consulta de vagas dispara requisições assíncronas à API REST (`/api/agendamentos/disponibilidade`), onde o motor de agendamento calcula dinamicamente os slots de **20 minutos** necessários com base na duração do serviço escolhido (`RN-AGE-01`), filtrando horários no passado (`RN-AGE-03`), respeitando os horários de encerramento (`RN-AGE-02`) e avaliando a escala dos profissionais ativos (`RN-AGE-04`).

---

## 2. ⚡ Pré-Condições

1. A aplicação deve possuir serviços ativos e ao menos um profissional ativo cadastrado na equipe.
2. A barbearia deve possuir expediente configurado para o dia da semana consultado (`tab_dias_funcionamento`).

---

## 3. ✅ Pós-Condições

1. O cliente visualiza a lista de procedimentos com duração e preço.
2. Ao escolher data e profissional, os botões com os horários disponíveis são renderizados para seleção.

---

## 4. 🚀 Gatilho (Trigger)

* O usuário clica em "Agendar Horário" ou "Serviços" no menu superior da barbearia.

---

## 5. 🔄 Fluxo Principal (Consulta de Catálogo e Vagas na Grade)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Usuário | Acessa `/servicos`. |
| **2** | Sistema (*UC15*) | [`Router.servico`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/Router.java) consulta a lista de serviços e a lista de profissionais ativos e renderiza `site/booking/servicos.html`. |
| **3** | Usuário | Seleciona o serviço desejado (ex: *Corte + Barba*, duração 40 min). |
| **4** | Usuário | Escolhe um profissional específico OU deixa selecionada a opção *"Qualquer profissional livre"* (`profissionalId = null` ou `0`). |
| **5** | Usuário | Escolhe uma data no calendário (ex: hoje ou data futura). |
| **6** | Interface (JS) (*UC16*) | Dispara requisição GET assíncrona para `/api/agendamentos/disponibilidade?servicoId={id}&profissionalId={id}&data={data}`. |
| **7** | Sistema | [`AgendamentoService.getHorariosDisponiveis`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/service/AgendamentoService.java) processa as regras de negócio: |
| **7.1** | *RN-AGE-01* | Calcula a quantidade de blocos necessários: $\lceil \text{duracao} / 20 \rceil$. |
| **7.2** | *RN-AGE-02* | Descarta horários cujo término extrapole o fechamento do dia. |
| **7.3** | *RN-AGE-03* | Se a data for hoje, descarta horários que já passaram da hora atual. |
| **7.4** | *RN-AGE-04* | Se for "Qualquer Profissional", valida se ao menos um barbeiro ativo possui todos os blocos consecutivos livres. |
| **8** | Sistema | Retorna lista JSON com os slots de horários indicando `hora` e `disponivel: true/false`. |
| **9** | Interface (JS) | Habilita os botões clicáveis dos horários livres para que o usuário avance para o checkout de agendamento. |

---

## 6. 🔀 Fluxos Alternativos

### **FA01 - Seleção Direta de Serviço via Parâmetro de URL**
* **Condição:** O usuário clica em um atalho na Home (ex: `/servicos?servicoId=2`).
* **Ação do Sistema:** A página já é carregada com o serviço pré-selecionado, abrindo diretamente o seletor de data.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Dia Sem Expediente (Barbearia Fechada)**
* **No Passo 7:** O usuário escolhe um domingo ou feriado em que a barbearia não opera (`tab_dias_funcionamento` inexistente ou fechado).
* **Ação do Sistema:** O serviço retorna lista vazia de slots e a interface exibe a mensagem: *"Não há expediente da barbearia nesta data. Por favor, escolha outro dia."*

### **FE02 - Todos os Horários Ocupados (Agenda Lotada)**
* Se todos os barbeiros estiverem com suas agendas confirmadas para a data, os botões são marcados como indisponíveis, sugerindo ao cliente selecionar outra data.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-AGE-01** | Ocupação de Múltiplos Blocos Consecutivos | Valida se a sequência contígua de slots de 20 minutos está desocupada. |
| **RN-AGE-02** | Respeito ao Horário de Encerramento | Bloqueia horários cujo término ultrapassaria a hora final de funcionamento. |
| **RN-AGE-03** | Bloqueio de Horários no Passado | Elimina slots anteriores à hora atual do dia corrente. |
| **RN-AGE-04** | Atribuição Automática | Avalia profissionais ativos de forma transparente quando o cliente não escolhe um barbeiro específico. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas da Consulta de Disponibilidade:
* `servicoId` *(Long, Obrigatório)*: Procedimento escolhido.
* `profissionalId` *(Long, Opcional)*: Barbeiro ou zero/nulo para balanceamento automático.
* `data` *(String no formato `YYYY-MM-DD`, Obrigatório)*: Data pretendida.

### Saídas (Payload JSON):
* Array de objetos contendo:
  * `hora` *(String, ex: "09:00", "09:20", "09:40")*.
  * `disponivel` *(Boolean)*.
