/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fashionhub.service;

/**
 *
 * @author Ajeng Puspita Sari
 */

import com.fashionhub.model.RiwayatTransaksi;
import com.fashionhub.model.Transaksi;
import com.fashionhub.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service Layer untuk RiwayatTransaksi
 * Implementasi method dari Class Diagram
 */
public class RiwayatService {
    
    /**
     * Get riwayat by periode
     */
    public List<RiwayatTransaksi> getRiwayatByPeriode(int bulan, int tahun) throws SQLException {
        List<RiwayatTransaksi> listRiwayat = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM riwayat_transaksi " +
                        "WHERE periode_bulan = ? AND periode_tahun = ? " +
                        "ORDER BY created_at DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bulan);
            pstmt.setInt(2, tahun);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                listRiwayat.add(mapResultSetToRiwayat(rs));
            }
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return listRiwayat;
    }
    
    /**
     * Filter by tanggal (implementasi dari class diagram)
     */
    public List<Transaksi> filterByTanggal(String tanggal) throws SQLException {
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
                        "WHERE DATE(t.waktu_transaksi) = ? " +
                        "ORDER BY t.waktu_transaksi DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, tanggal);
            rs = pstmt.executeQuery();
            
            TransaksiService transaksiService = new TransaksiService();
            while (rs.next()) {
                // Reuse mapping dari TransaksiService
                Transaksi t = new Transaksi();
                t.setId(rs.getInt("id"));
                t.setNoTransaksi(rs.getString("no_transaksi"));
                t.setTotalBayar(rs.getDouble("total_bayar"));
                t.setMetodeBayar(rs.getString("metode_bayar"));
                t.setWaktuTransaksi(rs.getTimestamp("waktu_transaksi"));
                listTransaksi.add(t);
            }
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return listTransaksi;
    }
    
    /**
     * Filter by pelanggan (implementasi dari class diagram)
     */
    public List<Transaksi> filterByPelanggan(String idPelanggan) throws SQLException {
        List<Transaksi> listTransaksi = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            int pelangganId = Integer.parseInt(idPelanggan);
            
            conn = DBConnection.getConnection();
            
            String sql = "SELECT t.*, k.nama_kasir, p.nama_pelanggan " +
                        "FROM transaksi t " +
                        "JOIN kasir k ON t.kasir_id = k.id " +
                        "LEFT JOIN pelanggan p ON t.pelanggan_id = p.id " +
                        "WHERE t.pelanggan_id = ? " +
                        "ORDER BY t.waktu_transaksi DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pelangganId);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Transaksi t = new Transaksi();
                t.setId(rs.getInt("id"));
                t.setNoTransaksi(rs.getString("no_transaksi"));
                t.setTotalBayar(rs.getDouble("total_bayar"));
                t.setMetodeBayar(rs.getString("metode_bayar"));
                t.setWaktuTransaksi(rs.getTimestamp("waktu_transaksi"));
                listTransaksi.add(t);
            }
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return listTransaksi;
    }
    
    /**
     * Hitung total penjualan by periode
     */
    public double hitungTotalPenjualan(int bulan, int tahun) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            String sql = "SELECT SUM(total_pendapatan) as total " +
                        "FROM riwayat_transaksi " +
                        "WHERE periode_bulan = ? AND periode_tahun = ?";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bulan);
            pstmt.setInt(2, tahun);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble("total");
            }
            
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            DBConnection.closeConnection(conn);
        }
        
        return 0;
    }
    
    /**
     * Cetak laporan (implementasi dari class diagram)
     * Generate laporan text yang bisa di-print
     */
    public String cetakLaporan(int bulan, int tahun) throws SQLException {
        StringBuilder laporan = new StringBuilder();
        
        List<RiwayatTransaksi> riwayat = getRiwayatByPeriode(bulan, tahun);
        double totalPenjualan = hitungTotalPenjualan(bulan, tahun);
        
        laporan.append("════════════════════════════════════════════════\n");
        laporan.append("        LAPORAN PENJUALAN FASHIONHUB\n");
        laporan.append("════════════════════════════════════════════════\n");
        laporan.append(String.format("Periode: %02d/%d\n", bulan, tahun));
        laporan.append("════════════════════════════════════════════════\n\n");
        
        laporan.append("RINGKASAN:\n");
        laporan.append(String.format("Total Transaksi : %d\n", riwayat.size()));
        laporan.append(String.format("Total Penjualan : Rp %,.0f\n", totalPenjualan));
        
        if (!riwayat.isEmpty()) {
            double rata = totalPenjualan / riwayat.size();
            laporan.append(String.format("Rata-rata/Trx  : Rp %,.0f\n", rata));
        }
        
        laporan.append("\n════════════════════════════════════════════════\n");
        laporan.append("DETAIL TRANSAKSI:\n");
        laporan.append("────────────────────────────────────────────────\n");
        
        for (RiwayatTransaksi r : riwayat) {
            laporan.append(String.format("Kasir: %s | Pelanggan: %s\n", 
                r.getNamaKasir(), 
                r.getNamaPelanggan() != null ? r.getNamaPelanggan() : "Umum"));
            laporan.append(String.format("Total Item: %d | Pendapatan: Rp %,.0f\n", 
                r.getTotalItem(), r.getTotalPendapatan()));
            laporan.append(String.format("Metode: %s\n", r.getMetodeBayar()));
            laporan.append("────────────────────────────────────────────────\n");
        }
        
        laporan.append("\n════════════════════════════════════════════════\n");
        laporan.append("              AKHIR LAPORAN\n");
        laporan.append("════════════════════════════════════════════════\n");
        
        return laporan.toString();
    }
    
    /**
     * MAPPING: ResultSet → RiwayatTransaksi
     */
    private RiwayatTransaksi mapResultSetToRiwayat(ResultSet rs) throws SQLException {
        RiwayatTransaksi riwayat = new RiwayatTransaksi();
        riwayat.setId(rs.getInt("id"));
        riwayat.setTransaksiId(rs.getInt("transaksi_id"));
        riwayat.setPeriodeBulan(rs.getInt("periode_bulan"));
        riwayat.setPeriodeTahun(rs.getInt("periode_tahun"));
        riwayat.setTotalItem(rs.getInt("total_item"));
        riwayat.setTotalPendapatan(rs.getDouble("total_pendapatan"));
        riwayat.setMetodeBayar(rs.getString("metode_bayar"));
        riwayat.setNamaKasir(rs.getString("nama_kasir"));
        riwayat.setNamaPelanggan(rs.getString("nama_pelanggan"));
        riwayat.setCreatedAt(rs.getTimestamp("created_at"));
        return riwayat;
    }
}