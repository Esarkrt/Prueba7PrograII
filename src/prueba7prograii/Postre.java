package prueba7prograii;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Postre extends Producto {

    private final int calorias;

    public Postre(int codigo, String nombre, double precio, int calorias) {
        super(codigo, nombre, precio);
        this.calorias = calorias;
    }

    public int getCalorias() {
        return calorias;
    }

    @Override
    public CategoriaProducto getCategoria() {
        return CategoriaProducto.POSTRE;
    }

    @Override
    public String getDetalleEspecifico() {
        return calorias + " kcal";
    }

    @Override
    protected void guardarExtras(RandomAccessFile raf) throws IOException {
        raf.writeInt(calorias);
    }
}