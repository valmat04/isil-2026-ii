package com.isil.edu.pe.spedidos.controller;

import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.isil.edu.pe.spedidos.client.reportes.PedidoReporteClient;
import com.isil.edu.pe.spedidos.client.reportes.dto.ResumenRevisionesResponse;
import com.isil.edu.pe.spedidos.service.PedidoRevisionService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ActiveProfiles("test")
class PedidoSupervisionControllerSecurityTest {

    @Autowired private WebApplicationContext context;
    @MockitoBean private PedidoReporteClient reporteClient;
    @MockitoBean private PedidoRevisionService revisionService;
    private MockMvc mvc;

    @BeforeEach
    void configurar() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        when(reporteClient.listarRevisiones(nullable(String.class))).thenReturn(List.of());
        when(reporteClient.obtenerResumenRevisiones()).thenReturn(
                new ResumenRevisionesResponse(0, 0, 0, BigDecimal.ZERO));
    }

    @Test
    void usuarioConsultaPuedeVerSupervision() throws Exception {
        mvc.perform(get("/supervision/pedidos").with(user("consulta").roles("CONSULTA")))
                .andExpect(status().isOk())
                .andExpect(view().name("supervision-pedidos"));
    }

    @Test
    void anonimoEsRedirigidoAlLogin() throws Exception {
        mvc.perform(get("/supervision/pedidos"))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    void consultaNoPuedeRevisarAunConCsrf() throws Exception {
        mvc.perform(post("/supervision/pedidos/1/revisar")
                        .with(user("consulta").roles("CONSULTA"))
                        .with(csrf()).param("observacion", "Validado"))
                .andExpect(status().isForbidden());
    }

    @Test
    void operadorSinCsrfNoPuedeRevisar() throws Exception {
        mvc.perform(post("/supervision/pedidos/1/revisar")
                        .with(user("operador").roles("OPERADOR"))
                        .param("observacion", "Validado"))
                .andExpect(status().isForbidden());
    }
}
