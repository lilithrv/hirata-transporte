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
import modelo.Vehiculo;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class VehiculoDAO {

    public List<Vehiculo> listarVehiculos() {
        List<Vehiculo> lista = new ArrayList<>();
        // consultamos la tabla de vehículos
        String sql = "SELECT * FROM vehiculos ORDER BY patente ASC";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vehiculo v = new Vehiculo();
                v.setIdVehiculo(rs.getInt("id_vehiculo"));
                v.setPatente(rs.getString("patente"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));
                v.setAnio(rs.getInt("anio"));
                v.setKilometrajeInicial(rs.getInt("kilometraje"));

                lista.add(v);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar flota de vehículos: " + e.getMessage());
        }
        return lista;
    }

    public boolean registrar(Vehiculo vehiculo) {

        if (buscarPorPatente(vehiculo.getPatente()) != null) {
            System.out.println("Error: La patente " + vehiculo.getPatente() + " ya está registrada en el sistema.");
            return false; // Retornamos false para que la Vista sepa que no se guardó
        }

        String sql = "INSERT INTO vehiculos (patente, marca, modelo, anio, kilometraje) VALUES (?, ?, ?, ?, ?)";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehiculo.getPatente());
            ps.setString(2, vehiculo.getMarca());
            ps.setString(3, vehiculo.getModelo());
            ps.setInt(4, vehiculo.getAnio());
            ps.setInt(5, vehiculo.getKilometrajeInicial());

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar vehículo: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Vehiculo vehiculo) {
        String sql = "UPDATE vehiculos SET marca = ?, modelo = ?, anio = ?, kilometraje = ? WHERE id_vehiculo = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, vehiculo.getMarca());
            ps.setString(2, vehiculo.getModelo());
            ps.setInt(3, vehiculo.getAnio());
            ps.setInt(4, vehiculo.getKilometrajeInicial());
            ps.setInt(5, vehiculo.getIdVehiculo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar vehículo: " + e.getMessage());
            return false;
        }
    }

    public Vehiculo buscarPorPatente(String patente) {
        String sql = "SELECT * FROM vehiculos WHERE patente = ?";
        Connection conn = Conexion.getInstancia();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Limpiamos la patente por si viene con espacios o guiones
            ps.setString(1, patente.trim().toUpperCase());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vehiculo v = new Vehiculo();
                    v.setIdVehiculo(rs.getInt("id_vehiculo"));
                    v.setPatente(rs.getString("patente"));
                    v.setMarca(rs.getString("marca"));
                    v.setModelo(rs.getString("modelo"));
                    v.setAnio(rs.getInt("anio"));
                    v.setKilometrajeInicial(rs.getInt("kilometraje"));
                    return v;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar vehículo por patente: " + e.getMessage());
        }
        return null; // Si no lo encuentra, devuelve null
    }

    public boolean eliminarPorPatente(String patente) {
        String sql = "DELETE FROM vehiculos WHERE patente = ?";
        Connection conn = Conexion.getInstancia();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patente.trim().toUpperCase());

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            // ERROR DE LLAVE FORÁNEA: Si el camión tiene mantenimientos, MySQL no dejará borrarlo.
            if (e.getErrorCode() == 1451) {
                System.out.println("Error: No se puede eliminar un vehículo que tiene historial de mantenimiento.");
            } else {
                System.out.println("Error al eliminar vehículo: " + e.getMessage());
            }
            return false;
        }
    }

    
    public boolean eliminar(String patente) {
        
        String sql = "DELETE FROM vehiculos WHERE patente = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, patente.trim().toUpperCase());

            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar vehículo: " + e.getMessage());
            return false;
        }
    }
    
    
    public Vehiculo obtenerVehiculoPorConductor(int idUsuario) {
        String sql = "SELECT * FROM vehiculos WHERE id_conductor = ?";
        Connection conn = Conexion.getInstancia();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Vehiculo v = new Vehiculo();
                    v.setIdVehiculo(rs.getInt("id_vehiculo"));
                    v.setPatente(rs.getString("patente"));
                    v.setMarca(rs.getString("marca"));
                    v.setModelo(rs.getString("modelo"));
                    v.setAnio(rs.getInt("anio"));
                    v.setKilometrajeInicial(rs.getInt("kilometraje_inicial"));
                    v.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                    return v;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener vehículo del conductor: " + e.getMessage());
        }
        return null; // Si el conductor no tiene un vehículo asignado
    }
    
    
} // Class
