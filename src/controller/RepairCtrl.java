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
        List<Repair> listRepairs = repairDAO.selectAllRepairs();
        DefaultTableModel model = (DefaultTableModel) tblRepairs.getModel();
        model.setRowCount(0);

        for (Repair repair : listRepairs) {
            Object[] rowData = new Object[11];
            rowData[0] = repair.getId();
            rowData[1] = repair.getIdDevice();
            rowData[2] = repair.getDevice();
            rowData[3] = repair.getProblem();
            rowData[4] = repair.getService();
            rowData[5] = repair.getPrice();
            rowData[6] = repair.getAbonado();
            rowData[7] = repair.getReceivedDate();
            rowData[8] = repair.getDeliveredDate();
            rowData[9] = repair.getState();
            rowData[10] = repair.getPaymentState();

            model.addRow(rowData);
        }

        tblRepairs.setModel(model);
    }

    public List<String> listRepairStates() {
        return repairDAO.getRepairStates();
    }

    public List<String> listRepairPaymentStates() {
        return repairDAO.getRepairPaymentStates();
    }

    public void updateRepair(Repair repair) {
        boolean isUpdated = repairDAO.updateRepair(repair);
        if (isUpdated) {
            msg.successfulMessage("Datos de la reparacióin actualizados", "Actualizar Datos de la Reparación");
        } else {
            msg.errorMessage("Error al intentar actualizar los datos de la reparación", "Actualizar Datos de la Reparación");
        }
    }

    public void deleteRepair(int id) {
        boolean isDeleted = repairDAO.deleteRepair(id);
        if (isDeleted) {
            msg.successfulMessage("Reparación eliminada exitosamente!", "Eliminar Reparación");
        } else {
            msg.errorMessage("Error al intentar eliminar esta reparación", "Eliminar Reparación");
        }
    }

    public Repair searchRepair(int id) {
        return repairDAO.searchRepair(id);
    }
}
