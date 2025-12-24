/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fashionhub.service;

/**
 *
 * @author Ajeng Puspita Sari
 */
import com.fashionhub.model.Pelanggan;
import com.fashionhub.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Layer untuk Pelanggan
 * Menangani business logic dan database operations
 */
public class PelangganService {
    
    /**
     * Get semua pelanggan
     */
    public List<Pelanggan> getAllPelanggan() throws SQLException {
        List<Pelanggan> listPelanggan = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM pelanggan ORDER BY created_at DESC");
            
            while (rs.next()) {
                Pelanggan pelanggan = mapResultSetToPelanggan(rs);
                listPelanggan.add(pelanggan);
            }
            
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return listPelanggan;
    }
    
    /**
     * Get pelanggan by ID
     */
    public Pelanggan getPelangganById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM pelanggan WHERE id = ?");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPelanggan(rs);
            }
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return null;
    }
    
    /**
     * Tambah pelanggan baru (OOP: terima object Pelanggan)
     */
    public boolean tambahPelanggan(Pelanggan pelanggan) throws SQLException {
        // Validasi menggunakan method di class Pelanggan
        if (!pelanggan.validate()) {
            throw new IllegalArgumentException("Data pelanggan tidak valid");
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "INSERT INTO pelanggan (nama_pelanggan, no_hp, alamat) VALUES (?, ?, ?)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pelanggan.getNamaPelanggan());
            pstmt.setString(2, pelanggan.getNoHp());
            pstmt.setString(3, pelanggan.getAlamat());
            
            return pstmt.executeUpdate() > 0;
            
        } finally {
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    /**
     * Update pelanggan
     */
    public boolean updatePelanggan(Pelanggan pelanggan) throws SQLException {
        if (!pelanggan.validate()) {
            throw new IllegalArgumentException("Data pelanggan tidak valid");
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "UPDATE pelanggan SET nama_pelanggan = ?, no_hp = ?, alamat = ? WHERE id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, pelanggan.getNamaPelanggan());
            pstmt.setString(2, pelanggan.getNoHp());
            pstmt.setString(3, pelanggan.getAlamat());
            pstmt.setInt(4, pelanggan.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } finally {
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    /**
     * Hapus pelanggan
     */
    public boolean deletePelanggan(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM pelanggan WHERE id = ?");
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate() > 0;
            
        } finally {
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    /**
     * Cari pelanggan by nama
     */
    public List<Pelanggan> cariPelangganByNama(String nama) throws SQLException {
        List<Pelanggan> listPelanggan = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(
                "SELECT * FROM pelanggan WHERE nama_pelanggan LIKE ? ORDER BY nama_pelanggan"
            );
            pstmt.setString(1, "%" + nama + "%");
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                listPelanggan.add(mapResultSetToPelanggan(rs));
            }
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return listPelanggan;
    }
    
    /**
     * MAPPING: ResultSet → Pelanggan
     */
    private Pelanggan mapResultSetToPelanggan(ResultSet rs) throws SQLException {
        Pelanggan pelanggan = new Pelanggan();
        pelanggan.setId(rs.getInt("id"));
        pelanggan.setNamaPelanggan(rs.getString("nama_pelanggan"));
        pelanggan.setNoHp(rs.getString("no_hp"));
        pelanggan.setAlamat(rs.getString("alamat"));
        pelanggan.setCreatedAt(rs.getTimestamp("created_at"));
        pelanggan.setUpdatedAt(rs.getTimestamp("updated_at"));
        return pelanggan;
    }
}