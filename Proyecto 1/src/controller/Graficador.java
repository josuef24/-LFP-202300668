/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.AFD;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.QuadCurve2D;
import java.util.*;

/**
 * clase para graficar autómatas Finitos deterministas (AFD)
 *
 * @author jmfuente
 */
public class Graficador {

    // Constantes de configuración
    private static final int STATE_SIZE = 40;
    private static final int HORIZONTAL_SPACING = 100;
    private static final int VERTICAL_SPACING = 80;
    private static final int ARROW_SIZE = 10;
    private static final int LOOP_RADIUS = 25;
    private static final int LABEL_OFFSET = 15;
    private static final int INITIAL_ARROW_LENGTH = 30;

    // Colores
    private static final Color STATE_COLOR = new Color(200, 230, 255);
    private static final Color FINAL_STATE_COLOR = new Color(150, 200, 255);
    private static final Color TRANSITION_COLOR = new Color(0, 100, 200);
    private static final Color TEXT_COLOR = Color.BLACK;
    private static final Color LABEL_BG_COLOR = Color.WHITE;

    /**
     * Grafica un AFD en el panel especificado
     *
     * @param afd Autómata a graficar (no puede ser nulo)
     * @param panel Panel donde se dibujará el AFD (no puede ser nulo)
     * @throws IllegalArgumentException si afd o panel son nulos
     */
    public static void graficarAFD(AFD afd, JPanel panel) {
        if (afd == null || panel == null) {
            throw new IllegalArgumentException("AFD y panel no pueden ser nulos");
        }

        panel.removeAll();

        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Configurar fuente
                g2d.setFont(new Font("Arial", Font.BOLD, 12));

                try {
                    // Calcular disposición de estados
                    Map<String, Point> posiciones = calcularDisposicionEstados(afd);

                    // Dibujar elementos
                    dibujarTransiciones(g2d, afd, posiciones);
                    dibujarEstados(g2d, afd, posiciones);
                    dibujarFlechaInicial(g2d, afd, posiciones);
                } catch (Exception e) {
                    g2d.setColor(Color.RED);
                    g2d.drawString("Error al graficar AFD: " + e.getMessage(), 20, 20);
                }
            }

            private static void dibujarTransiciones(Graphics2D g2d, AFD afd, Map<String, Point> posiciones) {
                g2d.setColor(TRANSITION_COLOR);
                g2d.setStroke(new BasicStroke(1.8f));

                // Verificar si hay transiciones definidas
                if (afd.getTransiciones() == null) {
                    return;
                }

                for (Map.Entry<String, Map<String, String>> transiciones : afd.getTransiciones().entrySet()) {
                    String desde = transiciones.getKey();
                    Point pDesde = posiciones.get(desde);
                    if (pDesde == null) {
                        continue;
                    }

                    for (Map.Entry<String, String> trans : transiciones.getValue().entrySet()) {
                        String simbolo = trans.getKey();
                        String hacia = trans.getValue();
                        Point pHacia = posiciones.get(hacia);
                        if (pHacia == null) {
                            continue;
                        }

                        if (desde.equals(hacia)) {
                            dibujarAutoTransicion(g2d, pDesde, simbolo);
                        } else {
                            if (yaHayTransicionDe(afd, hacia, desde)) {
                                dibujarTransicionCurva(g2d, pDesde, pHacia, simbolo, true); // curva arriba
                            } else {
                                dibujarTransicionCurva(g2d, pDesde, pHacia, simbolo, false); // línea normal
                            }

                        }
                    }
                }
            }

            private static boolean yaHayTransicionDe(AFD afd, String desde, String hacia) {
                return afd.getTransiciones().containsKey(desde)
                        && afd.getTransiciones().get(desde).containsValue(hacia);
            }

            private static void dibujarTransicionCurva(Graphics2D g2d, Point desde, Point hacia, String simbolo, boolean invertirCurva) {
                int r = STATE_SIZE / 2;
                int curvaOffset = 30;

                double dx = hacia.x - desde.x;
                double dy = hacia.y - desde.y;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist == 0) {
                    return;
                }

                double nx = dx / dist;
                double ny = dy / dist;
                double px = -ny;
                double py = nx;

                // Ajustar inicio y fin para que no toquen los bordes del estado
                int x1 = (int) (desde.x + r * nx);
                int y1 = (int) (desde.y + r * ny);
                int x2 = (int) (hacia.x - r * nx);
                int y2 = (int) (hacia.y - r * ny);

                // Punto de control para la curva
                int cx = (x1 + x2) / 2 + (invertirCurva ? -curvaOffset : curvaOffset) * (int) px;
                int cy = (y1 + y2) / 2 + (invertirCurva ? -curvaOffset : curvaOffset) * (int) py;

                // Dibujar curva
                QuadCurve2D curvaBezier = new QuadCurve2D.Float();
                curvaBezier.setCurve(x1, y1, cx, cy, x2, y2);
                g2d.draw(curvaBezier);

                // Dibujar flecha (cerca del destino)
                double t = 0.95;
                double xt = (1 - t) * (1 - t) * x1 + 2 * (1 - t) * t * cx + t * t * x2;
                double yt = (1 - t) * (1 - t) * y1 + 2 * (1 - t) * t * cy + t * t * y2;
                double angle = Math.atan2(y2 - cy, x2 - cx);

                Polygon flecha = new Polygon();
                flecha.addPoint((int) xt,
                        (int) yt);
                flecha.addPoint((int) (xt - ARROW_SIZE * Math.cos(angle - Math.PI / 6)),
                        (int) (yt - ARROW_SIZE * Math.sin(angle - Math.PI / 6)));
                flecha.addPoint((int) (xt - ARROW_SIZE * Math.cos(angle + Math.PI / 6)),
                        (int) (yt - ARROW_SIZE * Math.sin(angle + Math.PI / 6)));
                g2d.fillPolygon(flecha);

                // Etiqueta bien posicionada
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(simbolo);

                g2d.setColor(LABEL_BG_COLOR);
                g2d.fillRect(cx - textWidth / 2 - 2, cy - fm.getAscent(), textWidth + 4, fm.getHeight());

                g2d.setColor(TEXT_COLOR);
                g2d.drawString(simbolo, cx - textWidth / 2, cy);
            }

            private static void dibujarAutoTransicion(Graphics2D g2d, Point punto, String simbolo) {
                int x = punto.x - LOOP_RADIUS;
                int y = punto.y - LOOP_RADIUS - STATE_SIZE / 2;

                // Dibujar círculo
                g2d.drawOval(x, y, LOOP_RADIUS * 2, LOOP_RADIUS * 2);

                // Dibujar punta de flecha
                int arrowX = punto.x;
                int arrowY = punto.y - STATE_SIZE / 2;
                g2d.fillPolygon(
                        new int[]{arrowX, arrowX - ARROW_SIZE / 2, arrowX + ARROW_SIZE / 2},
                        new int[]{arrowY, arrowY - ARROW_SIZE, arrowY - ARROW_SIZE},
                        3
                );

                // Dibujar etiqueta
                FontMetrics fm = g2d.getFontMetrics();
                g2d.setColor(TEXT_COLOR);
                g2d.drawString(simbolo, punto.x - fm.stringWidth(simbolo) / 2, y - 5);
            }

            /**
             * Dibuja una transición entre dos estados diferentes
             */
            private static void dibujarTransicionEntreEstados(Graphics2D g2d, Point desde, Point hacia, String simbolo) {
                // Calcular ángulo y puntos de inicio/fin
                double angle = Math.atan2(hacia.y - desde.y, hacia.x - desde.x);
                int offset = STATE_SIZE / 2;
                int x1 = (int) (desde.x + offset * Math.cos(angle));
                int y1 = (int) (desde.y + offset * Math.sin(angle));
                int x2 = (int) (hacia.x - offset * Math.cos(angle));
                int y2 = (int) (hacia.y - offset * Math.sin(angle));

                // Dibujar línea
                g2d.drawLine(x1, y1, x2, y2);

                // Dibujar punta de flecha
                Polygon arrowHead = new Polygon();
                arrowHead.addPoint(x2, y2);
                arrowHead.addPoint(
                        (int) (x2 - ARROW_SIZE * Math.cos(angle - Math.PI / 6)),
                        (int) (y2 - ARROW_SIZE * Math.sin(angle - Math.PI / 6))
                );
                arrowHead.addPoint(
                        (int) (x2 - ARROW_SIZE * Math.cos(angle + Math.PI / 6)),
                        (int) (y2 - ARROW_SIZE * Math.sin(angle + Math.PI / 6))
                );
                g2d.fill(arrowHead);

                // Calcular punto medio
                FontMetrics fm = g2d.getFontMetrics();
                int centerX = (x1 + x2) / 2;
                int centerY = (y1 + y2) / 2;
                int textWidth = fm.stringWidth(simbolo);

// Calcular desplazamiento perpendicular
                double dx = x2 - x1;
                double dy = y2 - y1;
                double length = Math.sqrt(dx * dx + dy * dy);
                double offsetX = -dy / length * 12; // 12 píxeles de desplazamiento
                double offsetY = dx / length * 12;

                int labelX = (int) (centerX + offsetX);
                int labelY = (int) (centerY + offsetY);

// Fondo para etiqueta
                g2d.setColor(LABEL_BG_COLOR);
                g2d.fillRect(labelX - textWidth / 2 - 2, labelY - fm.getAscent(), textWidth + 4, fm.getHeight());

// Texto
                g2d.setColor(TEXT_COLOR);
                g2d.drawString(simbolo, labelX - textWidth / 2, labelY);

            }

            /**
             * Dibuja todos los estados del AFD
             */
            private static void dibujarEstados(Graphics2D g2d, AFD afd, Map<String, Point> posiciones) {
                FontMetrics fm = g2d.getFontMetrics();

                // Verificar si hay estados definidos
                if (afd.getEstados() == null || posiciones == null) {
                    return;
                }

                for (Map.Entry<String, Point> entry : posiciones.entrySet()) {
                    String estado = entry.getKey();
                    Point p = entry.getValue();

                    // Determinar color del estado
                    boolean esFinal = afd.getEstadosFinales() != null
                            && afd.getEstadosFinales().contains(estado);
                    g2d.setColor(esFinal ? FINAL_STATE_COLOR : STATE_COLOR);

                    // Dibujar estado
                    g2d.fillOval(p.x - STATE_SIZE / 2, p.y - STATE_SIZE / 2, STATE_SIZE, STATE_SIZE);
                    g2d.setColor(TEXT_COLOR);
                    g2d.drawOval(p.x - STATE_SIZE / 2, p.y - STATE_SIZE / 2, STATE_SIZE, STATE_SIZE);

                    // Doble círculo para estados finales
                    if (esFinal) {
                        g2d.drawOval(
                                p.x - STATE_SIZE / 2 + 4,
                                p.y - STATE_SIZE / 2 + 4,
                                STATE_SIZE - 8,
                                STATE_SIZE - 8
                        );
                    }

                    // Nombre del estado (centrado)
                    String nombre = estado.length() > 5 ? estado.substring(0, 5) + "..." : estado;
                    int textWidth = fm.stringWidth(nombre);
                    g2d.drawString(nombre, p.x - textWidth / 2, p.y + fm.getAscent() / 3);
                }
            }

            @Override
            public Dimension getPreferredSize() {
                try {
                    Map<String, Point> posiciones = calcularDisposicionEstados(afd);
                    int maxX = posiciones.values().stream().mapToInt(p -> p.x).max().orElse(0) + STATE_SIZE + INITIAL_ARROW_LENGTH;
                    int maxY = posiciones.values().stream().mapToInt(p -> p.y).max().orElse(0) + STATE_SIZE;
                    return new Dimension(Math.max(maxX + 50, 400), Math.max(maxY + 50, 300)); // Mínimo 400x300
                } catch (Exception e) {
                    return new Dimension(400, 300); // Tamaño por defecto en caso de error
                }
            }
        };

        panel.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(canvas);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Calcula las posiciones de los estados en el canvas
     *
     * @throws IllegalStateException si el AFD no tiene estado inicial definido
     */
    private static Map<String, Point> calcularDisposicionEstados(AFD afd) {
        Map<String, Point> posiciones = new LinkedHashMap<>();
        Set<String> visitados = new HashSet<>();
        Queue<String> cola = new LinkedList<>();

        // Inicializar variables de posición
        int xInicial = STATE_SIZE + INITIAL_ARROW_LENGTH;
        int yInicial = STATE_SIZE * 2;
        int nivel = 0;

        // Verificar si el AFD tiene estado inicial
        if (afd.getEstadoInicial() == null) {
            throw new IllegalArgumentException("El AFD no tiene estado inicial definido");
        }

        // BFS para disposición jerárquica
        cola.add(afd.getEstadoInicial());
        visitados.add(afd.getEstadoInicial());

        while (!cola.isEmpty()) {
            int tamañoNivel = cola.size();
            int y = yInicial;

            for (int i = 0; i < tamañoNivel; i++) {
                String estado = cola.poll();
                if (estado == null) {
                    continue;
                }

                posiciones.put(estado, new Point(xInicial + nivel * HORIZONTAL_SPACING, y));
                y += VERTICAL_SPACING;

                // Agregar estados destino no visitados
                Map<String, String> transiciones = afd.getTransiciones().getOrDefault(estado, Collections.emptyMap());
                for (String e : transiciones.values()) {
                    if (!visitados.contains(e)) {
                        visitados.add(e);
                        cola.add(e);
                    }
                }
            }
            nivel++;
        }

        // Agregar estados aislados (sin transiciones)
        for (String e : afd.getEstados()) {
            if (!posiciones.containsKey(e)) {
                posiciones.put(e, new Point(xInicial + nivel * HORIZONTAL_SPACING, yInicial));
            }
        }

        return posiciones;
    }

    // Resto de métodos permanecen igual...
    // [Mantener todos los demás métodos sin cambios]
    /**
     * Dibuja la flecha que indica el estado inicial
     */
    private static void dibujarFlechaInicial(Graphics2D g2d, AFD afd, Map<String, Point> posiciones) {
        if (afd.getEstadoInicial() == null) {
            return;
        }

        Point p = posiciones.get(afd.getEstadoInicial());
        if (p == null) {
            return;
        }

        g2d.setColor(TEXT_COLOR);
        g2d.setStroke(new BasicStroke(2f));

        // Línea
        int startX = p.x - STATE_SIZE / 2 - INITIAL_ARROW_LENGTH;
        int endX = p.x - STATE_SIZE / 2;
        g2d.drawLine(startX, p.y, endX, p.y);

        // Punta de flecha
        g2d.fillPolygon(
                new int[]{endX, endX - ARROW_SIZE, endX - ARROW_SIZE},
                new int[]{p.y, p.y - ARROW_SIZE / 2, p.y + ARROW_SIZE / 2},
                3
        );
    }
}
