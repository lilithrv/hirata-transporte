/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import dao.KilometrajeDAO;
import dao.UsuarioDAO;
import dao.VehiculoDAO;
import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
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

        List<Usuario> conductores = usuarioDAO.listarConductores();
        for (Usuario u : conductores) {
            String nombre = u.getNombreUsuario();
            cmbConductorFlota.addItem(nombre);
            mapaUsuarios.put(nombre, u);
        }
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        javax.swing.GroupLayout lblConductorFlotaLayout = new javax.swing.GroupLayout(lblConductorFlota);
        lblConductorFlota.setLayout(lblConductorFlotaLayout);
        lblConductorFlotaLayout.setHorizontalGroup(
            lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lblConductorFlotaLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(309, 309, 309))
            .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 991, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
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
                                    .addComponent(cmbConductorFlota, 0, 148, Short.MAX_VALUE)
                                    .addComponent(txtKmInicial))))))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        lblConductorFlotaLayout.setVerticalGroup(
            lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lblConductorFlotaLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPatenteFlota)
                    .addComponent(txtPatenteFlota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMarcaFlota)
                    .addComponent(txtMarcaFlota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblModeloFlota)
                    .addComponent(txtModeloFlota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAnioFlota)
                    .addComponent(txtAnioFlota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbConductorFlota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(lblConductorFlotaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKmInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addContainerGap(49, Short.MAX_VALUE))
        );

        getContentPane().add(lblConductorFlota, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 1090, 530));

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
        } catch (IndexOutOfBoundsException e) {
            // Manejar el error si hay un problema al acceder a la lista
            JOptionPane.showMessageDialog(this, "Error: Problema de sincronización al cargar datos.", "Error de Sincronización", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_tablaFlotaMouseClicked

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
