package com.tienda.ms_carrito.repository;

import com.tienda.ms_carrito.model.CarritoItem;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository//interface es un repositorio, solo habla con la base de datos
public interface CarritoItemRepository extends JpaRepository<CarritoItem, Integer> {//se genera automaticamente los metodos crud sin escribir
        //CarritoItem: entidad que maneja (tabla carrito_item)
        //Integer: tipo de dato (id, lo q sale en el model)

        //metodo para buscar por id cliente
        @Query("SELECT c FROM CarritoItem c WHERE c.id_cliente = :id_cliente")
        List<CarritoItem> findByIdCliente(@Param("id_cliente")Integer id_cliente); //Lo necesito para el endpoint
        //@Param conecta el parametro del metodo con la query
}
