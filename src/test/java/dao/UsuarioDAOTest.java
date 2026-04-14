/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package dao;

import conn.Conexion;
import java.util.List;
import modelo.Rol;
import modelo.Usuario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioDAOTest {

    private static UsuarioDAO dao;

    @BeforeAll
    public static void setUpClass() {
        //Activamos el modo test para apuntar a mantenimiento_test
        Conexion.setModoTest(true);
        dao = new UsuarioDAO();
    }

    @Test
    @Order(1)
    public void testInsertarUsuario() {
        System.out.println("Ejecutando: Insertar Usuario");

        // Creamos un rol (asegúrate que el ID 1 existe en tu BD de test, ej: Administrador)
        Rol rol = new Rol();
        rol.setIdRol(1);
        rol.setNombreRol("Administrador");

        Usuario u = new Usuario();
        u.setNombreUsuario("Sacarias Rosas");
        u.setEmail("Sacarias.Rosas@hirata.cl");
        u.setPassword("pass123");
        u.setRol(rol);

        boolean result = dao.insertar(u);
        assertTrue(result, "El usuario debería insertarse correctamente");
    }

    @Test
    @Order(2)
    public void testVerificarCredencialesCorrectas() {
        System.out.println("Ejecutando: Verificar Credenciales");
        // El DAO usa BCrypt, así que pasamos la clave en texto plano y él compara el hash
        Usuario u = dao.verificarCredenciales("Sacarias.Rosas@hirata.cl", "pass123");

        assertNotNull(u, "Debería encontrar al usuario con credenciales válidas");
        assertEquals("Sacarias.Rosas@hirata.cl", u.getEmail());
    }

    @Test
    @Order(3)
    public void testListarTodos() {
        System.out.println("Ejecutando: Listar Usuarios");
        List<Usuario> lista = dao.listarTodos();
        assertFalse(lista.isEmpty(), "La lista no debería estar vacía");
    }

    @Test
    @Order(4)
    public void testActualizarUsuario() {
        // Este usa actualizar2
        System.out.println("Ejecutando: Actualizar Usuario (bool)");
        List<Usuario> lista = dao.listarTodos();
        Usuario u = lista.stream()
                .filter(user -> user.getEmail().equals("Sacarias.Rosas@hirata.cl"))
                .findFirst()
                .orElse(null);

        assertNotNull(u);
        u.setNombreUsuario("Sacarias Actualizado");
        u.setPassword("nuevaClave456");
        boolean actualizado = dao.actualizar2(u);
        assertTrue(actualizado);
    }

    @Test
    @Order(5)
    public void testActualizarVoid() {
        
        System.out.println("Ejecutando: Actualizar Usuario (void)");
        List<Usuario> lista = dao.listarTodos();
        Usuario u = lista.stream()
                .filter(user -> user.getEmail().equals("Sacarias.Rosas@hirata.cl"))
                .findFirst()
                .orElse(null);

        assertNotNull(u);
        u.setNombreUsuario("Nombre Temporal");
        dao.actualizar(u); 

        Usuario verificado = dao.obtenerPorId(u.getIdUsuario());
        assertEquals("Nombre Temporal", verificado.getNombreUsuario());
    }

    @Test
    @Order(6)
    public void testVerificarCredencialesEmailIncorrecto() {
        System.out.println("Ejecutando: Email Incorrecto");
        Usuario u = dao.verificarCredenciales("no_existo@test.cl", "123456");
        assertNull(u);
    }

    @Test
    @Order(7)
    public void testVerificarCredencialesPasswordIncorrecta() {
        System.out.println("Ejecutando: Password Incorrecta");
        // Usamos el usuario que todavía existe en la BD
        Usuario u = dao.verificarCredenciales("Sacarias.Rosas@hirata.cl", "clave_erronea");
        assertNull(u);
    }

    @Test
    @Order(8) // LA ELIMINACIÓN AL FINAL DE TODO
    public void testEliminarUsuario() {
        System.out.println("Ejecutando: Eliminar Usuario");
        List<Usuario> lista = dao.listarTodos();
        Usuario u = lista.stream()
                .filter(user -> user.getEmail().equals("Sacarias.Rosas@hirata.cl"))
                .findFirst()
                .orElse(null);

        assertNotNull(u);
        dao.eliminar(u.getIdUsuario());

        Usuario eliminado = dao.obtenerPorId(u.getIdUsuario());
        assertNull(eliminado);
    }
    
    @Test
    @Order(9) 
    public void testEliminarUsuarioInexistente() {
        System.out.println("Ejecutando: Eliminar ID inexistente");
        // Intentamos eliminar un ID que sabemos que no existe
        dao.eliminar(999999);

        Usuario u = dao.obtenerPorId(999999);
        assertNull(u);
    }
    
    @Test
    @Order(11)
    public void testListarConductores() {
        System.out.println("Ejecutando: Listar Conductores");


        List<Usuario> conductores = dao.listarConductores();
 
        assertNotNull(conductores, "La lista no debe ser nula");
        System.out.println("Conductores encontrados: " + conductores.size());
    }
    
    
    @Test
    @Order(12)
    public void testListarConductoresSinAsignacion() {
        System.out.println("Ejecutando: Listar Conductores Sin Asignación");

        // Llamamos al método
        List<Usuario> lista = dao.listarConductoresSinAsignacion();

        // Verificamos que no sea nulo 
        assertNotNull(lista, "La lista de conductores sin asignación no debería ser nula");

        System.out.println("Conductores libres encontrados: " + lista.size());
    }

}
