/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.sql.Timestamp;
import modelo.enums.EstadoSoftware;

/**
 *
 * @author leslie
 */
public class SoftwareEquipo {

    private int idSwEquipo;
    private int idEquipo;         
    private Software software;
    private String version;
    private EstadoSoftware estado;
    private int idTecnico;         
    private String nombreTecnico;     
    private Timestamp fechaAccion;
    private String notas;
    private Timestamp fechaRegistro;

    public SoftwareEquipo() {
    }

    public SoftwareEquipo(int idEquipo, Software software, String version, EstadoSoftware estado, int idTecnico, Timestamp fechaAccion) {
        this.idEquipo = idEquipo;
        this.software = software;
        this.version = version;
        this.estado = estado;
        this.idTecnico = idTecnico;
        this.fechaAccion = fechaAccion;
    }

    public int getIdSwEquipo() {
        return idSwEquipo;
    }

    public int getIdEquipo() {
        return idEquipo;
    }

    public Software getSoftware() {
        return software;
    }

    public String getVersion() {
        return version;
    }

    public EstadoSoftware getEstado() {
        return estado;
    }

    public int getIdTecnico() {
        return idTecnico;
    }

    public String getNombreTecnico() {
        return nombreTecnico;
    }

    public Timestamp getFechaAccion() {
        return fechaAccion;
    }

    public String getNotas() {
        return notas;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setIdSwEquipo(int idSwEquipo) {
        this.idSwEquipo = idSwEquipo;
    }

    public void setIdEquipo(int idEquipo) {
        this.idEquipo = idEquipo;
    }

    public void setSoftware(Software software) {
        this.software = software;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setEstado(EstadoSoftware estado) {
        this.estado = estado;
    }

    public void setIdTecnico(int idTecnico) {
        this.idTecnico = idTecnico;
    }

    public void setNombreTecnico(String nombreTecnico) {
        this.nombreTecnico = nombreTecnico;
    }

    public void setFechaAccion(Timestamp fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    public void setNotas(String notas) {
        this.notas = notas;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
