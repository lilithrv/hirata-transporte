package dao;

import conn.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Kilometraje;
import modelo.Usuario;
import modelo.Vehiculo;

public class KilometrajeDAO {

    public boolean insertarKilometraje(Kilometraje kil) {

        String sql = "INSERT INTO kilometraje (id_conductor, id_vehiculo, kilometros) VALUES (?, ?, ?)";

        try {
            Connection conn = Conexion.getInstancia();
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, kil.getConductor().getIdUsuario());
            ps.setInt(2, kil.getVehiculo().getIdVehiculo());
            ps.setInt(3, kil.getKilometros());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                System.out.println("Se guardo el kilometraje en MySQL");
                return true;
            } else {
                return false;
            }

        } catch (Exception e) {

            System.out.println("Error al guardar: " + e.getMessage());
            return false;
        }
    }

    public List<Kilometraje> verTodos() {
        List<Kilometraje> lista = new ArrayList<>();

        String sql = "SELECT k.id_kilometraje, k.kilometros, k.fecha_registro, "
                + "u.id_usuario, u.nombre AS nombre_conductor, "
                + "v.id_vehiculo, v.patente, v.marca, v.modelo "
                + "FROM kilometraje k "
                + "JOIN usuarios u ON k.id_conductor = u.id_usuario "
                + "JOIN vehiculos v ON k.id_vehiculo = v.id_vehiculo "
                + "ORDER BY k.fecha_registro DESC";

        try {
            Connection conn = Conexion.getInstancia();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Usuario conductor = new Usuario();
                conductor.setIdUsuario(rs.getInt("id_usuario"));
                conductor.setNombreUsuario(rs.getString("nombre_conductor"));

                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setIdVehiculo(rs.getInt("id_vehiculo"));
                vehiculo.setPatente(rs.getString("patente"));
                vehiculo.setMarca(rs.getString("marca"));
                vehiculo.setModelo(rs.getString("modelo"));

                Kilometraje k = new Kilometraje();
                k.setIdKilometraje(rs.getInt("id_kilometraje"));
                k.setConductor(conductor);
                k.setVehiculo(vehiculo);
                k.setKilometros(rs.getInt("kilometros"));
                k.setFechaRegistro(rs.getTimestamp("fecha_registro"));

                lista.add(k);
            }
        } catch (SQLException e) {
            System.out.println("Problema al listar: " + e);
        }

        return lista;
    }

    public Kilometraje obtenerUltimoPorVehiculo(int idVehiculo) {
        Kilometraje kilometraje = null;

        String sql = """
            SELECT k.id_kilometraje, k.kilometros, k.fecha_registro,
                   u.id_usuario, u.nombre, u.email,
                   v.id_vehiculo, v.patente, v.marca, v.modelo, v.anio, v.kilometraje_inicial
            FROM kilometraje k
            JOIN usuarios u ON k.id_conductor = u.id_usuario
            JOIN vehiculos v ON k.id_vehiculo = v.id_vehiculo
            WHERE k.id_vehiculo = ?
            ORDER BY k.fecha_registro DESC
            LIMIT 1
            """;

        Connection conn = Conexion.getInstancia();
        try ( PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idVehiculo);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Usuario conductor = new Usuario();
                conductor.setIdUsuario(rs.getInt("id_usuario"));
                conductor.setNombreUsuario(rs.getString("nombre"));
                conductor.setEmail(rs.getString("email"));

                Vehiculo vehiculo = new Vehiculo();
                vehiculo.setIdVehiculo(rs.getInt("id_vehiculo"));
                vehiculo.setPatente(rs.getString("patente"));
                vehiculo.setMarca(rs.getString("marca"));
                vehiculo.setModelo(rs.getString("modelo"));
                vehiculo.setAnio(rs.getInt("anio"));
                vehiculo.setKilometrajeInicial(rs.getInt("kilometraje_inicial"));

                kilometraje = new Kilometraje();
                kilometraje.setIdKilometraje(rs.getInt("id_kilometraje"));
                kilometraje.setConductor(conductor);
                kilometraje.setVehiculo(vehiculo);
                kilometraje.setKilometros(rs.getInt("kilometros"));
                kilometraje.setFechaRegistro(rs.getTimestamp("fecha_registro"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener último kilometraje: " + e.getMessage());
        }

        return kilometraje; // si no hay km registrados, retorna null
    }
}
