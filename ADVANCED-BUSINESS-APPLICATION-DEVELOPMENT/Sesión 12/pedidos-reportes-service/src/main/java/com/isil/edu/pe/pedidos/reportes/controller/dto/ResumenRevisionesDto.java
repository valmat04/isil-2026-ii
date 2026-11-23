package com.isil.edu.pe.pedidos.reportes.controller.dto;

import com.isil.edu.pe.pedidos.reportes.service.domain.ResumenRevisiones;
import java.math.BigDecimal;

public record ResumenRevisionesDto(
        long totalPedidos,
        long totalRevisados,
        long totalPendientes,
        BigDecimal porcentajeRevisado
) {
    public static ResumenRevisionesDto from(ResumenRevisiones resumen) {
        return new ResumenRevisionesDto(resumen.totalPedidos(),
                resumen.totalRevisados(), resumen.totalPendientes(), resumen.porcentajeRevisado());
    }
}
