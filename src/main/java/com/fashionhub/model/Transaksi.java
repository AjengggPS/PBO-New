/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fashionhub.model;

/**
 *
 * @author Ajeng Puspita Sari
 */

import com.fashionhub.payment.MetodePembayaran;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Transaksi
 * Menerapkan konsep Encapsulation, Association, dan Composition
 */
public class Transaksi {
    
    // Atribut private
    private int id;
    private String noTransaksi;
    private int kasirId;
    private Integer pelangganId; // Nullable
    private double totalBelanja;
    private double diskon;
    private double totalBayar;
    private String metodeBayar;
    private double jumlahBayar;
    private double kembalian;
    private String status;
    private Timestamp waktuTransaksi;
    
    // Composition: Transaksi memiliki list detail items
    private List<DetailTransaksi> daftarItem;
    
    // Association: Transaksi terhubung dengan objek lain
    private Kasir kasir;
    private Pelanggan pelanggan;
    private MetodePembayaran paymentMethod;
    
    // Constructor
    public Transaksi() {
        this.daftarItem = new ArrayList<>();
        this.status = "pending";
    }
    
    public Transaksi(String noTransaksi, int kasirId) {
        this();
        this.noTransaksi = noTransaksi;
        this.kasirId = kasirId;
    }
    
    // === ENCAPSULATION: Getter & Setter ===
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNoTransaksi() {
        return noTransaksi;
    }
    
    public void setNoTransaksi(String noTransaksi) {
        this.noTransaksi = noTransaksi;
    }
    
    public int getKasirId() {
        return kasirId;
    }
    
    public void setKasirId(int kasirId) {
        this.kasirId = kasirId;
    }
    
    public Integer getPelangganId() {
        return pelangganId;
    }
    
    public void setPelangganId(Integer pelangganId) {
        this.pelangganId = pelangganId;
    }
    
    public double getTotalBelanja() {
        return totalBelanja;
    }
    
    public void setTotalBelanja(double totalBelanja) {
        this.totalBelanja = totalBelanja;
    }
    
    public double getDiskon() {
        return diskon;
    }
    
    public void setDiskon(double diskon) {
        this.diskon = diskon;
    }
    
    public double getTotalBayar() {
        return totalBayar;
    }
    
    public void setTotalBayar(double totalBayar) {
        this.totalBayar = totalBayar;
    }
    
    public String getMetodeBayar() {
        return metodeBayar;
    }
    
    public void setMetodeBayar(String metodeBayar) {
        this.metodeBayar = metodeBayar;
    }
    
    public double getJumlahBayar() {
        return jumlahBayar;
    }
    
    public void setJumlahBayar(double jumlahBayar) {
        this.jumlahBayar = jumlahBayar;
    }
    
    public double getKembalian() {
        return kembalian;
    }
    
    public void setKembalian(double kembalian) {
        this.kembalian = kembalian;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getWaktuTransaksi() {
        return waktuTransaksi;
    }
    
    public void setWaktuTransaksi(Timestamp waktuTransaksi) {
        this.waktuTransaksi = waktuTransaksi;
    }
    
    public List<DetailTransaksi> getDaftarItem() {
        return daftarItem;
    }
    
    public void setDaftarItem(List<DetailTransaksi> daftarItem) {
        this.daftarItem = daftarItem;
    }
    
    public Kasir getKasir() {
        return kasir;
    }
    
    public void setKasir(Kasir kasir) {
        this.kasir = kasir;
        if (kasir != null) {
            this.kasirId = kasir.getId();
        }
    }
    
    public Pelanggan getPelanggan() {
        return pelanggan;
    }
    
    public void setPelanggan(Pelanggan pelanggan) {
        this.pelanggan = pelanggan;
        if (pelanggan != null) {
            this.pelangganId = pelanggan.getId();
        }
    }
    
    public MetodePembayaran getPaymentMethod() {
        return paymentMethod;
    }
    
    public void setPaymentMethod(MetodePembayaran paymentMethod) {
        this.paymentMethod = paymentMethod;
        if (paymentMethod != null) {
            this.metodeBayar = paymentMethod.getNamaMetode();
        }
    }
    
    // === BUSINESS METHODS ===
    
    /**
     * Tambah item ke transaksi
     */
    public void tambahItem(Pakaian produk, int jumlah) {
        if (produk == null || jumlah <= 0) {
            throw new IllegalArgumentException("Produk atau jumlah tidak valid");
        }
        
        if (!produk.cekStokTersedia(jumlah)) {
            throw new IllegalStateException("Stok tidak mencukupi untuk " + produk.getNamaProduk());
        }
        
        DetailTransaksi detail = new DetailTransaksi();
        detail.setProdukId(produk.getId());
        detail.setNamaProduk(produk.getNamaProduk());
        detail.setKategori(produk.getKategori());
        detail.setHarga(produk.getHarga());
        detail.setJumlah(jumlah);
        detail.setSubtotal(produk.getHarga() * jumlah);
        detail.setDiskonItem(produk.getHarga() - produk.hitungDiskon());
        
        daftarItem.add(detail);
    }
    
    /**
     * Hitung total belanja dari semua item
     */
    public void hitungTotal() {
        double total = 0;
        double totalDiskonItem = 0;
        
        for (DetailTransaksi item : daftarItem) {
            total += item.getSubtotal();
            totalDiskonItem += item.getDiskonItem() * item.getJumlah();
        }
        
        this.totalBelanja = total;
        this.totalBayar = total - this.diskon;
    }
    
    /**
     * Set metode pembayaran menggunakan interface
     */
    public void setMetodePembayaran(MetodePembayaran metode) {
        this.paymentMethod = metode;
        this.metodeBayar = metode.getNamaMetode();
    }
    
    /**
     * Proses pembayaran menggunakan interface
     */
    public boolean prosesPembayaran() {
        if (paymentMethod == null) {
            throw new IllegalStateException("Metode pembayaran belum dipilih");
        }
        
        if (daftarItem.isEmpty()) {
            throw new IllegalStateException("Tidak ada item dalam transaksi");
        }
        
        hitungTotal();
        
        boolean success = paymentMethod.prosesPembayaran(this.totalBayar);
        
        if (success) {
            this.status = "success";
            this.kembalian = paymentMethod.hitungKembalian(this.totalBayar, this.jumlahBayar);
            this.waktuTransaksi = new Timestamp(System.currentTimeMillis());
        }
        
        return success;
    }
    
    /**
     * Generate nomor transaksi otomatis
     */
    public static String generateNoTransaksi() {
        return "TRX" + System.currentTimeMillis();
    }
    
    /**
     * Cetak nota/struk
     */
    public String cetakNota() {
        StringBuilder nota = new StringBuilder();
        nota.append("========================================\n");
        nota.append("           FASHIONHUB\n");
        nota.append("     Jl. Fashion No. 123, Jakarta\n");
        nota.append("========================================\n");
        nota.append("No: ").append(noTransaksi).append("\n");
        nota.append("Tanggal: ").append(waktuTransaksi).append("\n");
        nota.append("Kasir: ").append(kasir != null ? kasir.getNamaKasir() : "-").append("\n");
        nota.append("Pelanggan: ").append(pelanggan != null ? pelanggan.getNamaPelanggan() : "Umum").append("\n");
        nota.append("========================================\n");
        
        for (DetailTransaksi item : daftarItem) {
            nota.append(item.getNamaProduk()).append("\n");
            nota.append(String.format("  %dx @ Rp %,.0f = Rp %,.0f\n", 
                item.getJumlah(), item.getHarga(), item.getSubtotal()));
        }
        
        nota.append("========================================\n");
        nota.append(String.format("Subtotal:   Rp %,.0f\n", totalBelanja));
        if (diskon > 0) {
            nota.append(String.format("Diskon:    -Rp %,.0f\n", diskon));
        }
        nota.append(String.format("TOTAL:      Rp %,.0f\n", totalBayar));
        nota.append("========================================\n");
        nota.append(String.format("Bayar (%s): Rp %,.0f\n", metodeBayar, jumlahBayar));
        nota.append(String.format("Kembali:    Rp %,.0f\n", kembalian));
        nota.append("========================================\n");
        nota.append("   Terima Kasih atas Kunjungan Anda\n");
        nota.append("========================================\n");
        
        return nota.toString();
    }
    
    /**
     * Validasi transaksi sebelum disimpan
     */
    public boolean validate() {
        return noTransaksi != null && !noTransaksi.isEmpty() &&
               kasirId > 0 &&
               !daftarItem.isEmpty() &&
               totalBayar > 0 &&
               metodeBayar != null;
    }
    
    /**
     * Get jumlah total item
     */
    public int getTotalItem() {
        int total = 0;
        for (DetailTransaksi item : daftarItem) {
            total += item.getJumlah();
        }
        return total;
    }
    
    @Override
    public String toString() {
        return String.format("Transaksi[%s, Total: Rp %,.0f, Status: %s]", 
                           noTransaksi, totalBayar, status);
    }
    
    // === INNER CLASS: DetailTransaksi ===
    
    public static class DetailTransaksi {
        private int id;
        private int transaksiId;
        private int produkId;
        private String namaProduk;
        private String kategori;
        private double harga;
        private int jumlah;
        private double subtotal;
        private double diskonItem;
        
        // Getter & Setter
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }
        
        public int getTransaksiId() { return transaksiId; }
        public void setTransaksiId(int transaksiId) { this.transaksiId = transaksiId; }
        
        public int getProdukId() { return produkId; }
        public void setProdukId(int produkId) { this.produkId = produkId; }
        
        public String getNamaProduk() { return namaProduk; }
        public void setNamaProduk(String namaProduk) { this.namaProduk = namaProduk; }
        
        public String getKategori() { return kategori; }
        public void setKategori(String kategori) { this.kategori = kategori; }
        
        public double getHarga() { return harga; }
        public void setHarga(double harga) { this.harga = harga; }
        
        public int getJumlah() { return jumlah; }
        public void setJumlah(int jumlah) { this.jumlah = jumlah; }
        
        public double getSubtotal() { return subtotal; }
        public void setSubtotal(double subtotal) { this.subtotal = subtotal; }
        
        public double getDiskonItem() { return diskonItem; }
        public void setDiskonItem(double diskonItem) { this.diskonItem = diskonItem; }
        
        @Override
        public String toString() {
            return String.format("%s x%d = Rp %,.0f", namaProduk, jumlah, subtotal);
        }
    }
}