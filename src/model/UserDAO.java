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
import model.classes.User;

/**
 *
 * @author Antonio
 */
public class UserDAO {

    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    SQLConnection cn = new SQLConnection();

    public User log(String email, String pass) {
        User userLog = new User();
        String sql = "SELECT * FROM usuarios WHERE email = ? AND contrasena = ?";
        try {
            con = cn.getConnectDB();
            ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            if (rs.next()) {
                userLog.setId(rs.getInt("id_usuario"));
                userLog.setName(rs.getString("nombre"));
                userLog.setEmail(rs.getString("email"));
                userLog.setPassword(rs.getString("contrasena"));

            }
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return userLog;
    }
}
