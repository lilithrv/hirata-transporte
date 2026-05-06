/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package conn;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.Connection;

/**
 *
 * @author leslie
 */
public class ConexionTest {

    @Test
    void testConexionExitosa() {
        Connection conn = Conexion.getInstancia();

        assertNotNull(conn);
    }

    @Test
    void testConexionUnica() {
        Connection conn1 = Conexion.getInstancia();
        Connection conn2 = Conexion.getInstancia();

        assertSame(conn1, conn2);
    }
}
