package prueba7prograii;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
public enum ModalidadServicio {
    CONSUMO_LOCAL("Consumo en el establecimiento"), DOMICILIO("Pedido a domicilio");
    private final String etiqueta;

    ModalidadServicio(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
