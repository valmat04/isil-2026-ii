package com.isil.edu.pe.pedidos.reportes.repository;

import com.isil.edu.pe.pedidos.reportes.service.domain.PedidoRevisionDetalle;
import com.isil.edu.pe.pedidos.reportes.service.domain.ResumenRevisiones;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PedidoRevisionReporteJdbcRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public PedidoRevisionReporteJdbcRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<PedidoRevisionDetalle> listarRevisiones(Boolean revisado) {
        String sql = """
                SELECT p.id, p.cliente, p.producto_codigo, p.producto_nombre,
                       p.total, p.revisado, r.observacion, r.fecha_revision
                FROM pedido p
                LEFT JOIN pedido_revision r ON r.pedido_id = p.id
                """;

        Map<String, Object> parametros = Map.of();
        if (revisado != null) {
            sql += " WHERE p.revisado = :revisado ";
            parametros = Map.of("revisado", revisado);
        }
        sql += " ORDER BY p.id DESC ";

        return jdbc.query(sql, parametros, (rs, fila) -> {
            Timestamp fecha = rs.getTimestamp("fecha_revision");
            return new PedidoRevisionDetalle(
                    rs.getLong("id"),
                    rs.getString("cliente"),
                    rs.getString("producto_codigo"),
                    rs.getString("producto_nombre"),
                    rs.getBigDecimal("total"),
                    rs.getBoolean("revisado"),
                    rs.getString("observacion"),
                    fecha == null ? null : fecha.toLocalDateTime());
        });
    }

    public ResumenRevisiones obtenerResumen() {
        String sql = """
                SELECT COUNT(*) AS total_pedidos,
                       COALESCE(SUM(CASE WHEN revisado = TRUE THEN 1 ELSE 0 END), 0) AS total_revisados,
                       COALESCE(SUM(CASE WHEN revisado = FALSE THEN 1 ELSE 0 END), 0) AS total_pendientes,
                       CASE WHEN COUNT(*) = 0 THEN 0
                            ELSE ROUND(100.0 * SUM(CASE WHEN revisado = TRUE THEN 1 ELSE 0 END) / COUNT(*), 2)
                       END AS porcentaje_revisado
                FROM pedido
                """;

        return jdbc.getJdbcTemplate().queryForObject(sql, (rs, fila) ->
                new ResumenRevisiones(
                        rs.getLong("total_pedidos"),
                        rs.getLong("total_revisados"),
                        rs.getLong("total_pendientes"),
                        rs.getBigDecimal("porcentaje_revisado")));
    }
}
