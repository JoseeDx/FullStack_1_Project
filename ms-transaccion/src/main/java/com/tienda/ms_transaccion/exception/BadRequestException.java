package com.tienda.ms_transaccion.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String mensaje){
        super(mensaje);
    }

}
