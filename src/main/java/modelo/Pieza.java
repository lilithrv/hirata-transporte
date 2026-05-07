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
public class Pieza {

    private int idPieza;
    private TipoPieza tipoPieza;      
    private String marca;
    private String modelo;
    private String descripcion;
    private int stockActual;
    private int stockMinimo;
    private Timestamp fechaRegistro;

    public Pieza() {
    }

    public Pieza(TipoPieza tipoPieza, String marca, String modelo, String descripcion, int stockActual, int stockMinimo) {
        this.tipoPieza = tipoPieza;
        this.marca = marca;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
    }

    public int getIdPieza() {
        return idPieza;
    }

    public TipoPieza getTipoPieza() {
        return tipoPieza;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getStockActual() {
        return stockActual;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setIdPieza(int idPieza) {
        this.idPieza = idPieza;
    }

    public void setTipoPieza(TipoPieza tipoPieza) {
        this.tipoPieza = tipoPieza;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // True si el stock bajó del mínimo — útil para alertas
    public boolean stockBajo() {
        return stockActual <= stockMinimo;
    }

    @Override
    public String toString() {
        return marca + " " + modelo + " (" + tipoPieza.getNombre() + ")";
    }
}
