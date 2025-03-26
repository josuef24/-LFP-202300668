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

    private final Map<String, AFD> afdMap = new LinkedHashMap<>();
    private final Map<String, List<String>> erroresPorAFD = new HashMap<>();

    private static final String ERROR_ARCHIVO = "Error al leer el archivo: ";
    private static final String EXTENSION_INVALIDA = "Debe seleccionar un archivo con extensión .lfp";

    public void analizarArchivo(JFrame parent) {
        String contenido = seleccionarYLeerArchivo(parent);
        if (contenido != null) {
            procesarContenido(contenido);
            mostrarResultadoAnalisis(parent);
        }
    }

    public String analizarArchivoConRetorno(JFrame parent) {
        String contenido = seleccionarYLeerArchivo(parent);
        if (contenido != null) {
            procesarContenido(contenido);
            return contenido;
        }
        return "";
    }

    private String seleccionarYLeerArchivo(JFrame parent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Seleccionar archivo .lfp");
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Archivos LFP", "lfp"));

        if (chooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File archivo = chooser.getSelectedFile();
            if (!archivo.getName().toLowerCase().endsWith(".lfp")) {
                JOptionPane.showMessageDialog(parent, EXTENSION_INVALIDA);
                return null;
            }

            try {
                return leerArchivoCompleto(archivo);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(parent, ERROR_ARCHIVO + e.getMessage());
            }
        }
        return null;
    }

    private String leerArchivoCompleto(File archivo) throws IOException {
        StringBuilder contenido = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        }
        return contenido.toString();
    }

    private void mostrarResultadoAnalisis(JFrame parent) {
        int totalAFDs = afdMap.size();
        int afdsValidos = (int) afdMap.values().stream()
                .filter(afd -> !tieneErrores(afd.getNombre()))
                .count();

        String mensaje = String.format("""
            Archivo analizado correctamente.
            Total AFDs cargados: %d
            AFDs válidos: %d
            AFDs con errores: %d
            """, totalAFDs, afdsValidos, totalAFDs - afdsValidos);

        JOptionPane.showMessageDialog(parent, mensaje);
    }

    private void procesarContenido(String texto) {
        afdMap.clear();
        erroresPorAFD.clear();

        // Limpiar comentarios
        texto = texto.replaceAll("//.*|/\\*(.|\\n)*?\\*/", "").trim();

        if (!texto.startsWith("{") || !texto.endsWith("}")) {
            erroresPorAFD.put("GLOBAL", List.of("El archivo no tiene el formato esperado (debe empezar con '{' y terminar con '}')"));
            return;
        }

        // Extraer bloques AFD individuales
        List<String> bloquesAFD = extraerBloquesAFD(texto.substring(1, texto.length() - 1).trim());

        // Procesar cada AFD
        bloquesAFD.forEach(bloque -> {
            try {
                procesarAFDIndividual(bloque);
            } catch (Exception e) {
                System.err.println("Error procesando AFD: " + e.getMessage());
                erroresPorAFD.computeIfAbsent("DESCONOCIDO", k -> new ArrayList<>())
                        .add("Error inesperado procesando AFD: " + e.getMessage());
            }
        });
    }

    private List<String> extraerBloquesAFD(String texto) {
        List<String> bloques = new ArrayList<>();
        int nivelLlaves = 0;
        int inicioBloque = 0;

        for (int i = 0; i < texto.length(); i++) {
            char c = texto.charAt(i);

            if (c == '{') {
                if (nivelLlaves == 0) {
                    inicioBloque = encontrarInicioNombre(texto, i);
                }
                nivelLlaves++;
            } else if (c == '}') {
                nivelLlaves--;
                if (nivelLlaves == 0) {
                    bloques.add(texto.substring(inicioBloque, i + 1).trim());
                }
            }
        }
        return bloques;
    }

    private int encontrarInicioNombre(String texto, int posicionLlave) {
        int inicio = posicionLlave - 1;
        while (inicio >= 0 && !esCaracterNombre(texto.charAt(inicio))) {
            inicio--;
        }
        while (inicio >= 0 && esCaracterNombre(texto.charAt(inicio))) {
            inicio--;
        }
        return inicio + 1;
    }

    private boolean esCaracterNombre(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    private void procesarAFDIndividual(String bloqueAFD) {
        // Extraer nombre del AFD
        int indiceDosPuntos = bloqueAFD.indexOf(":");
        if (indiceDosPuntos == -1) {
            erroresPorAFD.put("DESCONOCIDO", List.of("Bloque AFD sin nombre (falta ':')"));
            return;
        }

        String nombreAFD = bloqueAFD.substring(0, indiceDosPuntos).trim();
        List<String> erroresActuales = new ArrayList<>();
        erroresPorAFD.put(nombreAFD, erroresActuales);

        // Crear AFD
        AFD afd = new AFD(nombreAFD);

        // Extraer contenido entre llaves
        int inicioLlave = bloqueAFD.indexOf("{", indiceDosPuntos);
        int finLlave = bloqueAFD.lastIndexOf("}");

        if (inicioLlave == -1 || finLlave == -1) {
            erroresActuales.add("Estructura inválida: faltan llaves {}");
            return;
        }

        String contenidoAFD = bloqueAFD.substring(inicioLlave + 1, finLlave).trim();
        procesarPropiedadesAFD(afd, contenidoAFD, erroresActuales);

        // Validación final
        validarAFDCompleto(afd, erroresActuales);

        // Solo agregar si tiene estados definidos
        if (!afd.getEstados().isEmpty()) {
            afdMap.put(nombreAFD, afd);
        }
    }

    private void procesarPropiedadesAFD(AFD afd, String contenido, List<String> errores) {
        // Usar split que respete comas dentro de paréntesis
        String[] propiedades = contenido.split(",(?![^\\[\\](){}]*[\\]\\)])(?=\\s*\\w+\\s*:)");

        for (String propiedad : propiedades) {
            propiedad = propiedad.trim();
            if (propiedad.isEmpty()) {
                continue;
            }

            int indiceDosPuntos = propiedad.indexOf(":");
            if (indiceDosPuntos == -1) {
                errores.add("Propiedad mal formada (falta ':'): " + propiedad);
                continue;
            }

            String clave = propiedad.substring(0, indiceDosPuntos).trim();
            String valor = propiedad.substring(indiceDosPuntos + 1).trim();

            try {
                procesarPropiedadIndividual(afd, clave, valor, errores);
            } catch (Exception e) {
                errores.add("Error procesando propiedad '" + clave + "': " + e.getMessage());
            }
        }
    }

    private void procesarPropiedadIndividual(AFD afd, String clave, String valor, List<String> errores) {
        switch (clave.toLowerCase()) { // Hacer case-insensitive
            case "descripcion":
                afd.setDescripcion(valor);
                break;

            case "estados":
                procesarLista(valor, afd::agregarEstado);
                break;

            case "alfabeto":
                procesarLista(valor, s -> afd.agregarSimbolo(s.replace("\"", "")));
                break;

            case "inicial":
                afd.setEstadoInicial(valor);
                break;

            case "finales":
                procesarLista(valor, afd::agregarFinal);
                break;

            case "transiciones":
                procesarTransiciones(afd, valor, errores);
                break;

            default:
                errores.add("Propiedad no reconocida: " + clave);
        }
    }

    private void validarAFDCompleto(AFD afd, List<String> erroresActuales) {
        // Validar estado inicial
        if (afd.getEstadoInicial() == null || afd.getEstadoInicial().isEmpty()) {
            erroresActuales.add("No se definió estado inicial");
        } else if (!afd.getEstados().contains(afd.getEstadoInicial())) {
            erroresActuales.add("Estado inicial '" + afd.getEstadoInicial() + "' no existe en los estados definidos");
        }

        // Validar estados finales
        for (String estadoFinal : afd.getEstadosFinales()) {
            if (!afd.getEstados().contains(estadoFinal)) {
                erroresActuales.add("Estado final '" + estadoFinal + "' no existe en los estados definidos");
            }
        }

        // Validar que todos los estados tengan transiciones definidas
        for (String estado : afd.getEstados()) {
            if (!afd.getTransiciones().containsKey(estado) && !afd.getEstadosFinales().contains(estado)) {
                erroresActuales.add("No hay transiciones definidas para el estado '" + estado + "'");
            }
        }
    }

    private void procesarLista(String valor, java.util.function.Consumer<String> accion) {
        valor = valor.replaceAll("[\\[\\]]", "");
        for (String item : valor.split(",")) {
            if (!item.trim().isEmpty()) {
                accion.accept(item.trim());
            }
        }
    }

    private void procesarTransiciones(AFD afd, String valor, List<String> errores) {
        valor = valor.replaceAll("^\\{|\\}$", "").trim();

        // Procesar cada bloque tipo: S0 = (...), S1 = (...)
        Arrays.stream(valor.split("(?<=\\)),"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(transicion -> procesarTransicionEstado(afd, transicion, errores));
    }

    private void procesarTransicionEstado(AFD afd, String transicion, List<String> errores) {
        int igual = transicion.indexOf('=');
        if (igual == -1) {
            errores.add("Transición mal formada (falta '='): " + transicion);
            return;
        }

        String estadoOrigen = transicion.substring(0, igual).trim();
        String bloque = transicion.substring(igual + 1).trim();

        // Verificamos paréntesis válidos
        if (!bloque.startsWith("(") || !bloque.endsWith(")")) {
            errores.add("Transición sin paréntesis válidos: " + transicion);
            return;
        }

        String contenido = bloque.substring(1, bloque.length() - 1).trim();
        procesarTransicionesIndividuales(afd, estadoOrigen, contenido, errores);
    }

    private void procesarTransicionesIndividuales(AFD afd, String estadoOrigen, String transiciones, List<String> errores) {
        if (transiciones.isEmpty()) {
            return;
        }

        // Manejar comas al final
        if (transiciones.endsWith(",")) {
            transiciones = transiciones.substring(0, transiciones.length() - 1).trim();
        }

        // Split que respete comillas
        String[] transicionesArray = transiciones.split("\\s*,\\s*(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        for (String transicion : transicionesArray) {
            transicion = transicion.trim();
            if (transicion.isEmpty()) {
                continue;
            }

            procesarUnaTransicion(afd, estadoOrigen, transicion, errores);
        }
    }

    private void procesarUnaTransicion(AFD afd, String estadoOrigen, String transicion, List<String> erroresActuales) {
        // Verificar símbolos inválidos
        if (transicion.matches(".*[/@+$].*")) {
            erroresActuales.add("Transición inválida (contiene caracteres especiales): " + transicion);
            return;
        }

        int inicioComilla = transicion.indexOf("\"");
        int finComilla = transicion.indexOf("\"", inicioComilla + 1);
        int indiceFlecha = transicion.indexOf("->");

        if (inicioComilla == -1 || finComilla == -1 || indiceFlecha == -1) {
            erroresActuales.add("Transición mal formada en estado " + estadoOrigen + ": " + transicion);
            return;
        }

        String simbolo = transicion.substring(inicioComilla + 1, finComilla);
        String estadoDestino = transicion.substring(indiceFlecha + 2).replaceAll("[^a-zA-Z0-9]", "").trim();

        if (simbolo.isEmpty() || estadoDestino.isEmpty()) {
            erroresActuales.add("Transición incompleta en estado " + estadoOrigen + ": " + transicion);
            return;
        }

        // Verificar si el símbolo está en el alfabeto
        if (!afd.getAlfabeto().contains(simbolo)) {
            erroresActuales.add("Símbolo '" + simbolo + "' no está en el alfabeto");
        }

        // Verificar si el estado destino existe
        if (!afd.getEstados().contains(estadoDestino)) {
            erroresActuales.add("Estado destino '" + estadoDestino + "' no existe");
        }

        afd.agregarTransicion(estadoOrigen, simbolo, estadoDestino);
    }

    // 🔍 Consultas
    public boolean tieneErrores(String nombreAFD) {
        return erroresPorAFD.getOrDefault(nombreAFD, Collections.emptyList()).size() > 0;
    }

    public Map<String, AFD> getAFDs() {
        return Collections.unmodifiableMap(afdMap);
    }

    public List<String> getErroresPorAFD(String nombreAFD) {
        return Collections.unmodifiableList(erroresPorAFD.getOrDefault(nombreAFD, Collections.emptyList()));
    }

    public Map<String, List<String>> getErroresTotales() {
        return Collections.unmodifiableMap(erroresPorAFD);
    }

    public List<String> getTodosLosErrores() {
        return erroresPorAFD.values().stream()
                .flatMap(List::stream)
                .toList();
    }

    public int getTotalAFDs() {
        return afdMap.size();
    }

    public int getAFDsValidos() {
        return (int) afdMap.keySet().stream()
                .filter(nombre -> !tieneErrores(nombre))
                .count();
    }
}
