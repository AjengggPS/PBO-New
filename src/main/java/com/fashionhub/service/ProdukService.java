/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fashionhub.service;

/**
 *
 * @author Ajeng Puspita Sari
 */

import com.fashionhub.model.*;
import com.fashionhub.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Layer untuk Produk
 * Menangani business logic dan database operations untuk Pakaian
 */
public class ProdukService {
    
    /**
     * Get semua produk (return sebagai Pakaian - polymorphism!)
     */
    public List<Pakaian> getAllProduk() throws SQLException {
        List<Pakaian> listProduk = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM produk ORDER BY created_at DESC");
            
            while (rs.next()) {
                Pakaian produk = mapResultSetToPakaian(rs);
                if (produk != null) {
                    listProduk.add(produk);
                }
            }
            
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return listProduk;
    }
    
    /**
     * Get produk by ID (return Pakaian - bisa Baju/Celana/Aksesoris)
     */
    public Pakaian getProdukById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM produk WHERE id = ?");
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPakaian(rs);
            }
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return null;
    }
    
    /**
     * Tambah produk baru (OOP: terima object Pakaian)
     */
    public boolean tambahProduk(Pakaian produk) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "INSERT INTO produk (kode_produk, nama_produk, kategori, ukuran, " +
                        "warna, bahan, jenis, ukuran_pinggang, tipe, brand, harga, stok) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, produk.getKodeProduk().toUpperCase());
            pstmt.setString(2, produk.getNamaProduk());
            pstmt.setString(3, produk.getKategori());
            pstmt.setString(4, produk.getUkuran());
            
            // Set atribut spesifik berdasarkan kategori (Polymorphism in action!)
            if (produk instanceof Baju) {
                Baju baju = (Baju) produk;
                pstmt.setString(5, baju.getWarna());
                pstmt.setString(6, baju.getBahan());
                pstmt.setNull(7, Types.VARCHAR);
                pstmt.setNull(8, Types.INTEGER);
                pstmt.setNull(9, Types.VARCHAR);
                pstmt.setNull(10, Types.VARCHAR);
            } else if (produk instanceof Celana) {
                Celana celana = (Celana) produk;
                pstmt.setNull(5, Types.VARCHAR);
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setString(7, celana.getJenis());
                pstmt.setInt(8, celana.getUkuranPinggang());
                pstmt.setNull(9, Types.VARCHAR);
                pstmt.setNull(10, Types.VARCHAR);
            } else if (produk instanceof Aksesoris) {
                Aksesoris aksesoris = (Aksesoris) produk;
                pstmt.setNull(5, Types.VARCHAR);
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setNull(7, Types.VARCHAR);
                pstmt.setNull(8, Types.INTEGER);
                pstmt.setString(9, aksesoris.getTipe());
                pstmt.setString(10, aksesoris.getBrand());
            }
            
            pstmt.setDouble(11, produk.getHarga());
            pstmt.setInt(12, produk.getStok());
            
            return pstmt.executeUpdate() > 0;
            
        } finally {
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    /**
     * Update produk (OOP: terima object Pakaian)
     */
    public boolean updateProduk(Pakaian produk) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "UPDATE produk SET nama_produk = ?, kategori = ?, ukuran = ?, " +
                        "warna = ?, bahan = ?, jenis = ?, ukuran_pinggang = ?, " +
                        "tipe = ?, brand = ?, harga = ?, stok = ? WHERE id = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, produk.getNamaProduk());
            pstmt.setString(2, produk.getKategori());
            pstmt.setString(3, produk.getUkuran());
            
            // Set atribut spesifik
            if (produk instanceof Baju) {
                Baju baju = (Baju) produk;
                pstmt.setString(4, baju.getWarna());
                pstmt.setString(5, baju.getBahan());
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setNull(7, Types.INTEGER);
                pstmt.setNull(8, Types.VARCHAR);
                pstmt.setNull(9, Types.VARCHAR);
            } else if (produk instanceof Celana) {
                Celana celana = (Celana) produk;
                pstmt.setNull(4, Types.VARCHAR);
                pstmt.setNull(5, Types.VARCHAR);
                pstmt.setString(6, celana.getJenis());
                pstmt.setInt(7, celana.getUkuranPinggang());
                pstmt.setNull(8, Types.VARCHAR);
                pstmt.setNull(9, Types.VARCHAR);
            } else if (produk instanceof Aksesoris) {
                Aksesoris aksesoris = (Aksesoris) produk;
                pstmt.setNull(4, Types.VARCHAR);
                pstmt.setNull(5, Types.VARCHAR);
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setNull(7, Types.INTEGER);
                pstmt.setString(8, aksesoris.getTipe());
                pstmt.setString(9, aksesoris.getBrand());
            }
            
            pstmt.setDouble(10, produk.getHarga());
            pstmt.setInt(11, produk.getStok());
            pstmt.setInt(12, produk.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } finally {
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    /**
     * Hapus produk
     */
    public boolean deleteProduk(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM produk WHERE id = ?");
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate() > 0;
            
        } finally {
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
    }
    
    /**
     * Cari produk by kode
     */
    public Pakaian getProdukByKode(String kode) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM produk WHERE kode_produk = ?");
            pstmt.setString(1, kode.toUpperCase());
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPakaian(rs);
            }
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return null;
    }
    
    /**
     * MAPPING: ResultSet → Pakaian (Polymorphism!)
     * Method ini otomatis buat object Baju/Celana/Aksesoris sesuai kategori
     */
    private Pakaian mapResultSetToPakaian(ResultSet rs) throws SQLException {
        String kategori = rs.getString("kategori");
        Pakaian produk = null;
        
        // Polymorphism: Buat object sesuai kategori
        switch (kategori) {
            case "Baju":
                Baju baju = new Baju();
                baju.setWarna(rs.getString("warna"));
                baju.setBahan(rs.getString("bahan"));
                produk = baju;
                break;
                
            case "Celana":
                Celana celana = new Celana();
                celana.setJenis(rs.getString("jenis"));
                celana.setUkuranPinggang(rs.getInt("ukuran_pinggang"));
                produk = celana;
                break;
                
            case "Aksesoris":
                Aksesoris aksesoris = new Aksesoris();
                aksesoris.setTipe(rs.getString("tipe"));
                aksesoris.setBrand(rs.getString("brand"));
                produk = aksesoris;
                break;
        }
        
        // Set atribut umum dari parent class
        if (produk != null) {
            produk.setId(rs.getInt("id"));
            produk.setKodeProduk(rs.getString("kode_produk"));
            produk.setNamaProduk(rs.getString("nama_produk"));
            produk.setHarga(rs.getDouble("harga"));
            produk.setUkuran(rs.getString("ukuran"));
            produk.setStok(rs.getInt("stok"));
        }
        
        return produk;
    }
    
    /**
     * Get produk by kategori
     */
    public List<Pakaian> getProdukByKategori(String kategori) throws SQLException {
        List<Pakaian> listProduk = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM produk WHERE kategori = ? ORDER BY nama_produk");
            pstmt.setString(1, kategori);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Pakaian produk = mapResultSetToPakaian(rs);
                if (produk != null) {
                    listProduk.add(produk);
                }
            }
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return listProduk;
    }
}