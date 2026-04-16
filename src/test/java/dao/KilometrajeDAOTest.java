/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conn.Conexion;
import java.util.List;
import modelo.Kilometraje;
import modelo.Usuario;
import modelo.Vehiculo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class KilometrajeDAOTest {

    private static KilometrajeDAO dao;
    private static VehiculoDAO vehiculoDao;
    private static int idVehiculoConHistorial;
    private static int idVehiculoSinHistorial;
    private static final int ID_CONDUCTOR_TEST = 89;
    private static final String PATENTE_HISTORIAL = "KMTJ88";
    private static final String PATENTE_SIN_HISTORIAL = "KMSH89";

    @BeforeAll
    public static void setUpClass() {
        Conexion.setModoTest(true);
        dao = new KilometrajeDAO();
        vehiculoDao = new VehiculoDAO();

        vehiculoDao.eliminar(PATENTE_HISTORIAL);
        vehiculoDao.eliminar(PATENTE_SIN_HISTORIAL);

        Vehiculo vehiculo1 = new Vehiculo();
        vehiculo1.setPatente(PATENTE_HISTORIAL);
        vehiculo1.setMarca("VOLVO");
        vehiculo1.setModelo("FH16");
        vehiculo1.setAnio(2024);
        vehiculo1.setKilometrajeInicial(1000);
        vehiculoDao.registrar(vehiculo1);

        Vehiculo vehiculo2 = new Vehiculo();
        vehiculo2.setPatente(PATENTE_SIN_HISTORIAL);
        vehiculo2.setMarca("SCANIA");
        vehiculo2.setModelo("R500");
        vehiculo2.setAnio(2024);
        vehiculo2.setKilometrajeInicial(500);
        vehiculoDao.registrar(vehiculo2);

        idVehiculoConHistorial = vehiculoDao.buscarPorPatente(PATENTE_HISTORIAL).getIdVehiculo();
        idVehiculoSinHistorial = vehiculoDao.buscarPorPatente(PATENTE_SIN_HISTORIAL).getIdVehiculo();
    }

    @Test
    @Order(1)
    public void testInsertarKilometraje() {
        System.out.println("1. Ejecutando: Insertar Kilometraje");

        Usuario conductor = new Usuario();
        conductor.setIdUsuario(ID_CONDUCTOR_TEST);

        Vehiculo vehiculo = vehiculoDao.obtenerPorId(idVehiculoConHistorial);

        Kilometraje km = new Kilometraje();
        km.setConductor(conductor);
        km.setVehiculo(vehiculo);
        km.setKilometros(1500);
        km.setDireccionOrigen("Santiago Centro");
        km.setDireccionTermino("Pudahuel");

        boolean resultado = dao.insertarKilometraje(km);
        assertTrue(resultado, "El kilometraje debería insertarse correctamente");
    }

    @Test
    @Order(2)
    public void testVerTodos() {
        System.out.println("2. Ejecutando: Ver Todos los Kilometrajes");

        List<Kilometraje> lista = dao.verTodos();

        assertNotNull(lista);
        assertFalse(lista.isEmpty(), "La lista no debería venir vacía");
        assertTrue(lista.stream().anyMatch(k -> PATENTE_HISTORIAL.equals(k.getVehiculo().getPatente())));
    }

    @Test
    @Order(3)
    public void testUltimoKmPorVehiculo() throws InterruptedException {
        System.out.println("3. Ejecutando: Último KM por Vehículo");

        Usuario conductor = new Usuario();
        conductor.setIdUsuario(ID_CONDUCTOR_TEST);

        Vehiculo vehiculo = vehiculoDao.obtenerPorId(idVehiculoConHistorial);

        // Espera breve para asegurar distinto timestamp y orden correcto
        Thread.sleep(1100);

        Kilometraje kmNuevo = new Kilometraje();
        kmNuevo.setConductor(conductor);
        kmNuevo.setVehiculo(vehiculo);
        kmNuevo.setKilometros(1800);
        kmNuevo.setDireccionOrigen("Quilicura");
        kmNuevo.setDireccionTermino("Maipú");
        assertTrue(dao.insertarKilometraje(kmNuevo));

        Kilometraje ultimo = dao.ultimoKmPorVehiculo(idVehiculoConHistorial);

        assertNotNull(ultimo);
        assertEquals(1800, ultimo.getKilometros());
        assertEquals(PATENTE_HISTORIAL, ultimo.getVehiculo().getPatente());
        assertEquals(ID_CONDUCTOR_TEST, ultimo.getConductor().getIdUsuario());
    }

    @Test
    @Order(4)
    public void testHistorialPorConductor() {
        System.out.println("4. Ejecutando: Historial por Conductor");

        List<Kilometraje> historial = dao.historialPorConductor(ID_CONDUCTOR_TEST);

        assertNotNull(historial);
        assertFalse(historial.isEmpty(), "El conductor debería tener historial");

        Kilometraje registro = historial.stream()
                .filter(k -> PATENTE_HISTORIAL.equals(k.getVehiculo().getPatente()))
                .findFirst()
                .orElse(null);

        assertNotNull(registro, "Debería existir al menos un registro del vehículo de prueba");
        assertEquals(ID_CONDUCTOR_TEST, registro.getConductor().getIdUsuario());
        assertNotNull(registro.getDireccionOrigen());
        assertNotNull(registro.getDireccionTermino());
    }

    @Test
    @Order(5)
    public void testUltimoKmPorVehiculoSinHistorial() {
        System.out.println("5. Ejecutando: Último KM por Vehículo sin historial");

        Kilometraje ultimo = dao.ultimoKmPorVehiculo(idVehiculoSinHistorial);

        assertNull(ultimo, "Si el vehículo no tiene kilometrajes, debe retornar null");
    }
}
