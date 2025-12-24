<%-- 
    Document   : index
    Created on : 15 Dec 2025, 22.48.54
    Author     : Ajeng Puspita Sari
--%>


<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>FashionHub - Start Shift</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        body {
            font-family: 'Poppins', sans-serif;
            /* Gradient background Misty Rose ke Rosewood Muda */
            background: linear-gradient(135deg, #FFDEE2 0%, #d4a5ad 100%);
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .container {
            background: white;
            padding: 40px;
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(112, 2, 15, 0.15);
            width: 400px;
            border: 1px solid #FFDEE2;
        }
        h1 {
            text-align: center;
            color: #70020F;
            margin-bottom: 5px;
            font-size: 28px;
        }
        .subtitle {
            text-align: center;
            color: #999;
            margin-bottom: 30px;
            font-size: 14px;
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
        input[type="text"] {
            width: 100%;
            padding: 12px;
            border: 2px solid #FFDEE2;
            border-radius: 10px;
            font-size: 16px;
            transition: border 0.3s;
            outline: none;
        }
        input[type="text"]:focus {
            border-color: #70020F;
        }
        
        /* Pilihan Shift */
        .shift-options {
            display: flex;
            gap: 10px;
        }
        .shift-option {
            flex: 1;
        }
        .shift-option input[type="radio"] {
            display: none;
        }
        .shift-option label {
            display: block;
            padding: 15px;
            background: #fff;
            border: 2px solid #FFDEE2;
            border-radius: 10px;
            text-align: center;
            cursor: pointer;
            transition: all 0.3s;
            color: #666;
        }
        .shift-option label:hover {
            border-color: #70020F;
            background: #fff0f2;
        }
        .shift-option input[type="radio"]:checked + label {
            background: #70020F;
            color: white;
            border-color: #70020F;
            box-shadow: 0 4px 10px rgba(112, 2, 15, 0.2);
        }
        .shift-time {
            font-size: 11px;
            margin-top: 5px;
            opacity: 0.9;
        }
        
        button {
            width: 100%;
            padding: 15px;
            background: #70020F;
            color: white;
            border: none;
            border-radius: 10px;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: background 0.3s;
            box-shadow: 0 4px 10px rgba(112, 2, 15, 0.3);
        }
        button:hover {
            background: #52000a;
        }
        
        .error {
            background: #fff;
            color: #70020F;
            padding: 10px;
            border-radius: 8px;
            margin-bottom: 20px;
            text-align: center;
            border: 1px solid #70020F;
            font-size: 14px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🛍️ FashionHub</h1>
        <p class="subtitle">Sistem Kasir Toko Fashion</p>
        
        <!-- Tampilkan error jika ada -->
        <% if(request.getParameter("error") != null) { %>
            <div class="error">
                ⚠️ <%= request.getParameter("error") %>
            </div>
        <% } %>
        
        <!-- Form Start Shift -->
        <form action="StartShiftServlet" method="post">
            <div class="form-group">
                <label for="namaKasir">Nama Kasir:</label>
                <input type="text" id="namaKasir" name="namaKasir" 
                       placeholder="Masukkan nama Anda" required>
            </div>
            
            <div class="form-group">
                <label>Pilih Shift:</label>
                <div class="shift-options">
                    <div class="shift-option">
                        <input type="radio" id="pagi" name="shift" value="Pagi" required>
                        <label for="pagi">
                            <strong>Pagi</strong>
                            <div class="shift-time">08:00 - 14:00</div>
                        </label>
                    </div>
                    <div class="shift-option">
                        <input type="radio" id="siang" name="shift" value="Siang">
                        <label for="siang">
                            <strong>Siang</strong>
                            <div class="shift-time">14:00 - 20:00</div>
                        </label>
                    </div>
                    <div class="shift-option">
                        <input type="radio" id="malam" name="shift" value="Malam">
                        <label for="malam">
                            <strong>Malam</strong>
                            <div class="shift-time">20:00 - 02:00</div>
                        </label>
                    </div>
                </div>
            </div>
            
            <button type="submit">Mulai Shift</button>
        </form>
    </div>
</body>
</html>