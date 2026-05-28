package vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JFormattedTextField;
import javax.swing.JList;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.table.JTableHeader;

public final class EstiloHirata {
    public static final Color FONDO = new Color(236, 240, 247);
    public static final Color AZUL_NOCHE = new Color(12, 24, 44);
    public static final Color AZUL_CARD = new Color(18, 38, 69);
    public static final Color AZUL = new Color(37, 99, 235);
    public static final Color AZUL_HOVER = new Color(29, 78, 216);
    public static final Color TEXTO = new Color(30, 41, 59);
    public static final Color TEXTO_SUAVE = new Color(71, 85, 105);
    public static final Color BORDE = new Color(221, 226, 235);

    private EstiloHirata() {}

 public static void aplicarVentana(javax.swing.JFrame frame, String titulo, int ancho, int alto) {
    frame.setTitle(titulo);
    frame.getContentPane().setBackground(FONDO);

    aplicar(frame.getContentPane());

    frame.pack();

    frame.setMinimumSize(new java.awt.Dimension(ancho, alto));
    frame.setSize(Math.max(frame.getWidth(), ancho), Math.max(frame.getHeight(), alto));

    frame.setLocationRelativeTo(null);
    frame.setResizable(true);
}

    public static void aplicar(Container contenedor) {
        for (Component c : contenedor.getComponents()) {
            if (c instanceof JPanel panel) {
                panel.setBackground(panel.getBackground().equals(new Color(238, 238, 238)) ? Color.WHITE : panel.getBackground());
            }
            if (c instanceof JLabel label) {
                label.setFont(new Font("Segoe UI", Font.PLAIN, Math.max(label.getFont().getSize(), 13)));
                if (label.getForeground().equals(Color.BLACK)) label.setForeground(TEXTO);
            }
            if (c instanceof JTextField campo) aplicarCampo(campo);
            if (c instanceof JPasswordField campo) aplicarCampo(campo);
            if (c instanceof JTextArea area) aplicarArea(area);
            if (c instanceof JComboBox<?> combo) aplicarCombo(combo);
            if (c instanceof JButton boton) aplicarBoton(boton);
            if (c instanceof JTable tabla) aplicarTabla(tabla);
            if (c instanceof JScrollPane scroll) scroll.setBorder(BorderFactory.createLineBorder(BORDE));
            if (c instanceof Container child) aplicar(child);
        }
    }

    public static void aplicarCampo(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setForeground(TEXTO);
        campo.setBackground(new Color(248, 250, 252));
        campo.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE), new EmptyBorder(8, 12, 8, 12)));
    }

    public static void aplicarArea(JTextArea area) {
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setForeground(TEXTO);
        area.setBackground(new Color(248, 250, 252));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(10, 12, 10, 12));
    }

    public static void aplicarCombo(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setForeground(TEXTO);
        combo.setBackground(Color.WHITE);
        combo.setBorder(BorderFactory.createLineBorder(BORDE));
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public static void aplicarBoton(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.setBorder(new EmptyBorder(10, 16, 10, 16));
        String texto = boton.getText() == null ? "" : boton.getText().toLowerCase();
        Color normal = (texto.contains("eliminar") || texto.contains("cerrar") || texto.contains("salir")) ? new Color(239, 68, 68) : AZUL;
        Color hover = normal.equals(AZUL) ? AZUL_HOVER : new Color(220, 38, 38);
        boton.setBackground(normal);
        boton.setForeground(Color.WHITE);
        boton.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { boton.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e) { boton.setBackground(normal); }
        });
    }

    public static void aplicarBotonSecundario(JButton boton) {
        boton.setBackground(Color.WHITE);
        boton.setForeground(TEXTO_SUAVE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE), new EmptyBorder(10, 16, 10, 16)));
    }

    public static void aplicarTabla(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(30);
        tabla.setForeground(TEXTO);
        tabla.setSelectionBackground(new Color(219, 234, 254));
        tabla.setSelectionForeground(new Color(15, 23, 42));
        tabla.setGridColor(new Color(226, 232, 240));
        JTableHeader header = tabla.getTableHeader();
        if (header != null) {
            header.setFont(new Font("Segoe UI", Font.BOLD, 13));
            header.setBackground(AZUL_NOCHE);
            header.setForeground(Color.WHITE);
        }
    }

    public static void tarjeta(JComponent c) {
        c.setBackground(Color.WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDE), new EmptyBorder(18, 18, 18, 18)));
    }


    // ===================== ESTILO MODERNO OSCURO =====================
    // Se agregan métodos nuevos para no alterar las vistas excluidas que ya usan el estilo claro.
    public static final Color DARK_FONDO = new Color(3, 15, 30);
    public static final Color DARK_PANEL = new Color(10, 28, 52);
    public static final Color DARK_CARD = new Color(18, 39, 68);
    public static final Color DARK_INPUT = new Color(9, 24, 43);
    public static final Color DARK_BORDE = new Color(76, 106, 145);
    public static final Color DARK_ACCENT = new Color(45, 104, 235);
    public static final Color DARK_ACCENT_HOVER = new Color(88, 151, 255);
    public static final Color DARK_TEXTO = new Color(239, 246, 255);
    public static final Color DARK_TEXTO_SUAVE = new Color(171, 196, 229);
    public static final Color DARK_DANGER = new Color(235, 75, 92);

    public static void aplicarVentanaOscura(javax.swing.JFrame frame, String titulo, int ancho, int alto) {
        frame.setTitle(titulo);
        try {
            UIManager.put("Panel.background", DARK_FONDO);
            UIManager.put("Label.foreground", DARK_TEXTO);
            UIManager.put("TextField.caretForeground", DARK_TEXTO);
            UIManager.put("ComboBox.selectionBackground", DARK_ACCENT);
            UIManager.put("ComboBox.selectionForeground", Color.WHITE);
            UIManager.put("Table.gridColor", DARK_BORDE);
        } catch (Exception ignored) {}

        frame.getContentPane().setBackground(DARK_FONDO);
        aplicarOscuro(frame.getContentPane());
        frame.pack();
        frame.setMinimumSize(new java.awt.Dimension(ancho, alto));
        frame.setSize(ancho, alto);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
    }

    public static void aplicarDialogoOscuro(javax.swing.JDialog dialog) {
        dialog.getContentPane().setBackground(DARK_FONDO);
        aplicarOscuro(dialog.getContentPane());
        dialog.pack();
        dialog.setLocationRelativeTo(dialog.getParent());
        dialog.setResizable(false);
    }

    public static void aplicarOscuro(Container contenedor) {
        if (contenedor == null) return;
        contenedor.setBackground(DARK_FONDO);
        for (Component c : contenedor.getComponents()) {
            if (c instanceof JPanel panel) aplicarPanelOscuro(panel);
            if (c instanceof JLabel label) aplicarLabelOscuro(label);
            if (c instanceof JTextField campo) aplicarCampoOscuro(campo);
            if (c instanceof JPasswordField campo) aplicarCampoOscuro(campo);
            if (c instanceof JTextArea area) aplicarAreaOscura(area);
            if (c instanceof JComboBox<?> combo) aplicarComboOscuro(combo);
            if (c instanceof JButton boton) aplicarBotonOscuro(boton);
            if (c instanceof JTable tabla) aplicarTablaOscura(tabla);
            if (c instanceof JScrollPane scroll) aplicarScrollOscuro(scroll);
            if (c instanceof javax.swing.JTabbedPane tabs) aplicarTabsOscuro(tabs);
            if (c instanceof javax.swing.JCheckBox check) aplicarCheckOscuro(check);
            if (c instanceof javax.swing.JRadioButton radio) aplicarRadioOscuro(radio);
            if (c instanceof javax.swing.JSpinner spinner) aplicarSpinnerOscuro(spinner);
            if (c instanceof Container child) aplicarOscuro(child);
        }
    }

    private static void aplicarPanelOscuro(JPanel panel) {
        Color actual = panel.getBackground();
        boolean fondoDefault = actual == null || actual.equals(new Color(238, 238, 238)) || actual.equals(Color.WHITE) || actual.equals(FONDO);
        panel.setBackground(fondoDefault ? DARK_PANEL : actual);
        if (panel.getBorder() instanceof TitledBorder tb) {
            tb.setTitleColor(DARK_TEXTO);
            tb.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
            tb.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(new Color(39, 71, 111), 1, true),
                    new EmptyBorder(10, 12, 12, 12)));
            panel.setBackground(DARK_CARD);
        } else if (panel.getBorder() == null) {
            panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        }
    }

    private static void aplicarLabelOscuro(JLabel label) {
        int size = Math.max(label.getFont() == null ? 13 : label.getFont().getSize(), 13);
        int style = label.getFont() == null ? Font.PLAIN : label.getFont().getStyle();
        label.setFont(new Font("Segoe UI", style, size));
        // En las vistas oscuras se fuerza siempre texto claro para evitar que NetBeans/Nimbus deje labels en negro.
        label.setForeground(DARK_TEXTO);
    }

    public static void aplicarCampoOscuro(JTextField campo) {
        campo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        campo.setForeground(DARK_TEXTO);
        campo.setBackground(DARK_INPUT);
        campo.setCaretColor(DARK_TEXTO);
        campo.setSelectionColor(DARK_ACCENT);
        campo.setSelectedTextColor(Color.WHITE);
        campo.setOpaque(true);
        campo.setPreferredSize(new Dimension(Math.max(campo.getPreferredSize().width, 160), 38));
        campo.setBorder(BorderFactory.createCompoundBorder(new LineBorder(DARK_BORDE, 1, true), new EmptyBorder(8, 12, 8, 12)));
    }

    public static void aplicarAreaOscura(JTextArea area) {
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setForeground(DARK_TEXTO);
        area.setBackground(DARK_INPUT);
        area.setCaretColor(DARK_TEXTO);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createCompoundBorder(new LineBorder(DARK_BORDE, 1, true), new EmptyBorder(10, 12, 10, 12)));
    }

    public static void aplicarComboOscuro(JComboBox<?> combo) {
        combo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        combo.setForeground(DARK_TEXTO);
        combo.setBackground(DARK_INPUT);
        combo.setOpaque(true);
        combo.setPreferredSize(new Dimension(Math.max(combo.getPreferredSize().width, 160), 38));
        combo.setBorder(BorderFactory.createCompoundBorder(new LineBorder(DARK_BORDE, 1, true), new EmptyBorder(6, 10, 6, 10)));
        combo.setCursor(new Cursor(Cursor.HAND_CURSOR));
        combo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setForeground(Color.WHITE);
                c.setBackground(isSelected ? DARK_ACCENT : DARK_INPUT);
                if (c instanceof JComponent jc) jc.setOpaque(true);
                return c;
            }
        });
    }

    public static void aplicarBotonOscuro(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.setContentAreaFilled(true);
        boton.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(77, 139, 255), 1, true), new EmptyBorder(10, 18, 10, 18)));
        String texto = boton.getText() == null ? "" : boton.getText().toLowerCase();
        Color normal = (texto.contains("eliminar") || texto.contains("cerrar") || texto.contains("salir") || texto.contains("cancelar")) ? DARK_DANGER : DARK_ACCENT;
        Color hover = normal.equals(DARK_ACCENT) ? DARK_ACCENT_HOVER : new Color(248, 81, 98);
        boton.setBackground(normal);
        boton.setForeground(Color.WHITE);
        for (MouseAdapter adapter : new MouseAdapter[]{new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { boton.setBackground(hover); }
            @Override public void mouseExited(MouseEvent e) { boton.setBackground(normal); }
        }}) boton.addMouseListener(adapter);
    }

    public static void aplicarTablaOscura(JTable tabla) {
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(32);
        tabla.setForeground(DARK_TEXTO);
        tabla.setBackground(DARK_PANEL);
        tabla.setSelectionBackground(new Color(30, 83, 138));
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setGridColor(DARK_BORDE);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setFillsViewportHeight(true);
        JTableHeader header = tabla.getTableHeader();
        if (header != null) {
            header.setFont(new Font("Segoe UI", Font.BOLD, 13));
            header.setBackground(new Color(14, 34, 62));
            header.setForeground(Color.WHITE);
        }
    }

    private static void aplicarScrollOscuro(JScrollPane scroll) {
        scroll.setBorder(new LineBorder(new Color(39, 71, 111), 1, true));
        scroll.getViewport().setBackground(DARK_PANEL);
        scroll.setBackground(DARK_PANEL);
    }

    private static void aplicarTabsOscuro(javax.swing.JTabbedPane tabs) {
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabs.setForeground(DARK_TEXTO);
        tabs.setBackground(DARK_PANEL);
    }

    private static void aplicarCheckOscuro(javax.swing.JCheckBox check) {
        check.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        check.setForeground(DARK_TEXTO);
        check.setBackground(DARK_PANEL);
        check.setFocusPainted(false);
    }

    private static void aplicarRadioOscuro(javax.swing.JRadioButton radio) {
        radio.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        radio.setForeground(DARK_TEXTO);
        radio.setBackground(DARK_PANEL);
        radio.setFocusPainted(false);
    }

    public static void aplicarSpinnerOscuro(javax.swing.JSpinner spinner) {
        spinner.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        spinner.setForeground(DARK_TEXTO);
        spinner.setBackground(DARK_INPUT);
        spinner.setOpaque(true);
        spinner.setBorder(BorderFactory.createLineBorder(DARK_BORDE));
        if (spinner.getEditor() instanceof javax.swing.JSpinner.DefaultEditor editor) {
            JFormattedTextField txt = editor.getTextField();
            txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            txt.setForeground(DARK_TEXTO);
            txt.setDisabledTextColor(DARK_TEXTO_SUAVE);
            txt.setBackground(DARK_INPUT);
            txt.setCaretColor(DARK_TEXTO);
            txt.setSelectionColor(DARK_ACCENT);
            txt.setSelectedTextColor(Color.WHITE);
            txt.setOpaque(true);
            txt.setBorder(new EmptyBorder(8, 12, 8, 12));
        }
    }

}
