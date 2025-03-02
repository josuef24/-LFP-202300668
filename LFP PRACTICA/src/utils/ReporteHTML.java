/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;
import models.Personaje;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author jmfuente
 */
public class ReporteHTML {
    
     public static void generarReporteMayorAtaque(ArrayList<Personaje> personajes, String nombreArchivo) {
         
        personajes.sort((p1, p2) -> Integer.compare(p2.getAtaque(), p1.getAtaque()));
        int limite = Math.min(5, personajes.size());

        try (PrintWriter pw = new PrintWriter(new FileWriter("reportes/reporte_mayor_ataque.html"))) {
            pw.println("<html>");
            pw.println("<head><title>Reporte de Mayor Ataque</title></head>");
            pw.println("<body>");
            pw.println("<h1>Top 5 Jugadores con Mayor Ataque</h1>");
            pw.println("<table border='2'>");
            pw.println("<tr><th>Posición</th><th>Nombre</th><th>Ataque</th></tr>");

            for (int i = 0; i < limite; i++) {
                Personaje p = personajes.get(i);
                pw.println("<tr>");
                pw.println("<td>" + (i + 1) + "</td>");
                pw.println("<td>" + p.getNombre() + "</td>");
                pw.println("<td>" + p.getAtaque() + "</td>");
                pw.println("</tr>");
            }

            pw.println("</table>");
            pw.println("</body>");
            pw.println("</html>");
            
        } catch (IOException e) {
            System.out.println("Error al generar reporte de mayor ataque: " + e.getMessage());
        }
    }

    public static void generarReporteMayorDefensa(ArrayList<Personaje> personajes, String nombreArchivo) {
        
        personajes.sort((p1, p2) -> Integer.compare(p2.getDefensa(), p1.getDefensa()));
        int limite = Math.min(5, personajes.size());

        try (PrintWriter pw = new PrintWriter(new FileWriter("reportes/reporte_menor_ataque.html"))) {
            pw.println("<html>");
            pw.println("<head><title>Reporte de Mayor Defensa</title></head>");
            pw.println("<body>");
            pw.println("<h1>Top 5 Jugadores con Mayor Defensa</h1>");
            pw.println("<table border='2'>");
            pw.println("<tr><th>Posición</th><th>Nombre</th><th>Defensa</th></tr>");

            for (int i = 0; i < limite; i++) {
                Personaje p = personajes.get(i);
                pw.println("<tr>");
                pw.println("<td>" + (i + 1) + "</td>");
                pw.println("<td>" + p.getNombre() + "</td>");
                pw.println("<td>" + p.getDefensa() + "</td>");
                pw.println("</tr>");
            }

            pw.println("</table>");
            pw.println("</body>");
            pw.println("</html>");
            
        } catch (IOException e) {
            System.out.println("Error al generar reporte de mayor defensa: " + e.getMessage());
        }
    }
    
}
