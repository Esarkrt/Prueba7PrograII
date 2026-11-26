package prueba7prograii;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class PanelPedidos extends JPanel {

    private final Restaurante rest;
    private Pedido borrador = new Pedido();
    private final JComboBox<Producto> cmbProducto = new JComboBox<>();
    private final JSpinner spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 999, 1));
    private final JComboBox<ModalidadServicio> cmbModalidad = new JComboBox<>(ModalidadServicio.values());
    private final JLabel lblTotal = new JLabel("Total: 0.00");
    private final JTable tabla;
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Producto", "Cantidad", "Precio unit.", "Subtotal"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    public PanelPedidos(Restaurante rest) {
        this.rest = rest;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel arriba = new JPanel(new FlowLayout(FlowLayout.LEFT));
        arriba.setBorder(BorderFactory.createTitledBorder("Agregar productos al pedido"));
        JButton btnAgregar = new JButton("Agregar");
        arriba.add(new JLabel("Producto:"));
        arriba.add(cmbProducto);
        arriba.add(new JLabel("Cantidad:"));
        arriba.add(spnCantidad);
        arriba.add(btnAgregar);

        tabla = new JTable(modelo);

        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnQuitar = new JButton("Quitar seleccionado");
        JButton btnConfirmar = new JButton("Confirmar pedido");
        lblTotal.setFont(lblTotal.getFont().deriveFont(Font.BOLD, 16f));
        abajo.add(lblTotal);
        abajo.add(new JLabel("Modalidad:"));
        abajo.add(cmbModalidad);
        abajo.add(btnQuitar);
        abajo.add(btnConfirmar);

        add(arriba, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(abajo, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> {
            Producto p = (Producto) cmbProducto.getSelectedItem();
            if (p == null) {
                JOptionPane.showMessageDialog(this, "No hay productos registrados.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            borrador.agregarProducto(p, (Integer) spnCantidad.getValue());
            actualizarDetalle();
        });
        btnQuitar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            borrador.eliminarDetalle(fila);
            actualizarDetalle();
        });
        btnConfirmar.addActionListener(e -> confirmar());
    }

    private void confirmar() {
        try {
            borrador.setModalidad((ModalidadServicio) cmbModalidad.getSelectedItem());
            rest.registrarPedido(borrador);
            JOptionPane.showMessageDialog(this, "Pedido #" + borrador.getId() + " registrado. Total: "
                    + String.format("%.2f", borrador.calcularTotal()));
            borrador = new Pedido();
            actualizarDetalle();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "No se pudo crear", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al generar el ID del pedido: " + ex.getMessage(),
                    "Error de archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarDetalle() {
        modelo.setRowCount(0);
        for (DetallePedido d : borrador.getDetalles()) {
            modelo.addRow(new Object[]{d.getProducto().getNombre(), d.getCantidad(),
                String.format("%.2f", d.getProducto().getPrecio()),
                String.format("%.2f", d.getSubtotal())});
        }
        lblTotal.setText("Total: " + String.format("%.2f", borrador.calcularTotal())); // recursivo
    }

    public void refrescar() {
        cmbProducto.removeAllItems();
        for (Producto p : rest.getProductos()) {
            cmbProducto.addItem(p);
        }
        actualizarDetalle();
    }
}