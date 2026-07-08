package com.tienda.ms_producto.service;

import com.tienda.ms_producto.exception.ResourceNotFoundException;
import com.tienda.ms_producto.exception.BadRequestException;
import com.tienda.ms_producto.model.Categoria;
import com.tienda.ms_producto.model.Producto;
import com.tienda.ms_producto.repository.CategoriaRepository;
import com.tienda.ms_producto.repository.ProductoRepository;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service//Clase service contiene la logica del negocio
@Transactional//Si algo falla todos los cambios de la base de datos se devuelven automaticamente
public class CategoriaService {

    private static Logger log = LoggerFactory.getLogger(CategoriaService.class);

    @Autowired//Inyecta repositorio
    private CategoriaRepository categoriaRepository;

    @Autowired//Inyecta repositorio de productos, para verificar si una categoria tiene productos activos
    private ProductoRepository productoRepository;

    //Trae todas las categorias de la base de datos
    public List<Categoria> findAll() {
        log.info("Consultando todas las categorias");
        try {
            return categoriaRepository.findAll();//Le pide a repository todos los registros
        } catch (Exception e) {
            log.error("Error al consultar categorias: {}", e.getMessage());
            throw new RuntimeException("Error al obtener las categorias");   
        }
    }    

    //Busca una categoria por su id
    public Categoria findById(Integer id){
        log.info("Obteniendo categoria con ID: {}", id);
        try {
            return categoriaRepository.findById(id).get();//le pide a repository que lo busque
            //.get() extrae el valor y si no existe lanza excepcion
        } catch (Exception e) {
            log.warn("Categoria con ID: {} no encontrada", id);
            throw new ResourceNotFoundException("Categoria no encontrada con ID: " + id);
            //excepcion personalizada 404
        }
    }

    //Guarda una categoria
    public Categoria save(Categoria categoria){
        log.info("Guardando categoria: {}", categoria.getNombre_categoria());
        try {
            return categoriaRepository.save(categoria);//guarda la categoria
        } catch (Exception e) {
            log.error("Error al guardar categoria: {}", e.getMessage());
            throw new RuntimeException("Error al guardar la categoria");
        }
    }

    //Actualiza los campos de una categoria existente
    public Categoria actualizar(Integer id, Categoria datosActualizados){
        log.info("Actualizando categoria con ID: {}", id);
        Categoria existing = findById(id);//verifica si existe por la id
        existing.setNombre_categoria(datosActualizados.getNombre_categoria());
        return save(existing);
    }

    //Activa una categoria por su id
    public Categoria activar(Integer id){
        log.info("Activando categoria con ID: {}", id);
        Categoria existing = findById(id);
        existing.setActivo(true);
        return save(existing);
    }

    //Borra una categoria por su id
    public void delete(Integer id) {
        log.info("Eliminando categoria con ID: {}", id);
        try {
            categoriaRepository.deleteById(id); //elimina la categoria
        } catch (Exception e) {
            //obtiene el mensaje de error y la causa
            String mensaje = e.getMessage() != null ? e.getMessage() : ""; // ? = entonces, : si no
            //guarda en mensaje: si el error tiene mensaje, guardalo, si no tiene guarda cadena vacia
            String causa = e.getCause() != null ? e.getCause().getMessage() : "";
            //lanza un error de foreign key constraint, no se puede borrar categoria con productos asociados
            if (mensaje.contains("foreign key") || causa.contains("foreign key") || 
                mensaje.contains("constraint") || causa.contains("constraint")) {
                log.warn("No se puede eliminar categoria ID: {} tiene productos asociados", id);
                throw new BadRequestException("No se puede eliminar una categoría que tiene productos asociados");
                //retorna 400
            }
            log.error("Error al eliminar categoria con ID: {}", id);
            throw new RuntimeException("Error al eliminar la categoria");//cualquier otro error
        }
    }

    //Desactivar categoria
    public Categoria desactivar(Integer id) {
        List<Producto> productosActivos = productoRepository.findByCategoriaAndActivoTrue(id);//Metodo personalizado del repository que filtra por categoria y activo true
        if (!productosActivos.isEmpty()) { 
            //si la lista no esta vacia entonces hay productos activos 
            log.warn("No se puede desactivar categoria ID: {} tiene {} productos activos", id, productosActivos.size());
            throw new BadRequestException("No se puede desactivar una categoría con productos activos");
            //no permite desactivar la categoria si tiene productos activos
        }
        //si no tiene productos activos entonces puede desactivar
        try {
            Categoria existing = findById(id);//busca si la categoria existe
            existing.setActivo(false);//cambia el campo activo a false
            log.info("Desactivando categoria ID: {}",id);
            return categoriaRepository.save(existing);//guarda el cambio en la base de datos
        } catch (Exception e) {
            log.error("Error al desactivar categoria con ID: {}", id);
            throw new RuntimeException("Error al desactivar categoria");
        }
    }
}
