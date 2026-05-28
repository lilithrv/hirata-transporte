/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import dao.EquipoOficinaDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.EquipoOficina;
/**
 *
 * @author 
 */
public class VistaListaEquipos extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VistaListaEquipos.class.getName());

    /**
     * Creates new form VistaListaEquipos
     */
    public VistaListaEquipos() {
        
        initComponents();
        EstiloHirata.aplicarVentana(this, getTitle(), getWidth() > 0 ? getWidth() : 1000, getHeight() > 0 ? getHeight() : 650);

        this.setSize(900, 700);

        // Evita que el usuario cambie el tamaño de la ventana
        this.setResizable(false);

        // Centra la ventana en la pantalla
        this.setLocationRelativeTo(null);

        this.setTitle("Inventario Central");

        
        llenarComboEstados();
        llenarComboTipos();
        refrescarTabla();
        cargarDatosTabla();

        
        
    } // Vista Equipos
    
    // Este método lo escribes tú manualmente
    public void llenarComboEstados() {
        // 1. Instanciamos el DAO (Asegúrate de tener el import dao.EquipoOficinaDAO;)
        dao.EquipoOficinaDAO dao = new dao.EquipoOficinaDAO();

        // 2. Traemos la lista de la BD
        java.util.List<String> listaEstados = dao.obtenerEstadosUnicos();

        // 3. Limpiamos y llenamos
        cmbEstados.removeAllItems();
        cmbEstados.addItem("Todos"); // Opción inicial

        for (String estado : listaEstados) {
            cmbEstados.addItem(estado);
        }
    } // Llenar combox
    
    public void cargarDatosTabla() {
        String[] titulos = {"ID", "Tipo", "Marca", "Modelo", "Serie", "Estado", "Responsable", "Adquisición"};
        DefaultTableModel modelo = new DefaultTableModel(null, titulos);

        EquipoOficinaDAO dao = new EquipoOficinaDAO();
        List<EquipoOficina> lista = dao.listarTodo();

        for (EquipoOficina eq : lista) {
            Object[] fila = new Object[8];
            fila[0] = eq.getIdEquipo();
            fila[1] = eq.getTipoEquipo().getNombre();
            fila[2] = eq.getMarca();
            fila[3] = eq.getModelo();
            fila[4] = eq.getNumeroSerie();
            fila[5] = eq.getEstado();
            fila[6] = eq.getNombreResponsable();
            fila[7] = eq.getFechaAdquisicion();

            modelo.addRow(fila);
        }
        tblTabla.setModel(modelo);

        tblTabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        // Obtenemos el modelo de columnas de la tabla
        javax.swing.table.TableColumnModel columnModel = tblTabla.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(40);  // ID: Pequeño
        columnModel.getColumn(0).setMaxWidth(60);       // Evitamos que crezca demasiado
        columnModel.getColumn(1).setPreferredWidth(100); // Tipo
        columnModel.getColumn(2).setPreferredWidth(80);  // Marca
        columnModel.getColumn(3).setPreferredWidth(120); // Modelo
        columnModel.getColumn(4).setPreferredWidth(130); // Serie
        columnModel.getColumn(5).setPreferredWidth(100); // Estado
        columnModel.getColumn(6).setPreferredWidth(180); // Responsable
        columnModel.getColumn(7).setPreferredWidth(120); // Adquisición
    }

    public void refrescarTabla() {
        // Definir los títulos de la tabla
        String[] titulos = {"ID", "Tipo", "Marca", "Modelo", "Serie", "Estado", "Responsable", "Adquisición"};
        DefaultTableModel modelo = new DefaultTableModel(null, titulos) {
            // Hacemos que las celdas no sean editables al hacer doble clic
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Capturar lo que el usuario seleccionó en los dos combos
        // Usamos una validación simple por si el combo de tipos aún no tiene datos
        String estado = cmbEstados.getSelectedItem() != null ? cmbEstados.getSelectedItem().toString() : "Todos";
        String tipo = cmbTipo.getSelectedItem() != null ? cmbTipo.getSelectedItem().toString() : "Todos";

        // Consultar al DAO con ambos filtros
        EquipoOficinaDAO dao = new EquipoOficinaDAO();
        List<EquipoOficina> lista = dao.listarFiltrado(estado, tipo);

        //  Llenar el modelo con los resultados
        for (EquipoOficina eq : lista) {
            Object[] fila = new Object[8];
            fila[0] = eq.getIdEquipo();
            fila[1] = (eq.getTipoEquipo() != null) ? eq.getTipoEquipo().getNombre() : "N/A";
            fila[2] = eq.getMarca();
            fila[3] = eq.getModelo();
            fila[4] = eq.getNumeroSerie();
            fila[5] = eq.getEstado();
            fila[6] = eq.getNombreResponsable();
            fila[7] = eq.getFechaAdquisicion();
            modelo.addRow(fila);
        }

        // Aplicar el modelo a la tabla
        tblTabla.setModel(modelo);

        // RE-APLICAR DISEÑO 
        tblTabla.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        javax.swing.table.TableColumnModel columnModel = tblTabla.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(40);  // ID
        columnModel.getColumn(1).setPreferredWidth(100); // Tipo
        columnModel.getColumn(2).setPreferredWidth(100); // Marca
        columnModel.getColumn(3).setPreferredWidth(120); // Modelo
        columnModel.getColumn(4).setPreferredWidth(130); // Serie
        columnModel.getColumn(5).setPreferredWidth(100); // Estado
        columnModel.getColumn(6).setPreferredWidth(180); // Responsable
        columnModel.getColumn(7).setPreferredWidth(120); // Adquisición
    }
    
    public void llenarComboTipos() {
        cmbTipo.removeAllItems();
        cmbTipo.addItem("Todos"); // Opción para quitar el filtro

        // Necesitas un método en tu DAO que liste los tipos o usar una consulta rápida aquí
        try (Connection cn = conn.Conexion.getInstancia(); PreparedStatement ps = cn.prepareStatement("SELECT nombre FROM tipos_equipo"); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                cmbTipo.addItem(rs.getString("nombre"));
            }
        } catch (SQLException e) {
            System.err.println("Error al llenar combo tipos: " + e.getMessage());
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

        pnlPrincipal = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        pnlFiltro = new javax.swing.JPanel();
        lblFiltroPorEstado = new javax.swing.JLabel();
        cmbEstados = new javax.swing.JComboBox<>();
        lblFiltrarTipo = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox<>();
        pnlTabla = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTabla = new javax.swing.JTable();
        btnGestionarE = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlPrincipal.setBackground(new java.awt.Color(255, 255, 255));

        lblTitulo.setFont(new java.awt.Font("Noto Sans", 1, 24)); // NOI18N
        lblTitulo.setText("Mantenimiento de Equipos");

        pnlFiltro.setBackground(new java.awt.Color(255, 255, 255));
        pnlFiltro.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Filtros de Búsqueda", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Noto Sans", 1, 12))); // NOI18N

        lblFiltroPorEstado.setText("Filtrar por Estado :");

        cmbEstados.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEstados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstadosActionPerformed(evt);
            }
        });

        lblFiltrarTipo.setText("Filtrar por Tipo :");

        cmbTipo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlFiltroLayout = new javax.swing.GroupLayout(pnlFiltro);
        pnlFiltro.setLayout(pnlFiltroLayout);
        pnlFiltroLayout.setHorizontalGroup(
            pnlFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltroLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblFiltroPorEstado)
                    .addComponent(lblFiltrarTipo))
                .addGap(29, 29, 29)
                .addGroup(pnlFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cmbEstados, 0, 154, Short.MAX_VALUE)
                    .addComponent(cmbTipo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(111, Short.MAX_VALUE))
        );
        pnlFiltroLayout.setVerticalGroup(
            pnlFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlFiltroLayout.createSequentialGroup()
                .addGroup(pnlFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlFiltroLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblFiltroPorEstado))
                    .addComponent(cmbEstados, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblFiltrarTipo)
                    .addComponent(cmbTipo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        pnlTabla.setBackground(new java.awt.Color(255, 255, 255));
        pnlTabla.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Listado de Equipos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Noto Sans", 1, 12))); // NOI18N

        jScrollPane1.setBackground(new java.awt.Color(255, 255, 255));

        tblTabla.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane1.setViewportView(tblTabla);
        if (tblTabla.getColumnModel().getColumnCount() > 0) {
            tblTabla.getColumnModel().getColumn(0).setPreferredWidth(40);
        }

        javax.swing.GroupLayout pnlTablaLayout = new javax.swing.GroupLayout(pnlTabla);
        pnlTabla.setLayout(pnlTablaLayout);
        pnlTablaLayout.setHorizontalGroup(
            pnlTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTablaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 771, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnlTablaLayout.setVerticalGroup(
            pnlTablaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTablaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnGestionarE.setText("Gestionar Equipo");
        btnGestionarE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGestionarEActionPerformed(evt);
            }
        });

        btnLogout.setText("CERRAR SESIÓN");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlPrincipalLayout = new javax.swing.GroupLayout(pnlPrincipal);
        pnlPrincipal.setLayout(pnlPrincipalLayout);
        pnlPrincipalLayout.setHorizontalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGestionarE)
                    .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnlFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(pnlTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(502, Short.MAX_VALUE))
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(lblTitulo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnLogout)
                .addGap(352, 352, 352))
        );
        pnlPrincipalLayout.setVerticalGroup(
            pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPrincipalLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pnlPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitulo)
                    .addComponent(btnLogout))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(pnlTabla, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnGestionarE)
                .addGap(70, 70, 70))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbEstadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEstadosActionPerformed
        // TODO add your handling code here:
        
        refrescarTabla();
    }//GEN-LAST:event_cmbEstadosActionPerformed

    private void cmbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipoActionPerformed
        // TODO add your handling code here:
        
        refrescarTabla();
    }//GEN-LAST:event_cmbTipoActionPerformed

    private void btnGestionarEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGestionarEActionPerformed
        // TODO add your handling code here:
        
        int fila = tblTabla.getSelectedRow();

        if (fila == -1) {
            javax.swing.JOptionPane.showMessageDialog(this, "Por favor, seleccione un equipo de la lista.");
            return;
        }

        // Capturamos el ID y el Nombre del Tipo
        int idEquipo = (int) tblTabla.getValueAt(fila, 0);
        String tipo = tblTabla.getValueAt(fila, 1).toString();

        // Redirección inteligente
        switch (tipo) {
            case "Notebook":
                VistaMantenimientoNotebook ventanaNB = new VistaMantenimientoNotebook(idEquipo, this);
                ventanaNB.setVisible(true);
                
                this.setVisible(false);
                break;

            case "PC Escritorio":
                VistaMantenimientoPC ventanaPC = new VistaMantenimientoPC(idEquipo, this); 
                ventanaPC.setVisible(true);

                this.setVisible(false); 
                break;

            case "Celular":
                VistaMantenimientoCelular ventanaCelular = new VistaMantenimientoCelular(idEquipo, this); 
                ventanaCelular.setVisible(true);

                this.setVisible(false); 
                break;

            case "Impresora":
                VistaMantenimientoImpresora ventanaImp = new VistaMantenimientoImpresora(idEquipo, this);
                ventanaImp.setVisible(true);
                
                this.setVisible(false); 
                break;

            default:
                javax.swing.JOptionPane.showMessageDialog(this, "El tipo '" + tipo + "' aún no tiene un checklist configurado.");
                break;
        }
    }//GEN-LAST:event_btnGestionarEActionPerformed

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new VistaListaEquipos().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGestionarE;
    private javax.swing.JButton btnLogout;
    private javax.swing.JComboBox<String> cmbEstados;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblFiltrarTipo;
    private javax.swing.JLabel lblFiltroPorEstado;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlFiltro;
    private javax.swing.JPanel pnlPrincipal;
    private javax.swing.JPanel pnlTabla;
    private javax.swing.JTable tblTabla;
    // End of variables declaration//GEN-END:variables
}
