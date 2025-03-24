/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.*;

/**
 *
 * @author jmfuente
 */
public class AFD {
    private String nombre;
    private String descripcion;
    private Set<String> estados;
    private Set<String> alfabeto;
    private String estadoInicial;
    private Set<String> estadosFinales;
    private Map<String, Map<String, String>> transiciones;

    public AFD(String nombre) {
        this.nombre = nombre;
        this.estados = new HashSet<>();
        this.alfabeto = new HashSet<>();
        this.estadosFinales = new HashSet<>();
        this.transiciones = new HashMap<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setEstadoInicial(String estadoInicial) {
        this.estadoInicial = estadoInicial;
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public void agregarEstado(String estado) {
        estados.add(estado);
    }

    public Set<String> getEstados() {
        return estados;
    }

    public void agregarSimbolo(String simbolo) {
        alfabeto.add(simbolo);
    }

    public Set<String> getAlfabeto() {
        return alfabeto;
    }

    public void agregarFinal(String estado) {
        estadosFinales.add(estado);
    }

    public Set<String> getEstadosFinales() {
        return estadosFinales;
    }

    public void agregarTransicion(String desde, String simbolo, String hacia) {
        transiciones.putIfAbsent(desde, new HashMap<>());
        transiciones.get(desde).put(simbolo, hacia);
    }

    public Map<String, Map<String, String>> getTransiciones() {
        return transiciones;
    }

    public String getSiguienteEstado(String estadoActual, String simbolo) {
        if (transiciones.containsKey(estadoActual)) {
            return transiciones.get(estadoActual).get(simbolo);
        }
        return null;
    }
    
}
