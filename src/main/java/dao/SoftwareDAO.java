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
}
