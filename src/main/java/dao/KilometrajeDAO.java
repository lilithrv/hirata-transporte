
package dao;

import conn.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Kilometraje;

public class KilometrajeDAO {

    
    public boolean insertarKilometraje(Kilometraje kil) {
        
        String sql = "INSERT INTO kilometraje (id_conductor, id_vehiculo, kilometros) VALUES (?, ?, ?)";
        
        try {
            Connection conn = Conexion.getInstancia();
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, kil.getIdConductor());
            ps.setInt(2, kil.getIdVehiculo());
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
        
        String sql = "SELECT * FROM kilometraje";
        
        try {
            Connection conn = Conexion.getInstancia();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Kilometraje k = new Kilometraje();
                k.setIdKilometraje(rs.getInt("id_kilometraje"));
                k.setIdConductor(rs.getInt("id_conductor"));
                k.setIdVehiculo(rs.getInt("id_vehiculo"));
                k.setKilometros(rs.getInt("kilometros"));
                k.setFechaRegistro(rs.getTimestamp("fecha_registro"));
                
                lista.add(k);
            }
        } catch (SQLException e) {
            System.out.println("Problema al listar: " + e);
        }
        
        return lista;
    }
}
