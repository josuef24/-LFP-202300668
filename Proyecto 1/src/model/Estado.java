/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 * Representa un estado en un Autómata Finito Determinista (AFD)
 * @author jmfuente
 */
public class Estado {
    private final String nombre;
    private boolean esFinal;
    private boolean esInicial;

    /**
     * Constructor principal
     * @param nombre Nombre identificador del estado (no puede ser nulo o vacío)
     */
    public Estado(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del estado no puede ser nulo o vacío");
        }
        this.nombre = nombre;
        this.esFinal = false;
        this.esInicial = false;
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public boolean esFinal() {
        return esFinal;
    }

    public boolean esInicial() {
        return esInicial;
    }

    // Setters con validación
    public void setEsFinal(boolean esFinal) {
        if (this.esInicial && esFinal) {
            throw new IllegalStateException("Un estado no puede ser inicial y final simultáneamente");
        }
        this.esFinal = esFinal;
    }

    public void setEsInicial(boolean esInicial) {
        if (this.esFinal && esInicial) {
            throw new IllegalStateException("Un estado no puede ser final e inicial simultáneamente");
        }
        this.esInicial = esInicial;
    }

    /**
     * Representación textual del estado
     */
    @Override
    public String toString() {
        return String.format("Estado{nombre='%s', inicial=%s, final=%s}", 
               nombre, esInicial, esFinal);
    }

    /**
     * Comparación basada en el nombre del estado
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Estado estado = (Estado) obj;
        return nombre.equals(estado.nombre);
    }

    /**
     * Hash code basado en el nombre del estado
     */
    @Override
    public int hashCode() {
        return nombre.hashCode();
    }
}
