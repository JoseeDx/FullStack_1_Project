package com.tienda.ms_facturacion.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String mensaje){ 
        super(mensaje); 
    }

//es igual a resourcenotfound pero cambia el proposito, se lanza cuando el cliente manda 
// datos incorrectos o invalidos (400 bad request)
}
