/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba7prograii;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class PanelProductos extends JPanel {

    private final Restaurante rest;
    private final JTextField txtNombre = new JTextField();
    private final JTextField txtPrecio = new JTextField();
    private final JComboBox<CategoriaProducto> cmbCategoria = new JComboBox<>(CategoriaProducto.values());
    private final JLabel lblExtra = new JLabel();
    private final JTextField txtExtra = new JTextField();
    private final JCheckBox chkAlcohol = new JCheckBox("Contiene alcohol");
    private final JComboBox<Object> cmbFiltro = new JComboBox<>();
    private final DefaultTableModel modelo = new DefaultTableModel(
            new String[]{"Código", "Categoría", "Nombre", "Precio", "Detalle"}, 0) {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    };

    public PanelProductos(Restaurante rest) {
        this.rest = rest;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel form = new JPanel(new GridLayout(0, 2, 5, 5));
        form.setBorder(BorderFactory.createTitledBorder("Registrar producto"));
        form.add(new JLabel("Código:"));
        form.add(new JLabel("(se genera automáticamente)"));
        form.add(new JLabel("Categoría:"));
        form.add(cmbCategoria);
        form.add(new JLabel("Nombre:"));
        form.add(txtNombre);
        form.add(new JLabel("Precio:"));
        form.add(txtPrecio);
        form.add(lblExtra);
        form.add(txtExtra);
        form.add(new JLabel(""));
        form.add(chkAlcohol);
        JButton btnRegistrar = new JButton("Registrar producto");
        form.add(new JLabel(""));
        form.add(btnRegistrar);

        cmbCategoria.addActionListener(e -> actualizarCamposExtra());
        btnRegistrar.addActionListener(e -> registrar());
        actualizarCamposExtra();

        cmbFiltro.addItem("Todas");
        for (CategoriaProducto c : CategoriaProducto.values()) {
            cmbFiltro.addItem(c);
        }
        cmbFiltro.addActionListener(e -> refrescar());
        JPanel filtro = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filtro.add(new JLabel("Ver categoría:"));
        filtro.add(cmbFiltro);

        JPanel centro = new JPanel(new BorderLayout());
        centro.add(filtro, BorderLayout.NORTH);
        centro.add(new JScrollPane(new JTable(modelo)), BorderLayout.CENTER);

        add(form, BorderLayout.WEST);
        add(centro, BorderLayout.CENTER);
    }

    private void actualizarCamposExtra() {
        CategoriaProducto c = (CategoriaProducto) cmbCategoria.getSelectedItem();
        switch (c) {
            case PLATO:
                lblExtra.setText("Tiempo de preparación (min):");
                break;
            case BEBIDA:
                lblExtra.setText("Tamaño (ml):");
                break;
            default:
                lblExtra.setText("Calorías (kcal):");
                break;
        }
        chkAlcohol.setVisible(c == CategoriaProducto.BEBIDA);
    }

    private void registrar() {
        try {
            String nombre = txtNombre.getText().trim();
            if (nombre.isEmpty()) {
                throw new IllegalArgumentException("El nombre es obligatorio.");
            }
            double precio = Double.parseDouble(txtPrecio.getText().trim().replace(',', '.'));
            int extra = Integer.parseInt(txtExtra.getText().trim());
            if (precio <= 0 || extra <= 0) {
                throw new IllegalArgumentException("El precio y el valor adicional tienen que ser mayores que cero.");
            }

            int codigo = rest.siguienteCodigoProducto();   
            Producto p;
            switch ((CategoriaProducto) cmbCategoria.getSelectedItem()) {
                case PLATO:
                    p = new Plato(codigo, nombre, precio, extra);
                    break;
                case BEBIDA:
                    p = new Bebida(codigo, nombre, precio, extra, chkAlcohol.isSelected());
                    break;
                default:
                    p = new Postre(codigo, nombre, precio, extra);
                    break;
            }
            rest.registrarProducto(p);
            txtNombre.setText("");
            txtPrecio.setText("");
            txtExtra.setText("");
            chkAlcohol.setSelected(false);
            refrescar();
            JOptionPane.showMessageDialog(this, "Producto registrado con el código " + codigo + ".");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Precio y valor adicional deben ser números válidos.",
                    "Dato inválido", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dato inválido", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "No se pudo generar el código: " + ex.getMessage(),
                    "Error de archivo", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void refrescar() {
        modelo.setRowCount(0);
        Object filtro = cmbFiltro.getSelectedItem();
        for (Producto p : rest.getProductos()) {
            if (filtro instanceof CategoriaProducto && p.getCategoria() != filtro) {
                continue;
            }
            modelo.addRow(new Object[]{p.getCodigo(), p.getCategoria(), p.getNombre(),
                String.format("%.2f", p.getPrecio()), p.getDetalleEspecifico()});
        }
    }
}
