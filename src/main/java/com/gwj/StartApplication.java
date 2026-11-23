package com.gwj;

import com.gwj.model.dataAccessObject.ConnectionDB;
import com.gwj.model.dataAccessObject.ConnectionPostGreSQL;
import com.gwj.model.dataAccessObject.DataMapper;
import com.gwj.model.dataAccessObject.QueryBuilder;
import com.gwj.model.dataTransferObject.EntityMapper;
import com.gwj.model.dataTransferObject.SetupDTO;
import com.gwj.model.dataTransferObject.dto.ClienteDTO;
import com.gwj.model.dataTransferObject.dto.DtoMapper;
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
import com.gwj.model.domain.factory.SchemaValidator;
import com.gwj.model.domain.factory.SimpleObjectFactory;
import com.gwj.model.repository.GenericRepository;
import com.gwj.model.repository.UsuarioRepository;
import com.gwj.service.AgendamentoService;
import com.gwj.service.GenericService;
import com.gwj.service.ServiceRegistry;
import com.gwj.service.SettingService;
import com.gwj.service.SetupService;
import com.gwj.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

@SpringBootApplication
@ImportRuntimeHints(StartApplication.GwjRuntimeHints.class)
@RegisterReflectionForBinding({
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
  Usuario.class,
  Carrinho.class,
  CarrinhoItem.class,
  IEntity.class,
  PerfilTipo.class,
  SetupDTO.class,
  ClienteDTO.class,
  UsuarioDTO.class
})
public class StartApplication {

  public static void main(String[] args) {
    // Carrega as variáveis do arquivo .env para o sistema
    AppConfig.loadEnv();

    // Roda a verificação automática de classes de entidade (não depende de banco)
    SchemaValidator.validateAllEntities();

    // Validações de banco executadas de forma resiliente
    // Se o banco ainda não existir ou não estiver configurado, o Setup Wizard assume em /setup
    try {
      SchemaValidator.ensureUniqueEmailIndex();
      SchemaValidator.ensureSettingsTableExists();
      SchemaValidator.ensureProductImageColumnAndDataExist();
      SchemaValidator.ensureOrderTablesExist();
    } catch (Exception e) {
      System.out.println(
          "ℹ️ Banco de dados ainda não inicializado no boot. O Setup Wizard estará disponível em /setup.");
    }

    // Este comando inicia o servidor Tomcat embutido e sobe a aplicação
    SpringApplication.run(StartApplication.class, args);
    System.out.println("=== Servidor Spring Boot iniciado com sucesso! ===");
  }

  static class GwjRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
      hints.resources().registerPattern("templates/**");
      hints.resources().registerPattern("static/**");
      hints.resources().registerPattern("db/**");
      hints.resources().registerPattern("application.properties");

      MemberCategory[] allMembers = MemberCategory.values();
      List<Class<?>> reflectionTypes =
          List.of(
              AppConfig.class,
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
              Usuario.class,
              Carrinho.class,
              CarrinhoItem.class,
              IEntity.class,
              PerfilTipo.class,
              SetupDTO.class,
              ClienteDTO.class,
              UsuarioDTO.class,
              DtoMapper.class,
              EntityMapper.class,
              DataMapper.class,
              QueryBuilder.class,
              ConnectionDB.class,
              ConnectionPostGreSQL.class,
              SchemaValidator.class,
              SimpleObjectFactory.class,
              GenericRepository.class,
              UsuarioRepository.class,
              AgendamentoService.class,
              GenericService.class,
              ServiceRegistry.class,
              SettingService.class,
              SetupService.class,
              UsuarioService.class);

      for (Class<?> type : reflectionTypes) {
        hints.reflection().registerType(type, allMembers);
      }

      hints.reflection().registerType(TypeReference.of("org.mariadb.jdbc.Driver"), allMembers);
      hints.reflection().registerType(TypeReference.of("org.postgresql.Driver"), allMembers);

      // Registra todas as classes de com.gwj, nz.net.ultraq (Layout Dialect + Groovy Extensions)
      // e org.thymeleaf (TemplateMode.HTML, engine, model, standard, context, etc.) para
      // que o MetaClassImpl do Groovy acesse propriedades, constantes e métodos via reflexão
      registerPackageClasses(
          hints,
          classLoader,
          allMembers,
          "classpath*:com/gwj/**/*.class",
          "classpath*:nz/net/ultraq/**/*.class",
          "classpath*:org/thymeleaf/**/*.class");

      // Proxies dinâmicos JDK usados pelo Spring ao injetar HttpSession / HttpServletRequest via @Autowired
      hints.proxies().registerJdkProxy(HttpSession.class);
      hints.proxies().registerJdkProxy(HttpServletRequest.class);
      hints.proxies().registerJdkProxy(HttpServletResponse.class);
    }

    private void registerPackageClasses(
        RuntimeHints hints,
        ClassLoader classLoader,
        MemberCategory[] categories,
        String... patterns) {
      PathMatchingResourcePatternResolver resolver =
          new PathMatchingResourcePatternResolver(classLoader);
      CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(resolver);
      for (String pattern : patterns) {
        try {
          for (Resource resource : resolver.getResources(pattern)) {
            if (resource.isReadable()) {
              MetadataReader reader = readerFactory.getMetadataReader(resource);
              String className = reader.getClassMetadata().getClassName();
              if (!className.endsWith("module-info") && !className.endsWith("package-info")) {
                hints.reflection().registerType(TypeReference.of(className), categories);
              }
            }
          }
        } catch (Exception e) {
          System.err.println(
              "Aviso ao registrar hints AOT para o padrão " + pattern + ": " + e.getMessage());
        }
      }
    }
  }
}
