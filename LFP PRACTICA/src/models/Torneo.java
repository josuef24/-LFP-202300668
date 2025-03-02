/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package models;

import java.util.ArrayList;

/**
 *
 * @author jmfuente
 */
public class Torneo {
    
    public static void iniciarTorneo(ArrayList<Personaje> listaPersonajes) {
        ArrayList<Personaje> competidores = new ArrayList<>(listaPersonajes);
        int ronda = 1;

        while (competidores.size() > 1) {
            
            System.out.println("\n----------- RONDA " + ronda + " -----------");
            ArrayList<Personaje> ganadores = new ArrayList<>();

            for (int i = 0; i < competidores.size(); i += 2) {
                
                Personaje p1 = competidores.get(i);
                if (i + 1 < competidores.size()) {
                    
                    Personaje p2 = competidores.get(i + 1);
                    Personaje ganador = simularBatalla(p1, p2);
                    ganadores.add(ganador);
                    
                } else {
                    
                    System.out.println("\n" + p1.getNombre() + " avanza automaticamente a la siguiente ronda.");
                    ganadores.add(p1);
                    
                }
            }

            competidores = ganadores;
            ronda++;
        }

        Personaje campeon = competidores.get(0);
        System.out.println("\n" + campeon.getNombre() + " es el campeon del torneo!!");
    }

    private static Personaje simularBatalla(Personaje p1, Personaje p2) {
        p1.setVidaActual(p1.getSalud() * 10);
        p2.setVidaActual(p2.getSalud() * 10);

        System.out.println("\nBatalla entre " + p1.getNombre() + " y " + p2.getNombre());
        boolean turnoP1 = true;

        while (p1.estaVivo() && p2.estaVivo()) {
            
            if (turnoP1) {

                int danio = p1.calcularDaño(p2);
                p1.atacar(p2);
                if (danio > 0) {
                    System.out.println(p1.getNombre() + " ataca a " + p2.getNombre() + " y causa " + danio + " de danio");
                } else {
                    System.out.println(p1.getNombre() + " ataca a " + p2.getNombre() + " pero no logra causar danio");
                }
                
            } else {
                
                int danio = p2.calcularDaño(p1);
                p2.atacar(p1);
                if (danio > 0) {
                    System.out.println(p2.getNombre() + " ataca a " + p1.getNombre() + " y causa " + danio + " de danio");
                } else {
                    System.out.println(p2.getNombre() + " ataca a " + p1.getNombre() + " pero no logra causar danio");
                }
            }

            turnoP1 = !turnoP1;
        }

        Personaje ganador = p1.estaVivo() ? p1 : p2;
        System.out.println(ganador.getNombre() + " gana la batalla.");

        return ganador; 
    }
  
    
}
