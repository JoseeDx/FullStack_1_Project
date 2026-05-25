package com.tienda.ms_carrito.service;

import com.tienda.ms_carrito.client.ProductoClient;
import com.tienda.ms_carrito.client.ProductoResponse;
import com.tienda.ms_carrito.exception.BadRequestException;
import com.tienda.ms_carrito.exception.ResourceNotFoundException;
import com.tienda.ms_carrito.model.CarritoItem;
import com.tienda.ms_carrito.repository.CarritoItemRepository;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CarritoItemService {

    private static Logger log = LoggerFactory.getLogger(CarritoItemService.class);

    @Autowired
    private CarritoItemRepository carritoItemRepository;

    @Autowired
    private ProductoClient productoClient;

    public List<CarritoItem> findAll() {
        log.info("Consultando todos los items del carrito");
        try {
            return carritoItemRepository.findAll();
        } catch (Exception e) {
            log.error("Error al consultar items del carrito: {}", e.getMessage());
            throw new RuntimeException("Error al obtener los items del carrito");
        }
    }

    public CarritoItem findById(Integer id) {
        log.info("Buscando item del carrito con ID: {}", id);
        try {
            return carritoItemRepository.findById(id).get();
        } catch (Exception e) {
            log.warn("Item del carrito con ID: {} no encontrado", id);
            throw new ResourceNotFoundException("Item del carrito no encontrado con ID: " + id);
        }
    }

    public List<CarritoItem> findByIdCliente(Integer id_cliente) {
        log.info("Buscando items del cliente ID: {}", id_cliente);
        try {
            return carritoItemRepository.findByIdCliente(id_cliente);
        } catch (Exception e) {
            log.error("Error al buscar items del cliente ID: {}", id_cliente);
            throw new RuntimeException("Error al obtener items del cliente");
        }
    }

    public Double getTotalByIdCliente(Integer id_cliente) {
        log.info("Calculando total del carrito del cliente ID: {}", id_cliente);
        try {
            List<CarritoItem> items = carritoItemRepository.findByIdCliente(id_cliente);
            return items.stream()
                    .mapToDouble(item -> item.getPrecio_unitario() * item.getCantidad())
                    .sum();
        } catch (Exception e) {
            log.error("Error al calcular total del cliente ID: {}", id_cliente);
            throw new RuntimeException("Error al calcualar el total del carrito");
        }
    }

    public CarritoItem save(CarritoItem carritoItem) {
        log.info("Guardando item en el carrito");
        try {
            // Verifica que el producto existe y esta activo
            ProductoResponse producto = productoClient.findById(carritoItem.getId_producto());
            if (producto == null || !producto.getActivo()) {
                log.warn("Producto con ID: {} no disponible", carritoItem.getId_producto());
                throw new BadRequestException("El producto no existe o no está disponible");
            }
        // Asignar el precio actual del producto
        carritoItem.setPrecio_unitario(producto.getPrecio_producto());
        return carritoItemRepository.save(carritoItem);
    } catch (BadRequestException e) {
        throw e;
    } catch (Exception e) {
        log.error("Error al guardar item del carrito: {}", e.getMessage());
        throw new RuntimeException("Error al guardar el item del carrito");
    }

    }

    public void delete(Integer id) {
        log.info("Eliminando item del carrito con ID: {}", id);
        try {
            carritoItemRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error al eliminar item del carrito con ID: {}", id);
            throw new RuntimeException("Error al eliminar el item del carrito");
        }
    }
}
