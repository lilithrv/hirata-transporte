/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import dao.MantenimientoDAO;
import dao.VehiculoDAO;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import modelo.Mantenimiento;
import modelo.enums.TipoMantenimiento;
import modelo.enums.EstadoMantenimiento;
import modelo.enums.OrigenMantenimiento;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class VistaAdminMantenimiento extends javax.swing.JFrame {

    /**
     * Creates new form VistaAdminMantenimiento
     */
    private DefaultTableModel modeloTablaGeneral;
    private MantenimientoDAO mantenimientoDAO;
    private VehiculoDAO vehiculoDAO;
    private Mantenimiento mantenimientoSeleccionado;
    private Border bordeOriginal;

    public VistaAdminMantenimiento() {
        initComponents();

        bordeOriginal = txtPaneDescripcion.getBorder();

        this.setSize(1260, 750);

        // Evita que el usuario cambie el tamaño de la ventana
        this.setResizable(false);

        // Centra la ventana en la pantalla
        this.setLocationRelativeTo(null);

        // Inicializar DAO
        mantenimientoDAO = new MantenimientoDAO();
        vehiculoDAO = new VehiculoDAO();

        this.setTitle("Administrador de Mantenimiento");

        // Configurar modelos de tabla
        configurarModelosTabla();
        tablaMantenimiento.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Cargar datos iniciales
        cargarTabla();
        cargarComboTipoMantenimiento();
        cargarComboEstadoMantenimiento();
        cargarComboOrigenMantenimiento();
        limpiarCampos();
    }

    private void configurarModelosTabla() {

        modeloTablaGeneral = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Patente", "Marca", "Modelo", "Año", "Conductor", "Tipo Mantenimiento", "Fecha Programación", "Kilometraje", "Estado",
                    "Realizado por", "Fecha Actualización"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
    }

    private void ajustarColumnas(JTable tabla) {
        for (int col = 0; col < tabla.getColumnCount(); col++) {
            int maxAncho = 0;

            // Medir el header
            TableCellRenderer headerRenderer = tabla.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    tabla, tabla.getColumnModel().getColumn(col).getHeaderValue(),
                    false, false, 0, col);
            maxAncho = headerComp.getPreferredSize().width;

            // Medir cada celda de la columna
            for (int fila = 0; fila < tabla.getRowCount(); fila++) {
                TableCellRenderer cellRenderer = tabla.getCellRenderer(fila, col);
                Component cellComp = tabla.prepareRenderer(cellRenderer, fila, col);
                maxAncho = Math.max(maxAncho, cellComp.getPreferredSize().width);
            }

            // Aplicar ancho + un pequeño margen
            tabla.getColumnModel().getColumn(col).setPreferredWidth(maxAncho + 10);
        }
    }

    private void cargarTabla() {
        tablaMantenimiento.setModel(modeloTablaGeneral);

        // Limpiar la tabla
        modeloTablaGeneral.setRowCount(0);

        try {
            List<Mantenimiento> mantenimientos = mantenimientoDAO.listarTodos();

            // Llenar la tabla
            for (Mantenimiento mant : mantenimientos) {
                // Conductor puede ser null si el vehículo no tiene uno asignado
                String nombreConductor = (mant.getVehiculo().getConductor() != null)
                        ? mant.getVehiculo().getConductor().getNombreUsuario()
                        : "Sin conductor";

                // Usuario que realizó el mantenimiento (puede ser null si está Programado)
                String nombreUsuarioMant = (mant.getUsuarioMantenimiento() != null)
                        ? mant.getUsuarioMantenimiento().getNombreUsuario()
                        : "Pendiente";

                Object[] fila = {
                    mant.getIdMantenimiento(),
                    mant.getVehiculo().getPatente(),
                    mant.getVehiculo().getMarca(),
                    mant.getVehiculo().getModelo(),
                    mant.getVehiculo().getAnio(),
                    nombreConductor, // conductor del vehículo
                    mant.getTipoMantenimiento(),
                    mant.getFechaCreacion(),
                    mant.getKilometraje(),
                    mant.getEstado(),
                    nombreUsuarioMant, // quién realizó el mantenimiento
                    mant.getFechaCompletado()
                };
                modeloTablaGeneral.addRow(fila);
                ajustarColumnas(tablaMantenimiento);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los mantenimientos programados: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void cargarComboTipoMantenimiento() {
        cmbTipoMantenimiento.removeAllItems();
        cmbTipoMantenimiento.addItem("Seleccionar");

        for (TipoMantenimiento tipo : TipoMantenimiento.values()) {
            cmbTipoMantenimiento.addItem(tipo);
        }
    }

    private void cargarComboEstadoMantenimiento() {
        cmbEstadoMantenimiento.removeAllItems();
        cmbEstadoMantenimiento.addItem("Seleccionar");

        for (EstadoMantenimiento estado : EstadoMantenimiento.values()) {
            cmbEstadoMantenimiento.addItem(estado);
        }
    }

    private void cargarComboOrigenMantenimiento() {
        cmbOrigenMantenimiento.removeAllItems();
        cmbOrigenMantenimiento.addItem("Seleccionar");

        for (OrigenMantenimiento origen : OrigenMantenimiento.values()) {
            cmbOrigenMantenimiento.addItem(origen);
        }
    }

    private void limpiarCampos() {
        txtPatente.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnio.setText("");
        txtConductor.setText("");
        txtKm.setText("");
        cmbTipoMantenimiento.setSelectedIndex(0);
        cmbTipoMantenimiento.setEnabled(true);
        cmbEstadoMantenimiento.setSelectedIndex(0);
        cmbOrigenMantenimiento.setSelectedIndex(0);
        cmbOrigenMantenimiento.setEnabled(true);
        txtPaneDescripcion.setText("");
    }

    private Border crearBordeError(Border bordeOriginal) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 2),
                bordeOriginal
        );
    }

    //fx para validar campos no nulos
    private void validarTexto(JTextPane campo, JLabel label) {
        if (campo.getText().trim().isEmpty()) {
            campo.setBorder(crearBordeError(bordeOriginal));
            label.setForeground(Color.BLACK);
        } else {
            campo.setBorder(bordeOriginal);
            label.setForeground(pnlContenido.getBackground());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        pnlContenido = new javax.swing.JPanel();
        lblMantenimiento = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaMantenimiento = new javax.swing.JTable();
        lblDatos = new javax.swing.JLabel();
        pnlDatos = new javax.swing.JPanel();
        lblPatente = new javax.swing.JLabel();
        lblMarca = new javax.swing.JLabel();
        lblModelo = new javax.swing.JLabel();
        lblAnio = new javax.swing.JLabel();
        txtPatente = new javax.swing.JTextField();
        txtMarca = new javax.swing.JTextField();
        txtModelo = new javax.swing.JTextField();
        txtAnio = new javax.swing.JTextField();
        lblConductor = new javax.swing.JLabel();
        txtConductor = new javax.swing.JTextField();
        lblTipoMant = new javax.swing.JLabel();
        cmbTipoMantenimiento = new javax.swing.JComboBox();
        lblKm = new javax.swing.JLabel();
        txtKm = new javax.swing.JTextField();
        lblEstado = new javax.swing.JLabel();
        cmbEstadoMantenimiento = new javax.swing.JComboBox();
        lblOrigen = new javax.swing.JLabel();
        cmbOrigenMantenimiento = new javax.swing.JComboBox();
        lblDescripcion = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtPaneDescripcion = new javax.swing.JTextPane();
        btnActualizar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        lblMensajeDescripcion = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnCorrectivo = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(664, 70));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblMantenimiento.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblMantenimiento.setText("MENÚ MANTENIMIENTO");

        tablaMantenimiento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tablaMantenimiento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaMantenimientoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaMantenimiento);

        lblDatos.setText("Datos Mantenimiento");

        lblPatente.setText("PATENTE");

        lblMarca.setText("MARCA");

        lblModelo.setText("MODELO");

        lblAnio.setText("AÑO");

        lblConductor.setText("CONDUCTOR");

        lblTipoMant.setText("MANTENIMIENTO");

        cmbTipoMantenimiento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblKm.setText("KILOMETRAJE");

        lblEstado.setText("ESTADO");

        cmbEstadoMantenimiento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblOrigen.setText("ORIGEN");

        cmbOrigenMantenimiento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        lblDescripcion.setText("DESCRIPCIÓN");

        jScrollPane2.setViewportView(txtPaneDescripcion);

        btnActualizar.setText("ACTUALIZAR");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        lblMensajeDescripcion.setFont(new java.awt.Font("Liberation Sans", 1, 10)); // NOI18N
        lblMensajeDescripcion.setForeground(new java.awt.Color(242, 242, 242));
        lblMensajeDescripcion.setText("Campo requerido");

        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlDatosLayout = new javax.swing.GroupLayout(pnlDatos);
        pnlDatos.setLayout(pnlDatosLayout);
        pnlDatosLayout.setHorizontalGroup(
            pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlDatosLayout.createSequentialGroup()
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPatente)
                            .addComponent(lblMarca)
                            .addComponent(lblModelo)
                            .addComponent(lblAnio)
                            .addComponent(lblConductor)
                            .addComponent(lblKm)
                            .addComponent(lblEstado))
                        .addGap(42, 42, 42)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtPatente)
                            .addComponent(txtMarca)
                            .addComponent(txtModelo)
                            .addComponent(txtAnio)
                            .addComponent(txtConductor)
                            .addComponent(txtKm)
                            .addComponent(cmbEstadoMantenimiento, 0, 165, Short.MAX_VALUE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosLayout.createSequentialGroup()
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTipoMant)
                            .addComponent(lblOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbTipoMantenimiento, 0, 165, Short.MAX_VALUE)
                            .addComponent(cmbOrigenMantenimiento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDatosLayout.createSequentialGroup()
                        .addGap(133, 133, 133)
                        .addComponent(lblDescripcion)
                        .addGap(18, 18, 18)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblMensajeDescripcion, javax.swing.GroupLayout.DEFAULT_SIZE, 269, Short.MAX_VALUE)
                            .addComponent(jScrollPane2)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosLayout.createSequentialGroup()
                                .addComponent(btnActualizar)
                                .addGap(44, 44, 44)
                                .addComponent(btnLimpiar)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosLayout.createSequentialGroup()
                                .addComponent(btnEliminar)
                                .addGap(75, 75, 75))))))
        );
        pnlDatosLayout.setVerticalGroup(
            pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDatosLayout.createSequentialGroup()
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(lblPatente)
                            .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtPatente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblDescripcion)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMarca)
                            .addComponent(txtMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblModelo)
                            .addComponent(txtModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAnio)
                            .addComponent(txtAnio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblConductor)
                            .addComponent(txtConductor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblTipoMant)
                            .addComponent(cmbTipoMantenimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblOrigen)
                            .addComponent(cmbOrigenMantenimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblKm)
                            .addComponent(txtKm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbEstadoMantenimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEstado))
                        .addGap(39, 39, 39))
                    .addGroup(pnlDatosLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMensajeDescripcion)
                        .addGap(18, 18, 18)
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnActualizar)
                            .addComponent(btnLimpiar))
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        btnCorrectivo.setText("<html>PROGRAMAR<br>MANTENIMIENTO</html>");
        btnCorrectivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCorrectivoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlContenidoLayout = new javax.swing.GroupLayout(pnlContenido);
        pnlContenido.setLayout(pnlContenidoLayout);
        pnlContenidoLayout.setHorizontalGroup(
            pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContenidoLayout.createSequentialGroup()
                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlContenidoLayout.createSequentialGroup()
                        .addGap(463, 463, 463)
                        .addComponent(lblMantenimiento))
                    .addGroup(pnlContenidoLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDatos)
                            .addComponent(pnlDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(pnlContenidoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1079, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCorrectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        pnlContenidoLayout.setVerticalGroup(
            pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContenidoLayout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(lblMantenimiento)
                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlContenidoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlContenidoLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(btnCorrectivo, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(32, 32, 32)
                .addComponent(lblDatos)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlDatos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(199, Short.MAX_VALUE))
        );

        btnCorrectivo.getAccessibleContext().setAccessibleName("PROGRAMAR \nMANTENIMIENTO");

        getContentPane().add(pnlContenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 1290, 780));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaMantenimientoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaMantenimientoMouseClicked
        // TODO add your handling code here:
        int filaSeleccionada = tablaMantenimiento.getSelectedRow();

        // Verificar si la fila es válida
        if (filaSeleccionada == -1) {
            return;
        }

        try {

            if (tablaMantenimiento.getModel() != modeloTablaGeneral) {
                JOptionPane.showMessageDialog(this,
                        "Para editar, primero cargue la tabla general con el botón 'Listar'",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int idMant = (int) tablaMantenimiento.getValueAt(filaSeleccionada, 0);

            Mantenimiento mantSeleccionado = mantenimientoDAO.obtenerPorId(idMant);

            // Cargar los datos del objeto en los campos de texto
            txtPatente.setText(mantSeleccionado.getVehiculo().getPatente());
            txtMarca.setText(mantSeleccionado.getVehiculo().getMarca());
            txtModelo.setText(mantSeleccionado.getVehiculo().getModelo());
            txtAnio.setText(String.valueOf(mantSeleccionado.getVehiculo().getAnio()));
            txtConductor.setText(mantSeleccionado.getVehiculo().getConductor().getNombreUsuario());
            cmbTipoMantenimiento.setSelectedItem(mantSeleccionado.getTipoMantenimiento());
            txtKm.setText(String.valueOf(mantSeleccionado.getKilometraje()));
            cmbEstadoMantenimiento.setSelectedItem(mantSeleccionado.getEstado());
            cmbOrigenMantenimiento.setSelectedItem(mantSeleccionado.getOrigen());

            //campos no editables
            txtPatente.setEditable(false);
            txtMarca.setEditable(false);
            txtModelo.setEditable(false);
            txtAnio.setEditable(false);
            txtConductor.setEditable(false);
            cmbTipoMantenimiento.setEnabled(false);
            txtKm.setEditable(false);
            cmbOrigenMantenimiento.setEnabled(false);

        } catch (IndexOutOfBoundsException e) {
            // Manejar el error si hay un problema al acceder a la lista
            JOptionPane.showMessageDialog(this, "Error: Problema de sincronización al cargar datos.", "Error de Sincronización", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_tablaMantenimientoMouseClicked

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        limpiarCampos();

        // Restaurar bordes originales 
        txtPaneDescripcion.setBorder(bordeOriginal);

        // Ocultar mensajes de error
        Color fondo = pnlContenido.getBackground();

        // Restaurar la tabla completa
        cargarTabla();

        //  Mostrar mensaje
        JOptionPane.showMessageDialog(this, "Formulario y tabla restaurados. Mostrando todos los mantenimientos.");

    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnCorrectivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCorrectivoActionPerformed
        // TODO add your handling code here:
        DialogProgramarCorrectivo dialog = new DialogProgramarCorrectivo(this, true);
        dialog.setVisible(true);
        cargarTabla();
    }//GEN-LAST:event_btnCorrectivoActionPerformed

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:

        int filaSeleccionada = tablaMantenimiento.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un registro de la tabla para actualizar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTablaGeneral.getValueAt(filaSeleccionada, 0);
        mantenimientoSeleccionado = mantenimientoDAO.obtenerPorId(id);

        // No se puede actualizar si ya está completado o cancelado
        if (mantenimientoSeleccionado.getEstado() == EstadoMantenimiento.Completado
                || mantenimientoSeleccionado.getEstado() == EstadoMantenimiento.Cancelado) {
            JOptionPane.showMessageDialog(this, "No se puede modificar un mantenimiento ya finalizado.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Validar que seleccionó un estado
        if (cmbEstadoMantenimiento.getSelectedItem() == null
                || cmbEstadoMantenimiento.getSelectedItem().equals("Seleccionar")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un estado.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        EstadoMantenimiento estadoNuevo = EstadoMantenimiento.valueOf(cmbEstadoMantenimiento.getSelectedItem().toString());

        if (estadoNuevo == mantenimientoSeleccionado.getEstado()) {
            JOptionPane.showMessageDialog(this, "El estado seleccionado es igual al estado actual.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String descripcion = txtPaneDescripcion.getText().trim();

        validarTexto(txtPaneDescripcion, lblMensajeDescripcion);

        // Descripción obligatoria si va a completar o cancelar
        if ((estadoNuevo == EstadoMantenimiento.Completado || estadoNuevo == EstadoMantenimiento.Cancelado)
                && txtPaneDescripcion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar una descripción al completar o cancelar.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        mantenimientoSeleccionado.setDescripcion(descripcion);
        mantenimientoSeleccionado.setEstado(estadoNuevo);

        // El usuario lo maneja el DAO directamente desde Sesion.getUsuarioActivo()
        boolean actualizado = mantenimientoDAO.actualizar2(mantenimientoSeleccionado);
        if (actualizado) {
            // Si se completó o canceló, remover conductor del vehículo
            if (estadoNuevo == EstadoMantenimiento.Completado
                    || estadoNuevo == EstadoMantenimiento.Cancelado) {
                vehiculoDAO.removerConductor(mantenimientoSeleccionado.getVehiculo().getIdVehiculo());
            }
            JOptionPane.showMessageDialog(this, "Mantenimiento actualizado correctamente.", "Éxito", INFORMATION_MESSAGE);
            cargarTabla();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el mantenimiento.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        int filaSeleccionada = tablaMantenimiento.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un registro de la tabla para eliminar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTablaGeneral.getValueAt(filaSeleccionada, 0);
        String patente = (String) tablaMantenimiento.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el mantenimiento del auto patente :\n"
                + patente + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            mantenimientoDAO.eliminar(id);

            JOptionPane.showMessageDialog(this,
                    "Mantenimiento eliminado correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            // Limpiar campos y recargar tabla
            limpiarCampos();
            cargarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar país: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(VistaAdminMantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaAdminMantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaAdminMantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaAdminMantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaAdminMantenimiento().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnCorrectivo;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cmbEstadoMantenimiento;
    private javax.swing.JComboBox cmbOrigenMantenimiento;
    private javax.swing.JComboBox cmbTipoMantenimiento;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblConductor;
    private javax.swing.JLabel lblDatos;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblEstado;
    private javax.swing.JLabel lblKm;
    private javax.swing.JLabel lblMantenimiento;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblMensajeDescripcion;
    private javax.swing.JLabel lblModelo;
    private javax.swing.JLabel lblOrigen;
    private javax.swing.JLabel lblPatente;
    private javax.swing.JLabel lblTipoMant;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JPanel pnlDatos;
    private javax.swing.JTable tablaMantenimiento;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtConductor;
    private javax.swing.JTextField txtKm;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextPane txtPaneDescripcion;
    private javax.swing.JTextField txtPatente;
    // End of variables declaration//GEN-END:variables
}
