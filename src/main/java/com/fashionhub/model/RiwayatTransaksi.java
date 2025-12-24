/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fashionhub.model;

/**
 *
 * @author Ajeng Puspita Sari
 */

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Class RiwayatTransaksi (UPDATED sesuai Class Diagram Baru!)
 * Untuk fitur reporting dan history transaksi
 * Menerapkan konsep Encapsulation dan Association
 */
public class RiwayatTransaksi {
    
    // Atribut private
    private int id;
    private int transaksiId;
    private int periodeBulan;
    private int periodeTahun;
    private int totalItem;
    private double totalPendapatan;
    private String metodeBayar;
    private String namaKasir;
    private String namaPelanggan;
    private Timestamp createdAt;
    
    // Association dengan Transaksi
    private Transaksi transaksi;
    
    // List untuk menampung riwayat (untuk method filter)
    private static List<Transaksi> daftarTransaksi = new ArrayList<>();
    
    // Constructor
    public RiwayatTransaksi() {}
    
    public RiwayatTransaksi(int transaksiId, int periodeBulan, int periodeTahun) {
        this.transaksiId = transaksiId;
        this.periodeBulan = periodeBulan;
        this.periodeTahun = periodeTahun;
    }
    
    // === ENCAPSULATION: Getter & Setter ===
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getTransaksiId() {
        return transaksiId;
    }
    
    public void setTransaksiId(int transaksiId) {
        this.transaksiId = transaksiId;
    }
    
    public int getPeriodeBulan() {
        return periodeBulan;
    }
    
    public void setPeriodeBulan(int periodeBulan) {
        if (periodeBulan >= 1 && periodeBulan <= 12) {
            this.periodeBulan = periodeBulan;
        } else {
            throw new IllegalArgumentException("Bulan harus antara 1-12");
        }
    }
    
    public int getPeriodeTahun() {
        return periodeTahun;
    }
    
    public void setPeriodeTahun(int periodeTahun) {
        if (periodeTahun >= 2000 && periodeTahun <= 2100) {
            this.periodeTahun = periodeTahun;
        } else {
            throw new IllegalArgumentException("Tahun tidak valid");
        }
    }
    
    public int getTotalItem() {
        return totalItem;
    }
    
    public void setTotalItem(int totalItem) {
        this.totalItem = totalItem;
    }
    
    public double getTotalPendapatan() {
        return totalPendapatan;
    }
    
    public void setTotalPendapatan(double totalPendapatan) {
        this.totalPendapatan = totalPendapatan;
    }
    
    public String getMetodeBayar() {
        return metodeBayar;
    }
    
    public void setMetodeBayar(String metodeBayar) {
        this.metodeBayar = metodeBayar;
    }
    
    public String getNamaKasir() {
        return namaKasir;
    }
    
    public void setNamaKasir(String namaKasir) {
        this.namaKasir = namaKasir;
    }
    
    public String getNamaPelanggan() {
        return namaPelanggan;
    }
    
    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Transaksi getTransaksi() {
        return transaksi;
    }
    
    public void setTransaksi(Transaksi transaksi) {
        this.transaksi = transaksi;
        if (transaksi != null) {
            this.transaksiId = transaksi.getId();
        }
    }
    
    public static List<Transaksi> getDaftarTransaksi() {
        return daftarTransaksi;
    }
    
    public static void setDaftarTransaksi(List<Transaksi> list) {
        daftarTransaksi = list;
    }
    
    // === BUSINESS METHODS (dari Class Diagram Lama) ===
    
    /**
     * Get nama bulan dalam bahasa Indonesia
     */
    public String getNamaBulan() {
        if (periodeBulan < 1 || periodeBulan > 12) return "Invalid";
        
        Month month = Month.of(periodeBulan);
        return month.getDisplayName(TextStyle.FULL, new Locale("id", "ID"));
    }
    
    /**
     * Get periode dalam format string
     */
    public String getPeriodeString() {
        return getNamaBulan() + " " + periodeTahun;
    }
    
    /**
     * Validasi periode
     */
    public boolean isPeriodeValid() {
        return periodeBulan >= 1 && periodeBulan <= 12 && 
               periodeTahun >= 2000 && periodeTahun <= 2100;
    }
    
    /**
     * Get rata-rata harga per item
     */
    public double getRataHargaPerItem() {
        if (totalItem == 0) return 0;
        return totalPendapatan / totalItem;
    }
    
    // === METHOD BARU dari Class Diagram! ===
    
    /**
     * 🆕 Method 1: Tambah Transaksi ke Riwayat
     * Menambahkan transaksi baru ke dalam daftar riwayat
     */
    public void tambahTransaksi(Transaksi transaksi) {
        if (transaksi == null) {
            throw new IllegalArgumentException("Transaksi tidak boleh null");
        }
        
        if (transaksi.validate()) {
            daftarTransaksi.add(transaksi);
            System.out.println("✅ Transaksi " + transaksi.getNoTransaksi() + " berhasil ditambahkan ke riwayat");
        } else {
            throw new IllegalStateException("Transaksi tidak valid, tidak dapat ditambahkan");
        }
    }
    
    /**
     * 🆕 Method 2: Lihat Semua Transaksi
     * Mengembalikan semua transaksi dalam riwayat
     */
    public List<Transaksi> lihatSemuaTransaksi() {
        return new ArrayList<>(daftarTransaksi);
    }
    
    /**
     * 🆕 Method 3: Filter By Tanggal
     * Format tanggal: "yyyy-MM-dd" (contoh: "2025-01-15")
     */
    public List<Transaksi> filterByTanggal(String tanggal) {
        if (tanggal == null || tanggal.isEmpty()) {
            throw new IllegalArgumentException("Tanggal tidak boleh kosong");
        }
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate targetDate = LocalDate.parse(tanggal, formatter);
            
            return daftarTransaksi.stream()
                .filter(t -> {
                    if (t.getWaktuTransaksi() == null) return false;
                    LocalDate transDate = t.getWaktuTransaksi().toLocalDateTime().toLocalDate();
                    return transDate.equals(targetDate);
                })
                .collect(Collectors.toList());
                
        } catch (Exception e) {
            throw new IllegalArgumentException("Format tanggal salah. Gunakan: yyyy-MM-dd (contoh: 2025-01-15)");
        }
    }
    
    /**
     * 🆕 Method 4: Filter By Pelanggan (by ID)
     * Filter transaksi berdasarkan ID pelanggan
     */
    public List<Transaksi> filterByPelanggan(String idPelanggan) {
        if (idPelanggan == null || idPelanggan.isEmpty()) {
            throw new IllegalArgumentException("ID Pelanggan tidak boleh kosong");
        }
        
        try {
            int id = Integer.parseInt(idPelanggan);
            
            return daftarTransaksi.stream()
                .filter(t -> t.getPelangganId() != null && t.getPelangganId() == id)
                .collect(Collectors.toList());
                
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ID Pelanggan harus berupa angka");
        }
    }
    
    /**
     * 🆕 Method 5: Hitung Total Penjualan
     * Menghitung total penjualan dari semua transaksi dalam riwayat
     */
    public double hitungTotalPenjualan() {
        return daftarTransaksi.stream()
            .filter(t -> "success".equals(t.getStatus()))
            .mapToDouble(Transaksi::getTotalBayar)
            .sum();
    }
    
    /**
     * 🆕 Method 6: Cetak Laporan
     * Generate laporan lengkap riwayat transaksi
     */
    public void cetakLaporan() {
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║          LAPORAN RIWAYAT TRANSAKSI - FASHIONHUB              ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();
        
        if (daftarTransaksi.isEmpty()) {
            System.out.println("⚠️  Tidak ada transaksi dalam riwayat");
            return;
        }
        
        System.out.println("📊 STATISTIK UMUM:");
        System.out.println("   Total Transaksi    : " + daftarTransaksi.size());
        System.out.println("   Total Penjualan    : Rp " + String.format("%,.0f", hitungTotalPenjualan()));
        System.out.println("   Rata-rata/Transaksi: Rp " + String.format("%,.0f", hitungTotalPenjualan() / daftarTransaksi.size()));
        System.out.println();
        
        System.out.println("📝 DETAIL TRANSAKSI:");
        System.out.println("─────────────────────────────────────────────────────────────────");
        System.out.printf("%-15s %-20s %-15s %-15s%n", "No. Transaksi", "Waktu", "Pelanggan", "Total");
        System.out.println("─────────────────────────────────────────────────────────────────");
        
        for (Transaksi t : daftarTransaksi) {
            String pelanggan = t.getPelanggan() != null ? 
                              t.getPelanggan().getNamaPelanggan() : "Umum";
            
            String waktu = t.getWaktuTransaksi() != null ? 
                          t.getWaktuTransaksi().toString().substring(0, 16) : "-";
            
            System.out.printf("%-15s %-20s %-15s Rp %,-12.0f%n", 
                t.getNoTransaksi(), 
                waktu,
                pelanggan, 
                t.getTotalBayar()
            );
        }
        
        System.out.println("─────────────────────────────────────────────────────────────────");
        System.out.println();
        
        // Statistik Metode Pembayaran
        long cashCount = daftarTransaksi.stream()
            .filter(t -> "Cash".equals(t.getMetodeBayar()))
            .count();
        long qrisCount = daftarTransaksi.stream()
            .filter(t -> "QRIS".equals(t.getMetodeBayar()))
            .count();
        
        System.out.println("💳 METODE PEMBAYARAN:");
        System.out.println("   Cash : " + cashCount + " transaksi");
        System.out.println("   QRIS : " + qrisCount + " transaksi");
        System.out.println();
        
        System.out.println("════════════════════════════════════════════════════════════════");
        System.out.println("                    AKHIR LAPORAN");
        System.out.println("════════════════════════════════════════════════════════════════");
    }
    
    /**
     * 🆕 Method 7: Cari Transaksi by No Transaksi
     */
    public Transaksi cariTransaksi(String noTransaksi) {
        return daftarTransaksi.stream()
            .filter(t -> t.getNoTransaksi().equals(noTransaksi))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Generate summary report
     */
    public String generateSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("=== RIWAYAT TRANSAKSI ===\n");
        summary.append("Periode: ").append(getPeriodeString()).append("\n");
        summary.append("Kasir: ").append(namaKasir).append("\n");
        summary.append("Pelanggan: ").append(namaPelanggan != null ? namaPelanggan : "Pelanggan Umum").append("\n");
        summary.append("Total Item: ").append(totalItem).append(" pcs\n");
        summary.append("Total Pendapatan: Rp ").append(String.format("%,.0f", totalPendapatan)).append("\n");
        summary.append("Metode Bayar: ").append(metodeBayar).append("\n");
        summary.append("Rata-rata per Item: Rp ").append(String.format("%,.0f", getRataHargaPerItem())).append("\n");
        return summary.toString();
    }
    
    @Override
    public String toString() {
        return String.format("Riwayat[%s, %s, Rp %,.0f]", 
                           getPeriodeString(), namaKasir, totalPendapatan);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        RiwayatTransaksi that = (RiwayatTransaksi) obj;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}