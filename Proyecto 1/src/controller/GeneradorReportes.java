/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GeneradorReportes {

    private static final String RUTA_CARPETA = "reportes/";

    public static void generarReporteTokens(String nombreAFD, List<Token> tokens) {
        StringBuilder html = new StringBuilder();
        html.append("""
            <html><head><meta charset="UTF-8"><title>Reporte de Tokens</title>
            <style>
            body { font-family: Arial; }
            table { border-collapse: collapse; width: 80%; margin: 20px auto; }
            th, td { border: 1px solid #444; padding: 8px; text-align: center; }
            th { background-color: #f2f2f2; }
            h2 { text-align: center; }
            </style></head><body>
            <h2>Reporte de Tokens - AFD: """ + nombreAFD + "</h2>" +
            "<table><tr><th>Token</th><th>Lexema</th><th>Línea</th><th>Columna</th></tr>");

        for (Token t : tokens) {
            html.append("<tr>")
                .append("<td>").append(t.getTipo()).append("</td>")
                .append("<td>").append(t.getLexema()).append("</td>")
                .append("<td>").append(t.getLinea()).append("</td>")
                .append("<td>").append(t.getColumna()).append("</td>")
                .append("</tr>");
        }

        html.append("</table></body></html>");
        guardarHTML(nombreAFD + "_tokens.html", html.toString());
    }

    public static void generarReporteErrores(String nombreAFD, List<ErrorLexico> errores) {
        StringBuilder html = new StringBuilder();
        html.append("""
            <html><head><meta charset="UTF-8"><title>Reporte de Errores Léxicos</title>
            <style>
            body { font-family: Arial; }
            table { border-collapse: collapse; width: 60%; margin: 20px auto; }
            th, td { border: 1px solid #444; padding: 8px; text-align: center; }
            th { background-color: #f2f2f2; }
            h2 { text-align: center; }
            </style></head><body>
            <h2>Reporte de Errores Léxicos - AFD: """ + nombreAFD + "</h2>" +
            "<table><tr><th>Carácter</th><th>Línea</th><th>Columna</th></tr>");

        for (ErrorLexico e : errores) {
            html.append("<tr>")
                .append("<td>").append(e.getLexema()).append("</td>")
                .append("<td>").append(e.getLinea()).append("</td>")
                .append("<td>").append(e.getColumna()).append("</td>")
                .append("</tr>");
        }

        html.append("</table></body></html>");
        guardarHTML(nombreAFD + "_errores.html", html.toString());
    }

    private static void guardarHTML(String nombreArchivo, String contenido) {
        try {
            File carpeta = new File(RUTA_CARPETA);
            if (!carpeta.exists()) carpeta.mkdirs();

            File archivo = new File(carpeta, nombreArchivo);
            try (FileWriter writer = new FileWriter(archivo)) {
                writer.write(contenido);
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el reporte: " + e.getMessage());
        }
    }
}

