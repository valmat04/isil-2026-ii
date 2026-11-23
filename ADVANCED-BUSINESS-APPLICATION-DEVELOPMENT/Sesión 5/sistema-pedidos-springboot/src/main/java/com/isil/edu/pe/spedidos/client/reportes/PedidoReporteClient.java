package com.isil.edu.pe.spedidos.client.reportes;

import com.isil.edu.pe.spedidos.client.reportes.dto.PedidoReporteResponse;
import com.isil.edu.pe.spedidos.client.reportes.dto.PedidoRevisionResponse;
import com.isil.edu.pe.spedidos.client.reportes.dto.ProductoVendidoResponse;
import com.isil.edu.pe.spedidos.client.reportes.dto.ResumenPedidosResponse;
import com.isil.edu.pe.spedidos.client.reportes.dto.ResumenRevisionesResponse;
import java.util.List;

public interface PedidoReporteClient {
    ResumenPedidosResponse obtenerResumen();
    List<PedidoReporteResponse> buscarPorCliente(String cliente);
    List<ProductoVendidoResponse> listarProductosMasVendidos();
    List<PedidoRevisionResponse> listarRevisiones(String estado);
    ResumenRevisionesResponse obtenerResumenRevisiones();
}
