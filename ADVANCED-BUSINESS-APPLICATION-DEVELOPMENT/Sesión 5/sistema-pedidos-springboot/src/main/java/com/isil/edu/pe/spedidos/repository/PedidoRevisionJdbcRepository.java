package com.isil.edu.pe.spedidos.repository;

import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PedidoRevisionJdbcRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public PedidoRevisionJdbcRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public int marcarComoRevisado(long pedidoId) {
        String sql = """
                UPDATE pedido
                SET revisado = TRUE
                WHERE id = :pedidoId AND revisado = FALSE
                """;
        return jdbc.update(sql, Map.of("pedidoId", pedidoId));
    }

    public Optional<Boolean> buscarEstado(long pedidoId) {
        String sql = "SELECT revisado FROM pedido WHERE id = :pedidoId";
        return jdbc.query(sql, Map.of("pedidoId", pedidoId),
                (rs, fila) -> rs.getBoolean("revisado")).stream().findFirst();
    }

    public int insertarRevision(long pedidoId, String observacion) {
        String sql = """
                INSERT INTO pedido_revision (pedido_id, observacion, fecha_revision)
                VALUES (:pedidoId, :observacion, CURRENT_TIMESTAMP)
                """;
        return jdbc.update(sql, Map.of(
                "pedidoId", pedidoId,
                "observacion", observacion));
    }
}
