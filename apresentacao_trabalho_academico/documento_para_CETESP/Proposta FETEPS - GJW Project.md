**PROPOSTA ESCRITA \- FETEPS**

**GJW Projects**

**Nome do produto**

Tgo’s Barbearia \- Sistema Integrado de Agendamento Autônomo, E-Commerce e Gestão Empresarial Dinâmica.

**Descrição detalhada do produto**

O produto é uma plataforma web completa desenvolvida sob medida para a Tgo’s Barbearia com o objetivo de digitalizar, automatizar e integrar os pilares operacionais do negócio: atendimento ao cliente, agendamento de serviços, venda de produtos e gestão administrativa. Pelo ambiente do cliente, o usuário pode consultar serviços, preços e profissionais, visualizar a grade de horários disponíveis em tempo real, realizar agendamentos de forma autônoma, remarcar ou cancelar reservas e receber confirmações instantâneas. No backoffice administrativo, o sistema contempla catálogo de produtos cosméticos masculinos com controle de estoque integrado, gerenciamento de comandas para atendimentos presenciais, fluxo financeiro, notificações e painel com controle de acesso baseado em perfis (RBAC). O motor de agendamento trabalha com slots modulares de 20 minutos e valida a disponibilidade de janelas contínuas para serviços de 40 ou 60 minutos, garantindo transações atômicas e prevenindo matematicamente qualquer conflito de horários (*double-booking*). A solução foi desenvolvida com Java 21 LTS, Spring Boot 3.2.5, Thymeleaf, JavaScript, CSS Vanilla e MySQL, assegurando alta performance, código modular desacoplado e sólida segurança.

**Justificativa empreendedora do produto (diferencial)**

O projeto surgiu da identificação de gargalos reais enfrentados por barbearias que operam por anotações manuais e atendimento via mensagens de texto: respostas tardias, perda de novos clientes por espera, no-shows (faltas), colisões acidentais de horários e falta de controle sobre estoque e receitas. A plataforma GJW resolve essas dores centralizando a operação em um ambiente digital intuitivo e autônomo. 

Frente às alternativas existentes no mercado, o produto se destaca por três diferenciais estratégicos decisivos:
1. **Identidade Própria e Fidelização (White-Label):** Ao contrário de aplicativos de mercado que inserem a barbearia em marketplaces genéricos (onde o cliente é exposto a concorrentes diretos), a plataforma preserva e fortalece exclusivamente a marca da Tgo’s Barbearia.
2. **Previsibilidade Financeira e Custo Zero por Agendamento:** Não há cobrança de taxas percentuais sobre os serviços realizados nem mensalidades abusivas por funcionário adicional, viabilizando a transformação digital para microempreendedores (MEIs).
3. **Ecossistema Unificado All-in-One:** Integração nativa entre agenda, frente de atendimento (comandas), e-commerce e controle de inventário em uma única base de dados relacional, eliminando planilhas paralelas.
Além disso, a solução possui como diferencial de inovação futura a integração de um simulador de corte e visagismo baseado em Inteligência Artificial.

&nbsp;

**Apresentação da produção**

A solução foi concebida e desenvolvida pela equipe GJW Projects, composta pelos desenvolvedores Gabriel Calidonio André, Josias da Conceição Sobrinho e Wallace Francis Miranda. O projeto foi executado sob metodologia ágil Scrum ao longo de cinco Sprints bem definidas, englobando o levantamento de requisitos com o cliente real, modelagem de dados EER, arquitetura de software, prototipagem UX/UI, engenharia do back-end, blindagem de segurança e validação por testes. A solidez da produção técnica é evidenciada pela arquitetura em camadas orientada a padrões GoF e PoEAA (*DataMapper*, *Unit of Work* com controle transacional ACID e *QueryBuilder* dinâmico), controle de acessos RBAC, proteção contra injeções de código (PreparedStatement), mecanismo de defesa ativa (*Honeypot*) e validação por suíte de testes unitários automatizados com JUnit 5, que atesta 100% de consistência nos cenários de concorrência e reserva de horários.

**Estratégia de precificação**

A estratégia comercial foi concebida para atender à realidade de pequenos negócios, aliando viabilidade de implantação a um custo operacional extremamente reduzido. O modelo pode operar via taxa única de implantação e customização (cobrada pelas horas técnicas de setup e treinamento) associada a um plano de manutenção e suporte preventivo, ou através de modelo SaaS com mensalidade acessível. O estudo de viabilidade técnica e infraestrutura comprovou que a aplicação pode ser hospedada em servidores VPS com excelente estabilidade a partir de R$ 35,00 a R$ 50,00 mensais, conferindo custos fixos totalmente previsíveis e proporcionando rápido retorno sobre o investimento (ROI) para a barbearia.

**Estratégias de apresentação do produto para venda na FETEPS**

O estande na FETEPS será estruturado em torno de uma demonstração dinâmica, interativa e simultânea em dois ambientes:
- **Ponto de Vista do Cliente (Totem/Tablet ou Smartphone):** O visitante da feira é convidado a navegar na Landing Page, escolher um serviço, selecionar barbeiro e horário em tempo real e concluir sua reserva pelo fluxo de checkout simplificado.
- **Ponto de Vista do Gestor (Notebook do Estande):** O visitante visualiza imediatamente a atualização instantânea da comanda e da agenda no painel administrativo, comprovando o funcionamento do motor de prevenção de colisões de horário e a baixa latência do sistema.
Materiais visuais de apoio (banners explicativos, pranchas gráficas com a arquitetura do sistema e cartões com QR Code para acesso direto ao protótipo) permitirão que avaliadores e investidores compreendam rapidamente a dor, a solução e a viabilidade do negócio. Para reforçar a atratividade tecnológica, será apresentada a proposta conceitual da simulação de cortes por IA.

&nbsp;

**Capacidade de produção**

Por se tratar de um ativo de software modular e baseado em padrões arquiteturais dinâmicos, o sistema possui alta capacidade de replicação e escalabilidade. Uma nova instância do sistema pode ser implantada, parametrizada e colocada em produção em poucas horas, sem necessidade de retrabalho na base estrutural do código. A separação clara entre a camada de persistência (*DataMapper*), serviços genéricos e regras de negócio permite que a solução se adapte não apenas a novas filiais da Tgo’s Barbearia, mas também seja expandida para outros estabelecimentos do setor de estética e bem-estar. A equipe dispõe de documentação técnica completa (diagramas UML, modelo EER, matriz RBAC e catálogo de rotas), o que assegura manutenibilidade contínua e facilidade de evolução.

**Fotos do protótipo do produto**

O projeto dispõe de prototipação completa e interfaces operacionais para todos os fluxos críticos de uso: Landing Page responsiva, catálogo de serviços e profissionais, seletor de horários em tempo real, fluxo de identificação e checkout, tela de confirmação de agendamento, catálogo de produtos para venda e o painel administrativo integrado com login seguro. O protótipo navegável de validação funcional e de experiência do usuário (UX/UI) pode ser acessado em: https://tgos-barbearia.lovestoblog.com/templates/home.php, servindo como referência de validação de mercado para a arquitetura definitiva consolidada no back-end de alta performance em Java 21 / Spring Boot 3.2.5. Na FETEPS, essas interfaces serão demonstradas em dispositivos móveis, computadores e apresentadas em materiais visuais de alta definição.

**Sustentabilidade**

O projeto está formalmente alinhado aos **Objetivos de Desenvolvimento Sustentável (ODS) da Organização das Nações Unidas (ONU)**, gerando impactos positivos em três dimensões principais:
1. **ODS 12 – Consumo e Produção Responsáveis:** Promove a transição para uma operação *Paperless* (zero papel), substituindo fichas físicas de clientes, cadernos de agendamento, recibos e comandas manuais por registros em banco de dados relacional e notificações digitais, gerando redução no desperdício de insumos materiais.
2. **ODS 8 – Trabalho Decente e Crescimento Econômico:** Impulsiona a formalização, a previsibilidade de receita e a produtividade de profissionais autônomos e pequenos empreendedores (MEIs), reduzindo o tempo improdutivo gasto em triagem de mensagens e permitindo maior foco na execução dos serviços e na geração de renda.
3. **ODS 9 – Indústria, Inovação e Infraestrutura:** Democratiza o acesso de microempresas de bairro a tecnologias corporativas robustas de ponta (Java 21, Spring Boot, arquitetura de microsserviços e computação em nuvem com baixo consumo de recursos de servidor), promovendo a inclusão digital do setor de serviços locais.

**Recursos financeiros**

O desenvolvimento inicial foi executado com investimento financeiro direto zero durante a fase acadêmica, valendo-se da competência técnica dos membros da GJW Projects e de ferramentas modernas *open-source* (Java, Spring Framework, MySQL, Git e JUnit). Para a etapa de lançamento comercial em ambiente de produção contínua, os custos financeiros necessários resumem-se à contratação de domínio anual (cerca de R$ 40,00/ano) e infraestrutura de hospedagem VPS (na faixa de R$ 35,00 a R$ 50,00 mensais), totalizando um investimento inicial inferior a R$ 100,00 para início imediato das operações. Esse baixíssimo custo de entrada comprova a viabilidade financeira irrestrita da solução para pequenos negócios, gerando sustentabilidade econômica e fluxo de caixa positivo logo no primeiro mês de adoção.