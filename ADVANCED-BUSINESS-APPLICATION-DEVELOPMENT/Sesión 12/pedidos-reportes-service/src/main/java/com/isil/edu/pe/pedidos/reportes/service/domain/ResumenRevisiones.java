package com.isil.edu.pe.pedidos.reportes.service.domain;

import java.math.BigDecimal;

public record ResumenRevisiones(
        long totalPedidos,
        long totalRevisados,
        long totalPendientes,
        BigDecimal porcentajeRevisado
) {
}
