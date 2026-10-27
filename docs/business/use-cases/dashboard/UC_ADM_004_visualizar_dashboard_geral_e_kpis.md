# UC_ADM_004 - Visualizar Dashboard Geral e KPIs em Tempo Real

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_ADM_004` (engloba `UC04` a `UC10` em [`general_dashboard.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/general_dashboard.puml)) |
| **Nome** | Visualizar Dashboard Geral e Indicadores em Tempo Real |
| **Módulo** | Dashboard & Indicadores em Tempo Real |
| **Atores Primários** | Administrador Geral (*Perfil 1*), Recepcionista (*Perfil 3*) |
| **Atores Secundários** | `ServiceRegistry` (Camada de Serviços do Sistema) |
| **Tipo** | Essencial / Concreto (com Inclusões Estruturadas) |
| **Frequência de Uso** | Muito Alta (tela inicial do painel e hub central de monitoramento diário) |
| **Rastreabilidade** | [`AdminDashboardController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminDashboardController.java), [`RNF001`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/requirements/non_functional/RNF001_performance.md), [`RNF004`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/requirements/non_functional/RNF004_usabilidade_e_responsividade.md) |

---

## 1. 🎯 Descrição Sumária

Apresenta a visão executiva e operacional consolidada da barbearia. Ao acessar o endpoint raiz do painel administrativo (`/MRYnZpAsC9sp`), o sistema consulta dinamicamente os serviços da aplicação via `ServiceRegistry` para computar e exibir 6 indicadores essenciais (KPIs) em tempo real:
1. **UC05:** Total de Agendamentos Registrados (`totalAgendamentos`)
2. **UC06:** Total de Pedidos Realizados na Loja Virtual (`totalPedidos`)
3. **UC07:** Total de Serviços Cadastrados no Catálogo (`totalServicos`)
4. **UC08:** Total de Produtos Físicos / Cosméticos no Inventário (`totalProdutos`)
5. **UC09:** Total de Clientes na Base de Dados (`totalClientes`)
6. **UC10:** Total de Profissionais e Barbeiros da Equipe (`totalProfissionais`)

Além dos cartões de métricas, o Dashboard atua como *Hub de Navegação*, disponibilizando links de acesso rápido aos módulos operacionais e de gestão estratégica conforme o perfil do usuário logado.

---

## 2. ⚡ Pré-Condições

1. O usuário deve estar autenticado com sessão válida (`UC_ADM_001`).
2. A requisição deve ser autorizada pelo `AdminInterceptor` (`UC_ADM_002`).
3. Os serviços de domínio correspondentes devem estar registrados no `ServiceRegistry`.

---

## 3. ✅ Pós-Condições

1. A página do Dashboard (`admin/dashboard/index.html`) é renderizada com todos os cards e valores atualizados.
2. O operador visualiza os números consolidados e pode navegar para os módulos de detalhamento com um único clique.

---

## 4. 🚀 Gatilho (Trigger)

* Conclusão bem-sucedida do fluxo de login em `/MRYnZpAsC9sp/login`; OU
* O operador clica no logotipo da barbearia ou no menu "Dashboard" no cabeçalho.

---

## 5. 🔄 Fluxo Principal (Carregamento e Exibição do Dashboard)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Operador | Requisita a URL `/MRYnZpAsC9sp` (ou clica em "Dashboard"). |
| **2** | Sistema | [`AdminInterceptor`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminInterceptor.java) valida a sessão e autoriza a requisição. |
| **3** | Sistema | [`AdminDashboardController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminDashboardController.java) inicia o processamento dos 6 KPIs: |
| **3.1** | *UC05* | Obtém `AgendamentoService` via `ServiceRegistry`, invoca `read(new Agendamento())` e armazena `totalAgendamentos` no `Model`. |
| **3.2** | *UC06* | Obtém `PedidoService` via `ServiceRegistry`, invoca `read(new Pedido())` e armazena `totalPedidos` no `Model`. |
| **3.3** | *UC07* | Obtém `ServicoService` via `ServiceRegistry`, invoca `read(new Servico())` e armazena `totalServicos` no `Model`. |
| **3.4** | *UC08* | Obtém `ProdutoService` via `ServiceRegistry`, invoca `read(new Produto())` e armazena `totalProdutos` no `Model`. |
| **3.5** | *UC09* | Obtém `ClienteService` via `ServiceRegistry`, invoca `read(new Cliente())` e armazena `totalClientes` no `Model`. |
| **3.6** | *UC10* | Obtém `ProfissionalService` via `ServiceRegistry`, invoca `read(new Profissional())` e armazena `totalProfissionais` no `Model`. |
| **4** | Sistema | Encaminha o modelo populado para a view Thymeleaf `admin/dashboard/index`. |
| **5** | Operador | Visualiza o painel com as métricas e os atalhos operacionais. |

---

## 6. 🔀 Casos de Uso Incluídos (`<<include>>`)

Os 6 indicadores essenciais (KPIs) computados no Dashboard operam de maneira desacoplada através de blocos `try/catch` independentes no controlador [`AdminDashboardController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminDashboardController.java), assegurando alta resiliência e tolerância a falhas parciais:

### **UC05 - Consultar Total de Agendamentos**
* **Objetivo:** Computar o volume acumulado de reservas de horários registradas no sistema.
* **Mecanismo de Execução:** Invocação de `AgendamentoService.read(new Agendamento())` via `ServiceRegistry.getService(Agendamento.class)`.
* **Entidade de Dados:** `tab_agendamento`.
* **Atributo no Model:** `totalAgendamentos` *(Integer)*.
* **Relevância Operacional:** Permite ao gestor monitorar a taxa de ocupação das cadeiras da barbearia e subsidiar decisões de escala e dimensionamento da equipe.
* **Comportamento em Falha:** Caso ocorra indisponibilidade temporária na tabela, o sistema registra warning no log e injeta `0` no modelo, mantendo o carregamento dos demais cards.

### **UC06 - Consultar Total de Pedidos da Loja**
* **Objetivo:** Computar a contagem de compras geradas na vitrine virtual e-commerce da barbearia.
* **Mecanismo de Execução:** Invocação de `PedidoService.read(new Pedido())` via `ServiceRegistry.getService(Pedido.class)`.
* **Entidade de Dados:** `tab_pedidos`.
* **Atributo no Model:** `totalPedidos` *(Integer)*.
* **Relevância Operacional:** Fornece visibilidade imediata da tração de vendas de produtos cosméticos e kits, alertando a recepção sobre a existência de ordens a processar para retirada no balcão.
* **Comportamento em Falha:** Retorna `0` em caso de erro, garantindo a renderização do painel.

### **UC07 - Consultar Total de Serviços Ativos**
* **Objetivo:** Retornar a quantidade de procedimentos capilares e de barba cadastrados e disponíveis no catálogo.
* **Mecanismo de Execução:** Invocação de `ServicoService.read(new Servico())` via `ServiceRegistry.getService(Servico.class)`.
* **Entidade de Dados:** `tab_servico`.
* **Atributo no Model:** `totalServicos` *(Integer)*.
* **Relevância Operacional:** Assegura que o catálogo corporativo possui serviços vigentes e atua como atalho para o módulo de cadastro e precificação de serviços (`UC_ADM_008`).

### **UC08 - Consultar Total de Produtos em Estoque**
* **Objetivo:** Apurar o número de itens físicos e kits promocionais cadastrados no inventário.
* **Mecanismo de Execução:** Invocação de `ProdutoService.read(new Produto())` via `ServiceRegistry.getService(Produto.class)`.
* **Entidade de Dados:** `tab_produto`.
* **Atributo no Model:** `totalProdutos` *(Integer)*.
* **Relevância Operacional:** Subsidia o controle de sortimento de produtos comercializáveis e previne ruptura de estoque de cosméticos de alta rotatividade.

### **UC09 - Consultar Total de Clientes Cadastrados**
* **Objetivo:** Mensurar a carteira consolidada de clientes cadastrados na base de dados.
* **Mecanismo de Execução:** Invocação de `ClienteService.read(new Cliente())` via `ServiceRegistry.getService(Cliente.class)`.
* **Entidade de Dados:** `tab_cliente` vinculada a `tab_usuario`.
* **Atributo no Model:** `totalClientes` *(Integer)*.
* **Relevância Operacional:** Indicador chave de crescimento da barbearia, retenção e alcance da base de consumidores fidelizados.

### **UC10 - Consultar Total de Profissionais**
* **Objetivo:** Exibir a quantidade de barbeiros e especialistas que compõem o corpo técnico da barbearia.
* **Mecanismo de Execução:** Invocação de `ProfissionalService.read(new Profissional())` via `ServiceRegistry.getService(Profissional.class)`.
* **Entidade de Dados:** `tab_profissional`.
* **Atributo no Model:** `totalProfissionais` *(Integer)*.
* **Relevância Operacional:** Quantifica a capacidade instalada de atendimento simultâneo do estabelecimento.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Falha no Cálculo de um KPI Específico**
* **No Passo 3 (qualquer subitem):** Caso uma consulta de entidade lance uma exceção (ex: tabela temporariamente indisponível ou erro de mapeamento), o bloco `catch` correspondente intercepta a falha, atribui o valor numérico `0` ao modelo e permite que os demais KPIs continuem sendo calculados normalmente, sem interromper a exibição do dashboard para o usuário.

---

## 8. 📜 Regras de Negócio e Diretrizes de Performance

* **Tolerância a Falhas:** Nenhuma falha pontual em uma consulta de KPI deve resultar em erro HTTP 500 para a tela inteira do Dashboard.
* **Segurança de Acesso:** Clientes comuns são impedidos de visualizar o dashboard (`RN-SEG-02`).

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas:
* Requisição HTTP GET para `/MRYnZpAsC9sp` ou `/MRYnZpAsC9sp/`.

### Saídas (Atributos injetados no Modelo Thymeleaf):
* `totalAgendamentos` *(Integer)*: Volume total de agendamentos.
* `totalPedidos` *(Integer)*: Quantidade de pedidos gerados.
* `totalClientes` *(Integer)*: Total de clientes cadastrados.
* `totalProdutos` *(Integer)*: Itens de produtos/kits cadastrados.
* `totalServicos` *(Integer)*: Total de serviços no catálogo.
* `totalProfissionais` *(Integer)*: Total de barbeiros/profissionais cadastrados.
