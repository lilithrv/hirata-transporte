package dao;

import conn.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.EquipoOficina;
import modelo.TipoEquipo;

public class EquipoOficinaDAO {

    public List<EquipoOficina> listarActivos() {
        List<EquipoOficina> equipos = new ArrayList<>();
        String sql = "SELECT e.*, t.nombre AS tipo_nombre "
                + "FROM equipos_oficina e "
                + "JOIN tipos_equipo t ON e.id_tipo_equipo = t.id_tipo_equipo "
                + "WHERE e.estado = 'Activo' "
                + "ORDER BY e.id_equipo";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {

                TipoEquipo tipo = new TipoEquipo();
                tipo.setIdTipoEquipo(rs.getInt("id_tipo_equipo"));
                tipo.setNombre(rs.getString("tipo_nombre"));

                EquipoOficina eq = new EquipoOficina();
                eq.setIdEquipo(rs.getInt("id_equipo"));
                eq.setTipoEquipo(tipo);
                eq.setMarca(rs.getString("marca"));
                eq.setModelo(rs.getString("modelo"));
                eq.setNumeroSerie(rs.getString("numero_serie"));
                eq.setEstado(rs.getString("estado"));

                equipos.add(eq);
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

    public List<EquipoOficina> listarPorTipo(int idTipoEquipo) {
        List<EquipoOficina> lista = new ArrayList<>();
        String sql = "SELECT e.*, t.nombre AS tipo_nombre "
                + "FROM equipos_oficina e "
                + "JOIN tipos_equipo t ON e.id_tipo_equipo = t.id_tipo_equipo "
                + "WHERE e.id_tipo_equipo = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTipoEquipo);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    TipoEquipo tipo = new TipoEquipo();
                    tipo.setIdTipoEquipo(rs.getInt("id_tipo_equipo"));
                    tipo.setNombre(rs.getString("tipo_nombre"));

                    EquipoOficina eq = new EquipoOficina();
                    eq.setIdEquipo(rs.getInt("id_equipo"));
                    eq.setTipoEquipo(tipo);
                    eq.setMarca(rs.getString("marca"));
                    eq.setModelo(rs.getString("modelo"));
                    eq.setNumeroSerie(rs.getString("numero_serie"));
                    eq.setEstado(rs.getString("estado"));

                    lista.add(eq);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar equipos por tipo: " + e.getMessage());
        }
        return lista;
    }

    public List<String> obtenerEstadosUnicos() {
        List<String> estados = new ArrayList<>();
        String sql = "SELECT DISTINCT estado FROM equipos_oficina ORDER BY estado ASC";

        // Usamos tu método getInstancia()
        Connection conn = Conexion.getInstancia();

        try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                estados.add(rs.getString("estado"));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener estados: " + e.getMessage());
        }
        return estados;
    }

    public List<EquipoOficina> listarTodo() {
        List<EquipoOficina> lista = new ArrayList<>();
        // JOIN para traer los nombres en lugar de solo IDs
        String sql = "SELECT e.*, t.nombre AS tipo_nombre, u.nombre AS responsable_nombre "
                + "FROM equipos_oficina e "
                + "JOIN tipos_equipo t ON e.id_tipo_equipo = t.id_tipo_equipo "
                + "LEFT JOIN usuarios u ON e.id_responsable = u.id_usuario "
                + "ORDER BY e.id_equipo";

        try (Connection cn = Conexion.getInstancia(); PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EquipoOficina eq = new EquipoOficina();

                // Datos básicos
                eq.setIdEquipo(rs.getInt("id_equipo"));
                eq.setMarca(rs.getString("marca"));
                eq.setModelo(rs.getString("modelo"));
                eq.setNumeroSerie(rs.getString("numero_serie"));
                eq.setEstado(rs.getString("estado"));
                eq.setIdResponsable(rs.getInt("id_responsable"));

                // MANEJO DE FECHAS 
                // rs.getDate devuelve un java.sql.Date, lo pasamos a String con .toString()
                if (rs.getDate("fecha_adquisicion") != null) {
                    eq.setFechaAdquisicion(rs.getDate("fecha_adquisicion").toString());
                }

                eq.setFechaRegistro(rs.getTimestamp("fecha_registro"));

                // MANEJO DEL TIPO
                TipoEquipo tipo = new TipoEquipo();
                tipo.setIdTipoEquipo(rs.getInt("id_tipo_equipo"));
                tipo.setNombre(rs.getString("tipo_nombre"));
                eq.setTipoEquipo(tipo);

                eq.setNombreResponsable(rs.getString("responsable_nombre"));

                lista.add(eq);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar: " + e.getMessage());
        }
        return lista;
    } // Listar Todo

    public List<EquipoOficina> listarFiltrado(String estado, String tipoNombre) {
        List<EquipoOficina> lista = new ArrayList<>();

        // Base de la consulta con los JOINs que ya funcionan
        StringBuilder sql = new StringBuilder(
                "SELECT e.*, t.nombre AS tipo_nombre, u.nombre AS responsable_nombre "
                + "FROM equipos_oficina e "
                + "JOIN tipos_equipo t ON e.id_tipo_equipo = t.id_tipo_equipo "
                + "LEFT JOIN usuarios u ON e.id_responsable = u.id_usuario WHERE 1=1 "
        );

        // Condicionales dinámicos
        if (estado != null && !estado.equals("Todos")) {
            sql.append(" AND e.estado = '").append(estado).append("'");
        }

        if (tipoNombre != null && !tipoNombre.equals("Todos")) {
            sql.append(" AND t.nombre = '").append(tipoNombre).append("'");
        }

        sql.append(" ORDER BY e.id_equipo");

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql.toString()); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EquipoOficina eq = new EquipoOficina();
                eq.setIdEquipo(rs.getInt("id_equipo"));
                eq.setMarca(rs.getString("marca"));
                eq.setModelo(rs.getString("modelo"));
                eq.setNumeroSerie(rs.getString("numero_serie"));
                eq.setEstado(rs.getString("estado"));

                if (rs.getDate("fecha_adquisicion") != null) {
                    eq.setFechaAdquisicion(rs.getDate("fecha_adquisicion").toString());
                }
                eq.setFechaRegistro(rs.getTimestamp("fecha_registro"));

                TipoEquipo tipo = new TipoEquipo();
                tipo.setNombre(rs.getString("tipo_nombre"));
                eq.setTipoEquipo(tipo);

                eq.setNombreResponsable(rs.getString("responsable_nombre"));
                lista.add(eq);
            }
        } catch (SQLException e) {
            System.err.println("Error en el filtro combinado: " + e.getMessage());
        }
        return lista;
    }
    
    
       public List<String> obtenerPiezasPorTipos(List<String> tiposPermitidos) {
        List<String> listaPiezas = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tiposPermitidos.size(); i++) {
            sb.append("?");
            if (i < tiposPermitidos.size() - 1) {
                sb.append(", ");
            }
        }

        // Traemos el nombre del tipo, marca, modelo y descripción
        String sql = "SELECT tp.nombre AS tipo_nombre, p.marca, p.modelo, p.descripcion "
                + "FROM piezas p "
                + "JOIN tipos_pieza tp ON p.id_tipo_pieza = tp.id_tipo_pieza "
                + "WHERE p.stock_actual > 0 AND tp.nombre IN (" + sb.toString() + ") "
                + "ORDER BY CASE WHEN tp.nombre = 'Otro' THEN 1 ELSE 0 END ASC, "
                + "tp.nombre ASC, p.marca ASC";

        Connection conn = Conexion.getInstancia();

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            // Inyectamos dinámicamente los tipos permitidos en los parámetros del query
            for (int i = 0; i < tiposPermitidos.size(); i++) {
                ps.setString(i + 1, tiposPermitidos.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    // Formateamos la información: [Tipo] Marca - Modelo (Descripción)
                    String pieza = "[" + rs.getString("tipo_nombre") + "] "
                            + rs.getString("marca") + " - "
                            + rs.getString("modelo") + " ("
                            + rs.getString("descripcion") + ")";
                    listaPiezas.add(pieza);
                }
               
            }

        } catch (SQLException e) {
            System.out.println("Error al filtrar las piezas desde el DAO: " + e.getMessage());
        }

        return listaPiezas;
    }

    public EquipoOficina buscarPorId(int id) {
        String sql = "SELECT e.*, t.nombre AS tipo_nombre "
                + "FROM equipos_oficina e "
                + "JOIN tipos_equipo t ON e.id_tipo_equipo = t.id_tipo_equipo "
                + "WHERE e.id_equipo = ?";

        Connection conn = Conexion.getInstancia();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);                          
            try (ResultSet rs = ps.executeQuery()) {   
                if (rs.next()) {
                    TipoEquipo tipo = new TipoEquipo();
                    tipo.setIdTipoEquipo(rs.getInt("id_tipo_equipo"));
                    tipo.setNombre(rs.getString("tipo_nombre"));

                    EquipoOficina equipo = new EquipoOficina();
                    equipo.setIdEquipo(rs.getInt("id_equipo"));
                    equipo.setTipoEquipo(tipo);
                    equipo.setMarca(rs.getString("marca"));
                    equipo.setModelo(rs.getString("modelo"));
                    equipo.setNumeroSerie(rs.getString("numero_serie"));
                    equipo.setEstado(rs.getString("estado"));

                    return equipo;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar equipo: " + e.getMessage());
        }
        return null;
    }
    
    
    public boolean guardarMantenimientoNotebook(int idEquipo, String estadoEqui, String estadoMant,
            boolean desarme, boolean limpieza, boolean ram, boolean pasta, boolean cierre, boolean sustitucion,
            String tipoMant, String observaciones) { // <-- 🚀 Nuevos parámetros

        Connection conn = Conexion.getInstancia();

        try {
            conn.setAutoCommit(false);

            // Buscamos primero el id_mantenimiento activo para este equipo
            int idMantenimiento = -1;
            String sqlBuscarMant = "SELECT id_mantenimiento FROM mantenimiento_equipos WHERE id_equipo = ? AND estado != 'Completado' LIMIT 1";

            try (PreparedStatement psBusca = conn.prepareStatement(sqlBuscarMant)) {
                psBusca.setInt(1, idEquipo);
                try (ResultSet rs = psBusca.executeQuery()) {
                    if (rs.next()) {
                        idMantenimiento = rs.getInt("id_mantenimiento");
                    }
                }
            }

            //  Si no existe, lo creamos al vuelo
            if (idMantenimiento == -1) {
                // Nota: Se pone id_tecnico = 1 por defecto para pruebas. Luego lo vinculas a tu inicio de sesión.
                String sqlCrearMant = "INSERT INTO mantenimiento_equipos (id_equipo, tipo_mantenimiento, estado, descripcion, id_tecnico, fecha_inicio) VALUES (?, ?, ?, ?, 1, NOW())";
                try (PreparedStatement psCrear = conn.prepareStatement(sqlCrearMant, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    psCrear.setInt(1, idEquipo);
                    psCrear.setString(2, tipoMant);
                    psCrear.setString(3, estadoMant);
                    psCrear.setString(4, observaciones);
                    psCrear.executeUpdate();

                    try (ResultSet rsKeys = psCrear.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            idMantenimiento = rsKeys.getInt(1);
                        }
                    }
                }
            } else {
                // Si ya existía, simplemente lo actualizamos
                String sqlUpdateMant = "UPDATE mantenimiento_equipos SET estado = ?, fecha_completado = ?, tipo_mantenimiento = ?, descripcion = ? WHERE id_mantenimiento = ?";
                try (PreparedStatement psMant = conn.prepareStatement(sqlUpdateMant)) {
                    psMant.setString(1, estadoMant);
                    psMant.setTimestamp(2, estadoMant.equals("Completado") ? new java.sql.Timestamp(System.currentTimeMillis()) : null);
                    psMant.setString(3, tipoMant);
                    psMant.setString(4, observaciones);
                    psMant.setInt(5, idMantenimiento);
                    psMant.executeUpdate();
                }
            }

            // Actualizar la tabla del Equipo (Activo / En Mantenimiento)
            String sqlUpdateEquipo = "UPDATE equipos_oficina SET estado = ? WHERE id_equipo = ?";
            try (PreparedStatement psEqui = conn.prepareStatement(sqlUpdateEquipo)) {
                psEqui.setString(1, estadoEqui);
                psEqui.setInt(2, idEquipo);
                psEqui.executeUpdate();
            }

            // Insertar o actualizar el Checklist detallado
            String sqlChecklist = "INSERT INTO detalle_mant_notebook (id_mantenimiento, desarme_inicial, limpieza_fisica, check_ram, cambio_pasta, armado_cierre, sustitucion_piezas) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?) "
                    + "ON DUPLICATE KEY UPDATE desarme_inicial = ?, limpieza_fisica = ?, check_ram = ?, cambio_pasta = ?, armado_cierre = ?, sustitucion_piezas = ?";
            try (PreparedStatement psCheck = conn.prepareStatement(sqlChecklist)) {
                psCheck.setInt(1, idMantenimiento);
                psCheck.setBoolean(2, desarme);
                psCheck.setBoolean(3, limpieza);
                psCheck.setBoolean(4, ram);
                psCheck.setBoolean(5, pasta);
                psCheck.setBoolean(6, cierre);
                psCheck.setBoolean(7, sustitucion);

                psCheck.setBoolean(8, desarme);
                psCheck.setBoolean(9, limpieza);
                psCheck.setBoolean(10, ram);
                psCheck.setBoolean(11, pasta);
                psCheck.setBoolean(12, cierre);
                psCheck.setBoolean(13, sustitucion);
                psCheck.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error en transacción: " + e.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex) {
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
            }
        }
        return false;
    }
    

} // Class
