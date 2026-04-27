package modelo;

import java.util.Date;

public class MantenimientoEquipoOficina {
    private int idMantenimientoEquipo;
    private EquipoOficina equipo;
    private Usuario tecnico;
    private Date fechaRegistro;
    private String tipoMantenimiento;
    private String descripcion;
    private String accionesRealizadas;
    private String piezasRevisadas;
    private String estadoResultado;
    private String observaciones;

    public int getIdMantenimientoEquipo() { return idMantenimientoEquipo; }
    public void setIdMantenimientoEquipo(int idMantenimientoEquipo) { this.idMantenimientoEquipo = idMantenimientoEquipo; }
    public EquipoOficina getEquipo() { return equipo; }
    public void setEquipo(EquipoOficina equipo) { this.equipo = equipo; }
    public Usuario getTecnico() { return tecnico; }
    public void setTecnico(Usuario tecnico) { this.tecnico = tecnico; }
    public Date getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(Date fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getTipoMantenimiento() { return tipoMantenimiento; }
    public void setTipoMantenimiento(String tipoMantenimiento) { this.tipoMantenimiento = tipoMantenimiento; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getAccionesRealizadas() { return accionesRealizadas; }
    public void setAccionesRealizadas(String accionesRealizadas) { this.accionesRealizadas = accionesRealizadas; }
    public String getPiezasRevisadas() { return piezasRevisadas; }
    public void setPiezasRevisadas(String piezasRevisadas) { this.piezasRevisadas = piezasRevisadas; }
    public String getEstadoResultado() { return estadoResultado; }
    public void setEstadoResultado(String estadoResultado) { this.estadoResultado = estadoResultado; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
