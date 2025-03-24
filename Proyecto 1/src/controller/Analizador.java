/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.AFD;

import java.io.*;
import java.util.*;
import javax.swing.*;

/**
 *
 * @author jmfuente
 */
public class Analizador {

    private final Map<String, AFD> afdMap = new HashMap<>();
    private final List<String> erroresLexicos = new ArrayList<>();

    public void analizarArchivo(JFrame parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar archivo .lfp");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos LFP", "lfp"));

        int opcion = chooser.showOpenDialog(parent);

        if (opcion == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            if (!archivo.getName().endsWith(".lfp")) {
                JOptionPane.showMessageDialog(parent, "Debe seleccionar un archivo con extensión .lfp");
                return;
            }

            StringBuilder contenido = new StringBuilder();

            try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
                String linea;

                while ((linea = br.readLine()) != null) {
                    if (linea.trim().isEmpty()) {
                        continue;
                    }
                    contenido.append(linea.trim());
                }

                if (contenido.length() > 50000) {
                    JOptionPane.showMessageDialog(parent, "Archivo demasiado grande o mal formado.");
                    return;
                }

                procesarContenido(contenido.toString());
                JOptionPane.showMessageDialog(parent, "Archivo analizado correctamente.");

            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, "Error al leer el archivo: " + e.getMessage());
            }
        }
    }

    private void procesarContenido(String texto) {
        System.out.println("CONTENIDO LFP:\n" + texto);
        afdMap.clear();
        erroresLexicos.clear();

        texto = texto.replaceAll("\\s+", "");

        if (!texto.startsWith("{") || !texto.endsWith("}")) {
            erroresLexicos.add("El archivo no inicia o termina correctamente con llaves.");
            return;
        }

        int i = 1; // saltar la primera llave inicial
        while (i < texto.length()) {
            int nombreFin = texto.indexOf(":", i);
            if (nombreFin == -1) {
                break;
            }

            String nombreAFD = texto.substring(i, nombreFin).trim();
            AFD afd = new AFD(nombreAFD);

            i = texto.indexOf("{", nombreFin);
            if (i == -1) {
                break;
            }
            i++; // saltar llave de apertura del AFD

            while (i < texto.length() && texto.charAt(i) != '}') {
                int claveFin = texto.indexOf(":", i);
                if (claveFin == -1) {
                    break;
                }

                String clave = texto.substring(i, claveFin).trim();
                i = claveFin + 1;

                switch (clave) {
                    case "descripcion":
                        int descIni = texto.indexOf("\"", i) + 1;
                        int descFin = texto.indexOf("\"", descIni);
                        afd.setDescripcion(texto.substring(descIni, descFin));
                        i = descFin + 1;
                        break;

                    case "estados":
                        i = procesarLista(texto, i, afd::agregarEstado);
                        break;

                    case "alfabeto":
                        i = procesarLista(texto, i, afd::agregarSimbolo);
                        break;

                    case "inicial":
                        int comaIni = texto.indexOf(",", i);
                        int llaveIni = texto.indexOf("}", i);
                        int finIni = (comaIni == -1 || comaIni > llaveIni) ? llaveIni : comaIni;
                        afd.setEstadoInicial(texto.substring(i, finIni));
                        i = finIni;
                        break;

                    case "finales":
                        i = procesarLista(texto, i, afd::agregarFinal);
                        break;

                    case "transiciones":
                        i = texto.indexOf("{", i) + 1;

                        while (i < texto.length() && texto.charAt(i) != '}') {
                            int igual = texto.indexOf("=", i);
                            if (igual == -1) {
                                break;
                            }
                            String desde = texto.substring(i, igual).trim();

                            int iniPar = texto.indexOf("(", igual);
                            int finPar = texto.indexOf(")", iniPar);
                            if (iniPar == -1 || finPar == -1) {
                                break;
                            }

                            String bloque = texto.substring(iniPar + 1, finPar);
                            String[] transiciones = bloque.split(",");

                            for (String t : transiciones) {
                                t = t.trim();
                                int com1 = t.indexOf("\"");
                                int com2 = t.indexOf("\"", com1 + 1);
                                if (com1 == -1 || com2 == -1 || !t.contains("->")) {
                                    continue;
                                }

                                String simbolo = t.substring(com1 + 1, com2);
                                String hacia = t.substring(t.indexOf("->") + 2).trim();
                                hacia = hacia.replaceAll("[)}]", "").trim();

                                afd.agregarTransicion(desde, simbolo, hacia);
                                System.out.println("Cargada: " + desde + " --" + simbolo + "--> " + hacia);
                            }

                            i = finPar + 1;
                            if (i < texto.length() && texto.charAt(i) == ',') {
                                i++;
                            }
                        }

                        // cerrar llave de transiciones
                        i = texto.indexOf("}", i) + 1;
                        break;

                    default:
                        erroresLexicos.add("Clave no reconocida: " + clave);
                        break;
                }

                if (i < texto.length() && texto.charAt(i) == ',') {
                    i++;
                }
            }

            i = texto.indexOf("}", i) + 1;

            afdMap.put(afd.getNombre(), afd);
            System.out.println("AFD guardado: " + afd.getNombre());
            System.out.println("Transiciones: " + afd.getTransiciones());

            if (i < texto.length() && texto.charAt(i) == ',') {
                i++;
            }
        }
    }

    
    private int procesarLista(String texto, int i, java.util.function.Consumer<String> accion) {
        i = texto.indexOf('[', i) + 1;
        while (texto.charAt(i) != ']') {
            int coma = texto.indexOf(',', i);
            int fin = (coma == -1 || coma > texto.indexOf(']', i)) ? texto.indexOf(']', i) : coma;
            if (fin == -1 || fin <= i) {
                break;
            }

            String valor = texto.substring(i, fin).trim();
            if (!valor.isEmpty()) {
                accion.accept(valor);
            }
            i = fin + 1;
        }
        return texto.indexOf(']', i) + 1;
    }



    public Map<String, AFD> getAFDs() {
        return afdMap;
    }

    public List<String> getErroresLexicos() {
        return erroresLexicos;
    }
}
