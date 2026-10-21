import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel de acceso de administrador - Cruz del Sur
 * Compilar:  javac CruzDelSurLogin.java
 * Ejecutar:  java CruzDelSurLogin
 */
public class CruzDelSurLogin extends JFrame {

    // Colores
    private static final Color NOCHE   = new Color(0x0F1C33);
    private static final Color NOCHE_2 = new Color(0x18294A);
    private static final Color ESTRELLA = new Color(0xE9C46A);
    private static final Color AZUL    = new Color(0x1F4FA3);
    private static final Color GRIS    = new Color(0x5A6780);
    private static final Color ERROR   = new Color(0xB3261E);
    private static final Color OK      = new Color(0x1D6B3F);

    private final JTextField txtUsuario = new JTextField();
    private final JPasswordField txtClave = new JPasswordField();
    private final JButton btnVer = new JButton("Mostrar");
    private final JButton btnDespues = new JButton("Recordarme más tarde");
    private final JButton btnIngresar = new JButton("Ingresar");
    private final JLabel lblMensaje = new JLabel(" ");
    private final char ocultar = txtClave.getEchoChar();

    public CruzDelSurLogin() {
        super("Cruz del Sur - Panel de administración");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));
        add(crearPanelMarca());
        add(crearPanelFormulario());
        setSize(820, 460);
        setMinimumSize(new Dimension(700, 440));
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(btnIngresar);
        configurarEventos();
    }

    /** Panel izquierdo: nombre de la empresa y la constelación de la Cruz del Sur. */
    private JPanel crearPanelMarca() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setPaint(new GradientPaint(0, 0, NOCHE_2, 0, getHeight(), NOCHE));
                g2.fillRect(0, 0, getWidth(), getHeight());

                // Texto
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("SansSerif", Font.BOLD, 26));
                g2.drawString("Cruz del Sur", 36, 62);
                g2.setColor(new Color(0xB9C5DC));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 14));
                g2.drawString("Panel de administración", 36, 86);
                g2.setColor(new Color(0x8FA0C0));
                g2.setFont(new Font("SansSerif", Font.PLAIN, 12));
                g2.drawString("Acceso solo para personal autorizado.", 36, getHeight() - 30);

                // Constelación (coordenadas relativas al centro del panel)
                int cx = getWidth() / 2, cy = getHeight() / 2 + 10;
                int[][] e = { {0,-110,7}, {8,95,9}, {-70,-25,6}, {70,10,7}, {28,48,4} };
                g2.setColor(new Color(0x4A6190));
                g2.setStroke(new BasicStroke(1f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                        10f, new float[]{3f, 4f}, 0f));
                g2.drawLine(cx + e[0][0], cy + e[0][1], cx + e[1][0], cy + e[1][1]);
                g2.drawLine(cx + e[2][0], cy + e[2][1], cx + e[3][0], cy + e[3][1]);
                g2.setColor(ESTRELLA);
                for (int[] s : e) {
                    g2.fillOval(cx + s[0] - s[2], cy + s[1] - s[2], s[2] * 2, s[2] * 2);
                }
                g2.dispose();
            }
        };
        return p;
    }

    /** Panel derecho: formulario de credenciales. */
    private JPanel crearPanelFormulario() {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(40, 36, 40, 36));

        JLabel titulo = new JLabel("Ingresa a tu cuenta");
        titulo.setFont(new Font("SansSerif", Font.BOLD, 20));
        JLabel sub = new JLabel("Usa tus credenciales de administrador.");
        sub.setForeground(GRIS);

        JLabel lblUsuario = new JLabel("Usuario");
        JLabel lblClave = new JLabel("Contraseña");

        estiloCampo(txtUsuario);
        estiloCampo(txtClave);
        txtUsuario.setToolTipText("Ingresar usuario");
        txtClave.setToolTipText("Ingresar contraseña");

        // Campo de contraseña con el botón "Mostrar" al lado
        JPanel filaClave = new JPanel(new BorderLayout(6, 0));
        filaClave.setOpaque(false);
        filaClave.add(txtClave, BorderLayout.CENTER);
        btnVer.setFocusPainted(false);
        btnVer.setToolTipText("Mantén presionado para ver la contraseña");
        filaClave.add(btnVer, BorderLayout.EAST);
        filaClave.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));

        // Botones de acción
        btnIngresar.setBackground(AZUL);
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFont(btnIngresar.getFont().deriveFont(Font.BOLD));
        btnIngresar.setOpaque(true);
        btnIngresar.setBorderPainted(false);
        btnIngresar.setFocusPainted(false);
        btnDespues.setFocusPainted(false);

        JPanel acciones = new JPanel(new GridLayout(1, 2, 10, 0));
        acciones.setOpaque(false);
        acciones.add(btnDespues);
        acciones.add(btnIngresar);
        acciones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        for (JComponent c : new JComponent[]{titulo, sub, lblUsuario, txtUsuario, lblClave,
                filaClave, acciones, lblMensaje}) {
            c.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        p.add(titulo);
        p.add(Box.createVerticalStrut(4));
        p.add(sub);
        p.add(Box.createVerticalStrut(24));
        p.add(lblUsuario);
        p.add(Box.createVerticalStrut(6));
        p.add(txtUsuario);
        p.add(Box.createVerticalStrut(16));
        p.add(lblClave);
        p.add(Box.createVerticalStrut(6));
        p.add(filaClave);
        p.add(Box.createVerticalStrut(22));
        p.add(acciones);
        p.add(Box.createVerticalStrut(14));
        p.add(lblMensaje);
        return p;
    }

    private void estiloCampo(JTextField campo) {
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        campo.setPreferredSize(new Dimension(200, 36));
    }

    private void configurarEventos() {
        // Ver la contraseña solo mientras se mantiene presionado el botón
        btnVer.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                txtClave.setEchoChar((char) 0);
                btnVer.setText("Ocultar");
            }
            @Override public void mouseReleased(MouseEvent e) {
                txtClave.setEchoChar(ocultar);
                btnVer.setText("Mostrar");
            }
            @Override public void mouseExited(MouseEvent e) {
                txtClave.setEchoChar(ocultar);
                btnVer.setText("Mostrar");
            }
        });

        btnDespues.addActionListener(e -> {
            txtUsuario.setText("");
            txtClave.setText("");
            mostrar("Listo, te lo recordaremos más tarde.", OK);
        });

        btnIngresar.addActionListener(e -> {
            if (txtUsuario.getText().trim().isEmpty()) {
                mostrar("Ingresa tu usuario.", ERROR);
                txtUsuario.requestFocus();
            } else if (txtClave.getPassword().length == 0) {
                mostrar("Ingresa tu contraseña.", ERROR);
                txtClave.requestFocus();
            } else {
                // Aquí iría la validación real (base de datos, servicio, etc.)
                mostrar("Demostración: las credenciales no se validan contra ningún servidor.", OK);
            }
        });
    }

    private void mostrar(String texto, Color color) {
        lblMensaje.setForeground(color);
        lblMensaje.setText("<html>" + texto + "</html>");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CruzDelSurLogin().setVisible(true));
    }
}
