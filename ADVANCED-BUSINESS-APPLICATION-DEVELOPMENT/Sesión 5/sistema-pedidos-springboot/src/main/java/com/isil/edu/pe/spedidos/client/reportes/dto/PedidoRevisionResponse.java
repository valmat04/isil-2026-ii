package com.isil.edu.pe.spedidos.client.reportes.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PedidoRevisionResponse(
        Long id,
        String cliente,
        String productoCodigo,
        String productoNombre,
        BigDecimal total,
        boolean revisado,
        String observacion,
        LocalDateTime fechaRevision
) {
}
