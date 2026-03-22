/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.util.Date;
import modelo.enums.EstadoMantenimiento;
import modelo.enums.OrigenMantenimiento;
import modelo.enums.TipoMantenimiento;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class Mantenimiento {

    private int idMantenimiento;
    private Vehiculo vehiculo;
    private Date fechaCreacion;    // cuándo se programó
    private Date fechaCompletado;
    private TipoMantenimiento tipoMantenimiento;
    private OrigenMantenimiento origen;
    private String descripcion;
    private int kilometraje;
    private EstadoMantenimiento estado;
    private Usuario usuarioMantenimiento;

    public Mantenimiento() {
    }

    //programar mantenimiento
    public Mantenimiento(int idMantenimiento, Vehiculo vehiculo, Date fechaCreacion, TipoMantenimiento tipoMantenimiento, OrigenMantenimiento origen, int kilometraje, EstadoMantenimiento estado) {
        this.idMantenimiento = idMantenimiento;
        this.vehiculo = vehiculo;
        this.fechaCreacion = fechaCreacion;
        this.tipoMantenimiento = tipoMantenimiento;
        this.origen = origen;
        this.kilometraje = kilometraje;
        this.estado = estado;
    }

    public int getIdMantenimiento() {
        return idMantenimiento;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public Date getFechaCompletado() {
        return fechaCompletado;
    }

    public TipoMantenimiento getTipoMantenimiento() {
        return tipoMantenimiento;
    }

    public OrigenMantenimiento getOrigen() {
        return origen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public EstadoMantenimiento getEstado() {
        return estado;
    }

    public Usuario getUsuarioMantenimiento() {
        return usuarioMantenimiento;
    }

    public void setIdMantenimiento(int idMantenimiento) {
        this.idMantenimiento = idMantenimiento;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    //fechas las asigna base de datos
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public void setFechaCompletado(Date fechaCompletado) {
        this.fechaCompletado = fechaCompletado;
    }

    public void setTipoMantenimiento(TipoMantenimiento tipoMantenimiento) {
        this.tipoMantenimiento = tipoMantenimiento;
    }

    public void setOrigen(OrigenMantenimiento origen) {
        this.origen = origen;
    }

    public void setDescripcion(String descripcion) {
        if ((this.estado == EstadoMantenimiento.Completado
                || this.estado == EstadoMantenimiento.Cancelado)
                && (descripcion == null || descripcion.trim().isEmpty())) {
            throw new IllegalArgumentException("La descripción es obligatoria al completar o cancelar un mantenimiento.");
        }
        this.descripcion = descripcion;
    }

    public void setKilometraje(int kilometraje) {
        if (kilometraje < 0) {
            throw new IllegalArgumentException("El kilometraje no puede ser negativo.");
        }
        this.kilometraje = kilometraje;
    }

    public void setEstado(EstadoMantenimiento estado) {
        if (this.estado == EstadoMantenimiento.Completado
                || this.estado == EstadoMantenimiento.Cancelado) {
            throw new IllegalStateException("No se puede cambiar el estado de un mantenimiento ya finalizado.");
        }
        this.estado = estado;
    }

    public void setUsuarioMantenimiento(Usuario usuarioMantenimiento) {
        this.usuarioMantenimiento = usuarioMantenimiento;
    }

    @Override
    public String toString() {
        return "Mantenimiento{" + "idMantenimiento=" + idMantenimiento + ", vehiculo=" + vehiculo + ", fechaCreacion=" + fechaCreacion + ", fechaCompletado=" + fechaCompletado + ", tipoMantenimiento=" + tipoMantenimiento + ", origen=" + origen + ", descripcion=" + descripcion + ", kilometraje=" + kilometraje + ", estado=" + estado + ", usuarioMantenimiento=" + usuarioMantenimiento + '}';
    }
}
