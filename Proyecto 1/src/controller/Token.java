/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.Objects;

/**
 * Representa un token identificado durante el análisis léxico
 */
public class Token {
    // Tipos de tokens posibles
    public enum TipoToken {
        IDENTIFICADOR,
        PALABRA_RESERVADA,
        SIMBOLO,
        CADENA,
        NUMERO,
        COMENTARIO,
        DESCONOCIDO,
        FLECHA
    }

    private final TipoToken tipo;
    private final String lexema;
    private final int linea;
    private final int columna;

    /**
     * Constructor completo
     * @param tipo Tipo del token (usar enum TipoToken)
     * @param lexema Texto del token
     * @param linea Número de línea donde se encontró
     * @param columna Número de columna donde se encontró
     */
    public Token(TipoToken tipo, String lexema, int linea, int columna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }

    /**
     * Constructor alternativo para compatibilidad
     */
    public Token(String tipo, String lexema, int linea, int columna) {
        this(TipoToken.valueOf(tipo.toUpperCase()), lexema, linea, columna);
    }

    // Getters
    public TipoToken getTipo() {
        return tipo;
    }

    public String getLexema() {
        return lexema;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    /**
     * Representación para reportes
     */
    public String toReporteString() {
        return String.format("%-15s | %-20s | %-6d | %-6d", 
                tipo, 
                truncarLexema(lexema), 
                linea, 
                columna);
    }

    /**
     * Encabezado para reportes en tabla
     */
    public static String getEncabezadoReporte() {
        return String.format("%-15s | %-20s | %-6s | %-6s",
                "Tipo", "Lexema", "Línea", "Columna");
    }

    @Override
    public String toString() {
        return String.format("[%s] '%s' (Línea %d, Columna %d)",
                tipo, truncarLexema(lexema), linea, columna);
    }

    /**
     * Trunca lexemas largos para mejor visualización
     */
    private String truncarLexema(String lexema) {
        final int MAX_LENGTH = 20;
        if (lexema.length() > MAX_LENGTH) {
            return lexema.substring(0, MAX_LENGTH - 3) + "...";
        }
        return lexema;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Token other = (Token) obj;
        return linea == other.linea && 
               columna == other.columna &&
               tipo == other.tipo &&
               lexema.equals(other.lexema);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tipo, lexema, linea, columna);
    }
}
