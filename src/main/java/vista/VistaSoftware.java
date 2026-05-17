/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package vista;

import dao.EquipoOficinaDAO;
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
    private TipoSoftware vehiculoDAO;
    private HashMap<String, TipoEquipo> mapaTipos;
    private SoftwareDAO softwareDAO;
    private EquipoOficinaDAO equipoDAO;
    private TipoEquipoDAO tipoEquipoDAO;
    private EquipoOficina equipoSeleccionado;

    /**
     * Creates new form Vista
     */
    public VistaSoftware() {
        initComponents();
        this.setSize(915, 750);

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
        cargarTablaCatalogo(); // tab 2
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

    private void cargarTablaCatalogo() {
        SoftwareDAO dao = new SoftwareDAO();
        List<Software> lista = dao.listarTodos();

        DefaultTableModel modelo = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "Tipo", "Nombre", "Fabricante", "Descripción"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // tabla no editable directamente
            }
        };

        for (Software s : lista) {
            modelo.addRow(new Object[]{
                s.getIdSoftware(),
                s.getTipoSoftware().getNombre(),
                s.getNombre(),
                s.getFabricante(),
                s.getDescripcion()
            });
        }

        tablaCatalogo.setModel(modelo);
        // Ajuste de ancho según contenido
        tablaCatalogo.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int anchoTotal = 0;

        for (int col = 0; col < tablaCatalogo.getColumnCount(); col++) {
            int maxAncho = 0;

            TableColumn columna = tablaCatalogo.getColumnModel().getColumn(col);
            TableCellRenderer headerRenderer = tablaCatalogo.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(
                    tablaCatalogo, columna.getHeaderValue(), false, false, 0, col);
            maxAncho = headerComp.getPreferredSize().width;

            for (int fila = 0; fila < tablaCatalogo.getRowCount(); fila++) {
                TableCellRenderer renderer = tablaCatalogo.getCellRenderer(fila, col);
                Component comp = tablaCatalogo.prepareRenderer(renderer, fila, col);
                maxAncho = Math.max(maxAncho, comp.getPreferredSize().width);
            }

            columna.setPreferredWidth(maxAncho + 20);
            anchoTotal += maxAncho + 20;
        }

        int anchoVisible = tablaCatalogo.getParent().getWidth();
        if (anchoTotal < anchoVisible) {
            tablaCatalogo.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
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
        jPanel1 = new javax.swing.JPanel();
        lblTitulo = new javax.swing.JLabel();
        pnlFiltro = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox<>();
        btnBuscarTipo = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaEquipos = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        btnInstalar = new javax.swing.JButton();
        btnActualizar = new javax.swing.JButton();
        btnDesinstalar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablaSoftwareEquipo = new javax.swing.JTable();
        btnLogout = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaCatalogo = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        btnAgregar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnExit = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

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

        btnLogout.setText("CERRAR SESIÓN");
        btnLogout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogoutActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addComponent(pnlFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(42, 42, 42)
                                .addComponent(btnInstalar)
                                .addGap(39, 39, 39)
                                .addComponent(btnActualizar)
                                .addGap(29, 29, 29)
                                .addComponent(btnDesinstalar))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 497, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(163, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(lblTitulo)
                        .addGap(280, 280, 280))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnLogout)
                        .addContainerGap())))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnLogout)
                .addGap(5, 5, 5)
                .addComponent(lblTitulo)
                .addGap(27, 27, 27)
                .addComponent(pnlFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInstalar)
                    .addComponent(btnActualizar)
                    .addComponent(btnDesinstalar))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Gestión por equipo", jPanel1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        tablaCatalogo.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane3.setViewportView(tablaCatalogo);

        jLabel4.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        jLabel4.setText("CATÁLOGO");

        btnAgregar.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        btnAgregar.setText("AGREGAR");

        btnEditar.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        btnEditar.setText("EDITAR");

        btnEliminar.setFont(new java.awt.Font("Liberation Sans", 1, 15)); // NOI18N
        btnEliminar.setText("ELIMINAR");

        btnExit.setText("CERRAR SESIÓN");
        btnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExitActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 45, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnExit)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 842, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(234, 234, 234)
                        .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(71, 71, 71)
                        .addComponent(btnEliminar))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(389, 389, 389)
                        .addComponent(jLabel4)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnExit)
                .addGap(22, 22, 22)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEditar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregar, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(124, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Catálogo de Software", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
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
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscarTipo;
    private javax.swing.JButton btnDesinstalar;
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnExit;
    private javax.swing.JButton btnInstalar;
    private javax.swing.JButton btnLogout;
    private javax.swing.JComboBox<String> cmbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JPanel pnlFiltro;
    private javax.swing.JTable tablaCatalogo;
    private javax.swing.JTable tablaEquipos;
    private javax.swing.JTable tablaSoftwareEquipo;
    // End of variables declaration//GEN-END:variables
}
