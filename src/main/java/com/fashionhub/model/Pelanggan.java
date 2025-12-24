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

/**
 * Class Pelanggan
 * Menerapkan konsep Encapsulation
 */
public class Pelanggan {
    
    // Atribut private - hanya diakses melalui getter/setter
    private int id;
    private String namaPelanggan;
    private String noHp;
    private String alamat;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Constructor
    public Pelanggan() {}
    
    public Pelanggan(String namaPelanggan, String noHp, String alamat) {
        this.namaPelanggan = namaPelanggan;
        this.noHp = noHp;
        this.alamat = alamat;
    }
    
    public Pelanggan(int id, String namaPelanggan, String noHp, String alamat) {
        this.id = id;
        this.namaPelanggan = namaPelanggan;
        this.noHp = noHp;
        this.alamat = alamat;
    }
    
    // === ENCAPSULATION: Getter & Setter ===
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNamaPelanggan() {
        return namaPelanggan;
    }
    
    public void setNamaPelanggan(String namaPelanggan) {
        if (namaPelanggan != null && !namaPelanggan.trim().isEmpty()) {
            this.namaPelanggan = namaPelanggan.trim();
        } else {
            throw new IllegalArgumentException("Nama pelanggan tidak boleh kosong");
        }
    }
    
    public String getNoHp() {
        return noHp;
    }
    
    public void setNoHp(String noHp) {
        this.noHp = noHp != null ? noHp.trim() : null;
    }
    
    public String getAlamat() {
        return alamat;
    }
    
    public void setAlamat(String alamat) {
        this.alamat = alamat != null ? alamat.trim() : null;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // === BUSINESS METHODS ===
    
    /**
     * Validasi nomor HP
     */
    public boolean isNoHpValid() {
        if (noHp == null || noHp.isEmpty()) return false;
        
        // Nomor HP Indonesia harus mulai dengan 08 atau +62
        String cleanNoHp = noHp.replaceAll("[^0-9]", "");
        return cleanNoHp.matches("^(08|628)\\d{8,11}$");
    }
    
    /**
     * Format nomor HP
     */
    public String getFormattedNoHp() {
        if (noHp == null || noHp.isEmpty()) return "-";
        return noHp;
    }
    
    /**
     * Cek apakah pelanggan punya alamat lengkap
     */
    public boolean hasCompleteAddress() {
        return alamat != null && !alamat.trim().isEmpty();
    }
    
    /**
     * Get initial nama untuk display
     */
    public String getInitial() {
        if (namaPelanggan == null || namaPelanggan.isEmpty()) return "?";
        
        String[] words = namaPelanggan.split("\\s+");
        if (words.length >= 2) {
            return String.valueOf(words[0].charAt(0)) + words[1].charAt(0);
        } else {
            return String.valueOf(namaPelanggan.charAt(0));
        }
    }
    
    /**
     * Validasi data pelanggan
     */
    public boolean validate() {
        return namaPelanggan != null && !namaPelanggan.trim().isEmpty();
    }
    
    @Override
    public String toString() {
        return String.format("Pelanggan[ID=%d, Nama=%s, HP=%s]", 
                           id, namaPelanggan, getFormattedNoHp());
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Pelanggan that = (Pelanggan) obj;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}