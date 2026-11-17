package com.gwj;

import static org.assertj.core.api.Assertions.assertThat;

import com.gwj.model.dataTransferObject.SetupDTO;
import com.gwj.model.dataTransferObject.dto.ClienteDTO;
import com.gwj.model.dataTransferObject.dto.UsuarioDTO;
import com.gwj.model.domain.Carrinho;
import com.gwj.model.domain.CarrinhoItem;
import com.gwj.model.domain.IEntity;
import com.gwj.model.domain.PerfilTipo;
import com.gwj.model.domain.entities.Agendamento;
import com.gwj.model.domain.entities.Cliente;
import com.gwj.model.domain.entities.DiasFuncionamento;
import com.gwj.model.domain.entities.Endereco;
import com.gwj.model.domain.entities.GradeHorarios;
import com.gwj.model.domain.entities.ItemPedido;
import com.gwj.model.domain.entities.Pedido;
import com.gwj.model.domain.entities.Perfil;
import com.gwj.model.domain.entities.Permissao;
import com.gwj.model.domain.entities.Produto;
import com.gwj.model.domain.entities.Profissional;
import com.gwj.model.domain.entities.Servico;
import com.gwj.model.domain.entities.Setting;
import com.gwj.model.domain.entities.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.predicate.RuntimeHintsPredicates;

@DisplayName("Testes de Metadados AOT / GraalVM Native Image (AppRuntimeHints)")
class AppRuntimeHintsTest {

  private RuntimeHints buildHints() {
    RuntimeHints hints = new RuntimeHints();
    new AppRuntimeHints().registerHints(hints, getClass().getClassLoader());
    return hints;
  }

  @Test
  @DisplayName("Deve registrar reflexão para todas as entidades de domínio")
  void deveRegistrarTodasAsEntidades() {
    RuntimeHints hints = buildHints();

    List<Class<?>> entities =
        List.of(
            Agendamento.class,
            Cliente.class,
            DiasFuncionamento.class,
            Endereco.class,
            GradeHorarios.class,
            ItemPedido.class,
            Pedido.class,
            Perfil.class,
            Permissao.class,
            Produto.class,
            Profissional.class,
            Servico.class,
            Setting.class,
            Usuario.class);

    for (Class<?> entity : entities) {
      assertThat(RuntimeHintsPredicates.reflection().onType(entity).test(hints))
          .as("Reflexão deve estar registrada para %s", entity.getName())
          .isTrue();
    }
  }

  @Test
  @DisplayName("Deve registrar reflexão para DTOs, objetos de domínio e tipos explícitos")
  void deveRegistrarDtosEDominioAuxiliar() {
    RuntimeHints hints = buildHints();

    List<Class<?>> types =
        List.of(
            IEntity.class,
            PerfilTipo.class,
            Carrinho.class,
            CarrinhoItem.class,
            SetupDTO.class,
            ClienteDTO.class,
            UsuarioDTO.class,
            AppConfig.class);

    for (Class<?> type : types) {
      assertThat(RuntimeHintsPredicates.reflection().onType(type).test(hints))
          .as("Reflexão deve estar registrada para %s", type.getName())
          .isTrue();
    }
  }

  @Test
  @DisplayName("Deve registrar drivers JDBC MariaDB e PostgreSQL")
  void deveRegistrarDriversJdbc() {
    RuntimeHints hints = buildHints();

    assertThat(
            RuntimeHintsPredicates.reflection().onType(org.mariadb.jdbc.Driver.class).test(hints))
        .isTrue();
    assertThat(RuntimeHintsPredicates.reflection().onType(org.postgresql.Driver.class).test(hints))
        .isTrue();
  }

  @Test
  @DisplayName("Deve registrar scripts SQL do setup e application.properties como recursos")
  void deveRegistrarRecursosDoSetup() {
    RuntimeHints hints = buildHints();

    assertThat(RuntimeHintsPredicates.resource().forResource("db/gwj5.sql").test(hints)).isTrue();
    assertThat(RuntimeHintsPredicates.resource().forResource("db/gwj5_postgres.sql").test(hints))
        .isTrue();
    assertThat(RuntimeHintsPredicates.resource().forResource("application.properties").test(hints))
        .isTrue();
  }

  @Test
  @DisplayName("Deve registrar serialização dos objetos armazenados em sessão HTTP")
  void deveRegistrarSerializacaoDeSessao() {
    RuntimeHints hints = buildHints();

    assertThat(RuntimeHintsPredicates.serialization().onType(Carrinho.class).test(hints)).isTrue();
    assertThat(RuntimeHintsPredicates.serialization().onType(CarrinhoItem.class).test(hints))
        .isTrue();
    assertThat(RuntimeHintsPredicates.serialization().onType(Produto.class).test(hints)).isTrue();
  }

  @Test
  @DisplayName("Deve registrar proxies dinâmicos JDK para HttpSession e HttpServletRequest")
  void deveRegistrarProxiesServlet() {
    RuntimeHints hints = buildHints();

    assertThat(RuntimeHintsPredicates.proxies().forInterfaces(HttpSession.class).test(hints))
        .isTrue();
    assertThat(RuntimeHintsPredicates.proxies().forInterfaces(HttpServletRequest.class).test(hints))
        .isTrue();
  }

  @Test
  @DisplayName("Deve registrar tipos JDK e utilitários de expressão do Thymeleaf para SpEL")
  void deveRegistrarTiposJdkEUtilitariosThymeleafParaSpel() {
    RuntimeHints hints = buildHints();

    List<Class<?>> spelTypes =
        List.of(
            String.class,
            List.class,
            Map.class,
            LocalDate.class,
            LocalTime.class,
            LocalDateTime.class,
            BigDecimal.class,
            org.thymeleaf.expression.Strings.class,
            org.thymeleaf.expression.Numbers.class,
            org.thymeleaf.expression.Temporals.class,
            org.thymeleaf.expression.Lists.class,
            org.thymeleaf.expression.Dates.class);

    for (Class<?> type : spelTypes) {
      assertThat(
              RuntimeHintsPredicates.reflection()
                  .onType(type)
                  .withMemberCategory(MemberCategory.INVOKE_PUBLIC_METHODS)
                  .test(hints))
          .as("Reflexão INVOKE_PUBLIC_METHODS deve estar registrada para %s", type.getName())
          .isTrue();
    }
  }
}
