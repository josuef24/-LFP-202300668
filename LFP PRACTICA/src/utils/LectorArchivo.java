/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import models.Personaje;

/**
 *
 * @author jmfuente
 */
public class LectorArchivo {

    public static ArrayList<Personaje> cargarPersonajes(String rutaArchivo) {
        ArrayList<Personaje> personajes = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            boolean esPrimeraLinea = true;

            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                if (esPrimeraLinea) {
                    esPrimeraLinea = false;
                    continue;
                }

                String[] partes = linea.split("\\|");

                if (partes.length != 4) {
                    System.out.println("Formato incorrecto en la línea: " + linea);
                    continue;
                }

                String nombre = partes[0].trim();
                int salud = Integer.parseInt(partes[1].trim());
                int ataque = Integer.parseInt(partes[2].trim());
                int defensa = Integer.parseInt(partes[3].trim());

                Personaje p = new Personaje(nombre, salud, ataque, defensa);
                personajes.add(p);
            }
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error en el formato numérico: " + e.getMessage());
        }

        return personajes;
    }

}
