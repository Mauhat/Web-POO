import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Incidencia {
    private final String tipo;
    private final Conductor conductor;
    private final List<ServicioEmergencia> servicios;
    private final String hora;
    private final double posicion; // avance a lo largo de la vía (0 a 1)

    public Incidencia(String tipo, Conductor conductor, List<ServicioEmergencia> servicios) {
        this.tipo = tipo;
        this.conductor = conductor;
        this.servicios = servicios;
        this.hora = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        this.posicion = 0.2 + Math.random() * 0.7;
    }
    public String getTipo() { return tipo; }
    public Conductor getConductor() { return conductor; }
    public Ruta getRuta() { return conductor.getRuta(); }
    public double getPosicion() { return posicion; }

    public String mostrar() {
        StringBuilder sb = new StringBuilder();
        sb.append(tipo).append(" a las ").append(hora).append("\n");
        sb.append("  Conductor: ").append(conductor.getNombre()).append("\n");
        sb.append("  Vía: ").append(getRuta()).append("\n");
        sb.append("  Llamar a: ");
        for (int i = 0; i < servicios.size(); i++) {
            if (i > 0) sb.append(" | ");
            sb.append(servicios.get(i));
        }
        return sb.append("\n\n").toString();
    }
}
