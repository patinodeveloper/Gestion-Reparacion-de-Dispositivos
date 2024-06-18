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
import javax.swing.JOptionPane;
import model.classes.Device;

/**
 *
 * @author Antonio
 */
public class DeviceDAO {

    SQLConnection cn = new SQLConnection();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean insertDevice(Device dv) {
        String sql = "INSERT INTO dispositivos (id_cliente, id_tipo_dispositivo, marca, modelo, numero_serie, color, problema) VALUES (?,?,?,?,?,?,?)";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, dv.getIdClient());
            ps.setInt(2, dv.getIdTypeDevice());
            ps.setString(3, dv.getBrand());
            ps.setString(4, dv.getModel());
            ps.setString(5, dv.getSerie_number());
            ps.setString(6, dv.getColor());
            ps.setString(7, dv.getProblem());
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public List<Device> selectDevice() {
        List<Device> devices = new ArrayList<>();
        String sql = "SELECT d.id_dispositivo, d.id_cliente, d.id_tipo_dispositivo, c.nombre AS nombre_cliente, td.tipo, "
                + "d.marca, d.modelo, d.numero_serie, d.color, d.problema "
                + "FROM dispositivos d "
                + "JOIN tipos_dispositivos td ON d.id_tipo_dispositivo = td.id_tipo_dispositivo "
                + "JOIN clientes c ON d.id_cliente = c.id_cliente";
        try (Connection con = cn.getConnectDB();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Device device = new Device();
                device.setId(rs.getInt("id_dispositivo"));
                device.setIdClient(rs.getInt("id_cliente"));
                device.setIdTypeDevice(rs.getInt("id_tipo_dispositivo"));
                device.setClientName(rs.getString("nombre_cliente"));
                device.setTypeDevice(rs.getString("tipo"));
                device.setBrand(rs.getString("marca"));
                device.setModel(rs.getString("modelo"));
                device.setSerie_number(rs.getString("numero_serie"));
                device.setColor(rs.getString("color"));
                device.setProblem(rs.getString("problema"));
                devices.add(device);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return devices;
    }
//    public List selectDevice() {
//        List<Device> ListDev = new ArrayList();
//        String sql = "SELECT * FROM dispositivos";
//        try {
//            con = cn.getConnectDB();
//            ps = con.prepareStatement(sql);
//            rs = ps.executeQuery();
//            while (rs.next()) {
//                Device dv = new Device();
//                dv.setId(rs.getInt("id_dispositivo"));
//                dv.setIdClient(rs.getInt("id_cliente"));
//                dv.setIdTypeDevice(rs.getInt("id_tipo_dispositivo"));
//                dv.setBrand(rs.getString("marca"));
//                dv.setModel(rs.getString("modelo"));
//                dv.setSerie_number(rs.getString("numero_serie"));
//                dv.setColor(rs.getString("color"));
//                dv.setProblem(rs.getString("problema"));
//                ListDev.add(dv);
//            }
//        } catch (SQLException e) {
//            System.out.println(e.toString());
//        }
//        return ListDev;
//    }

    public boolean updateDevice(Device dv) {
        String sql = "UPDATE dispositivos SET id_cliente=?, id_tipo_dispositivo=?, marca=?, modelo=?, numero_serie=?, color=?, problema=? WHERE id_dispositivo=?";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, dv.getIdClient());
            ps.setInt(2, dv.getIdTypeDevice());
            ps.setString(3, dv.getBrand());
            ps.setString(4, dv.getModel());
            ps.setString(5, dv.getSerie_number());
            ps.setString(6, dv.getColor());
            ps.setString(7, dv.getProblem());
            ps.setInt(8, dv.getId());

            ps.execute();
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

    public boolean deleteDevice(int id) {
        String sql = "DELETE FROM dispositivos WHERE id_dispositivo = ?";
        try {
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
    }

    public Device searchId(int id) {
        Device dv = new Device();
        String sql = "SELECT d.id_dispositivo, d.id_cliente, d.id_tipo_dispositivo, c.nombre AS nombre_cliente, td.tipo AS tipo_dispositivo, "
                + "d.marca, d.modelo, d.numero_serie, d.color, d.problema "
                + "FROM dispositivos d "
                + "JOIN clientes c ON d.id_cliente = c.id_cliente "
                + "JOIN tipos_dispositivos td ON d.id_tipo_dispositivo = td.id_tipo_dispositivo "
                + "WHERE d.id_dispositivo = ?";

        try (Connection con = cn.getConnectDB();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    dv.setId(rs.getInt("id_dispositivo"));
                    dv.setIdClient(rs.getInt("id_cliente"));
                    dv.setIdTypeDevice(rs.getInt("id_tipo_dispositivo"));
                    dv.setClientName(rs.getString("nombre_cliente")); // Nombre del cliente
                    dv.setTypeDevice(rs.getString("tipo_dispositivo")); // Tipo de dispositivo
                    dv.setBrand(rs.getString("marca"));
                    dv.setModel(rs.getString("modelo"));
                    dv.setSerie_number(rs.getString("numero_serie"));
                    dv.setColor(rs.getString("color"));
                    dv.setProblem(rs.getString("problema"));
                } else {
                    System.out.println("No device found with id: " + id);
                }
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.toString());
        }
        return dv;
    }
}
