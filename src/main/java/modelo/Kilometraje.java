
package modelo;

import java.util.Date;


public class Kilometraje {
    
    private int idKilometraje;
    private int idConductor;
    private int idVehiculo;
    private int kilometros;
    private Date fechaRegistro;

    // constructor vacio
    public Kilometraje() {
    }

    // constructor con datos
    public Kilometraje(int idKilometraje, int idConductor, int idVehiculo, int kilometros, Date fechaRegistro) {
        this.idKilometraje = idKilometraje;
        this.idConductor = idConductor;
        this.idVehiculo = idVehiculo;
        this.kilometros = kilometros;
        this.fechaRegistro = fechaRegistro;
    }

    
    public int getIdKilometraje() {
        return idKilometraje;
    }

    public void setIdKilometraje(int idKilometraje) {
        this.idKilometraje = idKilometraje;
    }

    public int getIdConductor() {
        return idConductor;
    }

    public void setIdConductor(int idConductor) {
        this.idConductor = idConductor;
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
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