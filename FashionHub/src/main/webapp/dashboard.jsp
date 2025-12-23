<%-- 
    Document   : dashboard
    Created on : 15 Dec 2025, 22.49.10
    Author     : Ajeng Puspita Sari
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
<%
    // Cek apakah kasir sudah start shift
    if (session.getAttribute("namaKasir") == null) {
        response.sendRedirect("index.jsp?error=Silakan start shift terlebih dahulu!");
        return;
    }
    
    // Ambil data kasir dari session
    String namaKasir = (String) session.getAttribute("namaKasir");
    String shift = (String) session.getAttribute("shift");
    Integer kasirId = (Integer) session.getAttribute("kasirId");
    
    // Format waktu
    SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy - HH:mm");
    String waktuSekarang = sdf.format(new Date());
%>
<!DOCTYPE html>
<html>
<head>
    <title>FashionHub - Dashboard Kasir</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Poppins', sans-serif;
            background: #FFDEE2; /* Misty Rose */
            color: #333;
        }
        
        /* Header Rosewood */
        .header {
            background: #70020F;
            color: white;
            padding: 20px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .header-content {
            max-width: 1200px;
            margin: 0 auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .logo h1 {
            font-size: 28px;
            font-weight: 600;
        }
        .kasir-info {
            text-align: right;
        }
        .kasir-info p {
            margin: 5px 0;
        }
        .kasir-info .nama {
            font-size: 18px;
            font-weight: bold;
        }
        .kasir-info .shift-badge {
            display: inline-block;
            background: rgba(255,255,255,0.2);
            padding: 5px 15px;
            border-radius: 20px;
            margin-top: 5px;
            border: 1px solid rgba(255,255,255,0.4);
            font-size: 12px;
        }
        
        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        /* Kartu Selamat Datang */
        .welcome-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(112, 2, 15, 0.05);
            margin-bottom: 30px;
            text-align: center;
            border: 1px solid white;
        }
        .welcome-card h2 {
            color: #70020F;
            margin-bottom: 10px;
        }
        .welcome-card .time {
            color: #999;
            font-size: 14px;
        }
        
        .menu-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
        }
        
        /* Kartu Menu */
        .menu-card {
            background: white;
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(112, 2, 15, 0.05);
            text-align: center;
            cursor: pointer;
            transition: transform 0.3s, box-shadow 0.3s, border-color 0.3s;
            text-decoration: none;
            color: inherit;
            border: 2px solid transparent;
        }
        .menu-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 25px rgba(112, 2, 15, 0.1);
            border-color: #70020F;
        }
        .menu-card .icon {
            font-size: 48px;
            margin-bottom: 15px;
        }
        .menu-card h3 {
            color: #70020F;
            margin-bottom: 10px;
        }
        .menu-card p {
            color: #666;
            font-size: 14px;
        }
        
        /* Tombol Logout */
        .logout-btn {
            position: fixed;
            bottom: 30px;
            right: 30px;
            background: #fff;
            color: #70020F;
            padding: 15px 30px;
            border-radius: 50px;
            text-decoration: none;
            box-shadow: 0 5px 15px rgba(112, 2, 15, 0.2);
            transition: all 0.3s;
            font-weight: bold;
            border: 2px solid #70020F;
        }
        .logout-btn:hover {
            background: #70020F;
            color: white;
        }
    </style>
</head>
<body>
    <!-- Header -->
    <div class="header">
        <div class="header-content">
            <div class="logo">
                <h1>🛍️ FashionHub</h1>
            </div>
            <div class="kasir-info">
                <p class="nama">👤 <%= namaKasir %></p>
                <span class="shift-badge">Shift <%= shift %></span>
                <p style="font-size: 12px; margin-top: 5px; opacity: 0.8;">ID: <%= kasirId %></p>
            </div>
        </div>
    </div>
    
    <!-- Container -->
    <div class="container">
        <!-- Welcome Card -->
        <div class="welcome-card">
            <h2>Selamat Datang, <%= namaKasir %>! 👋</h2>
            <p class="time">📅 <%= waktuSekarang %></p>
        </div>
        
        <!-- Menu Grid -->
        <div class="menu-grid">
            <!-- Menu Produk -->
            <a href="produk/list-produk.jsp" class="menu-card">
                <div class="icon">👕</div>
                <h3>Manajemen Produk</h3>
                <p>Kelola data baju, celana, dan aksesoris</p>
            </a>
            
            <!-- Menu Pelanggan -->
            <a href="pelanggan/list-pelanggan.jsp" class="menu-card">
                <div class="icon">👥</div>
                <h3>Manajemen Pelanggan</h3>
                <p>Kelola data pelanggan toko</p>
            </a>
            
            <!-- Menu Transaksi -->
            <a href="transaksi/buat-transaksi.jsp" class="menu-card">
                <div class="icon">🛒</div>
                <h3>Transaksi Penjualan</h3>
                <p>Buat transaksi penjualan baru</p>
            </a>
            
            <!-- Menu Riwayat -->
            <a href="transaksi/riwayat-transaksi.jsp" class="menu-card">
                <div class="icon">📊</div>
                <h3>Riwayat Transaksi</h3>
                <p>Lihat riwayat transaksi hari ini</p>
            </a>
        </div>
    </div>
    
    <!-- Tombol Logout -->
    <a href="index.jsp" class="logout-btn">🚪 Akhiri Shift</a>
</body>
</html>