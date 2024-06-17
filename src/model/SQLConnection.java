package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Antonio
 */
public class SQLConnection {

    public Connection ConnectionSQL;
    public static SQLConnection instancia;

    String host = "localhost";
    String port = "3308";
    String dbname = "sis_gestion_rep";

    String username = "root";
    String password = "root";

    String driver = "com.mysql.cj.jdbc.Driver";

    String dbURL = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
// + "?useSSL=false"

    public SQLConnection() {

    }

    public Connection getConnectDB() {
        try {
            Class.forName(driver);
            this.ConnectionSQL = DriverManager.getConnection(dbURL, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return ConnectionSQL;
    }
}
