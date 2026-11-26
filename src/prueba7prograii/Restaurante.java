/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba7prograii;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class Restaurante {

    private final List<Producto> productos = new ArrayList<>();
    private final List<Pedido> pedidos = new ArrayList<>();
    private final Contador contProductos = new Contador(new File("cod_productos.dat"));
    private final Contador contPedidos = new Contador(new File("cod_pedidos.dat"));

    public List<Producto> getProductos() {
        return productos;
    }

    public List<Pedido> getPedidos() {
        return pedidos;
    }

    public int siguienteCodigoProducto() throws IOException {
        return contProductos.siguiente();
    }

    public Producto buscarProducto(int codigo) {
        for (Producto p : productos) {
            if (p.getCodigo() == codigo) {
                return p;
            }
        }
        return null;
    }

    public void registrarProducto(Producto p) {
        if (buscarProducto(p.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el código " + p.getCodigo());
        }
        productos.add(p);
    }

    public void registrarPedido(Pedido p) throws IOException {
        if (p.getDetalles().isEmpty()) {
            throw new IllegalArgumentException("No se puede crear un pedido sin productos.");
        }
        p.setId(contPedidos.siguiente());
        pedidos.add(p);
    }

    public void guardarProductos(File f) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(f, "rw")) {
            raf.setLength(0);
            raf.writeInt(productos.size());
            for (Producto p : productos) {
                p.guardar(raf);
            }
        }
    }

    public void cargarProductos(File f) throws IOException {
        List<Producto> leidos = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
            int n = raf.readInt();
            for (int i = 0; i < n; i++) {
                leidos.add(Producto.leer(raf));
            }
        } catch (RuntimeException ex) {
            throw new IOException("El archivo de productos está dañado.", ex);
        }
        productos.clear();
        productos.addAll(leidos);
        int max = 0;
        for (Producto p : productos) {
            max = Math.max(max, p.getCodigo());
        }
        contProductos.asegurarMinimo(max + 1);
    }

    public void guardarPedidos(File f) throws IOException {
        try (RandomAccessFile raf = new RandomAccessFile(f, "rw")) {
            raf.setLength(0);
            raf.writeInt(pedidos.size());
            for (Pedido p : pedidos) {
                p.guardar(raf);
            }
        }
    }

    public void cargarPedidos(File f) throws IOException {
        List<Pedido> leidos = new ArrayList<>();
        try (RandomAccessFile raf = new RandomAccessFile(f, "r")) {
            int n = raf.readInt();
            for (int i = 0; i < n; i++) {
                leidos.add(Pedido.leer(raf, productos));
            }
        } catch (RuntimeException ex) {
            throw new IOException("El archivo de pedidos está dañado.", ex);
        }
        pedidos.clear();
        pedidos.addAll(leidos);
        int max = 0;
        for (Pedido p : pedidos) {
            max = Math.max(max, p.getId());
        }
        contPedidos.asegurarMinimo(max + 1);
    }
}
