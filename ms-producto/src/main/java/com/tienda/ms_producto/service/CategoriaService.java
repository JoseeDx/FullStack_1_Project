package com.tienda.ms_producto.service;

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
        return categoriaRepository.findAll();
    }

    public Categoria findById(Integer id){
        log.info("Obteniendo categoria con ID: {}", id);
        return categoriaRepository.findById(id).get();
    }

    public Categoria save(Categoria categoria){
        log.info("Guardando categoria: {}", categoria.getNombre_categoria());
        return categoriaRepository.save(categoria);
    }

    public void delete(Integer id){
        log.info("Borrando categoria con ID: {}", id);
        categoriaRepository.deleteById(id);
    }

    public Categoria desactivar(Integer id) {
        Categoria existing = findById(id);
    
        List<Producto> productosActivos = productoRepository.findByCategoriaIdCategoriaAndActivoTrue(id);
        if (!productosActivos.isEmpty()) {
            log.warn("No se puede desactivar categoria ID: {} tiene {} productos activos", id, productosActivos.size());
            throw new RuntimeException("No se puede desactivar una categoría con productos activos");
        }
        existing.setActivo(false);
        log.info("Desactivando categoria ID: {}",id);
        return categoriaRepository.save(existing);

    }
}
