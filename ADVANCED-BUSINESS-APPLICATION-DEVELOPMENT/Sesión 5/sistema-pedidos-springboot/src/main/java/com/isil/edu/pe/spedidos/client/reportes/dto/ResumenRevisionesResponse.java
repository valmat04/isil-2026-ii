package com.isil.edu.pe.spedidos.client.reportes.dto;

import java.math.BigDecimal;

public record ResumenRevisionesResponse(
        long totalPedidos,
        long totalRevisados,
        long totalPendientes,
        BigDecimal porcentajeRevisado
) {
}
