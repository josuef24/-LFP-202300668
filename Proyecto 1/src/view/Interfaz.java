/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.Analizador;
import controller.Graficador;
import model.AFD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 *
 * @author jmfuente
 */
public class Interfaz extends JFrame {

    private JButton btnAnalizarArchivo;
    private JButton btnGraficar;
    private JButton btnGenerarReporte;
    private JComboBox<String> comboAFDs;
    private JPanel panelDibujo;
    private Analizador analizador;

    public Interfaz() {
        setTitle("AFDGraph - Proyecto 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        analizador = new Analizador();

        // Panel superior con botones y combo box
        JPanel panelSuperior = new JPanel();
        btnAnalizarArchivo = new JButton("Analizar Archivo");
        btnGraficar = new JButton("Graficar");
        btnGenerarReporte = new JButton("Generar Reportes");
        comboAFDs = new JComboBox<>();

        panelSuperior.add(btnAnalizarArchivo);
        panelSuperior.add(comboAFDs);
        panelSuperior.add(btnGraficar);
        panelSuperior.add(btnGenerarReporte);

        add(panelSuperior, BorderLayout.NORTH);

        // Panel para el dibujo del autómata
        panelDibujo = new JPanel();
        panelDibujo.setBackground(Color.WHITE);
        add(panelDibujo, BorderLayout.CENTER);

        // Listener para Analizar Archivo
        btnAnalizarArchivo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                analizador.analizarArchivo(Interfaz.this);
                comboAFDs.removeAllItems();
                for (String nombre : analizador.getAFDs().keySet()) {
                    comboAFDs.addItem(nombre);
                }
            }
        });

        // Listener para Graficar
        btnGraficar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String seleccionado = (String) comboAFDs.getSelectedItem();
                if (seleccionado != null) {
                    AFD afd = analizador.getAFDs().get(seleccionado);
                    
                    System.out.println("TRANSICIONES:");
                    System.out.println(afd.getTransiciones());

                    Graficador.graficarAFD(afd, panelDibujo);
                } else {
                    JOptionPane.showMessageDialog(Interfaz.this, "No hay AFD seleccionado.");
                }
            }
        });

        // Listener para Generar Reporte (pendiente)
        btnGenerarReporte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Generar reportes
            }
        });

        setVisible(true);
    }

    public JPanel getPanelDibujo() {
        return panelDibujo;
    }

    public JComboBox<String> getComboAFDs() {
        return comboAFDs;
    }
}
