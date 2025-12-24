<%-- 
    Document   : tambah-produk
    Created on : 15 Dec 2025, 22.54.18
    Author     : Ajeng Puspita Sari
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
    <title>Tambah Produk - FashionHub</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body { font-family: 'Poppins', sans-serif; background: #FFDEE2; color: #333; }
        
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
        }
        
        .container {
            max-width: 800px;
            margin: 30px auto;
            padding: 0 20px;
        }
        
        .card {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 4px 15px rgba(112, 2, 15, 0.05);
        }
        
        .form-group {
            margin-bottom: 25px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            color: #70020F;
            font-weight: bold;
        }
        .required {
            color: #e74c3c;
        }
        input, select {
            width: 100%;
            padding: 12px;
            border: 2px solid #FFDEE2;
            border-radius: 10px;
            font-size: 14px;
            transition: border 0.3s;
            outline: none;
            font-family: 'Poppins', sans-serif;
        }
        input:focus, select:focus {
            border-color: #70020F;
        }
        
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        
        .btn-group {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }
        button {
            flex: 1;
            padding: 15px;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.3s;
        }
        .btn-submit {
            background: #70020F;
            color: white;
            box-shadow: 0 4px 10px rgba(112, 2, 15, 0.3);
        }
        .btn-submit:hover {
            background: #52000a;
        }
        .btn-cancel {
            background: white;
            border: 1px solid #70020F;
            color: #70020F;
        }
        .btn-cancel:hover {
            background: #fff0f2;
        }
        
        .alert-error {
            background: #f8d7da;
            color: #721c24;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
    <div class="header">
        <h2>➕ Tambah Produk Baru</h2>
        <a href="list-produk.jsp" class="btn-back">← Kembali</a>
    </div>
    
    <div class="container">
        <div class="card">
            <% if (request.getParameter("error") != null) { %>
                <div class="alert-error"><%= request.getParameter("error") %></div>
            <% } %>
            
            <form action="../ProdukServlet" method="post">
                <input type="hidden" name="action" value="add">
                
                <div class="form-group">
                    <label>Kode Produk <span class="required">*</span></label>
                    <input type="text" name="kodeProduk" 
                           placeholder="Contoh: BJ001" required>
                </div>
                
                <div class="form-group">
                    <label>Nama Produk <span class="required">*</span></label>
                    <input type="text" name="namaProduk" 
                           placeholder="Contoh: Kaos Polos Premium" required>
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label>Kategori <span class="required">*</span></label>
                        <select name="kategori" required>
                            <option value="">-- Pilih Kategori --</option>
                            <option value="Baju">Baju</option>
                            <option value="Celana">Celana</option>
                            <option value="Aksesoris">Aksesoris</option>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label>Ukuran</label>
                        <input type="text" name="ukuran" 
                               placeholder="Contoh: M, L, 32, All Size">
                    </div>
                </div>
                
                <div class="form-group">
                    <label>Warna</label>
                    <input type="text" name="warna" 
                           placeholder="Contoh: Hitam, Putih, Navy">
                </div>
                
                <div class="form-row">
                    <div class="form-group">
                        <label>Harga (Rp) <span class="required">*</span></label>
                        <input type="number" name="harga" 
                               placeholder="Contoh: 89000" 
                               min="0" step="1000" required>
                    </div>
                    
                    <div class="form-group">
                        <label>Stok <span class="required">*</span></label>
                        <input type="number" name="stok" 
                               placeholder="Contoh: 50" 
                               min="0" required>
                    </div>
                </div>
                
                <div class="btn-group">
                    <button type="button" class="btn-cancel" 
                            onclick="window.location.href='list-produk.jsp'">
                        Batal
                    </button>
                    <button type="submit" class="btn-submit">
                        💾 Simpan Produk
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>
