/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import dao.KilometrajeDAO;
import dao.UsuarioDAO;
import dao.VehiculoDAO;
import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import modelo.Kilometraje;
import modelo.Usuario;
import modelo.Vehiculo;

/**
 *
 * @author gustavo
 */
public class VistaAdminFlota extends javax.swing.JFrame {

    /**
     * Creates new form VistaAdminFlota
     */
    private DefaultTableModel modeloTablaGeneral;
    private VehiculoDAO vehiculoDAO;
    private KilometrajeDAO kilometrajeDAO;
    private UsuarioDAO usuarioDAO;
    private HashMap<String, Usuario> mapaUsuarios;

    public VistaAdminFlota() {
        initComponents();

        this.setSize(1260, 750);

        // Evita que el usuario cambie el tamaño de la ventana
        this.setResizable(false);

        // Centra la ventana en la pantalla
        this.setLocationRelativeTo(null);

        this.setTitle("Administrador de Flota");

        // Inicializar DAO
        vehiculoDAO = new VehiculoDAO();
        kilometrajeDAO = new KilometrajeDAO();
        usuarioDAO = new UsuarioDAO();
        mapaUsuarios = new HashMap<>();

        // Configurar modelos de tabla
        configurarModelosTabla();
        tablaFlota.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Cargar datos iniciales
        cargarTabla();
        cargarComboConductor();
        limpiarCampos();
    }

    private void configurarModelosTabla() {

        modeloTablaGeneral = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Patente", "Marca", "Modelo", "Año", "Conductor", "Kilometraje Inicial", "Últimos Km", "Fecha Registro"}
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

    private void cargarComboConductor() {
        cmbConductorFlota.removeAllItems();
        mapaUsuarios.clear();

        cmbConductorFlota.addItem("Asignar");

        List<Usuario> conductores = usuarioDAO.listarConductoresSinAsignacion();
        for (Usuario u : conductores) {
            String nombre = u.getNombreUsuario();
            cmbConductorFlota.addItem(nombre);
            mapaUsuarios.put(nombre, u);
        }
    }

    private void limpiarCampos() {
        txtPatenteFlota.setText("");
        txtMarcaFlota.setText("");
        txtModeloFlota.setText("");
        txtAnioFlota.setText("");
        cmbConductorFlota.setSelectedIndex(0);
        txtKmInicial.setText("");
    }

    private void cargarTabla() {
        tablaFlota.setModel(modeloTablaGeneral);

        // Limpiar la tabla
        modeloTablaGeneral.setRowCount(0);

        try {
            List<Vehiculo> vehiculos = vehiculoDAO.listarVehiculos();

            // Llenar la tabla
            for (Vehiculo v : vehiculos) {
                // Conductor puede ser null si el vehículo no tiene uno asignado
                String nombreConductor = (v.getConductor() != null)
                        ? v.getConductor().getNombreUsuario()
                        : "Sin conductor";

                // Obtener el último km registrado
                Kilometraje ultimoKm = kilometrajeDAO.ultimoKmPorVehiculo(v.getIdVehiculo());
                String ultimoKmStr = (ultimoKm != null)
                        ? ultimoKm.getKilometros() + " km"
                        : "Sin registros";

                Object[] fila = {
                    v.getIdVehiculo(),
                    v.getPatente(),
                    v.getMarca(),
                    v.getModelo(),
                    v.getAnio(),
                    nombreConductor, // conductor del vehículo
                    v.getKilometrajeInicial(),
                    ultimoKmStr,
                    v.getFechaRegistro()
                };
                modeloTablaGeneral.addRow(fila);
            }
            ajustarColumnas(tablaFlota);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar los vehículos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
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

        lblConductorFlota = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaFlota = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        lblPatenteFlota = new javax.swing.JLabel();
        txtPatenteFlota = new javax.swing.JTextField();
        lblMarcaFlota = new javax.swing.JLabel();
        txtMarcaFlota = new javax.swing.JTextField();
        lblModeloFlota = new javax.swing.JLabel();
        txtModeloFlota = new javax.swing.JTextField();
        lblAnioFlota = new javax.swing.JLabel();
        txtAnioFlota = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cmbConductorFlota = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        txtKmInicial = new javax.swing.JTextField();
        btnAsignar = new javax.swing.JButton();
        btnActualizarFlota = new javax.swing.JButton();
        btnAgregarFlota = new javax.swing.JButton();
        btnEliminarFlota = new javax.swing.JButton();
        btnAgregarConductor = new javax.swing.JButton();
        btnLimpiarFlota = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(1260, 760));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblConductorFlota.setBackground(new java.awt.Color(255, 255, 255));
        lblConductorFlota.setPreferredSize(new java.awt.Dimension(1260, 760));

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        jLabel1.setText("MENÚ ADMINISTRACIÓN FLOTA");

        tablaFlota.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaFlota.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaFlotaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaFlota);

        jLabel2.setText("DATOS VEHÍCULO");

        lblPatenteFlota.setText("PATENTE");

        lblMarcaFlota.setText("MARCA");

        lblModeloFlota.setText("MODELO");

        lblAnioFlota.setText("AÑO");

        jLabel3.setText("CONDUCTOR");

        cmbConductorFlota.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText("KM INICIAL");

        btnAsignar.setBackground(new java.awt.Color(0, 0, 0));
        btnAsignar.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        btnAsignar.setForeground(new java.awt.Color(255, 255, 255));
        btnAsignar.setText("ASIGNAR");

        btnActualizarFlota.setBackground(new java.awt.Color(0, 0, 0));
        btnActualizarFlota.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        btnActualizarFlota.setForeground(new java.awt.Color(255, 255, 255));
        btnActualizarFlota.setText("ACTUALIZAR VEHÍCULO");
        btnActualizarFlota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarFlotaActionPerformed(evt);
            }
        });

        btnAgregarFlota.setBackground(new java.awt.Color(0, 0, 0));
        btnAgregarFlota.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        btnAgregarFlota.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarFlota.setText("NUEVO VEHÍCULO");
        btnAgregarFlota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarFlotaActionPerformed(evt);
            }
        });

        btnEliminarFlota.setBackground(new java.awt.Color(255, 0, 51));
        btnEliminarFlota.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        btnEliminarFlota.setForeground(new java.awt.Color(255, 255, 255));
        btnEliminarFlota.setText("ELIMINAR VEHÍCULO");
        btnEliminarFlota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarFlotaActionPerformed(evt);
            }
        });

        btnAgregarConductor.setBackground(new java.awt.Color(0, 0, 0));
        btnAgregarConductor.setFont(new java.awt.Font("Liberation Sans", 1, 14)); // NOI18N
        btnAgregarConductor.setForeground(new java.awt.Color(255, 255, 255));
        btnAgregarConductor.setText("AGREGAR CONDUCTOR");
        btnAgregarConductor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarConductorActionPerformed(evt);
            }
        });

        btnLimpiarFlota.setBackground(new java.awt.Color(0, 0, 0));
        btnLimpiarFlota.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        btnLimpiarFlota.setForeground(new java.awt.Color(255, 255, 255));
        btnLimpiarFlota.setText("LIMPIAR CAMPOS");
        btnLimpiarFlota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarFlotaActionPerformed(evt);
            }
        });

        btnLogout.setText("CERRAR SESIÓN");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout lblConductorFlotaLayout = new javax.swing.GroupLayout(lblConductorFlota);
        lblConductorFlota.setLayout(lblConductorFlotaLayout);
        lblConductorFlotaLayout.setHorizontalGroup(
            lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblPatenteFlota)
                                    .addComponent(lblMarcaFlota)
                                    .addComponent(lblModeloFlota)
                                    .addComponent(lblAnioFlota)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel4))
                                .addGap(57, 57, 57)
                                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtPatenteFlota)
                                    .addComponent(txtMarcaFlota)
                                    .addComponent(txtModeloFlota)
                                    .addComponent(txtAnioFlota)
                                    .addComponent(cmbConductorFlota, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtKmInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(217, 217, 217)
                                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnAgregarFlota, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnEliminarFlota))
                                .addGap(49, 49, 49)
                                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(btnLimpiarFlota, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnActualizarFlota)))))
                    .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 991, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregarConductor))
                    .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                        .addGap(251, 251, 251)
                        .addComponent(btnAsignar)))
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                .addGap(415, 415, 415)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogout)
                .addGap(33, 33, 33))
        );
        lblConductorFlotaLayout.setVerticalGroup(
            lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel1))
                    .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(btnLogout)))
                .addGap(18, 18, 18)
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblConductorFlotaLayout.createSequentialGroup()
                        .addComponent(btnAgregarConductor, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71)))
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                        .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPatenteFlota)
                            .addComponent(txtPatenteFlota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblMarcaFlota)
                            .addComponent(txtMarcaFlota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnAgregarFlota, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnActualizarFlota, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblModeloFlota)
                            .addComponent(txtModeloFlota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblAnioFlota)
                            .addComponent(txtAnioFlota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                        .addGap(25, 25, 25)
                        .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEliminarFlota, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLimpiarFlota, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbConductorFlota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKmInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAsignar)
                .addGap(246, 246, 246))
        );

        getContentPane().add(lblConductorFlota, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1260, 760));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaFlotaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaFlotaMouseClicked
        // TODO add your handling code here:

        int filaSeleccionada = tablaFlota.getSelectedRow();

        // Verificar si la fila es válida
        if (filaSeleccionada == -1) {
            return;
        }

        try {

            if (tablaFlota.getModel() != modeloTablaGeneral) {
                JOptionPane.showMessageDialog(this,
                        "Para editar, primero cargue la tabla general con el botón 'Listar'",
                        "Información",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int idVeh = (int) tablaFlota.getValueAt(filaSeleccionada, 0);

            Vehiculo v = vehiculoDAO.obtenerPorId(idVeh);

            // Cargar los datos del objeto en los campos de texto
            txtPatenteFlota.setText(v.getPatente());
            txtMarcaFlota.setText(v.getMarca());
            txtModeloFlota.setText(v.getModelo());
            txtAnioFlota.setText(String.valueOf(v.getAnio()));
            txtKmInicial.setText(String.valueOf(v.getKilometrajeInicial()));

            if (v.getConductor() != null) {
                cmbConductorFlota.setSelectedItem(v.getConductor().getNombreUsuario()); // ✅
            } else {
                cmbConductorFlota.setSelectedItem("Asignar");
            }

            //campos no editables
            txtPatenteFlota.setEditable(false);
            txtMarcaFlota.setEditable(false);
            txtModeloFlota.setEditable(false);
            txtAnioFlota.setEditable(false);
            txtKmInicial.setEditable(false);

        } catch (IndexOutOfBoundsException e) {
            // Manejar el error si hay un problema al acceder a la lista
            JOptionPane.showMessageDialog(this, "Error: Problema de sincronización al cargar datos.", "Error de Sincronización", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_tablaFlotaMouseClicked

    private void btnAgregarConductorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarConductorActionPerformed
        // TODO add your handling code here:
        DialogAgregarConductor dialog = new DialogAgregarConductor(this, true);

        dialog.getContentPane().setBackground(new Color(255, 255, 255));
        dialog.setVisible(true);
        cargarComboConductor();
    }//GEN-LAST:event_btnAgregarConductorActionPerformed

    private void btnEliminarFlotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarFlotaActionPerformed
        // TODO add your handling code here:
        int filaSeleccionada = tablaFlota.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un registro de la tabla para eliminar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) modeloTablaGeneral.getValueAt(filaSeleccionada, 0);
        String patente = (String) tablaFlota.getValueAt(filaSeleccionada, 1);

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar el auto patente :\n"
                + patente + "?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            vehiculoDAO.eliminar(patente);

            JOptionPane.showMessageDialog(this,
                    "Vehículo eliminado correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            // Limpiar campos y recargar tabla
            limpiarCampos();
            cargarTabla();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al eliminar vehículoú: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }//GEN-LAST:event_btnEliminarFlotaActionPerformed

    private void btnLimpiarFlotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarFlotaActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        limpiarCampos();

        // Restaurar la tabla completa
        cargarTabla();

        //  Mostrar mensaje
        JOptionPane.showMessageDialog(this, "Formulario y tabla restaurados. Mostrando todos los vehículos.");
    }//GEN-LAST:event_btnLimpiarFlotaActionPerformed

    private void btnActualizarFlotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarFlotaActionPerformed
        // TODO add your handling code here:
        int filaSeleccionada = tablaFlota.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un registro de la tabla para actualizar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idVeh = (int) modeloTablaGeneral.getValueAt(filaSeleccionada, 0);
        Vehiculo v = vehiculoDAO.obtenerPorId(idVeh);

        System.out.println(v);
        DialogActualizarVehiculo dialog = new DialogActualizarVehiculo(this, true, v);
        dialog.setVisible(true);

        limpiarCampos();
        cargarTabla();
    }//GEN-LAST:event_btnActualizarFlotaActionPerformed

    private void btnAgregarFlotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarFlotaActionPerformed
        // TODO add your handling code here:
        DialogNuevoVehiculo dialog = new DialogNuevoVehiculo(this, true);

        dialog.getContentPane().setBackground(new Color(255, 255, 255));
        dialog.setVisible(true);
        cargarTabla();
    }//GEN-LAST:event_btnAgregarFlotaActionPerformed

    private void btnLogoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogoutActionPerformed
        // TODO add your handling code here:
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que desea cerrar sesión?",
                "Cerrar sesión",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (respuesta == JOptionPane.YES_OPTION) {
            util.Sesion.cerrarSesion(); // limpiar sesión
            new VistaLogin().setVisible(true); // abrir login
            this.dispose(); // cerrar vista actual
        }
    }//GEN-LAST:event_btnLogoutActionPerformed

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
            java.util.logging.Logger.getLogger(VistaAdminFlota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaAdminFlota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaAdminFlota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaAdminFlota.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaAdminFlota().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizarFlota;
    private javax.swing.JButton btnAgregarConductor;
    private javax.swing.JButton btnAgregarFlota;
    private javax.swing.JButton btnAsignar;
    private javax.swing.JButton btnEliminarFlota;
    private javax.swing.JButton btnLimpiarFlota;
    private javax.swing.JButton btnLogout;
    private javax.swing.JComboBox<String> cmbConductorFlota;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAnioFlota;
    private javax.swing.JPanel lblConductorFlota;
    private javax.swing.JLabel lblMarcaFlota;
    private javax.swing.JLabel lblModeloFlota;
    private javax.swing.JLabel lblPatenteFlota;
    private javax.swing.JTable tablaFlota;
    private javax.swing.JTextField txtAnioFlota;
    private javax.swing.JTextField txtKmInicial;
    private javax.swing.JTextField txtMarcaFlota;
    private javax.swing.JTextField txtModeloFlota;
    private javax.swing.JTextField txtPatenteFlota;
    // End of variables declaration//GEN-END:variables
}
