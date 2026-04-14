/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package modelo;

import java.util.Date;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class KilometrajeTest {

    public KilometrajeTest() {
    }

    @Test
    public void testConstructorLargoYGetters() {
        Usuario cond = new Usuario();
        Vehiculo veh = new Vehiculo();
        Date fecha = new Date();

        // Probamos el constructor con parámetros
        Kilometraje k = new Kilometraje(1, cond, veh, 500, fecha);

        assertEquals(1, k.getIdKilometraje());
        assertEquals(cond, k.getConductor());
        assertEquals(veh, k.getVehiculo());
        assertEquals(500, k.getKilometros());
        assertEquals(fecha, k.getFechaRegistro());
    }

    @Test
    public void testSettersYGetters() {
        Kilometraje k = new Kilometraje();
        Usuario cond = new Usuario();
        Vehiculo veh = new Vehiculo();
        Date fecha = new Date();

        // Asignamos valores
        k.setIdKilometraje(10);
        k.setConductor(cond);
        k.setVehiculo(veh);
        k.setKilometros(1200);
        k.setFechaRegistro(fecha);
        k.setDireccionOrigen("Santiago");
        k.setDireccionTermino("Valparaíso");

        // Verificamos
        assertEquals(10, k.getIdKilometraje());
        assertEquals(cond, k.getConductor());
        assertEquals(veh, k.getVehiculo());
        assertEquals(1200, k.getKilometros());
        assertEquals(fecha, k.getFechaRegistro());
        assertEquals("Santiago", k.getDireccionOrigen());
        assertEquals("Valparaíso", k.getDireccionTermino());
    }

    @Test
    public void testSetKilometrosInvalido() {
        Kilometraje k = new Kilometraje();
        assertThrows(IllegalArgumentException.class, () -> {
            k.setKilometros(-1);
        });
    }

    @Test
    public void testToString() {
        Kilometraje k = new Kilometraje();
        k.setKilometros(1500);
        String esperado = "Kilometraje registrado: 1500 km";
        assertEquals(esperado, k.toString());
    }

}
