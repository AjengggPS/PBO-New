/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fashionhub.model;

/**
 *
 * @author Ajeng Puspita Sari
 */

public class Baju extends Pakaian {
    
    // Atribut khusus Baju
    private String bahan;
    private String warna;
    
    // Constructor default
    public Baju() {
        super();
    }
    
    // Constructor dengan parameter
    public Baju(String kodeProduk, String namaProduk, double harga, 
                String ukuran, int stok, String bahan, String warna) {
        super(kodeProduk, namaProduk, harga, ukuran, stok);
        this.bahan = bahan;
        this.warna = warna;
    }
    
    // === ENCAPSULATION: Getter & Setter khusus Baju ===
    
    public String getBahan() {
        return bahan;
    }
    
    public void setBahan(String bahan) {
        this.bahan = bahan;
    }
    
    public String getWarna() {
        return warna;
    }
    
    public void setWarna(String warna) {
        this.warna = warna;
    }
    
    // === POLYMORPHISM: Override Abstract Methods ===
    
    /**
     * Implementasi hitungDiskon untuk Baju
     * Diskon 10% untuk baju
     */
    @Override
    public double hitungDiskon() {
        double persenDiskon = 0.10; // 10% diskon untuk baju
        return this.harga * (1 - persenDiskon);
    }
    
    /**
     * Implementasi deskripsiProduk untuk Baju
     */
    @Override
    public String deskripsiProduk() {
        StringBuilder desc = new StringBuilder();
        desc.append("=== BAJU ===\n");
        desc.append("Kode: ").append(kodeProduk).append("\n");
        desc.append("Nama: ").append(namaProduk).append("\n");
        desc.append("Harga: Rp ").append(String.format("%,.0f", harga)).append("\n");
        desc.append("Ukuran: ").append(ukuran != null ? ukuran : "-").append("\n");
        desc.append("Bahan: ").append(bahan != null ? bahan : "-").append("\n");
        desc.append("Warna: ").append(warna != null ? warna : "-").append("\n");
        desc.append("Stok: ").append(getStok()).append(" pcs\n");
        desc.append("Harga Diskon: Rp ").append(String.format("%,.0f", hitungDiskon()));
        return desc.toString();
    }
    
    /**
     * Implementasi getKategori
     */
    @Override
    public String getKategori() {
        return "Baju";
    }
    
    // === BUSINESS METHODS khusus Baju ===
    
    /**
     * Method untuk cek kualitas bahan
     */
    public String cekKualitasBahan() {
        if (bahan == null) return "Tidak diketahui";
        
        String bahanLower = bahan.toLowerCase();
        if (bahanLower.contains("katun") || bahanLower.contains("cotton")) {
            return "Premium - Adem dan Menyerap Keringat";
        } else if (bahanLower.contains("polyester")) {
            return "Standard - Tahan Lama";
        } else if (bahanLower.contains("sutra") || bahanLower.contains("silk")) {
            return "Luxury - Lembut dan Elegan";
        } else {
            return "Standard";
        }
    }
    
    @Override
    public String toString() {
        return String.format("Baju: %s - %s [%s, %s] (Rp %.0f) - Stok: %d", 
                           kodeProduk, namaProduk, bahan, warna, harga, getStok());
    }
}