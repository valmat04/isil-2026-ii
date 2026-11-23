package com.isil.edu.pe.spedidos.service.impl;

import com.isil.edu.pe.spedidos.exception.PedidoRevisionException;
import com.isil.edu.pe.spedidos.repository.PedidoRevisionJdbcRepository;
import com.isil.edu.pe.spedidos.service.PedidoRevisionService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoRevisionServiceImpl implements PedidoRevisionService {

    private final PedidoRevisionJdbcRepository repository;

    public PedidoRevisionServiceImpl(PedidoRevisionJdbcRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void revisarPedido(Long pedidoId, String observacion) {
        if (pedidoId == null || pedidoId <= 0) {
            throw new PedidoRevisionException(HttpStatus.BAD_REQUEST,
                    "El ID del pedido debe ser mayor que cero.");
        }
        if (observacion == null || observacion.isBlank()) {
            throw new PedidoRevisionException(HttpStatus.BAD_REQUEST,
                    "La observación es obligatoria.");
        }

        int modificados = repository.marcarComoRevisado(pedidoId);
        if (modificados != 1) {
            boolean existe = repository.buscarEstado(pedidoId).isPresent();
            if (!existe) {
                throw new PedidoRevisionException(HttpStatus.NOT_FOUND,
                        "El pedido no existe.");
            }
            throw new PedidoRevisionException(HttpStatus.CONFLICT,
                    "Este pedido ya fue revisado.");
        }

        int revisiones = repository.insertarRevision(pedidoId, observacion.trim());
        if (revisiones != 1) {
            throw new IllegalStateException("No se pudo registrar la revisión.");
        }
    }
}
