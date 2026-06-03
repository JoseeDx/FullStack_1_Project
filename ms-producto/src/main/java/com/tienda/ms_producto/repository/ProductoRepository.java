package com.tienda.ms_producto.repository;

import com.tienda.ms_producto.model.Producto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer>{

    @Query("SELECT p FROM Producto p WHERE p.categoria.id_categoria = :idCategoria AND p.activo = true")
    List<Producto> findByCategoriaAndActivoTrue(@Param("idCategoria") Integer idCategoria);
    //para filtrar por categoria y estado activo
}
