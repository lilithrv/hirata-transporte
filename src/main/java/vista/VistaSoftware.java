/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import dao.EquipoOficinaDAO;
import dao.TipoSoftwareDAO;
import dao.SoftwareDAO;
import dao.TipoEquipoDAO;
import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import modelo.EquipoOficina;
import modelo.Software;
import modelo.SoftwareEquipo;
import modelo.TipoEquipo;
import modelo.TipoSoftware;

/**
 *
 * @author leslie
 */
public class VistaSoftware extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VistaSoftware.class.getName());

    /**
     * Creates new form VistaSoftware
     */
    private TipoSoftware vehiculoDAO;
    private HashMap<String, TipoEquipo> mapaTipos;
    private SoftwareDAO softwareDAO;
    private EquipoOficinaDAO equipoDAO;
    private TipoEquipoDAO tipoEquipoDAO;
    private EquipoOficina equipoSeleccionado;

    public VistaSoftware() {
        initComponents();

        this.setSize(1260, 750);

        // Evita que el usuario cambie el tamaño de la ventana
        this.setResizable(false);

        // Centra la ventana en la pantalla
        this.setLocationRelativeTo(null);

        this.setTitle("Software Equipos");

        // Inicializar DAOs
        softwareDAO = new SoftwareDAO();
        equipoDAO = new EquipoOficinaDAO();
        tipoEquipoDAO = new TipoEquipoDAO();
        mapaTipos = new HashMap<>();

        tablaEquipos.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Tipo", "Marca", "Modelo", "N° Serie"}
        ));

        tablaSoftwareEquipo.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Tipo", "Software", "Versión"}
        ));

        cargarTipos();

    }

    private void cargarTipos() {
        cmbTipo.removeAllItems();
        mapaTipos.clear();
        cmbTipo.addItem("Seleccionar");

        List<TipoEquipo> tipos = tipoEquipoDAO.listarTodos();

        for (TipoEquipo t : tipos) {
            String nombre = t.getNombre();
            cmbTipo.addItem(nombre);
            mapaTipos.put(nombre, t);
        }
    }

    private void cargarTablaEquipos(int idTipo) {

        EquipoOficinaDAO dao = new EquipoOficinaDAO();
        List<EquipoOficina> equipos = dao.listarPorTipo(idTipo);

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
            "ID", "Tipo", "Marca", "Modelo", "N° Serie"
        });

        for (EquipoOficina eq : equipos) {
            modelo.addRow(new Object[]{
                eq.getIdEquipo(),
                eq.getTipoEquipo().getNombre(),
                eq.getMarca(),
                eq.getModelo(),
                eq.getNumeroSerie()
            });
        }

        tablaEquipos.setModel(modelo);

        // Ajusta cada columna al contenido más ancho
        tablaEquipos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int anchoTotal = 0;

        for (int col = 0; col < tablaEquipos.getColumnCount(); col++) {
            int maxAncho = 0;

            // Medir header
            TableColumn columna = tablaEquipos.getColumnModel().getColumn(col);
            TableCellRenderer headerRenderer = tablaEquipos.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    tablaEquipos, columna.getHeaderValue(), false, false, 0, col);
            maxAncho = headerComp.getPreferredSize().width;

            // Medir celdas
            for (int fila = 0; fila < tablaEquipos.getRowCount(); fila++) {
                TableCellRenderer renderer = tablaEquipos.getCellRenderer(fila, col);
                Component comp = tablaEquipos.prepareRenderer(renderer, fila, col);
                maxAncho = Math.max(maxAncho, comp.getPreferredSize().width);
            }

            columna.setPreferredWidth(maxAncho + 20);
            anchoTotal += maxAncho + 20;
        }

        int anchoVisible = tablaEquipos.getParent().getWidth();
        if (anchoTotal < anchoVisible) {
            tablaEquipos.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }

    }

    private void cargarTablaSoftware(int idEquipo) {
        tablaSoftwareEquipo.setVisible(true);

        SoftwareDAO dao = new SoftwareDAO();
        List<SoftwareEquipo> lista = dao.listarPorEquipo(idEquipo);

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
            "ID", "Tipo", "Software", "Versión"
        });

        for (SoftwareEquipo se : lista) {
            modelo.addRow(new Object[]{
                se.getIdSwEquipo(),
                se.getSoftware().getTipoSoftware().getNombre(),
                se.getSoftware().getNombre(),
                se.getVersion()
            });
        }

        tablaSoftwareEquipo.setModel(modelo);

        // Ajuste de ancho
        tablaSoftwareEquipo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int anchoTotal = 0;
        for (int col = 0; col < tablaSoftwareEquipo.getColumnCount(); col++) {
            int maxAncho = 0;
            TableColumn columna = tablaSoftwareEquipo.getColumnModel().getColumn(col);
            TableCellRenderer headerRenderer = tablaSoftwareEquipo.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    tablaSoftwareEquipo, columna.getHeaderValue(), false, false, 0, col);
            maxAncho = headerComp.getPreferredSize().width;
            for (int fila = 0; fila < tablaSoftwareEquipo.getRowCount(); fila++) {
                TableCellRenderer renderer = tablaSoftwareEquipo.getCellRenderer(fila, col);
                Component comp = tablaSoftwareEquipo.prepareRenderer(renderer, fila, col);
                maxAncho = Math.max(maxAncho, comp.getPreferredSize().width);
            }
            columna.setPreferredWidth(maxAncho + 20);
            anchoTotal += maxAncho + 20;
        }
        int anchoVisible = tablaSoftwareEquipo.getParent().getWidth();
        if (anchoTotal < anchoVisible) {
            tablaSoftwareEquipo.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
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

        pnlContenido = new javax.swing.JPanel();
        btnLogout = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        pnlFiltro = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox<>();
        btnBuscarTipo = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEquipos = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaSoftwareEquipo = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        btnInstalar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnDesinstalar = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlContenido.setBackground(new java.awt.Color(255, 255, 255));

        btnLogout.setText("CERRAR SESIÓN");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        lblTitulo.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblTitulo.setText(" GESTIÓN DE SOFTWARE EN EQUIPOS ");

        pnlFiltro.setBackground(new java.awt.Color(255, 255, 255));
        pnlFiltro.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Filtro tipo de equipo");

        cmbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        btnBuscarTipo.setText("BUSCAR");
        btnBuscarTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarTipoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFiltroLayout = new javax.swing.GroupLayout(pnlFiltro);
        pnlFiltro.setLayout(pnlFiltroLayout);
        pnlFiltroLayout.setHorizontalGroup(
            pnlFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltroLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63)
                .addComponent(btnBuscarTipo)
                .addContainerGap(134, Short.MAX_VALUE))
        );
        pnlFiltroLayout.setVerticalGroup(
            pnlFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltroLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(pnlFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBuscarTipo))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        jLabel2.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        jLabel2.setText("EQUIPOS");

        tablaEquipos.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaEquipos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaEquiposMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaEquipos);

        tablaSoftwareEquipo.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane2.setViewportView(tablaSoftwareEquipo);

        jLabel3.setText("SOFTWARE INSTALADO EN:");

        btnInstalar.setText("INSTALAR");
        btnInstalar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInstalarActionPerformed(evt);
            }
        });

        btnActualizar.setText("ACTUALIZAR VERSIÓN");
        btnActualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnActualizarActionPerformed(evt);
            }
        });

        btnDesinstalar.setText("DESINSTALAR");
        btnDesinstalar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDesinstalarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlContenidoLayout = new javax.swing.GroupLayout(pnlContenido);
        pnlContenido.setLayout(pnlContenidoLayout);
        pnlContenidoLayout.setHorizontalGroup(
            pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContenidoLayout.createSequentialGroup()
                .addContainerGap(487, Short.MAX_VALUE)
                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlContenidoLayout.createSequentialGroup()
                        .addComponent(btnLogout)
                        .addGap(47, 47, 47))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlContenidoLayout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addGap(427, 427, 427))))
            .addGroup(pnlContenidoLayout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(pnlFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(pnlContenidoLayout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addGap(18, 18, 18)
                            .addComponent(jTextField1))
                        .addGroup(pnlContenidoLayout.createSequentialGroup()
                            .addComponent(btnInstalar)
                            .addGap(43, 43, 43)
                            .addComponent(btnActualizar)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 39, Short.MAX_VALUE)
                            .addComponent(btnDesinstalar))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING)))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        pnlContenidoLayout.setVerticalGroup(
            pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlContenidoLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(btnLogout)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTitulo)
                .addGap(27, 27, 27)
                .addComponent(pnlFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlContenidoLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(36, 36, 36)
                        .addComponent(jLabel3))
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlContenidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInstalar)
                    .addComponent(btnActualizar)
                    .addComponent(btnDesinstalar))
                .addGap(22, 22, 22)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(157, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlContenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlContenido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBuscarTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarTipoActionPerformed
        // TODO add your handling code here:
        String seleccion = (String) cmbTipo.getSelectedItem();

        if (seleccion == null || seleccion.equals("Seleccionar")) {
            JOptionPane.showMessageDialog(this, "Selecciona un tipo.");
            return;
        }

        TipoEquipo tipo = mapaTipos.get(seleccion);
        int idTipo = tipo.getIdTipoEquipo();

        cargarTablaEquipos(idTipo);
    }//GEN-LAST:event_btnBuscarTipoActionPerformed

    private void tablaEquiposMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaEquiposMouseClicked
        // TODO add your handling code here:
        int filaSeleccionada = tablaEquipos.getSelectedRow();

        // Verificar si la fila es válida
        if (filaSeleccionada == -1) {
            return;
        }

        int idEquipo = (int) tablaEquipos.getValueAt(filaSeleccionada, 0);

        // Guardar el equipo seleccionado para usarlo en el dialog
        equipoSeleccionado = equipoDAO.buscarPorId(idEquipo);

        // Mostrar en el label "SOFTWARE INSTALADO EN:"
        if (equipoSeleccionado != null) {
            jTextField1.setText(
                    equipoSeleccionado.getMarca() + " "
                    + equipoSeleccionado.getModelo() + " — "
                    + equipoSeleccionado.getNumeroSerie()
            );
        }

        cargarTablaSoftware(idEquipo);
    }//GEN-LAST:event_tablaEquiposMouseClicked

    private void btnInstalarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInstalarActionPerformed
        // TODO add your handling code here:
        if (equipoSeleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Selecciona un equipo primero.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        DialogAgregarSoftware dialog = new DialogAgregarSoftware(
                this, true, equipoSeleccionado);
        dialog.setVisible(true);

        // Refrescar tabla después de cerrar el dialog
        if (dialog.isConfirmado()) {
            cargarTablaSoftware(equipoSeleccionado.getIdEquipo());
        }
    }//GEN-LAST:event_btnInstalarActionPerformed

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

    private void btnActualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnActualizarActionPerformed
        // TODO add your handling code here:
        int filaEquipo = tablaEquipos.getSelectedRow();
        if (filaEquipo == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un equipo primero.");
            return;
        }

        int filaSoftware = tablaSoftwareEquipo.getSelectedRow();
        if (filaSoftware == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un software para actualizar.");
            return;
        }

        int idSwEquipo = (int) tablaSoftwareEquipo.getValueAt(filaSoftware, 0);
        String nombreSoftware = (String) tablaSoftwareEquipo.getValueAt(filaSoftware, 2);
        String versionActual = (String) tablaSoftwareEquipo.getValueAt(filaSoftware, 3);

        DialogActualizarVersion dialog = new DialogActualizarVersion(
                this, true,
                idSwEquipo,
                nombreSoftware,
                versionActual
        );
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            cargarTablaSoftware(equipoSeleccionado.getIdEquipo());
        }
    }//GEN-LAST:event_btnActualizarActionPerformed

    private void btnDesinstalarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDesinstalarActionPerformed
        // TODO add your handling code here:
        int filaSeleccionada = tablaSoftwareEquipo.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un software para desinstalar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tablaSoftwareEquipo.getValueAt(filaSeleccionada, 0);
        String sw = (String) tablaSoftwareEquipo.getValueAt(filaSeleccionada, 2);

        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");

        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro que deseas desinstalar " + sw + "\n"
                + "del equipo " + equipoSeleccionado.getMarca() + " " + equipoSeleccionado.getModelo()
                + " (" + equipoSeleccionado.getNumeroSerie() + ")?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            softwareDAO.desinstalar(id);

            JOptionPane.showMessageDialog(this,
                    "Software desinstalado correctamente",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE);

            cargarTablaSoftware(equipoSeleccionado.getIdEquipo()); // ← recargar
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error al desinstalar software: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDesinstalarActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VistaSoftware().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnActualizar;
    private javax.swing.JButton btnBuscarTipo;
    private javax.swing.JButton btnDesinstalar;
    private javax.swing.JButton btnInstalar;
    private javax.swing.JButton btnLogout;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlContenido;
    private javax.swing.JPanel pnlFiltro;
    private javax.swing.JTable tablaEquipos;
    private javax.swing.JTable tablaSoftwareEquipo;
    // End of variables declaration//GEN-END:variables
}
