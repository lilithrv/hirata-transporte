/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class Conexion {
    private static final String NAME_DB = "transporte_hirata";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String CONN = "jdbc:mysql://" + HOST + ":" + PORT + "/" + NAME_DB + "?useSSL=false&serverTimezone=UTC";
    
// comenzar con el patron singleton
    private static Connection conn;

    private Conexion() {
        try {
            // Cargar el driver (opcional desde JDBC 4.0, pero se puede incluir)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Crear la conexión
            conn = DriverManager.getConnection(CONN, USER, PASSWORD);
            System.out.println("✅ Conexión exitosa a MySQL 8.0.33");
        } catch (ClassNotFoundException e) {
            System.out.println("❌ No se encontró el driver JDBC");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar con la base de datos");
            e.printStackTrace();
        }
    }

    // Método para obtener la instancia única
    public static Connection getInstancia() {
        if (conn == null) {
            new Conexion();
        }
        return conn;
    }
    
    // Cierre
    public static void close() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                conn = null;
                System.out.println("Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar: " + e.getMessage());
        }
    }
}
