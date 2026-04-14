/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conn;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class Conexion {

    private static final Properties props = new Properties();
    private static String dbActual;
    private static Connection conn;

    static {
        try (InputStream input = Conexion.class.getClassLoader().getResourceAsStream("config/dbConfig.properties")) {
            if (input == null) {
                System.out.println("❌ No se encontró config/dbConfig.properties. Usando transporte_hirata.");
                dbActual = "transporte_hirata";
            } else {
                props.load(input);
                dbActual = props.getProperty("db.name");
                System.out.println("⚙️ Configuración cargada con éxito.");
            }
        } catch (IOException e) {
            System.err.println("❌ Error crítico al leer configuración: " + e.getMessage());
        }
    }

    private Conexion() {
        conectar();
    }

    // Encapsulamos la lógica de conexión para poder llamarla al re-conectar
    private static void conectar() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String host = props.getProperty("db.host", "localhost");
            String port = props.getProperty("db.port", "3306");
            String user = props.getProperty("db.user", "root");
            String pass = props.getProperty("db.pass", "root");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbActual
                    + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Conectado a la base de datos: " + dbActual);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("❌ Error crítico en el Driver o SQL: " + e.getMessage());
        }
    }

    public static void setModoTest(boolean esTest) {
        dbActual = esTest ? props.getProperty("db.test_name", "mantenimiento_test")
                : props.getProperty("db.name", "transporte_hirata");
        close(); // Cerramos para que getInstancia() fuerce una nueva conexión a la otra DB
    }

    /**
     * MÉTODO CLAVE: Ahora verifica si la conexión está cerrada o es null.
     */
    public static Connection getInstancia() {
        try {
            // Si no existe O si existe pero se cerró por error, volvemos a conectar
            if (conn == null || conn.isClosed()) {
                conectar();
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Re-conectando por error de estado: " + e.getMessage());
            conectar();
        }
        return conn;
    }

    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("🔌 Conexión [" + dbActual + "] cerrada.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al cerrar: " + e.getMessage());
        } finally {
            conn = null;
        }
    }

}
