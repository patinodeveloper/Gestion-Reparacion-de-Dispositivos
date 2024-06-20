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
import java.util.Date;
import java.util.List;
import model.classes.Repair;

/**
 *
 * @author Antonio
 */
public class RepairDAO {

    SQLConnection cn = new SQLConnection();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean insertRepair(Repair repair) {
        String sql = "INSERT INTO reparaciones (id_dispositivo, servicio, costo, fecha_recepcion, fecha_entrega) "
                + "VALUES (?, ?, ?, ?, ?)";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, repair.getIdDevice());
            ps.setString(2, repair.getService());
            ps.setDouble(3, repair.getPrice());
            ps.setDate(4, new java.sql.Date(repair.getReceivedDate().getTime()));
            ps.setDate(5, null);
//            ps.setString(6, repair.getState().toString());
//            ps.setString(7, repair.getPaymentState().toString());
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

    public List<Repair> selectAllRepairs() {
        List<Repair> repairs = new ArrayList<>();
        String sql = "SELECT r.id_reparacion, r.id_dispositivo, td.tipo AS dispositivo, d.problema, r.servicio, r.costo, r.fecha_recepcion, r.fecha_entrega, r.estado, r.estado_pago, "
                + "COALESCE((SELECT SUM(monto) FROM pagos WHERE pagos.id_reparacion = r.id_reparacion), 0) AS abonado "
                + "FROM reparaciones r "
                + "JOIN dispositivos d ON r.id_dispositivo = d.id_dispositivo "
                + "JOIN tipos_dispositivos td ON d.id_tipo_dispositivo = td.id_tipo_dispositivo";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Repair repair = new Repair();
                repair.setId(rs.getInt("id_reparacion"));
                repair.setIdDevice(rs.getInt("id_dispositivo"));
                repair.setDevice(rs.getString("dispositivo"));
                repair.setProblem(rs.getString("problema"));
                repair.setService(rs.getString("servicio"));
                repair.setPrice(rs.getDouble("costo"));
                repair.setAbonado(rs.getDouble("abonado"));
                repair.setReceivedDate(rs.getDate("fecha_recepcion"));
                repair.setDeliveredDate(rs.getDate("fecha_entrega"));
                repair.setState(Repair.Estado.fromString(rs.getString("estado")));
                repair.setPaymentState(Repair.EstadoPago.fromString(rs.getString("estado_pago")));
                repairs.add(repair);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                System.out.println(ex.toString());
            }
        }
        return repairs;
    }

    public List<String> getRepairStates() {
        List<String> states = new ArrayList<>();
        String sql = "SHOW COLUMNS FROM reparaciones LIKE 'estado'";

        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                String columnType = rs.getString("Type"); // Obtenemos el tipo de la columna
                String enumValues = columnType.replaceAll("enum\\(|\\)", ""); // Eliminamos "enum(" y ")"
                String[] values = enumValues.split(","); // Separamos los valores

                for (String value : values) {
                    states.add(value.replace("'", "")); // Eliminamos las comillas simples
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return states;
    }

    public List<String> getRepairPaymentStates() {
        List<String> paymentStates = new ArrayList<>();
        String sql = "SHOW COLUMNS FROM reparaciones LIKE 'estado_pago'";

        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                String columnType = rs.getString("Type"); // Obtenemos el tipo de la columna
                String enumValues = columnType.replaceAll("enum\\(|\\)", ""); // Eliminamos "enum(" y ")"
                String[] values = enumValues.split(","); // Separamos los valores

                for (String value : values) {
                    paymentStates.add(value.replace("'", "")); // Eliminamos las comillas simples
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return paymentStates;
    }

    public boolean updateRepair(Repair repair) {
        String sql = "UPDATE reparaciones SET id_dispositivo = ?, servicio = ?, costo = ?, fecha_recepcion = ?, fecha_entrega = ?, estado = ?, estado_pago = ? WHERE id_reparacion = ?";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, repair.getIdDevice());
            ps.setString(2, repair.getService());
            ps.setDouble(3, repair.getPrice());
            ps.setDate(4, new java.sql.Date(repair.getReceivedDate().getTime()));
            ps.setDate(5, repair.getDeliveredDate() != null ? new java.sql.Date(repair.getDeliveredDate().getTime()) : null);
            ps.setString(6, repair.getState().toString());
            ps.setString(7, repair.getPaymentState().toString());
            ps.setInt(8, repair.getId());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRepair(int id) {
        String sql = "DELETE FROM reparaciones WHERE id_reparacion = ?";
        try {
            con = cn.getConnectDB();
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

    public Repair searchRepair(int id) {
        Repair rep = new Repair();
        String sql = "SELECT r.id_reparacion, r.id_dispositivo, td.tipo AS dispositivo, d.problema, r.servicio, r.costo, "
                + "r.fecha_recepcion, r.fecha_entrega, r.estado, r.estado_pago, c.nombre AS nombre_cliente, "
                + "COALESCE(SUM(p.monto), 0) AS abonado "
                + "FROM reparaciones r "
                + "JOIN dispositivos d ON r.id_dispositivo = d.id_dispositivo "
                + "JOIN tipos_dispositivos td ON d.id_tipo_dispositivo = td.id_tipo_dispositivo "
                + "JOIN clientes c ON d.id_cliente = c.id_cliente "
                + "LEFT JOIN pagos p ON r.id_reparacion = p.id_reparacion "
                + "WHERE r.id_reparacion = ? ";

        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                rep.setId(rs.getInt("id_reparacion"));
                rep.setIdDevice(rs.getInt("id_dispositivo"));
                rep.setClient(rs.getString("nombre_cliente"));
                rep.setDevice(rs.getString("dispositivo"));
                rep.setProblem(rs.getString("problema"));
                rep.setService(rs.getString("servicio"));
                rep.setPrice(rs.getDouble("costo"));
                rep.setAbonado(rs.getDouble("abonado"));
                rep.setReceivedDate(rs.getDate("fecha_recepcion"));
                rep.setDeliveredDate(rs.getDate("fecha_entrega"));

                String estado = rs.getString("estado");
                if (estado != null) {
                    rep.setState(Repair.Estado.valueOf(estado.toUpperCase().replace(" ", "_")));
                }

                String estadoPago = rs.getString("estado_pago");
                if (estadoPago != null) {
                    rep.setPaymentState(Repair.EstadoPago.valueOf(estadoPago.toUpperCase().replace(" ", "_")));
                }
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return rep;
    }

}
