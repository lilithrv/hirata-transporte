package dao;

import conn.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.EquipoOficina;

public class EquipoOficinaDAO {

    public List<EquipoOficina> listarActivos() {
        List<EquipoOficina> equipos = new ArrayList<>();
        String sql = "SELECT id_equipo, codigo_inventario, tipo_equipo, marca, modelo, ubicacion, estado "
                + "FROM equipos_oficina ORDER BY codigo_inventario";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                equipos.add(new EquipoOficina(
                        rs.getInt("id_equipo"),
                        rs.getString("codigo_inventario"),
                        rs.getString("tipo_equipo"),
                        rs.getString("marca"),
                        rs.getString("modelo"),
                        rs.getString("ubicacion"),
                        rs.getString("estado")
                ));
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
}
