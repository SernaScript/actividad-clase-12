package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

interface Pagable {
    void procesarPago(double monto);
}

abstract class Producto {
    protected String id;
    protected String nombre;
    protected double precio;
    protected int stock;

    public Producto(String nombre, double precio, int stock) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public int getStock() {
        return stock;
    }

    public void reducirStock(int cantidad) {
        if (cantidad <= stock) {
            stock -= cantidad;
        } else {
            throw new IllegalArgumentException("Stock insuficiente para el producto: " + nombre);
        }
    }

    public abstract String getTipo();
}

class ProductoFisico extends Producto {
    private double peso;

    public ProductoFisico(String nombre, double precio, int stock, double peso) {
        super(nombre, precio, stock);
        this.peso = peso;
    }

    public double getPeso() {
        return peso;
    }

    @Override
    public String getTipo() {
        return "Físico";
    }
}

class ProductoDigital extends Producto {
    private double tamanoMB;

    public ProductoDigital(String nombre, double precio, int stock, double tamanoMB) {
        super(nombre, precio, stock);
        this.tamanoMB = tamanoMB;
    }

    public double getTamanoMB() {
        return tamanoMB;
    }

    @Override
    public String getTipo() {
        return "Digital";
    }
}

class Usuario {
    private String id;
    private String nombre;
    private String correo;

    public Usuario(String nombre, String correo) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.correo = correo;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }
}

class Carrito {
    private Usuario usuario;
    private List<Producto> productos;

    public Carrito(Usuario usuario) {
        this.usuario = usuario;
        this.productos = new ArrayList<>();
    }

    public void agregarProducto(Producto producto) {
        if (producto.getStock() > 0) {
            productos.add(producto);
            producto.reducirStock(1);
        } else {
            throw new IllegalArgumentException("Producto sin stock: " + producto.getNombre());
        }
    }

    public double calcularTotal() {
        double total = 0;
        for (Producto p : productos) {
            total += p.getPrecio();
        }
        return total;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void vaciarCarrito() {
        productos.clear();
    }
}

class ProcesadorPago implements Pagable {
    @Override
    public void procesarPago(double monto) {
        System.out.println("Procesando pago de $" + monto);
        System.out.println("Pago realizado con éxito.");
    }
}

public class TiendaVirtual {
    public static void main(String[] args) {
        Usuario usuario = new Usuario("Diego", "diego@example.com");

        ProductoFisico libro = new ProductoFisico("Libro Java", 50.0, 10, 1.2);
        ProductoFisico teclado = new ProductoFisico("Teclado Mecánico", 150.0, 3, 0.9);
        ProductoFisico bolso = new ProductoFisico("bolso marca velez", 85.0, 4, 1.5);

        ProductoDigital cursoPOO = new ProductoDigital("Curso de programacion orientada a objetos", 100.0, 5, 500.0);
        ProductoDigital licenciaAntivirus = new ProductoDigital("Antivirus Avast", 40.0, 8, 200.0);
        ProductoDigital libroPDF = new ProductoDigital("Libro Digital La vendedora de rosas", 35.0, 6, 150.0);

        Carrito carrito = new Carrito(usuario);

        try {
            carrito.agregarProducto(libro);
            carrito.agregarProducto(cursoPOO);
            carrito.agregarProducto(teclado);
            carrito.agregarProducto(licenciaAntivirus);
            carrito.agregarProducto(bolso);
            carrito.agregarProducto(libroPDF);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("Productos en el carrito:");
        for (Producto p : carrito.getProductos()) {
            System.out.println("- " + p.getNombre() + " (" + p.getTipo() + "): $" + p.getPrecio());
        }

        double total = carrito.calcularTotal();
        System.out.println("Total a pagar: $" + total);

        ProcesadorPago procesador = new ProcesadorPago();
        procesador.procesarPago(total);

        carrito.vaciarCarrito();
    }
}
