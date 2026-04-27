package modelo;

public class EquipoOficina {
    private int idEquipo;
    private String codigoInventario;
    private String tipoEquipo;
    private String marca;
    private String modelo;
    private String ubicacion;
    private String estado;

    public EquipoOficina() {}

    public EquipoOficina(int idEquipo, String codigoInventario, String tipoEquipo, String marca, String modelo, String ubicacion, String estado) {
        this.idEquipo = idEquipo;
        this.codigoInventario = codigoInventario;
        this.tipoEquipo = tipoEquipo;
        this.marca = marca;
        this.modelo = modelo;
        this.ubicacion = ubicacion;
        this.estado = estado;
    }

    public int getIdEquipo() { return idEquipo; }
    public void setIdEquipo(int idEquipo) { this.idEquipo = idEquipo; }
    public String getCodigoInventario() { return codigoInventario; }
    public void setCodigoInventario(String codigoInventario) { this.codigoInventario = codigoInventario; }
    public String getTipoEquipo() { return tipoEquipo; }
    public void setTipoEquipo(String tipoEquipo) { this.tipoEquipo = tipoEquipo; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return codigoInventario + " - " + tipoEquipo + " " + marca + " " + modelo + " (" + ubicacion + ")";
    }
}
