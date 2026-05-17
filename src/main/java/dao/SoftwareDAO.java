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
import modelo.Software;
import modelo.SoftwareEquipo;
import modelo.TipoSoftware;

/**
 *
 * @author leslie
 */
public class SoftwareDAO {

    public List<SoftwareEquipo> listarPorEquipo(int idEquipo) {
        List<SoftwareEquipo> lista = new ArrayList<>();
        String sql = "SELECT se.*, "
                + "s.nombre AS sw_nombre, s.fabricante, "
                + "t.id_tipo_software, t.nombre AS tipo_nombre "
                + "FROM software_equipo se "
                + "JOIN software s       ON se.id_software = s.id_software "
                + "JOIN tipos_software t ON s.id_tipo_software = t.id_tipo_software "
                + "WHERE se.id_equipo = ? "
                + "ORDER BY se.fecha_accion DESC";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    TipoSoftware tipo = new TipoSoftware();
                    tipo.setIdTipoSoftware(rs.getInt("id_tipo_software"));
                    tipo.setNombre(rs.getString("tipo_nombre"));

                    Software sw = new Software();
                    sw.setIdSoftware(rs.getInt("id_software"));
                    sw.setTipoSoftware(tipo);
                    sw.setNombre(rs.getString("sw_nombre"));
                    sw.setFabricante(rs.getString("fabricante"));

                    SoftwareEquipo se = new SoftwareEquipo();
                    se.setIdSwEquipo(rs.getInt("id_sw_equipo"));
                    se.setIdEquipo(rs.getInt("id_equipo"));
                    se.setSoftware(sw);
                    se.setVersion(rs.getString("version"));
                    se.setFechaAccion(rs.getTimestamp("fecha_accion"));

                    lista.add(se);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar software del equipo: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrarInstalacion(int idEquipo, int idSoftware, String version, int idTecnico) {
        String sql = "INSERT INTO software_equipo "
                + "(id_equipo, id_software, version, estado, id_tecnico, fecha_accion) "
                + "VALUES (?, ?, ?, 'Instalado', ?, NOW())";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ps.setInt(2, idSoftware);
            ps.setString(3, version);
            ps.setInt(4, idTecnico);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar instalación: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarVersion(int idSwEquipo, String nuevaVersion, int idTecnico,  String notas) {
        String sql = "UPDATE software_equipo "
                + "SET version = ?, estado = 'Actualizado', "
                + "id_tecnico = ?, fecha_accion = NOW(), notas = ?  "
                + "WHERE id_sw_equipo = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nuevaVersion);
            ps.setInt(2, idTecnico);
            ps.setString(3, notas.isEmpty() ? null : notas);
            ps.setInt(4, idSwEquipo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar versión: " + e.getMessage());
            return false;
        }
    }

    public boolean desinstalar(int idSwEquipo) {
        String sql = "UPDATE software_equipo "
                + "SET estado = 'Desinstalado', fecha_accion = NOW() "
                + "WHERE id_sw_equipo = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idSwEquipo);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al desinstalar software: " + e.getMessage());
            return false;
        }
    }

    public List<Software> listarTodos() {
        List<Software> lista = new ArrayList<>();
        String sql = "SELECT s.*, t.nombre AS tipo_nombre "
                + "FROM software s "
                + "JOIN tipos_software t ON s.id_tipo_software = t.id_tipo_software "
                + "ORDER BY t.nombre, s.nombre";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {

                TipoSoftware tipo = new TipoSoftware();
                tipo.setIdTipoSoftware(rs.getInt("id_tipo_software"));
                tipo.setNombre(rs.getString("tipo_nombre"));

                Software s = new Software();
                s.setIdSoftware(rs.getInt("id_software"));
                s.setTipoSoftware(tipo);
                s.setNombre(rs.getString("nombre"));
                s.setFabricante(rs.getString("fabricante"));
                s.setDescripcion(rs.getString("descripcion"));

                lista.add(s);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar software: " + e.getMessage());
        }
        return lista;
    }

    public List<Software> listarPorTipo(int idTipoSoftware) {
        List<Software> lista = new ArrayList<>();
        String sql = "SELECT s.id_software, s.id_tipo_software, s.nombre, t.nombre AS tipo_nombre "
                + "FROM software s "
                + // <-- espacio antes de WHERE
                "JOIN tipos_software t ON s.id_tipo_software = t.id_tipo_software "
                + "WHERE s.id_tipo_software = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTipoSoftware);                          
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TipoSoftware tipo = new TipoSoftware();
                    tipo.setIdTipoSoftware(rs.getInt("id_tipo_software"));
                    tipo.setNombre(rs.getString("tipo_nombre"));

                    Software s = new Software();
                    s.setIdSoftware(rs.getInt("id_software"));
                    s.setTipoSoftware(tipo);
                    s.setNombre(rs.getString("nombre"));

                    lista.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar software por tipo: " + e.getMessage());
        }
        return lista;
    }

    public List<Software> listarCompatiblesPorTipoEquipo(int idTipoEquipo) {
        List<Software> lista = new ArrayList<>();
        String sql = "SELECT DISTINCT s.id_software, s.nombre, s.fabricante, "
                + "       t.id_tipo_software, t.nombre AS tipo_nombre "
                + "FROM software s "
                + "JOIN tipos_software t      ON s.id_tipo_software = t.id_tipo_software "
                + "JOIN software_equipo se    ON se.id_software = s.id_software "
                + "JOIN equipos_oficina e     ON se.id_equipo = e.id_equipo "
                + "WHERE e.id_tipo_equipo = ? "
                + "ORDER BY t.nombre, s.nombre";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTipoEquipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TipoSoftware tipo = new TipoSoftware();
                    tipo.setIdTipoSoftware(rs.getInt("id_tipo_software"));
                    tipo.setNombre(rs.getString("tipo_nombre"));

                    Software s = new Software();
                    s.setIdSoftware(rs.getInt("id_software"));
                    s.setTipoSoftware(tipo);
                    s.setNombre(rs.getString("nombre"));
                    s.setFabricante(rs.getString("fabricante"));

                    lista.add(s);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar software por tipo de equipo: " + e.getMessage());
        }
        return lista;
    }

    public boolean existeInstalacion(int idEquipo, int idSoftware) {
        String sql = "SELECT COUNT(*) FROM software_equipo "
                + "WHERE id_equipo = ? AND id_software = ? "
                + "AND estado != 'Desinstalado'";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idEquipo);
            ps.setInt(2, idSoftware);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al verificar instalación: " + e.getMessage());
        }
        return false;
    }
}
