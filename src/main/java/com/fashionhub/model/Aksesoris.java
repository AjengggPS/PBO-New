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
 * Class Aksesoris extends Pakaian
 * Menerapkan konsep Inheritance & Polymorphism
 */
public class Aksesoris extends Pakaian {
    
    // Atribut khusus Aksesoris
    private String tipe;
    private String brand;
    
    // Constructor default
    public Aksesoris() {
        super();
    }
    
    // Constructor dengan parameter
    public Aksesoris(String kodeProduk, String namaProduk, double harga, 
                     String ukuran, int stok, String tipe, String brand) {
        super(kodeProduk, namaProduk, harga, ukuran, stok);
        this.tipe = tipe;
        this.brand = brand;
    }
    
    // === ENCAPSULATION: Getter & Setter khusus Aksesoris ===
    
    public String getTipe() {
        return tipe;
    }
    
    public void setTipe(String tipe) {
        this.tipe = tipe;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    // === POLYMORPHISM: Override Abstract Methods ===
    
    /**
     * Implementasi hitungDiskon untuk Aksesoris
     * Diskon 5% untuk aksesoris (lebih kecil karena margin tipis)
     */
    @Override
    public double hitungDiskon() {
        double persenDiskon = 0.05; // 5% diskon untuk aksesoris
        return this.harga * (1 - persenDiskon);
    }
    
    /**
     * Implementasi deskripsiProduk untuk Aksesoris
     */
    @Override
    public String deskripsiProduk() {
        StringBuilder desc = new StringBuilder();
        desc.append("=== AKSESORIS ===\n");
        desc.append("Kode: ").append(kodeProduk).append("\n");
        desc.append("Nama: ").append(namaProduk).append("\n");
        desc.append("Harga: Rp ").append(String.format("%,.0f", harga)).append("\n");
        desc.append("Ukuran: ").append(ukuran != null ? ukuran : "All Size").append("\n");
        desc.append("Tipe: ").append(tipe != null ? tipe : "-").append("\n");
        desc.append("Brand: ").append(brand != null ? brand : "No Brand").append("\n");
        desc.append("Stok: ").append(getStok()).append(" pcs\n");
        desc.append("Harga Diskon: Rp ").append(String.format("%,.0f", hitungDiskon()));
        return desc.toString();
    }
    
    /**
     * Implementasi getKategori
     */
    @Override
    public String getKategori() {
        return "Aksesoris";
    }
    
    // === BUSINESS METHODS khusus Aksesoris ===
    
    /**
     * Method untuk menampilkan brand pada nota
     */
    public String tampilkanBrand() {
        if (brand != null && !brand.isEmpty()) {
            return "Brand: " + brand;
        }
        return "No Brand";
    }
    
    /**
     * Method untuk cek kategori aksesoris
     */
    public String getKategoriAksesoris() {
        if (tipe == null) return "Aksesoris Umum";
        
        String tipeLower = tipe.toLowerCase();
        if (tipeLower.contains("topi") || tipeLower.contains("cap") || tipeLower.contains("hat")) {
            return "Headwear";
        } else if (tipeLower.contains("sabuk") || tipeLower.contains("belt")) {
            return "Belt & Buckle";
        } else if (tipeLower.contains("tas") || tipeLower.contains("bag")) {
            return "Bag & Pouch";
        } else if (tipeLower.contains("syal") || tipeLower.contains("scarf")) {
            return "Scarf & Shawl";
        } else if (tipeLower.contains("dompet") || tipeLower.contains("wallet")) {
            return "Wallet";
        } else {
            return "Aksesoris Fashion";
        }
    }
    
    /**
     * Method untuk cek apakah branded item
     */
    public boolean isBrandedItem() {
        return brand != null && !brand.isEmpty() && !brand.equalsIgnoreCase("no brand");
    }
    
    @Override
    public String toString() {
        return String.format("Aksesoris: %s - %s [%s - %s] (Rp %.0f) - Stok: %d", 
                           kodeProduk, namaProduk, tipe, brand, harga, getStok());
    }
}