/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author leslie
 */
public class TipoSoftware {

    private int idTipoSoftware;
    private String nombre;

    public TipoSoftware() {
    }

    public TipoSoftware(int idTipoSoftware, String nombre) {
        this.idTipoSoftware = idTipoSoftware;
        this.nombre = nombre;
    }

    public int getIdTipoSoftware() {
        return idTipoSoftware;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIdTipoSoftware(int idTipoSoftware) {
        this.idTipoSoftware = idTipoSoftware;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
