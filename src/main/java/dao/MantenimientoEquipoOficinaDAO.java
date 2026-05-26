package dao;

import conn.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.EquipoOficina;
import modelo.MantenimientoEquipoOficina;
import modelo.TipoEquipo;
import modelo.Usuario;

public class MantenimientoEquipoOficinaDAO {

    public boolean registrar(MantenimientoEquipoOficina mantenimiento) {
        String sql = "INSERT INTO mantenimiento_equipos_oficina "
                + "(id_equipo, id_tecnico, tipo_mantenimiento, descripcion, acciones_realizadas, piezas_revisadas, estado_resultado, observaciones) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, mantenimiento.getEquipo().getIdEquipo());
            ps.setInt(2, mantenimiento.getTecnico().getIdUsuario());
            ps.setString(3, mantenimiento.getTipoMantenimiento());
            ps.setString(4, mantenimiento.getDescripcion());
            ps.setString(5, mantenimiento.getAccionesRealizadas());
            ps.setString(6, mantenimiento.getPiezasRevisadas());
            ps.setString(7, mantenimiento.getEstadoResultado());
            ps.setString(8, mantenimiento.getObservaciones());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar mantenimiento de equipo de oficina: " + e.getMessage());
            return false;
        }
    }

    public List<MantenimientoEquipoOficina> listarTodos() throws SQLException {
        List<MantenimientoEquipoOficina> lista = new ArrayList<>();

        String sql = """
            SELECT
                me.id_mantenimiento,
                me.tipo_mantenimiento,
                me.estado,
                me.descripcion,
                me.fecha_registro,
                me.fecha_inicio,
                me.fecha_completado,

                -- Equipo
                eo.id_equipo,
                eo.marca       AS equipo_marca,
                eo.modelo      AS equipo_modelo,
                eo.numero_serie,
                te.id_tipo_equipo,
                te.nombre      AS tipo_equipo,

                -- Técnico
                u.id_usuario,
                u.nombre       AS tecnico_nombre,
                u.email        AS tecnico_email,

                -- Piezas usadas (concatenadas)
                (
                    SELECT GROUP_CONCAT(
                               CONCAT(p.marca, ' ', p.modelo, ' (', mp.tipo_uso, ')')
                               ORDER BY mp.id_mant_pieza
                               SEPARATOR ' | '
                           )
                    FROM mantenimiento_piezas mp
                    JOIN piezas p ON mp.id_pieza = p.id_pieza
                    WHERE mp.id_mantenimiento = me.id_mantenimiento
                ) AS piezas_revisadas

            FROM mantenimiento_equipos me
            JOIN equipos_oficina eo  ON me.id_equipo  = eo.id_equipo
            JOIN tipos_equipo    te  ON eo.id_tipo_equipo = te.id_tipo_equipo
            LEFT JOIN usuarios   u   ON me.id_tecnico = u.id_usuario
            ORDER BY me.fecha_registro DESC
        """;

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                MantenimientoEquipoOficina m = new MantenimientoEquipoOficina();

                m.setIdMantenimientoEquipo(rs.getInt("id_mantenimiento"));
                m.setTipoMantenimiento(rs.getString("tipo_mantenimiento"));
                m.setEstadoResultado(rs.getString("estado"));
                m.setDescripcion(rs.getString("descripcion"));
                m.setFechaRegistro(rs.getDate("fecha_registro"));
                m.setPiezasRevisadas(rs.getString("piezas_revisadas"));

                // Equipo
                TipoEquipo tipoEquipo = new TipoEquipo(
                        rs.getInt("id_tipo_equipo"),
                        rs.getString("tipo_equipo")
                );

                EquipoOficina equipo = new EquipoOficina();
                equipo.setIdEquipo(rs.getInt("id_equipo"));
                equipo.setMarca(rs.getString("equipo_marca"));
                equipo.setModelo(rs.getString("equipo_modelo"));
                equipo.setNumeroSerie(rs.getString("numero_serie"));
                equipo.setTipoEquipo(tipoEquipo);

                m.setEquipo(equipo);

                // Técnico (puede ser null si no tiene asignado)
                int idTecnico = rs.getInt("id_usuario");
                if (!rs.wasNull()) {
                    Usuario tecnico = new Usuario();
                    tecnico.setIdUsuario(idTecnico);
                    tecnico.setNombreUsuario(rs.getString("tecnico_nombre"));
                    tecnico.setEmail(rs.getString("tecnico_email"));
                    m.setTecnico(tecnico);
                }

                lista.add(m);
            }
        }

        return lista;
    }
}
