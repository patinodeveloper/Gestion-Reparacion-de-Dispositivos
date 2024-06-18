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
import model.ClientDAO;
import model.classes.Client;

/**
 *
 * @author Antonio
 */
public class ClientCtrl {

    private ClientDAO clientDAO = new ClientDAO();
    private Messages msg = new Messages();

    public void listClients(JTable tblClient) {
        List<Client> ListarCl = clientDAO.selectClient();
        DefaultTableModel model = new DefaultTableModel();
        model = (DefaultTableModel) tblClient.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            model.removeRow(i);
            i = i - 1;
        }
        Object[] ob = new Object[4];
        for (int i = 0; i < ListarCl.size(); i++) {
            ob[0] = ListarCl.get(i).getId();
            ob[1] = ListarCl.get(i).getName();
            ob[2] = ListarCl.get(i).getPhone();
            ob[3] = ListarCl.get(i).getAddress();
            model.addRow(ob);
        }
        tblClient.setModel(model);

    }

    public void updateClient(Client client) {
        boolean exito = clientDAO.updateClient(client);
        if (exito) {
            msg.successfulMessage("Datos del cliente actualizados", "Actualzar Cliente");
        } else {
            msg.errorMessage("Los datos del cliente no se pudieron actualizar", "Actualzar Cliente");
        }
    }

    public void registerClient(Client client) {
        boolean exito = clientDAO.insertClient(client);
        if (exito) {
            msg.successfulMessage("Cliente registrado exitosamente", "Registrar Cliente");
        } else {
            msg.errorMessage("Error al intentar registrar un nuevo cliente", "Registrar Cliente");
        }
    }

    public void deleteClient(int id) {
        boolean exito = clientDAO.deleteClient(id);
        if (exito) {
            msg.successfulMessage("Cliente eliminado exitosamente", "Eliminar Cliente");
        } else {
            msg.errorMessage("Error al intentar eliminar el cliente", "Eliminar Cliente");
        }
    }

    public Client searchClientId(int id) {
        Client cl = clientDAO.searchClient(id);
        return cl;
    }
}
