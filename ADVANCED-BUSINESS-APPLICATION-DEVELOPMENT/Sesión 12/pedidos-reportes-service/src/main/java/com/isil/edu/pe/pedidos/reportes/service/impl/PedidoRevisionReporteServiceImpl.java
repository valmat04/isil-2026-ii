package com.isil.edu.pe.pedidos.reportes.service.impl;

import com.isil.edu.pe.pedidos.reportes.repository.PedidoRevisionReporteJdbcRepository;
import com.isil.edu.pe.pedidos.reportes.service.PedidoRevisionReporteService;
import com.isil.edu.pe.pedidos.reportes.service.domain.PedidoRevisionDetalle;
import com.isil.edu.pe.pedidos.reportes.service.domain.ResumenRevisiones;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;

@Service
public class PedidoRevisionReporteServiceImpl implements PedidoRevisionReporteService {

    private final PedidoRevisionReporteJdbcRepository repository;

    public PedidoRevisionReporteServiceImpl(PedidoRevisionReporteJdbcRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PedidoRevisionDetalle> listarRevisiones(String estado) {
        if (estado == null || estado.isBlank()) {
            return repository.listarRevisiones(null);
        }
        return switch (estado.trim().toUpperCase(Locale.ROOT)) {
            case "REVISADO" -> repository.listarRevisiones(true);
            case "PENDIENTE" -> repository.listarRevisiones(false);
            default -> throw new IllegalArgumentException("Estado inválido. Use PENDIENTE o REVISADO.");
        };
    }

    @Override
    public ResumenRevisiones obtenerResumenRevisiones() {
        return repository.obtenerResumen();
    }
}
