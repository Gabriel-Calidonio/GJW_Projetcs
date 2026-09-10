# Walkthrough-011: Edição de Configurações da Barbearia & Diagnóstico de Ambiente

Implementamos o módulo completo de **Edição de Configurações da Barbearia** no painel administrativo, permitindo que o administrador altere o nome da loja, slogan, dados de contato, horários de atendimento, endereço e textos da seção institucional.

---

## 🛠️ O que foi Desenvolvido

1. **[SettingService.java](/src/main/java/com/gwj/service/SettingService.java)**
   - Extende `GenericService<Setting>`.
   - Adiciona método `getAllAsMap()` para leitura das propriedades em mapa `chave -> valor`.
   - Adiciona método `updateSettings(Map<String, String>)` para atualização em lote com `UPSERT` (`INSERT ... ON DUPLICATE KEY UPDATE`) em uma única transação atômica (`UnitOfWork`).

2. **[ServiceRegistry.java](/src/main/java/com/gwj/service/ServiceRegistry.java)**
   - Registrado o `SettingService` no catálogo de serviços do projeto.

3. **[AdminSettingController.java](/src/main/java/com/gwj/controller/AdminSettingController.java)**
   - `GET /MRYnZpAsC9sp/configuracoes`: Carrega o mapa de configurações da barbearia para o formulário.
   - `POST /MRYnZpAsC9sp/configuracoes/salvar`: Recebe todas as alterações, salva no banco e redireciona com mensagem flash de sucesso.

4. **[form.html](/src/main/resources/templates/admin/setting/store-setting/form.html)**
   - Interface com design moderno e responsivo dividida em 4 seções:
     - 🏢 **Identificação & Marca:** Nome da loja, slogan, URL do logo e texto alternativo.
     - 📞 **Contato & Localização:** Telefone/WhatsApp, e-mail, endereço resumido e endereço completo.
     - 🕒 **Horários de Funcionamento:** Dias e horários de atendimento.
     - 📜 **Institucional ("Sobre Nós"):** Título da seção, parágrafos 1 e 2, e URL da foto ilustrativa.
   - Barra de ação inferior com botão "Salvar Todas as Configurações" e alertas visuais de sucesso/erro.

---

## 🧪 Validação dos Testes

1. **Persistência no Banco de Dados (`tab_setting`):**
   - Testado envio via `POST /MRYnZpAsC9sp/configuracoes/salvar` com novos valores de nome, telefone, endereço, etc.
   - Os registros foram atualizados com sucesso no MariaDB.

2. **Reflexo Dinâmico no Site Público:**
   - Acesso à rota pública `/` e verificação do cabeçalho e rodapé. Os novos dados (`Tgo's Barbearia Elegance`, `(11) 97777-8888`, `Av. Paulista, 1000`) foram renderizados automaticamente em tempo real através do `GlobalAttributesAdvice`.

3. **Validador de Ambiente (`./check.sh`):**
   - Executado e validado com sucesso.
