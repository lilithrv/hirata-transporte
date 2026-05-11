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
import modelo.Pieza;
import modelo.TipoPieza;

/**
 *
 * @author leslie
 */
public class PiezaDAO {
     public boolean insertar(Pieza p) {
        String sql = "INSERT INTO piezas "
                   + "(id_tipo_pieza, marca, modelo, descripcion, stock_actual, stock_minimo) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        
        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, p.getTipoPieza().getIdTipoPieza());
            ps.setString(2, p.getMarca());
            ps.setString(3, p.getModelo());
            ps.setString(4, p.getDescripcion());
            ps.setInt   (5, p.getStockActual());
            ps.setInt   (6, p.getStockMinimo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar pieza: " + e.getMessage());
            return false;
        }
    }


    public List<Pieza> listarTodos() {
        List<Pieza> lista = new ArrayList<>();
        String sql = "SELECT p.*, t.nombre AS tipo_nombre "
                   + "FROM piezas p "
                   + "JOIN tipos_pieza t ON p.id_tipo_pieza = t.id_tipo_pieza "
                   + "ORDER BY t.nombre, p.marca";
        
        Connection conn = Conexion.getInstancia();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar piezas: " + e.getMessage());
        }
        return lista;
    }


    public Pieza buscarPorId(int idPieza) {
        String sql = "SELECT p.*, t.nombre AS tipo_nombre "
                   + "FROM piezas p "
                   + "JOIN tipos_pieza t ON p.id_tipo_pieza = t.id_tipo_pieza "
                   + "WHERE p.id_pieza = ?";
        
        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPieza);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapear(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar pieza: " + e.getMessage());
        }
        return null;
    }


    public List<Pieza> listarStockBajo() {
        List<Pieza> lista = new ArrayList<>();
        String sql = "SELECT p.*, t.nombre AS tipo_nombre "
                   + "FROM piezas p "
                   + "JOIN tipos_pieza t ON p.id_tipo_pieza = t.id_tipo_pieza "
                   + "WHERE p.stock_actual <= p.stock_minimo "
                   + "ORDER BY p.stock_actual ASC";
        
        Connection conn = Conexion.getInstancia();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar stock bajo: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizar(Pieza p) {
        String sql = "UPDATE piezas SET "
                   + "id_tipo_pieza = ?, marca = ?, modelo = ?, "
                   + "descripcion = ?, stock_actual = ?, stock_minimo = ? "
                   + "WHERE id_pieza = ?";
        
        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt   (1, p.getTipoPieza().getIdTipoPieza());
            ps.setString(2, p.getMarca());
            ps.setString(3, p.getModelo());
            ps.setString(4, p.getDescripcion());
            ps.setInt   (5, p.getStockActual());
            ps.setInt   (6, p.getStockMinimo());
            ps.setInt   (7, p.getIdPieza());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar pieza: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idPieza) {
        String sql = "DELETE FROM piezas WHERE id_pieza = ?";
        
        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPieza);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar pieza: " + e.getMessage());
            return false;
        }
    }

    private Pieza mapear(ResultSet rs) throws SQLException {
        TipoPieza tipo = new TipoPieza(
            rs.getInt("id_tipo_pieza"),
            rs.getString("tipo_nombre")
        );
        Pieza p = new Pieza();
        p.setIdPieza      (rs.getInt      ("id_pieza"));
        p.setTipoPieza    (tipo);
        p.setMarca        (rs.getString   ("marca"));
        p.setModelo       (rs.getString   ("modelo"));
        p.setDescripcion  (rs.getString   ("descripcion"));
        p.setStockActual  (rs.getInt      ("stock_actual"));
        p.setStockMinimo  (rs.getInt      ("stock_minimo"));
        p.setFechaRegistro(rs.getTimestamp("fecha_registro"));
        return p;
    }
}
