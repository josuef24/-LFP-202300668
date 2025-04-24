/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parser;

import models.*;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorSintactico {

    private final List<Token> tokens;
    private int posicion = 0;
    private final List<Mundo> mundos = new ArrayList<>();

    public AnalizadorSintactico(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void iniciar() {
        while (!esFin()) {
            Mundo mundo = analizarMundo();
            if (mundo != null) {
                mundos.add(mundo);
            }
        }
    }

    public List<Mundo> getMundos() {
        return mundos;
    }

    // ================================================================
    // METODO analizarMundo()
    private Mundo analizarMundo() {
        try {
            verificar(TipoToken.WORLD);
            String nombre = extraerLexema(TipoToken.CADENA).replace("\"", "");
            verificar(TipoToken.LLAVE_IZQ);

            Mundo mundo = new Mundo(nombre);

            while (!verificaActual(TipoToken.LLAVE_DER)) {
                if (verificaActual(TipoToken.PLACE)) {
                    mundo.lugares.add(analizarLugar());
                } else if (verificaActual(TipoToken.CONNECT)) {
                    mundo.conexiones.add(analizarConexion());
                } else if (verificaActual(TipoToken.OBJECT)) {
                    mundo.objetos.add(analizarObjeto());
                } else {
                    error("Token inesperado dentro de mundo: " + actual().lexema);
                    avanzar();
                }
            }

            verificar(TipoToken.LLAVE_DER);
            if (verificaActual(TipoToken.COMA)) {
                avanzar(); // mundo opcional separado por coma
            }
            return mundo;

        } catch (Exception e) {
            error("Error al analizar mundo: " + e.getMessage());
            sincronizar();
            return null;
        }
    }

    private Lugar analizarLugar() {
        verificar(TipoToken.PLACE);
        String nombre = extraerLexema(TipoToken.IDENTIFICADOR);
        verificar(TipoToken.DOS_PUNTOS);
        String tipo = extraerLexema(TipoToken.IDENTIFICADOR);
        verificar(TipoToken.AT);
        verificar(TipoToken.PARENTESIS_IZQ);
        int x = Integer.parseInt(extraerLexema(TipoToken.NUMERO));
        verificar(TipoToken.COMA);
        int y = Integer.parseInt(extraerLexema(TipoToken.NUMERO));
        verificar(TipoToken.PARENTESIS_DER);

        return new Lugar(nombre, tipo, x, y);
    }

    private Conexion analizarConexion() {
        verificar(TipoToken.CONNECT);
        String origen = extraerLexema(TipoToken.IDENTIFICADOR);
        verificar(TipoToken.TO);
        String destino = extraerLexema(TipoToken.IDENTIFICADOR);
        verificar(TipoToken.WITH);
        String tipo = extraerLexema(TipoToken.CADENA).replace("\"", "");

        return new Conexion(origen, destino, tipo);
    }

    private ObjetoEspecial analizarObjeto() {
        verificar(TipoToken.OBJECT);
        String nombre = extraerLexema(TipoToken.CADENA).replace("\"", "");
        verificar(TipoToken.DOS_PUNTOS);
        String tipo = extraerLexema(TipoToken.IDENTIFICADOR);
        verificar(TipoToken.AT);

        if (verificaActual(TipoToken.IDENTIFICADOR)) {
            String lugar = extraerLexema(TipoToken.IDENTIFICADOR);
            return new ObjetoEspecial(nombre, tipo, lugar);
        } else if (verificaActual(TipoToken.PARENTESIS_IZQ)) {
            verificar(TipoToken.PARENTESIS_IZQ);
            int x = Integer.parseInt(extraerLexema(TipoToken.NUMERO));
            verificar(TipoToken.COMA);
            int y = Integer.parseInt(extraerLexema(TipoToken.NUMERO));
            verificar(TipoToken.PARENTESIS_DER);
            return new ObjetoEspecial(nombre, tipo, x, y);
        } else {
            throw new RuntimeException("Se esperaba un lugar o coordenadas para el objeto.");
        }
    }

    // ================================================================
    // MÉTODOS DE AYUDA
    private Token actual() {
        return tokens.get(posicion);
    }

    private boolean esFin() {
        return actual().tipo == TipoToken.EOF;
    }

    private boolean verificaActual(TipoToken tipo) {
        return actual().tipo == tipo;
    }

    private void verificar(TipoToken tipo) {
        if (!verificaActual(tipo)) {
            throw new RuntimeException("Se esperaba token " + tipo + " pero se encontró " + actual().tipo);
        }
        avanzar();
    }

    private String extraerLexema(TipoToken tipo) {
        if (!verificaActual(tipo)) {
            throw new RuntimeException("Se esperaba token " + tipo + " pero se encontró " + actual().tipo);
        }
        String lexema = actual().lexema;
        avanzar();
        return lexema;
    }

    private void avanzar() {
        if (!esFin()) {
            posicion++;
        }
    }

    private void error(String mensaje) {
        System.err.println("Error sintáctico: " + mensaje);
    }

    private void sincronizar() {
        while (!esFin() && !verificaActual(TipoToken.WORLD)) {
            avanzar();
        }
    }
}
