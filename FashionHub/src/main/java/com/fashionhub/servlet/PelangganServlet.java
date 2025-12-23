/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fashionhub.servlet;

import com.fashionhub.util.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/PelangganServlet")
public class PelangganServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("delete".equals(action)) {
            deletePelanggan(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addPelanggan(request, response);
        } else if ("edit".equals(action)) {
            editPelanggan(request, response);
        }
    }
    
    // Method Tambah Pelanggan
    private void addPelanggan(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String namaPelanggan = request.getParameter("namaPelanggan");
        String noHp = request.getParameter("noHp");
        String alamat = request.getParameter("alamat");
        
        if (namaPelanggan == null || namaPelanggan.trim().isEmpty()) {
            response.sendRedirect("pelanggan/tambah-pelanggan.jsp?error=Nama pelanggan wajib diisi!");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "INSERT INTO pelanggan (nama_pelanggan, no_hp, alamat) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, namaPelanggan.trim());
            pstmt.setString(2, noHp != null && !noHp.trim().isEmpty() ? noHp.trim() : null);
            pstmt.setString(3, alamat != null && !alamat.trim().isEmpty() ? alamat.trim() : null);
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                response.sendRedirect("pelanggan/list-pelanggan.jsp?message=Pelanggan berhasil ditambahkan!");
            } else {
                response.sendRedirect("pelanggan/tambah-pelanggan.jsp?error=Gagal menambahkan pelanggan!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("pelanggan/tambah-pelanggan.jsp?error=Error: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // Method Edit Pelanggan
    private void editPelanggan(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String idStr = request.getParameter("id");
        String namaPelanggan = request.getParameter("namaPelanggan");
        String noHp = request.getParameter("noHp");
        String alamat = request.getParameter("alamat");
        
        if (idStr == null || namaPelanggan == null || namaPelanggan.trim().isEmpty()) {
            response.sendRedirect("pelanggan/list-pelanggan.jsp?error=Data tidak lengkap!");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            int id = Integer.parseInt(idStr);
            
            conn = DBConnection.getConnection();
            
            String sql = "UPDATE pelanggan SET nama_pelanggan = ?, no_hp = ?, alamat = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, namaPelanggan.trim());
            pstmt.setString(2, noHp != null && !noHp.trim().isEmpty() ? noHp.trim() : null);
            pstmt.setString(3, alamat != null && !alamat.trim().isEmpty() ? alamat.trim() : null);
            pstmt.setInt(4, id);
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                response.sendRedirect("pelanggan/list-pelanggan.jsp?message=Pelanggan berhasil diupdate!");
            } else {
                response.sendRedirect("pelanggan/edit-pelanggan.jsp?id=" + id + "&error=Gagal update pelanggan!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("pelanggan/list-pelanggan.jsp?error=Error: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // Method Hapus Pelanggan
    private void deletePelanggan(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null) {
            response.sendRedirect("pelanggan/list-pelanggan.jsp?error=ID pelanggan tidak valid!");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            int id = Integer.parseInt(idStr);
            
            conn = DBConnection.getConnection();
            
            pstmt = conn.prepareStatement("DELETE FROM pelanggan WHERE id = ?");
            pstmt.setInt(1, id);
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                response.sendRedirect("pelanggan/list-pelanggan.jsp?message=Pelanggan berhasil dihapus!");
            } else {
                response.sendRedirect("pelanggan/list-pelanggan.jsp?error=Pelanggan tidak ditemukan!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("pelanggan/list-pelanggan.jsp?error=Error: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}