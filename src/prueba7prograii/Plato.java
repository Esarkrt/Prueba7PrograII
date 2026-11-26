/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba7prograii;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Plato extends Producto {

    private final int tiempoPreparacion; // minutos

    public Plato(int codigo, String nombre, double precio, int tiempoPreparacion) {
        super(codigo, nombre, precio);
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    @Override
    public CategoriaProducto getCategoria() {
        return CategoriaProducto.PLATO;
    }

    @Override
    public String getDetalleEspecifico() {
        return "Preparación: " + tiempoPreparacion + " min";
    }

    @Override
    protected void guardarExtras(RandomAccessFile raf) throws IOException {
        raf.writeInt(tiempoPreparacion);
    }
}
