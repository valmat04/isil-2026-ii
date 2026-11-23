<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!doctype html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Supervisión de pedidos - ISIL</title>
    <c:url var="cssUrl" value="/assets/css/app.css" />
    <link rel="stylesheet" href="${cssUrl}">
    <c:url var="supervisionCss" value="/assets/css/supervision.css" />
    <link rel="stylesheet" href="${supervisionCss}">
</head>
<body>
<div class="contenedor">
    <header class="cabecera">
        <div>
            <p class="eyebrow">PA4 · Spring MVC · Spring JDBC</p>
            <h1>Supervisión de pedidos</h1>
            <p class="subtitulo">Revisión transaccional y trazabilidad de pedidos.</p>
        </div>
        <div class="acciones-cabecera">
            <c:url var="pedidosUrl" value="/pedidos" />
            <c:url var="reportesUrl" value="/reportes/pedidos" />
            <a class="boton-link boton-link-secundario" href="${pedidosUrl}">Pedidos</a>
            <a class="boton-link boton-link-secundario" href="${reportesUrl}">Reportes</a>
            <c:url var="logoutUrl" value="/logout" />
            <form method="post" action="${logoutUrl}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <button type="submit">Salir</button>
            </form>
        </div>
    </header>

    <c:if test="${not empty exito}">
        <div class="alerta exito"><c:out value="${exito}" /></div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alerta error"><c:out value="${error}" /></div>
    </c:if>

    <section class="tarjeta">
        <h2>Indicadores</h2>
        <div class="indicadores">
            <div><span>Total pedidos</span><strong><c:out value="${resumen.totalPedidos}" /></strong></div>
            <div><span>Revisados</span><strong><c:out value="${resumen.totalRevisados}" /></strong></div>
            <div><span>Pendientes</span><strong><c:out value="${resumen.totalPendientes}" /></strong></div>
            <div><span>% revisado</span><strong><c:out value="${resumen.porcentajeRevisado}" /> %</strong></div>
        </div>
    </section>

    <section class="tarjeta">
        <h2>Estado de revisión</h2>
        <c:url var="supervisionUrl" value="/supervision/pedidos" />
        <form method="get" action="${supervisionUrl}" class="filtro-supervision">
            <label>Mostrar
                <select name="estado">
                    <option value="" ${empty estado ? 'selected' : ''}>Todos</option>
                    <option value="PENDIENTE" ${estado eq 'PENDIENTE' ? 'selected' : ''}>Pendientes</option>
                    <option value="REVISADO" ${estado eq 'REVISADO' ? 'selected' : ''}>Revisados</option>
                </select>
            </label>
            <button type="submit">Filtrar</button>
        </form>
        <div class="tabla-scroll">
            <table>
                <thead><tr><th>ID</th><th>Cliente</th><th>Producto</th><th>Total</th><th>Estado</th><th>Observación / fecha</th><th>Acción</th></tr></thead>
                <tbody>
                <c:forEach var="pedido" items="${pedidos}">
                    <c:url var="revisionUrl" value="/supervision/pedidos/${pedido.id}/revisar" />
                    <tr>
                        <td><c:out value="${pedido.id}" /></td>
                        <td><c:out value="${pedido.cliente}" /></td>
                        <td><c:out value="${pedido.productoNombre}" /></td>
                        <td>S/ <fmt:formatNumber value="${pedido.total}" minFractionDigits="2" maxFractionDigits="2" /></td>
                        <td>
                            <c:choose><c:when test="${pedido.revisado}">REVISADO</c:when><c:otherwise>PENDIENTE</c:otherwise></c:choose>
                        </td>
                        <td>
                            <c:if test="${pedido.revisado}">
                                <c:out value="${pedido.observacion}" />
                                <div class="fecha-revision"><c:out value="${pedido.fechaRevision}" /></div>
                            </c:if>
                        </td>
                        <td>
                            <c:if test="${not pedido.revisado and puedeRevisar}">
                                <form method="post" action="${revisionUrl}" class="form-revision">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                    <input name="observacion" required maxlength="500" placeholder="Observación de revisión">
                                    <button type="submit">Marcar revisado</button>
                                </form>
                            </c:if>
                            <c:if test="${pedido.revisado}">Sin acciones</c:if>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty pedidos}"><tr><td colspan="7" class="sin-datos">No hay pedidos para el filtro elegido.</td></tr></c:if>
                </tbody>
            </table>
        </div>
    </section>
</div>
</body>
</html>
