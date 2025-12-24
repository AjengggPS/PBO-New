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
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Class Kasir
 * Menerapkan konsep Encapsulation
 */
public class Kasir {
    
    // Atribut private
    private int id;
    private String namaKasir;
    private String shift;
    private Timestamp waktuMulai;
    private Timestamp waktuSelesai;
    private String status;
    private Timestamp createdAt;
    
    // Constructor
    public Kasir() {}
    
    public Kasir(String namaKasir, String shift) {
        this.namaKasir = namaKasir;
        this.shift = shift;
        this.status = "aktif";
    }
    
    public Kasir(int id, String namaKasir, String shift, String status) {
        this.id = id;
        this.namaKasir = namaKasir;
        this.shift = shift;
        this.status = status;
    }
    
    // === ENCAPSULATION: Getter & Setter ===
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNamaKasir() {
        return namaKasir;
    }
    
    public void setNamaKasir(String namaKasir) {
        if (namaKasir != null && !namaKasir.trim().isEmpty()) {
            this.namaKasir = namaKasir.trim();
        } else {
            throw new IllegalArgumentException("Nama kasir tidak boleh kosong");
        }
    }
    
    public String getShift() {
        return shift;
    }
    
    public void setShift(String shift) {
        if (isValidShift(shift)) {
            this.shift = shift;
        } else {
            throw new IllegalArgumentException("Shift tidak valid. Pilih: Pagi, Siang, atau Malam");
        }
    }
    
    public Timestamp getWaktuMulai() {
        return waktuMulai;
    }
    
    public void setWaktuMulai(Timestamp waktuMulai) {
        this.waktuMulai = waktuMulai;
    }
    
    public Timestamp getWaktuSelesai() {
        return waktuSelesai;
    }
    
    public void setWaktuSelesai(Timestamp waktuSelesai) {
        this.waktuSelesai = waktuSelesai;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    // === BUSINESS METHODS ===
    
    /**
     * Validasi shift
     */
    private boolean isValidShift(String shift) {
        return shift != null && 
               (shift.equalsIgnoreCase("Pagi") || 
                shift.equalsIgnoreCase("Siang") || 
                shift.equalsIgnoreCase("Malam"));
    }
    
    /**
     * Start shift
     */
    public void startShift() {
        this.waktuMulai = new Timestamp(System.currentTimeMillis());
        this.status = "aktif";
    }
    
    /**
     * End shift
     */
    public void endShift() {
        this.waktuSelesai = new Timestamp(System.currentTimeMillis());
        this.status = "selesai";
    }
    
    /**
     * Cek apakah kasir sedang aktif
     */
    public boolean isAktif() {
        return "aktif".equalsIgnoreCase(status);
    }
    
    /**
     * Hitung durasi kerja (dalam jam)
     */
    public long getDurasiKerja() {
        if (waktuMulai == null) return 0;
        
        Timestamp end = waktuSelesai != null ? waktuSelesai : new Timestamp(System.currentTimeMillis());
        
        LocalDateTime start = waktuMulai.toLocalDateTime();
        LocalDateTime endTime = end.toLocalDateTime();
        
        Duration duration = Duration.between(start, endTime);
        return duration.toHours();
    }
    
    /**
     * Get jam kerja shift
     */
    public String getJamKerjaShift() {
        switch (shift.toLowerCase()) {
            case "pagi":
                return "08:00 - 14:00";
            case "siang":
                return "14:00 - 20:00";
            case "malam":
                return "20:00 - 02:00";
            default:
                return "Tidak diketahui";
        }
    }
    
    /**
     * Get initial nama untuk display
     */
    public String getInitial() {
        if (namaKasir == null || namaKasir.isEmpty()) return "?";
        
        String[] words = namaKasir.split("\\s+");
        if (words.length >= 2) {
            return String.valueOf(words[0].charAt(0)) + words[1].charAt(0);
        } else {
            return String.valueOf(namaKasir.charAt(0));
        }
    }
    
    /**
     * Validasi data kasir
     */
    public boolean validate() {
        return namaKasir != null && !namaKasir.trim().isEmpty() && isValidShift(shift);
    }
    
    @Override
    public String toString() {
        return String.format("Kasir[ID=%d, Nama=%s, Shift=%s, Status=%s]", 
                           id, namaKasir, shift, status);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Kasir that = (Kasir) obj;
        return id == that.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
