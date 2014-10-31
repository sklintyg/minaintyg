<%@ page contentType="text/html;charset=UTF-8" language="java" session="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="status" value="${healthcheck.ping}"/>
<pingdom_http_custom_check>
    <status>${status.ok ? "OK" : "FAIL"}</status>
    <response_time>${status.measurement}</response_time>
</pingdom_http_custom_check>
