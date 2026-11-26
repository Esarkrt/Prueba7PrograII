/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba7prograii;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private final CardLayout cards = new CardLayout();
    private final JPanel contenido = new JPanel(cards);
    private final PanelProductos productos;
    private final PanelPedidos pedidos;
    private final PanelConsulta consulta;

    public VentanaPrincipal(Restaurante rest) {
        super("Sistema de Gestión de Pedidos - Restaurante");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(950, 600);
        setLocationRelativeTo(null);

        productos = new PanelProductos(rest);
        pedidos = new PanelPedidos(rest);
        consulta = new PanelConsulta(rest);

        JLabel titulo = new JLabel("Sistema de Gestión de Pedidos del Restaurante", SwingConstants.CENTER);
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 24f));
        JPanel inicio = new JPanel(new BorderLayout());
        inicio.add(titulo, BorderLayout.CENTER);

        contenido.add(inicio, "inicio");
        contenido.add(productos, "productos");
        contenido.add(pedidos, "pedidos");
        contenido.add(consulta, "consulta");
        contenido.add(new PanelAlmacenamiento(rest), "almacenamiento");

        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER));
        nav.add(boton("Inicio", "inicio"));
        nav.add(boton("Gestión de productos", "productos"));
        nav.add(boton("Gestión de pedidos", "pedidos"));
        nav.add(boton("Consulta de pedidos", "consulta"));
        nav.add(boton("Almacenamiento", "almacenamiento"));

        add(nav, BorderLayout.NORTH);
        add(contenido, BorderLayout.CENTER);
    }

    private JButton boton(String texto, String vista) {
        JButton b = new JButton(texto);
        b.addActionListener(e -> {
            productos.refrescar();
            pedidos.refrescar();
            consulta.refrescar();
            cards.show(contenido, vista);
        });
        return b;
    }
}
