package com.tienda.ms_producto.service;

import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria findById(Integer id){
        return categoriaRepository.findById(id).get();
    }

    public Categoria save(Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    public void delete(Integer id){
        categoriaRepository.deleteById(id);
    }
}
