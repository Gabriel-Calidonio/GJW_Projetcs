# Plano de Implementação: Especificação dos Casos de Uso do Storefront (Frente de Loja)

Este plano estabelece a estrutura e especificação formal de todos os Casos de Uso da Frente de Loja / Vitrine Pública em [`docs/business/use-cases/storefront/`](/docs/business/use-cases/storefront/), mantendo rastreabilidade estrita com o diagrama [`storefront.puml`](/docs/business/use-cases/storefront.puml), os controladores Spring Boot e as regras de negócio do sistema GWJ.

---

## Diagnóstico do Diagrama [`storefront.puml`](/docs/business/use-cases/storefront.puml)

O diagrama de casos de uso da vitrine pública abrange **5 pacotes funcionais** e **22 casos de uso**:
1. **Vitrine de Cosméticos & Kits Promocionais**:
   - `UC01`: Navegar no Catálogo de Produtos e Kits (`/loja`)
   - `UC02`: Visualizar Detalhes do Produto (`/single-product`)
   - `UC03`: Adicionar Produto ao Carrinho (`POST /carrinho/adicionar`)
   - `UC04`: Validar Saldo em Estoque (`RN-EST-01`)
2. **Carrinho de Compras (`HttpSession`)**:
   - `UC05`: Visualizar Carrinho de Compras (`/carrinho`)
   - `UC06`: Alterar Quantidade do Item (`POST /carrinho/atualizar`)
   - `UC07`: Remover Item do Carrinho (`POST /carrinho/remover`)
   - `UC08`: Esvaziar Carrinho (`POST /carrinho/limpar`)
3. **Checkout & Pedido de Produtos**:
   - `UC09`: Realizar Checkout de Produtos (`/carrinho/checkout`)
   - `UC10`: Identificar Comprador (Cliente Logado vs Visitante - `RN-EST-03`)
   - `UC11`: Selecionar Forma de Pagamento (PIX, Dinheiro na Retirada, Cartão)
   - `UC12`: Concluir Pedido com Baixa Atômica (`/carrinho/checkout/confirmar`)
   - `UC13`: Visualizar Confirmação de Compra (`/compra-confirmada`)
   - `UC14`: Esvaziar Carrinho Pós-Venda (`RN-EST-04`)
4. **Agendamento Online de Serviços**:
   - `UC15`: Consultar Catálogo de Serviços (`/servicos`)
   - `UC16`: Consultar Disponibilidade de Horários (`/api/agendamentos/disponibilidade`)
   - `UC17`: Realizar Checkout de Agendamento (`/checkout`)
   - `UC18`: Visualizar Comprovante do Agendamento (`/order-confirmation`)
5. **Área e Conta do Cliente**:
   - `UC19`: Cadastrar Nova Conta de Cliente (`/cadastro`)
   - `UC20`: Autenticar no Sistema (Login do Cliente) (`/login`)
   - `UC21`: Consultar Meus Agendamentos e Pedidos (`/meus-agendamentos`)
   - `UC22`: Encerrar Sessão (Logout do Cliente) (`/logout`)

---

## User Review Required

> [!IMPORTANT]
> **Estratégia de Organização**:
> Propõe-se estruturar as especificações em **7 documentos ricos e modulares** agrupando as extensões (`<<extend>>`) e inclusões (`<<include>>`), no mesmo padrão do dashboard:
> 
> 1. `UC_STR_001_catalogo_produtos_e_detalhes.md` (UC01 + UC02)
> 2. `UC_STR_002_adicionar_e_gerenciar_carrinho.md` (UC03 + UC04 + UC05 + UC06 + UC07 + UC08)
> 3. `UC_STR_003_checkout_e_pedido_produtos.md` (UC09 + UC10 + UC11 + UC12 + UC13 + UC14)
> 4. `UC_STR_004_catalogo_servicos_e_disponibilidade.md` (UC15 + UC16)
> 5. `UC_STR_005_realizar_agendamento_e_comprovante.md` (UC17 + UC18)
> 6. `UC_STR_006_cadastro_e_autenticacao_cliente.md` (UC19 + UC20 + UC22)
> 7. `UC_STR_007_historico_meus_agendamentos.md` (UC21)

---

## Proposed Changes

### 1. Documentação dos Casos de Uso do E-Commerce e Carrinho

#### [NEW] [UC_STR_001_catalogo_produtos_e_detalhes.md](/docs/business/use-cases/storefront/UC_STR_001_catalogo_produtos_e_detalhes.md)
- Vitrine `/loja`, cards de cosméticos e kits promocionais (`RN-EST-02`).
- Detalhes do item em `/single-product?id={id}`, foto, descrição e preço.

#### [NEW] [UC_STR_002_adicionar_e_gerenciar_carrinho.md](/docs/business/use-cases/storefront/UC_STR_002_adicionar_e_gerenciar_carrinho.md)
- Adicionar ao carrinho via AJAX (`POST /carrinho/adicionar`).
- Validação imediata de saldo em estoque (`RN-EST-01`).
- Manipulação da sessão HTTP (`Carrinho` em `HttpSession`).
- Operações de atualizar quantidade, remover item e esvaziar carrinho.

#### [NEW] [UC_STR_003_checkout_e_pedido_produtos.md](/docs/business/use-cases/storefront/UC_STR_003_checkout_e_pedido_produtos.md)
- Tela de checkout `/carrinho/checkout`.
- Identificação do comprador: cliente autenticado com `cliente_id` vs visitante com `nome_visitante` e `telefone_visitante` (`RN-EST-03`).
- Submissão atômica em transação JDBC (`UnitOfWork`): inserção de `Pedido`, `ItemPedido` e decremento de `Produto.estoque`.
- Limpeza automática do carrinho pós-venda (`RN-EST-04`).
- Tela de sucesso `/compra-confirmada?id={id}`.

---

### 2. Documentação dos Casos de Uso de Agendamento Online

#### [NEW] [UC_STR_004_catalogo_servicos_e_disponibilidade.md](/docs/business/use-cases/storefront/UC_STR_004_catalogo_servicos_e_disponibilidade.md)
- Navegação em `/servicos` com listagem de procedimentos e barbeiros.
- Consulta de disponibilidade assíncrona via API (`/api/agendamentos/disponibilidade`).
- Validações de múltiplos blocos de 20 minutos (`RN-AGE-01`), limite de expediente (`RN-AGE-02`), bloqueio do passado (`RN-AGE-03`) e atribuição automática ("Qualquer Profissional" - `RN-AGE-04`).

#### [NEW] [UC_STR_005_realizar_agendamento_e_comprovante.md](/docs/business/use-cases/storefront/UC_STR_005_realizar_agendamento_e_comprovante.md)
- Tela `/checkout` do agendamento com validação de horário retroativo.
- Confirmação da reserva com trava atômica contra double-booking (`RN-AGE-05`).
- Geração do agendamento e exibição do comprovante formal em `/order-confirmation?id={id}`.

---

### 3. Documentação da Área e Conta do Cliente

#### [NEW] [UC_STR_006_cadastro_e_autenticacao_cliente.md](/docs/business/use-cases/storefront/UC_STR_006_cadastro_e_autenticacao_cliente.md)
- Formulário `/cadastro` para novos clientes com criptografia de senha (`RN-SEG-01`).
- Login público `/login` com redirecionamento de clientes para a Home (`/`).
- Logout público `/logout` com invalidação de sessão (`RN-SEG-04`).

#### [NEW] [UC_STR_007_historico_meus_agendamentos.md](/docs/business/use-cases/storefront/UC_STR_007_historico_meus_agendamentos.md)
- Página `/meus-agendamentos` exclusiva para clientes autenticados.
- Consulta cronológica dos atendimentos filtrados pelo telefone do cliente.

---

### 4. Atualização do README Central de Casos de Uso

#### [MODIFY] [docs/business/use-cases/README.md](/docs/business/use-cases/README.md)
- Adicionar tabela com a relação e links diretos para as especificações da pasta `storefront/`.

---

## Verification Plan

### Validação Estrutural e Rastreabilidade
- Checar se todos os links para templates Thymeleaf em `site/` e controllers (`Router`, `CarrinhoController`, `AgendamentoController`, `LoginController`) estão corretos.
- Verificar consistência com o diagrama `storefront.puml`.
- Validar conformidade das regras de negócio (`RN-EST-01..04`, `RN-AGE-01..05`, `RN-SEG-01..04`).
