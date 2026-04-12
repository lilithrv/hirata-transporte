/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Leslie Reyes
 */
public class UsuarioTest {

    @Test
    public void testCorreoCorporativo() {
        Usuario u = new Usuario();

        String emailReal = "carlos.mendoza@hirata.cl";
        String nombreReal = "Carlos Mendoza";

        u.setNombreUsuario(nombreReal);
        u.setEmail(emailReal);

        assertEquals("Carlos Mendoza", u.getNombreUsuario());
        assertEquals("carlos.mendoza@hirata.cl", u.getEmail());
    }

    @Test
    public void testNombreNoPuedeSerNulo() {
        Usuario u = new Usuario();
        // Verificamos que lance NullPointerException si el nombre es null
        assertThrows(NullPointerException.class, () -> {
            u.setNombreUsuario(null);
        });
    }

    @Test
    public void testNombreNoPuedeSerVacio() {
        Usuario u = new Usuario();
        assertThrows(IllegalArgumentException.class, () -> u.setNombreUsuario("   "));
    }

    @Test
    public void testConstructores() {
        Rol rol = new Rol(1, "Admin");

        String correoValido = "gustavo@hirata.cl";
        String passValida = "1234";

        // Constructor con password
        Usuario u1 = new Usuario(1, "Gustavo", correoValido, passValida, rol);

        // Verificamos
        assertEquals("1234", u1.getPassword());
        assertEquals("gustavo@hirata.cl", u1.getEmail());

        // Constructor sin password
        Usuario u2 = new Usuario(2, "Leslie", "leslie@hirata.cl", rol);
        // Aquí es correcto que sea Null porque ese constructor no recibe pass
        assertNull(u2.getPassword());
    }

    @Test
    public void testIdUsuario() {
        Usuario u = new Usuario();
        u.setIdUsuario(100);
        assertEquals(100, u.getIdUsuario());
    }

    @Test
    public void testSetRol() {
        Usuario u = new Usuario();
        u.setRol(new Rol(3, "Conductor"));
        assertNotNull(u.getRol());
    }

    @Test
    public void testGetRol() {
        // Organizar (Arrange)
        Rol rol = new Rol(1, "Administrador de Flota");
        Usuario u = new Usuario();
        u.setRol(rol);

        assertEquals("Administrador de Flota", u.getRol().getNombreRol());
    }

    @Test
    public void testSetEmailNull() {
        Usuario u = new Usuario();
        assertThrows(NullPointerException.class, () -> {
            u.setEmail(null);
        });
    }

    @Test
    public void testSetEmailVacio() {
        Usuario u = new Usuario();
        assertThrows(IllegalArgumentException.class, () -> {
            u.setEmail("   ");
        });
    }

    @Test
    public void testPasswordErroresBasicos() {
        Usuario u = new Usuario();
        assertThrows(NullPointerException.class, () -> u.setPassword(null));
        assertThrows(IllegalArgumentException.class, () -> u.setPassword("   "));
    }

    @Test
    public void testPasswordConEmailInvalido() {
        Usuario u = new Usuario();
        u.setEmail("correo-malo"); // Formato incorrecto

        assertThrows(IllegalArgumentException.class, () -> u.setPassword("123456"));
    }

    @Test
    public void testPasswordExitoso() {
        Usuario u = new Usuario();
        u.setEmail("carlos@hirata.cl"); // Email válido para que pase el Regex

        u.setPassword("admin123");

        assertEquals("admin123", u.getPassword());
    }

    @Test
    public void testSetRolNullDebeFallar() {
        Usuario u = new Usuario();
        assertThrows(IllegalArgumentException.class, () -> {
            u.setRol(null);
        });
    }

    @Test
    public void testToString() {
        Rol rol = new Rol(3, "Conductor");
        Usuario u = new Usuario(1, "John", "j@hirata.cl", "1234", rol);

        String resultado = u.toString();

        // Verificamos que el String no sea nulo y que contenga datos clave
        assertNotNull(resultado);
        assertTrue(resultado.contains("John"));
        assertTrue(resultado.contains("idUsuario=1"));
        assertTrue(resultado.contains("Conductor"));
    }

}
