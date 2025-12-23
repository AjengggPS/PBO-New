/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fashionhub.util;

/**
 *
 * @author Ajeng Puspita Sari
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // Konfigurasi Database
    private static final String URL = "jdbc:mysql://localhost:3306/fashionhub";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    
    // Method untuk membuat koneksi
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Return koneksi
            return DriverManager.getConnection(URL, USER, PASSWORD);
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver tidak ditemukan: " + e.getMessage());
        }
    }
    
    // Method untuk tutup koneksi
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}