package prueba7prograii;

import java.io.IOException;
import java.io.RandomAccessFile;

public abstract class Producto {

    private final int codigo;
    private String nombre;
    private double precio;

    protected Producto(int codigo, String nombre, double precio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public abstract CategoriaProducto getCategoria();

    public abstract String getDetalleEspecifico();

    protected abstract void guardarExtras(RandomAccessFile raf) throws IOException;

    public void guardar(RandomAccessFile raf) throws IOException {
        raf.writeUTF(getCategoria().name());
        raf.writeInt(codigo);
        raf.writeUTF(nombre);
        raf.writeDouble(precio);
        guardarExtras(raf);
    }

    public static Producto leer(RandomAccessFile raf) throws IOException {
        CategoriaProducto cat = CategoriaProducto.valueOf(raf.readUTF());
        int codigo = raf.readInt();
        String nombre = raf.readUTF();
        double precio = raf.readDouble();
        switch (cat) {
            case PLATO:
                return new Plato(codigo, nombre, precio, raf.readInt());
            case BEBIDA: {
                int ml = raf.readInt();
                boolean alcohol = raf.readBoolean();
                return new Bebida(codigo, nombre, precio, ml, alcohol);
            }
            default:
                return new Postre(codigo, nombre, precio, raf.readInt());
        }
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + String.format("%.2f", precio) + ")";
    }
}
