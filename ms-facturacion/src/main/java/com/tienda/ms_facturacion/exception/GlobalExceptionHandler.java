package com.tienda.ms_facturacion.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice //maneja excepciones de forma centralizada para todos los controllers
public class GlobalExceptionHandler {

    private static Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    // 404 - Registro no encontrado
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException e) { //le dice a spring que ejecute el metodo cuando vea la excepcion
        log.warn("Recurso no encontrado: {}", e.getMessage());
        Map<String, String> error = new HashMap<>(); //crea un map con dos campos
        error.put("error", "No encontrado"); //tipo de error
        error.put("mensaje", e.getMessage());//mensaje con el detalle
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error); //lo devuelve con codigo 404
    }

    // 400 - Validaciones fallidas (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class) //por si el valid falla jjejeje (post)
    public ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        log.warn("Error de validacion: {}", e.getMessage());
        Map<String, String> errores = new HashMap<>(); //mapea dos campos
        e.getBindingResult().getFieldErrors()
        //obtiene resultado de la validacion - extrae la lista de errores por campo
                .forEach(err -> errores.put(err.getField(), err.getDefaultMessage())); //recorre todos los errores y los mete en el map
            //recorre cada error - mete llave-valor en el map - obtiene el nombre del campo - obtiene mensaje asignado
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

        // 400 - Bad Request
    @ExceptionHandler(BadRequestException.class) //se lanza cuando alguien manda datos incorrectos 
    public ResponseEntity<Map<String, String>> handleBadRequest(BadRequestException e) {
        log.warn("Solicitud invalida: {}", e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Solicitud invalida");
        error.put("mensaje", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // 500 - Error interno inesperado
    @ExceptionHandler(Exception.class)//clase padre de todas las expeciones
    public ResponseEntity<Map<String, String>> handleGeneric(Exception e) { //atrapa cualqueir excepcion que no agarraron las otras xd
        log.error("Error inesperado: {}", e.getMessage());
        Map<String, String> error = new HashMap<>();
        error.put("error", "Error interno del servidor");
        error.put("mensaje", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    //map<string, string> es la forma mas simple de devolver campos clave-valor sin crear una clase extra
    //hashMap<> crea una tabla con dos columnas: llave y valor
}
