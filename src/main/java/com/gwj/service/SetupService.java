package com.gwj.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Service;

import com.gwj.AppConfig;
import com.gwj.controller.PasswordUtil;
import com.gwj.model.dataTransferObject.SetupDTO;
import com.gwj.model.domain.factory.SchemaValidator;

@Service
public class SetupService {

    private volatile boolean configured = false;

    /**
     * Verifica se a aplicação já passou pelo setup inicial.
     * O sistema é considerado configurado se o arquivo .env existir, a conexão
     * com o banco funcionar e houver ao menos um usuário administrador cadastrado.
     */
    public boolean isConfigured() {
        if (configured) {
            return true;
        }

        File envFile = new File(".env");
        if (!envFile.exists()) {
            envFile = new File(System.getProperty("user.dir"), ".env");
        }
        if (!envFile.exists() || !envFile.isFile()) {
            return false;
        }

        // Tenta conectar ao banco configurado no AppConfig
        try {
            DriverManager.setLoginTimeout(2);
            try (Connection conn = DriverManager.getConnection(AppConfig.DB_URL, AppConfig.DB_USER, AppConfig.DB_PASS);
                 Statement stmt = conn.createStatement()) {

                // Checa se a tabela tab_usuario existe e possui um administrador cadastrado
                try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM tab_usuario WHERE perfil_id = 1")) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        this.configured = true;
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // Se o banco não responder ou a tabela não existir, setup necessário
            return false;
        }

        return false;
    }

    /**
     * Testa a conexão com os parâmetros fornecidos para MariaDB/MySQL.
     */
    public void testConnection(String host, int port, String dbName, String user, String password) throws SQLException {
        testConnection("mariadb", host, port, dbName, user, password);
    }

    /**
     * Testa a conexão suportando tanto MariaDB/MySQL quanto PostgreSQL.
     */
    public void testConnection(String dbType, String host, int port, String dbName, String user, String password) throws SQLException {
        boolean isPg = dbType != null && (dbType.equalsIgnoreCase("postgres") || dbType.equalsIgnoreCase("postgresql"));
        DriverManager.setLoginTimeout(3);

        if (isPg) {
            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ignored) {}

            String urlWithDb = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
            try (Connection conn = DriverManager.getConnection(urlWithDb, user, password)) {
                if (conn.isValid(3)) {
                    return;
                }
            } catch (SQLException e) {
                // Se o erro for de banco inexistente (SQLState 3D000 no Postgres), testa o banco raiz 'postgres'
                if ("3D000".equals(e.getSQLState()) || (e.getMessage() != null && e.getMessage().toLowerCase().contains("does not exist"))) {
                    String rootUrl = "jdbc:postgresql://" + host + ":" + port + "/postgres";
                    try (Connection rootConn = DriverManager.getConnection(rootUrl, user, password)) {
                        if (rootConn.isValid(3)) {
                            return; // Servidor acessível, o banco será criado automaticamente no finish
                        }
                    }
                }
                throw e;
            }
        } else {
            String urlWithDb = "jdbc:mariadb://" + host + ":" + port + "/" + dbName;
            try {
                try (Connection conn = DriverManager.getConnection(urlWithDb, user, password)) {
                    if (conn.isValid(3)) {
                        return;
                    }
                }
            } catch (SQLException e) {
                // Se o erro for de banco desconhecido (ER_BAD_DB_ERROR / 1049), testa a conexão raiz do servidor
                if (e.getErrorCode() == 1049 || "42000".equals(e.getSQLState())) {
                    String rootUrl = "jdbc:mariadb://" + host + ":" + port + "/";
                    try (Connection rootConn = DriverManager.getConnection(rootUrl, user, password)) {
                        if (rootConn.isValid(3)) {
                            return; // Servidor acessível, o banco será criado automaticamente no finish
                        }
                    }
                }
                throw e;
            }
        }
    }

    /**
     * Executa o processo completo de finalização do Setup Wizard:
     * 1. Cria o banco se não existir
     * 2. Gera o arquivo .env físico
     * 3. Recarrega o AppConfig
     * 4. Executa a criação das tabelas estruturais
     * 5. Cadastra o usuário administrador master
     * 6. Ativa a trava de segurança (configured = true)
     */
    public synchronized void completeSetup(SetupDTO dto) throws Exception {
        if (dto.getAdminPassword() == null || dto.getAdminPassword().length() < 4) {
            throw new IllegalArgumentException("A senha do administrador deve ter pelo menos 4 caracteres.");
        }
        if (!dto.getAdminPassword().equals(dto.getAdminPasswordConfirm())) {
            throw new IllegalArgumentException("A confirmação de senha não confere.");
        }
        if (dto.getAdminEmail() == null || !dto.getAdminEmail().contains("@")) {
            throw new IllegalArgumentException("Informe um e-mail válido para o administrador.");
        }

        boolean isPg = dto.getDbType() != null && (dto.getDbType().equalsIgnoreCase("postgres") || dto.getDbType().equalsIgnoreCase("postgresql"));
        String host = dto.getDbHost() != null && !dto.getDbHost().isBlank() ? dto.getDbHost().trim() : "localhost";
        int port = dto.getDbPort() != null ? dto.getDbPort() : (isPg ? 5432 : 3306);
        String dbName = dto.getDbName() != null && !dto.getDbName().isBlank() ? dto.getDbName().trim() : "gwj2";
        String user = dto.getDbUser() != null && !dto.getDbUser().isBlank() ? dto.getDbUser().trim() : (isPg ? "postgres" : "desenvolvedor");
        String password = dto.getDbPassword() != null ? dto.getDbPassword() : "";
        int serverPort = dto.getServerPort() != null ? dto.getServerPort() : 8089;
        String tablePrefix = dto.getTablePrefix() != null && !dto.getTablePrefix().isBlank() ? dto.getTablePrefix().trim() : "tab_";

        DriverManager.setLoginTimeout(5);

        String jdbcUrl;
        String driverClass;

        if (isPg) {
            driverClass = "org.postgresql.Driver";
            jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;

            // 1. Conecta ao banco de manutenção 'postgres' e cria o banco se necessário
            String rootUrl = "jdbc:postgresql://" + host + ":" + port + "/postgres";
            try (Connection rootConn = DriverManager.getConnection(rootUrl, user, password)) {
                boolean dbExists = false;
                try (PreparedStatement checkStmt = rootConn.prepareStatement("SELECT 1 FROM pg_database WHERE datname = ?")) {
                    checkStmt.setString(1, dbName);
                    try (ResultSet rs = checkStmt.executeQuery()) {
                        if (rs.next()) dbExists = true;
                    }
                }
                if (!dbExists) {
                    try (Statement stmt = rootConn.createStatement()) {
                        stmt.execute("CREATE DATABASE \"" + dbName + "\";");
                    }
                }
            }
        } else {
            driverClass = "org.mariadb.jdbc.Driver";
            jdbcUrl = "jdbc:mariadb://" + host + ":" + port + "/" + dbName;

            // 1. Conecta ao servidor e cria o banco se necessário
            String serverUrl = "jdbc:mariadb://" + host + ":" + port + "/";
            try (Connection rootConn = DriverManager.getConnection(serverUrl, user, password);
                 Statement stmt = rootConn.createStatement()) {
                stmt.execute("CREATE DATABASE IF NOT EXISTS `" + dbName + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;");
            }
        }

        // 2. Grava o arquivo .env
        writeEnvFile(serverPort, jdbcUrl, user, password, driverClass, tablePrefix);

        // 3. Recarrega as configurações em tempo de execução
        AppConfig.reload();

        // 4. Executa migrações / criação de tabelas
        try (Connection dbConn = DriverManager.getConnection(jdbcUrl, user, password)) {
            runSchemaScripts(dbConn, isPg);
        }

        // 5. Assegura tabelas e índices através do SchemaValidator
        try {
            SchemaValidator.ensureUniqueEmailIndex();
            SchemaValidator.ensureSettingsTableExists();
            SchemaValidator.ensureProductImageColumnAndDataExist();
            SchemaValidator.ensureOrderTablesExist();
        } catch (Exception e) {
            System.err.println("Aviso no SchemaValidator durante o Setup: " + e.getMessage());
        }

        // 6. Insere ou atualiza o Administrador Master
        try (Connection dbConn = DriverManager.getConnection(jdbcUrl, user, password)) {
            createAdminUser(dbConn, dto.getAdminName(), dto.getAdminEmail(), dto.getAdminPassword(), isPg);
        }

        // 7. Trava de segurança
        this.configured = true;
        System.out.println("✅ Setup Wizard concluído com sucesso para o banco '" + dbName + "' (" + (isPg ? "PostgreSQL" : "MariaDB/MySQL") + ").");
    }

    private void writeEnvFile(int serverPort, String dbUrl, String dbUser, String dbPassword, String driverClass, String prefix) throws Exception {
        File envFile = new File(".env");
        StringBuilder sb = new StringBuilder();
        sb.append("# ===================================================================\n");
        sb.append("# Arquivo de Credenciais e Variáveis de Ambiente Local (.env)\n");
        sb.append("# Gerado automaticamente pelo Assistente de Instalação (Setup Wizard)\n");
        sb.append("# ===================================================================\n\n");
        sb.append("SERVER_PORT=").append(serverPort).append("\n\n");
        sb.append("DB_URL=").append(dbUrl).append("\n");
        sb.append("DB_USERNAME=").append(dbUser).append("\n");
        sb.append("DB_PASSWORD=").append(dbPassword).append("\n");
        sb.append("DB_DRIVER=").append(driverClass).append("\n\n");
        sb.append("APP_DB_PREFIX=").append(prefix).append("\n");
        sb.append("APP_ENTITY_PACKAGE=com.gwj.model.domain\n");
        sb.append("APP_ENTITIES_PATH=com.gwj.model.domain.entities\n");

        try (FileWriter writer = new FileWriter(envFile, StandardCharsets.UTF_8)) {
            writer.write(sb.toString());
        }
    }

    private void runSchemaScripts(Connection conn, boolean isPg) {
        String scriptName = isPg ? "/db/gwj5_postgres.sql" : "/db/gwj5.sql";
        InputStream is = getClass().getResourceAsStream(scriptName);
        if (is == null) {
            File rootSql = new File(isPg ? "gwj5_postgres.sql" : "gwj5.sql");
            if (rootSql.exists()) {
                try {
                    is = new java.io.FileInputStream(rootSql);
                } catch (Exception ignored) {}
            }
        }

        if (is == null) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
             Statement stmt = conn.createStatement()) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("--") || line.startsWith("/*") || line.startsWith("#") || line.isEmpty()) {
                    continue;
                }
                sb.append(line).append(" ");
                if (line.endsWith(";")) {
                    String sql = sb.toString().trim();
                    sql = sql.substring(0, sql.length() - 1).trim();
                    if (!sql.isEmpty() && !sql.equalsIgnoreCase("START TRANSACTION") && !sql.equalsIgnoreCase("COMMIT")) {
                        try {
                            stmt.execute(sql);
                        } catch (SQLException e) {
                            // Ignora erros de tabelas ou registros que já existam
                        }
                    }
                    sb.setLength(0);
                }
            }
        } catch (Exception e) {
            System.err.println("Aviso ao executar scripts de banco: " + e.getMessage());
        }
    }

    private void createAdminUser(Connection conn, String name, String email, String plainPassword, boolean isPg) throws SQLException {
        String hashedPassword = "{sha256}" + PasswordUtil.hash(plainPassword);

        // Garante que o perfil 1 (Administrador) existe
        try (Statement stmt = conn.createStatement()) {
            if (isPg) {
                stmt.execute("INSERT INTO tab_perfil (id, nome) VALUES (1, 'Administrador') ON CONFLICT (id) DO NOTHING");
            } else {
                stmt.execute("INSERT IGNORE INTO `tab_perfil` (`id`, `nome`) VALUES (1, 'Administrador')");
            }
        } catch (Exception ignored) {}

        // Cria ou atualiza o usuário administrador
        String sql;
        if (isPg) {
            sql = "INSERT INTO tab_usuario (perfil_id, nome_usuario, email, senha, status, data_cadastro) " +
                  "VALUES (1, ?, ?, ?, TRUE, NOW()) " +
                  "ON CONFLICT (email) DO UPDATE SET senha = EXCLUDED.senha, perfil_id = 1, nome_usuario = EXCLUDED.nome_usuario";
        } else {
            sql = "INSERT INTO `tab_usuario` (`perfil_id`, `nome_usuario`, `email`, `senha`, `status`, `data_cadastro`) " +
                  "VALUES (1, ?, ?, ?, 1, NOW()) " +
                  "ON DUPLICATE KEY UPDATE `senha` = VALUES(`senha`), `perfil_id` = 1, `nome_usuario` = VALUES(`nome_usuario`)";
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPassword);
            pstmt.executeUpdate();
        }
    }
}
