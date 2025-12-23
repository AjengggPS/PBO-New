/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fashionhub.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/StartShiftServlet")
public class StartShiftServlet extends HttpServlet {
    
    // Konfigurasi Database
    private static final String DB_URL = "jdbc:mysql://localhost:3306/fashionhub";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Ambil data dari form
        String namaKasir = request.getParameter("namaKasir");
        String shift = request.getParameter("shift");
        
        // Validasi input
        if (namaKasir == null || namaKasir.trim().isEmpty() || 
            shift == null || shift.trim().isEmpty()) {
            response.sendRedirect("index.jsp?error=Semua field harus diisi!");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            // Load driver MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Koneksi ke database
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            
            // Insert data kasir ke database
            String sql = "INSERT INTO kasir (nama_kasir, shift, waktu_mulai, status) VALUES (?, ?, NOW(), 'aktif')";
            pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, namaKasir.trim());
            pstmt.setString(2, shift);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Ambil ID kasir yang baru dibuat
                rs = pstmt.getGeneratedKeys();
                int kasirId = 0;
                if (rs.next()) {
                    kasirId = rs.getInt(1);
                }
                
                // Simpan data kasir ke session
                HttpSession session = request.getSession();
                session.setAttribute("kasirId", kasirId);
                session.setAttribute("namaKasir", namaKasir.trim());
                session.setAttribute("shift", shift);
                
                // Redirect ke dashboard
                response.sendRedirect("dashboard.jsp");
            } else {
                response.sendRedirect("index.jsp?error=Gagal memulai shift!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index.jsp?error=Error: " + e.getMessage());
        } finally {
            // Tutup koneksi
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}