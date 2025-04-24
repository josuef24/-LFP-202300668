/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.ArrayList;
import java.util.List;

public class Mundo {

    public String nombre;
    public List<Lugar> lugares = new ArrayList<>();
    public List<Conexion> conexiones = new ArrayList<>();
    public List<ObjetoEspecial> objetos = new ArrayList<>();

    public Mundo(String nombre) {
        this.nombre = nombre;
    }
}
