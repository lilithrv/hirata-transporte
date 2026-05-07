/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author leslie
 */
public class TipoPieza {

    private int idTipoPieza;
    private String nombre;

    public TipoPieza() {
    }

    public TipoPieza(int idTipoPieza, String nombre) {
        this.idTipoPieza = idTipoPieza;
        this.nombre = nombre;
    }

    public int getIdTipoPieza() {
        return idTipoPieza;
    }

    public String getNombre() {
        return nombre;
    }

    public void setIdTipoPieza(int idTipoPieza) {
        this.idTipoPieza = idTipoPieza;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
