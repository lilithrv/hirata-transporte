package modelo;

import java.util.Date;

public class Kilometraje {

    private int idKilometraje;
    private Usuario conductor;
    private Vehiculo vehiculo;
    private int kilometros;
    private Date fechaRegistro;
    private String direccionOrigen;
    private String direccionTermino;

    // constructor vacio
    public Kilometraje() {
    }

    // constructor con datos
    public Kilometraje(int idKilometraje, Usuario conductor, Vehiculo vehiculo, int kilometros, Date fechaRegistro) {
        this.idKilometraje = idKilometraje;
        this.conductor = conductor;
        this.vehiculo = vehiculo;
        this.kilometros = kilometros;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdKilometraje() {
        return idKilometraje;
    }

    public void setIdKilometraje(int idKilometraje) {
        this.idKilometraje = idKilometraje;
    }

    public Usuario getConductor() {
        return conductor;
    }

    public void setConductor(Usuario conductor) {
        this.conductor = conductor;
    }

    public Vehiculo getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(Vehiculo vehiculo) {
        this.vehiculo = vehiculo;
    }

    public int getKilometros() {
        return kilometros;
    }

    public String getDireccionOrigen() {
        return direccionOrigen;
    }

    public String getDireccionTermino() {
        return direccionTermino;
    }
  
    public void setKilometros(int kilometros) {
        if (kilometros < 0) {
            throw new IllegalArgumentException("ERROR: El kilometraje no puede ser menor a cero.");
        }
        this.kilometros = kilometros;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public void setDireccionOrigen(String direccionOrigen) {
        this.direccionOrigen = direccionOrigen;
    }

    public void setDireccionTermino(String direccionTermino) {
        this.direccionTermino = direccionTermino;
    }

    @Override
    public String toString() {
        return "Kilometraje registrado: " + kilometros + " km";
    }
}
