# UC_ADM_009 - Gerenciar Produtos e Estoque

## 📋 Informações do Caso de Uso

| Atributo | Detalhe |
| :--- | :--- |
| **Identificador** | `UC_ADM_009` (ref. `UC18` em [`general_dashboard.puml`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/use-cases/general_dashboard.puml)) |
| **Nome** | Gerenciar Catálogo de Produtos e Controle de Estoque |
| **Módulo** | Módulos de Gestão Estratégica & Configurações |
| **Atores Primários** | Administrador Geral (*Perfil 1*) |
| **Atores Secundários** | Sistema de Inventário e Loja Virtual (`ProdutoService`) |
| **Tipo** | Estratégico / Concreto |
| **Frequência de Uso** | Regular (ao receber novas remessas de cosméticos, cadastrar kits ou ajustar saldos) |
| **Rastreabilidade** | [`AdminProdutoController.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminProdutoController.java), [`Produto.java`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/model/domain/entities/Produto.java), [`RN-EST-01`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_loja_estoque.md#rn-est-01-valida%C3%A7%C3%A3o-e-trava-de-estoque-dispon%C3%ADvel), [`RN-EST-02`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_loja_estoque.md#rn-est-02-estrutura-de-kits-promocionais), [`RN-SEG-03`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/docs/business/regras_negocio_seguranca_perfis.md#rn-seg-03-autoriza%C3%A7%C3%A3o-granular-por-entidade-no-crud-din%C3%A2mico) |

---

## 1. 🎯 Descrição Sumária

Permite ao Administrador gerenciar o inventário físico e os produtos comercializados na barbearia e no e-commerce (`/MRYnZpAsC9sp/produtos`). Compreende o cadastro de cosméticos individuais (pomadas, ceras, óleos e balms de barba), kits promocionais combinados, definição de preços de venda e manutenção da quantidade física disponível em estoque (`tab_produto.estoque`). O saldo aqui administrado serve de base para as travas atômicas durante o checkout dos clientes.

---

## 2. ⚡ Pré-Condições

1. Administrador autenticado no sistema (`UC_ADM_001`).
2. Requisição autorizada pelo `AdminInterceptor` com permissão `GERENCIAR_ESTOQUE` ou perfil Administrador (`UC_ADM_002`).

---

## 3. ✅ Pós-Condições

1. O produto ou kit promocional é criado, alterado ou excluído na base de dados (`tab_produto`).
2. O catálogo da loja virtual passa a exibir a nova precificação e limita a compra à nova quantidade disponível em estoque.

---

## 4. 🚀 Gatilho (Trigger)

* O Administrador acessa "Produtos" no menu do painel administrativo.

---

## 5. 🔄 Fluxo Principal (Cadastrar e Gerenciar Produtos)

| Passo | Ator | Ação do Sistema |
| :---: | :--- | :--- |
| **1** | Administrador | Acessa `/MRYnZpAsC9sp/produtos`. |
| **2** | Sistema | [`AdminProdutoController`](file:///var/www/html/tgos-barbearia/ProjetoIntegrador.GWJ.JAVA.Spring.Boot.dinamico/src/main/java/com/gwj/controller/AdminProdutoController.java) invoca `ProdutoService.read(new Produto())` e envia a lista para a view `admin/catalog/product/listar`. |
| **3** | Administrador | Clica em "Novo Produto" (`/novo`). |
| **4** | Sistema | Renderiza a tela de formulário `admin/catalog/product/form`. |
| **5** | Administrador | Informa o nome do produto (ou kit), descrição, preço unitário, quantidade em estoque e URL da imagem ilustrativa. |
| **6** | Administrador | Clica em "Salvar". |
| **7** | Sistema | Persiste a entidade via `ProdutoService.create(produto)` (ou `update`) e redireciona para a listagem. |
| **8** | Administrador | Visualiza o item cadastrado com seu respectivo saldo em estoque. |

---

## 6. 🔀 Fluxos Alternativos

### **FA01 - Atualização Manual de Saldo de Estoque (`/editar/{id}`)**
1. O gestor acessa o produto na listagem e altera o valor do campo "Estoque" após conferência física de prateleira ou entrada de nota fiscal.
2. Salva o registro, atualizando a quantidade máxima que pode ser adicionada aos carrinhos virtuais.

---

## 7. ⚠️ Fluxos de Exceção

### **FE01 - Saldo Negativo de Estoque**
* O sistema valida que a quantidade em estoque não pode ser preenchida com valores inferiores a zero.

---

## 8. 📜 Regras de Negócio Aplicadas

| Código | Nome da Regra | Descrição no Contexto do Caso de Uso |
| :--- | :--- | :--- |
| **RN-EST-01** | Trava de Estoque Disponível | O saldo aqui mantido impede que clientes comprem além da capacidade física do estoque. |
| **RN-EST-02** | Estrutura de Kits Promocionais | Kits com múltiplos cosméticos são cadastrados como produtos com saldo e precificação agregada própria. |

---

## 9. 🖥️ Interface & Campos de Entrada/Saída

### Entradas:
* `nome` *(String, Obrigatório)*: Nome comercial do produto ou kit.
* `descricao` *(String, Opcional)*: Detalhes, modo de uso e benefícios.
* `preco` *(Double/BigDecimal, Obrigatório)*: Preço de venda.
* `estoque` *(Integer, Obrigatório)*: Quantidade física disponível.
* `imagemUrl` *(String, Opcional)*: Link para foto de apresentação do produto.

### Saídas:
* Listagem de produtos com badges de estoque e preço.
