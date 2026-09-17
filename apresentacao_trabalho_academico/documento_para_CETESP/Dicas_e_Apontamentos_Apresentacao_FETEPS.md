# **Guia Estratégico de Apresentação – FETEPS**
### **Projeto: Tgo’s Barbearia – Sistema Integrado de Agendamento, E-Commerce e Gestão**
**Equipe GJW Projects:** Gabriel Calidonio André, Josias da Conceição Sobrinho, Wallace Francis Miranda

---

## **1. Visão Geral e Critérios dos Avaliadores**

A FETEPS (Feira Tecnológica do Centro Paula Souza) não avalia apenas código ou funcionalidade; os jurados analisam **quatro eixos essenciais**:
1. **Inovação e Relevância Tecnológica:** A qualidade da solução frente aos concorrentes existentes.
2. **Impacto Socioeconômico e ODS:** Alinhamento com metas globais da ONU (trabalho decente, consumo sustentável e democratização de tecnologia).
3. **Viabilidade Econômica e Empreendedora:** Capacidade real de gerar receita, custo operacional baixo e retorno ao cliente (Tgo's Barbearia).
4. **Comunicação e Domínio da Equipe:** Clareza, postura profissional, equilíbrio na participação de todos os membros e precisão nas respostas.

---

## **2. Roteiro do Pitch de 3 Minutos (Para Vídeo e Estande)**

Este roteiro cumpre com precisão a estrutura cronológica recomendada pelas diretrizes da FETEPS:

| Tempo | Etapa | O que falar / Conteúdo Chave |
| :--- | :--- | :--- |
| **00:00 - 00:30** | **Abertura, Equipe e ODS** | *"Olá! Nós somos a equipe GJW Projects, composta por Gabriel, Josias e Wallace. Nosso projeto é o sistema da Tgo’s Barbearia, que nasceu com o objetivo de digitalizar e profissionalizar o atendimento de barbearias e microempreendedores. Nosso projeto contribui diretamente com os ODS da ONU: ODS 8, fortalecendo a renda e gestão do MEI; ODS 9, levando tecnologia robusta ao comércio local; e ODS 12, com operação 100% sem papel."* |
| **00:30 - 01:10** | **O Problema Real** | *"Hoje, a maioria dos barbeiros autônomos gerencia agendamentos por WhatsApp e anotações em papel. O resultado? Respostas demoradas, perda de até 30% de clientes que desistem de esperar, colisões de horários e falta de controle sobre estoque de cosméticos e caixa no fim do mês."* |
| **01:10 - 02:00** | **A Solução e os Diferenciais** | *"Para resolver isso, desenvolvemos uma plataforma completa. Diferente de aplicativos como Trinks ou AppBarber que cobram taxas abusivas por agendamento e colocam concorrentes na mesma tela, nosso sistema é White-Label: valoriza a marca exclusiva da Tgo’s Barbearia e opera com custo fixo mínimo. O cliente agenda com autonomia em segundos, e o barbeiro conta com um motor de slots inteligentes que impede matematicamente o agendamento duplo (anti double-booking) e integra agenda, comandas presenciais e e-commerce de produtos."* |
| **02:00 - 02:35** | **Arquitetura, Testes e Viabilidade** | *"A plataforma foi desenvolvida com Java 21 LTS e Spring Boot 3.2.5, banco MySQL e arquitetura orientada a padrões de projeto corporativos (DataMapper e Unit of Work transacional). Comprovamos 100% de confiabilidade nos testes automatizados com JUnit 5. A viabilidade é comprovada: a infraestrutura roda em nuvem com custo a partir de R$ 35,00 mensais, tornando-se acessível para qualquer pequeno negócio."* |
| **02:35 - 03:00** | **Fechamento e Futuro** | *"A Tgo’s Barbearia já possui protótipo navegável validado, e o próximo passo é a inclusão de simulação virtual de cortes de cabelo com Inteligência Artificial. Com a GJW Projects, transformamos o pequeno comércio em uma empresa moderna e altamente rentável. Muito obrigado!"* |

---

## **3. Dinâmica do Estande: "Show, Don't Tell"**

Na feira física, apresentações puramente verbais tornam-se cansativas para os jurados. O segredo é fazê-los **interagir** com a solução:

### A Dinâmica dos 2 Dispositivos (Efeito "Uau")
* **Dispositivo 1 (Tablet ou Smartphone do Visitante):** Abra a tela pública de agendamento (`templates/home.php` ou tela do cliente) e entregue na mão do jurado:
  > *"Professor/Avaliador, por favor, escolha o barbeiro e agende um corte às 15h00."*
* **Dispositivo 2 (Notebook do Estande virado para o público):** Mantenha o Painel Administrativo do Barbeiro aberto.
* **O Momento da Mágica:** No instante em que o avaliador clica em confirmar no celular, mostre o notebook atualizando instantaneamente a grade, bloqueando os slots de 20 minutos e gerando a comanda:
  > *"Observe que o sistema bloqueou automaticamente a janela contínua e impediu qualquer sobreposição de horário. A comanda já está pronta para receber serviços adicionais ou produtos cosméticos."*

### Divisão de Papéis da Equipe
A banca desconta pontos se apenas um aluno falar. Dividam as falas de forma equilibrada:
* **Membro 1 (Negócios & Dor do Cliente):** Apresenta o problema real da Tgo's Barbearia, a justificativa empreendedora, os ODS e o comparativo com concorrentes.
* **Membro 2 (Demonstração Prática & UX):** Conduz a navegação das telas, mostra a agilidade do agendamento, o catálogo de produtos e as comandas.
* **Membro 3 (Engenharia Técnica, Arquitetura & Segurança):** Explica o back-end em Java 21 / Spring Boot, o controle de transações (Unit of Work), o algoritmo de slots e os testes unitários no JUnit 5.

---

## **4. FAQ da Banca: Como Responder às Perguntas Difíceis**

### Pergunta 1: *"Já existem vários apps de barbearia prontos no mercado. Por que o dono usaria o de vocês?"*
* **Como responder:**
  > *"Excelente pergunta. Os aplicativos de mercado operam como marketplaces genéricos: eles colocam o cliente dentro de uma lista com dezenas de barbearias concorrentes, além de cobrarem taxas por agendamento ou mensalidades crescentes por cada barbeiro cadastrado. Nossa proposta é White-Label e All-in-One: a barbearia tem sua própria plataforma e identidade, fortalecendo a fidelização dos clientes, sem pagar comissão sobre seus serviços, operando em VPS própria por menos de R$ 50/mês."*

### Pergunta 2: *"Por que usar Java 21 e Spring Boot para uma barbearia? Não é uma tecnologia muito 'pesada' para um pequeno negócio?"*
* **Como responder:**
  > *"Escolhemos Java 21 LTS e Spring Boot 3 pela confiabilidade transacional e segurança. Em sistemas de agendamento e e-commerce, concorrência de acessos e atomicidade são pontos críticos — não podemos permitir que dois clientes reservem o mesmo slot ao mesmo tempo. Com Java, implementamos padrões consolidados como DataMapper e Unit of Work com controle ACID de transações, garantindo que o sistema seja extremamente estável, seguro contra injeções SQL e pronto para escalar para dezenas de barbearias sem reescrever o código."*

### Pergunta 3: *"Como vocês garantem tecnicamente que não vai ocorrer agendamento duplicado (double-booking)?"*
* **Como responder:**
  > *"O motor divide o dia em slots modulares de 20 minutos. Quando um serviço de 40 ou 60 minutos é selecionado, o algoritmo valida se há janelas contínuas livres para o profissional escolhido. Essa validação ocorre tanto na camada de negócio quanto no momento da gravação no banco de dados via transação atômica. Validamos esse comportamento através de uma suíte de testes automatizados com JUnit 5 simulando cenários concorrentes, alcançando 100% de assertividade."*

### Pergunta 4: *"O que representa o link PHP que está no protótipo vs o projeto em Spring Boot?"*
* **Como responder:**
  > *"O link hospedado representa nosso MVP e protótipo funcional de UX/UI, utilizado para validar rapidamente os fluxos visuais, usabilidade e regras com o proprietário da Tgo’s Barbearia. Uma vez validada a experiência de usuário, a arquitetura definitiva foi construída sobre a suíte robusta Java 21 e Spring Boot 3, garantindo segurança empresarial, RBAC e testes automatizados."*

### Pergunta 5: *"De que forma a Inteligência Artificial entra no projeto?"*
* **Como responder:**
  > *"A IA é a nossa proposta de evolução tecnológica futura (Roadmap). A funcionalidade em desenvolvimento é um simulador de visagismo virtual: o cliente poderá enviar uma foto pelo smartphone e testar diferentes estilos de corte e barba antes do atendimento. Isso aumenta o engajamento na plataforma e agrega alto valor percebido ao serviço da barbearia."*

---

## **5. Checklist de Preparação para o Grande Dia**

### Material Visual e Estande:
- [ ] **QR Code em Acrílico ou Cartão:** Imprimir plaquinhas com QR Code apontando direto para o protótipo online para que visitantes e jurados possam testar em seus próprios celulares.
- [ ] **Banner / Painel Visual:** Destacar em letras legíveis: *Tgo's Barbearia*, o logotipo da equipe *GJW Projects*, os pilares (Agendamento, E-commerce, Gestão) e os selos dos **ODS 8, 9 e 12**.
- [ ] **Flyer Resumo (1 página):** Deixar algumas cópias impressas da proposta/resumo para entregar aos jurados que quiserem levar para deliberação da comissão.

### Equipamentos e Contingência Técnica:
- [ ] **Notebook principal** com bateria 100% carregada e carregador à mão.
- [ ] **Dispositivo móvel (Tablet ou Smartphone)** exclusivo para a demonstração do cliente.
- [ ] **Internet Reserva (Hotspot 4G/5G):** A rede Wi-Fi de feiras costuma oscilar muito. Tenham um plano de dados roteado no celular pronto para conectar imediatamente.
- [ ] **Ambiente Local de Backup:** Tenham o projeto (banco MySQL + aplicação) configurado localmente no notebook para rodar em `localhost`, garantindo que a demonstração funcione mesmo se não houver internet alguma.

### Postura da Equipe:
- [ ] Vestimenta alinhada (camiseta da equipe/curso ou social confortável).
- [ ] Crachás de identificação visíveis.
- [ ] Olhar no olho dos avaliadores, sorriso, postura receptiva e voz firme.
- [ ] Nunca interromper o colega durante uma resposta; se quiser complementar, aguarde a conclusão e diga: *"Complementando o ponto colocado pelo Gabriel/Josias/Wallace..."*.
