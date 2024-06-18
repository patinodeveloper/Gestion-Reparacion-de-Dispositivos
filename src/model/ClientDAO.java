/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.classes.Client;

/**
 *
 * @author Antonio
 */
public class ClientDAO {

    SQLConnection cn = new SQLConnection();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    public boolean insertClient(Client cl) {
        String sql = "INSERT INTO clientes (nombre, telefono, direccion) VALUES (?,?,?)";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setString(1, cl.getName());
            ps.setString(2, cl.getPhone());
            ps.setString(3, cl.getAddress());
            ps.execute();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public List selectClient() {
        List<Client> ListaCl = new ArrayList();
        String sql = "SELECT * FROM clientes";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Client cl = new Client();
                cl.setId(rs.getInt("id_cliente"));
                cl.setName(rs.getString("nombre"));
                cl.setPhone(rs.getString("telefono"));
                cl.setAddress(rs.getString("direccion"));
                ListaCl.add(cl);
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return ListaCl;
    }

    public boolean updateClient(Client cl) {
        String sql = "UPDATE clientes SET nombre=?, telefono=?, direccion=? WHERE id_cliente=?";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setString(1, cl.getName());
            ps.setString(2, cl.getPhone());
            ps.setString(3, cl.getAddress());
            ps.setInt(4, cl.getId());
            ps.execute();
            return true;
        } catch (SQLException e) {
            System.out.println(e.toString());
            return false;
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.out.println(e.toString());
            }
        }
    }

    public boolean deleteClient(int id) {
        String sql = "DELETE FROM clientes WHERE id_cliente = ?";
        try {
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

    public Client searchClient(int id) {
        Client cl = new Client();
        String sql = "SELECT * FROM clientes WHERE id_cliente = ?";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                cl.setId(rs.getInt("id_cliente"));
                cl.setName(rs.getString("nombre"));
                cl.setPhone(rs.getString("telefono"));
                cl.setAddress(rs.getString("direccion"));
            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return cl;
    }

}
