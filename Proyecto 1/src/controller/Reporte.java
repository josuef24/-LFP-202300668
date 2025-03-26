/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Reporte {

    public static void generarReporteGeneral(String textoFuente, JFrame parent) {
        try {
            // Crear carpeta de reportes si no existe
            Files.createDirectories(Paths.get("reportes"));

            // Analizar el texto fuente
            AnalizadorLexico analizadorLexico = new AnalizadorLexico(textoFuente);
            analizadorLexico.analizar();

            // Obtener tokens y errores
            List<Token> tokens = analizadorLexico.getTokens();
            List<ErrorLexico> errores = analizadorLexico.getErrores();

            // Guardar reportes HTML
            guardarHTMLTokens(tokens);
            guardarHTMLErrores(errores);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(parent, "Error generando reporte: " + e.getMessage());
        }
    }

    private static void guardarHTMLTokens(List<Token> tokens) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("""
            <html><head><meta charset="UTF-8"><style>
            body { font-family: Arial; }
            h2 { color: #003366; }
            table { border-collapse: collapse; width: 100%%; }
            th, td { border: 1px solid #888; padding: 6px; text-align: center; }
            th { background-color: #cce0ff; }
            </style></head><body>
            <h2>Reporte de Tokens</h2>
            <table>
            <tr><th>Tipo</th><th>Lexema</th><th>Línea</th><th>Columna</th></tr>
        """);

        for (Token token : tokens) {
            html.append("<tr>")
                .append("<td>").append(token.getTipo()).append("</td>")
                .append("<td>").append(escapeHTML(token.getLexema())).append("</td>")
                .append("<td>").append(token.getLinea()).append("</td>")
                .append("<td>").append(token.getColumna()).append("</td>")
                .append("</tr>");
        }

        html.append("</table></body></html>");

        Files.writeString(Paths.get("reportes/tokens.html"), html.toString(), StandardOpenOption.CREATE);
    }

    private static void guardarHTMLErrores(List<ErrorLexico> errores) throws IOException {
        StringBuilder html = new StringBuilder();
        html.append("""
            <html><head><meta charset="UTF-8"><style>
            body { font-family: Arial; }
            h2 { color: #990000; }
            table { border-collapse: collapse; width: 100%%; }
            th, td { border: 1px solid #888; padding: 6px; text-align: center; }
            th { background-color: #ffcccc; }
            </style></head><body>
            <h2>Reporte de Errores Léxicos</h2>
            <table>
            <tr><th>Descripción</th><th>Lexema</th><th>Línea</th><th>Columna</th></tr>
        """);

        for (ErrorLexico error : errores) {
            html.append("<tr>")
                .append("<td>").append(escapeHTML(error.getDescripcion())).append("</td>")
                .append("<td>").append(escapeHTML(error.getLexema())).append("</td>")
                .append("<td>").append(error.getLinea()).append("</td>")
                .append("<td>").append(error.getColumna()).append("</td>")
                .append("</tr>");
        }

        html.append("</table></body></html>");

        Files.writeString(Paths.get("reportes/errores.html"), html.toString(), StandardOpenOption.CREATE);
    }

    private static String escapeHTML(String texto) {
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }
}

