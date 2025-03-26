/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.Objects;

/**
 * Representa una transición entre estados en un Autómata Finito Determinista (AFD)
 * @author jmfuente
 */
public class Transicion {
    private final String estadoOrigen;
    private final String simbolo;
    private final String estadoDestino;

    /**
     * Constructor principal
     * @param estadoOrigen Estado de origen de la transición
     * @param simbolo Símbolo que activa la transición
     * @param estadoDestino Estado destino de la transición
     * @throws IllegalArgumentException Si alguno de los parámetros es nulo o vacío
     */
    public Transicion(String estadoOrigen, String simbolo, String estadoDestino) {
        if (estadoOrigen == null || estadoOrigen.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado origen no puede ser nulo o vacío");
        }
        if (simbolo == null || simbolo.trim().isEmpty()) {
            throw new IllegalArgumentException("El símbolo no puede ser nulo o vacío");
        }
        if (estadoDestino == null || estadoDestino.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado destino no puede ser nulo o vacío");
        }
        
        this.estadoOrigen = estadoOrigen;
        this.simbolo = simbolo;
        this.estadoDestino = estadoDestino;
    }

    // Getters
    public String getEstadoOrigen() {
        return estadoOrigen;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public String getEstadoDestino() {
        return estadoDestino;
    }

    /**
     * Representación textual de la transición
     */
    @Override
    public String toString() {
        return String.format("%s --%s--> %s", estadoOrigen, simbolo, estadoDestino);
    }

    /**
     * Comparación basada en estado origen, símbolo y estado destino
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transicion that = (Transicion) obj;
        return estadoOrigen.equals(that.estadoOrigen) &&
               simbolo.equals(that.simbolo) &&
               estadoDestino.equals(that.estadoDestino);
    }

    /**
     * Hash code basado en estado origen, símbolo y estado destino
     */
    @Override
    public int hashCode() {
        return Objects.hash(estadoOrigen, simbolo, estadoDestino);
    }

    /**
     * Representación formal para archivos .lfp
     */
    public String toFormalString() {
        return String.format("\"%s\" -> %s", simbolo, estadoDestino);
    }
}
