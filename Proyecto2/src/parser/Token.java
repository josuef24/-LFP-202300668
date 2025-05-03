/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parser;

public class Token {
    public TipoToken tipo;
    public String lexema;
    public int linea;
    public int columna;

    public Token(TipoToken tipo, String lexema, int linea, int columna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }

    @Override
    public String toString() {
        return tipo + " → \"" + lexema + "\" (línea " + linea + ", col " + columna + ")";
    }
}




