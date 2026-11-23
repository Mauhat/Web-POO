public class Conductor {
    private final int id;
    private final String nombre, licencia;
    private final Ruta ruta;

    public Conductor(int id, String nombre, String licencia, Ruta ruta) {
        this.id = id; this.nombre = nombre; this.licencia = licencia; this.ruta = ruta;
    }
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getLicencia() { return licencia; }
    public Ruta getRuta() { return ruta; }
    @Override public String toString() { return nombre; }
}
