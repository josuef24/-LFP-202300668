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
    private final List<Token> erroresSintacticos = new ArrayList<>();
    private int posicion = 0;
    private final List<Mundo> mundos = new ArrayList<>();

    public AnalizadorSintactico(List<Token> tokens) {
        this.tokens = tokens;
    }

    public void iniciar() {
        while (!esFin()) {
            if (verificaActual(TipoToken.WORLD)) {
                Mundo mundo = analizarMundo();
                if (mundo != null) {
                    mundos.add(mundo);
                }
            } else {
                error("Se esperaba 'world' pero se encontró: " + actual().tipo);
                sincronizar();
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

            while (!verificaActual(TipoToken.LLAVE_DER) && !esFin()) {
                if (verificaActual(TipoToken.PLACE)) {
                    Lugar lugar = analizarLugar();
                    if (lugar != null) {
                        mundo.lugares.add(lugar);
                    }
                } else if (verificaActual(TipoToken.CONNECT)) {
                    Conexion conexion = analizarConexion();
                    if (conexion != null) {
                        mundo.conexiones.add(conexion);
                    }
                } else if (verificaActual(TipoToken.OBJECT)) {
                    ObjetoEspecial objeto = analizarObjeto();
                    if (objeto != null) {
                        mundo.objetos.add(objeto);
                    }
                } else {
                    if (verificaActual(TipoToken.CADENA)) {
                        error("Se esperaba palabra reservada 'object' antes de la cadena");
                    } else {
                        error("Token inesperado dentro de mundo: " + actual().lexema);
                    }
                    sincronizarSentencia();

                }
            }

            if (!verificaActual(TipoToken.LLAVE_DER)) {
                error("Se esperaba '}' al final del mundo, pero no se encontró");
                sincronizar();
                return null;
            }
            verificar(TipoToken.LLAVE_DER);

            if (verificaActual(TipoToken.COMA)) {
                avanzar(); // coma opcional después de }
            }
            return mundo;

        } catch (Exception e) {
            error("Error al analizar mundo: " + e.getMessage());
            sincronizar();
            return null;
        }
    }

    private void sincronizarSentencia() {
        while (!esFin()) {
            TipoToken tipo = actual().tipo;

            // Nos detenemos si encontramos algo que puede ser el inicio de una nueva sentencia o bloque
            if (tipo == TipoToken.PLACE || tipo == TipoToken.CONNECT || tipo == TipoToken.OBJECT
                    || tipo == TipoToken.LLAVE_DER || tipo == TipoToken.WORLD) {
                return;
            }

            avanzar(); // seguimos avanzando tokens basura
        }
    }

    private Lugar analizarLugar() {
        verificar(TipoToken.PLACE);

        // NOMBRE del lugar
        if (!verificaActual(TipoToken.IDENTIFICADOR)) {
            error("Se esperaba IDENTIFICADOR para el nombre del lugar");
            sincronizarSentencia();
            return null;
        }
        String nombre = extraerLexema(TipoToken.IDENTIFICADOR);

        // :
        if (!verificaActual(TipoToken.DOS_PUNTOS)) {
            error("Se esperaba ':' después del nombre del lugar");
            sincronizarSentencia();
            return null;
        }
        verificar(TipoToken.DOS_PUNTOS);

        // TIPO del lugar
        if (!verificaActual(TipoToken.IDENTIFICADOR)) {
            error("Se esperaba IDENTIFICADOR para el tipo del lugar");
            sincronizarSentencia();
            return null;
        }
        String tipo = extraerLexema(TipoToken.IDENTIFICADOR);

        // at
        if (!verificaActual(TipoToken.AT)) {
            error("Se esperaba 'at' después del tipo del lugar");
            sincronizarSentencia();
            return null;
        }
        verificar(TipoToken.AT);

        // Coordenadas
        if (!verificaActual(TipoToken.PARENTESIS_IZQ)) {
            error("Se esperaba '(' para coordenadas del lugar");
            sincronizarSentencia();
            return null;
        }
        verificar(TipoToken.PARENTESIS_IZQ);

        if (!verificaActual(TipoToken.NUMERO)) {
            error("Se esperaba número X de coordenada del lugar");
            sincronizarSentencia();
            return null;
        }
        int x = Integer.parseInt(extraerLexema(TipoToken.NUMERO));

        if (!verificaActual(TipoToken.COMA)) {
            error("Se esperaba ',' entre coordenadas del lugar");
            sincronizarSentencia();
            return null;
        }
        verificar(TipoToken.COMA);

        if (!verificaActual(TipoToken.NUMERO)) {
            error("Se esperaba número Y de coordenada del lugar");
            sincronizarSentencia();
            return null;
        }
        int y = Integer.parseInt(extraerLexema(TipoToken.NUMERO));

        if (!verificaActual(TipoToken.PARENTESIS_DER)) {
            error("Se esperaba ')' después de coordenadas del lugar");
            sincronizarSentencia();
            return null;
        }
        verificar(TipoToken.PARENTESIS_DER);

        return new Lugar(nombre, tipo, x, y);
    }

    private Conexion analizarConexion() {
        verificar(TipoToken.CONNECT);

        // ORIGEN
        if (!verificaActual(TipoToken.IDENTIFICADOR)) {
            error("Se esperaba IDENTIFICADOR origen en conexión");
            sincronizarSentencia();
            return null;
        }
        String origen = extraerLexema(TipoToken.IDENTIFICADOR);

        // PREVENCIÓN: ¿aparece WITH antes de TO? → orden incorrecto
        if (verificaActual(TipoToken.WITH)) {
            error("Se esperaba 'to' después del origen pero se encontró 'with'");
            sincronizarSentencia();
            return null;
        }

        // TO
        if (!verificaActual(TipoToken.TO)) {
            error("Se esperaba 'to' después del origen");
            sincronizarSentencia();
            return null;
        }
        verificar(TipoToken.TO);

        // DESTINO
        if (!verificaActual(TipoToken.IDENTIFICADOR)) {
            error("Se esperaba IDENTIFICADOR destino en conexión");
            sincronizarSentencia();
            return null;
        }
        String destino = extraerLexema(TipoToken.IDENTIFICADOR);

        // WITH
        if (!verificaActual(TipoToken.WITH)) {
            error("Se esperaba 'with' después del destino");
            sincronizarSentencia();
            return null;
        }
        verificar(TipoToken.WITH);

        // TIPO DE CONEXIÓN (CADENA)
        if (!verificaActual(TipoToken.CADENA)) {
            if (verificaActual(TipoToken.IDENTIFICADOR)) {
                error("Se esperaba una CADENA entre comillas, no un IDENTIFICADOR");
            } else {
                error("Se esperaba CADENA para el tipo de conexión");
            }
            sincronizarSentencia();
            return null;
        }

        String tipo = extraerLexema(TipoToken.CADENA).replace("\"", "");

        return new Conexion(origen, destino, tipo);
    }

    private ObjetoEspecial analizarObjeto() {
        verificar(TipoToken.OBJECT);

        // NOMBRE
        if (!verificaActual(TipoToken.CADENA)) {
            error("Se esperaba CADENA para el nombre del objeto");
            sincronizarSentencia();
            return null;
        }
        String nombre = extraerLexema(TipoToken.CADENA).replace("\"", "");

        // DOS_PUNTOS
        if (!verificaActual(TipoToken.DOS_PUNTOS)) {
            error("Se esperaba ':' después del nombre del objeto");
            sincronizarSentencia();
            return null;
        }
        verificar(TipoToken.DOS_PUNTOS);

        // TIPO
        if (!verificaActual(TipoToken.IDENTIFICADOR)) {
            error("Se esperaba IDENTIFICADOR para el tipo del objeto");
            sincronizarSentencia();
            return null;
        }
        String tipoBruto = extraerLexema(TipoToken.IDENTIFICADOR);
        String tipo = tipoBruto.equalsIgnoreCase("objeto-magico") || tipoBruto.equalsIgnoreCase("objeto_magico")
                ? "objeto magico" : tipoBruto;

        // AT
        if (!verificaActual(TipoToken.AT)) {
            error("Se esperaba 'at' después del tipo del objeto");
            sincronizarSentencia();
            return null;
        }
        verificar(TipoToken.AT);

        // UBICACIÓN: puede ser IDENTIFICADOR o coordenadas
        if (verificaActual(TipoToken.IDENTIFICADOR)) {
            String lugar = extraerLexema(TipoToken.IDENTIFICADOR);
            return new ObjetoEspecial(nombre, tipo, lugar);
        } else if (verificaActual(TipoToken.PARENTESIS_IZQ)) {
            verificar(TipoToken.PARENTESIS_IZQ);

            if (!verificaActual(TipoToken.NUMERO)) {
                error("Se esperaba número X de coordenada del objeto");
                sincronizarSentencia();
                return null;
            }
            int x = Integer.parseInt(extraerLexema(TipoToken.NUMERO));

            if (!verificaActual(TipoToken.COMA)) {
                error("Se esperaba ',' entre coordenadas del objeto");
                sincronizarSentencia();
                return null;
            }
            verificar(TipoToken.COMA);

            if (!verificaActual(TipoToken.NUMERO)) {
                error("Se esperaba número Y de coordenada del objeto");
                sincronizarSentencia();
                return null;
            }
            int y = Integer.parseInt(extraerLexema(TipoToken.NUMERO));

            if (!verificaActual(TipoToken.PARENTESIS_DER)) {
                error("Se esperaba ')' después de las coordenadas del objeto");
                sincronizarSentencia();
                return null;
            }
            verificar(TipoToken.PARENTESIS_DER);

            return new ObjetoEspecial(nombre, tipo, x, y);
        } else {
            error("Se esperaba nombre de lugar o coordenadas después de 'at'");
            sincronizarSentencia();
            return null;
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
        erroresSintacticos.add(new Token(TipoToken.ERROR, mensaje, actual().linea, actual().columna));
    }

    private void sincronizar() {
        avanzar(); // Siempre avanzar al menos una vez

        while (!esFin()) {
            TipoToken tipo = actual().tipo;

            // Si encontramos un punto de reinicio válido, nos detenemos
            if (tipo == TipoToken.PLACE || tipo == TipoToken.CONNECT || tipo == TipoToken.OBJECT
                    || tipo == TipoToken.WORLD || tipo == TipoToken.LLAVE_DER) {
                return;
            }

            // Si encontramos un token de error, lo saltamos
            if (tipo == TipoToken.ERROR) {
                avanzar();
                continue;
            }

            avanzar(); // Avanzar en cualquier otro caso
        }
    }

    public List<Token> getErroresSintacticos() {
        return erroresSintacticos;
    }

}
