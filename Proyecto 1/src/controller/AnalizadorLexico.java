/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Analizador léxico para procesar archivos .lfp con definiciones de AFD
 */
public class AnalizadorLexico {

    private final String input;
    private int posicion;
    private int linea;
    private int columna;
    private final List<Token> tokens;
    private final List<ErrorLexico> errores;

    // Tipos de tokens (usando enum)
    public enum TipoToken {
        SIMBOLO,
        IDENTIFICADOR,
        CADENA,
        NUMERO,
        PALABRA_RESERVADA,
        FLECHA
    }

    // Palabras reservadas del lenguaje AFD
    private static final String[] PALABRAS_RESERVADAS = {
        "descripcion", "estados", "alfabeto", "inicial", "finales", "transiciones"
    };

    public AnalizadorLexico(String input) {
        this.input = input;
        this.tokens = new ArrayList<>();
        this.errores = new ArrayList<>();
        this.linea = 1;
        this.columna = 1;
    }

    /**
     * Realiza el análisis léxico del input
     */
    public void analizar() {
        while (posicion < input.length()) {
            char c = input.charAt(posicion);

            if (Character.isWhitespace(c)) {
                manejarEspacios();
                continue;
            }

            switch (c) {
                case '-':
                    if (posicion + 1 < input.length() && input.charAt(posicion + 1) == '>') {
                        agregarToken(TipoToken.FLECHA, "->");
                        avanzar(2); // Avanza dos posiciones por '->'
                    } else {
                        agregarError("Carácter no válido: '-'");
                        avanzar();
                    }
                    break;

                case '{':
                case '}':
                case '[':
                case ']':
                case ',':
                case ':':
                case '=':
                case '(':
                case ')':
                    agregarToken(TipoToken.SIMBOLO, String.valueOf(c));
                    avanzar();
                    break;

                case '"':
                    procesarCadena();
                    break;

                case '/':
                    procesarPosibleComentario();
                    break;

                default:
                    if (Character.isLetter(c)) {
                        procesarIdentificador();
                    } else if (Character.isDigit(c)) {
                        procesarNumero();
                    } else {
                        agregarError("Carácter no válido: '" + c + "'");
                        avanzar();
                    }
            }
        }
    }

    /**
     * Procesa un identificador (nombres de AFD, estados, etc.)
     */
    private void procesarIdentificador() {
        int inicio = posicion;
        while (posicion < input.length()
                && (Character.isLetterOrDigit(input.charAt(posicion))
                || input.charAt(posicion) == '_')) {
            avanzar();
        }

        String lexema = input.substring(inicio, posicion);
        TipoToken tipo = esPalabraReservada(lexema) ? TipoToken.PALABRA_RESERVADA : TipoToken.IDENTIFICADOR;
        agregarToken(tipo, lexema);
    }

    /**
     * Procesa una cadena entre comillas
     */
    private void procesarCadena() {
        int inicioLinea = linea;
        int inicioColumna = columna;
        int inicio = posicion;

        avanzar(); // Saltar comilla inicial

        StringBuilder sb = new StringBuilder();
        boolean cerrada = false;

        while (posicion < input.length()) {
            char c = input.charAt(posicion);

            if (c == '"') {
                cerrada = true;
                avanzar(); // Saltar comilla final
                break;
            } else {
                sb.append(c);
                avanzar();
            }
        }

        if (cerrada) {
            String lexema = input.substring(inicio, posicion); // Incluye comillas
            tokens.add(new Token(TipoToken.CADENA.name(), lexema, inicioLinea, inicioColumna));
        } else {
            agregarError("Cadena no terminada");
        }

    }

    /**
     * Procesa números (para posiciones o valores numéricos)
     */
    private void procesarNumero() {
        int inicio = posicion;
        while (posicion < input.length() && Character.isDigit(input.charAt(posicion))) {
            avanzar();
        }
        agregarToken(TipoToken.NUMERO, input.substring(inicio, posicion));
    }

    /**
     * Procesa comentarios (// o /*)
     */
    private void procesarPosibleComentario() {
        avanzar(); // Saltar el primer /
        if (posicion >= input.length()) {
            agregarError("Símbolo '/' no válido al final del archivo");
            return;
        }

        char c = input.charAt(posicion);
        if (c == '/') {
            // Comentario de línea
            while (posicion < input.length() && input.charAt(posicion) != '\n') {
                avanzar();
            }
        } else if (c == '*') {
            // Comentario multilínea
            boolean terminado = false;
            avanzar();
            while (posicion < input.length() - 1 && !terminado) {
                if (input.charAt(posicion) == '*' && input.charAt(posicion + 1) == '/') {
                    terminado = true;
                    avanzar(2); // Saltar */
                } else {
                    avanzar();
                }
            }
            if (!terminado) {
                agregarError("Comentario multilínea no terminado");
            }
        } else {
            agregarError("Símbolo '/' no válido");
        }
    }

    /**
     * Maneja espacios, tabs y saltos de línea
     */
    private void manejarEspacios() {
        while (posicion < input.length() && Character.isWhitespace(input.charAt(posicion))) {
            if (input.charAt(posicion) == '\n') {
                linea++;
                columna = 1;
            } else {
                columna++;
            }
            posicion++;
        }
    }

    /**
     * Avanza la posición actual
     */
    private void avanzar() {
        posicion++;
        columna++;
    }

    /**
     * Avanza la posición actual n veces
     */
    private void avanzar(int n) {
        for (int i = 0; i < n; i++) {
            avanzar();
        }
    }

    /**
     * Verifica si un lexema es palabra reservada
     */
    private boolean esPalabraReservada(String lexema) {
        for (String reservada : PALABRAS_RESERVADAS) {
            if (reservada.equalsIgnoreCase(lexema)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Agrega un token a la lista
     */
    private void agregarToken(TipoToken tipo, String lexema) {
        tokens.add(new Token(tipo.name(), lexema, linea, columna));
        columna += lexema.length();
        posicion += lexema.length();
    }

    private void agregarToken(TipoToken tipo, String lexema, int linea, int columna) {
        tokens.add(new Token(tipo.name(), lexema, linea, columna));
    }

    /**
     * Agrega un error a la lista
     */
    private void agregarError(String mensaje) {
        errores.add(new ErrorLexico(
                mensaje,
                linea,
                columna,
                posicion < input.length() ? String.valueOf(input.charAt(posicion)) : "EOF"
        ));
        avanzar();
    }

    // Getters
    public List<Token> getTokens() {
        return new ArrayList<>(tokens);
    }

    public List<ErrorLexico> getErrores() {
        return new ArrayList<>(errores);
    }
}
