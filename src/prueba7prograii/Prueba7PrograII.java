package prueba7prograii;

import javax.swing.SwingUtilities;

public class Prueba7PrograII {

    public static void main(String[] args) {
        Restaurante rest = new Restaurante();
        
        try {
            if (PanelAlmacenamiento.ARCHIVO_PRODUCTOS.exists()) {
                rest.cargarProductos(PanelAlmacenamiento.ARCHIVO_PRODUCTOS);
            }
            if (PanelAlmacenamiento.ARCHIVO_PEDIDOS.exists()) {
                rest.cargarPedidos(PanelAlmacenamiento.ARCHIVO_PEDIDOS);
            }
        } catch (Exception ex) {
            System.err.println("No se pudieron cargar los datos previos: " + ex.getMessage());
        }
        SwingUtilities.invokeLater(() -> new VentanaPrincipal(rest).setVisible(true));
    }
}