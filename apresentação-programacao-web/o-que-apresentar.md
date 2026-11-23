# Roteiro de Apresentação, Disciplina: Programação para Web

**Projeto:** Tgo's Barbearia (`GWJ Java Spring Boot Dinâmico`)  
**Duração Total:** **15 minutos** (**12 min 30 s** de apresentação ao vivo + **2 min 30 s** para perguntas)  
**Integrantes:** Gabriel Calidonio André, Josias da Conceição Sobrinho e Wallace Francis Miranda  
**Formato:** **Demonstração ao vivo no navegador** + **DevTools (`F12`, aba Network, Console e Modo Responsivo)**

> [!TIP]
> **Diferencial deste roteiro para Programação para Web:**  
> Diferente do Projeto Integrador (focado em regra de negócio e banco de dados), aqui o brilho está em demonstrar **o que acontece na camada Web**: ciclo requisição/resposta **HTTP**, **SSR + Fragments** com Thymeleaf, requisições assíncronas (**`fetch()` / JSON**), **manipulação de DOM** (QR Code PIX em SVG + máscaras + ViaCEP), **sessão HTTP (`HttpSession`)**, **Interceptadores**, **tratamento de erros HTTP (403/404/500)** e **responsividade**.

---

## Divisão Equilibrada por Blocos (15 min)

| Bloco | Tempo | Tema Web em Foco | Responsável |
| :---: | :---: | :--- | :--- |
| **1** | 00:00 – 01:45 (1m45s) | Visão Geral Web, Arquitetura Híbrida (SSR + JSON) e Componentização | **Gabriel** |
| **2** | 01:45 – 04:45 (3m00s) | Front-end Assíncrono ao Vivo: Agendamento + `DevTools Network` + Checkout PIX (SVG) | **Wallace** |
| **3** | 04:45 – 07:15 (2m30s) | E-commerce com Sessão HTTP (`HttpSession`), Mini-Cart Fragment e JS Modular | **Josias** |
| **4** | 07:15 – 10:15 (3m00s) | Segurança Web, Interceptadores, Honeypot (`403`), Erros (`404`) e CRUD Admin | **Gabriel & Wallace** |
| **5** | 10:15 – 12:30 (2m15s) | Setup Wizard Web (`/setup`), Responsividade Mobile, Nginx/HTTPS e Testes Web | **Josias & Gabriel** |
| **6** | 12:30 – 15:00 (2m30s) | Perguntas e Respostas da Banca / Professor | **Todos** |

---

## Preparação Antes de Abrir o Compartilhamento de Tela

1. **Navegador principal (Aba 1):** Aplicação aberta em `/` (ou URL publicada via Nginx + HTTPS).
2. **DevTools (`F12`) pré-posicionado:** Aba **Network** aberta (com filtro **Fetch/XHR** fácil de alternar) e ícone **Toggle Device Toolbar** (celular) pronto para testar responsividade.
3. **Aba 2 (Anônima ou secundária):** Pronta para testar `/admin` (Honeypot `403`), `/pagina-inexistente` (`404`) e login do painel administrativo ofuscado (`/${ADMIN_ROUTE_PREFIX}/login`).
4. **Dados prontos:** Ter ao menos 2 serviços, 2 barbeiros e 2 produtos cadastrados para não perder tempo digitando tudo do zero.

---

## Roteiro Detalhado Minuto a Minuto

### Bloco 1: Visão Geral Web, SSR e Componentização (00:00 – 01:45) | **Gabriel**
* **O que mostrar na tela:** Página inicial (`/home`) e transição rápida para `/servicos` e `/loja`.
* **O que falar e demonstrar:**
  1. Apresentar o grupo (**Gabriel, Josias e Wallace**) e o sistema **Tgo's Barbearia** (plataforma web unificada de agendamentos, loja virtual e painel administrativo).
  2. Explicar a **arquitetura Web híbrida (Spring MVC + Thymeleaf + JavaScript ES6+)**:
     * As páginas públicas nascem renderizadas no servidor (**SSR**) pelo `Router.java` usando **Thymeleaf Layout Dialect** (*Padrão Decorator* com `site/layouts/main-layout.html` e fragmentos reutilizáveis em `site/fragments/header.html`, `footer.html` e `product-card.html`).
     * Isso garante **carregamento inicial rápido e bom SEO**, enquanto as ações interativas da tela conversam de forma assíncrona em **JSON** via `fetch()`, sem recarregar a página inteira.

---

### Bloco 2: Front-end Assíncrono + `DevTools Network` + PIX SVG (01:45 – 04:45) | **Wallace**
* **O que mostrar na tela:** Fluxo `/servicos` → seleção de barbeiro/data → `/checkout` → `/order-confirmation` **com o DevTools (`F12` → Network) visível na lateral ou parte inferior**.
* **Passo a passo ao vivo:**
  1. **Consumo de API JSON via `fetch()` (Network aberto):**
     * Selecionar um serviço e escolher um barbeiro e uma data.
     * Apontar diretamente para a requisição na aba **Network**:
       ```text
       GET /api/agenda/horarios-disponiveis?servicoId=...&profissionalId=...&data=...
       ```
     * Clicar na aba **Response / Preview** do DevTools para mostrar o **JSON** retornado em tempo real (`200 OK`) e explicar como o JavaScript lê esse JSON e bloqueia visualmente os horários já ocupados.
  2. **Validação e Máscaras no Cliente (`masks.js` / `validation.js`):**
     * Avançar para `/checkout`, mostrar o preenchimento com **máscara automática** (telefone/CPF/cartão) e os atributos HTML/JS de validação antes mesmo de enviar o `POST /checkout/confirmar`.
  3. **Pagamento PIX Gerado 100% no Navegador (`pix-payment.js`):**
     * Selecionar **PIX via QR Code**: destacar que o JavaScript calcula o payload padrão **EMVCo (BRCode)** com **checksum CRC16-CCITT**, desenha o **QR Code em `<svg>` direto no DOM** (sem depender de API externa) e usa a **Clipboard API** no botão **"Copiar"** (mostrando o feedback `"✓ Código PIX copiado!"`).
     * Clicar em **"Já realizei o pagamento (Simular)"**, depois confirmar a reserva e exibir o comprovante em `/order-confirmation` (com o QR Code PIX mantido na confirmação).

---

### Bloco 3: Sessão HTTP (`HttpSession`), Mini-Cart e JS Modular (04:45 – 07:15) | **Josias**
* **O que mostrar na tela:** `/loja` → `/carrinho` → `/carrinho/checkout` → `/compra-confirmada` (ainda com o **Network** aberto para mostrar os `POST` assíncronos).
* **Passo a passo ao vivo:**
  1. **Persistência de Estado na Sessão HTTP:**
     * Na `/loja`, clicar para adicionar 1 ou 2 produtos.
     * Mostrar no **Network** a chamada assíncrona `POST /carrinho/adicionar` (`CarrinhoController`) e a atualização dinâmica do fragmento do mini-carrinho (`/carrinho/mini-cart-fragment`), explicando como o estado do carrinho fica associado à **`HttpSession` (Cookie `JSESSIONID`)** sem exigir login prévio.
  2. **Manipulação de Quantidade e API Externa de CEP (`automatic-address.js`):**
     * Abrir `/carrinho`, alterar a quantidade de um item (mostrando `POST /carrinho/atualizar` recalculando o subtotal em JSON).
     * No `/carrinho/checkout`, mostrar as opções **PIX**, **Cartão de Crédito** (com máscaras dinâmicas ativando/desativando campos `required` via JS) e **Pagar na Barbearia**, e comentar o uso de `automatic-address.js` para consulta assíncrona de CEP (ViaCEP) quando há formulário de endereço.
     * Finalizar mostrando `/compra-confirmada`.

---

### Bloco 4: Segurança Web, Interceptadores, Erros HTTP e CRUD Admin (07:15 – 10:15) | **Gabriel & Wallace**
* **O que mostrar na tela:** Tentativa em `/admin`, URL inválida (`/nao-existe`), login em `/${ADMIN_ROUTE_PREFIX}/login` e um CRUD completo no painel.
* **Passo a passo ao vivo:**
  1. **Segurança na Camada de Roteamento e Status HTTP (Gabriel):**
     * Digitar `/admin` na URL com o **Network** aberto: mostrar o retorno **`403 Forbidden`** imediato do `HoneypotController` (armadilha contra bots que varrem `/admin`).
     * Digitar uma URL aleatória (ex.: `/rota-inexistente`) para mostrar a página amigável `site/errors/error.html` renderizada pelo `GlobalExceptionHandler` (`@ControllerAdvice`), provando que erros `404`/`500` nunca vazam *stacktrace* no navegador.
     * Explicar que a rota real do painel é **ofuscada** via variável de ambiente (`ADMIN_ROUTE_PREFIX`) e protegida pelo **`AdminInterceptor`**, que valida sessão e perfil (RBAC) antes de liberar qualquer controller administrativo.
  2. **CRUD Completo pela Interface + API JSON (Wallace):**
     * Acessar `/${ADMIN_ROUTE_PREFIX}/login`, autenticar como Administrador e abrir um módulo (ex.: **Produtos** ou **Serviços**).
     * Demonstrar rapidamente **Create** (mostrando feedback de validação ao tentar salvar inválido e depois salvando correto), **Read** (aparecendo na listagem), **Update** (editando nome/preço) e **Delete** (removendo o item de teste).
     * Mencionar os scripts `create.js`, `update.js` e `delete.js` que comunicam com `JsonController` (`/create-json`, `/read-json`, `/update-json`, `DELETE /delete-json`) usando os verbos HTTP corretos.

---

### Bloco 5: Setup Wizard Web, Responsividade, Proxy Reverso e Testes (10:15 – 12:30) | **Josias & Gabriel**
* **O que mostrar na tela:** Alternar DevTools para **Mobile (Responsividade)** e mostrar rapidamente a aba **Console / Terminal** (ou diagrama de rede).
* **Passo a passo ao vivo:**
  1. **Responsividade (Josias):**
     * Ativar o **Modo Responsivo (`Ctrl+Shift+M`)** no DevTools na página inicial e no `/checkout`, mostrando que o layout CSS3 (`style.css`) e os cards/formulários se adaptam para celular sem quebra horizontal.
  2. **Setup Wizard Web (`/setup`) (Josias):**
     * Explicar (sem resetar a produção ao vivo) que o projeto possui um instalador web em `/setup` (`SetupController` + `SetupInterceptor`), onde o próprio navegador envia `POST /setup/test-db` e `POST /setup/finish` em JSON para testar o banco, definir a rota admin ofuscada e gravar o `.env`.
  3. **Publicação Web e Qualidade (Gabriel):**
     * Citar brevemente a arquitetura web em produção: **Nginx** como **Proxy Reverso** recebendo HTTPS (`443`) e repassando cabeçalhos (`Host`, `X-Real-IP`, `X-Forwarded-Proto`) para a aplicação Spring Boot na porta interna `8089`.
     * Encerrar citando os testes web automatizados do projeto: testes de rotas e controllers com **`MockMvc`** + testes visuais End-to-End em navegador real com **Playwright**, agradecer ao professor/banca e abrir para perguntas.

---

## Respostas Rápidas para Possíveis Perguntas de Programação para Web

1. **"Por que usar Thymeleaf (SSR) com JavaScript (`fetch`) em vez de uma SPA React/Angular separada?"**
   * *Resposta:* O SSR entrega o HTML já montado pelo servidor (melhor para SEO da vitrine da barbearia e tempo de primeira pintura), enquanto o JavaScript ES6+ (`fetch`, manipulação de DOM e SVG) resolve todas as partes dinâmicas (slots de agenda, mini-carrinho, máscaras, CEP e QR Code PIX) sem precisar manter dois servidores e builds separados.

2. **"Como vocês garantem que requisições não autorizadas não acessem o painel admin?"**
   * *Resposta:* Em três níveis na camada web: (1) ofuscação da URL base via `ADMIN_ROUTE_PREFIX`; (2) *Honeypot* em `/admin/**` retornando `403 Forbidden`; e (3) **`AdminInterceptor`**, que intercepta toda requisição antes do controller para checar usuário autenticado na `HttpSession` e permissão RBAC.

3. **"Como funciona o carrinho antes do cliente fazer login?"**
   * *Resposta:* O `CarrinhoController` armazena o objeto `Carrinho` na `HttpSession` do servlet (vinculada ao cookie `JSESSIONID` do navegador) e atualiza o contador/mini-cart de forma assíncrona via endpoints JSON e fragmentos Thymeleaf (`/carrinho/mini-cart-fragment`).

4. **"Por que o QR Code do PIX foi gerado no JavaScript em vez de chamar uma API externa?"**
   * *Resposta:* Para demonstrar manipulação avançada no front-end (cálculo de payload EMVCo + CRC16-CCITT, geração vetorial `<svg>` via DOM e Clipboard API) e garantir que a aplicação funcione de forma rápida, determinística e sem falhas de rede durante demonstrações ou ambientes offline.

5. **"Como vocês tratam erros HTTP e formulários inválidos na interface?"**
   * *Resposta:* No cliente, `validation.js` e `masks.js` validam e formatam antes do envio (`required` dinâmico no checkout). No servidor, o `GlobalExceptionHandler` (`@ControllerAdvice`) captura erros `404` (`NoHandlerFoundException` / `NoResourceFoundException`) e `500` (`Exception`), renderizando `site/errors/error.html` com mensagem amigável sem expor detalhes internos.

---

## Checklist Rápido 10 Minutos Antes de Começar

- [ ] Aplicação no ar e página `/home` abrindo rápido.
- [ ] DevTools (`F12`) testado na aba **Network** (`Fetch/XHR`) e **Modo Mobile** (`Ctrl+Shift+M`).
- [ ] Rota `/admin` conferida devolvendo `403` e `/teste-404` exibindo a página de erro amigável.
- [ ] Credenciais de um **Administrador** salvas para digitar rápido no login do painel.
- [ ] Cronômetro combinado entre **Gabriel, Wallace e Josias** para não passar de **12 min 30 s** na demo.
