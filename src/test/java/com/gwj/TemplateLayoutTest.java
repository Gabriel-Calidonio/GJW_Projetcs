package com.gwj;

import static org.assertj.core.api.Assertions.assertThat;

import com.gwj.model.domain.Carrinho;
import java.util.Collections;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

@DisplayName("Testes de Renderização dos Layouts Nativos do Thymeleaf")
class TemplateLayoutTest {

  private SpringTemplateEngine templateEngine;
  private MockServletContext servletContext;

  @BeforeEach
  void setUp() {
    ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
    resolver.setPrefix("templates/");
    resolver.setSuffix(".html");
    resolver.setTemplateMode(TemplateMode.HTML);
    resolver.setCharacterEncoding("UTF-8");

    templateEngine = new SpringTemplateEngine();
    templateEngine.setTemplateResolver(resolver);
    servletContext = new MockServletContext();
  }

  private String render(String templateName, Map<String, Object> model) {
    MockHttpServletRequest request = new MockHttpServletRequest(servletContext);
    MockHttpServletResponse response = new MockHttpServletResponse();
    JakartaServletWebApplication app =
        JakartaServletWebApplication.buildApplication(servletContext);
    WebContext context = new WebContext(app.buildExchange(request, response));
    context.setVariable(
        "empresa",
        Map.ofEntries(
            Map.entry("nome", "Tgo's Barbearia"),
            Map.entry("descricao", "Descrição"),
            Map.entry("endereco_curto", "Rua A, 1"),
            Map.entry("endereco_completo", "Rua A, 1"),
            Map.entry("telefone", "11999999999"),
            Map.entry("email", "contato@teste.com"),
            Map.entry("sobre_titulo", "Sobre"),
            Map.entry("sobre_texto1", "Texto 1"),
            Map.entry("sobre_texto2", "Texto 2"),
            Map.entry("sobre_imagem", "/img/no-image.png"),
            Map.entry("logo_url", "/img/logo.png"),
            Map.entry("logo_alt", "Logo")));
    context.setVariable("carrinho", new Carrinho());
    context.setVariable("adminPrefix", "adm_test");
    model.forEach(context::setVariable);
    return templateEngine.process(templateName, context);
  }

  @Test
  @DisplayName("Deve renderizar template do site com script extra no head (login)")
  void deveRenderizarSiteComScriptNoHead() {
    String html = render("site/auth/login", Map.of());

    assertThat(html)
        .contains("<title>Login - Barbearia GWJ</title>")
        .contains("/js/validation.js")
        .contains("id=\"loginForm\"")
        .contains("modern-header")
        .contains("modern-footer");
  }

  @Test
  @DisplayName("Deve renderizar template do site sem script extra no head (sobre e error)")
  void deveRenderizarSiteSemScriptNoHead() {
    String sobreHtml = render("site/pages/sobre", Map.of());
    assertThat(sobreHtml)
        .contains("<title>Sobre Nós - Tgo&#39;s Barbearia</title>")
        .contains("modern-header")
        .contains("modern-footer");

    String errorHtml =
        render(
            "site/errors/error",
            Map.of("status", 404, "error", "Não encontrado", "message", "Página ausente"));
    assertThat(errorHtml)
        .contains("<title>Página de Erro - GWJ</title>")
        .contains("Não encontrado");
  }

  @Test
  @DisplayName("Deve renderizar template do carrinho com scripts inline preservados")
  void deveRenderizarCarrinhoComScripts() {
    String html = render("site/cart/carrinho", Map.of());

    assertThat(html)
        .contains("<title>Carrinho - Barbearia</title>")
        .contains("atualizarQuantidadePagina")
        .contains("removerItemPagina");
  }

  @Test
  @DisplayName("Deve renderizar templates administrativos com layout nativo")
  void deveRenderizarTemplatesAdmin() {
    String dashboardHtml =
        render(
            "admin/dashboard/index",
            Map.of(
                "totalAgendamentos", 1,
                "totalPedidos", 2,
                "totalClientes", 3,
                "totalProdutos", 4,
                "totalServicos", 5,
                "totalProfissionais", 6));

    assertThat(dashboardHtml)
        .contains("<title>Painel Administrativo - GWJ</title>")
        .contains("gwj-sidebar")
        .contains("Painel de Controle");

    String listarClientesHtml =
        render("admin/customer/customer/listar", Map.of("clientes", Collections.emptyList()));

    assertThat(listarClientesHtml)
        .contains("<title>Gestão de Clientes - Admin</title>")
        .contains("Gestão de Clientes")
        .contains("gwj-p-4");
  }
}
