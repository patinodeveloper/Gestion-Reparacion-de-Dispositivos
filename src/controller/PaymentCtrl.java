/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.events.Messages;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.PaymentDAO;
import model.classes.Payment;

/**
 *
 * @author Antonio
 */
public class PaymentCtrl {

    private PaymentDAO paymentDAO = new PaymentDAO();
    private Messages msg = new Messages();

    public void listPayments(JTable tblPayments) {
        List<Payment> listPayments = paymentDAO.selectAllPayments();
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tblPayments.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
        Object[] ob = new Object[5];
        for (int i = 0; i < listPayments.size(); i++) {
            ob[0] = listPayments.get(i).getId();
            ob[1] = listPayments.get(i).getIdRepair();
            ob[2] = listPayments.get(i).getAmount();
            ob[3] = listPayments.get(i).getPaymentDate();
            ob[4] = listPayments.get(i).getPaymentMethod();
            model.addRow(ob);
        }
        tblPayments.setModel(model);
    }

    public void addPayment(Payment p) {
        boolean exito = paymentDAO.insertPayment(p);
        if (exito) {
            msg.successfulMessage("Pago registrado con éxito", "Añadir Pago");
        } else {
            msg.errorMessage("Error al intentar registrar un nuevo pago", "Añadir Pago");
        }
    }

    public void updatePayment(Payment p) {
        boolean isUpdated = paymentDAO.updatePayment(p);
        if (isUpdated) {
            msg.successfulMessage("Pago actualizado exitosamente", "Actualizar Pago");
        } else {
            msg.errorMessage("Error al intentar actualizar los datos del pago", "Actualizar Pago");
        }
    }

    public void deletePayment(int id) {
        boolean isDeleted = paymentDAO.deletePayment(id);
        if (isDeleted) {
            msg.successfulMessage("Pago eliminado exitosamente", "Eliminar Pago");
        } else {
            msg.errorMessage("Error al intentar eliminar este pago", "Eliminar Pago");
        }
    }

    public List<String> listPaymentMethods() {
        return paymentDAO.getPaymentMethods();
    }

    public Payment searchPayment(int id) {
        return paymentDAO.searchPayment(id);
    }
}
