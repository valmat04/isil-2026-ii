USE pedidosdb;

SET @columna_existe = (
    SELECT COUNT(*) FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'pedido'
      AND COLUMN_NAME = 'revisado'
);

SET @sql_columna = IF(
    @columna_existe = 0,
    'ALTER TABLE pedido ADD COLUMN revisado TINYINT(1) NOT NULL DEFAULT 0',
    'SELECT 1'
);
PREPARE agregar_columna FROM @sql_columna;
EXECUTE agregar_columna;
DEALLOCATE PREPARE agregar_columna;

CREATE TABLE IF NOT EXISTS pedido_revision (
    id BIGINT NOT NULL AUTO_INCREMENT,
    pedido_id BIGINT NOT NULL,
    observacion VARCHAR(500) NOT NULL,
    fecha_revision DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT uq_pedido_revision_pedido UNIQUE (pedido_id),
    CONSTRAINT fk_pedido_revision_pedido FOREIGN KEY (pedido_id)
        REFERENCES pedido(id)
) ENGINE=InnoDB;

SELECT COUNT(*) AS total_pedidos FROM pedido;
SHOW CREATE TABLE pedido_revision;
