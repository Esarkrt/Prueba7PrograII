package prueba7prograii;

import java.io.IOException;
import java.io.RandomAccessFile;

public class Bebida extends Producto {

    private final int tamanoMl;
    private final boolean alcoholica;

    public Bebida(int codigo, String nombre, double precio, int tamanoMl, boolean alcoholica) {
        super(codigo, nombre, precio);
        this.tamanoMl = tamanoMl;
        this.alcoholica = alcoholica;
    }

    public int getTamanoMl() {
        return tamanoMl;
    }

    public boolean isAlcoholica() {
        return alcoholica;
    }

    @Override
    public CategoriaProducto getCategoria() {
        return CategoriaProducto.BEBIDA;
    }

    @Override
    public String getDetalleEspecifico() {
        return tamanoMl + " ml" + (alcoholica ? ", con alcohol" : ", sin alcohol");
    }

    @Override
    protected void guardarExtras(RandomAccessFile raf) throws IOException {
        raf.writeInt(tamanoMl);
        raf.writeBoolean(alcoholica);
    }
}
