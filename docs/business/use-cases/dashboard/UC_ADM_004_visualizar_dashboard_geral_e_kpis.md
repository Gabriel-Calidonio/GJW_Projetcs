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

Os 6 indicadores abaixo operam de maneira desacoplada através de blocos `try/catch` independentes no controlador:

### **UC05 - Consultar Total de Agendamentos**
* Computa a contagem de todos os registros de agendamento na tabela `tab_agendamento`.
* Permite ao gestor avaliar a demanda da agenda da barbearia.

### **UC06 - Consultar Total de Pedidos da Loja**
* Computa a contagem global de compras realizadas na loja virtual da barbearia.

### **UC07 - Consultar Total de Serviços Ativos**
* Retorna a quantidade de procedimentos capilares e de barba oferecidos no catálogo.

### **UC08 - Consultar Total de Produtos em Estoque**
* Retorna a quantidade de cosméticos, pomadas e kits cadastrados para venda.

### **UC09 - Consultar Total de Clientes Cadastrados**
* Apura o tamanho da carteira de clientes registrada no sistema.

### **UC10 - Consultar Total de Profissionais**
* Exibe o total de barbeiros e colaboradores ativos na equipe.

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
