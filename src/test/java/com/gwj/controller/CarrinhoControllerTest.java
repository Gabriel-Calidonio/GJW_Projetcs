package com.gwj.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.gwj.model.domain.Carrinho;
import com.gwj.model.domain.entities.Pedido;
import com.gwj.model.domain.entities.Produto;
import com.gwj.model.domain.entities.Setting;
import com.gwj.service.IService;
import com.gwj.service.ServiceRegistry;
import com.gwj.service.SetupService;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({CarrinhoController.class, Router.class})
@DisplayName("Testes do Carrinho, Mini-Carrinho e Checkout da Loja")
class CarrinhoControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private SetupService setupService;

  @BeforeEach
  @SuppressWarnings("unchecked")
  void setUp() {
    when(setupService.isConfigured()).thenReturn(true);
    IService<Setting> settingService = mock(IService.class);
    when(settingService.read(any())).thenReturn(Collections.emptyList());
    ServiceRegistry.registerService("Setting", settingService);
  }

  @AfterEach
  void tearDown() {
    ServiceRegistry.reset();
  }

  @Test
  @DisplayName("Deve renderizar o fragmento #miniCartItems quando o carrinho estiver vazio")
  void deveRenderizarMiniCartFragmentVazio() throws Exception {
    mockMvc
        .perform(get("/carrinho/mini-cart-fragment"))
        .andExpect(status().isOk())
        .andExpect(view().name("site/fragments/header :: #miniCartItems"))
        .andExpect(content().string(containsString("id=\"miniCartItems\"")))
        .andExpect(content().string(containsString("Seu carrinho está vazio.")));
  }

  @Test
  @DisplayName("Deve renderizar o fragmento #miniCartItems com os produtos adicionados na sessão")
  void deveRenderizarMiniCartFragmentComItens() throws Exception {
    Produto produto = new Produto();
    produto.setId(7L);
    produto.setNome("Pomada Modeladora Matte");
    produto.setPreco(new BigDecimal("49.90"));

    Carrinho carrinho = new Carrinho();
    carrinho.adicionarItem(produto, 2);

    MockHttpSession session = new MockHttpSession();
    session.setAttribute("carrinho", carrinho);

    mockMvc
        .perform(get("/carrinho/mini-cart-fragment").session(session))
        .andExpect(status().isOk())
        .andExpect(view().name("site/fragments/header :: #miniCartItems"))
        .andExpect(content().string(containsString("id=\"miniCartItems\"")))
        .andExpect(content().string(containsString("Pomada Modeladora Matte")))
        .andExpect(content().string(containsString("49,90")));
  }

  @Test
  @DisplayName("Deve renderizar a página /carrinho incluindo o script removerItemPagina")
  void deveRenderizarPaginaCarrinhoComScriptDeRemocao() throws Exception {
    Produto produto = new Produto();
    produto.setId(5L);
    produto.setNome("Óleo para Barba");
    produto.setPreco(new BigDecimal("35.00"));

    Carrinho carrinho = new Carrinho();
    carrinho.adicionarItem(produto, 1);

    MockHttpSession session = new MockHttpSession();
    session.setAttribute("carrinho", carrinho);

    mockMvc
        .perform(get("/carrinho").session(session))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("removerItemPagina(5)")))
        .andExpect(content().string(containsString("function removerItemPagina(produtoId)")));
  }

  @Test
  @DisplayName("Deve remover o item do carrinho via POST /carrinho/remover")
  void deveRemoverItemDoCarrinho() throws Exception {
    Produto produto = new Produto();
    produto.setId(9L);
    produto.setNome("Shampoo Refrescante");
    produto.setPreco(new BigDecimal("39.90"));

    Carrinho carrinho = new Carrinho();
    carrinho.adicionarItem(produto, 1);

    MockHttpSession session = new MockHttpSession();
    session.setAttribute("carrinho", carrinho);

    mockMvc
        .perform(post("/carrinho/remover").param("produtoId", "9").session(session))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.sucesso").value(true))
        .andExpect(jsonPath("$.quantidadeTotal").value(0));

    Carrinho atualizado = (Carrinho) session.getAttribute("carrinho");
    assertThat(atualizado.getQuantidadeTotal()).isZero();
  }

  @Test
  @DisplayName(
      "Deve confirmar pedido, esvaziar carrinho na sessão e redirecionar para /compra-confirmada")
  @SuppressWarnings("unchecked")
  void deveConfirmarPedidoEsvaziarCarrinhoERedirecionar() throws Exception {
    IService<Pedido> pedidoService = mock(IService.class);
    when(pedidoService.create(any(Pedido.class)))
        .thenAnswer(
            invocation -> {
              Pedido p = invocation.getArgument(0);
              p.setId(42L);
              return p;
            });
    when(pedidoService.read(any(Pedido.class)))
        .thenAnswer(
            invocation -> {
              Pedido p = new Pedido();
              p.setId(42L);
              p.setNomeVisitante("Carlos Silva");
              p.setFormaPagamento("PIX");
              p.setStatus("Aguardando Retirada");
              p.setValorTotal(new BigDecimal("49.90"));
              return List.of(p);
            });
    ServiceRegistry.registerService("Pedido", pedidoService);

    Produto produto = new Produto();
    produto.setId(7L);
    produto.setNome("Pomada Modeladora Matte");
    produto.setPreco(new BigDecimal("49.90"));

    Carrinho carrinho = new Carrinho();
    carrinho.adicionarItem(produto, 1);

    MockHttpSession session = new MockHttpSession();
    session.setAttribute("carrinho", carrinho);

    mockMvc
        .perform(
            post("/carrinho/checkout/confirmar")
                .param("nomeVisitante", "Carlos Silva")
                .param("telefoneVisitante", "(11) 99999-9999")
                .param("formaPagamento", "PIX")
                .session(session))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/compra-confirmada?id=42"));

    Carrinho carrinhoPosCompra = (Carrinho) session.getAttribute("carrinho");
    assertThat(carrinhoPosCompra.getQuantidadeTotal()).isZero();

    mockMvc
        .perform(get("/compra-confirmada").param("id", "42").session(session))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("redirectCountdown")))
        .andExpect(content().string(containsString("Voltar para a Página Principal")));
  }
}
