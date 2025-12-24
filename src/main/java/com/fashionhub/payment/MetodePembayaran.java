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
 * Interface MetodePembayaran
 * Menerapkan konsep Interface untuk polymorphism
 * Semua metode pembayaran harus implement interface ini
 */
public interface MetodePembayaran {
    
    /**
     * Method untuk memproses pembayaran
     * @param total total yang harus dibayar
     * @return true jika pembayaran berhasil, false jika gagal
     */
    boolean prosesPembayaran(double total);
    
    /**
     * Method untuk mendapatkan nama metode pembayaran
     * @return nama metode (Cash, QRIS, dll)
     */
    String getNamaMetode();
    
    /**
     * Method untuk validasi pembayaran
     * @param total total pembayaran
     * @param jumlahBayar jumlah uang yang diterima
     * @return true jika valid
     */
    boolean validasiPembayaran(double total, double jumlahBayar);
    
    /**
     * Method untuk menghitung kembalian (opsional, bisa return 0)
     * @param total total pembayaran
     * @param jumlahBayar jumlah uang yang diterima
     * @return kembalian
     */
    double hitungKembalian(double total, double jumlahBayar);
}