<%-- 
    Document   : list-pelanggan
    Created on : 15 Dec 2025, 22.59.52
    Author     : Ajeng Puspita Sari
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.fashionhub.util.DBConnection" %>
<%
    if (session.getAttribute("namaKasir") == null) {
        response.sendRedirect("../index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Manajemen Pelanggan - FashionHub</title>
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
            if (confirm('Hapus pelanggan "' + nama + '"?')) {
                window.location.href = '../PelangganServlet?action=delete&id=' + id;
            }
        }
        
        function searchPelanggan() {
            var input = document.getElementById('searchInput');
            var filter = input.value.toUpperCase();
            var table = document.getElementById('tablePelanggan');
            var tr = table.getElementsByTagName('tr');
            
            for (var i = 1; i < tr.length; i++) {
                var tdNama = tr[i].getElementsByTagName('td')[0];
                var tdHp = tr[i].getElementsByTagName('td')[1];
                if (tdNama || tdHp) {
                    var txtNama = tdNama.textContent || tdNama.innerText;
                    var txtHp = tdHp.textContent || tdHp.innerText;
                    if (txtNama.toUpperCase().indexOf(filter) > -1 || 
                        txtHp.toUpperCase().indexOf(filter) > -1) {
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
    <div class="header">
        <h2>👥 Manajemen Pelanggan</h2>
        <a href="../dashboard.jsp" class="btn-back">← Kembali</a>
    </div>
    
    <div class="container">
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
        
        <div class="top-section">
            <div class="search-box">
                <input type="text" id="searchInput" 
                       placeholder="🔍 Cari nama atau nomor HP..." 
                       onkeyup="searchPelanggan()">
            </div>
            <a href="tambah-pelanggan.jsp" class="btn-add">+ Tambah Pelanggan</a>
        </div>
        
        <div class="card">
            <table id="tablePelanggan">
                <thead>
                    <tr>
                        <th>Nama Pelanggan</th>
                        <th>No. HP</th>
                        <th>Alamat</th>
                        <th>Terdaftar</th>
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
                        rs = stmt.executeQuery("SELECT * FROM pelanggan ORDER BY created_at DESC");
                        
                        boolean hasData = false;
                        while (rs.next()) {
                            hasData = true;
                            int id = rs.getInt("id");
                            String namaPelanggan = rs.getString("nama_pelanggan");
                            String noHp = rs.getString("no_hp");
                            String alamat = rs.getString("alamat");
                            Timestamp createdAt = rs.getTimestamp("created_at");
                            
                            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy");
                            String tanggal = sdf.format(createdAt);
                    %>
                    <tr>
                        <td><strong><%= namaPelanggan %></strong></td>
                        <td><%= noHp != null ? noHp : "-" %></td>
                        <td><%= alamat != null ? (alamat.length() > 50 ? alamat.substring(0, 50) + "..." : alamat) : "-" %></td>
                        <td><%= tanggal %></td>
                        <td>
                            <a href="edit-pelanggan.jsp?id=<%= id %>" class="btn-action btn-edit">Edit</a>
                            <button onclick="confirmDelete(<%= id %>, '<%= namaPelanggan %>')" 
                                    class="btn-action btn-delete">Hapus</button>
                        </td>
                    </tr>
                    <%
                        }
                        
                        if (!hasData) {
                    %>
                    <tr>
                        <td colspan="5">
                            <div class="empty-state">
                                <div class="icon">👥</div>
                                <h3>Belum ada data pelanggan</h3>
                                <p>Klik tombol "Tambah Pelanggan" untuk menambah data</p>
                            </div>
                        </td>
                    </tr>
                    <%
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    %>
                    <tr>
                        <td colspan="5" style="color: red; text-align: center;">
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