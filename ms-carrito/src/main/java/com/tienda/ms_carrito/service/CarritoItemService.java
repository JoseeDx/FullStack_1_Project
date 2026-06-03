package com.tienda.ms_carrito.service;

import com.tienda.ms_carrito.client.ClienteClient;//feign
import com.tienda.ms_carrito.client.ProductoClient;//feign
import com.tienda.ms_carrito.client.ProductoResponse;//clase del feign
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

@Service//clase service, contiene toda la logica
@Transactional//si algo falla todos los cambios en la base de datos se devuelven
public class CarritoItemService {

    private static Logger log = LoggerFactory.getLogger(CarritoItemService.class);

    @Autowired//inyecta cliente
    private ClienteClient clienteClient;
    
    @Autowired//inyecta repository (base de datos)
    private CarritoItemRepository carritoItemRepository;

    @Autowired//inyecta producto
    private ProductoClient productoClient;

    public List<CarritoItem> findAll() {//trae todos los items del carrito
        log.info("Consultando todos los items del carrito");
        try {
            return carritoItemRepository.findAll();//le pide a repository todos los registros
        } catch (Exception e) {
            log.error("Error al consultar items del carrito: {}", e.getMessage());
            throw new RuntimeException("Error al obtener los items del carrito");//si falla lanza error
        }
    }

    //busca un item por su id
    public CarritoItem findById(Integer id) {
        log.info("Buscando item del carrito con ID: {}", id);
        try {
            return carritoItemRepository.findById(id).get();
            //.get() extrae el valor que devuelve el findbyid
            //si no existe lanza excepcion
        } catch (Exception e) {
            log.warn("Item del carrito con ID: {} no encontrado", id);
            throw new ResourceNotFoundException("Item del carrito no encontrado con ID: " + id);
        }
    }

    //busca los items de un carrito de un cliente en especifico por su id
    public List<CarritoItem> findByIdCliente(Integer id_cliente) {
        log.info("Buscando items del cliente ID: {}", id_cliente);
        try {
            return carritoItemRepository.findByIdCliente(id_cliente);//busca el cliente
        } catch (Exception e) {
            //si no lo encuentra lanza error
            log.error("Error al buscar items del cliente ID: {}", id_cliente);
            throw new RuntimeException("Error al obtener items del cliente");
        }
    }

    //calcula el total a pagar
    public Double getTotalByIdCliente(Integer id_cliente) {
        log.info("Calculando total del carrito del cliente ID: {}", id_cliente);
        try {
            List<CarritoItem> items = carritoItemRepository.findByIdCliente(id_cliente);//trae los items del cliente
            return items.stream()
                    .mapToDouble(item -> item.getPrecio_unitario() * item.getCantidad())//multiplica el precio * cantidad
                    //-> para cada elemento haz esto
                    //transforma cada elemento en un numero double 
                    .sum();//suma los resultados y retorna el total
        } catch (Exception e) {
            //si no puede lanza este error
            log.error("Error al calcular total del cliente ID: {}", id_cliente);
            throw new RuntimeException("Error al calcualar el total del carrito");
        }
    }

    public CarritoItem save(CarritoItem carritoItem) {
        log.info("Guardando item en el carrito");
        try {
             //Verifica que el cliente existe
            Boolean clienteExiste = clienteClient.existeCliente(Long.valueOf(carritoItem.getId_cliente()));
            //convierte int a long pq cliente usa long
            if (!clienteExiste) { //si cliente no existe
                log.warn("Cliente con ID: {} no encontrado", carritoItem.getId_cliente());
            throw new BadRequestException("El cliente no existe"); //no guarda nada y retorna 400
            }
            //Verifica que el producto existe y esta activo
            ProductoResponse producto = productoClient.findById(carritoItem.getId_producto());
            if (producto == null || !producto.getActivo()) { //si el producto no existe o esta desactivado
                log.warn("Producto con ID: {} no disponible", carritoItem.getId_producto());
                throw new BadRequestException("El producto no existe o no está disponible");
                //no guarda nada y retorna 400
            }

        // Asigna el precio actual del producto obtenido desde ms-producto
        carritoItem.setPrecio_unitario(producto.getPrecio_producto());
        return carritoItemRepository.save(carritoItem);//lo guarda
    } catch (BadRequestException e) {
        throw e;//badrequest sin modificar 400, si no lo pongo cae todo al exception generico
    } catch (Exception e) {
        log.error("Error al guardar item del carrito: {}", e.getMessage());
        throw new RuntimeException("Error al guardar el item del carrito");
        //atrapa cualquier otro error 500
    }

    }

    //Elimina un item del carrito por su id
    public void delete(Integer id) {
        log.info("Eliminando item del carrito con ID: {}", id);
        try {
            carritoItemRepository.deleteById(id); //le pide al repository que lo elimine xd
        } catch (Exception e) {
            log.error("Error al eliminar item del carrito con ID: {}", id);
            throw new RuntimeException("Error al eliminar el item del carrito");
            //si no puede lanza este error
        }
    }
}
