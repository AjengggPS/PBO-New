<%-- 
    Document   : pembayaran
    Author     : Ajeng Puspita Sari (Updated for Custom SQL)
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    if (session.getAttribute("namaKasir") == null) {
        response.sendRedirect("../index.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>Pembayaran - FashionHub</title>
<style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body { font-family: 'Poppins', sans-serif; background: #FFDEE2; color: #333; }
    
    .header { background: #70020F; color: white; padding: 15px 20px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
    .container { max-width: 800px; margin: 30px auto; padding: 0 20px; }
    
    .card { background: white; padding: 30px; border-radius: 15px; box-shadow: 0 4px 15px rgba(112, 2, 15, 0.05); margin-bottom: 20px; }
    .card h3 { border-bottom: 2px solid #FFDEE2; padding-bottom: 10px; margin-bottom: 20px; color: #70020F; }
    
    .order-item { display: flex; justify-content: space-between; padding: 12px 0; border-bottom: 1px solid #FFDEE2; }
    .summary-row { display: flex; justify-content: space-between; margin: 10px 0; font-size: 16px; }
    .summary-row.total { font-size: 20px; font-weight: bold; color: #70020F; border-top: 2px solid #70020F; padding-top: 15px; margin-top: 15px; }
    
    .payment-methods { display: grid; grid-template-columns: 1fr 1fr; gap: 15px; }
    .payment-method { 
        border: 2px solid #FFDEE2; padding: 20px; border-radius: 12px; cursor: pointer; text-align: center; transition: 0.3s;
    }
    .payment-method:hover { border-color: #70020F; background: #fff0f2; }
    .payment-method.active { border-color: #70020F; background: #FFDEE2; color: #70020F; font-weight: bold; }
    
    .btn-submit { 
        width: 100%; padding: 15px; background: #70020F; color: white; border: none; border-radius: 10px; 
        font-size: 18px; font-weight: bold; cursor: pointer; margin-top: 20px; box-shadow: 0 4px 10px rgba(112, 2, 15, 0.3);
    }
    .btn-submit:disabled { background: #dcc0c3; cursor: not-allowed; box-shadow: none; }
    
    #changeDisplay { background: #FFDEE2; color: #70020F; padding: 15px; border-radius: 10px; margin-top: 15px; text-align: center; font-size: 20px; font-weight: bold; border: 1px solid #70020F; }
    input[type="number"] { width: 100%; padding: 12px; font-size: 16px; border: 2px solid #FFDEE2; border-radius: 8px; outline: none; }
    input[type="number"]:focus { border-color: #70020F; }
</style>
</head>
<body>
    <div class="header">
        <h2>💳 Pembayaran</h2>
        <a href="buat-transaksi.jsp" style="color:white; text-decoration:none;">← Kembali</a>
    </div>

    <div class="container">
        <!-- Ringkasan -->
        <div class="card">
            <h3>📋 Ringkasan Pesanan</h3>
            <div id="orderList"></div>
            
            <div style="margin-top: 20px;">
                <div class="summary-row">
                    <span>Subtotal:</span>
                    <span id="txtSubtotal">Rp 0</span>
                </div>
                <div class="summary-row">
                    <span>Diskon:</span>
                    <span id="txtDiskon" style="color:red;">- Rp 0</span>
                </div>
                <div class="summary-row total">
                    <span>TOTAL BAYAR:</span>
                    <span id="txtTotal">Rp 0</span>
                </div>
            </div>
        </div>

        <!-- Metode Pembayaran -->
        <div class="card">
            <h3>💰 Pilih Metode Pembayaran</h3>
            <div class="payment-methods">
                <div class="payment-method" id="pmCash" onclick="selectMethod('Cash')">
                    <h4>💵 Cash</h4>
                    <p>Bayar tunai</p>
                </div>
                <div class="payment-method" id="pmQRIS" onclick="selectMethod('QRIS')">
                    <h4>📱 QRIS</h4>
                    <p>Scan Barcode</p>
                </div>
            </div>

            <!-- Form Cash -->
            <div id="cashArea" style="display:none; margin-top:20px;">
                <label style="font-weight:bold;">Uang Diterima:</label>
                <input type="number" id="inputBayar" placeholder="Masukkan jumlah uang" oninput="calculateChange()">
                <div id="changeDisplay" style="display:none;">Kembalian: Rp 0</div>
            </div>

            <!-- Form QRIS -->
            <div id="qrisArea" style="display:none; margin-top:20px; text-align:center;">
                <img src="https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=FashionHubPayment" style="border:1px solid #ddd; padding:10px;">
                <p>Silakan scan QR Code di atas</p>
                <p style="font-size:12px; color:#999;">(Simulasi: Pembayaran dianggap lunas otomatis)</p>
            </div>
            
            <!-- Hidden Form untuk Submit ke Servlet -->
            <form id="payForm" action="../TransaksiServlet" method="post">
                <input type="hidden" name="action" value="process">
                <input type="hidden" name="pelangganId" id="hPelanggan">
                <input type="hidden" name="items" id="hItems">
                <input type="hidden" name="metodeBayar" id="hMetode">
                <input type="hidden" name="totalBelanja" id="hTotalBelanja">
                <input type="hidden" name="diskon" id="hDiskon">
                <input type="hidden" name="totalBayar" id="hTotalBayar">
                <input type="hidden" name="jumlahBayar" id="hJumlahBayar">
                
                <button type="submit" class="btn-submit" id="btnBayar" disabled>Proses Pembayaran</button>
            </form>
        </div>
    </div>

<script>
    // Load Data dari Session Storage
    const cart = JSON.parse(sessionStorage.getItem('cart')) || [];
    const pelangganId = sessionStorage.getItem('pelangganId') || '';
    const subtotal = parseFloat(sessionStorage.getItem('txSubtotal')) || 0;
    const diskon = parseFloat(sessionStorage.getItem('txDiskon')) || 0;
    const total = parseFloat(sessionStorage.getItem('txTotal')) || 0;
    
    let selectedMethod = '';

    // Render Halaman
    window.onload = function() {
        if(cart.length === 0) {
            alert("Data transaksi hilang, kembali ke menu utama.");
            window.location.href = 'buat-transaksi.jsp';
            return;
        }

        let html = '';
        cart.forEach(item => {
            html += `
            <div class="order-item">
                <span>\${item.nama} x \${item.qty}</span>
                <span>Rp \${(item.harga * item.qty).toLocaleString('id-ID')}</span>
            </div>`;
        });
        document.getElementById('orderList').innerHTML = html;
        document.getElementById('txtSubtotal').innerText = 'Rp ' + subtotal.toLocaleString('id-ID');
        document.getElementById('txtDiskon').innerText = '- Rp ' + diskon.toLocaleString('id-ID');
        document.getElementById('txtTotal').innerText = 'Rp ' + total.toLocaleString('id-ID');
    };

    function selectMethod(method) {
        selectedMethod = method;
        document.getElementById('pmCash').classList.remove('active');
        document.getElementById('pmQRIS').classList.remove('active');
        
        if(method === 'Cash') {
            document.getElementById('pmCash').classList.add('active');
            document.getElementById('cashArea').style.display = 'block';
            document.getElementById('qrisArea').style.display = 'none';
            document.getElementById('btnBayar').disabled = true; // Tunggu input uang
            calculateChange();
        } else {
            document.getElementById('pmQRIS').classList.add('active');
            document.getElementById('cashArea').style.display = 'none';
            document.getElementById('qrisArea').style.display = 'block';
            document.getElementById('btnBayar').disabled = false; // QRIS anggap lunas
            // Set hidden value untuk QRIS
            document.getElementById('hJumlahBayar').value = total; 
        }
    }

    function calculateChange() {
        if(selectedMethod !== 'Cash') return;
        
        let bayar = parseFloat(document.getElementById('inputBayar').value) || 0;
        let changeBox = document.getElementById('changeDisplay');
        let btn = document.getElementById('btnBayar');

        if(bayar >= total) {
            let kembalian = bayar - total;
            changeBox.style.display = 'block';
            changeBox.innerText = 'Kembalian: Rp ' + kembalian.toLocaleString('id-ID');
            btn.disabled = false;
            // Set hidden value
            document.getElementById('hJumlahBayar').value = bayar;
        } else {
            changeBox.style.display = 'none';
            btn.disabled = true;
        }
    }

    // Prepare Data sebelum Submit
    document.getElementById('payForm').onsubmit = function() {
        document.getElementById('hPelanggan').value = pelangganId;
        document.getElementById('hItems').value = JSON.stringify(cart);
        document.getElementById('hMetode').value = selectedMethod;
        document.getElementById('hTotalBelanja').value = subtotal;
        document.getElementById('hDiskon').value = diskon;
        document.getElementById('hTotalBayar').value = total;
        
        // Hapus session storage agar keranjang bersih untuk next transaksi
        sessionStorage.clear();
        return true;
    };
</script>
</body>
</html>