/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package modelo;

import java.time.Year;
import java.util.Date;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class VehiculoTest {

    public VehiculoTest() {
    }

    @Test
    public void testConstructorYGetters() {
        Usuario cond = new Usuario();
        Date ahora = new Date();
        Vehiculo v = new Vehiculo(1, cond, "BBCC12", "TOYOTA", "YARIS", 2020, 5000, ahora);

        assertEquals(1, v.getIdVehiculo());
        assertEquals(cond, v.getConductor());
        assertEquals("BBCC12", v.getPatente());
        assertEquals("TOYOTA", v.getMarca());
        assertEquals("YARIS", v.getModelo());
        assertEquals(2020, v.getAnio());
        assertEquals(5000, v.getKilometrajeInicial());
        assertEquals(ahora, v.getFechaRegistro());
    }

    @Test
    public void testSetIdVehiculoInvalido() {
        Vehiculo v = new Vehiculo();
        assertThrows(IllegalArgumentException.class, () -> v.setIdVehiculo(0));
        assertThrows(IllegalArgumentException.class, () -> v.setIdVehiculo(-1));
    }

    @Test
    public void testSetPatenteFormatosChilenos() {
        Vehiculo v = new Vehiculo();

        // Formato Antiguo (2 letras, 4 números)
        v.setPatente("AA1234");
        assertEquals("AA1234", v.getPatente());

        // Formato Moderno (4 letras, 2 números) - Sin vocales según tu Regex
        v.setPatente("BCDF 55");
        assertEquals("BCDF55", v.getPatente());

        // Formato Nuevo (5 letras, 1 número)
        v.setPatente("BCDFG-1");
        assertEquals("BCDFG1", v.getPatente());

        // Error: Patente inválida o con vocales (que tu regex moderno excluye)
        assertThrows(IllegalArgumentException.class, () -> v.setPatente("AEIO12"));
        assertThrows(IllegalArgumentException.class, () -> v.setPatente(""));
        assertThrows(IllegalArgumentException.class, () -> v.setPatente(null));
    }

    @Test
    public void testSetMarcaYModelo() {
        Vehiculo v = new Vehiculo();

        v.setMarca("Hyundai-N");
        assertEquals("HYUNDAI-N", v.getMarca());

        v.setModelo("Accent 1.6");
        assertEquals("ACCENT 1.6", v.getModelo());

        // Errores de Marca (Nulo, Vacío, Caracteres especiales prohibidos)
        assertThrows(IllegalArgumentException.class, () -> v.setMarca(null));
        assertThrows(IllegalArgumentException.class, () -> v.setMarca("   "));
        assertThrows(IllegalArgumentException.class, () -> v.setMarca("Marca123")); // Solo letras

        // Error largo > 50
        String largo = "ESTA ES UNA MARCA EXTREMADAMENTE LARGA PARA PROBAR EL ERROR DE CINCUENTA";
        assertThrows(IllegalArgumentException.class, () -> v.setMarca(largo));
    }

    @Test
    public void testSetAnio() {
        Vehiculo v = new Vehiculo();
        int anioFuturo = Year.now().getValue() + 2;

        v.setAnio(1980); // Límite inferior
        assertEquals(1980, v.getAnio());

        assertThrows(IllegalArgumentException.class, () -> v.setAnio(1979));
        assertThrows(IllegalArgumentException.class, () -> v.setAnio(anioFuturo));
    }

    @Test
    public void testKilometrajeYFecha() {
        Vehiculo v = new Vehiculo();
        v.setKilometrajeInicial(0);
        assertEquals(0, v.getKilometrajeInicial());

        assertThrows(IllegalArgumentException.class, () -> v.setKilometrajeInicial(-1));

        Date nuevaFecha = new Date();
        v.setFechaRegistro(nuevaFecha);
        assertEquals(nuevaFecha, v.getFechaRegistro());
    }

    @Test
    public void testToString() {
        Vehiculo v = new Vehiculo();
        v.setMarca("KIA");
        assertTrue(v.toString().contains("KIA"));
    }

    @Test
    public void testAsignacionesExitosas() {
        Vehiculo v = new Vehiculo();
        Usuario u = new Usuario(); // Creamos un conductor de prueba

        v.setIdVehiculo(5);
        assertEquals(5, v.getIdVehiculo());

        v.setConductor(u);
        assertEquals(u, v.getConductor());
    }

    @Test
    public void testSetModeloExceptions() {
        Vehiculo v = new Vehiculo();

        // Nulo o Vacío
        assertThrows(IllegalArgumentException.class, () -> v.setModelo(null));
        assertThrows(IllegalArgumentException.class, () -> v.setModelo("   "));

        // Largo > 50
        String modeloMuyLargo = "MODELO-SUPER-EXTRA-LARGO-QUE-SOBREPASA-LOS-CINCUENTA-CARACTERES-PERMITIDOS";
        assertThrows(IllegalArgumentException.class, () -> v.setModelo(modeloMuyLargo));

        // regex permite letras, números, espacios, guiones y puntos.
        assertThrows(IllegalArgumentException.class, () -> v.setModelo("Yaris @ 2024"));
    }

}
