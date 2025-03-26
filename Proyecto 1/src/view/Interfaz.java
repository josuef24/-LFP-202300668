/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package view;

import controller.Analizador;
import controller.Graficador;
import model.AFD;
import controller.Reporte;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 * @author jmfuente
 */
public class Interfaz extends JFrame {

    private final Analizador analizador;
    private final JButton btnAnalizarArchivo;
    private final JButton btnGraficar;
    private final JButton btnGenerarReporte;
    private final JComboBox<String> comboAFDs;
    private final JPanel panelDibujo;
    private final JTextArea areaTexto;
    private String contenidoOriginal = "";

    public Interfaz() {
        setTitle("AFDGraph");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        analizador = new Analizador();

        JPanel panelSuperior = new JPanel();
        btnAnalizarArchivo = new JButton("Analizar Archivo");
        btnGenerarReporte = new JButton("Generar Reporte");
        comboAFDs = new JComboBox<>();
        btnGraficar = new JButton("Graficar");

        panelSuperior.add(btnAnalizarArchivo);
        panelSuperior.add(btnGenerarReporte);
        panelSuperior.add(comboAFDs);
        panelSuperior.add(btnGraficar);

        add(panelSuperior, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new GridLayout(1, 2));

        areaTexto = new JTextArea();
        areaTexto.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaTexto.setEditable(false);
        JScrollPane scrollTexto = new JScrollPane(areaTexto);
        panelCentro.add(scrollTexto);

        panelDibujo = new JPanel();
        panelDibujo.setPreferredSize(new Dimension(1200, 1200));
        panelDibujo.setBackground(Color.WHITE);

        JScrollPane scrollDibujo = new JScrollPane(panelDibujo);
        scrollDibujo.setPreferredSize(new Dimension(1200, 1200));
        panelCentro.add(scrollDibujo);

        add(panelCentro, BorderLayout.CENTER);

        btnAnalizarArchivo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                contenidoOriginal = analizador.analizarArchivoConRetorno(Interfaz.this);
                actualizarInterfazDespuesDeAnalisis();
            }
        });

        btnGraficar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                graficarAFDSeleccionado();
            }
        });

        btnGenerarReporte.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Reporte.generarReporteGeneral(contenidoOriginal, Interfaz.this);
                JOptionPane.showMessageDialog(Interfaz.this, "Reporte generado correctamente en la carpeta 'reportes/'");
            }
        });

        setVisible(true);
    }

    private void actualizarInterfazDespuesDeAnalisis() {
        areaTexto.setText(contenidoOriginal);
        actualizarComboBox();

        // Mostrar resumen de análisis
        StringBuilder resumen = new StringBuilder();
        resumen.append("=== RESULTADO DEL ANÁLISIS ===\n");
        resumen.append("Total AFDs cargados: ").append(analizador.getAFDs().size()).append("\n");

        int afdsValidos = 0;
        for (String nombreAFD : analizador.getAFDs().keySet()) {
            if (!analizador.tieneErrores(nombreAFD)) {
                afdsValidos++;
            }
        }
        resumen.append("AFDs válidos: ").append(afdsValidos).append("\n");
        resumen.append("AFDs con errores: ").append(analizador.getAFDs().size() - afdsValidos).append("\n");

        JOptionPane.showMessageDialog(this, resumen.toString(), "Resultado del análisis", JOptionPane.INFORMATION_MESSAGE);
    }

    private void graficarAFDSeleccionado() {
        String seleccionado = (String) comboAFDs.getSelectedItem();
        if (seleccionado == null) {
            JOptionPane.showMessageDialog(this,
                    "Seleccione un AFD primero",
                    "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Verificar existencia del AFD
        if (!analizador.getAFDs().containsKey(seleccionado)) {
            JOptionPane.showMessageDialog(this,
                    "El AFD seleccionado no existe",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verificar errores
        if (analizador.tieneErrores(seleccionado)) {
            mostrarErroresAFD(seleccionado);
            return;
        }

        // Graficar el AFD válido
        AFD afd = analizador.getAFDs().get(seleccionado);
        panelDibujo.removeAll();
        Graficador.graficarAFD(afd, panelDibujo);
        panelDibujo.revalidate();
        panelDibujo.repaint();
    }

    private void mostrarErroresAFD(String nombreAFD) {
        List<String> errores = analizador.getErroresPorAFD(nombreAFD);

        if (errores.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "El AFD '" + nombreAFD + "' es válido y puede ser graficado",
                    "AFD Válido", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("El AFD '").append(nombreAFD).append("' tiene ").append(errores.size()).append(" errores:\n\n");

        for (int i = 0; i < errores.size(); i++) {
            mensaje.append(i + 1).append(". ").append(errores.get(i)).append("\n");
        }

        mensaje.append("\nNo se puede graficar hasta corregir los errores.");

        JTextArea areaErrores = new JTextArea(mensaje.toString());
        areaErrores.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(areaErrores);
        scrollPane.setPreferredSize(new Dimension(500, 300));

        JOptionPane.showMessageDialog(this, scrollPane,
                "Errores en AFD " + nombreAFD, JOptionPane.ERROR_MESSAGE);
    }

    private void actualizarComboBox() {
        comboAFDs.removeAllItems();
        for (String nombre : analizador.getAFDs().keySet()) {
            comboAFDs.addItem(nombre);
        }

        // Seleccionar el primer AFD si existe
        if (comboAFDs.getItemCount() > 0) {
            comboAFDs.setSelectedIndex(0);
        }
    }

}
