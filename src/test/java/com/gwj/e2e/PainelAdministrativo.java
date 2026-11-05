package com.gwj.e2e;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class PainelAdministrativo {

    @LocalServerPort
    private int port;

    private static Playwright playwright;
    private static Browser browser;
    private BrowserContext context;
    private Page page;

    private static final Path VIDEO_DIR = Paths.get("target/videos");

    @BeforeAll
    static void setUpAll() throws Exception {
        Files.createDirectories(VIDEO_DIR);
        playwright = Playwright.create();

        // No Debian 13, prioriza o Chromium nativo (/usr/bin/chromium) para fontes e renderização perfeitas
        Path nativeChromium = Paths.get("/usr/bin/chromium");
        BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(600); // 600ms para uma demonstração suave e visual

        if (Files.exists(nativeChromium)) {
            browser = playwright.chromium().launch(options.setExecutablePath(nativeChromium));
        } else {
            browser = playwright.firefox().launch(options);
        }
    }

    @AfterAll
    static void tearDownAll() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void setUp() {
        // Gravação em Full HD (1920 x 1080)
        Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setRecordVideoDir(VIDEO_DIR)
                .setRecordVideoSize(1920, 1080)
                .setViewportSize(1920, 1080);

        context = browser.newContext(options);
        page = context.newPage();
    }

    @Test
    @DisplayName("Tour do Gestor pelo Painel Administrativo e Edição de Agendamento")
    void tourGestorPainelAdministrativo() {
        String baseUrl = "http://localhost:" + port + "/" + com.gwj.AppConfig.ADMIN_ROUTE_PREFIX;

        // =========================================================================
        // 1. LOGIN ADMINISTRATIVO
        // =========================================================================
        page.navigate(baseUrl + "/login");
        page.waitForTimeout(500);

        // Preenche as credenciais do gestor
        page.locator("#email").fill("josias@tgosbarbearia.com");
        page.locator("#senha").fill("42010052");
        page.locator("button[type='submit']").click();

        // Aguarda entrada no Dashboard
        page.waitForURL("**/" + com.gwj.AppConfig.ADMIN_ROUTE_PREFIX + "**");
        page.waitForTimeout(1000);

        // =========================================================================
        // 2. TOUR DE VISUALIZAÇÃO PELOS MÓDULOS (SIDEBAR)
        // =========================================================================
        // Visualiza Catálogo -> Produtos
        page.locator("#sidebar a:has-text('Produtos')").click();
        page.waitForURL("**/produtos**");
        page.waitForTimeout(1000);

        // Visualiza Catálogo -> Serviços
        page.locator("#sidebar a:has-text('Serviços')").click();
        page.waitForURL("**/servicos**");
        page.waitForTimeout(1000);

        // Visualiza Vendas -> Pedidos
        page.locator("#sidebar a:has-text('Pedidos')").click();
        page.waitForURL("**/pedidos**");
        page.waitForTimeout(1000);

        // Visualiza Pessoas -> Clientes
        page.locator("#sidebar a:has-text('Clientes')").click();
        page.waitForURL("**/clientes**");
        page.waitForTimeout(1000);

        // Visualiza Pessoas -> Profissionais
        page.locator("#sidebar a:has-text('Profissionais')").click();
        page.waitForURL("**/profissionais**");
        page.waitForTimeout(1000);

        // Visualiza Configurações da Barbearia
        page.locator("#sidebar a:has-text('Configurações')").click();
        page.waitForURL("**/configuracoes**");
        page.waitForTimeout(1000);

        // =========================================================================
        // 3. GESTÃO DE AGENDAMENTOS
        // =========================================================================
        page.locator("#sidebar a:has-text('Agendamentos')").click();
        page.waitForURL("**/agendamentos**");
        page.waitForTimeout(1000);

        // =========================================================================
        // 4. EDIÇÃO DE DATA E HORÁRIO DE UM AGENDAMENTO
        // =========================================================================
        // Localiza a linha do primeiro agendamento na tabela e clica no botão Editar (ícone de lápis)
        Locator btnEditar = page.locator("a[title='Editar'], .bi-pencil").first();
        if (btnEditar.isVisible()) {
            btnEditar.click();
            page.waitForURL("**/agendamentos/editar/**");
            page.waitForTimeout(800);

            // Atualiza a data do agendamento
            page.locator("#dataAgendamento").fill("2026-11-12");

            // Atualiza o horário de início e término
            page.locator("#horaInicio").fill("15:30");
            page.locator("#horaFim").fill("16:15");

            // Confirmação de status
            page.locator("#status").selectOption("Confirmado");
            page.waitForTimeout(800);

            // Salva as alterações
            page.locator("button[type='submit']:has-text('Atualizar Agendamento')").click();

            // Retorno à lista de agendamentos com as informações atualizadas
            page.waitForURL("**/agendamentos**");
            page.waitForTimeout(1500);
        }
    }

    @AfterEach
    void tearDown() {
        try {
            if (page != null) {
                // Aguarda 1s final para capturar a tela estabilizada no vídeo
                page.waitForTimeout(1000);
                page.close();
            }
        } catch (Exception ignored) {}

        if (context != null) {
            try {
                context.close();
            } catch (Exception ignored) {}
        }

        // Salva o arquivo de vídeo com nome amigável
        if (page != null && page.video() != null) {
            try {
                Path destino = VIDEO_DIR.resolve("painel-administrativo-1080p.webm");
                page.video().saveAs(destino);
                System.out.println("Vídeo da demonstração salvo com sucesso em: " + destino.toAbsolutePath());
            } catch (Exception e) {
                System.err.println("Aviso ao salvar vídeo: " + e.getMessage());
            }
        }
    }
}
