package com.tienda.ms_producto.repository;

import com.tienda.ms_producto.model.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer>{

    List<Producto> findByCategoriaIdCategoriaAndActivoTrue(Integer idCategoria);

}
