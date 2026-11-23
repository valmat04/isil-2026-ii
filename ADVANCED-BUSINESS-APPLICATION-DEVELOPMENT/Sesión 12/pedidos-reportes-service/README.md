# pedidos-reportes-service — PA4

Servicio Spring Boot 4.1 / Java 21 / Spring JDBC que consume MySQL `pedidosdb` y publica reportes JSON en puerto **8083**.

## Nuevos recursos PA4

- `GET /pedidos-reportes-service/api/v1/pedidos/revisiones` devuelve los pedidos con su estado, observación y fecha.
- `GET /pedidos-reportes-service/api/v1/pedidos/revisiones?estado=PENDIENTE|REVISADO` filtra por estado.
- `GET /pedidos-reportes-service/api/v1/pedidos/estadisticas/revisiones` devuelve `totalPedidos`, `totalRevisados`, `totalPendientes` y `porcentajeRevisado`.
- Estado inválido responde `400 Bad Request` con `ProblemDetail`.

Usa `PedidoRevisionReporteController` → `PedidoRevisionReporteService` → `PedidoRevisionReporteJdbcRepository` → MySQL. Consultas con parámetros y `LEFT JOIN` entre `pedido` y `pedido_revision`. No comparte entidades JPA con el proyecto principal.

## Configurar y probar

Configurar `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` como variables de entorno. Usar un usuario local con permiso **SELECT** sobre `pedidosdb`; no guardar contraseñas personales en Git. Se debe haber ejecutado previamente la migración `database/pa4_revision_mysql.sql` incluida en `sistema-pedidos-springboot`.

```powershell
.\mvnw.cmd clean test
.\mvnw.cmd spring-boot:run
```

Las pruebas con `@ActiveProfiles("test")` utilizan H2 para verificar filtros, agregados y contratos JSON.
