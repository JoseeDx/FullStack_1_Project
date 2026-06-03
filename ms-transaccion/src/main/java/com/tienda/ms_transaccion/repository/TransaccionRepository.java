package com.tienda.ms_transaccion.repository;
//capa que se encarga de comunicarse con la base de datos

import com.tienda.ms_transaccion.model.Transaccion; //con que objeto debe trabajar = modelo transaccion
import org.springframework.data.jpa.repository.JpaRepository; //trae metodos crud
import org.springframework.stereotype.Repository;//indica que es un componente de acceso a datos para que spring la detecte y gestione

@Repository
public interface TransaccionRepository extends JpaRepository<Transaccion, Integer> { //hereda los meodos de JpaRepository (save,findbyid,findall,deletebyid,existsbyid,count)
// transaccion, entidad con la que trabaja - integer, el tipo del id (depende de lo que pongas en el model)
}

//es una interfaz, spring genera la implementacion automaticamente, no hay que escribir nada
