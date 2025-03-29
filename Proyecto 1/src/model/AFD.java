/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.util.*;
import java.util.stream.Collectors;

public class AFD {
    private final String nombre;
    private String descripcion;
    private final Set<String> estados;
    private final Set<String> alfabeto;
    private String estadoInicial;
    private final Set<String> estadosFinales;
    private final Map<String, Map<String, String>> transiciones;

    public AFD(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre del AFD no puede ser nulo");
        this.estados = new LinkedHashSet<>(); // Mantiene orden de inserción
        this.alfabeto = new LinkedHashSet<>();
        this.estadosFinales = new LinkedHashSet<>();
        this.transiciones = new LinkedHashMap<>();
    }

    // Métodos de acceso
    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(String estadoInicial) {
        if (estadoInicial != null && !estados.contains(estadoInicial)) {
            throw new IllegalArgumentException("El estado inicial debe existir en el conjunto de estados");
        }
        this.estadoInicial = estadoInicial;
    }

    public Set<String> getEstados() {
        return Collections.unmodifiableSet(estados);
    }

    public void agregarEstado(String estado) {
        Objects.requireNonNull(estado, "El estado no puede ser nulo");
        estados.add(estado);
    }

    public Set<String> getAlfabeto() {
        return Collections.unmodifiableSet(alfabeto);
    }

    public void agregarSimbolo(String simbolo) {
        Objects.requireNonNull(simbolo, "El símbolo no puede ser nulo");
        alfabeto.add(simbolo);
    }

    public Set<String> getEstadosFinales() {
        return Collections.unmodifiableSet(estadosFinales);
    }

    public void agregarFinal(String estado) {
        Objects.requireNonNull(estado, "El estado final no puede ser nulo");
        if (!estados.contains(estado)) {
            throw new IllegalArgumentException("El estado final debe existir en el conjunto de estados");
        }
        estadosFinales.add(estado);
    }

    public Map<String, Map<String, String>> getTransiciones() {
    return Collections.unmodifiableMap(
        transiciones.entrySet().stream()
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> Collections.unmodifiableMap(e.getValue())
            ))  
        )      
    ;          
}

    public void agregarTransicion(String desde, String simbolo, String hacia) {
        Objects.requireNonNull(desde, "El estado origen no puede ser nulo");
        Objects.requireNonNull(simbolo, "El símbolo no puede ser nulo");
        Objects.requireNonNull(hacia, "El estado destino no puede ser nulo");

        if (!estados.contains(desde)) {
            throw new IllegalArgumentException("Estado origen no existe: " + desde);
        }
        if (!estados.contains(hacia)) {
            throw new IllegalArgumentException("Estado destino no existe: " + hacia);
        }
        if (!alfabeto.contains(simbolo)) {
            throw new IllegalArgumentException("Símbolo no existe en el alfabeto: " + simbolo);
        }

        transiciones.putIfAbsent(desde, new LinkedHashMap<>());
        
        // Verificar que no exista ya una transición para este símbolo
        if (transiciones.get(desde).containsKey(simbolo)) {
            throw new IllegalStateException(String.format(
                "Ya existe una transición para el símbolo '%s' desde el estado '%s'", 
                simbolo, desde));
        }
        
        transiciones.get(desde).put(simbolo, hacia);
    }

    public String getSiguienteEstado(String estadoActual, String simbolo) {
        Objects.requireNonNull(estadoActual, "El estado actual no puede ser nulo");
        Objects.requireNonNull(simbolo, "El símbolo no puede ser nulo");

        if (!transiciones.containsKey(estadoActual)) {
            return null;
        }
        return transiciones.get(estadoActual).get(simbolo);
    }

    /**
     * Valida la estructura completa del AFD
     * @return true si el AFD está correctamente definido
     * @throws IllegalStateException si hay problemas en la definición
     */
    public boolean validar() throws IllegalStateException {
        List<String> errores = new ArrayList<>();

        if (estadoInicial == null) {
            errores.add("No se ha definido un estado inicial");
        } else if (!estados.contains(estadoInicial)) {
            errores.add("El estado inicial no existe en el conjunto de estados");
        }

        for (String estadoFinal : estadosFinales) {
            if (!estados.contains(estadoFinal)) {
                errores.add("El estado final '" + estadoFinal + "' no existe en el conjunto de estados");
            }
        }

        for (String estado : estados) {
            if (!transiciones.containsKey(estado) && !estadosFinales.contains(estado)) {
                errores.add("No hay transiciones definidas para el estado '" + estado + "'");
            }
        }

        if (!errores.isEmpty()) {
            throw new IllegalStateException("AFD inválido:\n" + 
                errores.stream().collect(Collectors.joining("\n")));
        }

        return true;
    }

    /**
     * Representación textual del AFD
     */
    @Override
    public String toString() {
        return String.format(
            "AFD '%s'%n" +
            "Descripción: %s%n" +
            "Estados: %s%n" +
            "Alfabeto: %s%n" +
            "Estado inicial: %s%n" +
            "Estados finales: %s%n" +
            "Transiciones: %s",
            nombre,
            descripcion != null ? descripcion : "Sin descripción",
            estados,
            alfabeto,
            estadoInicial != null ? estadoInicial : "No definido",
            estadosFinales,
            transiciones
        );
    }
}