/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import dao.MantenimientoEquipoOficinaDAO;
import java.awt.Component;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import modelo.MantenimientoEquipoOficina;

/**
 *
 * @author leslie
 */
public class VistaHistorialMantenimientoEquipo extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VistaHistorialMantenimientoEquipo.class.getName());
    private MantenimientoEquipoOficinaDAO mantenimientoDAO;

    /**
     * Creates new form VistaHistorialMantenimientoEquipo
     */
    public VistaHistorialMantenimientoEquipo() {
        initComponents();

        this.setSize(1260, 750);

        // Evita que el usuario cambie el tamaño de la ventana
        this.setResizable(false);

        // Centra la ventana en la pantalla
        this.setLocationRelativeTo(null);

        this.setTitle("Historial Mantenimiento Equipos");

        // Inicializar DAOs
        mantenimientoDAO = new MantenimientoEquipoOficinaDAO();

        lblSinRegistros.setText("No hay mantenimientos registrados");
        lblSinRegistros.setHorizontalAlignment(SwingConstants.CENTER);
        lblSinRegistros.setVisible(false);

        // Cargar datos iniciales
        cargarTabla();
    }

    private void cargarTabla() {

        MantenimientoEquipoOficinaDAO dao = new MantenimientoEquipoOficinaDAO();
        List<MantenimientoEquipoOficina> mantenimientos;

        try {
            mantenimientos = dao.listarTodos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar historial: " + e.getMessage());
            return;
        }

        if (mantenimientos.isEmpty()) {
            tablaHistorial.setVisible(false);
            jScrollPane1.setVisible(false);   // oculta también el scroll
            lblSinRegistros.setVisible(true);
            return;
        } else {
            tablaHistorial.setVisible(true);
            jScrollPane1.setVisible(true);
            lblSinRegistros.setVisible(false);
        }

        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabla solo lectura
            }
        };

        modelo.setColumnIdentifiers(new String[]{
            "ID", "Tipo Equipo", "Marca", "Modelo", "N° Serie",
            "Tipo Mantención", "Estado", "Técnico", "Fecha Registro", "Piezas Revisadas"
        });

        for (MantenimientoEquipoOficina m : mantenimientos) {
            modelo.addRow(new Object[]{
                m.getIdMantenimientoEquipo(),
                m.getEquipo().getTipoEquipo().getNombre(), // anidado
                m.getEquipo().getMarca(), // anidado
                m.getEquipo().getModelo(), // anidado
                m.getEquipo().getNumeroSerie(), // anidado
                m.getTipoMantenimiento(),
                m.getEstadoResultado(),
                m.getTecnico() != null ? m.getTecnico().getNombreUsuario() : "Sin asignar",
                m.getFechaRegistro(),
                m.getPiezasRevisadas() != null ? m.getPiezasRevisadas() : "—"
            });
        }

        tablaHistorial.setModel(modelo);

        // Ajusta cada columna al contenido más ancho
        tablaHistorial.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int anchoTotal = 0;

        for (int col = 0; col < tablaHistorial.getColumnCount(); col++) {
            int maxAncho = 0;

            TableColumn columna = tablaHistorial.getColumnModel().getColumn(col);
            TableCellRenderer headerRenderer = tablaHistorial.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    tablaHistorial, columna.getHeaderValue(), false, false, 0, col);
            maxAncho = headerComp.getPreferredSize().width;

            for (int fila = 0; fila < tablaHistorial.getRowCount(); fila++) {
                TableCellRenderer renderer = tablaHistorial.getCellRenderer(fila, col);
                Component comp = tablaHistorial.prepareRenderer(renderer, fila, col);
                maxAncho = Math.max(maxAncho, comp.getPreferredSize().width);
            }

            columna.setPreferredWidth(maxAncho + 20);
            anchoTotal += maxAncho + 20;
        }

        int anchoVisible = tablaHistorial.getParent().getWidth();
        if (anchoTotal < anchoVisible) {
            tablaHistorial.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
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

        jPanel1 = new javax.swing.JPanel();
        lblMantenimiento = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaHistorial = new javax.swing.JTable();
        lblSinRegistros = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblMantenimiento.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblMantenimiento.setText("MENÚ MANTENIMIENTO EQUIPOS");
        jPanel1.add(lblMantenimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(449, 32, 341, -1));

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

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 70, 1189, 159));

        lblSinRegistros.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        jPanel1.add(lblSinRegistros, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 120, 340, 50));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        java.awt.EventQueue.invokeLater(() -> new VistaHistorialMantenimientoEquipo().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblMantenimiento;
    private javax.swing.JLabel lblSinRegistros;
    private javax.swing.JTable tablaHistorial;
    // End of variables declaration//GEN-END:variables
}
