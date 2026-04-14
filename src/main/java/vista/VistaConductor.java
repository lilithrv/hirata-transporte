/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import dao.KilometrajeDAO;
import dao.MantenimientoDAO;
import dao.VehiculoDAO;
import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import modelo.Kilometraje;
import modelo.Mantenimiento;
import modelo.Usuario;
import modelo.Vehiculo;
import util.Sesion;

/**
 *
 * @author gustavo
 */
public class VistaConductor extends javax.swing.JFrame {

    private VehiculoDAO vehiculoDAO = new VehiculoDAO();
    private KilometrajeDAO kilometrajeDAO = new KilometrajeDAO();
    private MantenimientoDAO mantenimientoDAO = new MantenimientoDAO();

    private Vehiculo vehiculo;
    private Kilometraje ultimo;

    /**
     * Creates new form VistaConductor
     */
    public VistaConductor() {
        initComponents();

        //campos no editables
        bloquearCampos();

        this.setSize(1000, 650);

        // Evita que el usuario cambie el tamaño de la ventana
        this.setResizable(false);

        // Centra la ventana en la pantalla
        this.setLocationRelativeTo(null);

        this.setTitle("Conductor Designado");

        //cargar datos conductor
        cargarDatos();
        cargarTablaViajes();

        // Verificar si ya tiene mantenimiento programado
        if (mantenimientoDAO.tieneMantenimientoProgramado(vehiculo.getIdVehiculo())) {
            bloquearFormulario();
        }
    }

    private void bloquearFormulario() {
        txtNuevoKm.setEnabled(false);
        txtOrigen.setEnabled(false);
        txtDestino.setEnabled(false);
        btnEnviar.setEnabled(false);
        lblMensaje.setText("Vehículo con mantenimiento programado");
        lblMensaje.setForeground(java.awt.Color.RED);
        lblMensaje.setVisible(true);
    }

    private void cargarTablaViajes() {
        KilometrajeDAO dao = new KilometrajeDAO();
        Usuario conductorLogueado = Sesion.getUsuarioActivo();
        List<Kilometraje> viajes = dao.historialPorConductor(conductorLogueado.getIdUsuario());

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.setColumnIdentifiers(new String[]{
            "Patente", "Marca", "Modelo", "Origen", "Destino", "Km", "Fecha"
        });

        if (viajes.isEmpty()) {
            jScrollPane1.setVisible(false);      // ocultar tabla
            lblSinViajes.setText("No tienes viajes registrados aún");
            lblSinViajes.setVisible(true);
            return;
        }

        // Si hay datos
        lblSinViajes.setVisible(false);            // ocultar label
        jScrollPane1.setVisible(true);            // mostrar tabla
        for (Kilometraje k : viajes) {
            modelo.addRow(new Object[]{
                k.getVehiculo().getPatente(),
                k.getVehiculo().getMarca(),
                k.getVehiculo().getModelo(),
                k.getDireccionOrigen(),
                k.getDireccionTermino(),
                k.getKilometros(),
                k.getFechaRegistro()
            });
        }

        tablaConductor.setModel(modelo);

        // Ajusta cada columna al contenido más ancho
        tablaConductor.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int col = 0; col < tablaConductor.getColumnCount(); col++) {
            int maxAncho = 0;

            // Medir el header
            TableColumn columna = tablaConductor.getColumnModel().getColumn(col);
            TableCellRenderer headerRenderer = tablaConductor.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    tablaConductor, columna.getHeaderValue(), false, false, 0, col);
            maxAncho = headerComp.getPreferredSize().width;

            // Medir cada celda de esa columna
            for (int fila = 0; fila < tablaConductor.getRowCount(); fila++) {
                TableCellRenderer renderer = tablaConductor.getCellRenderer(fila, col);
                Component comp = tablaConductor.prepareRenderer(renderer, fila, col);
                maxAncho = Math.max(maxAncho, comp.getPreferredSize().width);
            }

            columna.setPreferredWidth(maxAncho + 10);
        }
    }

    private void bloquearCampos() {
        JTextField[] campos = {txtAnio, txtMarca, txtModelo, txtPatente, txtAnio, txtKilometroI, txtKmUltimoRegistrado, txtFechaUltimoKm};
        for (JTextField campo : campos) {
            campo.setEditable(false);
            campo.setFocusable(false);
        }
    }

    private void cargarDatos() {
        Usuario conductorLogueado = Sesion.getUsuarioActivo();
        vehiculo = vehiculoDAO.obtenerVehiculoPorConductor(conductorLogueado.getIdUsuario());

        if (vehiculo != null) {
            ultimo = kilometrajeDAO.ultimoKmPorVehiculo(vehiculo.getIdVehiculo());

            txtAnio.setText(vehiculo.getPatente());
            txtMarca.setText(vehiculo.getMarca());
            txtModelo.setText(vehiculo.getModelo());
            txtPatente.setText(vehiculo.getPatente());
            txtAnio.setText(String.valueOf(vehiculo.getAnio()));
            txtKilometroI.setText(String.valueOf(vehiculo.getKilometrajeInicial()));

            txtKmUltimoRegistrado.setText(ultimo != null ? String.valueOf(ultimo.getKilometros()) : "Sin registro");
            txtFechaUltimoKm.setText(ultimo != null ? ultimo.getFechaRegistro().toString() : "Sin registro");

        } else {
            // Si el conductor no tiene vehículo asignado
            txtAnio.setText("Sin vehículo asignado");
            txtMarca.setText("-");
            txtModelo.setText("-");
            txtPatente.setText("-");
            txtAnio.setText("-");
            txtKilometroI.setText("-");
            txtKmUltimoRegistrado.setText("-");
            txtFechaUltimoKm.setText("-");
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

        lblEstado = new javax.swing.JPanel();
        lblInfo = new javax.swing.JLabel();
        txtMarca = new javax.swing.JTextField();
        lblMarca = new javax.swing.JLabel();
        lblModelo = new javax.swing.JLabel();
        lblAnio = new javax.swing.JLabel();
        txtModelo = new javax.swing.JTextField();
        txtAnio = new javax.swing.JTextField();
        lblKmUltimoRegistro = new javax.swing.JLabel();
        txtKilometroI = new javax.swing.JTextField();
        txtFechaUltimoKm = new javax.swing.JTextField();
        lblKilometroF = new javax.swing.JLabel();
        btnEnviar = new javax.swing.JButton();
        lblKilometroI1 = new javax.swing.JLabel();
        lblKmUltimoRegistro1 = new javax.swing.JLabel();
        txtKmUltimoRegistrado = new javax.swing.JTextField();
        lblPatente = new javax.swing.JLabel();
        txtPatente = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaConductor = new javax.swing.JTable();
        lblSinViajes = new javax.swing.JLabel();
        lblOrigen = new javax.swing.JLabel();
        lblDestino = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtOrigen = new javax.swing.JTextField();
        txtDestino = new javax.swing.JTextField();
        txtNuevoKm = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        lblMensaje = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1000, 650));

        lblEstado.setBackground(new java.awt.Color(255, 255, 255));
        lblEstado.setForeground(new java.awt.Color(255, 255, 255));
        lblEstado.setMaximumSize(new java.awt.Dimension(600, 450));
        lblEstado.setMinimumSize(new java.awt.Dimension(600, 450));
        lblEstado.setPreferredSize(new java.awt.Dimension(600, 450));
        lblEstado.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblInfo.setFont(new java.awt.Font("PT Serif", 1, 24)); // NOI18N
        lblInfo.setText("Información del Conductor");
        lblEstado.add(lblInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 40, -1, -1));

        txtMarca.setMaximumSize(new java.awt.Dimension(80, 23));
        txtMarca.setPreferredSize(new java.awt.Dimension(220, 23));
        lblEstado.add(txtMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 340, -1, -1));

        lblMarca.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblMarca.setText("Marca");
        lblEstado.add(lblMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, -1, -1));

        lblModelo.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblModelo.setText("Modelo");
        lblEstado.add(lblModelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, -1, -1));

        lblAnio.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblAnio.setText("Año");
        lblEstado.add(lblAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 420, -1, -1));

        txtModelo.setMaximumSize(new java.awt.Dimension(80, 23));
        txtModelo.setPreferredSize(new java.awt.Dimension(220, 23));
        lblEstado.add(txtModelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 380, -1, -1));

        txtAnio.setMaximumSize(new java.awt.Dimension(80, 23));
        txtAnio.setPreferredSize(new java.awt.Dimension(220, 23));
        lblEstado.add(txtAnio, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 420, -1, -1));

        lblKmUltimoRegistro.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblKmUltimoRegistro.setText("Fecha último registro");
        lblEstado.add(lblKmUltimoRegistro, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 540, -1, -1));

        txtKilometroI.setMaximumSize(new java.awt.Dimension(80, 23));
        txtKilometroI.setPreferredSize(new java.awt.Dimension(220, 23));
        lblEstado.add(txtKilometroI, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 460, -1, -1));

        txtFechaUltimoKm.setMaximumSize(new java.awt.Dimension(80, 23));
        txtFechaUltimoKm.setPreferredSize(new java.awt.Dimension(220, 23));
        lblEstado.add(txtFechaUltimoKm, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 540, -1, -1));

        lblKilometroF.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblKilometroF.setText("KM Final");
        lblEstado.add(lblKilometroF, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 380, 60, 20));

        btnEnviar.setBackground(new java.awt.Color(0, 0, 0));
        btnEnviar.setFont(new java.awt.Font("Helvetica Neue", 1, 18)); // NOI18N
        btnEnviar.setForeground(new java.awt.Color(255, 255, 255));
        btnEnviar.setText("Enviar");
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });
        lblEstado.add(btnEnviar, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 450, 180, 40));

        lblKilometroI1.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblKilometroI1.setText("Kilómetro Inicial");
        lblEstado.add(lblKilometroI1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 460, -1, -1));

        lblKmUltimoRegistro1.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblKmUltimoRegistro1.setText("Último km registrado");
        lblEstado.add(lblKmUltimoRegistro1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, -1, -1));

        txtKmUltimoRegistrado.setMaximumSize(new java.awt.Dimension(80, 23));
        txtKmUltimoRegistrado.setPreferredSize(new java.awt.Dimension(220, 23));
        lblEstado.add(txtKmUltimoRegistrado, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 500, -1, -1));

        lblPatente.setFont(new java.awt.Font("Helvetica Neue", 1, 14)); // NOI18N
        lblPatente.setText("Patente");
        lblEstado.add(lblPatente, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, -1, -1));

        txtPatente.setMaximumSize(new java.awt.Dimension(80, 23));
        txtPatente.setPreferredSize(new java.awt.Dimension(220, 23));
        lblEstado.add(txtPatente, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 300, -1, -1));

        jButton1.setText("CERRAR SESIÓN");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        lblEstado.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(840, 10, -1, -1));

        tablaConductor.setModel(new javax.swing.table.DefaultTableModel(
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
        tablaConductor.setMaximumSize(new java.awt.Dimension(2147483630, 80));
        jScrollPane1.setViewportView(tablaConductor);

        lblEstado.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 880, 130));

        lblSinViajes.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        lblEstado.add(lblSinViajes, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 140, 730, 90));

        lblOrigen.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        lblOrigen.setText("Origen");
        lblEstado.add(lblOrigen, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 300, -1, -1));

        lblDestino.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        lblDestino.setText("Destino");
        lblEstado.add(lblDestino, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 340, -1, -1));

        jLabel3.setFont(new java.awt.Font("Liberation Sans", 1, 12)); // NOI18N
        jLabel3.setText("REGISTRAR RUTA");
        lblEstado.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 250, -1, -1));
        lblEstado.add(txtOrigen, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 300, 320, -1));
        lblEstado.add(txtDestino, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 340, 320, -1));
        lblEstado.add(txtNuevoKm, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 380, 320, -1));

        jLabel1.setFont(new java.awt.Font("Liberation Sans", 1, 12)); // NOI18N
        jLabel1.setText("VEHÍCULO ASIGNADO");
        lblEstado.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 250, -1, -1));

        lblMensaje.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        lblEstado.add(lblMensaje, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 510, 310, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 1000, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblEstado, javax.swing.GroupLayout.DEFAULT_SIZE, 650, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        // TODO add your handling code here:
        try {

            int kmNuevo = Integer.parseInt(txtNuevoKm.getText().trim());

            //  km de referencia 
            int kmUltimoRegistrado = (ultimo != null) ? ultimo.getKilometros() : vehiculo.getKilometrajeInicial();
            if (kmNuevo <= kmUltimoRegistrado) {
                JOptionPane.showMessageDialog(this,
                        "El km ingresado debe ser mayor al último registrado (" + kmUltimoRegistrado + " km).",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si tiene mantenimiento programado
            if (mantenimientoDAO.tieneMantenimientoProgramado(vehiculo.getIdVehiculo())) {
                JOptionPane.showMessageDialog(this,
                        "No puedes registrar km. El vehículo tiene un mantenimiento programado pendiente.",
                        "Mantenimiento Pendiente", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // nuevo kilometraje
            Kilometraje nuevoKm = new Kilometraje();
            nuevoKm.setConductor(Sesion.getUsuarioActivo());
            nuevoKm.setVehiculo(vehiculo);
            nuevoKm.setKilometros(kmNuevo);
            nuevoKm.setDireccionOrigen(txtOrigen.getText().trim());
            nuevoKm.setDireccionTermino(txtDestino.getText().trim());

            boolean insertado = kilometrajeDAO.insertarKilometraje(nuevoKm);

            if (insertado) {
                // PROGRAMAR MANTENIMIENTO Y ALERTA
                // referencia para el delta es el km del último mantenimiento, o el inicial si no hay
                Integer kmUltimoMant = mantenimientoDAO.obtenerKmUltimoMantenimiento(vehiculo.getIdVehiculo());
                int kmReferenciaMant = (kmUltimoMant != null) ? kmUltimoMant : vehiculo.getKilometrajeInicial();

                int diferencia = kmNuevo - kmReferenciaMant;

                if (diferencia >= 5000) {
                    // Insertar mantenimiento
                    Mantenimiento mant = new Mantenimiento();
                    mant.setVehiculo(vehiculo);
                    mant.setKilometraje(kmNuevo);
                    mantenimientoDAO.programar(mant);

                    JOptionPane.showMessageDialog(this,
                            "Km registrado. Se ha programado un mantenimiento preventivo automáticamente.",
                            "Mantenimiento Programado", JOptionPane.WARNING_MESSAGE);

                } else {
                    JOptionPane.showMessageDialog(this,
                            "Km registrado correctamente.",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
                
                bloquearFormulario();

                //  Refrescar datos 
                cargarDatos();
                cargarTablaViajes();
                txtNuevoKm.setText("");
                txtOrigen.setText("");
                txtDestino.setText("");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Ingresa un número válido.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
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
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(VistaConductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(VistaConductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(VistaConductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(VistaConductor.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new VistaConductor().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAnio;
    private javax.swing.JLabel lblDestino;
    private javax.swing.JPanel lblEstado;
    private javax.swing.JLabel lblInfo;
    private javax.swing.JLabel lblKilometroF;
    private javax.swing.JLabel lblKilometroI1;
    private javax.swing.JLabel lblKmUltimoRegistro;
    private javax.swing.JLabel lblKmUltimoRegistro1;
    private javax.swing.JLabel lblMarca;
    private javax.swing.JLabel lblMensaje;
    private javax.swing.JLabel lblModelo;
    private javax.swing.JLabel lblOrigen;
    private javax.swing.JLabel lblPatente;
    private javax.swing.JLabel lblSinViajes;
    private javax.swing.JTable tablaConductor;
    private javax.swing.JTextField txtAnio;
    private javax.swing.JTextField txtDestino;
    private javax.swing.JTextField txtFechaUltimoKm;
    private javax.swing.JTextField txtKilometroI;
    private javax.swing.JTextField txtKmUltimoRegistrado;
    private javax.swing.JTextField txtMarca;
    private javax.swing.JTextField txtModelo;
    private javax.swing.JTextField txtNuevoKm;
    private javax.swing.JTextField txtOrigen;
    private javax.swing.JTextField txtPatente;
    // End of variables declaration//GEN-END:variables
}
