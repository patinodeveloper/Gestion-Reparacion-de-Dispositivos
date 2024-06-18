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
import model.DeviceDAO;
import model.classes.Device;

/**
 *
 * @author Antonio
 */
public class DeviceCtrl {

    private DeviceDAO deviceDAO = new DeviceDAO();
    private TypeDeviceCtrl tdc = new TypeDeviceCtrl();
    private Messages msg = new Messages();

    public void addDevice(Device dv) {
        boolean exito = deviceDAO.insertDevice(dv);
        if (exito) {
            msg.successfulMessage("El dispositivo se ha agregado exitosamente", "Agregar Dispositivo");
        } else {
            msg.errorMessage("Error al intentar agregar un nuevo dispositivo", "Agregar Dispositivo");
        }
    }

    public void listDevices(JTable tblDevice) {
        List<Device> ListarDv = deviceDAO.selectDevice();
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tblDevice.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }

        Object[] ob = new Object[8];
        for (int i = 0; i < ListarDv.size(); i++) {
            ob[0] = ListarDv.get(i).getId();
            ob[1] = ListarDv.get(i).getClientName();
            ob[2] = ListarDv.get(i).getTypeDevice();
            ob[3] = ListarDv.get(i).getBrand();
            ob[4] = ListarDv.get(i).getModel();
            ob[5] = ListarDv.get(i).getSerie_number();
            ob[6] = ListarDv.get(i).getColor();
            ob[7] = ListarDv.get(i).getProblem();
            model.addRow(ob);
            tblDevice.setModel(model);
        }
    }

    public void updateDevice(Device dv) {
        boolean isUpdated = deviceDAO.updateDevice(dv);
        if (isUpdated) {
            msg.successfulMessage("Datos del dispositivo actualizados correctamente", "Actualizar Dispositivo");
        } else {
            msg.errorMessage("Error al intentar actualizar los datos del dispositivo", "Actualizar Dispositivo");
        }
    }

    public Device searchIdCtrl(int id) {
        Device dv = new Device();
        dv = deviceDAO.searchId(id);
        if (dv != null) {
            return dv;
        } else {
            return null;
        }
    }

}
