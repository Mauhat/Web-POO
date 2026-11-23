public abstract class Usuario {
    private final String usuario;
    private final String clave;

    public Usuario(String usuario, String clave) {
        this.usuario = usuario;
        this.clave = clave;
    }

    public boolean validarCredenciales(String u, String c) {
        return usuario.equals(u) && clave.equals(c);
    }

    public String getUsuario() { return usuario; }
    public abstract String getRol();
    public abstract boolean puedeRegistrar();
}
