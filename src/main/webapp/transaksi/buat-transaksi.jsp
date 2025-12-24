<%-- 
    Document   : buat-transaksi
    Author     : Ajeng Puspita Sari (Updated for Custom SQL)
--%>



<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.fashionhub.util.DBConnection" %>
<%@ page import="java.util.*" %>
<%
    if (session.getAttribute("namaKasir") == null) {
        response.sendRedirect("../index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Transaksi Penjualan - FashionHub</title>
<style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    /* Background utama jadi Misty Rose */
    body { font-family: 'Poppins', sans-serif; background: #FFDEE2; color: #333; }
    
    /* Header jadi Rosewood */
    .header {
        background: #70020F;
        color: white; padding: 15px 20px;
        display: flex; justify-content: space-between; align-items: center;
        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
    }
    .header h2 { font-size: 24px; font-weight: 600; }
    .btn-back {
        background: rgba(255,255,255,0.2); color: white; padding: 8px 15px;
        border-radius: 20px; text-decoration: none; font-size: 14px;
        border: 1px solid rgba(255,255,255,0.4);
    }
    
    .container { max-width: 1400px; margin: 30px auto; padding: 0 20px; }
    .main-grid { display: grid; grid-template-columns: 1fr 400px; gap: 20px; }
    
    /* Card tetap putih biar kontras sama background pink */
    .card { background: white; padding: 25px; border-radius: 15px; box-shadow: 0 4px 15px rgba(112, 2, 15, 0.05); margin-bottom: 20px;}
    
    /* Style judul card default */
    .card h3 { margin-bottom: 20px; color: #70020F; border-bottom: 2px solid #FFDEE2; padding-bottom: 10px; }
    
    select, input { width: 100%; padding: 12px; border: 2px solid #FFDEE2; border-radius: 8px; margin-bottom: 10px; outline: none; }
    select:focus, input:focus { border-color: #70020F; }
    
    .produk-list { max-height: 600px; overflow-y: auto; }
    .produk-item {
        border: 1px solid #FFDEE2; padding: 15px; margin-bottom: 10px; border-radius: 12px;
        background: #fff; transition: all 0.2s;
        display: flex; justify-content: space-between; align-items: center;
    }
    .produk-item:hover { border-color: #70020F; box-shadow: 0 4px 12px rgba(112, 2, 15, 0.1); }
    .produk-info h4 { margin-bottom: 5px; color: #333; font-size: 16px; }
    
    /* Badge kategori warnanya disesuaikan */
    .badge { display: inline-block; padding: 4px 8px; border-radius: 6px; font-size: 11px; font-weight: bold; margin-right: 5px; }
    .bg-cat { background: #FFDEE2; color: #70020F; }
    .bg-size { background: #f5f5f5; color: #666; }
    
    /* Harga jadi Rosewood */
    .produk-price { font-size: 16px; font-weight: bold; color: #70020F; }
    
    /* Tombol Add to Cart jadi Rosewood */
    .btn-add-cart {
        background: #70020F; color: white; border: none; padding: 8px 15px;
        border-radius: 8px; cursor: pointer; font-weight: bold; transition: 0.3s;
    }
    .btn-add-cart:hover { background: #52000a; }

    .cart-item { border-bottom: 1px solid #FFDEE2; padding: 15px 0; display: flex; justify-content: space-between; align-items: center; }
    
    /* Tombol Qty */
    .qty-btn { width: 25px; height: 25px; background: #FFDEE2; color: #70020F; border: none; border-radius: 5px; cursor: pointer; font-weight:bold; }
    .qty-input { width: 40px; text-align: center; padding: 2px; margin: 0; border: 1px solid #FFDEE2; }
    .btn-remove { background: #fff; border: 1px solid #70020F; color: #70020F; padding: 4px 8px; border-radius: 5px; cursor: pointer; margin-left: 5px;}
    .btn-remove:hover { background: #70020F; color: white; }
    
    .cart-summary { border-top: 2px dashed #FFDEE2; padding-top: 20px; margin-top: 20px; }
    .summary-row { display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 14px; align-items: center;}
    .summary-row.total { font-size: 20px; font-weight: bold; color: #70020F; border-top: 2px solid #70020F; padding-top: 15px; margin-top: 10px; }
    
    .btn-checkout {
        width: 100%; padding: 15px; background: #70020F; color: white; border: none;
        border-radius: 10px; font-size: 16px; font-weight: bold; cursor: pointer; margin-top: 20px;
        box-shadow: 0 4px 10px rgba(112, 2, 15, 0.3);
    }
    .btn-checkout:disabled { background: #dcc0c3; cursor: not-allowed; box-shadow: none; }
    .discount-control { display: flex; gap: 5px; }
    .discount-control input { margin: 0; }
    .discount-control select { width: 80px; margin: 0; }
    
    #searchProduct:focus {
        border-color: #70020F;
        box-shadow: 0 0 5px rgba(112, 2, 15, 0.2);
    }
</style>
</head>
<body>

<div class="header">
    <h2>🛍️ Transaksi Penjualan</h2>
    <a href="../dashboard.jsp" class="btn-back">← Kembali</a>
</div>

<div class="container">
    <div class="main-grid">
        <!-- Bagian Kiri: Produk -->
        <div>
            <div class="card">
                <h3>👤 Data Pelanggan</h3>
                <select id="pelangganSelect">
                    <option value="">-- Pelanggan Umum --</option>
                    <%
                        Connection conn = null;
                        try {
                            conn = DBConnection.getConnection();
                            Statement stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery("SELECT * FROM pelanggan ORDER BY nama_pelanggan");
                            while(rs.next()){
                    %>
                    <option value="<%= rs.getInt("id") %>">
                        <%= rs.getString("nama_pelanggan") %> 
                        <%= (rs.getString("no_hp") != null) ? "(" + rs.getString("no_hp") + ")" : "" %>
                    </option>
                    <%
                            }
                            rs.close(); stmt.close();
                        } catch(Exception e){ e.printStackTrace(); }
                    %>
                </select>
            </div>

            <div class="card">
                <!-- Header Card dengan Search Bar -->
                <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #FFDEE2; padding-bottom: 10px; margin-bottom: 20px;">
                    <h3 style="border:none; padding:0; margin:0; color: #70020F;">📦 Pilih Produk</h3>
                    <input type="text" id="searchProduct" placeholder="Cari nama atau kode..." 
                           style="width: 250px; margin:0; padding: 8px 15px; border: 2px solid #FFDEE2; border-radius: 20px; outline: none; transition: 0.3s; color: #333;">
                </div>
                
                <div class="produk-list">
                    <%
                        try {
                            Statement stmt = conn.createStatement();
                            // SQL Update: Ambil kolom lengkap produk
                            ResultSet rs = stmt.executeQuery("SELECT * FROM produk WHERE stok > 0 ORDER BY nama_produk");
                            while(rs.next()){
                                int id = rs.getInt("id");
                                String kode = rs.getString("kode_produk");
                                String nama = rs.getString("nama_produk");
                                String kategori = rs.getString("kategori");
                                String ukuran = rs.getString("ukuran");
                                String warna = rs.getString("warna");
                                double harga = rs.getDouble("harga");
                                int stok = rs.getInt("stok");
                                
                                // Format label detail
                                String detail = "";
                                if(ukuran != null && !ukuran.isEmpty()) detail += "Size: " + ukuran + " ";
                                if(warna != null && !warna.isEmpty()) detail += "| " + warna;
                    %>
                    <div class="produk-item">
                        <div class="produk-info">
                            <h4><%= nama %></h4>
                            <div class="produk-meta">
                                <span class="badge bg-cat"><%= kategori %></span>
                                <span style="color:#888;">#<%= kode %></span>
                            </div>
                            <div class="produk-meta">
                                <span class="badge bg-size"><%= detail %></span>
                                Stok: <strong><%= stok %></strong>
                            </div>
                            <p class="produk-price">Rp <%= String.format("%,.0f", harga) %></p>
                        </div>
                        <button class="btn-add-cart" onclick="addToCart(<%= id %>, '<%= nama %>', <%= harga %>, <%= stok %>)">
                            + Tambah
                        </button>
                    </div>
                    <%
                            }
                            rs.close(); stmt.close(); DBConnection.closeConnection(conn);
                        } catch(Exception e){ e.printStackTrace(); }
                    %>
                </div>
            </div>
        </div>

        <!-- Bagian Kanan: Keranjang -->
        <div>
            <div class="card">
                <h3>🛒 Keranjang Belanja</h3>
                <div id="cartItems">
                    <p style="text-align:center; color:#999; padding:20px;">Keranjang kosong</p>
                </div>
                
                <div class="cart-summary" id="cartSummary" style="display:none;">
                    <div class="summary-row">
                        <span>Subtotal:</span>
                        <span id="subtotal">Rp 0</span>
                    </div>
                    
                    <div class="summary-row">
                        <span>Atur Diskon:</span>
                        <div class="discount-control">
                            <input type="number" id="inputDiskon" placeholder="0" oninput="calculateTotal()" style="text-align:right;">
                            <select id="tipeDiskon" onchange="calculateTotal()">
                                <option value="rp">Rp</option>
                                <option value="percent">%</option>
                            </select>
                        </div>
                    </div>

                    <div class="summary-row">
                        <span style="color: #e74c3c;">Potongan:</span>
                        <span id="displayPotongan" style="color: #e74c3c;">- Rp 0</span>
                    </div>

                    <div class="summary-row total">
                        <span>TOTAL BAYAR:</span>
                        <span id="total">Rp 0</span>
                    </div>
                    
                    <button class="btn-checkout" id="btnCheckout" onclick="processCheckout()">
                        Lanjut ke Pembayaran
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    let cart = [];

    function addToCart(id, nama, harga, stokMax) {
        let item = cart.find(i => i.id === id);
        if (item) {
            if (item.qty < stokMax) { item.qty++; } 
            else { alert("Stok tidak mencukupi!"); }
        } else {
            cart.push({ id: id, nama: nama, harga: harga, qty: 1, stokMax: stokMax });
        }
        updateCartUI();
    }

    function updateQty(id, newQty) {
        let item = cart.find(i => i.id === id);
        if (item) {
            if (newQty > 0 && newQty <= item.stokMax) { item.qty = newQty; } 
            else if (newQty > item.stokMax) { 
                alert("Maksimal stok: " + item.stokMax); 
                item.qty = item.stokMax; 
            } else { item.qty = 1; }
            updateCartUI();
        }
    }

    function removeFromCart(id) {
        cart = cart.filter(i => i.id !== id);
        updateCartUI();
    }

    function updateCartUI() {
        let container = document.getElementById('cartItems');
        let summary = document.getElementById('cartSummary');
        
        if (cart.length === 0) {
            container.innerHTML = '<p style="text-align:center; color:#999; padding:20px;">Keranjang kosong</p>';
            summary.style.display = 'none';
            return;
        }

        let html = '';
        cart.forEach(item => {
            html += `
            <div class="cart-item">
                <div class="cart-info">
                    <h4>\${item.nama}</h4>
                    <p>@ Rp \${item.harga.toLocaleString('id-ID')}</p>
                </div>
                <div class="cart-qty">
                    <button class="qty-btn" onclick="updateQty(\${item.id}, \${item.qty-1})">-</button>
                    <input type="number" class="qty-input" value="\${item.qty}" onchange="updateQty(\${item.id}, parseInt(this.value))">
                    <button class="qty-btn" onclick="updateQty(\${item.id}, \${item.qty+1})">+</button>
                    <button class="btn-remove" onclick="removeFromCart(\${item.id})">×</button>
                </div>
            </div>`;
        });
        container.innerHTML = html;
        summary.style.display = 'block';
        calculateTotal();
    }

    function calculateTotal() {
        let subtotal = cart.reduce((sum, item) => sum + (item.harga * item.qty), 0);
        let inputDiskon = parseFloat(document.getElementById('inputDiskon').value) || 0;
        let tipeDiskon = document.getElementById('tipeDiskon').value;
        let nilaiPotongan = 0;

        if (tipeDiskon === 'percent') {
            if(inputDiskon > 100) inputDiskon = 100;
            nilaiPotongan = subtotal * (inputDiskon / 100);
        } else {
            if(inputDiskon > subtotal) inputDiskon = subtotal;
            nilaiPotongan = inputDiskon;
        }

        let total = subtotal - nilaiPotongan;
        document.getElementById('subtotal').innerText = 'Rp ' + subtotal.toLocaleString('id-ID');
        document.getElementById('displayPotongan').innerText = '- Rp ' + nilaiPotongan.toLocaleString('id-ID');
        document.getElementById('total').innerText = 'Rp ' + total.toLocaleString('id-ID');
    }

    function processCheckout() {
        if (cart.length === 0) return alert("Keranjang kosong!");
        
        let pelangganId = document.getElementById('pelangganSelect').value;
        let subtotal = cart.reduce((sum, item) => sum + (item.harga * item.qty), 0);
        let inputDiskon = parseFloat(document.getElementById('inputDiskon').value) || 0;
        let tipeDiskon = document.getElementById('tipeDiskon').value;
        let nilaiPotongan = 0;
        if (tipeDiskon === 'percent') nilaiPotongan = subtotal * (inputDiskon / 100);
        else nilaiPotongan = inputDiskon;
        
        sessionStorage.setItem('cart', JSON.stringify(cart));
        sessionStorage.setItem('pelangganId', pelangganId);
        sessionStorage.setItem('txSubtotal', subtotal);
        sessionStorage.setItem('txDiskon', nilaiPotongan);
        sessionStorage.setItem('txTotal', subtotal - nilaiPotongan);

        window.location.href = 'pembayaran.jsp';
    }

    // --- FITUR SEARCH PRODUK ---
    document.getElementById('searchProduct').addEventListener('keyup', function() {
        let filter = this.value.toLowerCase();
        let items = document.querySelectorAll('.produk-item');

        items.forEach(function(item) {
            let text = item.textContent.toLowerCase(); 
            if (text.includes(filter)) {
                item.style.display = "flex"; 
            } else {
                item.style.display = "none";
            }
        });
    });
</script>
</body>
</html>