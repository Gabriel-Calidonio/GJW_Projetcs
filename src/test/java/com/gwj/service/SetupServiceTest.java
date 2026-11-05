package com.gwj.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.gwj.model.dataTransferObject.SetupDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
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

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
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

    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> {
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

  @Test
  public void testCreateAdminUserInsertsNewUser() throws Exception {
    Connection conn = mock(Connection.class);
    Statement stmt = mock(Statement.class);
    PreparedStatement delStmt = mock(PreparedStatement.class);
    PreparedStatement checkStmt = mock(PreparedStatement.class);
    PreparedStatement insertStmt = mock(PreparedStatement.class);
    ResultSet checkRs = mock(ResultSet.class);

    when(conn.createStatement()).thenReturn(stmt);
    when(conn.prepareStatement(contains("DELETE"))).thenReturn(delStmt);
    when(conn.prepareStatement(contains("SELECT id"))).thenReturn(checkStmt);
    when(checkStmt.executeQuery()).thenReturn(checkRs);
    when(checkRs.next()).thenReturn(false);
    when(conn.prepareStatement(contains("INSERT INTO `tab_usuario`"))).thenReturn(insertStmt);

    setupService.createAdminUser(conn, "Gabriel", "gabriel@tgosbarbearia.com", "senha123", false);

    verify(insertStmt).setString(1, "Gabriel");
    verify(insertStmt).setString(2, "gabriel@tgosbarbearia.com");
    verify(insertStmt).executeUpdate();
  }

  @Test
  public void testCreateAdminUserUpdatesEmailWhenUserExistsByName() throws Exception {
    Connection conn = mock(Connection.class);
    Statement stmt = mock(Statement.class);
    PreparedStatement delStmt = mock(PreparedStatement.class);
    PreparedStatement checkStmt = mock(PreparedStatement.class);
    PreparedStatement updateStmt = mock(PreparedStatement.class);
    ResultSet checkRs = mock(ResultSet.class);

    when(conn.createStatement()).thenReturn(stmt);
    when(conn.prepareStatement(contains("DELETE"))).thenReturn(delStmt);
    when(conn.prepareStatement(contains("SELECT id"))).thenReturn(checkStmt);
    when(checkStmt.executeQuery()).thenReturn(checkRs);
    when(checkRs.next()).thenReturn(true, false);
    when(checkRs.getLong("id")).thenReturn(7L);
    when(checkRs.getString("email")).thenReturn("gabriel@admin.com");
    when(checkRs.getString("nome_usuario")).thenReturn("Gabriel");
    when(conn.prepareStatement(contains("UPDATE `tab_usuario`"))).thenReturn(updateStmt);

    setupService.createAdminUser(conn, "Gabriel", "gabriel@tgosbarbearia.com", "senha123", false);

    verify(updateStmt).setString(1, "Gabriel");
    verify(updateStmt).setString(2, "gabriel@tgosbarbearia.com");
    verify(updateStmt).setLong(4, 7L);
    verify(updateStmt).executeUpdate();
  }
}
