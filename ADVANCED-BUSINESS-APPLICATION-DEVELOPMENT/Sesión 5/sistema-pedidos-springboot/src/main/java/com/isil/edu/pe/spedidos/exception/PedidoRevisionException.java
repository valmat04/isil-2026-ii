package com.isil.edu.pe.spedidos.exception;

import org.springframework.http.HttpStatus;

public class PedidoRevisionException extends RuntimeException {
    private final HttpStatus estado;

    public PedidoRevisionException(HttpStatus estado, String mensaje) {
        super(mensaje);
        this.estado = estado;
    }

    public HttpStatus getEstado() {
        return estado;
    }
}
