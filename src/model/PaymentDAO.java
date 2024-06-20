/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import controller.events.Messages;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.classes.Payment;

/**
 *
 * @author Antonio
 */
public class PaymentDAO {

    private Messages msg = new Messages();
    SQLConnection cn = new SQLConnection();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean insertPayment(Payment payment) {
        String sqlInsertPayment = "INSERT INTO pagos (id_reparacion, monto, fecha_pago, metodo_pago) VALUES (?, ?, ?, ?)";
        String sqlGetRepairCost = "SELECT costo FROM reparaciones WHERE id_reparacion = ?";
        String sqlGetTotalPaid = "SELECT COALESCE(SUM(monto), 0) AS total_pagado FROM pagos WHERE id_reparacion = ?";
        String sqlCallUpdatePaymentStatus = "CALL actualizar_estado_pago(?)";

        try {
            con = cn.getConnectDB();
            // transacción
            con.setAutoCommit(false);

            // Obtener costo total de la reparación
            double totalCost;
            try (PreparedStatement psGetCost = con.prepareStatement(sqlGetRepairCost)) {
                psGetCost.setInt(1, payment.getIdRepair());
                try (ResultSet rsCost = psGetCost.executeQuery()) {
                    if (rsCost.next()) {
                        totalCost = rsCost.getDouble("costo");
                    } else {
                        msg.infoMessage("No se encontró la reparación.", "Error al agregar pago");
                        return false;
                    }
                }
            }

            // Obtener total pagado hasta ahora
            double totalPaid;
            try (PreparedStatement psGetTotalPaid = con.prepareStatement(sqlGetTotalPaid)) {
                psGetTotalPaid.setInt(1, payment.getIdRepair());
                try (ResultSet rsTotalPaid = psGetTotalPaid.executeQuery()) {
                    if (rsTotalPaid.next()) {
                        totalPaid = rsTotalPaid.getDouble("total_pagado");
                    } else {
                        msg.infoMessage("Error al consultar los pagos.", "Error al agregar pago");
                        return false;
                    }
                }
            }

            // Verificar si la reparación ya está completamente pagada
            if (totalPaid >= totalCost) {
                msg.infoMessage("La reparación ya está pagada en su totalidad.", "Agregar Pago");
                return false;
            }

            // Insertar el nuevo pago
            try (PreparedStatement psInsertPayment = con.prepareStatement(sqlInsertPayment)) {
                psInsertPayment.setInt(1, payment.getIdRepair());
                psInsertPayment.setDouble(2, payment.getAmount());
                psInsertPayment.setDate(3, new java.sql.Date(payment.getPaymentDate().getTime()));
                psInsertPayment.setString(4, payment.getPaymentMethod().toString());
                psInsertPayment.executeUpdate();
            }

            // Llamar al procedimiento almacenado para actualizar el estado de pago
            try (PreparedStatement psUpdatePaymentStatus = con.prepareStatement(sqlCallUpdatePaymentStatus)) {
                psUpdatePaymentStatus.setInt(1, payment.getIdRepair());
                psUpdatePaymentStatus.executeUpdate();
            }

            // Confirmar la transacción
            con.commit();

            // Calcular el nuevo total pagado y el monto restante o excedente
            double newTotalPaid = totalPaid + payment.getAmount();
            double amountRemaining = totalCost - newTotalPaid;

            if (amountRemaining > 0) {
                msg.infoMessage("Pago registrado. Resta por pagar: $" + amountRemaining, "Agregar Pago");
            } else if (amountRemaining < 0) {
                msg.infoMessage("Pago registrado. Excedente a devolver: $" + Math.abs(amountRemaining), "Agregar Pago");
            } else {
                msg.infoMessage("Pago completado. El estado de la reparación ha sido actualizado a 'Pagado'.", "Agregar Pago");
            }

            return true;
        } catch (SQLException e) {
            try {
                if (con != null) {
                    // Reversa la transacción en caso de error
                    con.rollback();
                }
            } catch (SQLException ex) {
                System.out.println("Error al hacer rollback: " + ex.toString());
            }
            System.out.println("Error SQL: " + e.toString());
            return false;
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.out.println("Error al cerrar recursos: " + ex.toString());
            }
        }
    }

    public List<Payment> selectAllPayments() {
        List<Payment> payments = new ArrayList();
        String sql = "SELECT * FROM pagos";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Payment payment = new Payment();
                payment.setId(rs.getInt("id_pago"));
                payment.setIdRepair(rs.getInt("id_reparacion"));
                payment.setAmount(rs.getDouble("monto"));
                payment.setPaymentDate(rs.getDate("fecha_pago"));
                payment.setPaymentMethod(Payment.PaymentMethod.fromString(rs.getString("metodo_pago")));
                payments.add(payment);
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
        return payments;
    }

    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE pagos SET id_reparacion = ?, monto = ?, fecha_pago = ?, metodo_pago = ? WHERE id_pago = ?";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, payment.getIdRepair());
            ps.setDouble(2, payment.getAmount());
            ps.setDate(3, new java.sql.Date(payment.getPaymentDate().getTime()));
            ps.setString(4, payment.getPaymentMethod().toString());
            ps.setInt(5, payment.getId());
            ps.executeUpdate();
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

    public boolean deletePayment(int id) {
        String sql = "DELETE FROM pagos WHERE id_pago = ?";
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

    public List<String> getPaymentMethods() {
        List<String> methods = new ArrayList();
        String sql = "SHOW COLUMNS FROM pagos LIKE 'metodo_pago'";

        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if (rs.next()) {
                String columnType = rs.getString("Type");
                String enumValues = columnType.replaceAll("enum\\(|\\)", "");
                String[] values = enumValues.split(",");

                for (String value : values) {
                    methods.add(value.replace("'", ""));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return methods;
    }

    public Payment searchPayment(int id) {
        Payment payment = new Payment();
        String sql = "SELECT * FROM pagos WHERE id_pago = ?";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                payment.setId(rs.getInt("id_pago"));
                payment.setIdRepair(rs.getInt("id_reparacion"));
                payment.setAmount(rs.getDouble("monto"));
                payment.setPaymentDate(rs.getDate("fecha_pago"));
                payment.setPaymentMethod(Payment.PaymentMethod.fromString(rs.getString("metodo_pago")));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return payment;
    }
}
