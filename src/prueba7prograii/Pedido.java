/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package prueba7prograii;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pedido {

    private int id;
    private final LocalDateTime fecha;
    private final List<DetallePedido> detalles = new ArrayList<>();
    private ModalidadServicio modalidad;
    private EstadoPedido estado;

    public Pedido() {
        this(0, LocalDateTime.now(), ModalidadServicio.CONSUMO_LOCAL, EstadoPedido.PENDIENTE);
    }

    private Pedido(int id, LocalDateTime fecha, ModalidadServicio modalidad, EstadoPedido estado) {
        this.id = id;
        this.fecha = fecha;
        this.modalidad = modalidad;
        this.estado = estado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFechaFormateada() {
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public ModalidadServicio getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadServicio m) {
        this.modalidad = m;
    }

    public EstadoPedido getEstado() {
        return estado;
    }

    public void setEstado(EstadoPedido e) {
        this.estado = e;
    }

    public List<DetallePedido> getDetalles() {
        return Collections.unmodifiableList(detalles);
    }

    public void agregarProducto(Producto p, int cantidad) {
        for (DetallePedido d : detalles) {
            if (d.getProducto().getCodigo() == p.getCodigo()) {
                d.sumarCantidad(cantidad);
                return;
            }
        }
        detalles.add(new DetallePedido(p, cantidad));
    }

    public void eliminarDetalle(int indice) {
        detalles.remove(indice);
    }

    public double calcularTotal() {
        return calcularTotal(0);
    }

    private double calcularTotal(int indice) {
        if (indice >= detalles.size()) {
            return 0;
        }
        return detalles.get(indice).getSubtotal() + calcularTotal(indice + 1);
    }

    public void guardar(RandomAccessFile raf) throws IOException {
        raf.writeInt(id);
        raf.writeUTF(fecha.toString());
        raf.writeUTF(modalidad.name());
        raf.writeUTF(estado.name());
        raf.writeInt(detalles.size());
        for (DetallePedido d : detalles) {
            raf.writeInt(d.getProducto().getCodigo());
            raf.writeInt(d.getCantidad());
        }
    }

    static Pedido leer(RandomAccessFile raf, List<Producto> catalogo) throws IOException {
        int id = raf.readInt();
        LocalDateTime fecha = LocalDateTime.parse(raf.readUTF());
        ModalidadServicio mod = ModalidadServicio.valueOf(raf.readUTF());
        EstadoPedido est = EstadoPedido.valueOf(raf.readUTF());
        Pedido p = new Pedido(id, fecha, mod, est);
        int n = raf.readInt();
        for (int i = 0; i < n; i++) {
            int codigo = raf.readInt();
            int cantidad = raf.readInt();
            Producto prod = null;
            for (Producto c : catalogo) {
                if (c.getCodigo() == codigo) {
                    prod = c;
                }
            }
            if (prod == null) {
                throw new IOException("El pedido " + id + " usa un producto inexistente (código " + codigo + ").");
            }
            p.agregarProducto(prod, cantidad);
        }
        return p;
    }
}
