/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

/**
 *
 * @author Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Leslie Reyes
 */
public class Usuario {

    private int idUsuario;
    private String nombreUsuario;
    private String email;
    private String password;
    private Rol rol;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombreUsuario, String email, Rol rol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.rol = rol;
    }

    public Usuario(int idUsuario, String nombreUsuario, String email, String password, Rol rol) {
        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        if (nombreUsuario == null) {
            throw new NullPointerException("El nombre no puede ser nulo");
        }

        if (nombreUsuario.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es requerido");
        }

        this.nombreUsuario = nombreUsuario;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new NullPointerException("El correo no puede ser nulo");
        }

        if (email.trim().isEmpty()) {
            throw new IllegalArgumentException("El correo es requerido");
        }

        this.email = email;
    }

    public void setPassword(String password) {
        if (password == null) {
            throw new NullPointerException("La contraseña no puede ser nula");
        }

        if (password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es requerida");
        }
        this.password = password;
    }

    public void setRol(Rol rol) {
        if (rol == null) {
            throw new IllegalArgumentException("El rol es requerido.");
        }
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idUsuario=" + idUsuario + ", nombreUsuario=" + nombreUsuario + ", email=" + email + ", password=" + password + ", rol=" + rol + '}';
    }

}
