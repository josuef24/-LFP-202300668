/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

    public Interfaz() {
        setTitle("AFDGraph - Proyecto 1");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

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

        // Listeners (vacíos por ahora)
        btnAnalizarArchivo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Analizar archivo .lfp
            }
        });

        btnGraficar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO: Graficar autómata seleccionado
            }
        });

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
