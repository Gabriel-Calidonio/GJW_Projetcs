# DP-11: Implementação de Edição de Configurações da Barbearia / Loja

- **Tipo:** Deployment plan
- **Status:** closed
- **Autor:** GWJ
- **Criado em:** 2026-08-31 14:04:10
- **Labels:** Nenhuma
- **Responsáveis:** Nenhum

## Descrição

# Plano de Implementação: Edição de Configurações da Barbearia / Loja

Elaboração do fluxo completo para permitir que o administrador edite e salve todos os parâmetros da loja (nome, contatos, endereços, horários, imagens de logo/fachada e textos institucionais) diretamente pelo painel administrativo, com persistência na tabela `tab_setting`.

## User Review Required

> [!NOTE]
> Os parâmetros configurados neste formulário são refletidos automaticamente em todo o site (Navbar, Rodapé, Página Inicial / Sobre Nós, etc.) por meio do `GlobalAttributesAdvice` que já consome os dados de `tab_setting`.

## Proposed Changes

### Camada de Serviço e Negócio

#### [NEW] [SettingService.java](/src/main/java/com/gwj/service/SettingService.java)
- Extende `GenericService<Setting>` para operações padrão.
- Adiciona os métodos utilitários:
  - `public Map<String, String> getAllAsMap()`: retorna todas as configurações em formato de dicionário `chave -> valor`.
  - `public void updateSettings(Map<String, String> newSettings)`: atualiza em lote as configurações existentes ou cria novas caso ainda não existam no banco, gerenciando a transação via `UnitOfWork`.

#### [MODIFY] [ServiceRegistry.java](/src/main/java/com/gwj/service/ServiceRegistry.java)
- Registra `SettingService` no mapa estático do `ServiceRegistry`: `registry.put("Setting", new SettingService());`.

---

### Camada de Controle

#### [MODIFY] [AdminSettingController.java](/src/main/java/com/gwj/controller/AdminSettingController.java)
- **`GET /MRYnZpAsC9sp/configuracoes`**: Carrega o mapa de configurações atuais e envia para a view `form.html`.
- **`POST /MRYnZpAsC9sp/configuracoes/salvar`**: Recebe os parâmetros do formulário, invoca `settingService.updateSettings(params)`, adiciona mensagem de sucesso via `RedirectAttributes` e redireciona de volta para a tela de configurações.

---

### Camada de Apresentação (View)

#### [MODIFY] [form.html](/src/main/resources/templates/admin/setting/store-setting/form.html)
- Reestruturação do template para um formulário moderno, dividido em 4 cartões temáticos:
  1. **🏢 Identificação da Barbearia:** Nome da Loja (`nome`), Slogan/Descrição Curta (`descricao`), URL da Logomarca (`logo_url`) e Texto Alternativo (`logo_alt`).
  2. **📞 Contato e Localização:** Telefone/WhatsApp (`telefone`), E-mail de Contato (`email`), Endereço Resumido (`endereco_curto`) e Endereço Completo com CEP (`endereco_completo`).
  3. **🕒 Horários de Funcionamento:** Dias e Horários de Atendimento (`horarios`).
  4. **📜 Seção "Sobre Nós" (Institucional):** Título (`sobre_titulo`), Parágrafo 1 (`sobre_texto1`), Parágrafo 2 (`sobre_texto2`) e URL da Foto da Barbearia (`sobre_imagem`).
- Inclusão de alerta de sucesso (`th:if="${sucesso}"`), botões de ação estilizados ("Salvar Alterações" e "Voltar") e preview visual para links de imagens.

---

## Verification Plan

### Automated / Manual Verification
1. **Compilação e Teste de Unidade/Serviço:**
   - Executar `./mvnw compile` para garantir que `SettingService` e `AdminSettingController` compilam perfeitamente sem erros de tipagem.
2. **Teste de Atualização de Dados:**
   - Realizar requisição POST com novos valores para `nome`, `telefone`, `horarios`, etc., e verificar se os registros na tabela `tab_setting` foram atualizados.
3. **Teste de Reflexo no Site Público:**
   - Acessar a página inicial pública (`/`) e rodapé (`/sobre-nos` ou footer) para verificar se as novas configurações salvas aparecem refletidas na interface pública.
