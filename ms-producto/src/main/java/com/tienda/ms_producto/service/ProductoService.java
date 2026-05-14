package com.tienda.ms_producto.service;

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
        return productoRepository.findAll();
    }

    public Producto findById(Integer id){
        log.info("Buscando producto con ID: {}", id);
        return productoRepository.findById(id).get();
    }

    public Producto save(Producto producto){
        log.info("Guardando producto: {}", producto.getNombre_producto());
        return productoRepository.save(producto);
    }

    public void delete(Integer id){
        log.info("Eliminando producto con ID: {}", id);
        productoRepository.deleteById(id);
    }

}
