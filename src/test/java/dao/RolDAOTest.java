/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conn.Conexion;
import java.util.List;
import modelo.Rol;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RolDAOTest {

    private static RolDAO dao;
    private static int idRolTest;
    private static final String NOMBRE_INICIAL = "Rol Test JUnit";
    private static final String NOMBRE_ACTUALIZADO = "Rol Test Editado";

    @BeforeAll
    public static void setUpClass() {
        Conexion.setModoTest(true);
        dao = new RolDAO();

        // Limpieza por si quedó desde una ejecución anterior
        List<Rol> roles = dao.listarTodos();
        roles.stream()
                .filter(r -> NOMBRE_INICIAL.equals(r.getNombreRol()) || NOMBRE_ACTUALIZADO.equals(r.getNombreRol()))
                .forEach(r -> dao.eliminar(r.getIdRol()));
    }

    @Test
    @Order(1)
    public void testInsertarRol() {
        System.out.println("1. Ejecutando: Insertar Rol");

        Rol rol = new Rol();
        rol.setNombreRol(NOMBRE_INICIAL);
        dao.insertar(rol);

        Rol creado = dao.listarTodos().stream()
                .filter(r -> NOMBRE_INICIAL.equals(r.getNombreRol()))
                .findFirst()
                .orElse(null);

        assertNotNull(creado, "El rol debería haberse insertado");
        idRolTest = creado.getIdRol();
    }

    @Test
    @Order(2)
    public void testListarTodos() {
        System.out.println("2. Ejecutando: Listar Roles");

        List<Rol> roles = dao.listarTodos();

        assertNotNull(roles);
        assertFalse(roles.isEmpty(), "La lista de roles no debería estar vacía");
        assertTrue(roles.stream().anyMatch(r -> r.getIdRol() == idRolTest));
    }

    @Test
    @Order(3)
    public void testObtenerPorId() {
        System.out.println("3. Ejecutando: Obtener Rol por ID");

        Rol rol = dao.obtenerPorId(idRolTest);

        assertNotNull(rol);
        assertEquals(NOMBRE_INICIAL, rol.getNombreRol());
    }

    @Test
    @Order(4)
    public void testActualizar2Rol() {
        System.out.println("4. Ejecutando: Actualizar2 Rol");

        Rol rol = dao.obtenerPorId(idRolTest);
        assertNotNull(rol);

        rol.setNombreRol(NOMBRE_ACTUALIZADO);
        boolean actualizado = dao.actualizar2(rol);

        assertTrue(actualizado, "Debería actualizar correctamente el rol");

        Rol verificado = dao.obtenerPorId(idRolTest);
        assertNotNull(verificado);
        assertEquals(NOMBRE_ACTUALIZADO, verificado.getNombreRol());
    }

    @Test
    @Order(5)
    public void testEliminarRol() {
        System.out.println("5. Ejecutando: Eliminar Rol");

        dao.eliminar(idRolTest);

        Rol eliminado = dao.obtenerPorId(idRolTest);
        assertNull(eliminado, "El rol debería haberse eliminado");
    }
}
