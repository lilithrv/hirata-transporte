/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

import modelo.Usuario;

/**
 *
 * Author:  Jonathan Fuentealba, Gustavo Gallegos, Rodolfo Guerrero, Rodolfo Guerrero, Leslie Reyes
 */

public class Sesion {

    private static Usuario usuarioActivo;

    public static Usuario getUsuarioActivo() {
        return usuarioActivo;
    }

    public static void setUsuarioActivo(Usuario u) {
        usuarioActivo = u;
    }
}
