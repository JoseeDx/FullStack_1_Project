package com.tienda.ms_producto.service;

import com.tienda.ms_producto.exception.ResourceNotFoundException;
import com.tienda.ms_producto.exception.BadRequestException;
import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.model.Producto;
import com.tienda.ms_producto.repository.CategoriaRepository;
import com.tienda.ms_producto.repository.ProductoRepository;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    private static Logger log = LoggerFactory.getLogger(CategoriaService.class);

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    public List<Categoria> findAll() {
        log.info("Consultando todas las categorias");
        try {
            return categoriaRepository.findAll();
        } catch (Exception e) {
            log.error("Error al consultar categorias: {}", e.getMessage());
            throw new RuntimeException("Error al obtener las categorias");   
        }
    }    

    public Categoria findById(Integer id){
        log.info("Obteniendo categoria con ID: {}", id);
        try {
            return categoriaRepository.findById(id).get();
        } catch (Exception e) {
            log.warn("Categoria con ID: {} no encontrada", id);
            throw new ResourceNotFoundException("Categoria no encontrada con ID: " + id);
        }
    }

    public Categoria save(Categoria categoria){
        log.info("Guardando categoria: {}", categoria.getNombre_categoria());
        try {
            return categoriaRepository.save(categoria);
        } catch (Exception e) {
            log.error("Error al guardar categoria: {}", e.getMessage());
            throw new RuntimeException("Error al guardar la categoria");
        }
    }

    public void delete(Integer id){
        log.info("Borrando categoria con ID: {}", id);
        try {
            categoriaRepository.deleteById(id);
        } catch (Exception e) {
            log.error("Error al eliminar categoria con ID: {}", id);
            throw new RuntimeException("Error al eliminar la categoria");

        }
    }

    public Categoria desactivar(Integer id) {
        List<Producto> productosActivos = productoRepository.findByCategoriaAndActivoTrue(id);
        if (!productosActivos.isEmpty()) {
            log.warn("No se puede desactivar categoria ID: {} tiene {} productos activos", id, productosActivos.size());
            throw new BadRequestException("No se puede desactivar una categoría con productos activos");
        }
        try {
            Categoria existing = findById(id);
            existing.setActivo(false);
            log.info("Desactivando categoria ID: {}",id);
            return categoriaRepository.save(existing);
        } catch (Exception e) {
            log.error("Error al desactivar categoria con ID: {}", id);
            throw new RuntimeException("Error al desactivar categoria");
        }
    }
}
