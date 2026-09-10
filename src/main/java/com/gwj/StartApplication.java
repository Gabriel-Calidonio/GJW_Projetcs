package com.gwj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.gwj.model.domain.factory.SchemaValidator;

@SpringBootApplication
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
			System.out.println("ℹ️ Banco de dados ainda não inicializado no boot. O Setup Wizard estará disponível em /setup.");
		}

		// Este comando inicia o servidor Tomcat embutido e sobe a aplicação
        SpringApplication.run(StartApplication.class, args);
        System.out.println("=== Servidor Spring Boot iniciado com sucesso! ===");
    }
}
