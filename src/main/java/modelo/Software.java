/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Timestamp;

/**
 *
 * @author leslie
 */

public class Software {

    private int idSoftware;
    private TipoSoftware tipoSoftware;
    private String nombre;
    private String fabricante;
    private String descripcion;
    private Timestamp fechaRegistro;

    public Software() {
    }

    public Software(TipoSoftware tipoSoftware, String nombre, String fabricante, String descripcion) {
        this.tipoSoftware = tipoSoftware;
        this.nombre = nombre;
        this.fabricante = fabricante;
        this.descripcion = descripcion;
    }

    public int getIdSoftware() {
        return idSoftware;
    }

    public TipoSoftware getTipoSoftware() {
        return tipoSoftware;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFabricante() {
        return fabricante;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setIdSoftware(int idSoftware) {
        this.idSoftware = idSoftware;
    }

    public void setTipoSoftware(TipoSoftware tipoSoftware) {
        this.tipoSoftware = tipoSoftware;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }


    @Override
    public String toString() {
        return nombre + " (" + tipoSoftware.getNombre() + ")";
    }
}
