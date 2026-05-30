package com.tienda.ms_transaccion.exception;

//hereda de runtime, es una excepcion no verificada, se lanza cuando no encuentra algo en la base de datos
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String mensaje) {//recibe un mensaje y lo pasa a runtime
        super(mensaje);//llama al constructor de runtime para que el mensaje quede disponible
    }

    //excepcion personalizada, para no lanzar un runtime generico cuando no encuetra algo.
    // sirve para que devuelva el codigo http correcto (404 not found)

}
