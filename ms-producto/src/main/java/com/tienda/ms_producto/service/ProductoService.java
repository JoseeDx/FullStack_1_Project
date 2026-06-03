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

@Service//Clase service, toda la logica del negocio va aqui
@Transactional//Si algo falla durante la operacion todos los cambios de la base de datos se devuelven
public class ProductoService {

    private static Logger log = LoggerFactory.getLogger(ProductoService.class);

    @Autowired//Inyecta producto repository
    private ProductoRepository productoRepository;

    //Lista de todos los productos de la base de datos
    public List<Producto> findAll(){
        log.info("Consultando todos los productos");
        try {
            return productoRepository.findAll();//le pide al repository todos los productos
        }catch (Exception e){
            log.error("Error al consultar productos: {}", e.getMessage());
            throw new RuntimeException("Error al obtener los productos");
            //si no encuentra devuelve error
        }
    }

    //Busca producto por su id
    public Producto findById(Integer id){
        log.info("Buscando producto con ID: {}", id);
        try {
            return productoRepository.findById(id).get();//le pide al repository que busque
            //.get() extrae el producto de adentro y si no existe lanza excepcion
        } catch (Exception e) {
            log.warn("Producto con ID: {} no encontrado", id);
            throw new ResourceNotFoundException("Producto no encontrado con ID: " + id);
            //retorna excepcion 404
        }
    }

    //Guarda producto nuevo o actualiza
    public Producto save(Producto producto){
        log.info("Guardando producto: {}", producto.getNombre_producto());
        try {
            return productoRepository.save(producto);
            //si el producto tiene id, actualiza y si no crea uno nuevo
        } catch (Exception e) {
            log.error("Error al guardar producto: {}", e.getMessage());
            throw new RuntimeException("Error al guardar el producto");
        }
    }

    //Borra producto por su id
    public void delete(Integer id){
        log.info("Eliminando producto con ID: {}", id);
        try {
            productoRepository.deleteById(id);//elimina el producto con ese id
        } catch (Exception e) {
            log.error("Error al eliminar producto con ID: {}", id);
            throw new RuntimeException("Error al eliminar producto");
            //retorna error 500
        }
    }

}
