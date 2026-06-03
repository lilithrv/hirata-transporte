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
import util.Sesion;

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
        String sql = "SELECT p.id_pieza, tp.nombre AS tipo_nombre, p.marca, p.modelo, p.descripcion "
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
                    String pieza = rs.getInt("id_pieza") + " | [" + rs.getString("tipo_nombre") + "] "
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

    // Método auxiliar privado para no repetir código en cada tipo de equipo
    private void registrarPiezasUsadas(Connection conn, int idMantenimiento, int idEquipo, List<Integer> piezasUsadas) throws SQLException {
        if (piezasUsadas != null && !piezasUsadas.isEmpty()) {
            String sqlDesc = "UPDATE piezas SET stock_actual = stock_actual - 1 WHERE id_pieza = ?";
            String sqlMantP = "INSERT INTO mantenimiento_piezas (id_mantenimiento, id_pieza, cantidad, tipo_uso) VALUES (?, ?, 1, 'Reemplazo')";
            String sqlEqComp = "INSERT INTO equipo_componentes (id_equipo, id_pieza, fecha_instalacion) VALUES (?, ?, NOW())";

            try (PreparedStatement psDesc = conn.prepareStatement(sqlDesc); PreparedStatement psMantP = conn.prepareStatement(sqlMantP); PreparedStatement psEqC = conn.prepareStatement(sqlEqComp)) {

                for (int idPieza : piezasUsadas) {
                    // Restar stock
                    psDesc.setInt(1, idPieza);
                    psDesc.executeUpdate();

                    // Historial de mantenimiento
                    psMantP.setInt(1, idMantenimiento);
                    psMantP.setInt(2, idPieza);
                    psMantP.executeUpdate();

                    // Vincular al equipo
                    psEqC.setInt(1, idEquipo);
                    psEqC.setInt(2, idPieza);
                    psEqC.executeUpdate();
                }
            }
        }
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
            boolean desarme, boolean limpieza, boolean ram, boolean almacenamiento, boolean pasta, boolean cierre, boolean sustitucion,
            String tipoMant, String observaciones, List<Integer> piezasUsadas) {

        Connection conn = Conexion.getInstancia();

        try {
            conn.setAutoCommit(false);
            int idMantenimiento = -1;
            // código de INSERT/UPDATE de mantenimiento_equipos 
            String sqlBuscarMant = "SELECT id_mantenimiento FROM mantenimiento_equipos WHERE id_equipo = ? AND estado != 'Completado' LIMIT 1";
            try (java.sql.PreparedStatement psBusca = conn.prepareStatement(sqlBuscarMant)) {
                psBusca.setInt(1, idEquipo);
                try (java.sql.ResultSet rs = psBusca.executeQuery()) {
                    if (rs.next()) idMantenimiento = rs.getInt("id_mantenimiento");
                }
            }

            if (idMantenimiento == -1) {
                String sqlCrearMant = "INSERT INTO mantenimiento_equipos (id_equipo, tipo_mantenimiento, estado, descripcion, id_tecnico, fecha_inicio, fecha_completado) VALUES (?, ?, ?, ?, ?, NOW(), ?)";
                try (java.sql.PreparedStatement psCrear = conn.prepareStatement(sqlCrearMant, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    psCrear.setInt(1, idEquipo); 
                    psCrear.setString(2, tipoMant); 
                    psCrear.setString(3, estadoMant); 
                    psCrear.setString(4, observaciones);
                    psCrear.setInt(5, Sesion.getUsuarioActivo().getIdUsuario());
                    psCrear.setTimestamp(6, estadoMant.equals("Completado")     
                    ? new java.sql.Timestamp(System.currentTimeMillis()) 
                    : null);
                    psCrear.executeUpdate();
                    try (java.sql.ResultSet rsKeys = psCrear.getGeneratedKeys()) {
                        if (rsKeys.next()) idMantenimiento = rsKeys.getInt(1);
                    }
                }
            } else {
                String sqlUpdateMant = "UPDATE mantenimiento_equipos SET estado = ?, fecha_completado = ?, tipo_mantenimiento = ?, descripcion = ?, id_tecnico= ? WHERE id_mantenimiento = ?";
                try (java.sql.PreparedStatement psMant = conn.prepareStatement(sqlUpdateMant)) {
                    psMant.setString(1, estadoMant); 
                    psMant.setTimestamp(2, estadoMant.equals("Completado") ? new java.sql.Timestamp(System.currentTimeMillis()) : null);
                    psMant.setString(3, tipoMant); 
                    psMant.setString(4, observaciones); 
                    psMant.setInt(5, Sesion.getUsuarioActivo().getIdUsuario()); 
                    psMant.setInt(6, idMantenimiento);    
                    psMant.executeUpdate();
                }
            }

            String sqlUpdateEquipo = "UPDATE equipos_oficina SET estado = ? WHERE id_equipo = ?";
            try (java.sql.PreparedStatement psEqui = conn.prepareStatement(sqlUpdateEquipo)) {
                psEqui.setString(1, estadoEqui); psEqui.setInt(2, idEquipo);
                psEqui.executeUpdate();
            }

            // Tu código de detalle_mant_notebook queda IGUAL
            String sqlChecklist = "INSERT INTO detalle_mant_notebook (id_mantenimiento, desarme_inicial, limpieza_fisica, check_ram, check_almacenamiento, cambio_pasta, armado_cierre, sustitucion_piezas) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE desarme_inicial=?, limpieza_fisica=?, check_ram=?, check_almacenamiento=?, cambio_pasta=?, armado_cierre=?, sustitucion_piezas=?";
            try (java.sql.PreparedStatement psCheck = conn.prepareStatement(sqlChecklist)) {
                psCheck.setInt(1, idMantenimiento); psCheck.setBoolean(2, desarme); psCheck.setBoolean(3, limpieza); psCheck.setBoolean(4, ram); psCheck.setBoolean(5, almacenamiento); psCheck.setBoolean(6, pasta); psCheck.setBoolean(7, cierre); psCheck.setBoolean(8, sustitucion);
                psCheck.setBoolean(9, desarme); psCheck.setBoolean(10, limpieza); psCheck.setBoolean(11, ram); psCheck.setBoolean(12, almacenamiento); psCheck.setBoolean(13, pasta); psCheck.setBoolean(14, cierre); psCheck.setBoolean(15, sustitucion);
                psCheck.executeUpdate();
            }

            // LÓGICA DE INVENTARIO
            registrarPiezasUsadas(conn, idMantenimiento, idEquipo, piezasUsadas);

            conn.commit();
            return true;
        } catch (java.sql.SQLException e) {
            System.err.println("Error en transacción Notebook: " + e.getMessage());
            try { conn.rollback(); } catch (java.sql.SQLException ex) {}
        } finally {
            try { conn.setAutoCommit(true); } catch (java.sql.SQLException e) {}
        }
        return false;
    }

    public boolean guardarMantenimientoPC(int idEquipo, String estadoEqui, String estadoMant,
            boolean desarme, boolean limpieza, boolean ram, boolean almacenamiento, boolean pasta, boolean cierre, boolean sustitucion,
            String tipoMant, String observaciones, List<Integer> piezasUsadas) {

        Connection conn = Conexion.getInstancia();
        try {
            conn.setAutoCommit(false);
            int idMantenimiento = -1;
            String sqlBuscarMant = "SELECT id_mantenimiento FROM mantenimiento_equipos WHERE id_equipo = ? AND estado != 'Completado' LIMIT 1";
            try (java.sql.PreparedStatement psBusca = conn.prepareStatement(sqlBuscarMant)) {
                psBusca.setInt(1, idEquipo);
                try (java.sql.ResultSet rs = psBusca.executeQuery()) {
                    if (rs.next()) {
                        idMantenimiento = rs.getInt("id_mantenimiento");
                    }
                }
            }

            // Insert o update en mantenimiento_equipos
            if (idMantenimiento == -1) {
                String sqlCrearMant = "INSERT INTO mantenimiento_equipos (id_equipo, tipo_mantenimiento, estado, descripcion, id_tecnico, fecha_inicio, fecha_completado) VALUES (?, ?, ?, ?, ?, NOW(), ?)";
                try (java.sql.PreparedStatement psCrear = conn.prepareStatement(sqlCrearMant, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    psCrear.setInt(1, idEquipo);
                    psCrear.setString(2, tipoMant);
                    psCrear.setString(3, estadoMant);
                    psCrear.setString(4, observaciones);
                    psCrear.setInt(5, Sesion.getUsuarioActivo().getIdUsuario());
                    psCrear.setTimestamp(6, estadoMant.equals("Completado")     
                    ? new java.sql.Timestamp(System.currentTimeMillis()) 
                    : null);
                    psCrear.executeUpdate();
                    try (java.sql.ResultSet rsKeys = psCrear.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            idMantenimiento = rsKeys.getInt(1);
                        }
                    }
                }
            } else {
                String sqlUpdateMant = "UPDATE mantenimiento_equipos SET estado = ?, fecha_completado = ?, tipo_mantenimiento = ?, descripcion = ?, id_tecnico = ? WHERE id_mantenimiento = ?";
                try (java.sql.PreparedStatement psMant = conn.prepareStatement(sqlUpdateMant)) {
                    psMant.setString(1, estadoMant);
                    psMant.setTimestamp(2, estadoMant.equals("Completado") ? new java.sql.Timestamp(System.currentTimeMillis()) : null);
                    psMant.setString(3, tipoMant);
                    psMant.setString(4, observaciones);
                    psMant.setInt(5, Sesion.getUsuarioActivo().getIdUsuario()); 
                    psMant.setInt(6, idMantenimiento); 
                    psMant.executeUpdate();
                }
            }

            // Update de equipo
            String sqlUpdateEquipo = "UPDATE equipos_oficina SET estado = ? WHERE id_equipo = ?";
            try (java.sql.PreparedStatement psEqui = conn.prepareStatement(sqlUpdateEquipo)) {
                psEqui.setString(1, estadoEqui);
                psEqui.setInt(2, idEquipo);
                psEqui.executeUpdate();
            }

            // Detalle PC
            String sqlChecklist = "INSERT INTO detalle_mant_computador (id_mantenimiento, desarmado_inicial, limpieza_fisica, cambio_pasta, check_ram, check_almacenamiento, armado_cierre) VALUES (?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE desarmado_inicial=?, limpieza_fisica=?, cambio_pasta=?, check_ram=?, check_almacenamiento=?, armado_cierre=?";
            try (java.sql.PreparedStatement psCheck = conn.prepareStatement(sqlChecklist)) {
                psCheck.setInt(1, idMantenimiento);
                psCheck.setBoolean(2, desarme);
                psCheck.setBoolean(3, limpieza);
                psCheck.setBoolean(4, pasta);
                psCheck.setBoolean(5, ram);
                psCheck.setBoolean(6, almacenamiento);
                psCheck.setBoolean(7, cierre);
                psCheck.setBoolean(8, desarme);
                psCheck.setBoolean(9, limpieza);
                psCheck.setBoolean(10, pasta);
                psCheck.setBoolean(11, ram);
                psCheck.setBoolean(12, almacenamiento);
                psCheck.setBoolean(13, cierre);
                psCheck.executeUpdate();
            }

            // LÓGICA DE INVENTARIO
            registrarPiezasUsadas(conn, idMantenimiento, idEquipo, piezasUsadas);

            conn.commit();
            return true;
        } catch (java.sql.SQLException e) {
            System.err.println("Error en transacción PC: " + e.getMessage());
            try {
                conn.rollback();
            } catch (java.sql.SQLException ex) {
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (java.sql.SQLException e) {
            }
        }
        return false;
    }

    public boolean guardarMantenimientoCelular(int idEquipo, String estadoEqui, String estadoMant,
            boolean revPantalla, boolean limpiezaPuertos, boolean testBateria, boolean cierre, boolean sustitucion,
            String tipoMant, String observaciones, List<Integer> piezasUsadas) {

        Connection conn = Conexion.getInstancia();
        try {
            conn.setAutoCommit(false);
            int idMantenimiento = -1;
            String sqlBuscarMant = "SELECT id_mantenimiento FROM mantenimiento_equipos WHERE id_equipo = ? AND estado != 'Completado' LIMIT 1";
            try (java.sql.PreparedStatement psBusca = conn.prepareStatement(sqlBuscarMant)) {
                psBusca.setInt(1, idEquipo);
                try (java.sql.ResultSet rs = psBusca.executeQuery()) {
                    if (rs.next()) {
                        idMantenimiento = rs.getInt("id_mantenimiento");
                    }
                }
            }

            if (idMantenimiento == -1) {
                String sqlCrearMant = "INSERT INTO mantenimiento_equipos (id_equipo, tipo_mantenimiento, estado, descripcion, id_tecnico, fecha_inicio, fehca_completado) VALUES (?, ?, ?, ?, ?, NOW()), ?";
                try (java.sql.PreparedStatement psCrear = conn.prepareStatement(sqlCrearMant, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    psCrear.setInt(1, idEquipo);
                    psCrear.setString(2, tipoMant);
                    psCrear.setString(3, estadoMant);
                    psCrear.setString(4, observaciones);
                    psCrear.setInt(5, Sesion.getUsuarioActivo().getIdUsuario());
                    psCrear.setTimestamp(6, estadoMant.equals("Completado")     
                    ? new java.sql.Timestamp(System.currentTimeMillis()) 
                    : null);
                    psCrear.executeUpdate();
                    try (java.sql.ResultSet rsKeys = psCrear.getGeneratedKeys()) {
                        if (rsKeys.next()) {
                            idMantenimiento = rsKeys.getInt(1);
                        }
                    }
                }
            } else {
                String sqlUpdateMant = "UPDATE mantenimiento_equipos SET estado = ?, fecha_completado = ?, tipo_mantenimiento = ?, descripcion = ?, id_tecnico = ? WHERE id_mantenimiento = ?";
                try (java.sql.PreparedStatement psMant = conn.prepareStatement(sqlUpdateMant)) {
                    psMant.setString(1, estadoMant);
                    psMant.setTimestamp(2, estadoMant.equals("Completado") ? new java.sql.Timestamp(System.currentTimeMillis()) : null);
                    psMant.setString(3, tipoMant);
                    psMant.setString(4, observaciones);
                    psMant.setInt(5, Sesion.getUsuarioActivo().getIdUsuario()); 
                    psMant.setInt(6, idMantenimiento); 
                    psMant.executeUpdate();
                }
            }

            String sqlUpdateEquipo = "UPDATE equipos_oficina SET estado = ? WHERE id_equipo = ?";
            try (java.sql.PreparedStatement psEqui = conn.prepareStatement(sqlUpdateEquipo)) {
                psEqui.setString(1, estadoEqui);
                psEqui.setInt(2, idEquipo);
                psEqui.executeUpdate();
            }

            String sqlChecklist = "INSERT INTO detalle_mant_celular (id_mantenimiento, revision_pantalla_tactil, limpieza_puertos_carga, test_rendimiento_bateria, armado_cierre) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE revision_pantalla_tactil=?, limpieza_puertos_carga=?, test_rendimiento_bateria=?, armado_cierre=?";
            try (java.sql.PreparedStatement psCheck = conn.prepareStatement(sqlChecklist)) {
                psCheck.setInt(1, idMantenimiento);
                psCheck.setBoolean(2, revPantalla);
                psCheck.setBoolean(3, limpiezaPuertos);
                psCheck.setBoolean(4, testBateria);
                psCheck.setBoolean(5, cierre);
                psCheck.setBoolean(6, revPantalla);
                psCheck.setBoolean(7, limpiezaPuertos);
                psCheck.setBoolean(8, testBateria);
                psCheck.setBoolean(9, cierre);
                psCheck.executeUpdate();
            }

            // LÓGICA DE INVENTARIO
            registrarPiezasUsadas(conn, idMantenimiento, idEquipo, piezasUsadas);

            conn.commit();
            return true;
        } catch (java.sql.SQLException e) {
            System.err.println("Error en transacción Celular: " + e.getMessage());
            try {
                conn.rollback();
            } catch (java.sql.SQLException ex) {
            }
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (java.sql.SQLException e) {
            }
        }
        return false;
    }

    public boolean guardarMantenimientoImpresora(int idEquipo, String estadoEqui, String estadoMant,
            boolean limpiezaRodillos, boolean revisionToner, boolean calibracionCabezales, boolean actualizacionFirmware,
            boolean armado, boolean sustitucion, String tipoMant, String observaciones, List<Integer> piezasUsadas) {

        Connection conn = Conexion.getInstancia();
         try {
            conn.setAutoCommit(false);
            int idMantenimiento = -1;
            String sqlBuscarMant = "SELECT id_mantenimiento FROM mantenimiento_equipos WHERE id_equipo = ? AND estado != 'Completado' LIMIT 1";
            try (java.sql.PreparedStatement psBusca = conn.prepareStatement(sqlBuscarMant)) {
                psBusca.setInt(1, idEquipo);
                try (java.sql.ResultSet rs = psBusca.executeQuery()) {
                    if (rs.next()) idMantenimiento = rs.getInt("id_mantenimiento");
                }
            }

             if (idMantenimiento == -1) {
                String sqlCrearMant = "INSERT INTO mantenimiento_equipos (id_equipo, tipo_mantenimiento, estado, descripcion, id_tecnico, fecha_inicio, fecha_completado) VALUES (?, ?, ?, ?, ?, NOW()), ?";
                try (java.sql.PreparedStatement psCrear = conn.prepareStatement(sqlCrearMant, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                    psCrear.setInt(1, idEquipo); 
                    psCrear.setString(2, tipoMant); 
                    psCrear.setString(3, estadoMant); 
                    psCrear.setString(4, observaciones);
                    psCrear.setInt(5, Sesion.getUsuarioActivo().getIdUsuario());
                    psCrear.setTimestamp(6, estadoMant.equals("Completado")     
                    ? new java.sql.Timestamp(System.currentTimeMillis()) 
                    : null);
                    psCrear.executeUpdate();
                    try (java.sql.ResultSet rsKeys = psCrear.getGeneratedKeys()) {
                        if (rsKeys.next()) idMantenimiento = rsKeys.getInt(1);
                    }
                }
            } else {
                String sqlUpdateMant = "UPDATE mantenimiento_equipos SET estado = ?, fecha_completado = ?, tipo_mantenimiento = ?, descripcion = ?, id_tecnico = ? WHERE id_mantenimiento = ?";
                try (java.sql.PreparedStatement psMant = conn.prepareStatement(sqlUpdateMant)) {
                    psMant.setString(1, estadoMant); 
                    psMant.setTimestamp(2, estadoMant.equals("Completado") ? new java.sql.Timestamp(System.currentTimeMillis()) : null);
                    psMant.setString(3, tipoMant); 
                    psMant.setString(4, observaciones); 
                    psMant.setInt(5, Sesion.getUsuarioActivo().getIdUsuario()); 
                    psMant.setInt(6, idMantenimiento); 
                    psMant.executeUpdate();
                }
            }

            String sqlUpdateEquipo = "UPDATE equipos_oficina SET estado = ? WHERE id_equipo = ?";
            try (java.sql.PreparedStatement psEqui = conn.prepareStatement(sqlUpdateEquipo)) {
                psEqui.setString(1, estadoEqui); psEqui.setInt(2, idEquipo);
                psEqui.executeUpdate();
            }

            String sqlChecklist = "INSERT INTO detalle_mant_impresora (id_mantenimiento, limpieza_rodillos, revision_toner, calibracion_cabezales, actualizacion_firmware) VALUES (?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE limpieza_rodillos=?, revision_toner=?, calibracion_cabezales=?, actualizacion_firmware=?";
            try (java.sql.PreparedStatement psCheck = conn.prepareStatement(sqlChecklist)) {
                psCheck.setInt(1, idMantenimiento); psCheck.setBoolean(2, limpiezaRodillos); psCheck.setBoolean(3, revisionToner); psCheck.setBoolean(4, calibracionCabezales); psCheck.setBoolean(5, actualizacionFirmware);
                psCheck.setBoolean(6, limpiezaRodillos); psCheck.setBoolean(7, revisionToner); psCheck.setBoolean(8, calibracionCabezales); psCheck.setBoolean(9, actualizacionFirmware);
                psCheck.executeUpdate();
            }

            // LÓGICA DE INVENTARIO
            registrarPiezasUsadas(conn, idMantenimiento, idEquipo, piezasUsadas);

            conn.commit();
            return true;

        } catch (java.sql.SQLException e) {
            System.err.println("Error en transacción Impresora: " + e.getMessage());
            try { conn.rollback(); } catch (java.sql.SQLException ex) {}
        } finally {
            try { conn.setAutoCommit(true); } catch (java.sql.SQLException e) {}
        }
        return false;
    }

    public boolean insertar(EquipoOficina equipo) {
        String sql = "INSERT INTO equipos_oficina "
                + "(id_tipo_equipo, marca, modelo, numero_serie, estado, id_responsable, fecha_adquisicion) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection cn = Conexion.getInstancia();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, equipo.getTipoEquipo().getIdTipoEquipo());
            ps.setString(2, equipo.getMarca());
            ps.setString(3, equipo.getModelo());
            ps.setString(4, equipo.getNumeroSerie());
            ps.setString(5, equipo.getEstado());

            // id_responsable puede ser 0 si no se asigna
            if (equipo.getIdResponsable() > 0) {
                ps.setInt(6, equipo.getIdResponsable());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            // fecha_adquisicion puede ser null
            if (equipo.getFechaAdquisicion() != null && !equipo.getFechaAdquisicion().isEmpty()) {
                ps.setDate(7, java.sql.Date.valueOf(equipo.getFechaAdquisicion()));
            } else {
                ps.setNull(7, java.sql.Types.DATE);
            }

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar equipo: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(EquipoOficina equipo) {
        String sql = "UPDATE equipos_oficina SET "
                + "id_tipo_equipo = ?, marca = ?, modelo = ?, numero_serie = ?, "
                + "estado = ?, id_responsable = ?, fecha_adquisicion = ? "
                + "WHERE id_equipo = ?";

        Connection cn = Conexion.getInstancia();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, equipo.getTipoEquipo().getIdTipoEquipo());
            ps.setString(2, equipo.getMarca());
            ps.setString(3, equipo.getModelo());
            ps.setString(4, equipo.getNumeroSerie());
            ps.setString(5, equipo.getEstado());

            if (equipo.getIdResponsable() > 0) {
                ps.setInt(6, equipo.getIdResponsable());
            } else {
                ps.setNull(6, java.sql.Types.INTEGER);
            }

            if (equipo.getFechaAdquisicion() != null && !equipo.getFechaAdquisicion().isEmpty()) {
                ps.setDate(7, java.sql.Date.valueOf(equipo.getFechaAdquisicion()));
            } else {
                ps.setNull(7, java.sql.Types.DATE);
            }

            ps.setInt(8, equipo.getIdEquipo());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar equipo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idEquipo) {
        String sql = "DELETE FROM equipos_oficina WHERE id_equipo = ?";

        Connection cn = Conexion.getInstancia();
        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idEquipo);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar equipo: " + e.getMessage());
            return false;
        }
    }

    public EquipoOficina buscarEquipo(int id) {
        String sql = "SELECT e.*, t.nombre AS tipo_nombre, u.nombre AS responsable_nombre "
                + "FROM equipos_oficina e "
                + "JOIN tipos_equipo t ON e.id_tipo_equipo = t.id_tipo_equipo "
                + "LEFT JOIN usuarios u ON e.id_responsable = u.id_usuario "
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
                    equipo.setIdResponsable(rs.getInt("id_responsable"));
                    equipo.setNombreResponsable(rs.getString("responsable_nombre"));
                    equipo.setFechaRegistro(rs.getTimestamp("fecha_registro"));

                    // fecha_adquisicion
                    java.sql.Date fechaSQL = rs.getDate("fecha_adquisicion");
                    if (fechaSQL != null) {
                        equipo.setFechaAdquisicion(fechaSQL.toString());
                    }

                    return equipo;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar equipo: " + e.getMessage());
        }
        return null;

    } // Class
}
