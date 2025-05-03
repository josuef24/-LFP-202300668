/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import parser.Token;
import parser.TipoToken;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;

public class Reporte {

    public static void generarReporteTokens(List<Token> tokens) {
        try {
            File carpeta = new File("reportes");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            PrintWriter writer = new PrintWriter("reportes/Tokens.html", "UTF-8");
            writer.println("<html><head><title>Tokens</title></head><body>");
            writer.println("<h1>Reporte de Tokens</h1>");
            writer.println("<table border='1'>");
            writer.println("<tr><th>Token</th><th>Lexema</th><th>Línea</th><th>Columna</th></tr>");

            for (Token token : tokens) {
                if (token.tipo != TipoToken.ERROR && token.tipo != TipoToken.EOF) {
                    writer.printf("<tr><td>%s</td><td>%s</td><td>%d</td><td>%d</td></tr>\n",
                            token.tipo, escapeHtml(token.lexema), token.linea, token.columna);
                }
            }

            writer.println("</table></body></html>");
            writer.close();

            System.out.println("Reporte Tokens.html generado correctamente.");

        } catch (Exception e) {
            System.err.println("Error al generar reporte de tokens: " + e.getMessage());
        }
    }

    public static void generarReporteErroresSeparados(List<Token> erroresLexicos, List<Token> erroresSintacticos) {
        try {
            File carpeta = new File("reportes");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            PrintWriter writer = new PrintWriter("reportes/Errores.html", "UTF-8");
            writer.println("<html><head><title>Errores</title></head><body>");
            writer.println("<h1>Errores Léxicos</h1>");
            writer.println("<table border='1'>");
            writer.println("<tr><th>Error</th><th>Línea</th><th>Columna</th></tr>");

            for (Token error : erroresLexicos) {
                writer.printf("<tr><td>%s</td><td>%d</td><td>%d</td></tr>\n",
                        escapeHtml(error.lexema), error.linea, error.columna);
            }

            writer.println("</table><br><h1>Errores Sintácticos</h1>");
            writer.println("<table border='1'>");
            writer.println("<tr><th>Error</th><th>Línea</th><th>Columna</th></tr>");

            for (Token error : erroresSintacticos) {
                writer.printf("<tr><td>%s</td><td>%d</td><td>%d</td></tr>\n",
                        escapeHtml(error.lexema), error.linea, error.columna);
            }

            writer.println("</table></body></html>");
            writer.close();

            System.out.println("Reporte Errores.html generado con separación correcta.");

        } catch (Exception e) {
            System.err.println("Error al generar reporte de errores separados: " + e.getMessage());
        }
    }

    public static void generarReporteErrores(List<Token> errores) {
        try {
            File carpeta = new File("reportes");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            PrintWriter writer = new PrintWriter("reportes/Errores.html", "UTF-8");
            writer.println("<html><head><title>Errores</title></head><body>");
            writer.println("<h1>Reporte de Errores Léxicos</h1>");
            writer.println("<table border='1'>");
            writer.println("<tr><th>Error</th><th>Línea</th><th>Columna</th></tr>");

            for (Token error : errores) {
                writer.printf("<tr><td>%s</td><td>%d</td><td>%d</td></tr>\n",
                        escapeHtml(error.lexema), error.linea, error.columna);
            }

            writer.println("</table></body></html>");
            writer.close();

            System.out.println("Reporte Errores.html generado correctamente.");

        } catch (Exception e) {
            System.err.println("Error al generar reporte de errores: " + e.getMessage());
        }
    }

    private static String escapeHtml(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\"", "&quot;").replace("'", "&#39;");
    }

}
