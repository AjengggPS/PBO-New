/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fashionhub.payment;

/**
 *
 * @author Ajeng Puspita Sari
 */

/**
 * Class PembayaranCash implements MetodePembayaran
 * Menerapkan konsep Implementation dari Interface
 */
public class PembayaranCash implements MetodePembayaran {
    
    private double jumlahBayar;
    private double kembalian;
    
    // Constructor
    public PembayaranCash() {}
    
    public PembayaranCash(double jumlahBayar) {
        this.jumlahBayar = jumlahBayar;
    }
    
    // Getter & Setter
    public double getJumlahBayar() {
        return jumlahBayar;
    }
    
    public void setJumlahBayar(double jumlahBayar) {
        this.jumlahBayar = jumlahBayar;
    }
    
    public double getKembalian() {
        return kembalian;
    }
    
    // === IMPLEMENTATION dari Interface MetodePembayaran ===
    
    /**
     * Proses pembayaran cash
     * Validasi: Uang yang dibayar harus >= total
     */
    @Override
    public boolean prosesPembayaran(double total) {
        if (validasiPembayaran(total, this.jumlahBayar)) {
            this.kembalian = hitungKembalian(total, this.jumlahBayar);
            System.out.println("✅ Pembayaran Cash berhasil!");
            System.out.println("Total: Rp " + String.format("%,.0f", total));
            System.out.println("Bayar: Rp " + String.format("%,.0f", jumlahBayar));
            System.out.println("Kembali: Rp " + String.format("%,.0f", kembalian));
            return true;
        }
        System.out.println("❌ Pembayaran gagal! Uang tidak cukup.");
        return false;
    }
    
    @Override
    public String getNamaMetode() {
        return "Cash";
    }
    
    /**
     * Validasi: Uang yang dibayar harus lebih besar atau sama dengan total
     */
    @Override
    public boolean validasiPembayaran(double total, double jumlahBayar) {
        return jumlahBayar >= total;
    }
    
    /**
     * Hitung kembalian
     */
    @Override
    public double hitungKembalian(double total, double jumlahBayar) {
        if (jumlahBayar >= total) {
            return jumlahBayar - total;
        }
        return 0;
    }
    
    // === BUSINESS METHODS Khusus Cash ===
    
    /**
     * Method untuk breakdown uang kembalian (pecahan)
     * Contoh: 87000 -> 1x50rb, 1x20rb, 1x10rb, 1x5rb, 1x2rb
     */
    public String breakdownKembalian() {
        if (kembalian <= 0) return "Tidak ada kembalian";
        
        int sisa = (int) kembalian;
        StringBuilder breakdown = new StringBuilder("Pecahan Kembalian:\n");
        
        int[] pecahan = {100000, 50000, 20000, 10000, 5000, 2000, 1000};
        String[] nama = {"100rb", "50rb", "20rb", "10rb", "5rb", "2rb", "1rb"};
        
        for (int i = 0; i < pecahan.length; i++) {
            if (sisa >= pecahan[i]) {
                int jumlah = sisa / pecahan[i];
                breakdown.append(jumlah).append("x ").append(nama[i]).append("\n");
                sisa %= pecahan[i];
            }
        }
        
        if (sisa > 0) {
            breakdown.append("Sisa: Rp ").append(sisa);
        }
        
        return breakdown.toString();
    }
    
    @Override
    public String toString() {
        return String.format("Pembayaran Cash - Bayar: Rp %.0f, Kembali: Rp %.0f", 
                           jumlahBayar, kembalian);
    }
}
