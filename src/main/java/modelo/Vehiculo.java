/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import java.time.Year;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class Vehiculo {

    private int idVehiculo;
    private int idConductor;
    private String patente;
    private String marca;
    private String modelo;
    private int anio;
    private int kilometraje;

    public Vehiculo(int idVehiculo, int idConductor, String patente, String marca, String modelo, int anio, int kilometraje) {
        this.idVehiculo = idVehiculo;
        this.idConductor = idConductor;
        this.patente = patente;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.kilometraje = kilometraje;
    }

    public Vehiculo() {
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public int getIdConductor() {
        return idConductor;
    }

    public String getPatente() {
        return patente;
    }

    public String getMarca() {
        return marca;
    }

    public String getModelo() {
        return modelo;
    }

    public int getAnio() {
        return anio;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setIdVehiculo(int idVehiculo) {
        if (idVehiculo <= 0) {
            throw new IllegalArgumentException("ERROR INTERNO: El ID del vehículo debe ser mayor a 0.");
        }
        this.idVehiculo = idVehiculo;
    }

    public void setIdConductor(int idConductor) {
        if (idConductor >= 0) {
            throw new IllegalArgumentException("ERROR INTERNO: El ID del conductor debe ser mayor a 0.");
        }
        this.idConductor = idConductor;
    }

    public void setPatente(String patente) {
        if (patente == null || patente.trim().isEmpty()) {
            throw new IllegalArgumentException("ERROR: La patente del vehículo es obligatoria.");
        }

        String patenteLimpia = patente.trim().toUpperCase().replace("-", "").replace(" ", "");

        // Formato patente antigua hasta 2007: 2 letras y 4 números
        String regexAntigua = "^[A-Z]{2}[0-9]{4}$";

        //Formato patente 2007 - 2025: 4 letras y 2 números
        String regexModerna = "^[BCDFGHJKLPRSTVWXYZ]{4}[0-9]{2}$";

        //Formato patente nueva: 5 letras y 1 número 
        String regexNueva = "^[BCDFGHJKLPRSTVWXYZ]{5}[0-9]{1}$";

        if (!patenteLimpia.matches(regexAntigua)
                && !patenteLimpia.matches(regexModerna)
                && !patenteLimpia.matches(regexNueva)) {

            throw new IllegalArgumentException("ERROR: La marca del vehículo es obligatoria");
        }

        this.patente = patenteLimpia;
    }

    public void setMarca(String marca) {
        if (marca == null || marca.trim().isEmpty()) {
            throw new IllegalArgumentException("ERROR: La patente del vehículo es obligatoria.");
        }

        String marcaLimpia = marca.trim().toUpperCase();

        if (marcaLimpia.length() > 50) {
            throw new IllegalArgumentException("ERROR: La marca no puede tener más de 50 caracteres.");
        }

        if (!marcaLimpia.matches("^[A-ZÁÉÍÓÚÑ\\s\\-]+$")) {
            throw new IllegalArgumentException("ERROR: La marca solo de contener letras, espacios o guiones.");
        }

        this.marca = marcaLimpia;
    }

    public void setModelo(String modelo) {
        if (modelo == null || modelo.trim().isEmpty()) {
            throw new IllegalArgumentException("ERROR: El modelo del vehículo es obligatorio.");
        }

        String modeloLimpia = modelo.trim().toUpperCase();

        if (modeloLimpia.length() > 50) {
            throw new IllegalArgumentException("ERROR: El modelo del vehículo no puede tener más de 50 caracteres.");
        }

        if (!modeloLimpia.matches("^[A-Z0-9ÁÉÍÓÚÑ\\s\\-]+$")) {
            throw new IllegalArgumentException("ERROR: El modelo del vehículo solo de contener letras, números, espacios o guiones.");
        }

        this.modelo = modeloLimpia;
    }

    public void setAnio(int anio) {

        int anioActual = Year.now().getValue();

        if (anio < 1980) {
            throw new IllegalArgumentException("ERROR: El año del vehículo es demasiado antiguo o inválido.");
        }

        if (anio > anioActual + 1) {
            throw new IllegalArgumentException("ERROR: Ingresa un año válido.");
        }

        this.anio = anio;
    }

    public void setKilometraje(int kilometraje) {
        if(kilometraje < 0){
            throw new IllegalArgumentException("ERROR: El kilometraje no puede ser menor a cero.");
        }
        
        this.kilometraje = kilometraje;
    }

    @Override
    public String toString() {
        return "Vehiculo{" + "idVehiculo=" + idVehiculo + ", idConductor=" + idConductor + ", patente=" + patente + ", marca=" + marca + ", modelo=" + modelo + ", anio=" + anio + ", kilometraje=" + kilometraje + '}';
    }

} // Class
