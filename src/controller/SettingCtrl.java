/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import controller.events.Messages;
import model.SettingDAO;
import model.classes.Settings;

/**
 *
 * @author Antonio
 */
public class SettingCtrl {

    private SettingDAO stDAO = new SettingDAO();
    private Messages msg = new Messages();

    public Settings selectData() {
        return stDAO.selectData();
    }

    public void updateData(Settings st) {
        boolean isUpdated = stDAO.updateData(st);
        if (isUpdated) {
            msg.successfulMessage("Datos de la empresa actualizados", "Actualizar Datos de Empresa");
        } else {
            msg.errorMessage("Error al intentar actualizar los datos de la empresa", "Actualizar Datos de Empresa");
        }
    }
}
