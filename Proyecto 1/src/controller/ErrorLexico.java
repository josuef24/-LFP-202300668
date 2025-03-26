/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 * Representa un error léxico encontrado durante el análisis
 */
public class ErrorLexico {
    private final String descripcion;
    private final int linea;
    private final int columna;
    private final String lexema;
    private final TipoError tipo;

    // Tipos de errores léxicos
    public enum TipoError {
        CARACTER_INVALIDO,
        CADENA_NO_TERMINADA,
        COMENTARIO_NO_TERMINADO,
        SIMBOLO_INESPERADO,
        ERROR_LEXICO_GENERAL
    }

    /**
     * Constructor completo
     * @param descripcion Mensaje descriptivo del error
     * @param linea Número de línea donde ocurrió el error
     * @param columna Número de columna donde ocurrió el error
     * @param lexema Lexema que causó el error
     * @param tipo Tipo de error léxico
     */
    public ErrorLexico(String descripcion, int linea, int columna, String lexema, TipoError tipo) {
        this.descripcion = descripcion;
        this.linea = linea;
        this.columna = columna;
        this.lexema = lexema;
        this.tipo = tipo;
    }

    /**
     * Constructor simplificado (asume ERROR_LEXICO_GENERAL)
     */
    public ErrorLexico(String descripcion, int linea, int columna, String lexema) {
        this(descripcion, linea, columna, lexema, TipoError.ERROR_LEXICO_GENERAL);
    }

    // Getters
    public String getDescripcion() {
        return descripcion;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }

    public String getLexema() {
        return lexema;
    }

    public TipoError getTipo() {
        return tipo;
    }

    /**
     * Representación del error en formato: [Línea X, Columna Y] Error: Descripción (Lexema)
     */
    @Override
    public String toString() {
        return String.format("[Línea %d, Columna %d] %s: %s (Lexema: '%s')",
                linea, columna, tipo.toString().replace('_', ' '), descripcion, lexema);
    }

    /**
     * Representación compacta para reportes
     */
    public String toReporteString() {
        return String.format("%-6d | %-6d | %-20s | %-50s | %-10s",
                linea, columna, lexema, descripcion, tipo);
    }

    /**
     * Encabezados para reportes en formato tabla
     */
    public static String getEncabezadoReporte() {
        return String.format("%-6s | %-6s | %-20s | %-50s | %-10s",
                "Línea", "Columna", "Lexema", "Descripción", "Tipo");
    }
}
