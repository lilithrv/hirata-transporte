package modelo;

import java.util.Date;

public class Kilometraje {

    private int idKilometraje;
    private Usuario conductor;
    private Vehiculo vehiculo;
    private int kilometros;
    private Date fechaRegistro;

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

    public void setKilometros(int kilometros) {
        this.kilometros = kilometros;
    }

    public Date getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Date fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Kilometraje registrado: " + kilometros + " km";
    }
}
