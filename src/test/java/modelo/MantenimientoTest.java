/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package modelo;

import java.util.Date;
import modelo.enums.EstadoMantenimiento;
import modelo.enums.OrigenMantenimiento;
import modelo.enums.TipoMantenimiento;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author gust
 */
public class MantenimientoTest {

    public MantenimientoTest() {
    }

    @Test
    public void testConstructorYGetters() {
        Vehiculo v = new Vehiculo();
        Date fecha = new Date();

        Mantenimiento m = new Mantenimiento(1, v, fecha, TipoMantenimiento.Correctivo,
                OrigenMantenimiento.Manual, 15000, EstadoMantenimiento.Programado);

        assertEquals(1, m.getIdMantenimiento());
        assertEquals(OrigenMantenimiento.Manual, m.getOrigen());
        assertEquals(EstadoMantenimiento.Programado, m.getEstado());
    }

    @Test
    public void testGettersMantenimiento() {
        // Preparamos los objetos necesarios
        Vehiculo v = new Vehiculo();
        Date fechaCreacion = new Date();
        Date fechaCompletado = new Date();
        TipoMantenimiento tipo = TipoMantenimiento.Correctivo;

        Mantenimiento m = new Mantenimiento();

        // Seteamos los valores
        m.setVehiculo(v);
        m.setFechaCreacion(fechaCreacion);
        m.setFechaCompletado(fechaCompletado);
        m.setTipoMantenimiento(tipo);

        assertEquals(v, m.getVehiculo());
        assertEquals(fechaCreacion, m.getFechaCreacion());
        assertEquals(fechaCompletado, m.getFechaCompletado());
        assertEquals(tipo, m.getTipoMantenimiento());
    }

    @Test
    public void testGettersYSettersMantenimiento() {
        Mantenimiento m = new Mantenimiento();
        Usuario user = new Usuario();
        Vehiculo v = new Vehiculo();

        
        m.setDescripcion("Cambio de neumáticos delanteros");
        m.setKilometraje(12500);
        m.setUsuarioMantenimiento(user);
        m.setVehiculo(v);

       
        assertEquals("Cambio de neumáticos delanteros", m.getDescripcion());
        assertEquals(12500, m.getKilometraje());
        assertEquals(user, m.getUsuarioMantenimiento());
        assertEquals(v, m.getVehiculo());
    }

    @Test
    public void testSetEstadoBloqueado() {
        Mantenimiento m = new Mantenimiento();
        
        m.setEstado(EstadoMantenimiento.Completado);

        assertThrows(IllegalStateException.class, () -> m.setEstado(EstadoMantenimiento.Programado));
    }
    
    
    @Test
    public void testSettersRestantesMantenimiento() {
        Mantenimiento m = new Mantenimiento();
        Date fechaPrueba = new Date();

        //  Probamos setFechaCreacion y setFechaCompletado
        m.setFechaCreacion(fechaPrueba);
        m.setFechaCompletado(fechaPrueba);

        // Probamos los Enums
        m.setTipoMantenimiento(TipoMantenimiento.Correctivo);
        m.setOrigen(OrigenMantenimiento.Manual);

        
        assertEquals(fechaPrueba, m.getFechaCreacion());
        assertEquals(fechaPrueba, m.getFechaCompletado());
        assertEquals(TipoMantenimiento.Correctivo, m.getTipoMantenimiento());
        assertEquals(OrigenMantenimiento.Manual, m.getOrigen());
    }

    
    @Test
    public void testSetDescripcionObligatoriaAlFinalizar() {
        Mantenimiento m = new Mantenimiento();
        
        m.setEstado(EstadoMantenimiento.Completado);

        assertThrows(IllegalArgumentException.class, () -> {
            m.setDescripcion("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            m.setDescripcion(null);
        });
    }
    

    @Test
    public void testKilometrajeNegativo() {
        Mantenimiento m = new Mantenimiento();
        assertThrows(IllegalArgumentException.class, () -> m.setKilometraje(-500));
    }

    @Test
    public void testToString() {
        Mantenimiento m = new Mantenimiento();
        m.setIdMantenimiento(500);
        assertTrue(m.toString().contains("500"));
    }

}
