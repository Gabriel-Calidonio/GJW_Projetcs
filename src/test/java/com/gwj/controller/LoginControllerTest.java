package com.gwj.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gwj.model.domain.entities.Perfil;
import com.gwj.model.domain.entities.Usuario;
import com.gwj.service.IService;
import com.gwj.service.ServiceRegistry;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@DisplayName("🧪 Suíte de Testes de Autenticação e Gestão de Sessão (Login)")
class LoginControllerTest {

  private MockMvc mockMvc;
  private IService<Usuario> usuarioServiceMock;

  @BeforeEach
  @SuppressWarnings("unchecked")
  void setUp() {
    usuarioServiceMock = mock(IService.class);
    ServiceRegistry.registerService("Usuario", usuarioServiceMock);

    LoginController controller = new LoginController();
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @AfterEach
  void tearDown() {
    ServiceRegistry.reset();
  }

  private Usuario criarUsuarioMock(String email, String senhaPura, Long perfilId) {
    Usuario usuario = new Usuario();
    usuario.setId(10L);
    usuario.setNomeUsuario("usuario_teste");
    usuario.setEmail(email);
    usuario.setSenha("{sha256}" + PasswordUtil.hash(senhaPura));
    usuario.setStatus(true);

    Perfil perfil = new Perfil();
    perfil.setId(perfilId);
    perfil.setNome(perfilId == 4L ? "Cliente" : "Administrador");
    usuario.setPerfil(perfil);

    return usuario;
  }

  // =========================================================================
  // 1. RENDERIZAÇÃO E ACESSO À TELA DE LOGIN
  // =========================================================================
  @Nested
  @DisplayName("1. Acesso à Página de Login (/login)")
  class AcessoPaginaLogin {

    @Test
    @DisplayName("Deve exibir o formulário de login para usuário não autenticado")
    void deveExibirFormularioDeLogin() throws Exception {
      mockMvc
          .perform(get("/login"))
          .andExpect(status().isOk())
          .andExpect(view().name("site/auth/login"));
    }

    @Test
    @DisplayName("Deve exibir mensagem de sucesso quando redirecionado do cadastro")
    void deveExibirMensagemDeSucessoDoCadastro() throws Exception {
      mockMvc
          .perform(get("/login").param("sucesso", "true"))
          .andExpect(status().isOk())
          .andExpect(view().name("site/auth/login"))
          .andExpect(model().attributeExists("sucesso"))
          .andExpect(
              model()
                  .attribute(
                      "sucesso", "Cadastro realizado com sucesso! Faça login para continuar."));
    }

    @Test
    @DisplayName("Deve redirecionar Cliente com sessão ativa direto para a home (/)")
    void deveRedirecionarClienteLogadoParaHome() throws Exception {
      MockHttpSession session = new MockHttpSession();
      session.setAttribute("usuarioLogado", criarUsuarioMock("cliente@teste.com", "12345", 4L));

      mockMvc
          .perform(get("/login").session(session))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/"));
    }

    @Test
    @DisplayName("Deve redirecionar Administrador com sessão ativa para o painel (/MRYnZpAsC9sp)")
    void deveRedirecionarAdminLogadoParaPainel() throws Exception {
      MockHttpSession session = new MockHttpSession();
      session.setAttribute("usuarioLogado", criarUsuarioMock("admin@teste.com", "12345", 1L));

      mockMvc
          .perform(get("/login").session(session))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/MRYnZpAsC9sp"));
    }
  }

  // =========================================================================
  // 2. FLUXO DE AUTENTICAÇÃO (POST /login)
  // =========================================================================
  @Nested
  @DisplayName("2. Processamento do Login (POST /login)")
  class ProcessamentoLogin {

    @Test
    @DisplayName("Deve autenticar Cliente com sucesso, criar sessão e redirecionar para a home (/)")
    void deveAutenticarClienteComSucesso() throws Exception {
      Usuario cliente = criarUsuarioMock("cliente@teste.com", "Senha@123", 4L);
      when(usuarioServiceMock.read(any(Usuario.class))).thenReturn(List.of(cliente));

      mockMvc
          .perform(post("/login").param("email", "cliente@teste.com").param("senha", "Senha@123"))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/"))
          .andExpect(request().sessionAttribute("usuarioLogado", cliente));
    }

    @Test
    @DisplayName("Deve autenticar Administrador e redirecionar para o painel (/MRYnZpAsC9sp)")
    void deveAutenticarAdminComSucesso() throws Exception {
      Usuario admin = criarUsuarioMock("admin@teste.com", "Senha@123", 1L);
      when(usuarioServiceMock.read(any(Usuario.class))).thenReturn(List.of(admin));

      mockMvc
          .perform(post("/login").param("email", "admin@teste.com").param("senha", "Senha@123"))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/MRYnZpAsC9sp"))
          .andExpect(request().sessionAttribute("usuarioLogado", admin));
    }

    @Test
    @DisplayName(
        "Deve autenticar com sucesso mesmo se o e-mail contiver letras maiúsculas (Case Insensitive)")
    void deveAutenticarMesmoComEmailMaiusculo() throws Exception {
      Usuario cliente = criarUsuarioMock("cliente@teste.com", "Senha@123", 4L);
      when(usuarioServiceMock.read(any(Usuario.class))).thenReturn(List.of(cliente));

      mockMvc
          .perform(post("/login").param("email", "CLIENTE@TESTE.COM").param("senha", "Senha@123"))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/"))
          .andExpect(request().sessionAttribute("usuarioLogado", cliente));
    }

    @Test
    @DisplayName("Deve autenticar com sucesso senhas em texto puro (retrocompatibilidade legada)")
    void deveAutenticarSenhaEmTextoPuro() throws Exception {
      Usuario usuarioLegado = criarUsuarioMock("legado@teste.com", "qualquer", 4L);
      usuarioLegado.setSenha("SenhaTextoPuro123"); // Sem prefixo {sha256}
      when(usuarioServiceMock.read(any(Usuario.class))).thenReturn(List.of(usuarioLegado));

      mockMvc
          .perform(
              post("/login").param("email", "legado@teste.com").param("senha", "SenhaTextoPuro123"))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/"))
          .andExpect(request().sessionAttribute("usuarioLogado", usuarioLegado));
    }

    @Test
    @DisplayName("Deve falhar quando a senha estiver incorreta (sem gravar sessão)")
    void deveFalharQuandoSenhaEstiverIncorreta() throws Exception {
      Usuario cliente = criarUsuarioMock("cliente@teste.com", "SenhaCorreta", 4L);
      when(usuarioServiceMock.read(any(Usuario.class))).thenReturn(List.of(cliente));

      mockMvc
          .perform(
              post("/login").param("email", "cliente@teste.com").param("senha", "SenhaIncorreta"))
          .andExpect(status().isOk())
          .andExpect(view().name("site/auth/login"))
          .andExpect(model().attribute("erro", "E-mail ou senha inválidos."))
          .andExpect(request().sessionAttributeDoesNotExist("usuarioLogado"));
    }

    @Test
    @DisplayName("Deve falhar com mensagem idêntica quando e-mail não existir (Anti-Enumeração)")
    void deveFalharQuandoEmailNaoExistir() throws Exception {
      when(usuarioServiceMock.read(any(Usuario.class))).thenReturn(Collections.emptyList());

      mockMvc
          .perform(
              post("/login")
                  .param("email", "inexistente@teste.com")
                  .param("senha", "QualquerSenha"))
          .andExpect(status().isOk())
          .andExpect(view().name("site/auth/login"))
          .andExpect(model().attribute("erro", "E-mail ou senha inválidos."))
          .andExpect(request().sessionAttributeDoesNotExist("usuarioLogado"));
    }
  }

  // =========================================================================
  // 3. FLUXO DE LOGIN ADMINISTRATIVO MASCARADO (/MRYnZpAsC9sp/login)
  // =========================================================================
  @Nested
  @DisplayName("3. Login Administrativo Restrito (/MRYnZpAsC9sp/login)")
  class LoginAdministrativo {

    @Test
    @DisplayName("Deve permitir login administrativo para usuários com Perfil Admin (ID != 4)")
    void devePermitirLoginParaAdmin() throws Exception {
      Usuario admin = criarUsuarioMock("admin@empresa.com", "Senha@Admin123", 1L);
      when(usuarioServiceMock.read(any(Usuario.class))).thenReturn(List.of(admin));

      mockMvc
          .perform(
              post("/MRYnZpAsC9sp/login")
                  .param("email", "admin@empresa.com")
                  .param("senha", "Senha@Admin123"))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/MRYnZpAsC9sp"))
          .andExpect(request().sessionAttribute("usuarioLogado", admin));
    }

    @Test
    @DisplayName(
        "Deve bloquear Cliente (Perfil 4) de logar na área administrativa com 'Acesso negado'")
    void deveBloquearClienteNaAreaAdministrativa() throws Exception {
      Usuario cliente = criarUsuarioMock("cliente@teste.com", "Senha@123", 4L);
      when(usuarioServiceMock.read(any(Usuario.class))).thenReturn(List.of(cliente));

      mockMvc
          .perform(
              post("/MRYnZpAsC9sp/login")
                  .param("email", "cliente@teste.com")
                  .param("senha", "Senha@123"))
          .andExpect(status().isOk())
          .andExpect(view().name("admin/auth/login"))
          .andExpect(
              model().attribute("erro", "Acesso negado: esta área é restrita a administradores."))
          .andExpect(request().sessionAttributeDoesNotExist("usuarioLogado"));
    }

    @Test
    @DisplayName(
        "Deve falhar com 'E-mail ou senha inválidos' para credenciais incorretas na área admin")
    void deveFalharParaCredenciaisIncorretasNoAdmin() throws Exception {
      when(usuarioServiceMock.read(any(Usuario.class))).thenReturn(Collections.emptyList());

      mockMvc
          .perform(
              post("/MRYnZpAsC9sp/login")
                  .param("email", "admin@teste.com")
                  .param("senha", "SenhaErrada"))
          .andExpect(status().isOk())
          .andExpect(view().name("admin/auth/login"))
          .andExpect(model().attribute("erro", "E-mail ou senha inválidos."));
    }
  }

  // =========================================================================
  // 4. ENCERRAMENTO DE SESSÃO (LOGOUT)
  // =========================================================================
  @Nested
  @DisplayName("4. Gestão e Encerramento de Sessão (Logout)")
  class LogoutTest {

    @Test
    @DisplayName("Deve invalidar sessão e redirecionar para /login ao chamar /logout")
    void deveEncerrarSessaoERedirecionarParaLogin() throws Exception {
      MockHttpSession session = new MockHttpSession();
      session.setAttribute("usuarioLogado", criarUsuarioMock("cliente@teste.com", "12345", 4L));

      mockMvc
          .perform(get("/logout").session(session))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/login"));

      assertThat(session.isInvalid()).isTrue();
    }

    @Test
    @DisplayName("Deve invalidar sessão administrativa e redirecionar para a rota administrativa")
    void deveEncerrarSessaoAdminERedirecionarParaLoginAdmin() throws Exception {
      MockHttpSession session = new MockHttpSession();
      session.setAttribute("usuarioLogado", criarUsuarioMock("admin@teste.com", "12345", 1L));

      mockMvc
          .perform(get("/MRYnZpAsC9sp/logout").session(session))
          .andExpect(status().is3xxRedirection())
          .andExpect(redirectedUrl("/MRYnZpAsC9sp/login"));

      assertThat(session.isInvalid()).isTrue();
    }
  }
}
