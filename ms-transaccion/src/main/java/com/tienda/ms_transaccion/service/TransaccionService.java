package com.tienda.ms_transaccion.service;
//es la lógica del negocio, el "cerebro" del microservicio, coordina entre el controller y el repository
import com.tienda.ms_transaccion.client.PedidoClient;
import com.tienda.ms_transaccion.client.PedidoResponse;
import com.tienda.ms_transaccion.exception.BadRequestException; //excepciones personalizadas
import com.tienda.ms_transaccion.exception.ResourceNotFoundException; //excepciones personalizadas 
import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.repository.TransaccionRepository;
import jakarta.transaction.Transactional; //asegura que las operaciones de bd se ejecuten como una unidad completa (si algo falla se revierte todo)
import org.slf4j.Logger;
import org.slf4j.LoggerFactory; //para dejar registros de lo que pasa
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service 
@Transactional //operaciones transaccionales, si algo falla a mitad de camino la bd vuelve al estado anterior automaticamente
public class TransaccionService {

    private static Logger log = LoggerFactory.getLogger(TransaccionService.class);

    @Autowired //inyecta automaticamente el repositorio
    private TransaccionRepository transaccionRepository;

    @Autowired
    private PedidoClient pedidoClient;
    
    //retorna lista con todas las transacciones
    public List<Transaccion> findAll() { 
        log.info("Consultando todas las transacciones"); //registra el log que se esta ejecutando esta consulta
        try {
            return transaccionRepository.findAll(); // le pide al repositorio que traiga todas las filas de la tabla 
        } catch (Exception e){
            log.error("Error al consultar transacciones: {}", e.getMessage()); //registra el error
            throw new RuntimeException("Error al obtener las transacciones"); //lanza una excepcion 
        }
    }

    //busca y retorna una sola transaccion por su ID
    public Transaccion findById(Integer id) { 
        log.info("Obteniendo transaccion por ID: {}", id);
        try {
            return transaccionRepository.findById(id).get(); //puede o no tener valor, .get() extrae la transaccion de adentro, si no existe lanza excepcion y cae al catch
        } catch (Exception e){
            log.warn("Transaccion con ID: {} no encontrada", id);
            throw new ResourceNotFoundException("Transaccion no encontrada con ID: " + id);
            //si no existe lanza la excepcion personalizada 
        }
               
    }

    //guarda una nueva transaccion en la base de datos 
    public Transaccion save(Transaccion transaccion) {
        log.info("Guardando transaccion");
        try {
            // Verificar que el pedido existe
            PedidoResponse pedido = pedidoClient.findById(Long.valueOf(transaccion.getId_pedido())); //Long.valueOf() convierte el integer a long pq el cliente es long
            if (pedido == null) {
                log.warn("Pedido con ID: {} no encontrado", transaccion.getId_pedido());
                throw new BadRequestException("El pedido no existe");
                //si el pedido no existe no guarda nada
            }
            return transaccionRepository.save(transaccion);
        } catch (BadRequestException e) {
            throw e;
            //no se modifica, es para que no lo atrape el catch generico de abajo
        } catch (Exception e) {
            log.error("Error al guardar transaccion: {}", e.getMessage());
            throw new RuntimeException("Error al guardar la transaccion");
            //CUALQUIER OTRO ERROR lo atrapa aqui
        }
    }

    //elimina una transaccion por su id
    public void delete(Integer id) {  //void pq no retorna nada
        log.info("Borrando transaccion con ID: {}",id);
        try {
            transaccionRepository.deleteById(id); //le pide al repositorio que elimine la fila con ese id 
        } catch (Exception e){
            log.error("Error al eliminar transaccion con ID: {}", id);
            throw new RuntimeException("Error al eliminar la transaccion"); 
            //si no lo puede eliminar lanza esta excepcion            
        }
    }

    //Actualiza el estado de pago
    public Transaccion updateEstado(Integer id, String estado) {
        List<String> estadosValidos = List.of("PENDIENTE", "COMPLETADA", "ANULADA");
        if (!estadosValidos.contains(estado)) {
            log.warn("Estado invalido recibido: {}", estado);
            throw new BadRequestException("Estado no valido. Debe ser: PENDIENTE, COMPLETADA o ANULADA");
            //verifica que el estado recibido sea alguna de las 3 opciones de la lista, si no lo rechaza y lanza la excepcion
        }
        try {      
            Transaccion existing = findById(id); //busca si la transaccion existe por la id
            existing.setEstado_pago(estado); //le cambia el estado con el setter
            log.info("Actualizando estado de transaccion ID: {} a estado: {}", id, estado);
            return transaccionRepository.save(existing); //guarda el nuevo estado con save() 
        } catch (RuntimeException e){
            log.error("Error al actualizar estado de transaccion ID: {}", id);
            throw e;  
            //atrapa cualquier error
        }
    }
}
