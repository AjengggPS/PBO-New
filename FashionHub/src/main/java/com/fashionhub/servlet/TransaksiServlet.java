/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fashionhub.servlet;

import com.fashionhub.util.DBConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/TransaksiServlet")
public class TransaksiServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if ("process".equals(action)) {
            processTransaksi(request, response);
        }
    }

    private void processTransaksi(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        HttpSession session = request.getSession();
        Integer kasirId = (Integer) session.getAttribute("kasirId");

        if (kasirId == null) {
            response.sendRedirect("index.jsp?error=Session%20expired.%20Silakan%20login%20kembali.");
            return;
        }

        String itemsJson = request.getParameter("items");
        String metodeBayar = request.getParameter("metodeBayar");
        String jumlahBayarStr = request.getParameter("jumlahBayar");
        String totalBelanjaStr = request.getParameter("totalBelanja");
        String diskonStr = request.getParameter("diskon");
        String totalBayarStr = request.getParameter("totalBayar");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            double totalBelanja = Double.parseDouble(totalBelanjaStr);
            double diskon = Double.parseDouble(diskonStr);
            double totalBayar = Double.parseDouble(totalBayarStr);
            double jumlahBayar = Double.parseDouble(jumlahBayarStr);
            double kembalian = jumlahBayar - totalBayar;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String noTransaksi = "TRX" + sdf.format(new Date());

            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            String sqlTransaksi =
                    "INSERT INTO transaksi " +
                    "(no_transaksi, kasir_id, pelanggan_id, total_belanja, diskon, total_bayar, metode_bayar, jumlah_bayar, kembalian) " +
                    "VALUES (?, ?, NULL, ?, ?, ?, ?, ?, ?)";

            pstmt = conn.prepareStatement(sqlTransaksi, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, noTransaksi);
            pstmt.setInt(2, kasirId);
            pstmt.setDouble(3, Math.round(totalBelanja));
            pstmt.setDouble(4, Math.round(diskon));
            pstmt.setDouble(5, Math.round(totalBayar));
            pstmt.setString(6, metodeBayar);
            pstmt.setDouble(7, Math.round(jumlahBayar));
            pstmt.setDouble(8, Math.round(kembalian));

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Gagal menyimpan transaksi, tidak ada baris yang terpengaruh.");
            }

            rs = pstmt.getGeneratedKeys();
            int transaksiId;

            if (rs.next()) {
                transaksiId = rs.getInt(1);
            } else {
                throw new SQLException("Gagal mendapatkan ID transaksi.");
            }

            rs.close();
            pstmt.close();

            JSONArray items = new JSONArray(itemsJson);

            String sqlDetail =
                    "INSERT INTO detail_transaksi " +
                    "(transaksi_id, produk_id, nama_produk, harga, jumlah, subtotal) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

            String sqlUpdateStok = "UPDATE produk SET stok = stok - ? WHERE id = ?";
            String sqlCheckStok = "SELECT stok FROM produk WHERE id = ?";

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);

                int produkId = item.getInt("id");
                String namaProduk = item.getString("nama");
                double harga = item.getDouble("harga");
                int qty = item.getInt("qty");
                double subtotal = harga * qty;

                pstmt = conn.prepareStatement(sqlCheckStok);
                pstmt.setInt(1, produkId);
                rs = pstmt.executeQuery();

                if (rs.next()) {
                    int stok = rs.getInt("stok");
                    if (stok < qty) {
                        throw new SQLException("Stok tidak mencukupi untuk produk: " + namaProduk);
                    }
                }

                rs.close();
                pstmt.close();

                pstmt = conn.prepareStatement(sqlDetail);
                pstmt.setInt(1, transaksiId);
                pstmt.setInt(2, produkId);
                pstmt.setString(3, namaProduk);
                pstmt.setDouble(4, Math.round(harga));
                pstmt.setInt(5, qty);
                pstmt.setDouble(6, Math.round(subtotal));
                pstmt.executeUpdate();
                pstmt.close();

                pstmt = conn.prepareStatement(sqlUpdateStok);
                pstmt.setInt(1, qty);
                pstmt.setInt(2, produkId);

                if (pstmt.executeUpdate() == 0) {
                    throw new SQLException("Gagal update stok produk: " + namaProduk);
                }

                pstmt.close();
            }

            conn.commit();

            System.out.println("✅ Transaksi sukses: " + noTransaksi + " | ID: " + transaksiId);

            response.sendRedirect("transaksi/struk.jsp?id=" + transaksiId);

        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            e.printStackTrace();
            response.sendRedirect(
                    "transaksi/buat-transaksi.jsp?error=" +
                    java.net.URLEncoder.encode(e.getMessage(), "UTF-8")
            );

        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    DBConnection.closeConnection(conn);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
