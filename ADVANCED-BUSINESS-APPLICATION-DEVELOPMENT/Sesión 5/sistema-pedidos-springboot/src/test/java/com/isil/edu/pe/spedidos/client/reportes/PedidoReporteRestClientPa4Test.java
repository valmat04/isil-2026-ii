package com.isil.edu.pe.spedidos.client.reportes;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withServerError;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

class PedidoReporteRestClientPa4Test {

    private static final String BASE = "http://localhost:8083/pedidos-reportes-service/api/v1/pedidos";

    @Test
    void convierteError503DelServicioDeReportesEnMensajeControlado() {
        RestClient.Builder builder = RestClient.builder();
        MockRestServiceServer server = MockRestServiceServer.bindTo(builder).build();
        PedidoReporteRestClient cliente = new PedidoReporteRestClient(builder.build(), BASE);
        server.expect(requestTo(BASE + "/estadisticas/revisiones"))
                .andRespond(withServerError());

        PedidoReporteNoDisponibleException error = assertThrows(
                PedidoReporteNoDisponibleException.class, cliente::obtenerResumenRevisiones);

        assertTrue(error.getMessage().contains("8083"));
        server.verify();
    }
}
