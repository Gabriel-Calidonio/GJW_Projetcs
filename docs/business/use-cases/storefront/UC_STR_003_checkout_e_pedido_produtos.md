# UC_STR_003 - Realizar Checkout e Concluir Pedido de Produtos

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_STR_003` (engloba `UC09` a `UC14` em [`storefront.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/storefront.puml)) |
| **Nome** | Realizar Checkout de Produtos, Baixa Atômica de Estoque e Confirmação |
| **Módulo** | Checkout & Pedido de Produtos |
| **Atores Primários** | Visitante (*Público Geral*), Cliente Registrado (*Perfil 4*) |
| **Atores Secundários** | Sistema de Transação (`UnitOfWork`), Serviço de Pedidos (`PedidoService`) |
| **Tipo** | Essencial / Concreto (com Inclusões Transacionais e Pós-Venda) |
| **Frequência de Uso** | Alta (a cada finalização de compra de cosméticos no e-commerce) |
| **Rastreabilidade** | [`CarrinhoController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/CarrinhoController.java), [`UnitOfWork.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/service/transaction/UnitOfWork.java), [`RN-EST-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_loja_estoque.md#rn-est-01-valida%C3%A7%C3%A3o-e-trava-de-estoque-dispon%C3%ADvel), [`RN-EST-03`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_loja_estoque.md#rn-est-03-identifica%C3%A7%C3%A3o-de-comprador-cliente-vs-visitante), [`RN-EST-04`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_loja_estoque.md#rn-est-04-atomicidade-e-limpeza-de-sess%C3%A3o) |

---

## 1. 🎯 Descrição Sumária

Permite a finalização da compra dos cosméticos e kits acumulados no carrinho de compras (`/carrinho/checkout`). O caso de uso atende tanto clientes com cadastro ativo (associando o pedido ao seu perfil e pré-populando os dados de contato) quanto visitantes não autenticados (coletando nome e telefone para contato conforme `RN-EST-03`). A confirmação do pedido (`/carrinho/checkout/confirmar`) é executada dentro de uma **transação JDBC atômica (ACID)** gerenciada pelo `UnitOfWork`, gravando a ordem de compra em `tab_pedidos`, os itens com seus preços congelados em `tab_itens_pedido` e efetuando a baixa física em `tab_produto.estoque`. Após o sucesso da transação, o carrinho da sessão é esvaziado (`RN-EST-04`) e o usuário é direcionado para a tela de comprovante `/compra-confirmada`.

---

## 2. ⚡ Pré-Condições

1. O carrinho de compras na sessão do usuário deve conter ao menos 1 item (`carrinho.getQuantidadeTotal() > 0`).
2. Todos os produtos presentes no carrinho devem possuir saldo disponível em estoque no momento do fechamento da transação.

---

## 3. ✅ Pós-Condições

1. Registro mestre gerado em `tab_pedidos` com status inicial `"Aguardando Retirada"` e forma de pagamento selecionada.
2. Linhas de itens persistidas em `tab_itens_pedido` vinculando os produtos e quantidades compradas.
3. Saldo em `tab_produto.estoque` decrementado imediatamente pela quantidade vendida.
4. Carrinho de compras da sessão completamente esvaziado (`carrinho.limpar()`).
5. Redirecionamento para a view `/compra-confirmada?id={id}` apresentando o número do pedido e instruções de retirada.

---

## 4. 🚀 Gatilho (Trigger)

* O usuário clica em "Finalizar Compra" ou "Avançar para o Checkout" na página do carrinho.

---

## 5. 🔄 Fluxo Principal (Checkout e Conclusão Atômica do Pedido)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Usuário | Clica em "Finalizar Compra" em `/carrinho`. |
| **2** | Sistema | [`CarrinhoController.checkout`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/CarrinhoController.java) verifica o total de itens. Se zero, redireciona para `/loja`. |
| **3** | Sistema (*UC10*) | Identifica o perfil do comprador (`RN-EST-03`): se houver `usuarioLogado` na sessão, injeta os dados do `Cliente` no modelo; se for visitante, habilita os campos de identificação avulsa. |
| **4** | Sistema | Renderiza a view `site/cart/carrinho-checkout.html` exibindo o resumo do pedido e as opções de pagamento. |
| **5** | Usuário (*UC11*) | Seleciona a forma de pagamento (PIX, Dinheiro na Retirada ou Cartão), informa seus dados (se visitante) e submete o formulário via botão "Confirmar Pedido". |
| **6** | Sistema (*UC12*) | O método `confirmar` recebe a requisição POST em `/carrinho/checkout/confirmar`. |
| **7** | Sistema | Inicia transação JDBC através de `UnitOfWork`. |
| **8** | Sistema | Instancia a entidade `Pedido`, define o status `"Aguardando Retirada"`, data/hora atual e associa o `Cliente` (ou `nomeVisitante` + `telefoneVisitante`). |
| **9** | Sistema | Itera sobre os itens do carrinho, cria as instâncias de `ItemPedido` e abate o saldo físico de estoque em `tab_produto.estoque` com trava atômica (`RN-EST-01`). |
| **10** | Sistema | Executa `PedidoService.create(pedido)` e efetua o *Commit* da transação atômica. |
| **11** | Sistema (*UC14*) | Invoca `carrinho.limpar()`, esvaziando a sessão HTTP do comprador (`RN-EST-04`). |
| **12** | Sistema (*UC13*) | Redireciona o navegador para `/compra-confirmada?id={pedidoId}`. |
| **13** | Usuário | Visualiza a confirmação com o número do pedido, itens e orientações para retirada na barbearia. |

---

## 6. 🔀 Casos de Uso Incluídos (`<<include>>`)

### **UC10 - Identificar Comprador (RN-EST-03)**
* **Cliente Autenticado:** Vincula a chave estrangeira `pedido.cliente_id`. O pedido passa a figurar automaticamente no histórico de compras do cliente em `/meus-agendamentos`.
* **Visitante:** Registra os campos de contato avulsos `nome_visitante` e `telefone_visitante`, possibilitando que a recepção contate o cliente sem exigir a criação prévia de conta.

### **UC11 - Selecionar Forma de Pagamento**
* Permite a escolha entre:
  * `PIX`: Pagamento instantâneo digital.
  * `Dinheiro`: Pagamento presencial no ato da retirada no balcão da barbearia.
  * `Cartão (Débito/Crédito)`: Maquininha física no salão.

### **UC14 - Esvaziar Carrinho Pós-Venda (RN-EST-04)**
* Garante que o carrinho só seja esvaziado após o sucesso incontestável da transação de persistência do pedido, evitando perda de carrinho em caso de falha de conexão.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Saldo de Estoque Esgotado Concorrentemente (RN-EST-01)**
* **No Passo 9:** Outro comprador ou atendimento de balcão arrematou o último item físico no intervalo entre a adição ao carrinho e o fechamento do checkout.
* **Ação do Sistema:**
  1. A validação atômica detecta saldo insuficiente (`tab_produto.estoque < quantidadeComprada`).
  2. O `UnitOfWork` executa **Rollback imediato** de todas as operações pendentes.
  3. O carrinho da sessão é preservado intacto.
  4. O sistema redireciona para `/carrinho/checkout` com mensagem de erro explicando a insuficiência do lote.

### **FE02 - Falha de Comunicação com o Banco de Dados**
* Qualquer falha de I/O de rede ou banco aborta a transação com Rollback seguro, informando o usuário sem duplicar registros de pedido.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-EST-01** | Trava de Estoque Disponível | Validação atômica e baixa física em estoque no momento do fechamento do pedido. |
| **RN-EST-03** | Identificação de Comprador | Trata a vinculação a `cliente_id` para usuários autenticados ou campos de visitante para anônimos. |
| **RN-EST-04** | Atomicidade e Limpeza de Sessão | Inserção do pedido, itens e baixa de estoque na mesma transação; limpeza de sessão apenas pós-commit. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas do Formulário (`/carrinho/checkout/confirmar`):
* `nomeVisitante` *(String, Condicional)*: Obrigatório se o comprador for visitante não logado.
* `telefoneVisitante` *(String, Condicional)*: WhatsApp/Telefone para aviso de retirada.
* `formaPagamento` *(String, Obrigatório)*: `PIX`, `Dinheiro` ou `Cartão`.

### Saídas:
* Registro persistido de `Pedido` com ID autoincrementado.
* Tela de confirmação `site/cart/compra-confirmada.html` com resumo do pedido.
