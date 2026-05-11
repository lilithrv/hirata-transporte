/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import conn.Conexion;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import modelo.TipoEquipo;

/**
 *
 * @author leslie
 */
public class TipoEquipoDAO {

    public List<TipoEquipo> listarTodos() {
        List<TipoEquipo> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipos_equipo ORDER BY nombre";
        
        Connection conn = Conexion.getInstancia();
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar tipos de equipo: " + e.getMessage());
        }
        return lista;
    }

    private TipoEquipo mapear(ResultSet rs) throws SQLException {
        return new TipoEquipo(
                rs.getInt("id_tipo_equipo"),
                rs.getString("nombre")
        );
    }
}
