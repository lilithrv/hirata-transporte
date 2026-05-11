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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.TipoSoftware;

/**
 *
 * @author leslie
 */
public class TipoSoftwareDAO {

    private TipoSoftware mapear(ResultSet rs) throws SQLException {
        return new TipoSoftware(
                rs.getInt("id_tipo_software"),
                rs.getString("nombre")
        );
    }

    public List<TipoSoftware> listarTodos() {
        List<TipoSoftware> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipos_software ORDER BY nombre";

        Connection conn = Conexion.getInstancia();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar tipos de software: " + e.getMessage());
        }
        return lista;
    }
    
    public TipoSoftware buscarPorId(int id) {
        String sql = "SELECT * FROM tipos_software WHERE id_tipo_software = ?";
        
        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar tipo de software: " + e.getMessage());
        }
        return null;
    }

    public boolean insertar(TipoSoftware t) {
        String sql = "INSERT INTO tipos_software (nombre) VALUES (?)";
        
        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar tipo de software: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(TipoSoftware t) {
        String sql = "UPDATE tipos_software SET nombre = ? WHERE id_tipo_software = ?";
        
        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, t.getNombre());
            ps.setInt   (2, t.getIdTipoSoftware());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar tipo de software: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int id) {
        String sql = "DELETE FROM tipos_software WHERE id_tipo_software = ?";
        
        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar tipo de software: " + e.getMessage());
            return false;
        }
    }
}
