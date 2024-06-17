/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.events;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Antonio
 */
public class Messages {

    public void successfulMessage(String message, String title) {
        Icon icon = new ImageIcon(getClass().getResource("../../resources/comprobado-64px.png"));
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE, icon);
    }

    public void errorMessage(String message, String title) {
        Icon icon = new ImageIcon(getClass().getResource("../../resources/error-64px.png"));
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE, icon);
    }

    public boolean confirmMessage(String message, String title) {
        Icon icon = new ImageIcon(getClass().getResource("../../resources/advertencia-64px.png"));
        int respuesta = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.YES_NO_CANCEL_OPTION, icon);
        if (respuesta == JOptionPane.YES_OPTION) {
            return true;
        } else if (respuesta == JOptionPane.NO_OPTION) {
            return false;
        }
        return false;
    }
}
