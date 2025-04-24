/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

public class ObjetoEspecial {

    public String nombre;
    public String tipo;
    public String lugar;
    public int x = -1;
    public int y = -1;

    // Constructor para objeto en lugar
    public ObjetoEspecial(String nombre, String tipo, String lugar) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.lugar = lugar;
    }

    // Constructor para objeto por coordenadas
    public ObjetoEspecial(String nombre, String tipo, int x, int y) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.x = x;
        this.y = y;
    }
}
