/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista.historialequipos;

import vista.software.*;
import vista.software.DialogEditarSoftware;
import vista.software.DialogNuevoSoftware;
import vista.software.DialogAgregarSoftware;
import dao.EquipoOficinaDAO;
import dao.MantenimientoEquipoOficinaDAO;
import dao.SoftwareDAO;
import dao.TipoEquipoDAO;
import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import modelo.EquipoOficina;
import modelo.MantenimientoEquipoOficina;
import modelo.Software;
import modelo.SoftwareEquipo;
import modelo.TipoEquipo;
import modelo.TipoSoftware;
import vista.EstiloHirata;
import vista.VistaLogin;

/**
 *
 * @author leslie
 */
public class VistaHistorialMantenimientoEquipos extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VistaHistorialMantenimientoEquipos.class.getName());
    private MantenimientoEquipoOficinaDAO mantenimientoDAO;
    private List<MantenimientoEquipoOficina> todosLosMantenimientos = new ArrayList<>();
    private EquipoOficinaDAO equipoDAO;

    /**
     * Creates new form Vista
     */
    public VistaHistorialMantenimientoEquipos() {
        initComponents();
        EstiloHirata.aplicarVentanaOscura(this, getTitle(), getWidth() > 0 ? getWidth() : 1000, getHeight() > 0 ? getHeight() : 650);

        this.setSize(1320, 760);

        // Evita que el usuario cambie el tamaño de la ventana
        this.setResizable(false);

        // Centra la ventana en la pantalla
        this.setLocationRelativeTo(null);

        this.setTitle("Equipos");

        // Inicializar DAOs
        mantenimientoDAO = new MantenimientoEquipoOficinaDAO();
        equipoDAO = new EquipoOficinaDAO();

        lblSinRegistros.setText("No hay mantenimientos registrados");
        lblSinRegistros.setForeground(Color.WHITE);
        lblSinRegistros.setHorizontalAlignment(SwingConstants.CENTER);
        lblSinRegistros.setVisible(false);

        // Cargar datos iniciales
        cargarTabla();
        cargarTablaEquipos();
    }

    private void cargarTabla() {

        MantenimientoEquipoOficinaDAO dao = new MantenimientoEquipoOficinaDAO();
        try {
            todosLosMantenimientos = dao.listarTodos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage());
            return;
        }
        poblarTabla(todosLosMantenimientos);
    }

       private void poblarTabla(List<MantenimientoEquipoOficina> lista) {
        if (lista.isEmpty()) {
            tablaHistorial.setVisible(false);
            jScrollPane1.setVisible(false);
            lblSinRegistros.setVisible(true);
            return;
        }

        tablaHistorial.setVisible(true);
        jScrollPane1.setVisible(true);
        lblSinRegistros.setVisible(false);

        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.setColumnIdentifiers(new String[]{
            "ID", "Tipo Equipo", "Marca", "Modelo", "N° Serie",
            "Tipo Mantención", "Estado", "Técnico", "Fecha Registro", "Piezas Revisadas"
        });

        for (MantenimientoEquipoOficina m : lista) {
            modelo.addRow(new Object[]{
                m.getIdMantenimientoEquipo(),
                m.getEquipo().getTipoEquipo().getNombre(),
                m.getEquipo().getMarca(),
                m.getEquipo().getModelo(),
                m.getEquipo().getNumeroSerie(),
                m.getTipoMantenimiento(),
                m.getEstadoResultado(),
                m.getTecnico() != null ? m.getTecnico().getNombreUsuario() : "Sin asignar",
                m.getFechaRegistro(),
                m.getPiezasRevisadas() != null ? m.getPiezasRevisadas() : "—"
            });
        }

        tablaHistorial.setModel(modelo);

        // Ajuste de ancho de columnas para tablaHistorial.
        tablaHistorial.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int anchoTotal = 0;

        for (int col = 0; col < tablaHistorial.getColumnCount(); col++) {
            int maxAncho = 0;
            TableColumn columna = tablaHistorial.getColumnModel().getColumn(col);

            TableCellRenderer headerRenderer = tablaHistorial.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    tablaHistorial, columna.getHeaderValue(), false, false, 0, col
            );
            maxAncho = headerComp.getPreferredSize().width;

            for (int fila = 0; fila < tablaHistorial.getRowCount(); fila++) {
                TableCellRenderer renderer = tablaHistorial.getCellRenderer(fila, col);
                Component comp = tablaHistorial.prepareRenderer(renderer, fila, col);
                maxAncho = Math.max(maxAncho, comp.getPreferredSize().width);
            }

            columna.setPreferredWidth(maxAncho + 25);
            anchoTotal += maxAncho + 25;
        }

        int anchoVisible = tablaHistorial.getParent().getWidth();
        if (anchoTotal < anchoVisible) {
            tablaHistorial.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        }
    }

    private void cargarTablaEquipos() {
        EquipoOficinaDAO dao = new EquipoOficinaDAO();
        List<EquipoOficina> equipos = dao.listarTodo();

        if (equipos.isEmpty()) {
            tablaEquipos.setVisible(false);
            return;
        } else {
            tablaEquipos.setVisible(true);
        }

        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.setColumnIdentifiers(new String[]{
            "ID", "Tipo Equipo", "Marca", "Modelo", "N° Serie",
            "Estado", "Responsable", "Fecha Adquisición", "Fecha Registro"
        });

        for (EquipoOficina e : equipos) {
            modelo.addRow(new Object[]{
                e.getIdEquipo(),
                e.getTipoEquipo().getNombre(),
                e.getMarca(),
                e.getModelo(),
                e.getNumeroSerie(),
                e.getEstado(),
                e.getNombreResponsable() != null ? e.getNombreResponsable() : "Sin asignar",
                e.getFechaAdquisicion() != null ? e.getFechaAdquisicion() : "—",
                e.getFechaRegistro()
            });
        }

        tablaEquipos.setModel(modelo);

        // Ajuste de ancho de columnas
        tablaEquipos.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int anchoTotal = 0;
        for (int col = 0; col < tablaEquipos.getColumnCount(); col++) {
            int maxAncho = 0;
            TableColumn columna = tablaEquipos.getColumnModel().getColumn(col);

            TableCellRenderer headerRenderer = tablaEquipos.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    tablaEquipos, columna.getHeaderValue(), false, false, 0, col);
            maxAncho = headerComp.getPreferredSize().width;

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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        panelHistorial = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        btnLogout = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaHistorial = new javax.swing.JTable();
        lblSinRegistros = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtBuscarSerie = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnDetalle = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnExit = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaEquipos = new javax.swing.JTable();
        btnEditar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));
        jTabbedPane1.setMinimumSize(new java.awt.Dimension(1300, 315));

        panelHistorial.setBackground(new java.awt.Color(255, 255, 255));
        panelHistorial.setMinimumSize(new java.awt.Dimension(1300, 280));
        panelHistorial.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitulo.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblTitulo.setText(" HISTORIAL MANTENIMIENTO EQUIPOS ");
        panelHistorial.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 20, -1, -1));

        btnLogout.setText("CERRAR SESIÓN");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });
        panelHistorial.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(1119, 20, 150, -1));

        tablaHistorial.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tablaHistorial);

        panelHistorial.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 180, 1230, 250));

        lblSinRegistros.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        panelHistorial.add(lblSinRegistros, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 330, 700, 30));

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 17)); // NOI18N
        jLabel1.setText("N° Serie");
        panelHistorial.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 120, 70, 20));
        panelHistorial.add(txtBuscarSerie, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 120, 280, -1));

        btnBuscar.setText("BUSCAR");
        btnBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarActionPerformed(evt);
            }
        });
        panelHistorial.add(btnBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 120, -1, -1));

        btnLimpiar.setText("LIMPIAR");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        panelHistorial.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 120, -1, -1));

        btnDetalle.setText("VER DETALLE");
        btnDetalle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDetalleActionPerformed(evt);
            }
        });
        panelHistorial.add(btnDetalle, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 120, 140, -1));

        jTabbedPane1.addTab("Historial", panelHistorial);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        jLabel4.setText("EQUIPOS");

        btnExit.setText("CERRAR SESIÓN");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

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
        jScrollPane2.setViewportView(tablaEquipos);

        btnEditar.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        btnEditar.setText("EDITAR");
        btnEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditarActionPerformed(evt);
            }
        });

        btnAgregar.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        btnAgregar.setText("AGREGAR");
        btnAgregar.setAlignmentY(0.3F);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        btnEliminar.setText("ELIMINAR");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel4)
                        .addGap(370, 370, 370)
                        .addComponent(btnExit, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(90, 90, 90)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1097, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(370, 370, 370)
                            .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(29, 29, 29)
                            .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(29, 29, 29)
                            .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(113, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnExit)
                    .addComponent(jLabel4))
                .addGap(61, 61, 61)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAgregar)
                    .addComponent(btnEditar)
                    .addComponent(btnEliminar))
                .addContainerGap(151, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Equipos", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExitActionPerformed
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
    }//GEN-LAST:event_btnExitActionPerformed

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

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        // TODO add your handling code here:
        DialogNuevoEquipo dialog = new DialogNuevoEquipo(this, true);
        dialog.setVisible(true);
        if (dialog.isConfirmado()) {
            cargarTablaEquipos();
        }
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        int filaSw = tablaEquipos.getSelectedRow();
        if (filaSw == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un equipo primero.");
            return;
        }

        int idEq = (int) tablaEquipos.getValueAt(filaSw, 0);

        EquipoOficina equipo = equipoDAO.buscarEquipo(idEq);

        if (equipo == null) {
            JOptionPane.showMessageDialog(this, "No se pudo obtener el equipo.");
            return;
        }

        DialogEditarEquipo dialog = new DialogEditarEquipo(this, true, equipo);
        dialog.setVisible(true);

        if (dialog.isConfirmado()) {
            cargarTablaEquipos();
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        int filaSeleccionada = tablaEquipos.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this,
                    "Debe seleccionar un equipo para eliminar.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) tablaEquipos.getValueAt(filaSeleccionada, 0);
        String marca = (String) tablaEquipos.getValueAt(filaSeleccionada, 2);
        String modelo = (String) tablaEquipos.getValueAt(filaSeleccionada, 3);

        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "<html>¿Está seguro que desea eliminar <b>" + marca + " " + modelo + "</b>?</html>",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        boolean ok = equipoDAO.eliminar(id);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Equipo eliminado correctamente.");
            cargarTablaEquipos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar.", "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarActionPerformed
        // TODO add your handling code here:
        String texto = txtBuscarSerie.getText().trim().toLowerCase();

        if (texto.isEmpty()) {
            poblarTabla(todosLosMantenimientos);
            return;
        }

        List<MantenimientoEquipoOficina> filtrados = todosLosMantenimientos.stream()
                .filter(m -> m.getEquipo().getNumeroSerie().toLowerCase().contains(texto))
                .collect(java.util.stream.Collectors.toList());

        poblarTabla(filtrados);

        if (filtrados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron registros para ese N° de serie.");
        }
    }//GEN-LAST:event_btnBuscarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        txtBuscarSerie.setText("");
        poblarTabla(todosLosMantenimientos);
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnDetalleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDetalleActionPerformed
        // TODO add your handling code here:
          int filaHist = tablaHistorial.getSelectedRow();
        if (filaHist == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un registro primero.");
            return;
        }

        int idHist = (int) tablaHistorial.getValueAt(filaHist, 0);

        try {
            MantenimientoEquipoOficina mant = mantenimientoDAO.obtenerDetallePorId(idHist);

            if (mant == null) {
                JOptionPane.showMessageDialog(this, "No se pudo obtener el registro.");
                return;
            }

            DialogDetalleMantenimiento dialog = new DialogDetalleMantenimiento(this, mant);
            dialog.setVisible(true);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al obtener detalle: " + e.getMessage());
        }

    }//GEN-LAST:event_btnDetalleActionPerformed

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
        java.awt.EventQueue.invokeLater(() -> new VistaHistorialMantenimientoEquipos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnDetalle;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnLogout;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblSinRegistros;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel panelHistorial;
    private javax.swing.JTable tablaEquipos;
    private javax.swing.JTable tablaHistorial;
    private javax.swing.JTextField txtBuscarSerie;
    // End of variables declaration//GEN-END:variables
}
