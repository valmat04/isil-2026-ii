package com.isil.edu.pe.spedidos.controller;

import com.isil.edu.pe.spedidos.client.reportes.PedidoReporteClient;
import com.isil.edu.pe.spedidos.client.reportes.PedidoReporteNoDisponibleException;
import com.isil.edu.pe.spedidos.exception.PedidoRevisionException;
import com.isil.edu.pe.spedidos.service.PedidoRevisionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/supervision/pedidos")
public class PedidoSupervisionController {

    private final PedidoRevisionService revisionService;
    private final PedidoReporteClient reporteClient;

    public PedidoSupervisionController(PedidoRevisionService revisionService,
                                      PedidoReporteClient reporteClient) {
        this.revisionService = revisionService;
        this.reporteClient = reporteClient;
    }

    @GetMapping
    public String supervisar(@RequestParam(required = false) String estado,
                             Model model,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        try {
            model.addAttribute("pedidos", reporteClient.listarRevisiones(estado));
            model.addAttribute("resumen", reporteClient.obtenerResumenRevisiones());
            model.addAttribute("estado", estado == null ? "" : estado);
            model.addAttribute("puedeRevisar", request.isUserInRole("OPERADOR"));
            return "supervision-pedidos";
        } catch (IllegalArgumentException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (PedidoReporteNoDisponibleException e) {
            response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}/revisar")
    public String revisar(@PathVariable Long id,
                          @RequestParam(required = false) String observacion,
                          RedirectAttributes redirectAttributes,
                          Model model,
                          HttpServletResponse response) {
        try {
            revisionService.revisarPedido(id, observacion);
            redirectAttributes.addFlashAttribute("exito", "Pedido #" + id + " revisado correctamente.");
            return "redirect:/supervision/pedidos";
        } catch (PedidoRevisionException e) {
            response.setStatus(e.getEstado().value());
            model.addAttribute("error", e.getMessage());
            return "error";
        } catch (RuntimeException e) {
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            model.addAttribute("error", "No se pudo registrar la revisión. Los cambios fueron revertidos.");
            return "error";
        }
    }
}
