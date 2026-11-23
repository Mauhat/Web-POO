public class Ruta {
    private final int id;
    private final String origen, destino;
    private final int km;
    private final double x, y; // posición relativa en el mapa (0 a 1)

    public Ruta(int id, String origen, String destino, int km, double x, double y) {
        this.id = id; this.origen = origen; this.destino = destino;
        this.km = km; this.x = x; this.y = y;
    }
    public int getId() { return id; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public int getKm() { return km; }
    public double getX() { return x; }
    public double getY() { return y; }
    @Override public String toString() { return origen + " - " + destino; }
}
