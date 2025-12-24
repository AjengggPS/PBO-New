/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fashionhub.model;

/**
 *
 * @author Ajeng Puspita Sari
 */

/**
 * Class Celana extends Pakaian
 * Menerapkan konsep Inheritance & Polymorphism
 */
public class Celana extends Pakaian {
    
    // Atribut khusus Celana
    private String jenis;
    private int ukuranPinggang; // dalam cm
    
    // Constructor default
    public Celana() {
        super();
    }
    
    // Constructor dengan parameter
    public Celana(String kodeProduk, String namaProduk, double harga, 
                  String ukuran, int stok, String jenis, int ukuranPinggang) {
        super(kodeProduk, namaProduk, harga, ukuran, stok);
        this.jenis = jenis;
        this.ukuranPinggang = ukuranPinggang;
    }
    
    // === ENCAPSULATION: Getter & Setter khusus Celana ===
    
    public String getJenis() {
        return jenis;
    }
    
    public void setJenis(String jenis) {
        this.jenis = jenis;
    }
    
    public int getUkuranPinggang() {
        return ukuranPinggang;
    }
    
    public void setUkuranPinggang(int ukuranPinggang) {
        if (ukuranPinggang > 0) {
            this.ukuranPinggang = ukuranPinggang;
        }
    }
    
    // === POLYMORPHISM: Override Abstract Methods ===
    
    /**
     * Implementasi hitungDiskon untuk Celana
     * Diskon 15% untuk celana
     */
    @Override
    public double hitungDiskon() {
        double persenDiskon = 0.15; // 15% diskon untuk celana
        return this.harga * (1 - persenDiskon);
    }
    
    /**
     * Implementasi deskripsiProduk untuk Celana
     */
    @Override
    public String deskripsiProduk() {
        StringBuilder desc = new StringBuilder();
        desc.append("=== CELANA ===\n");
        desc.append("Kode: ").append(kodeProduk).append("\n");
        desc.append("Nama: ").append(namaProduk).append("\n");
        desc.append("Harga: Rp ").append(String.format("%,.0f", harga)).append("\n");
        desc.append("Ukuran: ").append(ukuran != null ? ukuran : "-").append("\n");
        desc.append("Jenis: ").append(jenis != null ? jenis : "-").append("\n");
        desc.append("Ukuran Pinggang: ").append(ukuranPinggang).append(" cm\n");
        desc.append("Stok: ").append(getStok()).append(" pcs\n");
        desc.append("Harga Diskon: Rp ").append(String.format("%,.0f", hitungDiskon()));
        return desc.toString();
    }
    
    /**
     * Implementasi getKategori
     */
    @Override
    public String getKategori() {
        return "Celana";
    }
    
    // === BUSINESS METHODS khusus Celana ===
    
    /**
     * Method untuk cek ukuran berdasarkan standar internasional
     */
    public String cekUkuranStandar() {
        if (ukuranPinggang <= 0) return "Ukuran tidak valid";
        
        if (ukuranPinggang < 70) {
            return "XS - Extra Small";
        } else if (ukuranPinggang < 75) {
            return "S - Small";
        } else if (ukuranPinggang < 80) {
            return "M - Medium";
        } else if (ukuranPinggang < 85) {
            return "L - Large";
        } else if (ukuranPinggang < 90) {
            return "XL - Extra Large";
        } else {
            return "XXL - Extra Extra Large";
        }
    }
    
    /**
     * Method untuk rekomendasi berdasarkan jenis
     */
    public String getRekomendasi() {
        if (jenis == null) return "Tidak ada rekomendasi";
        
        String jenisLower = jenis.toLowerCase();
        if (jenisLower.contains("jeans")) {
            return "Cocok untuk casual & formal casual";
        } else if (jenisLower.contains("chino")) {
            return "Cocok untuk formal & semi formal";
        } else if (jenisLower.contains("training") || jenisLower.contains("jogger")) {
            return "Cocok untuk olahraga & santai";
        } else {
            return "Celana serbaguna";
        }
    }
    
    @Override
    public String toString() {
        return String.format("Celana: %s - %s [%s, W%d] (Rp %.0f) - Stok: %d", 
                           kodeProduk, namaProduk, jenis, ukuranPinggang, harga, getStok());
    }
}