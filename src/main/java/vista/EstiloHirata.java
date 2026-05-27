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
}
