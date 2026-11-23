public class Gestor extends Usuario {
    public Gestor() { super("odin", "odinadmin123"); }
    @Override public String getRol() { return "gestor"; }
    @Override public boolean puedeRegistrar() { return true; }
}
