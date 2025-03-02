
package lfp.practica;

import java.util.ArrayList;
import java.util.Scanner;


import models.Personaje;
import models.Torneo;
import utils.LectorArchivo;
import utils.ReporteHTML;

/**
 *
 * @author jmfuente
 */
public class LFPPRACTICA {

    
    
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        
        ArrayList<Personaje> personajes = new ArrayList<>();
        
        while (!salir) {
            System.out.println("\n----------------- Menu -----------------");
            System.out.println("1. Cargar archivo");
            System.out.println("2. Jugar");
            System.out.println("3. Reporte de mayor ataque");
            System.out.println("4. Reporte de mayor defensa");
            System.out.println("5. Informacion del desarrollador");
            System.out.println("6. Salir");
            System.out.print("Elige una opcion: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    
                    System.out.print("Ingresa la ruta del archivo (.lfp): ");
                    String ruta = scanner.nextLine();
                    personajes = LectorArchivo.cargarPersonajes(ruta);
                    System.out.println("Se han cargado " + personajes.size() + " personajes.");
                    
                    break;
                case 2:
                    
                    if (personajes.size() < 2) {
                        System.out.println("No hay suficientes personajes para iniciar el torneo, deben ser mass de 2");
                    } else {
                        System.out.println("¡Comienza el torneo!");
                        Torneo.iniciarTorneo(personajes);
                    }
                    
                    break;
                case 3:
                    
                    if (personajes.isEmpty()) {
                        System.out.println("No hay personajes cargados para generar el reporte.");
                    } else {
                        String archivoAtaque = "reporte_mayor_ataque.html";
                        ReporteHTML.generarReporteMayorAtaque(personajes, archivoAtaque);
                        System.out.println("Reporte de mayor ataque generado en carpeta 'reportes': " + archivoAtaque);
                    }
                    
                    break;
                case 4:
                    
                     if (personajes.isEmpty()) {
                        System.out.println("No hay personajes cargados para generar el reporte.");
                    } else {
                        String archivoDefensa = "reporte_mayor_defensa.html";
                        ReporteHTML.generarReporteMayorDefensa(personajes, archivoDefensa);
                        System.out.println("Reporte de mayor defensa generado en carpeta 'reportes': " + archivoDefensa);
                    }
                    
                    break;
                case 5:
                    System.out.println("Informacion del desarrollador:");
                    System.out.println("NOMBRE: Josue Daniel Fuentes Diaz");
                    System.out.println("CARNET: 202300668");
                    break;
                case 6:
                    salir = true;
                    System.out.println("Gracias por jugar");
                    break;
                default:
                    System.out.println("Opcion no valida, intenta de nuevo.");
                    break;
            }
        }
        scanner.close();
    }

}
