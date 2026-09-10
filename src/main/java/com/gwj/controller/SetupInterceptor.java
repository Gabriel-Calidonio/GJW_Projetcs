package com.gwj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.gwj.service.SetupService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SetupInterceptor implements HandlerInterceptor {

    @Autowired
    private SetupService setupService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();

        // 1. Libera recursos estáticos e rotas técnicas essenciais
        if (uri.startsWith("/css") || uri.startsWith("/js") || uri.startsWith("/img") ||
            uri.startsWith("/favicon.ico") || uri.startsWith("/webjars") || uri.startsWith("/error")) {
            return true;
        }

        boolean configured = setupService.isConfigured();

        // 2. Se o sistema ainda NÃO está configurado:
        if (!configured) {
            if (uri.startsWith("/setup")) {
                return true; // Permite navegar nas rotas do assistente
            }
            // Qualquer outra requisição é redirecionada para o assistente
            response.sendRedirect(request.getContextPath() + "/setup");
            return false;
        }

        // 3. Se o sistema JÁ ESTÁ configurado:
        if (uri.startsWith("/setup")) {
            // Trava de segurança: impede reconfiguração maliciosa
            response.sendRedirect(request.getContextPath() + "/MRYnZpAsC9sp/login");
            return false;
        }

        return true;
    }
}
