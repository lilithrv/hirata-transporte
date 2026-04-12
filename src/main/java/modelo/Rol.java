/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class Rol {

    private int idRol;
    private String nombreRol;

    public Rol() {
    }

    public Rol(int idRol, String nombreRol) {
        this.idRol = idRol;
        this.nombreRol = nombreRol;
    }

    public int getIdRol() {
        return idRol;
    }

    public String getNombreRol() {
        return nombreRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    public void setNombreRol(String nombreRol) {
        if (nombreRol== null) {
            throw new NullPointerException("El nombre no puede ser nulo");
        }

        if (nombreRol.trim().isEmpty()) {
            throw new IllegalArgumentException("El nomnbre es requerido");
        }

        if (nombreRol.length() < 3 || nombreRol.length() > 30) {
            throw new IllegalArgumentException("ERROR: El rol no cumple con el rango (3- 30 caracteres): " + nombreRol);
        }
        this.nombreRol = nombreRol;
    }

    @Override
    public String toString() {
        return "Rol{" + "idRol=" + idRol + ", nombreRol=" + nombreRol + '}';
    }

}
