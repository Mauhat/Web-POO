public class ServicioEmergencia {
    private final String nombre, telefono;
    public ServicioEmergencia(String nombre, String telefono) {
        this.nombre = nombre; this.telefono = telefono;
    }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    @Override public String toString() { return nombre + " " + telefono; }
}
