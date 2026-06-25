package com.tienda.ms_inventario.repository;

import com.tienda.ms_inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    @Query("SELECT i FROM Inventario i WHERE i.id_producto = :id_producto")
    Optional<Inventario> findByIdProducto(Integer id_producto);

    @Query("SELECT i FROM Inventario i WHERE i.stock_actual <= i.stock_minimo")
    List<Inventario> findProductosBajoStock();
    //Busca todos los productos cuyo stock_actual sea menor o igual al valor ingresado

}
