package com.isil.edu.pe.pedidos.reportes.service;

import com.isil.edu.pe.pedidos.reportes.service.domain.PedidoRevisionDetalle;
import com.isil.edu.pe.pedidos.reportes.service.domain.ResumenRevisiones;
import java.util.List;

public interface PedidoRevisionReporteService {
    List<PedidoRevisionDetalle> listarRevisiones(String estado);
    ResumenRevisiones obtenerResumenRevisiones();
}
