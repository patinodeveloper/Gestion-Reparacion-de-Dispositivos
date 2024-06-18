/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.ArrayList;
import java.util.List;
import model.TypeDeviceDAO;
import model.classes.TypeDevice;

/**
 *
 * @author Antonio
 */
public class TypeDeviceCtrl {

    TypeDeviceDAO typeDeviceDAO = new TypeDeviceDAO();
    TypeDevice td = new TypeDevice();

    public String selectTypeDevice(int id) {
        if (id != 0) {
            td = typeDeviceDAO.selectTypeDevice(id);
            String typeDevice = td.getType();
            return typeDevice;
        }
        return "";
    }

    public List<TypeDevice> listAllDevices() {
        List<TypeDevice> listTD = new ArrayList();
        listTD = typeDeviceDAO.selectAllTypeDevice();
        if (listTD != null) {
            return listTD;
        } else {
            return null;
        }
    }
}
