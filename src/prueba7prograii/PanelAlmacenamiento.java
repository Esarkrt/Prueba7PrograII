/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba7prograii;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class PanelAlmacenamiento extends JPanel {

    public static final File ARCHIVO_PRODUCTOS = new File("productos.dat");
    public static final File ARCHIVO_PEDIDOS = new File("pedidos.dat");
    private final Restaurante rest;
    private final JTextArea log = new JTextArea();

    public PanelAlmacenamiento(Restaurante rest) {
        this.rest = rest;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton btnGuardar = new JButton("Guardar datos");
        JButton btnCargar = new JButton("Recuperar datos");
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        botones.add(btnGuardar);
        botones.add(btnCargar);
        log.setEditable(false);
        add(botones, BorderLayout.NORTH);
        add(new JScrollPane(log), BorderLayout.CENTER);

        btnGuardar.addActionListener(e -> guardar());
        btnCargar.addActionListener(e -> cargar());
    }

    private void guardar() {
        try {
            rest.guardarProductos(ARCHIVO_PRODUCTOS);
            rest.guardarPedidos(ARCHIVO_PEDIDOS);
            informar("Datos guardados en " + ARCHIVO_PRODUCTOS.getAbsolutePath() + " y " + ARCHIVO_PEDIDOS.getName(), false);
        } catch (IOException ex) {
            informar("Error al guardar: " + ex.getMessage(), true);
        }
    }

    private void cargar() {
        try {
            rest.cargarProductos(ARCHIVO_PRODUCTOS);   
            rest.cargarPedidos(ARCHIVO_PEDIDOS);       
            informar("Datos recuperados: " + rest.getProductos().size() + " productos y "
                    + rest.getPedidos().size() + " pedidos.", false);
        } catch (IOException ex) {
            informar("Error al recuperar (¿existen los archivos?): " + ex.getMessage(), true);
        }
    }

    private void informar(String msg, boolean error) {
        log.append(msg + "\n");
        JOptionPane.showMessageDialog(this, msg, error ? "Error" : "Operación exitosa",
                error ? JOptionPane.ERROR_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
    }
}
