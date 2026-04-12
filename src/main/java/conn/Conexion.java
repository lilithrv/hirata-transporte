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

    // Bloque estático para cargar las propiedades una sola vez al iniciar la clase
    static {
        try (InputStream input = Conexion.class.getClassLoader().getResourceAsStream("config/dbConfig.properties")) {
            if (input == null) {
                System.out.println("❌ No se encontró config/dbConfig.properties. Usando valores por defecto.");
                dbActual = "transporte_hirata";
            } else {
                props.load(input);
                dbActual = props.getProperty("db.name");
                System.out.println("⚙️ Configuración de base de datos cargada.");
            }
        } catch (IOException e) {
            System.err.println("❌ Error al leer el archivo de configuración: " + e.getMessage());
        }
    }

    // Constructor privado para cumplir con el patrón Singleton estrictamente
    private Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Construimos la URL usando las propiedades del archivo
            String host = props.getProperty("db.host", "localhost");
            String port = props.getProperty("db.port", "3306");
            String user = props.getProperty("db.user", "root");
            String pass = props.getProperty("db.pass", "root");

            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbActual
                    + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

            conn = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Singleton conectado exitosamente a: " + dbActual);

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("❌ Error en la conexión Singleton");
            e.printStackTrace();
        }
    }

    /**
     * El "Interruptor" para los Tests Unitarios. Cambia el nombre de la DB y
     * reinicia la instancia.
     */
    public static void setModoTest(boolean esTest) {
        dbActual = esTest ? props.getProperty("db.test_name", "mantenimiento_test")
                : props.getProperty("db.name", "transporte_hirata");
        // Forzamos el cierre para que la próxima llamada a getInstancia cree una nueva con la otra DB
        close();
    }

    // Método para obtener la instancia única (Lazy Initialization)
    public static Connection getInstancia() {
        if (conn == null) {
            new Conexion();
        }
        return conn;
    }

    // Cierre de conexión seguro
    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("🔌 Conexión [" + dbActual + "] cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al cerrar: " + e.getMessage());
        } finally {
            conn = null; // Fundamental para que el Singleton pueda reiniciarse si cambiamos de DB
        }
    }

}
