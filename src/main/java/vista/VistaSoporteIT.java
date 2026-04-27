package vista;

import dao.EquipoOficinaDAO;
import dao.MantenimientoEquipoOficinaDAO;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import modelo.EquipoOficina;
import modelo.MantenimientoEquipoOficina;
import modelo.Usuario;
import util.Sesion;
import vista.VistaLogin;

public class VistaSoporteIT extends javax.swing.JFrame {

    private final Color fondoVentana = new Color(236, 240, 247);
    private final Color azulNoche = new Color(12, 24, 44);
    private final Color azulCard = new Color(18, 38, 69);
    private final Color azulPrimario = new Color(37, 99, 235);
    private final Color azulHover = new Color(29, 78, 216);
    private final Color grisTexto = new Color(71, 85, 105);
    private final Color bordeSuave = new Color(221, 226, 235);

    private int mouseX;
    private int mouseY;

    private final EquipoOficinaDAO equipoDAO = new EquipoOficinaDAO();
    private final MantenimientoEquipoOficinaDAO mantenimientoDAO = new MantenimientoEquipoOficinaDAO();

    public VistaSoporteIT() {
        setUndecorated(true);
        initComponents();
        configurarVentana();
        configurarEstilos();
        configurarEventosHover();
        cargarSesion();
        cargarEquipos();
    }

    private void configurarVentana() {
        setSize(1200, 740);
        setMinimumSize(new Dimension(1120, 680));
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(fondoVentana);
    }

    private void configurarEstilos() {
        panelRaiz.setBackground(fondoVentana);
        panelMenu.setBackground(azulNoche);
        panelSuperior.setBackground(azulNoche);
        panelFormulario.setBackground(Color.WHITE);
        panelResumen.setBackground(azulCard);
        panelAcciones.setBackground(Color.WHITE);

        lblLogo.setForeground(Color.WHITE);
        lblModulo.setForeground(new Color(186, 204, 232));
        lblUsuario.setForeground(new Color(226, 232, 240));
        lblTitulo.setForeground(new Color(15, 23, 42));
        lblSubtitulo.setForeground(grisTexto);
        lblIndicacion.setForeground(grisTexto);
        lblResumenTitulo.setForeground(Color.WHITE);
        lblResumenTexto.setForeground(new Color(203, 213, 225));

        aplicarEstiloCampo(txtCodigo);
        aplicarEstiloCombo(cmbEquipo);
        aplicarEstiloCombo(cmbTipo);
        aplicarEstiloCombo(cmbResultado);
        aplicarEstiloArea(txtDescripcion);
        aplicarEstiloArea(txtAcciones);
        aplicarEstiloArea(txtPiezas);
        aplicarEstiloArea(txtObservaciones);

        configurarBotonPrincipal(btnGuardar);
        configurarBotonClaro(btnLimpiar);
        configurarBotonMenu(btnCerrarSesion);
        configurarBotonVentana(btnCerrar, new Color(239, 68, 68));
        configurarBotonVentana(btnMinimizar, new Color(59, 130, 246));
    }

    private void configurarEventosHover() {
        agregarHover(btnGuardar, azulPrimario, azulHover, Color.WHITE, Color.WHITE);
        agregarHover(btnLimpiar, Color.WHITE, new Color(241, 245, 249), new Color(51, 65, 85), new Color(15, 23, 42));
        agregarHover(btnCerrarSesion, azulCard, new Color(30, 64, 115), Color.WHITE, Color.WHITE);
        agregarHover(btnCerrar, new Color(239, 68, 68), new Color(220, 38, 38), Color.WHITE, Color.WHITE);
        agregarHover(btnMinimizar, new Color(59, 130, 246), new Color(37, 99, 235), Color.WHITE, Color.WHITE);
    }

    private void aplicarEstiloCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setForeground(new Color(30, 41, 59));
        campo.setBackground(new Color(248, 250, 252));
        campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordeSuave),
                new EmptyBorder(8, 12, 8, 12)));
    }

    private void aplicarEstiloCombo(JComboBox combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setForeground(new Color(30, 41, 59));
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(bordeSuave));
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void aplicarEstiloArea(JTextArea area) {
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setForeground(new Color(30, 41, 59));
        area.setBackground(new Color(248, 250, 252));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(10, 12, 10, 12));
    }

    private void configurarBotonPrincipal(JButton boton) {
        boton.setBackground(azulPrimario);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(12, 24, 12, 24));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void configurarBotonClaro(JButton boton) {
        boton.setBackground(Color.WHITE);
        boton.setForeground(new Color(51, 65, 85));
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bordeSuave),
                new EmptyBorder(12, 24, 12, 24)));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void configurarBotonMenu(JButton boton) {
        boton.setBackground(azulCard);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setBorder(new EmptyBorder(12, 16, 12, 16));
        boton.setHorizontalAlignment(SwingConstants.LEFT);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void configurarBotonVentana(JButton boton, Color color) {
        boton.setBackground(color);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(null);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void agregarHover(JButton boton, Color fondoNormal, Color fondoHover, Color textoNormal, Color textoHover) {
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(fondoHover);
                boton.setForeground(textoHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(fondoNormal);
                boton.setForeground(textoNormal);
            }
        });
    }

    private void cargarSesion() {
        Usuario usuario = Sesion.getUsuarioActivo();
        if (usuario == null) {
            lblUsuario.setText("Sesión no iniciada");
        } else {
            lblUsuario.setText("Usuario: " + usuario.getEmail());
        }
    }

    private void cargarEquipos() {
        cmbEquipo.removeAllItems();
        List<EquipoOficina> equipos = equipoDAO.listarActivos();
        for (EquipoOficina equipo : equipos) {
            cmbEquipo.addItem(equipo);
        }
        lblTotalEquipos.setText(String.valueOf(equipos.size()));
        if (equipos.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "No hay equipos de oficina registrados. Debe registrar equipos antes de crear mantenimientos.",
                    "Sin equipos", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void guardarMantenimiento() {
        EquipoOficina equipo = (EquipoOficina) cmbEquipo.getSelectedItem();
        Usuario tecnico = Sesion.getUsuarioActivo();

        if (equipo == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un equipo registrado.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (tecnico == null) {
            JOptionPane.showMessageDialog(this, "No existe una sesión activa. Vuelva a iniciar sesión.", "Sesión", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (txtDescripcion.getText().trim().isEmpty() || txtAcciones.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "La descripción y las acciones realizadas son obligatorias.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!equipoDAO.existePorId(equipo.getIdEquipo())) {
            JOptionPane.showMessageDialog(this, "El equipo no está registrado o fue eliminado. Debe registrarse primero.", "Equipo no registrado", JOptionPane.ERROR_MESSAGE);
            cargarEquipos();
            return;
        }

        MantenimientoEquipoOficina mantenimiento = new MantenimientoEquipoOficina();
        mantenimiento.setEquipo(equipo);
        mantenimiento.setTecnico(tecnico);
        mantenimiento.setTipoMantenimiento((String) cmbTipo.getSelectedItem());
        mantenimiento.setDescripcion(txtDescripcion.getText().trim());
        mantenimiento.setAccionesRealizadas(txtAcciones.getText().trim());
        mantenimiento.setPiezasRevisadas(txtPiezas.getText().trim());
        mantenimiento.setEstadoResultado((String) cmbResultado.getSelectedItem());
        mantenimiento.setObservaciones(txtObservaciones.getText().trim());

        if (mantenimientoDAO.registrar(mantenimiento)) {
            JOptionPane.showMessageDialog(this, "Mantenimiento registrado correctamente.", "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo guardar el mantenimiento. Revise la conexión o la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarFormulario() {
        if (cmbEquipo.getItemCount() > 0) {
            cmbEquipo.setSelectedIndex(0);
        }
        cmbTipo.setSelectedIndex(0);
        cmbResultado.setSelectedIndex(0);
        txtDescripcion.setText("");
        txtAcciones.setText("");
        txtPiezas.setText("");
        txtObservaciones.setText("");
    }

    private void cerrarSesion() {
        Sesion.cerrarSesion();
        new VistaLogin().setVisible(true);
        dispose();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelRaiz = new javax.swing.JPanel();
        panelMenu = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        lblModulo = new javax.swing.JLabel();
        lblUsuario = new javax.swing.JLabel();
        panelResumen = new javax.swing.JPanel();
        lblResumenTitulo = new javax.swing.JLabel();
        lblResumenTexto = new javax.swing.JLabel();
        lblTotalEquiposTitulo = new javax.swing.JLabel();
        lblTotalEquipos = new javax.swing.JLabel();
        btnCerrarSesion = new javax.swing.JButton();
        panelSuperior = new javax.swing.JPanel();
        lblTituloVentana = new javax.swing.JLabel();
        btnMinimizar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();
        lblTitulo = new javax.swing.JLabel();
        lblSubtitulo = new javax.swing.JLabel();
        panelFormulario = new javax.swing.JPanel();
        lblSeccionEquipo = new javax.swing.JLabel();
        lblCodigo = new javax.swing.JLabel();
        txtCodigo = new javax.swing.JTextField();
        lblEquipo = new javax.swing.JLabel();
        cmbEquipo = new javax.swing.JComboBox();
        lblTipo = new javax.swing.JLabel();
        cmbTipo = new javax.swing.JComboBox();
        lblResultado = new javax.swing.JLabel();
        cmbResultado = new javax.swing.JComboBox();
        lblDescripcion = new javax.swing.JLabel();
        scrollDescripcion = new javax.swing.JScrollPane();
        txtDescripcion = new javax.swing.JTextArea();
        lblAcciones = new javax.swing.JLabel();
        scrollAcciones = new javax.swing.JScrollPane();
        txtAcciones = new javax.swing.JTextArea();
        lblPiezas = new javax.swing.JLabel();
        scrollPiezas = new javax.swing.JScrollPane();
        txtPiezas = new javax.swing.JTextArea();
        lblObservaciones = new javax.swing.JLabel();
        scrollObservaciones = new javax.swing.JScrollPane();
        txtObservaciones = new javax.swing.JTextArea();
        lblIndicacion = new javax.swing.JLabel();
        panelAcciones = new javax.swing.JPanel();
        btnLimpiar = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelRaiz.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        panelMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblLogo.setFont(new java.awt.Font("Segoe UI", 1, 32)); // NOI18N
        lblLogo.setText("HIRATA");
        panelMenu.add(lblLogo, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 34, 210, 42));

        lblModulo.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lblModulo.setText("Soporte IT / Oficina");
        panelMenu.add(lblModulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 78, 210, 28));

        lblUsuario.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblUsuario.setText("Usuario: soporte.it@hirata.cl");
        panelMenu.add(lblUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 116, 230, 28));

        panelResumen.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblResumenTitulo.setFont(new java.awt.Font("Segoe UI", 1, 22)); // NOI18N
        lblResumenTitulo.setText("RF-06");
        panelResumen.add(lblResumenTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 20, 140, 32));

        lblResumenTexto.setFont(new java.awt.Font("Segoe UI", 0, 13)); // NOI18N
        lblResumenTexto.setText("<html>Registro de mantenimiento preventivo y correctivo para PCs, notebooks, impresoras y otros equipos de oficina.</html>");
        panelResumen.add(lblResumenTexto, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 58, 210, 78));

        lblTotalEquiposTitulo.setText("Equipos disponibles");
        panelResumen.add(lblTotalEquiposTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 150, 150, 24));

        lblTotalEquipos.setFont(new java.awt.Font("Segoe UI", 1, 30)); // NOI18N
        lblTotalEquipos.setText("0");
        panelResumen.add(lblTotalEquipos, new org.netbeans.lib.awtextra.AbsoluteConstraints(22, 174, 90, 42));

        panelMenu.add(panelResumen, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 185, 240, 235));

        btnCerrarSesion.setText("Cerrar sesión");
        btnCerrarSesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarSesionActionPerformed(evt);
            }
        });
        panelMenu.add(btnCerrarSesion, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 655, 240, 44));

        panelRaiz.add(panelMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 290, 740));

        panelSuperior.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                panelSuperiorMouseDragged(evt);
            }
        });
        panelSuperior.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                panelSuperiorMousePressed(evt);
            }
        });
        panelSuperior.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTituloVentana.setText("Hirata Transporte  •  Módulo Soporte IT");
        panelSuperior.add(lblTituloVentana, new org.netbeans.lib.awtextra.AbsoluteConstraints(24, 8, 360, 28));

        btnMinimizar.setText("—");
        btnMinimizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinimizarActionPerformed(evt);
            }
        });
        panelSuperior.add(btnMinimizar, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 6, 34, 30));

        btnCerrar.setText("X");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });
        panelSuperior.add(btnCerrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 6, 34, 30));

        panelRaiz.add(panelSuperior, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 0, 910, 42));

        lblTitulo.setFont(new java.awt.Font("Segoe UI", 1, 28)); // NOI18N
        lblTitulo.setText("Registrar mantenimiento de equipos de oficina");
        panelRaiz.add(lblTitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 70, 650, 38));

        lblSubtitulo.setFont(new java.awt.Font("Segoe UI", 0, 15)); // NOI18N
        lblSubtitulo.setText("Complete la ficha técnica del mantenimiento realizado por soporte IT.");
        panelRaiz.add(lblSubtitulo, new org.netbeans.lib.awtextra.AbsoluteConstraints(332, 112, 620, 25));

        panelFormulario.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblSeccionEquipo.setText("Ficha de mantenimiento");
        panelFormulario.add(lblSeccionEquipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 22, 260, 30));

        lblCodigo.setText("Código interno");
        panelFormulario.add(lblCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 68, 180, 24));

        txtCodigo.setEditable(false);
        txtCodigo.setText("Generado automáticamente al guardar");
        panelFormulario.add(txtCodigo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 94, 330, 42));

        lblEquipo.setText("Equipo registrado");
        panelFormulario.add(lblEquipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 68, 180, 24));
        panelFormulario.add(cmbEquipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 94, 430, 42));

        lblTipo.setText("Tipo de mantenimiento");
        panelFormulario.add(lblTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 154, 190, 24));

        cmbTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Preventivo", "Correctivo" }));
        panelFormulario.add(cmbTipo, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 330, 42));

        lblResultado.setText("Resultado del equipo");
        panelFormulario.add(lblResultado, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 154, 190, 24));

        cmbResultado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Operativo", "Operativo con observaciones", "Requiere seguimiento", "Fuera de servicio" }));
        panelFormulario.add(cmbResultado, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 180, 430, 42));

        lblDescripcion.setText("Descripción del mantenimiento *");
        panelFormulario.add(lblDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 240, 24));

        txtDescripcion.setColumns(20);
        txtDescripcion.setRows(5);
        scrollDescripcion.setViewportView(txtDescripcion);

        panelFormulario.add(scrollDescripcion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 266, 390, 90));

        lblAcciones.setText("Acciones realizadas *");
        panelFormulario.add(lblAcciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 240, 240, 24));

        txtAcciones.setColumns(20);
        txtAcciones.setRows(5);
        scrollAcciones.setViewportView(txtAcciones);

        panelFormulario.add(scrollAcciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 266, 370, 90));

        lblPiezas.setText("Piezas revisadas o sustituidas");
        panelFormulario.add(lblPiezas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 372, 240, 24));

        txtPiezas.setColumns(20);
        txtPiezas.setRows(5);
        scrollPiezas.setViewportView(txtPiezas);

        panelFormulario.add(scrollPiezas, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 398, 390, 78));

        lblObservaciones.setText("Observaciones");
        panelFormulario.add(lblObservaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 372, 240, 24));

        txtObservaciones.setColumns(20);
        txtObservaciones.setRows(5);
        scrollObservaciones.setViewportView(txtObservaciones);

        panelFormulario.add(scrollObservaciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 398, 370, 78));

        lblIndicacion.setText("* Campos obligatorios para cumplir el flujo normal.");
        panelFormulario.add(lblIndicacion, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 500, 500, 24));

        panelRaiz.add(panelFormulario, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 160, 845, 540));

        panelAcciones.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnLimpiar.setText("Cancelar / limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });
        panelAcciones.add(btnLimpiar, new org.netbeans.lib.awtextra.AbsoluteConstraints(18, 14, 170, 46));

        btnGuardar.setText("Guardar mantenimiento");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });
        panelAcciones.add(btnGuardar, new org.netbeans.lib.awtextra.AbsoluteConstraints(204, 14, 220, 46));

        panelRaiz.add(panelAcciones, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 72, 425, 74));

        getContentPane().add(panelRaiz, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 740));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        guardarMantenimiento();
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnCerrarSesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarSesionActionPerformed
        cerrarSesion();
    }//GEN-LAST:event_btnCerrarSesionActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        System.exit(0);
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnMinimizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinimizarActionPerformed
        setState(JFrame.ICONIFIED);
    }//GEN-LAST:event_btnMinimizarActionPerformed

    private void panelSuperiorMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSuperiorMousePressed
        mouseX = evt.getX();
        mouseY = evt.getY();
    }//GEN-LAST:event_panelSuperiorMousePressed

    private void panelSuperiorMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_panelSuperiorMouseDragged
        setLocation(evt.getXOnScreen() - mouseX, evt.getYOnScreen() - mouseY);
    }//GEN-LAST:event_panelSuperiorMouseDragged

    public static class RoundedPanel extends JPanel {
        private int radio;

        public RoundedPanel() {
            this(20);
        }

        public RoundedPanel(int radio) {
            this.radio = radio;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radio, radio);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnCerrarSesion;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnMinimizar;
    private javax.swing.JComboBox cmbEquipo;
    private javax.swing.JComboBox cmbResultado;
    private javax.swing.JComboBox cmbTipo;
    private javax.swing.JLabel lblAcciones;
    private javax.swing.JLabel lblCodigo;
    private javax.swing.JLabel lblDescripcion;
    private javax.swing.JLabel lblEquipo;
    private javax.swing.JLabel lblIndicacion;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblModulo;
    private javax.swing.JLabel lblObservaciones;
    private javax.swing.JLabel lblPiezas;
    private javax.swing.JLabel lblResultado;
    private javax.swing.JLabel lblResumenTexto;
    private javax.swing.JLabel lblResumenTitulo;
    private javax.swing.JLabel lblSeccionEquipo;
    private javax.swing.JLabel lblSubtitulo;
    private javax.swing.JLabel lblTipo;
    private javax.swing.JLabel lblTitulo;
    private javax.swing.JLabel lblTituloVentana;
    private javax.swing.JLabel lblTotalEquipos;
    private javax.swing.JLabel lblTotalEquiposTitulo;
    private javax.swing.JLabel lblUsuario;
    private javax.swing.JPanel panelAcciones;
    private javax.swing.JPanel panelFormulario;
    private javax.swing.JPanel panelMenu;
    private javax.swing.JPanel panelRaiz;
    private javax.swing.JPanel panelResumen;
    private javax.swing.JPanel panelSuperior;
    private javax.swing.JScrollPane scrollAcciones;
    private javax.swing.JScrollPane scrollDescripcion;
    private javax.swing.JScrollPane scrollObservaciones;
    private javax.swing.JScrollPane scrollPiezas;
    private javax.swing.JTextArea txtAcciones;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextArea txtDescripcion;
    private javax.swing.JTextArea txtObservaciones;
    private javax.swing.JTextArea txtPiezas;
    // End of variables declaration//GEN-END:variables
}
