/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba7prograii;

public enum EstadoPedido {
    PENDIENTE("Pendiente"), EN_PREPARACION("En preparación"), LISTO("Listo"), ENTREGADO("Entregado");
    private final String etiqueta;
    EstadoPedido(String etiqueta) { this.etiqueta = etiqueta; }
    @Override public String toString() { return etiqueta; }
}
