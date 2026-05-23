package com.tienda.ms_transaccion.service;

import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.repository.TransaccionRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TransaccionService {

    private static Logger log = LoggerFactory.getLogger(TransaccionService.class);

    @Autowired
    private TransaccionRepository transaccionRepository;
    
    public List<Transaccion> findAll() {
        log.info("Consultando todas las transacciones");
        try {
            return transaccionRepository.findAll();
        } catch (Exception e){
            log.error("Error al consultar transacciones: {}", e.getMessage());
            throw new RuntimeException("Error al obtener las transacciones");
        }
    }

    public Transaccion findById(Integer id) {
        log.info("Obteniendo transaccion por ID: {}", id);
        try {
            return transaccionRepository.findById(id).get();
        } catch (Exception e){
            log.warn("Transaccion con ID: {} no encontrada", id);
            throw new RuntimeException("Transaccion no encontrada con ID: " + id); 
        }
               
    }

    public Transaccion save(Transaccion transaccion) {
        log.info("Guardando transaccion");
        try {
            return transaccionRepository.save(transaccion);
        } catch (Exception e){
            log.error("Error al guardar transaccion: {}", e.getMessage());
            throw new RuntimeException("Error al guardar la transaccion");
        }
    }

    public void delete(Integer id) {
        log.info("Borrando transaccion con ID: {}",id);
        try {
            transaccionRepository.deleteById(id);
        } catch (Exception e){
            log.error("Error al eliminar transaccion con ID: {}", id);
            throw new RuntimeException("Error al eliminar la transaccion");            
        }
    }
    public Transaccion updateEstado(Integer id, String estado) {

        List<String> estadosValidos = List.of("PENDIENTE", "COMPLETADA", "ANULADA");
        if (!estadosValidos.contains(estado)) {
            log.warn("Estado invalido recibido: {}", estado);
            throw new RuntimeException("Estado no valido. Debe ser: PENDIENTE, COMPLETADA o ANULADA");
        }
        try {      
            Transaccion existing = findById(id);
            existing.setEstado_pago(estado);
            log.info("Actualizando estado de transaccion ID: {} a estado: {}", id, estado);
            return transaccionRepository.save(existing);
        } catch (RuntimeException e){
            log.error("Error al actualizar estado de transaccion ID: {}", id);
            throw e;  
        }
    }
}
