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
import modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class UsuarioDAO {

    //VERIFICAR CREDENCIALES
    public Usuario verificarCredenciales(String email, String password) {
        String sql = "SELECT u.id_usuario, u.email, u.password, "
                + "r.id_rol, r.nombre AS nombre_rol "
                + "FROM usuarios u "
                + "JOIN roles r ON u.id_rol = r.id_rol "
                + "WHERE u.email = ?";

        Usuario usuarioEncontrado = null;

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String hashGuardado = rs.getString("password").trim();
                    
                    //BCrypt compara la contraseña ingresada con el hash de la BD
                    if (BCrypt.checkpw(password, hashGuardado)) {

                        Rol rol = new Rol();
                        rol.setIdRol(rs.getInt("id_rol"));
                        rol.setNombreRol(rs.getString("nombre_rol"));

                        usuarioEncontrado = new Usuario();
                        usuarioEncontrado.setIdUsuario(rs.getInt("id_usuario"));
                        usuarioEncontrado.setEmail(rs.getString("email"));
                        usuarioEncontrado.setRol(rol);
                    }
                    // si la contraseña no coincide retorna null
                }
                // si el email no existe retorna null
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar credenciales: " + e.getMessage());
        }

        return usuarioEncontrado;
    }

    //CREAR
    public void insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, password, id_rol) "
                + "VALUES (?, ?, ?, ?)";

        String passwordHasheada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt(12));

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, passwordHasheada);
            ps.setInt(4, usuario.getRol().getIdRol());

            int filas = ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
        }
    }

    //ACTUALIZAR
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, password = ?, id_rol = ?  "
                + "WHERE id_usuario = ?";

        String passwordHasheada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt(12));

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, passwordHasheada);
            ps.setInt(4, usuario.getRol().getIdRol());
            ps.setInt(5, usuario.getIdUsuario());

            int filas = ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
        }
    }

    public boolean actualizar2(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, password = ?, id_rol = ? "
                + "WHERE id_usuario = ?";

        String passwordHasheada = BCrypt.hashpw(usuario.getPassword(), BCrypt.gensalt(12));

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, passwordHasheada);
            ps.setInt(4, usuario.getRol().getIdRol());
            ps.setInt(5, usuario.getIdUsuario());

            int filas = ps.executeUpdate();

            return filas > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario: " + e.getMessage());
            return false;
        }
    }

    //ELIMINAR
    public void eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id_usuario = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Usuario eliminado correctamente. ID: " + id);
            } else {
                System.out.println("No se encontró el usuario con ID: " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar usuario:" + e.getMessage());
        }
    }

    //LISTAR
    public List<Usuario> listarTodos() {
        List<Usuario> listado = new ArrayList<>();

        String sql = "SELECT u.id_usuario, u.nombre AS nombre_usuario, u.email, "
                + "r.id_rol, r.nombre AS nombre_rol "
                + "FROM usuarios u "
                + "JOIN roles r ON u.id_rol = r.id_rol "
                + "ORDER BY u.id_usuario";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Rol rol = new Rol();
                    rol.setIdRol(rs.getInt("id_rol"));
                    rol.setNombreRol(rs.getString("nombre_rol"));

                    Usuario usuario = new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre_usuario"),
                            rs.getString("email"),
                            rol
                    );

                    listado.add(usuario);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
            e.printStackTrace();
        }

        return listado;
    }

    //OBTENER POR ID
    public Usuario obtenerPorId(int id) {
        String sql = "SELECT u.id_usuario, u.nombre, u.email, u.id_rol, r.nombre "
                + "FROM usuarios u "
                + "JOIN roles r ON u.id_rol = r.id_rol "
                + "WHERE u.id_usuario = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Rol rol = new Rol();
                    rol.setIdRol(rs.getInt("id_rol"));
                    rol.setNombreRol(rs.getString("nombre"));

                    return new Usuario(
                            rs.getInt("id_usuario"),
                            rs.getString("nombre"),
                            rs.getString("email"),
                            rol
                    );
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener usuario: " + e.getMessage());
        }

        return null;
    }
}
