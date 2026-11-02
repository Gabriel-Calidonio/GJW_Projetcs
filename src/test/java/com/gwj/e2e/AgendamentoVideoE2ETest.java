package com.gwj.e2e;

import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class AgendamentoVideoE2ETest {

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
        
        // No Debian 13, o /usr/bin/chromium nativo do sistema possui todas as bibliotecas de fontes
        // renderizando 100% dos botões e textos sem problemas de frames de vídeo
        Path nativeChromium = Paths.get("/usr/bin/chromium");
        BrowserType.LaunchOptions launchOptions = new BrowserType.LaunchOptions()
                .setHeadless(false)
                .setSlowMo(500); // 500ms de delay para tornar o vídeo didático e visível

        if (Files.exists(nativeChromium)) {
            browser = playwright.chromium().launch(launchOptions.setExecutablePath(nativeChromium));
        } else {
            browser = playwright.firefox().launch(launchOptions);
        }
    }

    @AfterAll
    static void tearDownAll() {
        if (browser != null) browser.close();
        if (playwright != null) playwright.close();
    }

    @BeforeEach
    void setUp() {
        // Configuração do Vídeo em 1920x1080
        Browser.NewContextOptions options = new Browser.NewContextOptions()
                .setRecordVideoDir(VIDEO_DIR)
                .setRecordVideoSize(1920, 1080)       // Resolução do vídeo
                .setViewportSize(1920, 1080);          // Resolução do viewport da página

        context = browser.newContext(options);
        page = context.newPage();
    }

    @Test
    @DisplayName("Grava o ciclo completo de agendamento em Full HD")
    void gravarCicloAgendamento() {
        // 1. Acessa a página inicial (porta configurada no Spring Boot)
        page.navigate("http://localhost:" + port + "/");

        // 2. Navega até a seleção de serviços
        page.locator("a:has-text('Agendar Agora'), a:has-text('Serviços')").first().click();
        page.waitForURL("**/servicos**");

        // 3. Seleciona data desejada e aguarda o carregamento dos horários
        // Definimos uma data próxima (Terça-feira com expediente aberto) e disparamos o evento change
        page.locator("#inputData").fill("2026-11-10");
        page.locator("#inputData").dispatchEvent("change");
        page.waitForSelector("#slotsGrid .time-slot-btn:not(:disabled)", new Page.WaitForSelectorOptions().setTimeout(10000));

        // 4. Seleciona o primeiro horário disponível
        page.locator("#slotsGrid .time-slot-btn:not(:disabled)").first().click();

        // 5. Clica em "Confirmar e Pagar" para prosseguir para o checkout
        page.locator("#btnConfirmar").click();
        page.waitForURL("**/checkout**");

        // 6. Preenche os dados do cliente no formulário de checkout
        page.locator("input[name='nome']").fill("Pedro");
        page.locator("input[name='sobrenome']").fill("Toledo");
        page.locator("input[name='telefone']").fill("11999999999");
        page.locator("input[name='email']").fill("pedro.toledo@exemplo.com");
        // Método de Pagamento
        page.locator("input[name='pagamento'][value='presencial']").check();

        // 7. Confirma a reserva
        page.locator("button[type='submit'][form='checkoutForm'], button:has-text('Confirmar Reserva')").click();

        // 8. Validação da tela de confirmação 
        page.waitForURL("**/order-confirmation**");
        Assertions.assertTrue(page.url().contains("order-confirmation"));
    }

    @AfterEach
    void tearDown() {
        try {
            if (page != null) {
                // Aguarda um pequeno intervalo para garantir a gravação do último quadro
                page.waitForTimeout(500);
            }
        } catch (Exception ignored) {}

        Video video = (page != null) ? page.video() : null;

        try {
            if (page != null) {
                page.close();
            }
        } catch (Exception ignored) {}

        if (context != null) {
            try {
                context.close();
            } catch (Exception ignored) {}
        }

        // Renomeia o arquivo UUID gerado para um nome fixo e legível
        if (video != null) {
            try {
                Path destino = VIDEO_DIR.resolve("ciclo-agendamento-1080p.webm");
                video.saveAs(destino);
                System.out.println("Vídeo salvo com sucesso em: " + destino.toAbsolutePath());
            } catch (Exception e) {
                System.err.println("Aviso ao salvar vídeo: " + e.getMessage());
            }
        }
    }
}