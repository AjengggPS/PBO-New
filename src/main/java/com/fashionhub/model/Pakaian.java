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
 * Abstract Class Pakaian
 * Class dasar untuk semua jenis produk pakaian
 * Menerapkan konsep Encapsulation & Abstraction
 */
public abstract class Pakaian {
    
    // Atribut protected - bisa diakses subclass
    protected int id;
    protected String kodeProduk;
    protected String namaProduk;
    protected double harga;
    protected String ukuran;
    
    // Atribut private - hanya diakses melalui getter/setter
    private int stok;
    
    // Constructor
    public Pakaian() {}
    
    public Pakaian(String kodeProduk, String namaProduk, double harga, 
                   String ukuran, int stok) {
        this.kodeProduk = kodeProduk;
        this.namaProduk = namaProduk;
        this.harga = harga;
        this.ukuran = ukuran;
        this.stok = stok;
    }
    
    // === ENCAPSULATION: Getter & Setter ===
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getKodeProduk() {
        return kodeProduk;
    }
    
    public void setKodeProduk(String kodeProduk) {
        this.kodeProduk = kodeProduk;
    }
    
    public String getNamaProduk() {
        return namaProduk;
    }
    
    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }
    
    public double getHarga() {
        return harga;
    }
    
    public void setHarga(double harga) {
        if (harga >= 0) {
            this.harga = harga;
        } else {
            throw new IllegalArgumentException("Harga tidak boleh negatif");
        }
    }
    
    public String getUkuran() {
        return ukuran;
    }
    
    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }
    
    public int getStok() {
        return stok;
    }
    
    public void setStok(int stok) {
        if (stok >= 0) {
            this.stok = stok;
        } else {
            throw new IllegalArgumentException("Stok tidak boleh negatif");
        }
    }
    
    // === BUSINESS METHODS ===
    
    /**
     * Method untuk mengurangi stok
     * @param jumlah jumlah yang akan dikurangi
     * @return true jika berhasil, false jika stok tidak cukup
     */
    public boolean kurangiStok(int jumlah) {
        if (this.stok >= jumlah) {
            this.stok -= jumlah;
            return true;
        }
        return false;
    }
    
    /**
     * Method untuk menambah stok
     * @param jumlah jumlah yang akan ditambah
     */
    public void tambahStok(int jumlah) {
        if (jumlah > 0) {
            this.stok += jumlah;
        }
    }
    
    /**
     * Method untuk cek apakah stok tersedia
     * @param jumlah jumlah yang diminta
     * @return true jika stok mencukupi
     */
    public boolean cekStokTersedia(int jumlah) {
        return this.stok >= jumlah;
    }
    
    // === ABSTRACT METHODS - Harus diimplementasi subclass ===
    
    /**
     * Method abstract untuk hitung diskon
     * Setiap kategori produk bisa punya aturan diskon berbeda
     * @return harga setelah diskon
     */
    public abstract double hitungDiskon();
    
    /**
     * Method abstract untuk deskripsi produk
     * Setiap kategori produk punya deskripsi berbeda
     * @return deskripsi lengkap produk
     */
    public abstract String deskripsiProduk();
    
    /**
     * Method abstract untuk kategori
     * @return kategori produk (Baju/Celana/Aksesoris)
     */
    public abstract String getKategori();
    
    // === COMMON METHODS ===
    
    @Override
    public String toString() {
        return String.format("%s - %s (Rp %.0f) - Stok: %d", 
                           kodeProduk, namaProduk, harga, stok);
    }
}