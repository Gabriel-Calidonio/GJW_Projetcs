# UC_ADM_006 - Gerenciar Pedidos da Loja Virtual

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_ADM_006` (engloba `UC14` e `UC15` em [`general_dashboard.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/general_dashboard.puml)) |
| **Nome** | Gerenciar Pedidos da Loja e Atualização de Status |
| **Módulo** | Módulos de Gestão Operacional |
| **Atores Primários** | Administrador Geral (*Perfil 1*), Recepcionista (*Perfil 3*) |
| **Atores Secundários** | Sistema de Pedidos (`AdminPedidoController`, `PedidoService`) |
| **Tipo** | Essencial / Concreto (com Extensão de Atualização de Status) |
| **Frequência de Uso** | Diária (a cada nova compra gerada no e-commerce ou retirada presencial) |
| **Rastreabilidade** | [`AdminPedidoController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminPedidoController.java), [`RN-EST-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_loja_estoque.md#rn-est-01-valida%C3%A7%C3%A3o-e-trava-de-estoque-dispon%C3%ADvel), [`RN-EST-03`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_loja_estoque.md#rn-est-03-identifica%C3%A7%C3%A3o-de-comprador-cliente-vs-visitante), [`RN-EST-04`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_loja_estoque.md#rn-est-04-atomicidade-e-limpeza-de-sess%C3%A3o) |

---

## 1. 🎯 Descrição Sumária

Permite que a equipe de atendimento acompanhe as ordens de compra originadas na vitrine virtual e-commerce da barbearia (`/MRYnZpAsC9sp/pedidos`). O operador visualiza a lista consolidada de pedidos ordenados dos mais recentes para os mais antigos, inspeciona o detalhamento de cada pedido (itens adquiridos, valores unitários congelados e identificação do comprador como cliente cadastrado ou visitante) e atualiza o ciclo operacional do pedido (`Aguardando Retirada` -> `Retirado` / `Cancelado`).

---

## 2. ⚡ Pré-Condições

1. Operador autenticado com sessão válida (`UC_ADM_001`).
2. Requisição autorizada pelo `AdminInterceptor` (`UC_ADM_002`).
3. Registros de pedidos existentes na tabela `tab_pedidos`.

---

## 3. ✅ Pós-Condições

1. A lista de pedidos é apresentada com filtros de status e dados sumarizados.
2. Em caso de alteração de status, o novo estado é persistido na base de dados e refletido imediatamente na tela de detalhes do pedido.

---

## 4. 🚀 Gatilho (Trigger)

* O operador clica na seção "Pedidos da Loja" no menu lateral do painel administrativo; OU
* Um cliente comparece ao balcão da barbearia para retirar produtos adquiridos na loja online.

---

## 5. 🔄 Fluxo Principal (Listagem de Pedidos)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Operador | Navega até `/MRYnZpAsC9sp/pedidos`. |
| **2** | Sistema | [`AdminPedidoController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminPedidoController.java) invoca `PedidoService.read(new Pedido())`. |
| **3** | Sistema | Ordena a lista de pedidos por ID de forma decrescente para priorizar as compras mais recentes. |
| **4** | Sistema | Carrega a view `admin/order/order/listar` enviando a coleção `pedidos` no modelo. |
| **5** | Operador | Visualiza ID do Pedido, Data da Compra, Comprador, Valor Total e Status Atual. |

---

## 6. 🔀 Extensões Operacionais

### **Visualizar Detalhes do Pedido (`/pedidos/detalhe?id={id}`)**
1. O operador clica no botão "Ver Detalhes" de um pedido na listagem.
2. O sistema busca a entidade `Pedido` pelo identificador primário.
3. Se o comprador for um cliente autenticado, exibe o nome e e-mail vinculado ao cadastro; se for checkout de visitante (`RN-EST-03`), exibe `nome_visitante` e `telefone_visitante`.
4. Carrega a coleção de `itens` contendo produto, quantidade e preço unitário congelado na data da venda.
5. Renderiza a view `admin/order/order/detalhe`.

### **UC15 - Atualizar Status do Pedido (`<<extend>>`)**
1. Na tela de detalhes do pedido, o operador seleciona o novo status no formulário (ex: `"Retirado"` quando o cliente pega os cosméticos no balcão, ou `"Cancelado"`).
2. O formulário submete via `POST /MRYnZpAsC9sp/pedidos/atualizar-status` com os parâmetros `id` e `status`.
3. O controlador recupera o pedido, define `pedido.setStatus(status)` e invoca `service.update(pedido)`.
4. O sistema redireciona de volta para `/MRYnZpAsC9sp/pedidos/detalhe?id={id}`, exibindo a situação atualizada.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Pedido Não Encontrado**
* Caso o identificador fornecido na URL de detalhes não exista no banco, a listagem é reexibida sem interromper a navegação da recepção.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-EST-03** | Identificação de Comprador | Trata pedidos associados a `cliente_id` ou identificados com `nome_visitante` e `telefone_visitante`. |
| **RN-EST-04** | Atomicidade de Pedido | Os itens de pedido exibidos na tela foram gerados atomicamente com a transação de compra. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas (Atualização de Status):
* `id` *(Long, Obrigatório)*: Identificador numérico do pedido.
* `status` *(String, Obrigatório)*: Novo estado (`Aguardando Retirada`, `Retirado`, `Cancelado`).

### Saídas:
* View de listagem com pedidos ordenados.
* View de detalhes com discriminação de itens, valores e comprador.
