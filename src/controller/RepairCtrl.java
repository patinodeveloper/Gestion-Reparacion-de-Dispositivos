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

    public void listRepairsByState(JTable tblRepairs, String estado) {
        List<Repair> listarReparaciones = repairDAO.selectRepairsByStatus(estado);
        DefaultTableModel model = (DefaultTableModel) tblRepairs.getModel();
        model.setRowCount(0); // Limpiar la tabla antes de agregar nuevas filas

        Object[] ob = new Object[11]; // Ajusta el tamaño del arreglo a 11 para incluir abonado
        for (Repair repair : listarReparaciones) {
            ob[0] = repair.getId();
            ob[1] = repair.getIdDevice();
            ob[2] = repair.getDevice();
            ob[3] = repair.getProblem();
            ob[4] = repair.getService();
            ob[5] = repair.getPrice();
            ob[6] = repair.getAbonado();
            ob[7] = repair.getReceivedDate();
            ob[8] = repair.getDeliveredDate();
            ob[9] = repair.getState();
            ob[10] = repair.getPaymentState();
            model.addRow(ob);
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

    public int getDeviceIdByRepairId(int idRepair) {
        return repairDAO.getDeviceIdByRepairId(idRepair);
    }

    public void pdfRepairDetails(int repairId, int clientId, String usuario) {
        repairDAO.pdfRepairDetails(repairId, clientId, usuario);
    }

}
