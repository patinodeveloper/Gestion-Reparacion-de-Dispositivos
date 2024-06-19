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
import model.RepairDAO;
import model.classes.Repair;

/**
 *
 * @author Antonio
 */
public class RepairCtrl {

    private RepairDAO repairDAO = new RepairDAO();
    Messages msg = new Messages();

    public void addRepar(Repair repair) {
        boolean exito = repairDAO.insertRepair(repair);
        if (exito) {
            msg.successfulMessage("Nueva reparación añadida, ¡A trabajar!", "Registro de Reparación");
        } else {
            msg.errorMessage("Error al intentar agregar la reparación", "Registro de Reparación");
        }
    }

    public void listRepairs(JTable tblRepairs) {
        List<Repair> ListarCl = repairDAO.selectAllRepairs();
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tblRepairs.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
        Object[] ob = new Object[10];
        for (int i = 0; i < ListarCl.size(); i++) {
            ob[0] = ListarCl.get(i).getId();
            ob[1] = ListarCl.get(i).getIdDevice();
            ob[2] = ListarCl.get(i).getDevice();
            ob[3] = ListarCl.get(i).getProblem();
            ob[4] = ListarCl.get(i).getService();
            ob[5] = ListarCl.get(i).getPrice();
            ob[6] = ListarCl.get(i).getReceivedDate();
            ob[7] = ListarCl.get(i).getDeliveredDate();
            ob[8] = ListarCl.get(i).getState();
            ob[9] = ListarCl.get(i).getPaymentState();
            model.addRow(ob);
        }
        tblRepairs.setModel(model);
    }

    public List<String> listRepairStates() {
        return repairDAO.getRepairStates();
    }
}
