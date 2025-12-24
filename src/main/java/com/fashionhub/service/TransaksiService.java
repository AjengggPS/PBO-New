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
import com.fashionhub.payment.*;
import com.fashionhub.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Layer untuk Transaksi
 * Menangani business logic transaksi dengan konsep OOP
 */
public class TransaksiService {
    
    private ProdukService produkService = new ProdukService();
    
    /**
     * Proses transaksi lengkap (OOP Way!)
     */
    public int prosesTransaksi(Transaksi transaksi, List<Integer> produkIds, 
                               List<Integer> quantities) throws SQLException {
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Start transaction
            
            // 1. Insert transaksi
            String sqlTransaksi = "INSERT INTO transaksi " +
                "(no_transaksi, kasir_id, pelanggan_id, total_belanja, diskon, " +
                "total_bayar, metode_bayar, jumlah_bayar, kembalian, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            pstmt = conn.prepareStatement(sqlTransaksi, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, transaksi.getNoTransaksi());
            pstmt.setInt(2, transaksi.getKasirId());
            
            if (transaksi.getPelangganId() != null) {
                pstmt.setInt(3, transaksi.getPelangganId());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }
            
            pstmt.setDouble(4, transaksi.getTotalBelanja());
            pstmt.setDouble(5, transaksi.getDiskon());
            pstmt.setDouble(6, transaksi.getTotalBayar());
            pstmt.setString(7, transaksi.getMetodeBayar());
            pstmt.setDouble(8, transaksi.getJumlahBayar());
            pstmt.setDouble(9, transaksi.getKembalian());
            pstmt.setString(10, "success");
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Gagal menyimpan transaksi");
            }
            
            // Get transaksi ID
            rs = pstmt.getGeneratedKeys();
            int transaksiId = 0;
            if (rs.next()) {
                transaksiId = rs.getInt(1);
            }
            
            rs.close();
            pstmt.close();
            
            // 2. Insert detail transaksi & update stok
            String sqlDetail = "INSERT INTO detail_transaksi " +
                "(transaksi_id, produk_id, nama_produk, kategori, harga, jumlah, subtotal, diskon_item) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            
            String sqlUpdateStok = "UPDATE produk SET stok = stok - ? WHERE id = ?";
            
            for (int i = 0; i < produkIds.size(); i++) {
                int produkId = produkIds.get(i);
                int qty = quantities.get(i);
                
                // Get produk menggunakan OOP service
                Pakaian produk = produkService.getProdukById(produkId);
                
                if (produk == null) {
                    throw new SQLException("Produk tidak ditemukan: " + produkId);
                }
                
                // Validasi stok menggunakan method di class Pakaian
                if (!produk.cekStokTersedia(qty)) {
                    throw new SQLException("Stok tidak cukup untuk: " + produk.getNamaProduk());
                }
                
                // Insert detail
                pstmt = conn.prepareStatement(sqlDetail);
                pstmt.setInt(1, transaksiId);
                pstmt.setInt(2, produkId);
                pstmt.setString(3, produk.getNamaProduk());
                pstmt.setString(4, produk.getKategori());
                pstmt.setDouble(5, produk.getHarga());
                pstmt.setInt(6, qty);
                pstmt.setDouble(7, produk.getHarga() * qty);
                
                // Hitung diskon menggunakan method polymorphic
                double hargaDiskon = produk.hitungDiskon();
                double diskonItem = produk.getHarga() - hargaDiskon;
                pstmt.setDouble(8, diskonItem);
                
                pstmt.executeUpdate();
                pstmt.close();
                
                // Update stok menggunakan method di class Pakaian
                pstmt = conn.prepareStatement(sqlUpdateStok);
                pstmt.setInt(1, qty);
                pstmt.setInt(2, produkId);
                pstmt.executeUpdate();
                pstmt.close();
            }
            
            conn.commit(); // Commit transaction
            return transaksiId;
            
        } catch (Exception e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Error proses transaksi: " + e.getMessage(), e);
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) {
                conn.setAutoCommit(true);
                DBConnection.closeConnection(conn);
            }
        }
    }
    
    /**
     * Get transaksi by ID dengan detail lengkap (OOP Way!)
     */
    public Transaksi getTransaksiById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            // Get transaksi header
            String sql = "SELECT t.*, k.nama_kasir, p.nama_pelanggan " +
                        "FROM transaksi t " +
                        "JOIN kasir k ON t.kasir_id = k.id " +
                        "LEFT JOIN pelanggan p ON t.pelanggan_id = p.id " +
                        "WHERE t.id = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Transaksi transaksi = mapResultSetToTransaksi(rs);
                
                // Set Kasir object
                Kasir kasir = new Kasir();
                kasir.setId(rs.getInt("kasir_id"));
                kasir.setNamaKasir(rs.getString("nama_kasir"));
                transaksi.setKasir(kasir);
                
                // Set Pelanggan object (jika ada)
                if (rs.getInt("pelanggan_id") > 0) {
                    Pelanggan pelanggan = new Pelanggan();
                    pelanggan.setId(rs.getInt("pelanggan_id"));
                    pelanggan.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    transaksi.setPelanggan(pelanggan);
                }
                
                rs.close();
                pstmt.close();
                
                // Get detail items
                String sqlDetail = "SELECT * FROM detail_transaksi WHERE transaksi_id = ?";
                pstmt = conn.prepareStatement(sqlDetail);
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                
                List<Transaksi.DetailTransaksi> items = new ArrayList<>();
                while (rs.next()) {
                    Transaksi.DetailTransaksi detail = new Transaksi.DetailTransaksi();
                    detail.setId(rs.getInt("id"));
                    detail.setTransaksiId(rs.getInt("transaksi_id"));
                    detail.setProdukId(rs.getInt("produk_id"));
                    detail.setNamaProduk(rs.getString("nama_produk"));
                    detail.setKategori(rs.getString("kategori"));
                    detail.setHarga(rs.getDouble("harga"));
                    detail.setJumlah(rs.getInt("jumlah"));
                    detail.setSubtotal(rs.getDouble("subtotal"));
                    detail.setDiskonItem(rs.getDouble("diskon_item"));
                    items.add(detail);
                }
                
                transaksi.setDaftarItem(items);
                return transaksi;
            }
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return null;
    }
    
    /**
     * Get transaksi by periode (bulan & tahun)
     */
    public List<Transaksi> getTransaksiByPeriode(int bulan, int tahun) throws SQLException {
        List<Transaksi> listTransaksi = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT t.*, k.nama_kasir, p.nama_pelanggan " +
                        "FROM transaksi t " +
                        "JOIN kasir k ON t.kasir_id = k.id " +
                        "LEFT JOIN pelanggan p ON t.pelanggan_id = p.id " +
                        "WHERE MONTH(t.waktu_transaksi) = ? AND YEAR(t.waktu_transaksi) = ? " +
                        "ORDER BY t.waktu_transaksi DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bulan);
            pstmt.setInt(2, tahun);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaksi transaksi = mapResultSetToTransaksi(rs);
                
                // Set Kasir
                Kasir kasir = new Kasir();
                kasir.setId(rs.getInt("kasir_id"));
                kasir.setNamaKasir(rs.getString("nama_kasir"));
                transaksi.setKasir(kasir);
                
                // Set Pelanggan (if exists)
                if (rs.getInt("pelanggan_id") > 0) {
                    Pelanggan pelanggan = new Pelanggan();
                    pelanggan.setId(rs.getInt("pelanggan_id"));
                    pelanggan.setNamaPelanggan(rs.getString("nama_pelanggan"));
                    transaksi.setPelanggan(pelanggan);
                }
                
                listTransaksi.add(transaksi);
            }
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return listTransaksi;
    }
    
    /**
     * MAPPING: ResultSet → Transaksi
     */
    private Transaksi mapResultSetToTransaksi(ResultSet rs) throws SQLException {
        Transaksi transaksi = new Transaksi();
        transaksi.setId(rs.getInt("id"));
        transaksi.setNoTransaksi(rs.getString("no_transaksi"));
        transaksi.setKasirId(rs.getInt("kasir_id"));
        
        int pelangganId = rs.getInt("pelanggan_id");
        if (!rs.wasNull()) {
            transaksi.setPelangganId(pelangganId);
        }
        
        transaksi.setTotalBelanja(rs.getDouble("total_belanja"));
        transaksi.setDiskon(rs.getDouble("diskon"));
        transaksi.setTotalBayar(rs.getDouble("total_bayar"));
        transaksi.setMetodeBayar(rs.getString("metode_bayar"));
        transaksi.setJumlahBayar(rs.getDouble("jumlah_bayar"));
        transaksi.setKembalian(rs.getDouble("kembalian"));
        transaksi.setStatus(rs.getString("status"));
        transaksi.setWaktuTransaksi(rs.getTimestamp("waktu_transaksi"));
        
        return transaksi;
    }
}