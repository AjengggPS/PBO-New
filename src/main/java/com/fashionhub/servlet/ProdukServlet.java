/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fashionhub.servlet;

import com.fashionhub.util.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ProdukServlet")
public class ProdukServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("delete".equals(action)) {
            deleteProduk(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("add".equals(action)) {
            addProduk(request, response);
        } else if ("edit".equals(action)) {
            editProduk(request, response);
        }
    }
    
    // Method Tambah Produk
    private void addProduk(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String kodeProduk = request.getParameter("kodeProduk");
        String namaProduk = request.getParameter("namaProduk");
        String kategori = request.getParameter("kategori");
        String ukuran = request.getParameter("ukuran");
        String warna = request.getParameter("warna");
        String hargaStr = request.getParameter("harga");
        String stokStr = request.getParameter("stok");
        
        // Validasi input
        if (kodeProduk == null || namaProduk == null || kategori == null || 
            hargaStr == null || stokStr == null) {
            response.sendRedirect("produk/tambah-produk.jsp?error=Data tidak lengkap!");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            double harga = Double.parseDouble(hargaStr);
            int stok = Integer.parseInt(stokStr);
            
            conn = DBConnection.getConnection();
            
            // Cek apakah kode produk sudah ada
            pstmt = conn.prepareStatement("SELECT id FROM produk WHERE kode_produk = ?");
            pstmt.setString(1, kodeProduk);
            rs = pstmt.getResultSet();
            
            if (rs != null && rs.next()) {
                response.sendRedirect("produk/tambah-produk.jsp?error=Kode produk sudah digunakan!");
                return;
            }
            
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            
            // Insert produk baru
            String sql = "INSERT INTO produk (kode_produk, nama_produk, kategori, ukuran, warna, harga, stok) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, kodeProduk.toUpperCase());
            pstmt.setString(2, namaProduk);
            pstmt.setString(3, kategori);
            pstmt.setString(4, ukuran != null && !ukuran.isEmpty() ? ukuran : null);
            pstmt.setString(5, warna != null && !warna.isEmpty() ? warna : null);
            pstmt.setDouble(6, harga);
            pstmt.setInt(7, stok);
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                response.sendRedirect("produk/list-produk.jsp?message=Produk berhasil ditambahkan!");
            } else {
                response.sendRedirect("produk/tambah-produk.jsp?error=Gagal menambahkan produk!");
            }
            
        } catch (NumberFormatException e) {
            response.sendRedirect("produk/tambah-produk.jsp?error=Format harga atau stok tidak valid!");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("produk/tambah-produk.jsp?error=Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // Method Edit Produk
    private void editProduk(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String idStr = request.getParameter("id");
        String namaProduk = request.getParameter("namaProduk");
        String kategori = request.getParameter("kategori");
        String ukuran = request.getParameter("ukuran");
        String warna = request.getParameter("warna");
        String hargaStr = request.getParameter("harga");
        String stokStr = request.getParameter("stok");
        
        // Validasi
        if (idStr == null || namaProduk == null || kategori == null || 
            hargaStr == null || stokStr == null) {
            response.sendRedirect("produk/list-produk.jsp?error=Data tidak lengkap!");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            int id = Integer.parseInt(idStr);
            double harga = Double.parseDouble(hargaStr);
            int stok = Integer.parseInt(stokStr);
            
            conn = DBConnection.getConnection();
            
            // Update produk
            String sql = "UPDATE produk SET nama_produk = ?, kategori = ?, ukuran = ?, " +
                        "warna = ?, harga = ?, stok = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, namaProduk);
            pstmt.setString(2, kategori);
            pstmt.setString(3, ukuran != null && !ukuran.isEmpty() ? ukuran : null);
            pstmt.setString(4, warna != null && !warna.isEmpty() ? warna : null);
            pstmt.setDouble(5, harga);
            pstmt.setInt(6, stok);
            pstmt.setInt(7, id);
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                response.sendRedirect("produk/list-produk.jsp?message=Produk berhasil diupdate!");
            } else {
                response.sendRedirect("produk/edit-produk.jsp?id=" + id + "&error=Gagal update produk!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("produk/list-produk.jsp?error=Error: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    // Method Hapus Produk
    private void deleteProduk(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        
        String idStr = request.getParameter("id");
        
        if (idStr == null) {
            response.sendRedirect("produk/list-produk.jsp?error=ID produk tidak valid!");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            int id = Integer.parseInt(idStr);
            
            conn = DBConnection.getConnection();
            
            // Hapus produk
            pstmt = conn.prepareStatement("DELETE FROM produk WHERE id = ?");
            pstmt.setInt(1, id);
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                response.sendRedirect("produk/list-produk.jsp?message=Produk berhasil dihapus!");
            } else {
                response.sendRedirect("produk/list-produk.jsp?error=Produk tidak ditemukan!");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("produk/list-produk.jsp?error=Error: " + e.getMessage());
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