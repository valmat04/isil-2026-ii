package com.isil.edu.pe.pedidos.reportes.controller.dto;

import com.isil.edu.pe.pedidos.reportes.service.domain.PedidoRevisionDetalle;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoRevisionDto(
        Long id,
        String cliente,
        String productoCodigo,
        String productoNombre,
        BigDecimal total,
        boolean revisado,
        String observacion,
        LocalDateTime fechaRevision
) {
    public static PedidoRevisionDto from(PedidoRevisionDetalle pedido) {
        return new PedidoRevisionDto(pedido.id(), pedido.cliente(),
                pedido.productoCodigo(), pedido.productoNombre(), pedido.total(),
                pedido.revisado(), pedido.observacion(), pedido.fechaRevision());
    }
}
