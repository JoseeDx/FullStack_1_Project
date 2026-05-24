package com.tienda.ms_producto.service;

import com.tienda.ms_producto.exception.ResourceNotFoundException;
import com.tienda.ms_producto.model.Producto;
import com.tienda.ms_producto.repository.ProductoRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductoService {

    private static Logger log = LoggerFactory.getLogger(ProductoService.class);

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> findAll(){
        log.info("Consultando todos los productos");
        try {
            return productoRepository.findAll();
        }catch (Exception e){
            log.error("Error al consultar productos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener los productos");
        }
    }

    public Producto findById(Integer id){
        log.info("Buscando producto con ID: {}", id);
        try {
            return productoRepository.findById(id).get();
        } catch (Exception e) {
            log.warn("Producto con ID: {} no encontrado", id);
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
        }
    }

    public Producto save(Producto producto){
        log.info("Guardando producto: {}", producto.getNombre_producto());
        try {
            return productoRepository.save(producto);
        } catch (Exception e) {
            log.error("Error al guardar producto: {}", e.getMessage());
            throw new RuntimeException("Error al guardar el producto");
        }
    }

    public void delete(Integer id){
        log.info("Eliminando producto con ID: {}", id);
        try {
            productoRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error al eliminar producto con ID: {}", id);
            throw new RuntimeException("Error al eliminar producto");
        }
    }

}
