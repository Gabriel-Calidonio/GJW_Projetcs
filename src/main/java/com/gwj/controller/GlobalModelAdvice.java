package com.gwj.controller;

import com.gwj.AppConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@Component("appConfig")
public class GlobalModelAdvice {

  /**
   * Fornece o prefixo da rota administrativa para todas as views Thymeleaf como atributo do modelo.
   * Exemplo de uso no HTML: th:href="@{|/${adminPrefix}/clientes|}"
   */
  @ModelAttribute("adminPrefix")
  public String getAdminPrefix() {
    return AppConfig.ADMIN_ROUTE_PREFIX;
  }

  /**
   * Alias de conveniência para templates que utilizem 'adminRoutePrefix'.
   */
  @ModelAttribute("adminRoutePrefix")
  public String getAdminRoutePrefix() {
    return AppConfig.ADMIN_ROUTE_PREFIX;
  }
}

