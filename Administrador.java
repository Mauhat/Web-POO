public class Administrador extends Usuario {
    public Administrador() { super("admin", "admin123"); }
    @Override public String getRol() { return "administrador"; }
    @Override public boolean puedeRegistrar() { return false; }
}
