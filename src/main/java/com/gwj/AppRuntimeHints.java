package com.gwj;

import com.gwj.model.domain.Carrinho;
import com.gwj.model.domain.CarrinhoItem;
import com.gwj.model.domain.IEntity;
import com.gwj.model.domain.PerfilTipo;
import com.gwj.model.domain.entities.Produto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.util.ClassUtils;

/**
 * Registra metadados AOT (reflexão, recursos e serialização) para compilação GraalVM Native Image.
 *
 * <p>O projeto utiliza reflexão dinâmica para persistência ({@code GenericRepository}, {@code
 * DataMapper}), instanciação de entidades ({@code SimpleObjectFactory}), binding de formulários
 * ({@code EntityMapper}) e templates Thymeleaf. Este registrador descobre automaticamente todas as
 * entidades e DTOs durante o processamento AOT no build.
 */
public class AppRuntimeHints implements RuntimeHintsRegistrar {

  private static final MemberCategory[] FULL_REFLECTION_CATEGORIES = {
    MemberCategory.PUBLIC_FIELDS,
    MemberCategory.DECLARED_FIELDS,
    MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
    MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
    MemberCategory.INVOKE_PUBLIC_METHODS,
    MemberCategory.INVOKE_DECLARED_METHODS
  };

  private static final List<String> SCAN_PACKAGES =
      List.of("com.gwj.model.domain.entities", "com.gwj.model.dataTransferObject");

  private static final List<Class<?>> EXPLICIT_DOMAIN_TYPES =
      List.of(IEntity.class, PerfilTipo.class, Carrinho.class, CarrinhoItem.class, AppConfig.class);

  private static final List<String> JDBC_DRIVER_CLASSES =
      List.of("org.mariadb.jdbc.Driver", "org.postgresql.Driver", "com.mysql.cj.jdbc.Driver");

  private static final List<Class<?>> SPEL_JDK_TYPES =
      List.of(
          Object.class,
          String.class,
          CharSequence.class,
          Boolean.class,
          Number.class,
          Integer.class,
          Long.class,
          Double.class,
          Float.class,
          BigDecimal.class,
          Iterable.class,
          Collection.class,
          List.class,
          ArrayList.class,
          LinkedList.class,
          Set.class,
          HashSet.class,
          LinkedHashSet.class,
          Map.class,
          HashMap.class,
          LinkedHashMap.class,
          Map.Entry.class,
          Collections.class,
          LocalDate.class,
          LocalTime.class,
          LocalDateTime.class,
          Temporal.class,
          TemporalAccessor.class,
          Date.class,
          Calendar.class);

  private static final List<String> THYMELEAF_EXPRESSION_TYPES =
      List.of(
          "org.thymeleaf.expression.Aggregates",
          "org.thymeleaf.expression.Arrays",
          "org.thymeleaf.expression.Bools",
          "org.thymeleaf.expression.Calendars",
          "org.thymeleaf.expression.Conversions",
          "org.thymeleaf.expression.Dates",
          "org.thymeleaf.expression.ExecutionInfo",
          "org.thymeleaf.expression.Ids",
          "org.thymeleaf.expression.Lists",
          "org.thymeleaf.expression.Maps",
          "org.thymeleaf.expression.Messages",
          "org.thymeleaf.expression.Numbers",
          "org.thymeleaf.expression.Objects",
          "org.thymeleaf.expression.Sets",
          "org.thymeleaf.expression.Strings",
          "org.thymeleaf.expression.Temporals",
          "org.thymeleaf.expression.Uris");

  @Override
  public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    ClassLoader effectiveLoader =
        classLoader != null ? classLoader : AppRuntimeHints.class.getClassLoader();

    registerScannedPackages(hints, effectiveLoader);
    registerExplicitTypes(hints);
    registerSpelAndThymeleafTypes(hints, effectiveLoader);
    registerJdbcDrivers(hints, effectiveLoader);
    registerResources(hints);
    registerSerialization(hints);
    registerProxies(hints);
  }

  private void registerScannedPackages(RuntimeHints hints, ClassLoader classLoader) {
    ClassPathScanningCandidateComponentProvider scanner =
        new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> true);

    for (String basePackage : SCAN_PACKAGES) {
      for (BeanDefinition candidate : scanner.findCandidateComponents(basePackage)) {
        String className = candidate.getBeanClassName();
        if (className == null) {
          continue;
        }
        try {
          Class<?> clazz = ClassUtils.forName(className, classLoader);
          hints.reflection().registerType(clazz, FULL_REFLECTION_CATEGORIES);
        } catch (ClassNotFoundException | LinkageError ignored) {
          // Ignora tipos opcionais que não possam ser carregados no contexto atual
        }
      }
    }
  }

  private void registerExplicitTypes(RuntimeHints hints) {
    for (Class<?> type : EXPLICIT_DOMAIN_TYPES) {
      hints.reflection().registerType(type, FULL_REFLECTION_CATEGORIES);
    }
  }

  private void registerSpelAndThymeleafTypes(RuntimeHints hints, ClassLoader classLoader) {
    for (Class<?> type : SPEL_JDK_TYPES) {
      hints
          .reflection()
          .registerType(
              type,
              MemberCategory.INVOKE_PUBLIC_METHODS,
              MemberCategory.INVOKE_DECLARED_METHODS,
              MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
    }
    for (String typeName : THYMELEAF_EXPRESSION_TYPES) {
      hints
          .reflection()
          .registerTypeIfPresent(
              classLoader,
              typeName,
              MemberCategory.INVOKE_PUBLIC_METHODS,
              MemberCategory.INVOKE_DECLARED_METHODS,
              MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
    }
  }

  private void registerJdbcDrivers(RuntimeHints hints, ClassLoader classLoader) {
    for (String driverClass : JDBC_DRIVER_CLASSES) {
      hints
          .reflection()
          .registerTypeIfPresent(
              classLoader,
              driverClass,
              MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS,
              MemberCategory.INVOKE_DECLARED_CONSTRUCTORS,
              MemberCategory.INVOKE_PUBLIC_METHODS);
    }
  }

  private void registerResources(RuntimeHints hints) {
    hints.resources().registerPattern("db/*.sql");
    hints.resources().registerPattern("application.properties");
    hints.resources().registerPattern("META-INF/services/java.sql.Driver");
  }

  private void registerSerialization(RuntimeHints hints) {
    hints.serialization().registerType(TypeReference.of(Carrinho.class));
    hints.serialization().registerType(TypeReference.of(CarrinhoItem.class));
    hints.serialization().registerType(TypeReference.of(Produto.class));
  }

  private void registerProxies(RuntimeHints hints) {
    hints.proxies().registerJdkProxy(HttpSession.class);
    hints.proxies().registerJdkProxy(HttpServletRequest.class);
    hints.reflection().registerType(HttpSession.class, MemberCategory.INVOKE_PUBLIC_METHODS);
    hints.reflection().registerType(HttpServletRequest.class, MemberCategory.INVOKE_PUBLIC_METHODS);
  }
}
