/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.AFD;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.Map;

/**
 *
 * @author jmfuente
 */
public class Graficador {

    public static void graficarAFD(AFD afd, JPanel panel) {
        panel.removeAll();

        JPanel canvas = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int radio = 40;
                int centroX = panel.getWidth() / 2;
                int centroY = panel.getHeight() / 2;

                Map<String, Map<String, String>> trans = afd.getTransiciones();
                int cantidad = afd.getEstados().size();
                int angulo = 360 / cantidad;
                int index = 0;

                Map<String, Point> posiciones = new java.util.HashMap<>();

                for (String estado : afd.getEstados()) {
                    int x = (int) (centroX + 200 * Math.cos(Math.toRadians(angulo * index)));
                    int y = (int) (centroY + 200 * Math.sin(Math.toRadians(angulo * index)));
                    posiciones.put(estado, new Point(x, y));
                    index++;
                }

                // Dibujar transiciones
                for (String desde : trans.keySet()) {
                    Map<String, String> mapaInterno = trans.get(desde);

                    for (String simbolo : mapaInterno.keySet()) {
                        String hacia = mapaInterno.get(simbolo);
                        Point p1 = posiciones.get(desde);
                        Point p2 = posiciones.get(hacia);

                        if (p1 == null || p2 == null) {
                            System.out.println("Estado no encontrado en posiciones: " + desde + " o " + hacia);
                            continue;
                        }

                        System.out.println("Dibujando transición: " + desde + " --" + simbolo + "--> " + hacia);

                        if (p1.equals(p2)) {
                            // Loop
                            g2d.drawArc(p1.x - 10, p1.y - 50, 30, 30, 0, 360);
                            g2d.drawString(simbolo, p1.x + 10, p1.y - 35);
                        } else {
                            // Curva entre estados diferentes
                            int midX = (p1.x + p2.x) / 2;
                            int midY = (p1.y + p2.y) / 2;
                            int ctrlX = midX + (p1.y - p2.y) / 4;
                            int ctrlY = midY + (p2.x - p1.x) / 4;

                            QuadCurve2D q = new QuadCurve2D.Float();
                            q.setCurve(p1.x, p1.y, ctrlX, ctrlY, p2.x, p2.y);
                            g2d.draw(q);

                            int labelX = (p1.x + p2.x + ctrlX) / 3;
                            int labelY = (p1.y + p2.y + ctrlY) / 3;
                            g2d.drawString(simbolo, labelX, labelY);

                            // Flecha
                            drawArrowHead(g2d, ctrlX, ctrlY, p2.x, p2.y);
                        }
                    }
                }

                // Dibujar estados
                for (String estado : posiciones.keySet()) {
                    Point p = posiciones.get(estado);
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(p.x - radio / 2, p.y - radio / 2, radio, radio);
                    g2d.setColor(Color.BLACK);
                    g2d.drawOval(p.x - radio / 2, p.y - radio / 2, radio, radio);

                    if (afd.getEstadosFinales().contains(estado)) {
                        g2d.drawOval(p.x - radio / 2 + 4, p.y - radio / 2 + 4, radio - 8, radio - 8);
                    }

                    g2d.drawString(estado, p.x - 10, p.y + 5);
                }

                // Indicar estado inicial
                Point inicio = posiciones.get(afd.getEstadoInicial());
                if (inicio != null) {
                    g2d.drawLine(inicio.x - 60, inicio.y, inicio.x - 20, inicio.y);
                    g2d.drawString("→", inicio.x - 75, inicio.y + 5);
                }
            }

            private void drawArrowHead(Graphics2D g2d, int x1, int y1, int x2, int y2) {
                double phi = Math.toRadians(30);
                int barb = 10;
                double dy = y2 - y1;
                double dx = x2 - x1;
                double theta = Math.atan2(dy, dx);
                int x, y;

                for (int j = 0; j < 2; j++) {
                    double rho = theta + (j == 0 ? phi : -phi);
                    x = (int) (x2 - barb * Math.cos(rho));
                    y = (int) (y2 - barb * Math.sin(rho));
                    g2d.drawLine(x2, y2, x, y);
                }
            }
        };

        canvas.setPreferredSize(panel.getSize());
        panel.setLayout(new BorderLayout());
        panel.add(canvas, BorderLayout.CENTER);
        panel.revalidate();
        panel.repaint();
    }
}
