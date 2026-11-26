package prueba7prograii;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelConsulta extends JPanel {

    private final Restaurante rest;
    private final JTable tabla;
    private final JTextArea txtDetalle = new JTextArea(8, 30);
    private final JComboBox<EstadoPedido> cmbEstado = new JComboBox<>(EstadoPedido.values());
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"ID", "Fecha", "Modalidad", "Total", "Estado"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    public PanelConsulta(Restaurante rest) {
        this.rest = rest;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        tabla = new JTable(modelo);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                mostrarDetalle();
            }
        });
        txtDetalle.setEditable(false);

        JPanel abajo = new JPanel(new BorderLayout(5, 5));
        abajo.setBorder(BorderFactory.createTitledBorder("Detalle del pedido seleccionado"));
        abajo.add(new JScrollPane(txtDetalle), BorderLayout.CENTER);
        JPanel estado = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnActualizar = new JButton("Actualizar estado");
        estado.add(new JLabel("Nuevo estado:"));
        estado.add(cmbEstado);
        estado.add(btnActualizar);
        abajo.add(estado, BorderLayout.SOUTH);

        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(abajo, BorderLayout.SOUTH);

        btnActualizar.addActionListener(e -> {
            int fila = tabla.getSelectedRow();
            if (fila < 0) {
                JOptionPane.showMessageDialog(this, "Seleccione un pedido.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            rest.getPedidos().get(fila).setEstado((EstadoPedido) cmbEstado.getSelectedItem());
            refrescar();
            tabla.setRowSelectionInterval(fila, fila);
        });
    }

    private void mostrarDetalle() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= rest.getPedidos().size()) {
            txtDetalle.setText("");
            return;
        }
        Pedido p = rest.getPedidos().get(fila);
        StringBuilder sb = new StringBuilder();
        for (DetallePedido d : p.getDetalles()) {
            sb.append(d.getCantidad()).append(" x ").append(d.getProducto().getNombre())
                    .append("  @ ").append(String.format("%.2f", d.getProducto().getPrecio()))
                    .append("  = ").append(String.format("%.2f", d.getSubtotal())).append("\n");
        }
        sb.append("\nTOTAL: ").append(String.format("%.2f", p.calcularTotal()));
        txtDetalle.setText(sb.toString());
        cmbEstado.setSelectedItem(p.getEstado());
    }

    public void refrescar() {
        modelo.setRowCount(0);
        for (Pedido p : rest.getPedidos()) {
            modelo.addRow(new Object[]{p.getId(), p.getFechaFormateada(), p.getModalidad(),
                String.format("%.2f", p.calcularTotal()), p.getEstado()});
        }
        txtDetalle.setText("");
    }
}