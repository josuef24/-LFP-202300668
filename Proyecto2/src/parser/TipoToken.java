/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package parser;

public enum TipoToken {
    // Palabras clave
    WORLD, PLACE, CONNECT, OBJECT, TO, WITH, AT,

    // Tipos
    IDENTIFICADOR, NUMERO, CADENA,

    // Símbolos
    LLAVE_IZQ, LLAVE_DER, PARENTESIS_IZQ, PARENTESIS_DER, COMA, DOS_PUNTOS,

    // Fin
    EOF,

    // Error
    ERROR
}
