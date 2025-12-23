<%-- 
    Document   : riwayat-transaksi
    Author     : Ajeng Puspita Sari (Updated for Custom SQL)
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.fashionhub.util.DBConnection" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%
    // 1. Cek Login
    if (session.getAttribute("namaKasir") == null) {
        response.sendRedirect("../index.jsp");
        return;
    }

    // 2. Logika Filter Tanggal (Default: Bulan & Tahun Saat Ini)
    Calendar cal = Calendar.getInstance();
    int selectedBulan = cal.get(Calendar.MONTH) + 1; // Java bulan mulai dari 0, jadi +1
    int selectedTahun = cal.get(Calendar.YEAR);

    String paramBulan = request.getParameter("bulan");
    String paramTahun = request.getParameter("tahun");

    if (paramBulan != null && !paramBulan.isEmpty()) {
        selectedBulan = Integer.parseInt(paramBulan);
    }
    if (paramTahun != null && !paramTahun.isEmpty()) {
        selectedTahun = Integer.parseInt(paramTahun);
    }

    // Array nama bulan untuk dropdown
    String[] namaBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", 
                          "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
%>
<!DOCTYPE html>
<html>
<head>
    <title>Riwayat Transaksi - FashionHub</title>
<style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body { font-family: 'Poppins', sans-serif; background: #FFDEE2; }
    
    .header { background: #70020F; color: white; padding: 15px 20px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
    .container { max-width: 1400px; margin: 30px auto; padding: 0 20px; }
    
    .filter-box {
        background: white; padding: 20px; border-radius: 15px; margin-bottom: 20px;
        box-shadow: 0 4px 15px rgba(112, 2, 15, 0.05); display: flex; align-items: center; gap: 15px;
    }
    .filter-box select { padding: 10px; border: 2px solid #FFDEE2; border-radius: 8px; min-width: 150px; font-size: 14px; outline: none; }
    .filter-box select:focus { border-color: #70020F; }
    
    .btn-filter { background: #70020F; color: white; border: none; padding: 10px 20px; border-radius: 8px; cursor: pointer; font-weight: bold; }
    .btn-filter:hover { background: #52000a; }

    table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 4px 15px rgba(112, 2, 15, 0.05); border-radius: 15px; overflow: hidden; }
    th, td { padding: 15px; text-align: left; border-bottom: 1px solid #FFDEE2; }
    
    /* Header Tabel jadi Rosewood */
    th { background: #70020F; color: white; }
    tr:hover { background: #fff0f2; }
    
    .btn-detail { background: white; border: 1px solid #70020F; color: #70020F; padding: 5px 12px; border-radius: 20px; text-decoration: none; font-size: 12px; font-weight: bold; transition: 0.2s; }
    .btn-detail:hover { background: #70020F; color: white; }
    
    .alert-success {
        background: #fff; color: #70020F; padding: 15px; border-radius: 10px; margin-bottom: 20px; border-left: 5px solid #70020F;
        display: none; animation: fadeIn 0.5s; box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }
    @keyframes fadeIn { from { opacity: 0; transform: translateY(-10px); } to { opacity: 1; transform: translateY(0); } }
</style>
</head>
<body>

<div class="header">
    <h2>📜 Riwayat Transaksi</h2>
    <a href="../dashboard.jsp" style="color:white; text-decoration:none;">← Kembali ke Dashboard</a>
</div>

<div class="container">
    
    <!-- Notifikasi Sukses -->
    <div id="successAlert" class="alert-success">
        ✅ <strong>Berhasil!</strong> Transaksi berhasil disimpan.
    </div>

    <!-- Filter Form -->
    <div class="filter-box">
        <form method="GET" style="display:flex; gap:10px; width:100%; align-items:center;">
            <label style="font-weight:bold;">Filter Periode:</label>
            
            <!-- Dropdown Bulan -->
            <select name="bulan">
                <% for (int i = 0; i < 12; i++) { %>
                    <option value="<%= (i + 1) %>" <%= (selectedBulan == (i + 1)) ? "selected" : "" %>>
                        <%= namaBulan[i] %>
                    </option>
                <% } %>
            </select>
            
            <!-- Dropdown Tahun (Dari 2 tahun lalu sampai 2 tahun ke depan) -->
            <select name="tahun">
                <% 
                    int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                    for (int i = currentYear - 2; i <= currentYear + 2; i++) { 
                %>
                    <option value="<%= i %>" <%= (selectedTahun == i) ? "selected" : "" %>>
                        <%= i %>
                    </option>
                <% } %>
            </select>
            
            <button type="submit" class="btn-filter">🔍 Tampilkan</button>
        </form>
    </div>

    <!-- Tabel Data -->
    <table>
        <thead>
            <tr>
                <th>No Transaksi</th>
                <th>Waktu</th>
                <th>Pelanggan</th>
                <th>Metode</th>
                <th>Total</th>
                <th>Aksi</th>
            </tr>
        </thead>
        <tbody>
            <%
                Connection conn = null;
                boolean hasData = false;
                try {
                    conn = DBConnection.getConnection();
                    
                    // QUERY UPDATE: Menggunakan MONTH() dan YEAR() sesuai input user
                    String sql = "SELECT t.*, p.nama_pelanggan FROM transaksi t " +
                                 "LEFT JOIN pelanggan p ON t.pelanggan_id = p.id " +
                                 "WHERE MONTH(t.waktu_transaksi) = ? AND YEAR(t.waktu_transaksi) = ? " + 
                                 "ORDER BY t.waktu_transaksi DESC";
                    
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, selectedBulan);
                    pstmt.setInt(2, selectedTahun);
                    
                    ResultSet rs = pstmt.executeQuery();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    
                    while(rs.next()){
                        hasData = true;
            %>
            <tr>
                <td><strong><%= rs.getString("no_transaksi") %></strong></td>
                <td><%= sdf.format(rs.getTimestamp("waktu_transaksi")) %></td>
                <td><%= (rs.getString("nama_pelanggan") != null) ? rs.getString("nama_pelanggan") : "Umum" %></td>
                <td>
                    <span style="padding:3px 8px; border-radius:10px; font-size:12px; 
                        background: <%= "Cash".equals(rs.getString("metode_bayar")) ? "#d4edda" : "#cce5ff" %>;">
                        <%= rs.getString("metode_bayar") %>
                    </span>
                </td>
                <td>Rp <%= String.format("%,.0f", rs.getDouble("total_bayar")) %></td>
                <td>
                    <a href="struk.jsp?id=<%= rs.getInt("id") %>" class="btn-detail" target="_blank">Lihat Struk</a>
                </td>
            </tr>
            <%
                    }
                    
                    if (!hasData) {
            %>
                <tr>
                    <td colspan="6" style="text-align:center; padding: 40px; color: #999;">
                        Tidak ada transaksi pada periode <strong><%= namaBulan[selectedBulan-1] %> <%= selectedTahun %></strong>
                    </td>
                </tr>
            <%
                    }
                    
                    rs.close(); pstmt.close();
                } catch(Exception e){ e.printStackTrace(); }
                finally { DBConnection.closeConnection(conn); }
            %>
        </tbody>
    </table>
</div>

<script>
    // Cek URL Parameter untuk menampilkan notifikasi sukses
    window.onload = function() {
        const urlParams = new URLSearchParams(window.location.search);
        if(urlParams.get('status') === 'success') {
            document.getElementById('successAlert').style.display = 'block';
            setTimeout(() => {
                document.getElementById('successAlert').style.display = 'none';
                // Hapus parameter status agar bersih
                const newUrl = window.location.protocol + "//" + window.location.host + window.location.pathname + "?bulan=<%=selectedBulan%>&tahun=<%=selectedTahun%>";
                window.history.replaceState({path:newUrl}, '', newUrl);
            }, 5000);
        }
    };
</script>

</body>
</html>