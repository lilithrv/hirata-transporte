/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package modelo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class RolTest {

    public RolTest() {
    }

    @Test
    public void testToString() {
        Rol rol = new Rol(1, "Administrador");
        String resultado = rol.toString();
        assertNotNull(resultado);
        assertTrue(resultado.contains("Administrador"));
    }

    @Test
    public void testConstructorVacio() {

        Rol rol = new Rol();

        // Verificamos que el objeto se creó (no es nulo)
        assertNotNull(rol);

        // Verificar que los valores por defecto sean los correctos
        assertEquals(0, rol.getIdRol());
        assertNull(rol.getNombreRol());
    }

    @Test
    public void testSetIdRol() {
        Rol rol = new Rol();
        rol.setIdRol(10);
        assertEquals(10, rol.getIdRol());
    }

    @Test
    public void testSetNombreRolExceptions() {
        Rol rol = new Rol();

        assertThrows(NullPointerException.class, () -> rol.setNombreRol(null));

        // Cubre el IllegalArgumentException por vacío 
        assertThrows(IllegalArgumentException.class, () -> rol.setNombreRol("   "));

        // Cubre el error de rango (Menos de 3 caracteres)
        assertThrows(IllegalArgumentException.class, () -> rol.setNombreRol("Ad"));

        // Cubre el error de rango (Más de 30 caracteres)
        assertThrows(IllegalArgumentException.class, () -> rol.setNombreRol("Este es un nombre de rol extremadamente largo para que falle"));
    }

    @Test
    public void testSetNombreRolExitoso() {
        Rol rol = new Rol();

        rol.setNombreRol("Conductor");
        assertEquals("Conductor", rol.getNombreRol());
    }

}
