package com.tienda.ms_transaccion.service;

import com.tienda.ms_transaccion.model.Transaccion;
import com.tienda.ms_transaccion.repository.TransaccionRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TransaccionService {
    @Autowired
    private TransaccionRepository transaccionRepository;
    
    public List<Transaccion> findAll() {
        return transaccionRepository.findAll();
    }

    public Transaccion findById(Integer id) {
        return transaccionRepository.findById(id).get();
    }

    public Transaccion save(Transaccion transaccion) {
        return transaccionRepository.save(transaccion);
    }

    public void delete(Integer id) {
        transaccionRepository.deleteById(id);
    }
}
