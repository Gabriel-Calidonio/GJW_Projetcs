package com.gwj.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gwj.model.dataTransferObject.SetupDTO;
import com.gwj.service.SetupService;

@Controller
@RequestMapping("/setup")
public class SetupController {

    @Autowired
    private SetupService setupService;

    @GetMapping
    public String index(Model model) {
        if (setupService.isConfigured()) {
            return "redirect:/MRYnZpAsC9sp/login";
        }

        SetupDTO dto = new SetupDTO();
        // Informações do sistema operacional e ambiente para diagnóstico
        Map<String, Object> sysInfo = new HashMap<>();
        sysInfo.put("javaVersion", System.getProperty("java.version"));
        sysInfo.put("osName", System.getProperty("os.name"));
        sysInfo.put("userDir", System.getProperty("user.dir"));
        sysInfo.put("canWrite", new File(".").canWrite());

        model.addAttribute("setup", dto);
        model.addAttribute("sysInfo", sysInfo);

        return "setup/wizard";
    }

    @PostMapping("/test-db")
    @ResponseBody
    public Map<String, Object> testDatabase(@RequestBody SetupDTO dto) {
        Map<String, Object> response = new HashMap<>();
        if (setupService.isConfigured()) {
            response.put("success", false);
            response.put("message", "O sistema já está configurado.");
            return response;
        }

        try {
            String dbType = dto.getDbType() != null ? dto.getDbType().trim() : "mariadb";
            boolean isPg = "postgres".equalsIgnoreCase(dbType) || "postgresql".equalsIgnoreCase(dbType);
            int port = dto.getDbPort() != null ? dto.getDbPort() : (isPg ? 5432 : 3306);
            String host = dto.getDbHost() != null && !dto.getDbHost().isBlank() ? dto.getDbHost().trim() : "localhost";
            String dbName = dto.getDbName() != null && !dto.getDbName().isBlank() ? dto.getDbName().trim() : "gwj2";
            String user = dto.getDbUser() != null && !dto.getDbUser().isBlank() ? dto.getDbUser().trim() : (isPg ? "postgres" : "desenvolvedor");
            String password = dto.getDbPassword() != null ? dto.getDbPassword() : "";

            setupService.testConnection(dbType, host, port, dbName, user, password);
            response.put("success", true);
            response.put("message", "Conexão com o banco de dados realizada com sucesso!");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Erro ao conectar ao banco: " + e.getMessage());
        }

        return response;
    }

    @PostMapping("/finish")
    @ResponseBody
    public Map<String, Object> finishSetup(@RequestBody SetupDTO dto) {
        Map<String, Object> response = new HashMap<>();
        if (setupService.isConfigured()) {
            response.put("success", false);
            response.put("message", "O sistema já foi previamente instalado.");
            return response;
        }

        try {
            setupService.completeSetup(dto);
            response.put("success", true);
            response.put("message", "Assistente concluído com sucesso!");
            response.put("redirectUrl", "/MRYnZpAsC9sp/login");
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }

        return response;
    }
}
