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
import java.util.ArrayList;
import java.util.List;
import model.classes.TypeDevice;

/**
 *
 * @author Antonio
 */
public class TypeDeviceDAO {

    SQLConnection cn = new SQLConnection();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public TypeDevice selectTypeDevice(int id) {
        String sql = "SELECT * FROM tipos_dispositivos WHERE id_tipo_dispositivo=?";
        TypeDevice td = new TypeDevice();
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                td.setId(rs.getInt("id_tipo_dispositivo"));
                td.setType(rs.getString("tipo"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return td;
    }

    public List<TypeDevice> selectAllTypeDevice() {
        List<TypeDevice> ListTD = new ArrayList();

        String sql = "SELECT * FROM tipos_dispositivos";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                TypeDevice td = new TypeDevice();
                td.setId(rs.getInt("id_tipo_dispositivo"));
                td.setType(rs.getString("tipo"));
                ListTD.add(td);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return ListTD;
    }

}
