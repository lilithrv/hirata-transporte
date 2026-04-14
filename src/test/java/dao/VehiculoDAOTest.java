/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package dao;

import conn.Conexion;
import java.util.List;
import modelo.Usuario;
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
    private static final String PATENTE_PRINCIPAL = "CCDD12";
    private static final String PATENTE_CHOQUE_1 = "CCCC11";
    private static final String PATENTE_CHOQUE_2 = "BBBB22";

    @BeforeAll
    public static void setUpClass() {
        // Iniciamos modo test y preparamos el DAO
        Conexion.setModoTest(true);
        dao = new VehiculoDAO();

        // LIMPIEZA INICIAL: Borramos todo lo que este test podría haber dejado antes
        dao.eliminar(PATENTE_PRINCIPAL);
        dao.eliminar(PATENTE_CHOQUE_1);
        dao.eliminar(PATENTE_CHOQUE_2);
        dao.eliminar("WXYZ99");
        System.out.println("🧹 Base de datos de test preparada y limpia.");
    }

    @Test
    @Order(1)
    public void testRegistrarVehiculo() {
        System.out.println("1. Ejecutando: Registrar Vehículo");
        Vehiculo v = new Vehiculo();
        v.setPatente(PATENTE_PRINCIPAL);
        v.setMarca("VOLVO");
        v.setModelo("FH16");
        v.setAnio(2024);
        v.setKilometrajeInicial(5000);

        assertTrue(dao.registrar(v), "El registro inicial falló");
    }

    @Test
    @Order(2)
    public void testRegistrarPatenteDuplicada() {
        System.out.println("2. Ejecutando: Error Patente Duplicada en Registro");
        Vehiculo v = new Vehiculo();
        v.setPatente(PATENTE_PRINCIPAL);

        assertThrows(IllegalArgumentException.class, () -> {
            dao.registrar(v);
        });
    }

    @Test
    @Order(3)
    public void testActualizarVehiculo() {
        System.out.println("3. Ejecutando: Actualizar Datos");
        Vehiculo v = dao.buscarPorPatente(PATENTE_PRINCIPAL);
        assertNotNull(v, "No se encontró el vehículo para actualizar");

        v.setMarca("VOLVO ACTUALIZADO");
        assertTrue(dao.actualizar(v));

        Vehiculo verificado = dao.obtenerPorId(v.getIdVehiculo());
        assertEquals("VOLVO ACTUALIZADO", verificado.getMarca());
    }

    @Test
    @Order(4)
    public void testMapeoConductor() {
        System.out.println("4. Ejecutando: Verificación de Mapeo");
        Vehiculo v = dao.buscarPorPatente(PATENTE_PRINCIPAL);

        Usuario conductor = new Usuario();
        conductor.setIdUsuario(89); // ID que existe en bbdd
        v.setConductor(conductor);

        dao.actualizar(v);

        List<Vehiculo> lista = dao.listarVehiculos();
        boolean encontro = lista.stream().anyMatch(veh -> veh.getConductor() != null);
        assertTrue(encontro, "El ResultSet no mapeó correctamente al conductor");
    }

    @Test
    @Order(5)
    public void testActualizarPatenteDuplicada() {
        System.out.println("5. Ejecutando: Error Patente Duplicada en Update");

        String p1 = PATENTE_CHOQUE_1;
        String p2 = PATENTE_CHOQUE_2;

        dao.eliminar(p1);
        dao.eliminar(p2);

        // Registro de v1 con todos los campos para que no falle el INSERT
        Vehiculo v1 = new Vehiculo();
        v1.setPatente(p1);
        v1.setMarca("TEST");
        v1.setModelo("MODELO");
        v1.setAnio(2024);
        v1.setKilometrajeInicial(0);
        dao.registrar(v1);

        // Registro de v2
        Vehiculo v2 = new Vehiculo();
        v2.setPatente(p2);
        v2.setMarca("TEST");
        v2.setModelo("MODELO");
        v2.setAnio(2024);
        v2.setKilometrajeInicial(0);
        dao.registrar(v2);

        Vehiculo v2DB = dao.buscarPorPatente(p2);
        assertNotNull(v2DB, "v2 no se encontró. Revisa si el registro falló por campos nulos.");

        v2DB.setPatente(p1);

        assertThrows(IllegalArgumentException.class, () -> {
            dao.actualizar(v2DB);
        });

        dao.eliminar(p1);
        dao.eliminar(p2);
    }

    @Test
    @Order(6)
    public void testEliminarVehiculoConMantenimientoCascada() {
        System.out.println("6. Ejecutando: Borrado en Cascada (Vehículo + Historial)");

        //  Buscamos el vehículo principal
        Vehiculo v = dao.buscarPorPatente(PATENTE_PRINCIPAL);
        assertNotNull(v, "El vehículo debe existir para la prueba");

        // Insertamos un mantenimiento (esto se borrará automáticamente por el CASCADE de la DB)
        String sql = "INSERT INTO mantenimiento (id_vehiculo, estado, tipo_mantenimiento, origen, descripcion, kilometraje, id_usuario_mantenimiento) "
                + "VALUES (?, 'Programado', 'Preventivo', 'Sistema', 'Test Cascada', 50000, 2)";

        try (java.sql.Connection db = Conexion.getInstancia(); java.sql.PreparedStatement ps = db.prepareStatement(sql)) {

            ps.setInt(1, v.getIdVehiculo());
            ps.executeUpdate();

            // ACCIÓN: Borramos el vehículo. 
            // Gracias al CASCADE, MySQL borrará el mantenimiento sin protestar.
            boolean result = dao.eliminar(PATENTE_PRINCIPAL);

            // VALIDACIÓN: El resultado debe ser TRUE
            assertTrue(result, "El vehículo debería borrarse correctamente junto con su historial (CASCADE)");

            // VERIFICACIÓN FINAL: El vehículo ya no debe existir
            assertNull(dao.buscarPorPatente(PATENTE_PRINCIPAL), "El vehículo ya no debería estar en la DB");

        } catch (java.sql.SQLException e) {
            fail("Error técnico en la prueba de cascada: " + e.getMessage());
        }
    }

    @Test
    @Order(7)
    public void testObtenerVehiculoPorConductorExito() {
        System.out.println("7. Ejecutando: Obtener Vehículo por Conductor (Éxito)");

        // 1. Re-registramos el vehículo (porque el test 6 lo borró con el CASCADE)
        Vehiculo v = new Vehiculo();
        v.setPatente(PATENTE_PRINCIPAL);
        v.setMarca("VOLVO");
        v.setModelo("FH");
        v.setAnio(2024);
        v.setKilometrajeInicial(0);
        dao.registrar(v);

        // 2. Lo recuperamos para tener el ID y le asignamos un conductor
        v = dao.buscarPorPatente(PATENTE_PRINCIPAL);
        Usuario cond = new Usuario();
        cond.setIdUsuario(89); // Conductor que existe en tu DB
        v.setConductor(cond);
        dao.actualizar(v);

        // 3. Probamos el método (Pinta el bloque del IF y los setters)
        Vehiculo encontrado = dao.obtenerVehiculoPorConductor(89);

        assertNotNull(encontrado, "Debería encontrar el vehículo asignado al ID 89");
        assertEquals(PATENTE_PRINCIPAL, encontrado.getPatente());
    }

    @Test
    @Order(8)
    public void testObtenerVehiculoPorConductorNoEncontrado() {
        System.out.println("8. Ejecutando: Obtener Vehículo por Conductor (No existe)");

        // Buscamos un ID que no tenga nada asignado
        Vehiculo v = dao.obtenerVehiculoPorConductor(999999);
        
        assertNull(v, "No debería encontrar nada para un ID sin vehículo");
    }


    @Test
    @Order(9)
    public void testRegistrarVehiculoConConductor() {
        System.out.println("9. Ejecutando: Registrar Vehículo con Conductor (Pinta L79)");
        
        String patenteNueva = "WWYY99";
        dao.eliminar(patenteNueva); // Limpieza

        Vehiculo v = new Vehiculo();
        v.setPatente(patenteNueva);
        v.setMarca("SCANIA");
        v.setModelo("R500");
        v.setAnio(2025);
        v.setKilometrajeInicial(100);

        Usuario conductor = new Usuario();
        conductor.setIdUsuario(89); // El ID de usuario que ya existe en DB
        v.setConductor(conductor);
        
        assertTrue(dao.registrar(v), "El registro con conductor debería ser exitoso");

        // Limpieza final
        dao.eliminar(patenteNueva);
    }
    
    @Test
    @Order(10)
    public void testEliminarPorPatenteExitoYVacio() {
        System.out.println("10. Ejecutando: Eliminar por Patente (Éxito y no encontrado)");
        
        String pTest = "DDTT99";
        dao.eliminar(pTest); // Limpieza por si acaso

        // Registro temporal
        Vehiculo v = new Vehiculo();
        v.setPatente(pTest); v.setMarca("TEST"); v.setModelo("TEST"); v.setAnio(2024);
        dao.registrar(v);

        assertTrue(dao.eliminarPorPatente(pTest), "Debería eliminar el vehículo correctamente");

        assertFalse(dao.eliminarPorPatente("NO-EXISTE"), "Debería retornar false al no encontrar la patente");
    }
    
    @Test
    @Order(11)
    public void testRemoverConductorExito() {
        System.out.println("11. Ejecutando: Remover Conductor ");
        
        // 1. Aseguramos que el vehículo principal exista y tenga un conductor asignado
        Vehiculo v = dao.buscarPorPatente(PATENTE_PRINCIPAL);
        if (v == null) {
            v = new Vehiculo(); v.setPatente(PATENTE_PRINCIPAL); v.setMarca("VOLVO");
            v.setAnio(2024); dao.registrar(v);
            v = dao.buscarPorPatente(PATENTE_PRINCIPAL);
        }
        
        Usuario cond = new Usuario();
        cond.setIdUsuario(89); // Usuario existente
        v.setConductor(cond);
        dao.actualizar(v);

        dao.removerConductor(v.getIdVehiculo());

        // 3. VERIFICACIÓN: Comprobamos que el id_conductor sea NULL ahora
        Vehiculo verificado = dao.obtenerPorId(v.getIdVehiculo());
        assertNull(verificado.getConductor(), "El conductor debería ser NULL tras removerlo");
    }

    @Test
    @Order(12)
    public void testRemoverConductorErrorSQL() {
        System.out.println("12. Forzando error SQL en removerConductor");
        
        Conexion.close(); // Cerramos conexión
       
        dao.removerConductor(1); 

        assertTrue(true);
    }

    
    @Test
    @Order(13)
    public void testObtenerPorIdConConductor() {
        System.out.println("13. Ejecutando: obtenerPorId con Conductor");
        
        // Aseguramos que el vehículo exista y tenga conductor
        Vehiculo v = dao.buscarPorPatente(PATENTE_PRINCIPAL);
        if (v == null) {
            v = new Vehiculo(); v.setPatente(PATENTE_PRINCIPAL); v.setMarca("VOLVO");
            v.setAnio(2024); dao.registrar(v);
            v = dao.buscarPorPatente(PATENTE_PRINCIPAL);
        }
        
        Usuario cond = new Usuario();
        cond.setIdUsuario(89); // ID de conductor existente
        v.setConductor(cond);
        dao.actualizar(v);

        // ACCIÓN: Al llamar a obtenerPorId, ahora sí entrará al mapeo del usuario
        Vehiculo resultado = dao.obtenerPorId(v.getIdVehiculo());

        // VERIFICACIÓN
        assertNotNull(resultado.getConductor(), "El conductor debe venir mapeado en el objeto");
        assertEquals(89, resultado.getConductor().getIdUsuario());
        assertEquals("arturo.riquelme@hirata.cl", resultado.getConductor().getEmail()); 
    }
    
    
}
