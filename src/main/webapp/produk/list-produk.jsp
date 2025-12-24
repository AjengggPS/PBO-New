<%-- 
    Document   : list-produk
    Created on : 15 Dec 2025, 22.53.46
    Author     : Ajeng Puspita Sari
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.fashionhub.util.DBConnection" %>
<%
    // Cek session kasir
    if (session.getAttribute("namaKasir") == null) {
        response.sendRedirect("../index.jsp");
        return;
    }
    String namaKasir = (String) session.getAttribute("namaKasir");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manajemen Produk - FashionHub</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Poppins', sans-serif; background: #FFDEE2; color: #333; }
        
        /* Header Rosewood */
        .header {
            background: #70020F;
            color: white;
            padding: 15px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        .header h2 { font-size: 24px; font-weight: 600; }
        .btn-back {
            background: rgba(255,255,255,0.2);
            color: white;
            padding: 8px 15px;
            border-radius: 20px;
            text-decoration: none;
            font-size: 14px;
            border: 1px solid rgba(255,255,255,0.4);
            transition: 0.3s;
        }
        .btn-back:hover { background: rgba(255,255,255,0.3); }
        
        .container {
            max-width: 1200px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .top-section {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .search-box input {
            padding: 12px 15px;
            border: 2px solid #FFDEE2;
            border-radius: 10px;
            width: 300px;
            font-size: 14px;
            outline: none;
        }
        .search-box input:focus { border-color: #70020F; }
        
        .btn-add {
            background: #70020F;
            color: white;
            padding: 12px 25px;
            border-radius: 10px;
            text-decoration: none;
            font-weight: bold;
            transition: background 0.3s;
            box-shadow: 0 4px 10px rgba(112, 2, 15, 0.2);
        }
        .btn-add:hover { background: #52000a; }
        
        .card {
            background: white;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(112, 2, 15, 0.05);
            overflow: hidden;
        }
        
        table {
            width: 100%;
            border-collapse: collapse;
        }
        thead {
            background: #70020F;
            color: white;
        }
        th, td {
            padding: 15px;
            text-align: left;
        }
        th { font-weight: 600; }
        tbody tr { border-bottom: 1px solid #FFDEE2; }
        tbody tr:hover { background: #fff0f2; }
        
        /* Badges */
        .badge {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
        }
        .badge-baju { background: #e3f2fd; color: #1976d2; }
        .badge-celana { background: #f3e5f5; color: #7b1fa2; }
        .badge-aksesoris { background: #fff3e0; color: #e65100; }
        
        .stok { font-weight: bold; }
        .stok-low { color: #e74c3c; }
        .stok-ok { color: #27ae60; }
        
        /* Tombol Aksi */
        .btn-action {
            padding: 6px 12px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 12px;
            margin-right: 5px;
            text-decoration: none;
            display: inline-block;
            font-weight: bold;
        }
        .btn-edit {
            background: #fff;
            border: 1px solid #70020F;
            color: #70020F;
        }
        .btn-edit:hover { background: #70020F; color: white; }
        
        .btn-delete {
            background: #e74c3c;
            color: white;
        }
        .btn-delete:hover { background: #c0392b; }
        
        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: #999;
        }
        .empty-state .icon {
            font-size: 64px;
            margin-bottom: 20px;
        }
        
        .alert {
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
        }
        .alert-success {
            background: #fff; border-left: 5px solid #70020F; color: #70020F;
        }
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
    <script>
        function confirmDelete(id, nama) {
            if (confirm('Hapus produk "' + nama + '"?')) {
                window.location.href = '../ProdukServlet?action=delete&id=' + id;
            }
        }
        
        function searchProduk() {
            var input = document.getElementById('searchInput');
            var filter = input.value.toUpperCase();
            var table = document.getElementById('tableProduk');
            var tr = table.getElementsByTagName('tr');
            
            for (var i = 1; i < tr.length; i++) {
                var tdKode = tr[i].getElementsByTagName('td')[0];
                var tdNama = tr[i].getElementsByTagName('td')[1];
                if (tdKode || tdNama) {
                    var txtKode = tdKode.textContent || tdKode.innerText;
                    var txtNama = tdNama.textContent || tdNama.innerText;
                    if (txtKode.toUpperCase().indexOf(filter) > -1 || 
                        txtNama.toUpperCase().indexOf(filter) > -1) {
                        tr[i].style.display = '';
                    } else {
                        tr[i].style.display = 'none';
                    }
                }
            }
        }
    </script>
</head>
<body>
    <!-- Header -->
    <div class="header">
        <h2>📦 Manajemen Produk</h2>
        <a href="../dashboard.jsp" class="btn-back">← Kembali</a>
    </div>
    
    <div class="container">
        <!-- Alert Messages -->
        <% 
        String message = request.getParameter("message");
        String error = request.getParameter("error");
        if (message != null) { 
        %>
            <div class="alert alert-success">✅ <%= message %></div>
        <% } 
        if (error != null) { 
        %>
            <div class="alert alert-error">⚠️ <%= error %></div>
        <% } %>
        
        <!-- Top Section -->
        <div class="top-section">
            <div class="search-box">
                <input type="text" id="searchInput" 
                       placeholder="🔍 Cari kode atau nama produk..." 
                       onkeyup="searchProduk()">
            </div>
            <a href="tambah-produk.jsp" class="btn-add">+ Tambah Produk</a>
        </div>
        
        <!-- Tabel Produk -->
        <div class="card">
            <table id="tableProduk">
                <thead>
                    <tr>
                        <th>Kode</th>
                        <th>Nama Produk</th>
                        <th>Kategori</th>
                        <th>Ukuran</th>
                        <th>Warna</th>
                        <th>Harga</th>
                        <th>Stok</th>
                        <th>Aksi</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                    Connection conn = null;
                    Statement stmt = null;
                    ResultSet rs = null;
                    try {
                        conn = DBConnection.getConnection();
                        stmt = conn.createStatement();
                        rs = stmt.executeQuery("SELECT * FROM produk ORDER BY created_at DESC");
                        
                        boolean hasData = false;
                        while (rs.next()) {
                            hasData = true;
                            int id = rs.getInt("id");
                            String kodeProduk = rs.getString("kode_produk");
                            String namaProduk = rs.getString("nama_produk");
                            String kategori = rs.getString("kategori");
                            String ukuran = rs.getString("ukuran");
                            String warna = rs.getString("warna");
                            double harga = rs.getDouble("harga");
                            int stok = rs.getInt("stok");
                            
                            String badgeClass = "";
                            if (kategori.equals("Baju")) badgeClass = "badge-baju";
                            else if (kategori.equals("Celana")) badgeClass = "badge-celana";
                            else badgeClass = "badge-aksesoris";
                            
                            String stokClass = stok < 10 ? "stok-low" : "stok-ok";
                    %>
                    <tr>
                        <td><strong><%= kodeProduk %></strong></td>
                        <td><%= namaProduk %></td>
                        <td><span class="badge <%= badgeClass %>"><%= kategori %></span></td>
                        <td><%= ukuran != null ? ukuran : "-" %></td>
                        <td><%= warna != null ? warna : "-" %></td>
                        <td>Rp <%= String.format("%,.0f", harga) %></td>
                        <td><span class="stok <%= stokClass %>"><%= stok %></span></td>
                        <td>
                            <a href="edit-produk.jsp?id=<%= id %>" class="btn-action btn-edit">Edit</a>
                            <button onclick="confirmDelete(<%= id %>, '<%= namaProduk %>')" 
                                    class="btn-action btn-delete">Hapus</button>
                        </td>
                    </tr>
                    <%
                        }
                        
                        if (!hasData) {
                    %>
                    <tr>
                        <td colspan="8">
                            <div class="empty-state">
                                <div class="icon">📦</div>
                                <h3>Belum ada produk</h3>
                                <p>Klik tombol "Tambah Produk" untuk menambah data</p>
                            </div>
                        </td>
                    </tr>
                    <%
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    %>
                    <tr>
                        <td colspan="8" style="color: red; text-align: center;">
                            Error: <%= e.getMessage() %>
                        </td>
                    </tr>
                    <%
                    } finally {
                        if (rs != null) rs.close();
                        if (stmt != null) stmt.close();
                        DBConnection.closeConnection(conn);
                    }
                    %>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>