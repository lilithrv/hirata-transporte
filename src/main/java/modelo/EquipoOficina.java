package modelo;

import java.sql.Timestamp;

public class EquipoOficina {

    private int idEquipo;
    private TipoEquipo tipoEquipo;
    private String marca;
    private String modelo;
    private String numeroSerie;
    private String estado;
    private int idResponsable;
    private String fechaAdquisicion;
    private Timestamp fechaRegistro;
    private String nombreResponsable;

    public EquipoOficina() {
    }

    public EquipoOficina(TipoEquipo tipoEquipo, String marca, String modelo,
            String numeroSerie, String estado) {
        this.tipoEquipo = tipoEquipo;
        this.marca = marca;
        this.modelo = modelo;
        this.numeroSerie = numeroSerie;
        this.estado = estado;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public TipoEquipo getTipoEquipo() {
        return tipoEquipo;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public String getEstado() {
        return estado;
    }

    public int getIdResponsable() {
        return idResponsable;
    }

    public String getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public String getNombreResponsable() {
        return nombreResponsable;
    }
    
    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public void setTipoEquipo(TipoEquipo tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setIdResponsable(int idResponsable) {
        this.idResponsable = idResponsable;
    }

    public void setFechaAdquisicion(String fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
    
    public void setNombreResponsable(String nombreResponsable) {
        this.nombreResponsable = nombreResponsable;
    }

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + numeroSerie + ")";
    }
}
