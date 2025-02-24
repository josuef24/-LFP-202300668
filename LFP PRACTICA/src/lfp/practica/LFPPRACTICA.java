
package lfp.practica;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author jmfuente
 */
public class LFPPRACTICA {

    
    
    public static void main(String[] args) {
        
        Scanner scanner = new Scanner(System.in);
        boolean salir = false;
        
        while (!salir) {
            System.out.println("\n--- Menu del Torneo ---");
            System.out.println("1. Cargar archivo");
            System.out.println("2. Jugar");
            System.out.println("3. Generar reporte de mayor ataque");
            System.out.println("4. Generar reporte de mayor defensa");
            System.out.println("5. Mostrar información del desarrollador");
            System.out.println("6. Salir");
            System.out.print("Elige una opcion: ");
            
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcion) {
                case 1:
                    System.out.println("Cargando archivo...");
                    // Lógica para cargar el archivo
                    break;
                case 2:
                    System.out.println("Iniciando el juego...");
                    // Lógica para iniciar el juego
                    break;
                case 3:
                    System.out.println("Generando reporte de mayor ataque...");
                    // Lógica para generar el reporte
                    break;
                case 4:
                    System.out.println("Generando reporte de mayor ataque...");
                    // Lógica para mostrar la información
                    break;
                case 5:
                    System.out.println("Informacion del desarrollador:");
                    System.out.println("NOMBRE: Josue Daniel Fuentes Diaz");
                    System.out.println("CARNET: 202300668");
                    break;
                case 6:
                    salir = true;
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opcion no válida, intenta de nuevo.");
                    break;
            }
        }
        scanner.close();
    }

}
