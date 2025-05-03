/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parser;

import java.util.ArrayList;
import java.util.List;

public class AnalizadorLexico {

    private final String entrada;
    private final List<Token> tokens = new ArrayList<>();
    private final List<Token> errores = new ArrayList<>();

    private int posicion = 0;
    private int linea = 1;
    private int columna = 1;

    public AnalizadorLexico(String entrada) {
        this.entrada = entrada;
    }

    public void analizar() {
        while (!estaAlFinal()) {
            char actual = avanzar();

            // Saltar espacios en blanco
            if (Character.isWhitespace(actual)) {
                if (actual == '\n') {
                    linea++;
                    columna = 1;
                } else {
                    columna++;
                }
                continue;
            }

            // Ignorar líneas que comienzan con @ (errores intencionales o comentarios)
            if (actual == '@') {
                errores.add(new Token(TipoToken.ERROR, "@", linea, columna));
                columna++;
                continue;
            }

            // Reconocer símbolos simples
            switch (actual) {
                case '{':
                    agregarToken(TipoToken.LLAVE_IZQ, "{");
                    break;
                case '}':
                    agregarToken(TipoToken.LLAVE_DER, "}");
                    break;
                case '(':
                    agregarToken(TipoToken.PARENTESIS_IZQ, "(");
                    break;
                case ')':
                    agregarToken(TipoToken.PARENTESIS_DER, ")");
                    break;
                case ',':
                    agregarToken(TipoToken.COMA, ",");
                    break;
                case ':':
                    agregarToken(TipoToken.DOS_PUNTOS, ":");
                    break;
                case '"':
                    escanearCadena();
                    break;
                default:
                    if (Character.isLetter(actual)) {
                        escanearPalabra(actual);
                    } else if (Character.isDigit(actual)) {
                        escanearNumero(actual);
                    } else {
                        errores.add(new Token(TipoToken.ERROR, String.valueOf(actual), linea, columna));
                        columna++;
                    }
                    break;
            }
        }

        tokens.add(new Token(TipoToken.EOF, "", linea, columna));
    }

    // Métodos auxiliares aquí...
    // GETTERS
    public List<Token> getTokens() {
        return tokens;
    }

    public List<Token> getErrores() {
        return errores;
    }

    private char avanzar() {
        return entrada.charAt(posicion++);
    }

    private char peek() {
        if (estaAlFinal()) {
            return '\0';
        }
        return entrada.charAt(posicion);
    }

    private boolean estaAlFinal() {
        return posicion >= entrada.length();
    }

    private void agregarToken(TipoToken tipo, String lexema) {
        tokens.add(new Token(tipo, lexema, linea, columna));
        columna += lexema.length();
    }

    private void escanearCadena() {
        int inicioCol = columna;
        StringBuilder lexema = new StringBuilder("\"");

        while (!estaAlFinal()) {
            char c = peek();
            if (c == '\n') {
                break; // cortar si hay salto de línea
            }
            if (c == '"') {
                break;
            }

            lexema.append(avanzar());
            columna++;
        }

        if (estaAlFinal() || peek() == '\n') {
            errores.add(new Token(TipoToken.ERROR, lexema.toString(), linea, inicioCol));
            return;
        }

        lexema.append(avanzar()); // consume cierre "
        columna++;
        agregarToken(TipoToken.CADENA, lexema.toString());
    }

    private void escanearPalabra(char inicial) {
        StringBuilder palabra = new StringBuilder();
        palabra.append(inicial);

        int inicioCol = columna;

        while (!estaAlFinal() && (Character.isLetterOrDigit(peek()) || peek() == '_')) {
            palabra.append(avanzar());
            columna++;
        }

        String lex = palabra.toString();

        // Palabras clave
        switch (lex.toLowerCase()) {
            case "world":
                agregarToken(TipoToken.WORLD, lex);
                break;
            case "place":
                agregarToken(TipoToken.PLACE, lex);
                break;
            case "connect":
                agregarToken(TipoToken.CONNECT, lex);
                break;
            case "object":
                agregarToken(TipoToken.OBJECT, lex);
                break;
            case "to":
                agregarToken(TipoToken.TO, lex);
                break;
            case "with":
                agregarToken(TipoToken.WITH, lex);
                break;
            case "at":
                agregarToken(TipoToken.AT, lex);
                break;
            default:
                agregarToken(TipoToken.IDENTIFICADOR, lex);
                break;
        }
    }

    private void escanearNumero(char inicial) {
        StringBuilder numero = new StringBuilder();
        numero.append(inicial);

        int inicioCol = columna;

        while (!estaAlFinal() && Character.isDigit(peek())) {
            numero.append(avanzar());
            columna++;
        }

        agregarToken(TipoToken.NUMERO, numero.toString());
    }

}
