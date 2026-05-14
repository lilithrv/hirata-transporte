package dao;

import conn.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.EquipoOficina;
import modelo.TipoEquipo;

public class EquipoOficinaDAO {

    public List<EquipoOficina> listarActivos() {
        List<EquipoOficina> equipos = new ArrayList<>();
        String sql = "SELECT e.*, t.nombre AS tipo_nombre "
                + "FROM equipos_oficina e "
                + "JOIN tipos_equipo t ON e.id_tipo_equipo = t.id_tipo_equipo "
                + "WHERE e.estado = 'Activo' "
                + "ORDER BY e.id_equipo";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {

                TipoEquipo tipo = new TipoEquipo();
                tipo.setIdTipoEquipo(rs.getInt("id_tipo_equipo"));
                tipo.setNombre(rs.getString("tipo_nombre"));

                EquipoOficina eq = new EquipoOficina();
                eq.setIdEquipo(rs.getInt("id_equipo"));
                eq.setTipoEquipo(tipo);
                eq.setMarca(rs.getString("marca"));
                eq.setModelo(rs.getString("modelo"));
                eq.setNumeroSerie(rs.getString("numero_serie"));
                eq.setEstado(rs.getString("estado"));

                equipos.add(eq);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar equipos de oficina: " + e.getMessage());
        }
        return equipos;
    }

    public boolean existePorId(int idEquipo) {
        String sql = "SELECT COUNT(*) FROM equipos_oficina WHERE id_equipo = ?";
        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al validar equipo de oficina: " + e.getMessage());
            return false;
        }
    }

    public List<EquipoOficina> listarPorTipo(int idTipoEquipo) {
        List<EquipoOficina> lista = new ArrayList<>();
        String sql = "SELECT e.*, t.nombre AS tipo_nombre "
                + "FROM equipos_oficina e "
                + "JOIN tipos_equipo t ON e.id_tipo_equipo = t.id_tipo_equipo "
                + "WHERE e.id_tipo_equipo = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTipoEquipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    TipoEquipo tipo = new TipoEquipo();
                    tipo.setIdTipoEquipo(rs.getInt("id_tipo_equipo"));
                    tipo.setNombre(rs.getString("tipo_nombre"));

                    EquipoOficina eq = new EquipoOficina();
                    eq.setIdEquipo(rs.getInt("id_equipo"));
                    eq.setTipoEquipo(tipo);
                    eq.setMarca(rs.getString("marca"));
                    eq.setModelo(rs.getString("modelo"));
                    eq.setNumeroSerie(rs.getString("numero_serie"));
                    eq.setEstado(rs.getString("estado"));

                    lista.add(eq);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar equipos por tipo: " + e.getMessage());
        }
        return lista;
    }
    
     public List<String> obtenerEstadosUnicos() {
        List<String> estados = new ArrayList<>();
        String sql = "SELECT DISTINCT estado FROM equipos_oficina ORDER BY estado ASC";

        // Usamos tu método getInstancia()
        Connection conn = Conexion.getInstancia();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                estados.add(rs.getString("estado"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estados: " + e.getMessage());
        }
        return estados;
    }

    public List<EquipoOficina> listarTodo() {
        List<EquipoOficina> lista = new ArrayList<>();
        // JOIN para traer los nombres en lugar de solo IDs
        String sql = "SELECT e.*, t.nombre AS tipo_nombre, u.nombre AS responsable_nombre "
                + "FROM equipos_oficina e "
                + "JOIN tipos_equipo t ON e.id_tipo_equipo = t.id_tipo_equipo "
                + "LEFT JOIN usuarios u ON e.id_responsable = u.id_usuario "
                + "ORDER BY e.id_equipo";

        try (Connection cn = Conexion.getInstancia(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EquipoOficina eq = new EquipoOficina();

                // Datos básicos
                eq.setIdEquipo(rs.getInt("id_equipo"));
                eq.setMarca(rs.getString("marca"));
                eq.setModelo(rs.getString("modelo"));
                eq.setNumeroSerie(rs.getString("numero_serie"));
                eq.setEstado(rs.getString("estado"));
                eq.setIdResponsable(rs.getInt("id_responsable"));

                // MANEJO DE FECHAS 
                // rs.getDate devuelve un java.sql.Date, lo pasamos a String con .toString()
                if (rs.getDate("fecha_adquisicion") != null) {
                    eq.setFechaAdquisicion(rs.getDate("fecha_adquisicion").toString());
                }

                eq.setFechaRegistro(rs.getTimestamp("fecha_registro"));

                // MANEJO DEL TIPO
                TipoEquipo tipo = new TipoEquipo();
                tipo.setIdTipoEquipo(rs.getInt("id_tipo_equipo"));
                tipo.setNombre(rs.getString("tipo_nombre"));
                eq.setTipoEquipo(tipo);

                eq.setNombreResponsable(rs.getString("responsable_nombre"));

                lista.add(eq);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
        }
        return lista;
    } // Listar Todo

    
} // Class
