package dao;

import conn.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import modelo.MantenimientoEquipoOficina;

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
}
