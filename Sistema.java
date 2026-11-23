import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Sistema {
    private final List<Usuario> usuarios = new ArrayList<>();
    private final List<Ruta> rutas = new ArrayList<>();
    private final List<Conductor> conductores = new ArrayList<>();
    private final List<Incidencia> incidencias = new ArrayList<>();
    private final Random azar = new Random();

    public Sistema() {
        usuarios.add(new Administrador());
        usuarios.add(new Gestor());
        rutas.add(new Ruta(1, "Lima", "Ica", 303, 0.32, 0.58));
        rutas.add(new Ruta(2, "Lima", "Arequipa", 1010, 0.65, 0.83));
        rutas.add(new Ruta(3, "Lima", "Trujillo", 560, 0.37, 0.20));
        rutas.add(new Ruta(4, "Lima", "Huancayo", 300, 0.50, 0.47));
        rutas.add(new Ruta(5, "Lima", "Cusco", 1100, 0.85, 0.63));
        String[][] c = {{"Carlos Quispe","A-IIIc"},{"Luis Ramos","A-IIIc"},{"Marco Soto","A-IIIb"},
                        {"Jorge Vega","A-IIIc"},{"Pedro Lara","A-IIIb"}};
        for (int i = 0; i < c.length; i++) {
            conductores.add(new Conductor(i + 1, c[i][0], c[i][1], rutas.get(i)));
        }
    }

    /** Devuelve el usuario si las credenciales son correctas, o null. */
    public Usuario login(String u, String c) {
        for (Usuario x : usuarios) {
            if (x.validarCredenciales(u, c)) return x;
        }
        return null;
    }

    public Ruta agregarRuta(String origen, String destino, int km) {
        Ruta r = new Ruta(rutas.size() + 1, origen, destino, km,
                0.35 + azar.nextDouble() * 0.55, 0.10 + azar.nextDouble() * 0.75);
        rutas.add(r);
        return r;
    }

    public Conductor agregarConductor(String nombre, String licencia, Ruta ruta) {
        Conductor c = new Conductor(conductores.size() + 1, nombre, licencia, ruta);
        conductores.add(c);
        return c;
    }

    public Incidencia generarIncidencia() {
        Conductor c = conductores.get(azar.nextInt(conductores.size()));
        boolean choque = azar.nextBoolean();
        List<ServicioEmergencia> s = new ArrayList<>();
        if (choque) {
            s.add(new ServicioEmergencia("Policía Nacional", "105"));
            s.add(new ServicioEmergencia("Bomberos", "116"));
        } else {
            s.add(new ServicioEmergencia("SAMU / Ambulancia", "106"));
            s.add(new ServicioEmergencia("Policía Nacional", "105"));
            s.add(new ServicioEmergencia("Bomberos", "116"));
        }
        Incidencia i = new Incidencia(choque ? "Choque" : "Accidente", c, s);
        incidencias.add(0, i);
        return i;
    }

    /** Tiempo aleatorio entre 1 y 3 minutos, en milisegundos. */
    public int tiempoAleatorioMs() { return (60 + azar.nextInt(121)) * 1000; }

    public List<Ruta> getRutas() { return rutas; }
    public List<Conductor> getConductores() { return conductores; }
    public List<Incidencia> getIncidencias() { return incidencias; }
}
