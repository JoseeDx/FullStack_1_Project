package com.tienda.ms_producto.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String mensaje){
        super(mensaje);
    }


}
