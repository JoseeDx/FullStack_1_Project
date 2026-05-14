package com.tienda.ms_producto.service;

import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.repository.CategoriaRepository;
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
}
