<%-- 
    Document   : struk
    Author     : Ajeng Puspita Sari (Updated for Custom SQL)
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.fashionhub.util.DBConnection" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%
    // Cek Session Login
    if (session.getAttribute("namaKasir") == null) {
        response.sendRedirect("../index.jsp");
        return;
    }

    String idParam = request.getParameter("id");
    
    // Validasi ID tidak boleh kosong
    if (idParam == null || idParam.isEmpty()) {
        out.println("<h3>ID Transaksi tidak valid.</h3>");
        return;
    }

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    // Variabel penampung data
    String noTransaksi = "", namaKasir = "", namaPelanggan = "Umum", metodeBayar = "", tanggal = "";
    double totalBelanja = 0, diskon = 0, totalBayar = 0, jumlahBayar = 0, kembalian = 0;
    boolean dataFound = false;
    
    try {
        conn = DBConnection.getConnection();
        
        // SQL Update: nama_pelanggan dan waktu_transaksi
        String sql = "SELECT t.*, k.nama_kasir, p.nama_pelanggan FROM transaksi t " +
                     "JOIN kasir k ON t.kasir_id = k.id " +
                     "LEFT JOIN pelanggan p ON t.pelanggan_id = p.id WHERE t.id = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, Integer.parseInt(idParam));
        rs = pstmt.executeQuery();
        
        if(rs.next()){
            dataFound = true;
            noTransaksi = rs.getString("no_transaksi");
            namaKasir = rs.getString("nama_kasir");
            if(rs.getString("nama_pelanggan") != null) namaPelanggan = rs.getString("nama_pelanggan");
            metodeBayar = rs.getString("metode_bayar");
            totalBelanja = rs.getDouble("total_belanja");
            diskon = rs.getDouble("diskon");
            totalBayar = rs.getDouble("total_bayar");
            jumlahBayar = rs.getDouble("jumlah_bayar");
            kembalian = rs.getDouble("kembalian");
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            // SQL Update: kolom waktu_transaksi
            Timestamp ts = rs.getTimestamp("waktu_transaksi");
            tanggal = (ts != null) ? sdf.format(ts) : "-";
        }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Struk - FashionHub</title>
    <style>
    body { font-family: 'Courier New', monospace; background: #FFDEE2; padding: 20px; }
    
    .struk { 
        max-width: 350px; background: white; margin: 0 auto; padding: 25px; 
        text-align: center; box-shadow: 0 10px 25px rgba(112, 2, 15, 0.1); border-radius: 5px; 
        position: relative;
    }
    /* Efek gerigi kertas struk (opsional, biar cantik) */
    .struk::before {
        content: ""; position: absolute; top: -5px; left: 0; width: 100%; height: 10px; 
        background: radial-gradient(circle, transparent, transparent 50%, white 50%, white); background-size: 10px 10px;
    }

    .dashed { border-bottom: 1px dashed #333; margin: 12px 0; }
    .row { display: flex; justify-content: space-between; font-size: 12px; margin-bottom: 6px; }
    .total-row { font-weight: bold; font-size: 14px; margin-top: 8px; }
    
    .btn-area { margin-top: 30px; text-align: center; }
    .btn { padding: 12px 25px; border: none; border-radius: 30px; cursor: pointer; text-decoration: none; font-family: 'Poppins', sans-serif; font-weight: bold; display: inline-block; margin: 5px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); transition: 0.2s; }
    
    .btn-print { background: white; color: #70020F; border: 2px solid #70020F; }
    .btn-print:hover { background: #fff0f2; }
    
    .btn-finish { background: #70020F; color: white; border: 2px solid #70020F; }
    .btn-finish:hover { background: #52000a; border-color: #52000a; }
    
    .error-msg { text-align: center; margin-top: 50px; color: #70020F; font-family: sans-serif; }
    
    @media print {
        body { background: white; }
        .btn-area { display: none; }
        .struk { box-shadow: none; width: 100%; max-width: 100%; margin: 0; padding: 0; border-radius: 0; }
        .struk::before { display: none; }
    }
</style>

</head>
<body>

    <% if (dataFound) { %>
    
    <div class="struk">
        <h3>FASHIONHUB</h3>
        <p style="font-size:12px;">Jl. Fashion No. 123, Jakarta</p>
        <div class="dashed"></div>
        
        <div class="row"><span>No: <%= noTransaksi %></span> <span><%= tanggal %></span></div>
        <div class="row"><span>Kasir: <%= namaKasir %></span></div>
        <div class="row"><span>Plg: <%= namaPelanggan %></span></div>
        
        <div class="dashed"></div>
        
        <%
            PreparedStatement pstmt2 = null;
            ResultSet rs2 = null;
            try {
                // SQL Detail Transaksi (Sama seperti schema, kolom nama_produk ada)
                pstmt2 = conn.prepareStatement("SELECT * FROM detail_transaksi WHERE transaksi_id = ?");
                pstmt2.setInt(1, Integer.parseInt(idParam));
                rs2 = pstmt2.executeQuery();
                while(rs2.next()){
        %>
        <div style="text-align:left; font-size:12px;"><%= rs2.getString("nama_produk") %></div>
        <div class="row">
            <span><%= rs2.getInt("jumlah") %> x <%= String.format("%,.0f", rs2.getDouble("harga")) %></span>
            <span><%= String.format("%,.0f", rs2.getDouble("subtotal")) %></span>
        </div>
        <% 
                } 
            } catch(Exception e) {
                out.print("Error item: " + e.getMessage());
            } finally {
                if(rs2 != null) rs2.close();
                if(pstmt2 != null) pstmt2.close();
            }
        %>
        
        <div class="dashed"></div>
        
        <div class="row"><span>Subtotal:</span> <span><%= String.format("%,.0f", totalBelanja) %></span></div>
        <% if(diskon > 0) { %>
        <div class="row"><span>Diskon:</span> <span>-<%= String.format("%,.0f", diskon) %></span></div>
        <% } %>
        <div class="row total-row"><span>TOTAL:</span> <span><%= String.format("%,.0f", totalBayar) %></span></div>
        
        <div class="dashed"></div>
        
        <div class="row"><span>Bayar (<%= metodeBayar %>):</span> <span><%= String.format("%,.0f", jumlahBayar) %></span></div>
        <div class="row"><span>Kembali:</span> <span><%= String.format("%,.0f", kembalian) %></span></div>
        
        <div class="dashed"></div>
        <p style="font-size:12px;">Terima Kasih atas Kunjungan Anda</p>
        <p style="font-size:12px;">Barang yang dibeli tidak dapat ditukar</p>
    </div>

    <div class="btn-area">
        <button onclick="window.print()" class="btn btn-print">🖨️ Cetak</button>
        <a href="riwayat-transaksi.jsp?status=success" class="btn btn-finish">✅ Selesai & Lanjut</a>
    </div>

    <% } else { %>
        <div class="error-msg">
            <h3>Transaksi Tidak Ditemukan</h3>
            <p>Mohon periksa kembali ID transaksi anda.</p>
            <a href="buat-transaksi.jsp" class="btn btn-print">Kembali</a>
        </div>
    <% } %>

</body>
</html>
<%
    } catch(Exception e){ 
        e.printStackTrace();
        out.println("<div class='error-msg'>Terjadi kesalahan sistem: " + e.getMessage() + "</div>");
    } finally { 
        if(rs != null) try { rs.close(); } catch(Exception e){}
        if(pstmt != null) try { pstmt.close(); } catch(Exception e){}
        DBConnection.closeConnection(conn); 
    }
%>