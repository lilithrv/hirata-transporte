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
   
    
}
