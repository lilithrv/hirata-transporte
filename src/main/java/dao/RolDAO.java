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
import modelo.Rol;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class RolDAO {

    //CREAR
    public void insertar(Rol rol) {
        String sql = "INSERT INTO roles (nombre) "
                + "VALUES (?)";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol.getNombreRol());

            int filas = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al crear rol: " + e.getMessage());
        }
    }

    //ACTUALIZAR
    public void actualizar(Rol rol) {
        String sql = "UPDATE roles SET nombre = ? "
                + "WHERE id_rol = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol.getNombreRol());

            int filas = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar idioma: " + e.getMessage());
        }
    }

    public boolean actualizar2(Rol rol) {
        String sql = "UPDATE roles SET nombre = ? "
                + "WHERE id_rol = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, rol.getNombreRol());

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar idioma: " + e.getMessage());
            return false;
        }
    }

    //ELIMINAR
    public void eliminar(int id) {
        String sql = "DELETE FROM roles WHERE id_rol = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Rol eliminado correctamente. ID: " + id);
            } else {
                System.out.println("No se encontró rol con ID: " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar rol:" + e.getMessage());
        }
    }

    //LISTAR
    public List<Rol> listarTodos() {
        List<Rol> listado = new ArrayList<>();

        String sql = "SELECT * FROM roles";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Rol rol = new Rol();
                    rol.setIdRol(rs.getInt("id_rol"));
                    rol.setNombreRol(rs.getString("nombre"));

                    listado.add(rol);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar roles: " + e.getMessage());
            e.printStackTrace();
        }

        return listado;
    }

    //BUSCAR POR ID
    public Rol obtenerPorId(int id) {
        String sql = "SELECT * FROM roles WHERE id_rol = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Rol rol = new Rol();
                    rol.setIdRol(rs.getInt("id_rol"));
                    rol.setNombreRol(rs.getString("nombre"));

                    return new Rol(
                            rs.getInt("id_rol"),
                            rs.getString("nombre")
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener rol: " + e.getMessage());
        }

        return null;
    }
}
