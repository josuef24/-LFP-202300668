/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import models.*;

import java.io.File;
import java.io.PrintWriter;

public class GeneradorMapa {

    public static void generarArchivoDot(Mundo mundo) {
        try {
            String nombreArchivo = mundo.nombre.replaceAll("[^a-zA-Z0-9_\\-]", "_");
            File carpeta = new File("graficos");
            if (!carpeta.exists()) {
                carpeta.mkdirs();
            }

            String rutaDot = "graficos/" + nombreArchivo + ".dot";
            PrintWriter writer = new PrintWriter(rutaDot, "UTF-8");

            writer.println("digraph \"" + mundo.nombre + "\" {");
            writer.println("    node [style=filled];");

            // Lugares
            for (Lugar l : mundo.lugares) {
                String forma = obtenerFormaLugar(l.tipo);
                String color = obtenerColorLugar(l.tipo);
                writer.printf("    \"%s\" [shape=%s, fillcolor=%s, pos=\"%d,%d!\"];\n",
                        l.nombre, forma, color, l.x * 100, l.y * 100);
            }

            // Conexiones
            for (Conexion c : mundo.conexiones) {
                String estilo = obtenerEstiloConexion(c.tipo);
                String color = obtenerColorConexion(c.tipo);
                writer.printf("    \"%s\" -> \"%s\" [label=\"%s\", style=%s, color=%s];\n",
                        c.origen, c.destino, c.tipo, estilo, color);
            }

            // Objetos
            for (ObjetoEspecial o : mundo.objetos) {
                String forma = obtenerFormaObjeto(o.tipo);
                String color = obtenerColorObjeto(o.tipo);
                String emoji = obtenerEmojiObjeto(o.tipo);
                String etiqueta = emoji + " " + o.nombre;

                String nombreNodo = "obj_" + etiqueta.replaceAll("\\s+", "_");

                if (o.lugar != null) {
                    // conectado al lugar
                    writer.printf("    \"%s\" [label=\"%s\", shape=%s, fillcolor=%s];\n",
                            nombreNodo, etiqueta, forma, color);
                    writer.printf("    \"%s\" -> \"%s\" [label=\"en\", style=dotted];\n", nombreNodo, o.lugar);
                } else {
                    // coordenadas
                    writer.printf("    \"%s\" [label=\"%s\", shape=%s, fillcolor=%s, pos=\"%d,%d!\", style=filled];\n",
                            nombreNodo, etiqueta, forma, color, o.x * 100, o.y * 100);
                }
            }

            writer.println("}");

            writer.close();

            // Generar imagen PNG
            String comando = "dot -Tpng " + rutaDot + " -o graficos/" + nombreArchivo + ".png";
            Process p = Runtime.getRuntime().exec(comando);
            p.waitFor();

        } catch (Exception e) {
            System.err.println("Error generando mapa real: " + e.getMessage());
        }
    }
    
    

    // Métodos para formas y colores
    private static String obtenerFormaLugar(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "playa" ->
                "ellipse";
            case "cueva" ->
                "box";
            case "templo" ->
                "octagon";
            case "jungla" ->
                "parallelogram";
            case "montaña" ->
                "triangle";
            case "pueblo" ->
                "house";
            case "isla" ->
                "invtriangle";
            case "río" ->
                "hexagon";
            case "volcán" ->
                "doublecircle";
            case "pantano" ->
                "trapezium";
            default ->
                "ellipse";
        };
    }
    
    private static String obtenerEmojiObjeto(String tipo) {
    return switch (tipo.toLowerCase()) {
        case "tesoro" -> "\uD83C\uDF81";           // 🎁
        case "llave" -> "\uD83D\uDD11";            // 🔑
        case "arma" -> "\uD83D\uDDE1\uFE0F";       // 🗡️
        case "objeto mágico" -> "\u2728";          // ✨
        case "poción" -> "\u2697\uFE0F";           // ⚗️
        case "trampa" -> "\uD83D\uDCA3";           // 💣
        case "libro" -> "\uD83D\uDCD5";            // 📕
        case "herramienta" -> "\uD83D\uDEE0\uFE0F";// 🛠️
        case "bandera" -> "\uD83D\uDEA9";          // 🚩
        case "gema" -> "\uD83D\uDC8E";             // 💎
        default -> "";
    };
}


    private static String obtenerColorLugar(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "playa" ->
                "lightblue";
            case "cueva" ->
                "gray";
            case "templo" ->
                "gold";
            case "jungla" ->
                "forestgreen";
            case "montaña" ->
                "sienna";
            case "pueblo" ->
                "burlywood";
            case "isla" ->
                "lightgoldenrod";
            case "río" ->
                "deepskyblue";
            case "volcán" ->
                "orangered";
            case "pantano" ->
                "darkseagreen";
            default ->
                "white";
        };
    }

    private static String obtenerEstiloConexion(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "camino" ->
                "solid";
            case "puente" ->
                "dotted";
            case "sendero" ->
                "dashed";
            case "carretera" ->
                "solid";
            case "nado" ->
                "dashed";
            case "lancha" ->
                "solid";
            case "teleférico" ->
                "dotted";
            default ->
                "solid";
        };
    }

    private static String obtenerColorConexion(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "camino" ->
                "black";
            case "puente" ->
                "gray";
            case "sendero" ->
                "saddlebrown";
            case "carretera" ->
                "darkgray";
            case "nado" ->
                "deepskyblue";
            case "lancha" ->
                "blue";
            case "teleférico" ->
                "purple";
            default ->
                "black";
        };
    }

    private static String obtenerFormaObjeto(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "tesoro" ->
                "box3d";
            case "llave" ->
                "pentagon";
            case "arma" ->
                "diamond";
            case "objeto mágico" ->
                "component";
            case "poción" ->
                "cylinder";
            case "trampa" ->
                "hexagon";
            case "libro" ->
                "note";
            case "herramienta" ->
                "folder";
            case "bandera" ->
                "tab";
            case "gema" ->
                "egg";
            default ->
                "box";
        };
    }

    private static String obtenerColorObjeto(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "tesoro" ->
                "gold";
            case "llave" ->
                "lightsteelblue";
            case "arma" ->
                "orangered";
            case "objeto mágico" ->
                "violet";
            case "poción" ->
                "plum";
            case "trampa" ->
                "crimson";
            case "libro" ->
                "navajowhite";
            case "herramienta" ->
                "darkkhaki";
            case "bandera" ->
                "white";
            case "gema" ->
                "deepskyblue";
            default ->
                "white";
        };
    }
}
