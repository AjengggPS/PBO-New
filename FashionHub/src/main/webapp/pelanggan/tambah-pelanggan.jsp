<%-- 
    Document   : tambah-pelanggan
    Created on : 15 Dec 2025, 23.00.09
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
    <title>Tambah Pelanggan - FashionHub</title>
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
        .required { color: #e74c3c; }
        input, textarea {
            width: 100%;
            padding: 12px;
            border: 2px solid #FFDEE2;
            border-radius: 10px;
            font-size: 14px;
            font-family: 'Poppins', sans-serif;
            transition: border 0.3s;
            outline: none;
        }
        input:focus, textarea:focus {
            border-color: #70020F;
        }
        textarea {
            resize: vertical;
            min-height: 100px;
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
        
        .info-box {
            background: #fff0f2;
            padding: 15px;
            border-radius: 10px;
            margin-bottom: 20px;
            border-left: 4px solid #70020F;
        }
        .info-box p {
            margin: 0;
            color: #70020F;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="header">
        <h2>➕ Tambah Pelanggan Baru</h2>
        <a href="list-pelanggan.jsp" class="btn-back">← Kembali</a>
    </div>
    
    <div class="container">
        <div class="card">
            <div class="info-box">
                <p>💡 Data pelanggan digunakan untuk keperluan transaksi dan dapat dikosongkan jika pelanggan tidak ingin memberikan data.</p>
            </div>
            
            <% if (request.getParameter("error") != null) { %>
                <div class="alert-error"><%= request.getParameter("error") %></div>
            <% } %>
            
            <form action="../PelangganServlet" method="post">
                <input type="hidden" name="action" value="add">
                
                <div class="form-group">
                    <label>Nama Pelanggan <span class="required">*</span></label>
                    <input type="text" name="namaPelanggan" 
                           placeholder="Contoh: Budi Santoso" required>
                </div>
                
                <div class="form-group">
                    <label>Nomor HP</label>
                    <input type="text" name="noHp" 
                           placeholder="Contoh: 081234567890">
                </div>
                
                <div class="form-group">
                    <label>Alamat</label>
                    <textarea name="alamat" 
                              placeholder="Contoh: Jl. Merdeka No. 123, Jakarta Pusat"></textarea>
                </div>
                
                <div class="btn-group">
                    <button type="button" class="btn-cancel" 
                            onclick="window.location.href='list-pelanggan.jsp'">
                        Batal
                    </button>
                    <button type="submit" class="btn-submit">
                        💾 Simpan Pelanggan
                    </button>
                </div>
            </form>
        </div>
    </div>
</body>
</html>