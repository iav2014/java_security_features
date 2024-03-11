package com.SQLInjection.SQLInjectionExample;
import java.sql.*;
/*
Problema de Seguridad: Inyección SQL. Este código es vulnerable porque permite que un atacante inyecte
comandos SQL arbitrarios. Por ejemplo, un atacante podría proporcionar un username que contenga
SQL malicioso, como a' OR '1'='1.

Solución: Utilizar consultas preparadas para evitar la inyección SQL.
 */
public class SQLInjectionExample {
  public static void getUser(String userId) throws SQLException {
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/testdb", "root", "password");
    Statement stmt = conn.createStatement();
    String query = "SELECT * FROM users WHERE userId = '" + userId + "'";
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      System.out.println(rs.getString("username"));
    }
    rs.close();
    stmt.close();
    conn.close();
  }
  // solución
  public static void getUserSecure(String userId) throws SQLException {
    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/testdb", "root", "password");
    String query = "SELECT * FROM users WHERE userId = ?";
    PreparedStatement pstmt = conn.prepareStatement(query);
    pstmt.setString(1, userId);
    ResultSet rs = pstmt.executeQuery();
    while (rs.next()) {
      System.out.println(rs.getString("username"));
    }
    rs.close();
    pstmt.close();
    conn.close();
  }

  public static void main(String[] args) throws SQLException {
    getUser("1' OR '1'='1"); // Inyección SQL
  }
}

