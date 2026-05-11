/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author leslie
 */
public class TipoEquipo {

    private int idTipoEquipo;
    private String nombre;

    public TipoEquipo() {
    }

    public TipoEquipo(int idTipoEquipo, String nombre) {
        this.idTipoEquipo = idTipoEquipo;
        this.nombre = nombre;
    }

    public int getIdTipoEquipo() {
        return idTipoEquipo;
    }

    public void setIdTipoEquipo(int id) {
        this.idTipoEquipo = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
