package com.isil.edu.pe.pedidos.reportes.controller;

import com.isil.edu.pe.pedidos.reportes.controller.dto.PedidoRevisionDto;
import com.isil.edu.pe.pedidos.reportes.controller.dto.ResumenRevisionesDto;
import com.isil.edu.pe.pedidos.reportes.service.PedidoRevisionReporteService;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoRevisionReporteController {

    private final PedidoRevisionReporteService service;

    public PedidoRevisionReporteController(PedidoRevisionReporteService service) {
        this.service = service;
    }

    @GetMapping(value = "/revisiones", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<PedidoRevisionDto> listar(@RequestParam(required = false) String estado) {
        return service.listarRevisiones(estado).stream().map(PedidoRevisionDto::from).toList();
    }

    @GetMapping(value = "/estadisticas/revisiones", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResumenRevisionesDto resumen() {
        return ResumenRevisionesDto.from(service.obtenerResumenRevisiones());
    }
}
