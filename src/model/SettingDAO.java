/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.classes.Settings;

/**
 *
 * @author Antonio
 */
public class SettingDAO {

    SQLConnection cn = new SQLConnection();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public Settings selectData() {
        Settings st = new Settings();
        String sql = "SELECT * FROM Config WHERE id = 1";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                st.setId(rs.getInt("id"));
                st.setName(rs.getString("nombre"));
                st.setPhone(rs.getString("telefono"));
                st.setAddress(rs.getString("direccion"));
                st.setMessage(rs.getString("mensaje"));
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return st;
    }

    public boolean updateData(Settings st) {
        String sql = "UPDATE config SET nombre=?, telefono=?, direccion=?, mensaje=? WHERE id = 1";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setString(1, st.getName());
            ps.setString(2, st.getPhone());
            ps.setString(3, st.getAddress());
            ps.setString(4, st.getMessage());

            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }
}
