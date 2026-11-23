import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelPrincipal extends JFrame {
    private final Sistema sis;
    private final Usuario usuario;
    private final DefaultTableModel mRutas = modelo("N°", "Ruta", "Km");
    private final DefaultTableModel mCond = modelo("N°", "Nombre", "Licencia", "Ruta");
    private final JComboBox<Ruta> cmbRuta = new JComboBox<>();
    private final JTextArea areaInc = new JTextArea();
    private final JLabel lblCuenta = new JLabel(" ");
    private final JLabel lblMsg = new JLabel(" ");
    private final MapaPanel mapa = new MapaPanel();
    private Timer temporizador, reloj;
    private long proxima;
    private Incidencia actual;

    public PanelPrincipal(Sistema sis, Usuario usuario) {
        super("Cruz del Sur - " + usuario.getUsuario() + " (" + usuario.getRol() + ")");
        this.sis = sis;
        this.usuario = usuario;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(crearBarra(), BorderLayout.NORTH);

        JPanel centro = new JPanel(new GridLayout(1, 2, 10, 0));
        centro.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centro.add(crearIzquierda());
        centro.add(crearDerecha());
        add(centro, BorderLayout.CENTER);

        refrescar();
        setSize(1000, 600);
        setLocationRelativeTo(null);
        iniciarTemporizador();
    }

    private static DefaultTableModel modelo(String... cols) {
        return new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private JPanel crearBarra() {
        JPanel b = new JPanel(new BorderLayout());
        b.setBackground(new Color(0x0F1C33));
        b.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        JLabel t = new JLabel("Cruz del Sur · " + usuario.getUsuario() + " (" + usuario.getRol() + ")");
        t.setForeground(Color.WHITE);
        t.setFont(t.getFont().deriveFont(Font.BOLD, 15f));
        lblCuenta.setForeground(new Color(0xB9C5DC));
        JButton sim = new JButton("Simular incidencia");
        JButton salir = new JButton("Cerrar sesión");
        sim.addActionListener(e -> nuevaIncidencia());
        salir.addActionListener(e -> {
            temporizador.stop();
            reloj.stop();
            dispose();
            new CruzDelSurLogin().setVisible(true);
        });
        JPanel der = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        der.setOpaque(false);
        der.add(lblCuenta);
        der.add(sim);
        der.add(salir);
        b.add(t, BorderLayout.WEST);
        b.add(der, BorderLayout.EAST);
        return b;
    }

    private JPanel crearIzquierda() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Rutas", new JScrollPane(new JTable(mRutas)));
        tabs.addTab("Conductores", new JScrollPane(new JTable(mCond)));
        p.add(tabs, BorderLayout.CENTER);
        if (usuario.puedeRegistrar()) p.add(crearFormulario(), BorderLayout.SOUTH);
        return p;
    }

    private JPanel crearFormulario() {
        JPanel f = new JPanel(new GridLayout(0, 2, 6, 6));
        f.setBorder(BorderFactory.createTitledBorder("Registrar"));
        JTextField ori = new JTextField(), des = new JTextField(), km = new JTextField();
        JTextField nom = new JTextField(), lic = new JTextField();
        JButton bRuta = new JButton("Agregar ruta"), bCond = new JButton("Agregar conductor");

        f.add(new JLabel("Origen")); f.add(ori);
        f.add(new JLabel("Destino")); f.add(des);
        f.add(new JLabel("Kilómetros")); f.add(km);
        f.add(new JLabel()); f.add(bRuta);
        f.add(new JLabel("Nombre")); f.add(nom);
        f.add(new JLabel("Licencia")); f.add(lic);
        f.add(new JLabel("Ruta")); f.add(cmbRuta);
        f.add(lblMsg); f.add(bCond);

        bRuta.addActionListener(e -> {
            try {
                int k = Integer.parseInt(km.getText().trim());
                if (ori.getText().trim().isEmpty() || des.getText().trim().isEmpty() || k <= 0) throw new NumberFormatException();
                sis.agregarRuta(ori.getText().trim(), des.getText().trim(), k);
                ori.setText(""); des.setText(""); km.setText("");
                refrescar();
                aviso("Ruta agregada.", true);
            } catch (NumberFormatException ex) {
                aviso("Completa origen, destino y km válidos.", false);
            }
        });
        bCond.addActionListener(e -> {
            if (nom.getText().trim().isEmpty() || lic.getText().trim().isEmpty() || cmbRuta.getSelectedItem() == null) {
                aviso("Completa nombre y licencia.", false);
                return;
            }
            sis.agregarConductor(nom.getText().trim(), lic.getText().trim(), (Ruta) cmbRuta.getSelectedItem());
            nom.setText(""); lic.setText("");
            refrescar();
            aviso("Conductor agregado.", true);
        });
        return f;
    }

    private JPanel crearDerecha() {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.add(mapa, BorderLayout.CENTER);
        areaInc.setEditable(false);
        areaInc.setText("Sin incidencias todavía. Aparecerá una cada 1 a 3 minutos.");
        JScrollPane sp = new JScrollPane(areaInc);
        sp.setBorder(BorderFactory.createTitledBorder("Incidencias"));
        sp.setPreferredSize(new Dimension(100, 200));
        p.add(sp, BorderLayout.SOUTH);
        return p;
    }

    private void aviso(String t, boolean ok) {
        lblMsg.setForeground(ok ? new Color(0x1D6B3F) : new Color(0xB3261E));
        lblMsg.setText(t);
    }

    private void refrescar() {
        mRutas.setRowCount(0);
        for (Ruta r : sis.getRutas()) mRutas.addRow(new Object[]{r.getId(), r, r.getKm()});
        mCond.setRowCount(0);
        for (Conductor c : sis.getConductores())
            mCond.addRow(new Object[]{c.getId(), c.getNombre(), c.getLicencia(), c.getRuta()});
        cmbRuta.removeAllItems();
        for (Ruta r : sis.getRutas()) cmbRuta.addItem(r);
        mapa.repaint();
    }

    private void nuevaIncidencia() {
        actual = sis.generarIncidencia();
        StringBuilder sb = new StringBuilder();
        int n = Math.min(6, sis.getIncidencias().size());
        for (int i = 0; i < n; i++) sb.append(sis.getIncidencias().get(i).mostrar());
        areaInc.setText(sb.toString());
        areaInc.setCaretPosition(0);
        mapa.repaint();
    }

    private void iniciarTemporizador() {
        programar();
        reloj = new Timer(1000, e -> {
            long s = Math.max(0, (proxima - System.currentTimeMillis()) / 1000);
            lblCuenta.setText(String.format("Próxima incidencia en %d:%02d", s / 60, s % 60));
        });
        reloj.start();
    }

    private void programar() {
        int ms = sis.tiempoAleatorioMs(); // 1 a 3 minutos
        proxima = System.currentTimeMillis() + ms;
        temporizador = new Timer(ms, e -> { nuevaIncidencia(); programar(); });
        temporizador.setRepeats(false);
        temporizador.start();
    }

    /** Mapa en miniatura dibujado con Graphics2D. */
    private class MapaPanel extends JPanel {
        MapaPanel() {
            setBackground(new Color(0x0F1C33));
            setBorder(BorderFactory.createTitledBorder("Mapa de rutas"));
        }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(), h = getHeight();
            int lx = (int) (w * 0.15), ly = (int) (h * 0.73);
            g2.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                    10f, new float[]{5f, 4f}, 0f));
            for (Ruta r : sis.getRutas()) {
                int x = (int) (w * r.getX()), y = (int) (h * r.getY());
                g2.setColor(new Color(0x4A6190));
                g2.drawLine(lx, ly, x, y);
                g2.setColor(Color.WHITE);
                g2.fillOval(x - 4, y - 4, 8, 8);
                g2.setColor(new Color(0xB9C5DC));
                g2.drawString(r.getDestino(), x + 7, y - 6);
            }
            g2.setColor(new Color(0xE9C46A));
            g2.fillOval(lx - 6, ly - 6, 12, 12);
            g2.drawString("Lima", lx - 14, ly + 22);
            if (actual != null) {
                Ruta r = actual.getRuta();
                double t = actual.getPosicion();
                int x = (int) (lx + (w * r.getX() - lx) * t), y = (int) (ly + (h * r.getY() - ly) * t);
                g2.setColor(new Color(0xFF5C5C));
                g2.fillOval(x - 7, y - 7, 14, 14);
                g2.setStroke(new BasicStroke(2f));
                g2.drawOval(x - 14, y - 14, 28, 28);
            }
            g2.dispose();
        }
    }
}
