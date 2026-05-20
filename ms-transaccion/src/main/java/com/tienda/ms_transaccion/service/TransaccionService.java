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
        return transaccionRepository.findAll();
    }

    public Transaccion findById(Integer id) {
        log.info("Obteniendo transaccion por ID");
        return transaccionRepository.findById(id).get();
    }

    public Transaccion save(Transaccion transaccion) {
        log.info("Guardando transaccion");
        return transaccionRepository.save(transaccion);
    }

    public void delete(Integer id) {
        log.info("Borrando transaccion con ID: {}",id);
        transaccionRepository.deleteById(id);
    }
}
