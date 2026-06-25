package com.tienda.ms_inventario.service;

import com.tienda.ms_inventario.client.ProductoClient;
import com.tienda.ms_inventario.dto.ProductoDTO;
import com.tienda.ms_inventario.exception.BadRequestException;
import com.tienda.ms_inventario.exception.ResourceNotFoundException;
import com.tienda.ms_inventario.model.Inventario;
import com.tienda.ms_inventario.repository.InventarioRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InventarioService {
    
    private static Logger log = LoggerFactory.getLogger(InventarioService.class);

    @Autowired
    private InventarioRepository inventarioRepository;

    @Autowired
    private ProductoClient productoClient;

    public List<Inventario> listar() {
        log.info("Listando todo el inventario");
        List<Inventario> inventarios = inventarioRepository.findAll();
        log.info("Total de inventarios encontrados: {}", inventarios.size());
        return inventarios;
    }

    public Inventario obtenerPorId(Long id) {
        log.info("Buscando inventario por id: {}", id);
        Inventario inventario = inventarioRepository.findById(id).orElse(null);
        if (inventario == null){
            log.warn("Inventario no encontrado con id: {}", id);
            throw new ResourceNotFoundException("Inventario no existe id: {}" + id);
        }
        log.info("Inventario encontrado por id: {}", id);
        return inventario;
    }

    public Inventario obtenerPorIdProducto(Integer id_producto){
        log.info("Buscando inventario por id producto: {}", id_producto);
        Inventario inventario = inventarioRepository.findByIdProducto(id_producto).orElse(null);
        if (inventario == null){
            log.warn("Inventario no encontrado para producto con id: {}", id_producto);
            throw new ResourceNotFoundException("Inventario no existe para producto con id: {}" + id_producto);
        }
        log.info("Inventario encontrado para producto id: {}", id_producto);
        return inventario;
    }

    public List<Inventario> obtenerBajoStock(){
        log.info("Buscando productos con bajo stock");
        List<Inventario> bajoStock = inventarioRepository.findProductosBajoStock();
        log.info("Productos con bajo stock encontrados: {}", bajoStock.size());
        return bajoStock;
    }

    public Inventario guardar(Inventario inventario) {
        log.info("Iniciando guardar inventario idProducto={}", inventario.getId_producto());

        if (inventario.getStock_actual() < 0)
            throw new BadRequestException("El stock actual no puede ser negativo");
        if (inventario.getStock_minimo() < 0)
            throw new BadRequestException("El stock mínimo no puede ser negativo");
        if (inventario.getStock_maximo() <= inventario.getStock_minimo())
            throw new BadRequestException("El stock máximo debe ser mayor al stock mínimo");

        try {
            ProductoDTO producto = productoClient.obtenerProducto(inventario.getId_producto());
            log.info("Producto validado: {}", producto.getNombre_producto());

            inventario.setFecha_actualizacion(LocalDateTime.now());
            Inventario guardado = inventarioRepository.save(inventario);
            log.info("Inventario guardado exitosamente id={}", guardado.getId_inventario());
            return guardado;
        } catch (Exception e) {
            log.error("Error al comunicarse con ms-producto o guardar inventario: {}", e.getMessage(), e);
            throw new BadRequestException("No se pudo validar el producto, intente más tarde");
        }
    }

    public Inventario actualizar(Long id, Inventario inventario) {
        log.info("Iniciando actualizar inventario id={}", id);

        // 1. Buscamos el inventario existente usando la manera antigua
        Inventario existente = inventarioRepository.findById(id).orElse(null);
        
        if (existente == null) {
            log.warn("No se pudo actualizar. Inventario no encontrado id={}", id);
            throw new ResourceNotFoundException("Inventario no existe");
        }

        // 2. Validaciones de negocio (Afuera del try para no ensuciar los logs de error)
        if (inventario.getStock_actual() < 0) {
            throw new BadRequestException("El stock actual no puede ser negativo");
        }
        if (inventario.getStock_minimo() < 0) {
            throw new BadRequestException("El stock mínimo no puede ser negativo");
        }
        if (inventario.getStock_maximo() <= inventario.getStock_minimo()) {
            throw new BadRequestException("El stock máximo debe ser mayor al stock mínimo");
        }

        // 3. Operación en la Base de Datos (Dentro del try por si ocurre un fallo real del servidor)
        try {
            // Traspasamos los datos nuevos al objeto que ya existía en la BD
            existente.setId_producto(inventario.getId_producto());
            existente.setStock_actual(inventario.getStock_actual());
            existente.setStock_minimo(inventario.getStock_minimo());
            existente.setStock_maximo(inventario.getStock_maximo());
            existente.setFecha_actualizacion(LocalDateTime.now());

            Inventario actualizado = inventarioRepository.save(existente);
            log.info("Inventario actualizado exitosamente id={}", actualizado.getId_inventario());
            return actualizado;

        } catch (Exception e) {
            // Esto solo se ejecuta si se cae la base de datos o hay un error interno real
            log.error("Error crítico al actualizar inventario id={} en la base de datos: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public void eliminar(Long id) {
        log.info("Iniciando eliminación de inventario id={}", id);

        // 1. Validación previa: Si no existe, lanzamos la excepción de inmediato
        if (!inventarioRepository.existsById(id)) {
            log.warn("Inventario no existe para eliminar id={}", id);
            throw new ResourceNotFoundException("Inventario no existe");
        }

        // 2. Operación de base de datos protegida
        try {
            inventarioRepository.deleteById(id);
            log.info("Inventario eliminado exitosamente id={}", id);
            
        } catch (Exception e) {
            // Esto solo saltará si, por ejemplo, el inventario está amarrado a otra tabla (ConstraintViolation)
            log.error("Error crítico al eliminar inventario id={}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    public boolean hayStockSuficiente(Integer id_producto, Integer cantidad) {
        log.info("Verificando stock para idProducto={}, cantidad={}", id_producto, cantidad);
        if (cantidad <= 0)
            throw new BadRequestException("La cantidad debe ser mayor a 0");
        Inventario inventario = obtenerPorIdProducto(id_producto);
        boolean haySuficiente = inventario.getStock_actual() >= cantidad;
        log.info("Stock suficiente para idProducto={}: {}", id_producto, haySuficiente);
        return haySuficiente;
    }

}
