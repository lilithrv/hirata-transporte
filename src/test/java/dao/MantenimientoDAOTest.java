/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package dao;

import conn.Conexion;
import java.util.List;
import modelo.Mantenimiento;
import modelo.Usuario;
import modelo.Vehiculo;
import modelo.enums.EstadoMantenimiento;
import modelo.enums.OrigenMantenimiento;
import modelo.enums.TipoMantenimiento;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import util.Sesion;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MantenimientoDAOTest {

    private static MantenimientoDAO dao;
    private static VehiculoDAO vehiculoDao;
    private static int idMantenimientoTest;
    private static final String PATENTE_VEHICULO = "FGFG76";

    @BeforeAll
    public static void setUpClass() {
        Conexion.setModoTest(true);
        dao = new MantenimientoDAO();
        vehiculoDao = new VehiculoDAO();

        // Simulamos una sesión activa (Necesario para los métodos actualizar)
        // Usamos el ID 2 que es Administrador de Mantenimiento
        Usuario admin = new Usuario();
        admin.setIdUsuario(2);
        admin.setNombreUsuario("Roberto Soto");
        Sesion.setUsuarioActivo(admin);

        // Limpieza y preparación de vehículo de prueba
        vehiculoDao.eliminar(PATENTE_VEHICULO);
        Vehiculo v = new Vehiculo();
        v.setPatente(PATENTE_VEHICULO);
        v.setMarca("TEST");
        v.setModelo("MANT");
        v.setAnio(2024);
        v.setKilometrajeInicial(10000);
        vehiculoDao.registrar(v);
    }

    @Test
    @Order(1)
    public void testProgramarMantenimientoSistema() {
        System.out.println("1. Ejecutando: Programar Mantenimiento (Sistema/Preventivo)");
        Vehiculo v = vehiculoDao.buscarPorPatente(PATENTE_VEHICULO);

        Mantenimiento m = new Mantenimiento();
        m.setVehiculo(v);
        m.setKilometraje(15000);

        dao.programar(m);

        // Verificamos si se creó buscando en la lista
        List<Mantenimiento> lista = dao.listarTodos();
        Mantenimiento creado = lista.stream()
                .filter(man -> man.getVehiculo().getPatente().equals(PATENTE_VEHICULO))
                .findFirst()
                .orElse(null);

        assertNotNull(creado);
        assertEquals(EstadoMantenimiento.Programado, creado.getEstado());
        idMantenimientoTest = creado.getIdMantenimiento();
    }

    @Test
    @Order(2)
    public void testProgramarMantenimientoCorrectivo() {
        System.out.println("2. Ejecutando: Programar Mantenimiento Correctivo");
        Vehiculo v = vehiculoDao.buscarPorPatente(PATENTE_VEHICULO);

        Mantenimiento m = new Mantenimiento();
        m.setVehiculo(v);
        m.setTipoMantenimiento(TipoMantenimiento.Correctivo);
        m.setOrigen(OrigenMantenimiento.Manual);
        m.setKilometraje(16000);

        dao.correctivo(m);

        assertTrue(dao.tieneMantenimientoProgramado(v.getIdVehiculo()));
    }

    @Test
    @Order(3)
    public void testActualizarMantenimiento() {
        System.out.println("3. Ejecutando: Actualizar Mantenimiento (Completar)");
        Mantenimiento m = dao.obtenerPorId(idMantenimientoTest);
        assertNotNull(m);

        m.setDescripcion("Cambio de aceite realizado");
        m.setEstado(EstadoMantenimiento.Completado);

        dao.actualizar(m);

        Mantenimiento verificado = dao.obtenerPorId(idMantenimientoTest);
        assertEquals(EstadoMantenimiento.Completado, verificado.getEstado());
        assertNotNull(verificado.getUsuarioMantenimiento());
        assertEquals(2, verificado.getUsuarioMantenimiento().getIdUsuario());
    }

    @Test
    @Order(4)
    public void testActualizar2ConFecha() {
        System.out.println("4. Ejecutando: Actualizar2 ");

        
        Vehiculo v = vehiculoDao.buscarPorPatente(PATENTE_VEHICULO);
        Mantenimiento mNuevo = new Mantenimiento();
        mNuevo.setVehiculo(v);
        mNuevo.setKilometraje(20000);
        dao.programar(mNuevo); // Se crea como Programado

        // Recuperamos el ID del que acabamos de crear
        Mantenimiento m = dao.listarTodos().get(0);

        m.setEstado(EstadoMantenimiento.Completado); 
        m.setDescripcion("Prueba de fecha completado");

        boolean result = dao.actualizar2(m);
        assertTrue(result);

        Mantenimiento verificado = dao.obtenerPorId(m.getIdMantenimiento());
        assertNotNull(verificado.getFechaCompletado());

        // Limpiamos este temporal
        dao.eliminar(m.getIdMantenimiento());
    }

    @Test
    @Order(5)
    public void testObtenerKmUltimoMantenimiento() {
        System.out.println("5. Ejecutando: Obtener KM Último Mantenimiento");
        Vehiculo v = vehiculoDao.buscarPorPatente(PATENTE_VEHICULO);
        Integer km = dao.obtenerKmUltimoMantenimiento(v.getIdVehiculo());

        assertNotNull(km);
        assertTrue(km >= 15000);
    }

//    @Test
//    @Order(6)
//    public void testListarVehiculosSinMantenimientoProgramado() {
//        System.out.println("6. Ejecutando: Listar Vehículos Sin Mantención Pendiente");
//        // Como completamos el mantenimiento en el test 3, el vehículo debería aparecer aquí
//        List<Vehiculo> lista = dao.listarSinMantenimientoProgramado();
//
//        boolean aparece = lista.stream()
//                .anyMatch(veh -> veh.getPatente().equals(PATENTE_VEHICULO));
//
//        assertTrue(aparece, "El vehículo debería aparecer en la lista porque ya no tiene nada 'Programado'");
//    }
    @Test
    @Order(7)
    public void testEliminarMantenimiento() {
        System.out.println("7. Ejecutando: Eliminar Mantenimiento");
        dao.eliminar(idMantenimientoTest);

        Mantenimiento eliminado = dao.obtenerPorId(idMantenimientoTest);
        assertNull(eliminado);
    }

}
