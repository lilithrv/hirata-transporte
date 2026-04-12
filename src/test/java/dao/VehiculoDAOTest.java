/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package dao;

import conn.Conexion;
import java.util.List;
import modelo.Vehiculo;
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
public class VehiculoDAOTest {

    private static VehiculoDAO dao;
    private static final String PATENTE_TEST = "BBDD12"; //  Formato chileno válido
    

    @BeforeAll
    public static void setUpClass() {
        Conexion.setModoTest(true);
        dao = new VehiculoDAO();
        // Limpieza preventiva: nos aseguramos de que la patente no exista antes de empezar
        dao.eliminarPorPatente(PATENTE_TEST);
    }

    @Test
    @Order(1)
    public void testRegistrarVehiculo() {
        System.out.println("Ejecutando: Registrar Vehículo");
        Vehiculo v = new Vehiculo();
        v.setPatente(PATENTE_TEST);
        v.setMarca("VOLVO");
        v.setModelo("FH16");
        v.setAnio(2024);
        v.setKilometrajeInicial(5000);
        v.setConductor(null); // Registro sin conductor asignado primero

        boolean result = dao.registrar(v);
        assertTrue(result, "El vehículo debería registrarse correctamente");
    }

    @Test
    @Order(2)
    public void testRegistrarPatenteDuplicada() {
        System.out.println("Ejecutando: Registrar Patente Duplicada (Error)");
        Vehiculo v = new Vehiculo();
        v.setPatente(PATENTE_TEST); // Misma patente que el anterior

        // El DAO lanza IllegalArgumentException si la patente existe
        assertThrows(IllegalArgumentException.class, () -> {
            dao.registrar(v);
        }, "Debería lanzar excepción por patente duplicada");
    }

    @Test
    @Order(3)
    public void testBuscarPorPatente() {
        System.out.println("Ejecutando: Buscar por Patente");
        Vehiculo v = dao.buscarPorPatente(PATENTE_TEST);
        assertNotNull(v);
        assertEquals("VOLVO", v.getMarca());
    }

    @Test
    @Order(4)
    public void testListarVehiculos() {
        System.out.println("Ejecutando: Listar Flota");
        List<Vehiculo> lista = dao.listarVehiculos();
        assertFalse(lista.isEmpty(), "La lista no debería estar vacía");
    }

    @Test
    @Order(5)
    public void testActualizarVehiculo() {
        System.out.println("Ejecutando: Actualizar Vehículo");
        Vehiculo v = dao.buscarPorPatente(PATENTE_TEST);
        assertNotNull(v);

        v.setMarca("VOLVO ACTUALIZADO");
        v.setKilometrajeInicial(6000);

        boolean result = dao.actualizar(v);
        assertTrue(result);

        Vehiculo verificado = dao.obtenerPorId(v.getIdVehiculo());
        assertEquals("VOLVO ACTUALIZADO", verificado.getMarca());
    }

    @Test
    @Order(6)
    public void testRemoverConductorYObtenerPorID() {
        System.out.println("Ejecutando: Remover Conductor y Obtener por ID");
        Vehiculo v = dao.buscarPorPatente(PATENTE_TEST);

        // Probamos el método de remover conductor
        dao.removerConductor(v.getIdVehiculo());

        Vehiculo verificado = dao.obtenerPorId(v.getIdVehiculo());
        assertNull(verificado.getConductor(), "El conductor debería ser NULL ahora");
    }

    @Test
    @Order(7)
    public void testEliminarVehiculo() {
        System.out.println("Ejecutando: Eliminar Vehículo");
        boolean result = dao.eliminar(PATENTE_TEST);
        assertTrue(result, "El vehículo debería ser eliminado por patente");

        Vehiculo eliminado = dao.buscarPorPatente(PATENTE_TEST);
        assertNull(eliminado);
    }

}
