package vista;

import conn.Conexion;
import dao.PiezaDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import modelo.Pieza;
import modelo.TipoPieza;

public class VistaInventarioPiezas extends JFrame {

    private final PiezaDAO piezaDAO = new PiezaDAO();
    private final List<Pieza> piezas = new ArrayList<>();

    private JComboBox<TipoPieza> cmbTipo;
    private JTextField txtMarca;
    private JTextField txtModelo;
    private JTextArea txtDescripcion;
    private JSpinner spStockActual;
    private JSpinner spStockMinimo;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField txtBuscar;
    private JLabel lblTotal;
    private JLabel lblStockBajo;
    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    private JButton btnStockBajo;
    private JButton btnVerTodo;
    private JButton btnCerrarSesion;

    private Integer idSeleccionado = null;

    public VistaInventarioPiezas() {
        construirInterfaz();

        EstiloHirata.aplicarVentana(this, "Mantenimiento de Piezas - Inventario", 1400, 1000);

        setMinimumSize(new java.awt.Dimension(1400, 850));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        cargarTiposPieza();
        cargarTabla(false);
    }

    private void construirInterfaz() {
        JPanel raiz = new JPanel(new BorderLayout(18, 18));
        raiz.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));
        raiz.setBackground(EstiloHirata.FONDO);
        setContentPane(raiz);

        JPanel cabecera = new JPanel(new BorderLayout(12, 8));
        cabecera.setBackground(EstiloHirata.AZUL_NOCHE);
        cabecera.setBorder(BorderFactory.createEmptyBorder(18, 22, 18, 22));

        JLabel titulo = new JLabel("Mantenimiento de piezas");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 26));

        JLabel subtitulo = new JLabel("RF-09 · Control de inventario de repuestos para mantenimiento de equipos");
        subtitulo.setForeground(new Color(203, 213, 225));
        subtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        cabecera.add(titulo, BorderLayout.NORTH);
        cabecera.add(subtitulo, BorderLayout.CENTER);
        raiz.add(cabecera, BorderLayout.NORTH);

        JPanel centro = new JPanel(new BorderLayout(18, 0));
        centro.setOpaque(false);
        raiz.add(centro, BorderLayout.CENTER);

        centro.add(crearFormulario(), BorderLayout.WEST);
        centro.add(crearPanelTabla(), BorderLayout.CENTER);

        JPanel resumen = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 10));
        resumen.setBackground(Color.WHITE);
        resumen.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(EstiloHirata.BORDE),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));

        lblTotal = new JLabel("Total piezas: 0");
        lblStockBajo = new JLabel("Stock bajo: 0");
        resumen.add(lblTotal);
        resumen.add(lblStockBajo);

        raiz.add(resumen, BorderLayout.SOUTH);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setPreferredSize(new Dimension(390, 0));
        EstiloHirata.tarjeta(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.weightx = 1;

        JLabel titulo = new JLabel("Datos de la pieza");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titulo.setForeground(new Color(15, 23, 42));
        gbc.gridy = 0;
        panel.add(titulo, gbc);

        cmbTipo = new JComboBox<>();
        txtMarca = new JTextField();
        txtModelo = new JTextField();
        txtDescripcion = new JTextArea(4, 20);
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        spStockActual = new JSpinner(new SpinnerNumberModel(0, 0, 9999, 1));
        spStockMinimo = new JSpinner(new SpinnerNumberModel(1, 0, 9999, 1));

        agregarCampo(panel, gbc, 1, "Tipo de pieza", cmbTipo);
        agregarCampo(panel, gbc, 2, "Marca", txtMarca);
        agregarCampo(panel, gbc, 3, "Modelo", txtModelo);
        agregarCampo(panel, gbc, 4, "Descripción", new JScrollPane(txtDescripcion));
        agregarCampo(panel, gbc, 5, "Stock actual", spStockActual);
        agregarCampo(panel, gbc, 6, "Stock mínimo", spStockMinimo);

        JPanel botones = new JPanel(new java.awt.GridLayout(2, 2, 8, 8));
        botones.setOpaque(false);

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        btnCerrarSesion = new JButton("Cerrar sesión");

        botones.add(btnGuardar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);

        gbc.gridy = 7;
        gbc.insets = new Insets(14, 0, 0, 0);
        panel.add(botones, gbc);

        gbc.gridy = 7;
        gbc.insets = new Insets(14, 0, 0, 0);
        panel.add(botones, gbc);

        btnCerrarSesion = new JButton("Cerrar sesión");

        gbc.gridy = 8;
        gbc.insets = new Insets(8, 0, 0, 0);
        panel.add(btnCerrarSesion, gbc);

        btnGuardar.addActionListener(e -> guardar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        btnCerrarSesion.addActionListener(e -> {
            dispose();
            new VistaLogin().setVisible(true);
        });

        return panel;

    }

    private void agregarCampo(JPanel panel, GridBagConstraints gbc, int y, String etiqueta, java.awt.Component campo) {
        JPanel grupo = new JPanel(new BorderLayout(0, 6));
        grupo.setOpaque(false);

        JLabel label = new JLabel(etiqueta);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setForeground(EstiloHirata.TEXTO_SUAVE);

        grupo.add(label, BorderLayout.NORTH);
        grupo.add(campo, BorderLayout.CENTER);

        gbc.gridy = y;
        gbc.insets = new Insets(7, 0, 7, 0);
        panel.add(grupo, gbc);
    }

    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        EstiloHirata.tarjeta(panel);

        JPanel superior = new JPanel(new BorderLayout(10, 0));
        superior.setOpaque(false);

        JLabel titulo = new JLabel("Inventario registrado");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        txtBuscar = new JTextField();
        txtBuscar.setToolTipText("Buscar por tipo, marca, modelo o descripción");

        superior.add(titulo, BorderLayout.WEST);
        superior.add(txtBuscar, BorderLayout.CENTER);

        JPanel filtros = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        filtros.setOpaque(false);

        btnStockBajo = new JButton("Ver stock bajo");
        btnVerTodo = new JButton("Ver todo");

        filtros.add(btnStockBajo);
        filtros.add(btnVerTodo);
        superior.add(filtros, BorderLayout.EAST);

        panel.add(superior, BorderLayout.NORTH);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Tipo", "Marca", "Modelo", "Descripción", "Stock", "Mínimo", "Registro"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabla = new JTable(modeloTabla);
        tabla.setAutoCreateRowSorter(true);

        sorter = new TableRowSorter<>(modeloTabla);
        tabla.setRowSorter(sorter);

        tabla.getColumnModel().getColumn(0).setMaxWidth(55);
        tabla.getColumnModel().getColumn(5).setMaxWidth(70);
        tabla.getColumnModel().getColumn(6).setMaxWidth(80);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                cargarSeleccion();
            }
        });

        txtBuscar.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrar();
            }
        });

        btnStockBajo.addActionListener(e -> cargarTabla(true));
        btnVerTodo.addActionListener(e -> cargarTabla(false));

        return panel;
    }

    private void cargarTiposPieza() {
        cmbTipo.removeAllItems();

        String sql = "SELECT id_tipo_pieza, nombre FROM tipos_pieza ORDER BY nombre";
        Connection conn = Conexion.getInstancia();

        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                cmbTipo.addItem(new TipoPieza(rs.getInt("id_tipo_pieza"), rs.getString("nombre")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "No se pudieron cargar los tipos de pieza: " + e.getMessage(),
                    "Base de datos",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarTabla(boolean soloStockBajo) {
        piezas.clear();
        piezas.addAll(soloStockBajo ? piezaDAO.listarStockBajo() : piezaDAO.listarTodos());

        modeloTabla.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        int stockBajo = 0;

        for (Pieza p : piezas) {
            if (p.stockBajo()) {
                stockBajo++;
            }

            modeloTabla.addRow(new Object[]{
                p.getIdPieza(),
                p.getTipoPieza().getNombre(),
                p.getMarca(),
                p.getModelo(),
                p.getDescripcion(),
                p.getStockActual(),
                p.getStockMinimo(),
                p.getFechaRegistro() == null ? "" : sdf.format(p.getFechaRegistro())
            });
        }

        lblTotal.setText("Total piezas: " + piezas.size());
        lblStockBajo.setText("Stock bajo: " + stockBajo);
        filtrar();
    }

    private void cargarSeleccion() {
        int filaModelo = tabla.convertRowIndexToModel(tabla.getSelectedRow());
        int id = (Integer) modeloTabla.getValueAt(filaModelo, 0);

        Pieza p = piezas.stream()
                .filter(x -> x.getIdPieza() == id)
                .findFirst()
                .orElse(null);

        if (p == null) {
            return;
        }

        idSeleccionado = p.getIdPieza();
        seleccionarTipo(p.getTipoPieza().getIdTipoPieza());
        txtMarca.setText(p.getMarca());
        txtModelo.setText(p.getModelo());
        txtDescripcion.setText(p.getDescripcion());
        spStockActual.setValue(p.getStockActual());
        spStockMinimo.setValue(p.getStockMinimo());
    }

    private void seleccionarTipo(int idTipo) {
        for (int i = 0; i < cmbTipo.getItemCount(); i++) {
            if (cmbTipo.getItemAt(i).getIdTipoPieza() == idTipo) {
                cmbTipo.setSelectedIndex(i);
                return;
            }
        }
    }

    private Pieza leerFormulario() {
        if (cmbTipo.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un tipo de pieza.", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        if (txtMarca.getText().trim().isEmpty() || txtModelo.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Marca y modelo son obligatorios.", "Validación", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int stockActual = (Integer) spStockActual.getValue();
        int stockMinimo = (Integer) spStockMinimo.getValue();

        Pieza p = new Pieza(
                (TipoPieza) cmbTipo.getSelectedItem(),
                txtMarca.getText().trim(),
                txtModelo.getText().trim(),
                txtDescripcion.getText().trim(),
                stockActual,
                stockMinimo
        );

        if (idSeleccionado != null) {
            p.setIdPieza(idSeleccionado);
        }

        return p;
    }

    private void guardar() {
        Pieza p = leerFormulario();

        if (p == null) {
            return;
        }

        if (piezaDAO.insertar(p)) {
            JOptionPane.showMessageDialog(this, "Pieza registrada correctamente.");
            limpiar();
            cargarTabla(false);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo registrar la pieza.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizar() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una pieza de la tabla para actualizar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Pieza p = leerFormulario();

        if (p == null) {
            return;
        }

        if (piezaDAO.actualizar(p)) {
            JOptionPane.showMessageDialog(this, "Pieza actualizada correctamente.");
            limpiar();
            cargarTabla(false);
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar la pieza.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminar() {
        if (idSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una pieza de la tabla para eliminar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int r = JOptionPane.showConfirmDialog(this,
                "¿Eliminar la pieza seleccionada?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (r == JOptionPane.YES_OPTION) {
            if (piezaDAO.eliminar(idSeleccionado)) {
                JOptionPane.showMessageDialog(this, "Pieza eliminada correctamente.");
                limpiar();
                cargarTabla(false);
            } else {
                JOptionPane.showMessageDialog(this,
                        "No se pudo eliminar. Puede estar asociada a un mantenimiento o equipo.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void limpiar() {
        idSeleccionado = null;

        if (cmbTipo.getItemCount() > 0) {
            cmbTipo.setSelectedIndex(0);
        }

        txtMarca.setText("");
        txtModelo.setText("");
        txtDescripcion.setText("");
        spStockActual.setValue(0);
        spStockMinimo.setValue(1);
        tabla.clearSelection();
        txtMarca.requestFocus();
    }

    private void filtrar() {
        String texto = txtBuscar.getText().trim();

        sorter.setRowFilter(
                texto.isEmpty()
                ? null
                : RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(texto))
        );
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new VistaInventarioPiezas().setVisible(true));
    }
}
