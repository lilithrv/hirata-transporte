/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conn.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Mantenimiento;
import modelo.Rol;
import modelo.Usuario;
import modelo.Vehiculo;
import modelo.enums.EstadoMantenimiento;
import modelo.enums.OrigenMantenimiento;
import modelo.enums.TipoMantenimiento;
import util.Sesion;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class MantenimientoDAO {

    //PROGRAMAR MANTENIMIENTO POR ALERTA
    public void programar(Mantenimiento mantenimiento) {
        //origen default 'Sistema'
        //tipo_mantenimiento default 'Preventivo'
        //descripcion no aplica ac
        //estado default 'Programado'
        String sql = "INSERT INTO mantenimiento (id_vehiculo, kilometraje) "
                + "VALUES (?, ?)";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mantenimiento.getVehiculo().getIdVehiculo());
            ps.setInt(2, mantenimiento.getKilometraje());

            int filas = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al programar el mantenimiento: " + e.getMessage());
        }
    }

    //PROGRAMAR MANTENIMIENTO CORRECTIVO
    public void correctivo(Mantenimiento mantenimiento) {
        String sql = "INSERT INTO mantenimiento (id_vehiculo, tipo_mantenimiento, origen, kilometraje) "
                + "VALUES (?, ?, ?, ?)";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mantenimiento.getVehiculo().getIdVehiculo());
            ps.setString(2, mantenimiento.getTipoMantenimiento().name());
            ps.setString(3, mantenimiento.getOrigen().name());
            ps.setInt(4, mantenimiento.getKilometraje());

            int filas = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al programar el mantenimiento: " + e.getMessage());
        }
    }

    //ACTUALIZAR MANTENIMIENTO
    public void actualizar(Mantenimiento mantenimiento) {
        String sql = "UPDATE mantenimiento SET descripcion = ?, estado = ?, id_usuario_mantenimiento = ?  "
                + "WHERE id_mantenimiento = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mantenimiento.getDescripcion());
            ps.setString(2, mantenimiento.getEstado().name());
            if (mantenimiento.getEstado() == EstadoMantenimiento.Completado
                    || mantenimiento.getEstado() == EstadoMantenimiento.Cancelado) {
                ps.setInt(3, Sesion.getUsuarioActivo().getIdUsuario());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            ps.setInt(4, mantenimiento.getIdMantenimiento());

            int filas = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar mantenimiento: " + e.getMessage());
        }
    }

    public boolean actualizar2(Mantenimiento mantenimiento) {
        String sql = "UPDATE mantenimiento SET descripcion = ?, estado = ?, id_usuario_mantenimiento = ?,  "
                + "fecha_completado = CASE WHEN ? IN ('Completado', 'Cancelado') THEN NOW() ELSE NULL END "
                + "WHERE id_mantenimiento = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, mantenimiento.getDescripcion());
            ps.setString(2, mantenimiento.getEstado().name());
            if (mantenimiento.getEstado() == EstadoMantenimiento.Completado
                    || mantenimiento.getEstado() == EstadoMantenimiento.Cancelado) {
                ps.setInt(3, Sesion.getUsuarioActivo().getIdUsuario());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER);
            }

            ps.setString(4, mantenimiento.getEstado().name());
            ps.setInt(5, mantenimiento.getIdMantenimiento());

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    //ELIMINAR MANTENIMIENTO
    public void eliminar(int id) {
        String sql = "DELETE FROM mantenimiento WHERE id_mantenimiento = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Mantenimiento eliminado correctamente. ID: " + id);
            } else {
                System.out.println("No se encontró mantenimiento con ID: " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar mantenimiento:" + e.getMessage());
        }
    }

    //LISTAR
    public List<Mantenimiento> listarTodos() {
        List<Mantenimiento> listado = new ArrayList<>();

        String sql = "SELECT m.id_mantenimiento, m.tipo_mantenimiento, m.origen, "
                + "m.descripcion, m.kilometraje, m.estado, "
                + "m.fecha_creacion, m.fecha_completado, "
                + "v.id_vehiculo, v.patente, v.marca, v.modelo, v.anio, "
                + "c.nombre AS nombre_conductor, " // <-- conductor del vehículo
                + "u.id_usuario, u.nombre AS nombre_usuario "
                + "FROM mantenimiento m "
                + "JOIN vehiculos v ON m.id_vehiculo = v.id_vehiculo "
                + "LEFT JOIN usuarios c ON v.id_conductor = c.id_usuario " // <-- JOIN conductor
                + "LEFT JOIN usuarios u ON m.id_usuario_mantenimiento = u.id_usuario "
                + "ORDER BY m.fecha_creacion DESC";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Vehiculo vehiculo = new Vehiculo();
                    vehiculo.setIdVehiculo(rs.getInt("id_vehiculo"));
                    vehiculo.setPatente(rs.getString("patente"));
                    vehiculo.setMarca(rs.getString("marca"));
                    vehiculo.setModelo(rs.getString("modelo"));
                    vehiculo.setAnio(rs.getInt("anio"));

                    if (rs.getString("nombre_conductor") != null) {
                        Usuario conductor = new Usuario();
                        conductor.setNombreUsuario(rs.getString("nombre_conductor"));
                        vehiculo.setConductor(conductor);
                    }

                    // LEFT JOIN: el usuario puede ser null si está Programado
                    Usuario usuario = null;
                    if (rs.getInt("id_usuario") != 0) {
                        usuario = new Usuario();
                        usuario.setIdUsuario(rs.getInt("id_usuario"));
                        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    }

                    //se utilzan los setters porque no todos los campos son obligatorios
                    Mantenimiento mantenimiento = new Mantenimiento();
                    mantenimiento.setIdMantenimiento(rs.getInt("id_mantenimiento"));
                    mantenimiento.setVehiculo(vehiculo);
                    mantenimiento.setTipoMantenimiento(TipoMantenimiento.valueOf(rs.getString("tipo_mantenimiento")));
                    mantenimiento.setOrigen(OrigenMantenimiento.valueOf(rs.getString("origen")));
                    mantenimiento.setDescripcion(rs.getString("descripcion"));
                    mantenimiento.setKilometraje(rs.getInt("kilometraje"));
                    mantenimiento.setEstado(EstadoMantenimiento.valueOf(rs.getString("estado")));
                    mantenimiento.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    mantenimiento.setFechaCompletado(rs.getTimestamp("fecha_completado"));
                    mantenimiento.setUsuarioMantenimiento(usuario);

                    listado.add(mantenimiento);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar mantenimientos: " + e.getMessage());
            e.printStackTrace();
        }

        return listado;
    }

    //OBTENER POR ID
    public Mantenimiento obtenerPorId(int id) {
        String sql = "SELECT m.id_mantenimiento, m.tipo_mantenimiento, m.origen, "
                + "m.descripcion, m.kilometraje, m.estado, "
                + "m.fecha_creacion, m.fecha_completado, "
                + "v.id_vehiculo, v.patente, v.marca, v.modelo, v.anio, "
                + "c.nombre AS nombre_conductor, " // <--
                + "u.id_usuario, u.nombre AS nombre_usuario "
                + "FROM mantenimiento m "
                + "JOIN vehiculos v ON m.id_vehiculo = v.id_vehiculo "
                + "LEFT JOIN usuarios c ON v.id_conductor = c.id_usuario " // <--
                + "LEFT JOIN usuarios u ON m.id_usuario_mantenimiento = u.id_usuario "
                + "WHERE m.id_mantenimiento = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    Vehiculo vehiculo = new Vehiculo();
                    vehiculo.setIdVehiculo(rs.getInt("id_vehiculo"));
                    vehiculo.setPatente(rs.getString("patente"));
                    vehiculo.setMarca(rs.getString("marca"));
                    vehiculo.setModelo(rs.getString("modelo"));
                    vehiculo.setAnio(rs.getInt("anio"));

                    // Conductor del vehículo
                    if (rs.getString("nombre_conductor") != null) {
                        Usuario conductor = new Usuario();
                        conductor.setNombreUsuario(rs.getString("nombre_conductor"));
                        vehiculo.setConductor(conductor);
                    }

                    Usuario usuario = null;
                    if (rs.getInt("id_usuario") != 0) {
                        usuario = new Usuario();
                        usuario.setIdUsuario(rs.getInt("id_usuario"));
                        usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    }

                    Mantenimiento mantenimiento = new Mantenimiento();
                    mantenimiento.setIdMantenimiento(rs.getInt("id_mantenimiento"));
                    mantenimiento.setVehiculo(vehiculo);
                    mantenimiento.setTipoMantenimiento(TipoMantenimiento.valueOf(rs.getString("tipo_mantenimiento")));
                    mantenimiento.setOrigen(OrigenMantenimiento.valueOf(rs.getString("origen")));
                    mantenimiento.setDescripcion(rs.getString("descripcion"));
                    mantenimiento.setKilometraje(rs.getInt("kilometraje"));
                    mantenimiento.setEstado(EstadoMantenimiento.valueOf(rs.getString("estado")));
                    mantenimiento.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                    mantenimiento.setFechaCompletado(rs.getTimestamp("fecha_completado"));
                    mantenimiento.setUsuarioMantenimiento(usuario);

                    return mantenimiento;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener mantenimiento: " + e.getMessage());
        }

        return null;
    }

    //MANTENIMIENTO PROGRAMADO
    public boolean tieneMantenimientoProgramado(int idVehiculo) {
        String sql = "SELECT COUNT(*) FROM mantenimiento WHERE id_vehiculo = ? AND estado = 'Programado'";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVehiculo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar mantenimiento: " + e.getMessage());
        }
        return false;
    }

    public Integer obtenerKmUltimoMantenimiento(int idVehiculo) {
        String sql = """
            SELECT kilometraje FROM mantenimiento 
            WHERE id_vehiculo = ? 
            ORDER BY fecha_creacion DESC 
            LIMIT 1
            """;

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVehiculo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("kilometraje");
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener km último mantenimiento: " + e.getMessage());
        }
        return null; // null = nunca ha tenido mantenimiento
    }

    public List<Vehiculo> listarSinMantenimientoProgramado() {
        List<Vehiculo> lista = new ArrayList<>();

        // solo muestra los vehiculos que no tienen mantención pendiente programada
        // los con estado Cancelado o Completado se excluyen, solo los activos del momento
        String sql = "SELECT v.id_vehiculo, v.patente, v.marca, v.modelo, v.anio, "
                + "v.kilometraje_inicial, "
                + "u.nombre AS nombre_conductor "
                + "FROM vehiculos v "
                + "LEFT JOIN usuarios u ON v.id_conductor = u.id_usuario "
                + "WHERE NOT EXISTS ( "
                + "    SELECT 1 FROM mantenimiento m "
                + "    WHERE m.id_vehiculo = v.id_vehiculo "
                + "    AND m.estado = 'Programado' "
                + ") "
                + "ORDER BY v.patente ASC";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    Vehiculo vehiculo = new Vehiculo();
                    vehiculo.setIdVehiculo(rs.getInt("id_vehiculo"));
                    vehiculo.setPatente(rs.getString("patente"));
                    vehiculo.setMarca(rs.getString("marca"));
                    vehiculo.setModelo(rs.getString("modelo"));
                    vehiculo.setAnio(rs.getInt("anio"));
                    vehiculo.setKilometrajeInicial(rs.getInt("kilometraje_inicial"));

                    if (rs.getString("nombre_conductor") != null) {
                        Usuario conductor = new Usuario();
                        conductor.setNombreUsuario(rs.getString("nombre_conductor"));
                        vehiculo.setConductor(conductor);
                    }

                    lista.add(vehiculo);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar vehículos sin mantenimiento programado: " + e.getMessage());
        }

        System.out.println(lista);
        return lista;
    }
}
