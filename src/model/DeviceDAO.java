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
        String sqlDeletePayments = "DELETE FROM pagos WHERE id_reparacion IN (SELECT id_reparacion FROM reparaciones WHERE id_dispositivo = ?)";
        String sqlDeleteRepairs = "DELETE FROM reparaciones WHERE id_dispositivo = ?";
        String sqlDeleteDevice = "DELETE FROM dispositivos WHERE id_dispositivo = ?";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = cn.getConnectDB();
            con.setAutoCommit(false);

            // Eliminar pagos relacionados a las reparaciones del dispositivo
            ps = con.prepareStatement(sqlDeletePayments);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();

            // Eliminar reparaciones del dispositivo
            ps = con.prepareStatement(sqlDeleteRepairs);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();

            // Eliminar dispositivo
            ps = con.prepareStatement(sqlDeleteDevice);
            ps.setInt(1, id);
            ps.executeUpdate();

            // Commit de la transacción
            con.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (con != null) {
                    // Rollback en caso de error
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.toString());
            }
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar conexión: " + ex.toString());
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

    public int getClientIdByDeviceId(int deviceId) {
        int clientId = -1;
        String query = "SELECT id_cliente FROM dispositivos WHERE id_dispositivo = ?";

        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(query);
            ps.setInt(1, deviceId);
            rs = ps.executeQuery();

            if (rs.next()) {
                clientId = rs.getInt("id_cliente");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el ID del cliente: " + e.toString());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar la conexión: " + ex.toString());
            }
        }
        return clientId;
    }

}
