package com.gwj.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.gwj.model.dataTransferObject.SetupDTO;

public class SetupServiceTest {

    private SetupService setupService;

    @BeforeEach
    public void setup() {
        setupService = new SetupService();
    }

    @Test
    public void testCompleteSetupValidationPasswordMismatch() {
        SetupDTO dto = new SetupDTO();
        dto.setAdminEmail("admin@teste.com");
        dto.setAdminPassword("12345");
        dto.setAdminPasswordConfirm("54321");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            setupService.completeSetup(dto);
        });

        assertTrue(ex.getMessage().contains("confirmação"));
    }

    @Test
    public void testCompleteSetupValidationShortPassword() {
        SetupDTO dto = new SetupDTO();
        dto.setAdminEmail("admin@teste.com");
        dto.setAdminPassword("12");
        dto.setAdminPasswordConfirm("12");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            setupService.completeSetup(dto);
        });

        assertTrue(ex.getMessage().contains("4 caracteres"));
    }

    @Test
    public void testCompleteSetupValidationInvalidEmail() {
        SetupDTO dto = new SetupDTO();
        dto.setAdminEmail("invalid-email");
        dto.setAdminPassword("12345");
        dto.setAdminPasswordConfirm("12345");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            setupService.completeSetup(dto);
        });

        assertTrue(ex.getMessage().contains("e-mail válido"));
    }

    @Test
    public void testSetupDTOPostgresConfiguration() {
        SetupDTO dto = new SetupDTO();
        dto.setDbType("postgres");
        dto.setDbPort(5432);
        dto.setDbUser("postgres");
        dto.setDbName("gwj2");

        assertEquals("postgres", dto.getDbType());
        assertEquals(5432, dto.getDbPort());
        assertEquals("postgres", dto.getDbUser());
        assertEquals("gwj2", dto.getDbName());
    }

    @Test
    public void testAppConfigPostgreSqlDetection() {
        com.gwj.AppConfig.DB_URL = "jdbc:postgresql://localhost:5432/gwj2";
        assertTrue(com.gwj.AppConfig.isPostgreSql());

        com.gwj.AppConfig.DB_URL = "jdbc:mariadb://localhost:3306/gwj2";
        com.gwj.AppConfig.DB_DRIVER = "org.mariadb.jdbc.Driver";
        assertFalse(com.gwj.AppConfig.isPostgreSql());
    }
}
