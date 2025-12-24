/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fashionhub.payment;

/**
 *
 * @author Ajeng Puspita Sari
 */

import java.util.Random;

/**
 * Class PembayaranQRIS implements MetodePembayaran
 * Menerapkan konsep Implementation dari Interface
 */
public class PembayaranQRIS implements MetodePembayaran {
    
    private String kodeQR;
    private boolean statusVerifikasi;
    
    // Constructor
    public PembayaranQRIS() {
        this.kodeQR = generateKodeQR();
        this.statusVerifikasi = false;
    }
    
    // Getter & Setter
    public String getKodeQR() {
        return kodeQR;
    }
    
    public boolean isStatusVerifikasi() {
        return statusVerifikasi;
    }
    
    public void setStatusVerifikasi(boolean statusVerifikasi) {
        this.statusVerifikasi = statusVerifikasi;
    }
    
    // === IMPLEMENTATION dari Interface MetodePembayaran ===
    
    /**
     * Proses pembayaran QRIS
     * Untuk simulasi, dianggap langsung berhasil
     */
    @Override
    public boolean prosesPembayaran(double total) {
        System.out.println("🔄 Memproses pembayaran QRIS...");
        System.out.println("Kode QR: " + kodeQR);
        System.out.println("Total: Rp " + String.format("%,.0f", total));
        
        // Simulasi proses verifikasi (dalam real app, ini dari payment gateway)
        boolean verifikasi = simulasiVerifikasi();
        
        if (verifikasi) {
            this.statusVerifikasi = true;
            System.out.println("✅ Pembayaran QRIS berhasil!");
            return true;
        } else {
            System.out.println("❌ Pembayaran QRIS gagal! Silakan coba lagi.");
            return false;
        }
    }
    
    @Override
    public String getNamaMetode() {
        return "QRIS";
    }
    
    /**
     * Validasi QRIS: Selalu true karena payment gateway yang handle
     */
    @Override
    public boolean validasiPembayaran(double total, double jumlahBayar) {
        // QRIS tidak perlu cek jumlah bayar, langsung exact amount
        return true;
    }
    
    /**
     * Kembalian QRIS selalu 0 (exact payment)
     */
    @Override
    public double hitungKembalian(double total, double jumlahBayar) {
        return 0; // QRIS tidak ada kembalian
    }
    
    // === BUSINESS METHODS Khusus QRIS ===
    
    /**
     * Generate kode QR unik untuk transaksi
     */
    private String generateKodeQR() {
        String prefix = "QRIS-FH-";
        String randomCode = String.format("%08d", new Random().nextInt(100000000));
        return prefix + randomCode;
    }
    
    /**
     * Simulasi verifikasi pembayaran dari payment gateway
     * Dalam real app, ini akan hit API payment gateway
     */
    private boolean simulasiVerifikasi() {
        try {
            // Simulasi delay processing
            Thread.sleep(1000);
            
            // Dalam simulasi, selalu berhasil
            // Dalam real app, ini cek response dari payment gateway
            return true;
            
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Method untuk generate URL QR Code
     * Bisa digunakan untuk generate QR image
     */
    public String getQRCodeURL() {
        String baseURL = "https://api.qrserver.com/v1/create-qr-code/";
        String data = kodeQR;
        String size = "200x200";
        return baseURL + "?size=" + size + "&data=" + data;
    }
    
    /**
     * Method untuk cek status transaksi
     */
    public String getStatusTransaksi() {
        if (statusVerifikasi) {
            return "Verified - Pembayaran Berhasil";
        } else {
            return "Pending - Menunggu Verifikasi";
        }
    }
    
    /**
     * Method untuk detail pembayaran QRIS
     */
    public String getDetailPembayaran() {
        StringBuilder detail = new StringBuilder();
        detail.append("=== DETAIL PEMBAYARAN QRIS ===\n");
        detail.append("Kode QR: ").append(kodeQR).append("\n");
        detail.append("Status: ").append(getStatusTransaksi()).append("\n");
        detail.append("Provider: FashionHub Payment\n");
        return detail.toString();
    }
    
    @Override
    public String toString() {
        return String.format("Pembayaran QRIS - Kode: %s, Status: %s", 
                           kodeQR, getStatusTransaksi());
    }
}