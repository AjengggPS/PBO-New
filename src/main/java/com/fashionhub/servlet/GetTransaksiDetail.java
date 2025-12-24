/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.fashionhub.servlet;

import com.fashionhub.util.DBConnection;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/GetTransaksiDetail")
public class GetTransaksiDetail extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        String idParam = request.getParameter("id");
        
        if (idParam == null) {
            out.print("{\"error\": \"ID tidak valid\"}");
            return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            // Get transaksi data
            String sql = "SELECT t.*, p.nama_pelanggan " +
                        "FROM transaksi t " +
                        "LEFT JOIN pelanggan p ON t.pelanggan_id = p.id " +
                        "WHERE t.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(idParam));
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                JSONObject jsonResponse = new JSONObject();
                
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                
                jsonResponse.put("noTransaksi", rs.getString("no_transaksi"));
                jsonResponse.put("waktu", sdf.format(rs.getTimestamp("waktu_transaksi")));
                jsonResponse.put("pelanggan", rs.getString("nama_pelanggan") != null ? 
                               rs.getString("nama_pelanggan") : "Pelanggan Umum");
                jsonResponse.put("metodeBayar", rs.getString("metode_bayar"));
                jsonResponse.put("totalBelanja", rs.getDouble("total_belanja"));
                jsonResponse.put("diskon", rs.getDouble("diskon"));
                jsonResponse.put("totalBayar", rs.getDouble("total_bayar"));
                jsonResponse.put("jumlahBayar", rs.getDouble("jumlah_bayar"));
                jsonResponse.put("kembalian", rs.getDouble("kembalian"));
                
                rs.close();
                pstmt.close();
                
                // Get detail items
                String sqlDetail = "SELECT * FROM detail_transaksi WHERE transaksi_id = ?";
                pstmt = conn.prepareStatement(sqlDetail);
                pstmt.setInt(1, Integer.parseInt(idParam));
                rs = pstmt.executeQuery();
                
                JSONArray items = new JSONArray();
                while (rs.next()) {
                    JSONObject item = new JSONObject();
                    item.put("nama", rs.getString("nama_produk"));
                    item.put("harga", rs.getDouble("harga"));
                    item.put("qty", rs.getInt("jumlah"));
                    item.put("subtotal", rs.getDouble("subtotal"));
                    items.put(item);
                }
                
                jsonResponse.put("items", items);
                
                out.print(jsonResponse.toString());
            } else {
                out.print("{\"error\": \"Transaksi tidak ditemukan\"}");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\": \"" + e.getMessage() + "\"}");
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                DBConnection.closeConnection(conn);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}