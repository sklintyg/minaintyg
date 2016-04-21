<%--
  ~ Copyright (C) 2016 Inera AB (http://www.inera.se)
  ~
  ~ This file is part of sklintyg (https://github.com/sklintyg).
  ~
  ~ sklintyg is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.
  ~
  ~ sklintyg is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  ~ GNU General Public License for more details.
  ~
  ~ You should have received a copy of the GNU General Public License
  ~ along with this program.  If not, see <http://www.gnu.org/licenses/>.
  --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%
String useMinifiedJavaScript = System.getProperty("minaintyg.useMinifiedJavaScript");
if (useMinifiedJavaScript == null) {
  useMinifiedJavaScript = "true";
}
pageContext.setAttribute("useMinifiedJavaScript", useMinifiedJavaScript);

String profile = System.getProperty("spring.profiles.active");
if ("prod".equals(profile)) {
  response.sendError(HttpServletResponse.SC_NOT_FOUND);
} else {
%>
<!DOCTYPE html>
<html lang="sv" id="ng-app" ng-app="minaintyg">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="ROBOTS" content="nofollow, noindex" />
<meta name="viewport" content="width=320,initial-scale=1.0,target-densityDPI=320dpi">

<title><spring:message code="application.name" />-<spring:message code="project.version" />-<%= profile %>
</title>

<link rel="icon" href="<c:url value="/favicon.ico" />" type="image/vnd.microsoft.icon" />

<link rel="stylesheet" href="<c:url value="/mvk-topbar/css/styles.css"/>?<spring:message code="buildNumber" />">
<link rel="stylesheet" href="<c:url value="/web/webjars/bootstrap/3.1.1/css/bootstrap.min.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera.css"/>?<spring:message code="buildNumber" />">
<link rel="stylesheet" href="<c:url value="/css/inera-certificate.css"/>?<spring:message code="buildNumber" />">

<script type="text/javascript">
  /**
   Global JS config/constants for this app, to be used by scripts
   **/
  var MI_CONFIG = {
    BUILD_NUMBER: '<spring:message code="buildNumber" />',
    USE_MINIFIED_JAVASCRIPT: '<c:out value="${useMinifiedJavaScript}"/>'
  }
</script>
</head>
<body ng-app="minaintyg">

  <mvk-top-bar></mvk-top-bar>

  <div class="container welcomepage">

    <div id="content-container">
      <div class="content">

        <div id="navigation-container"></div>

        <div class="row-fluid">
          <div id="content-body" class="col-xs-12 col-sm-12 col-md-12 col-lg-12" style="padding-top: 35px;">
            <pre>Detta är en startsida som inte skall finns tillgänglig i en produktionsmiljö!</pre>
            <h1>Testinloggningar</h1>

            <p>
              <a href="/web/sso?guid=19121212-1212">19121212-1212</a>
            </p>

            <p>
              <a href="/web/sso?guid=19121212-0000">19121212-0000</a>
            </p>

            <p>
              <a href="/web/sso?guid=19121212-0001">19121212-0001</a>
            </p>

            <p>
              <a href="/web/sso?guid=19121212-0002">19121212-0002</a>
            </p>

            <p>
              <a href="/web/sso?guid=19121212-0003">19121212-0003</a>
            </p>

            <h2>Logga in med annat guid</h2>

            <form id="customguidform" class="navbar-form pull-left">
              <input id="guid" type="text" class="col-xs-6 col-sm-6 col-md-6 col-lg-6" placeholder="ange guid"> <input
                type="button" class="btn" onclick="location.href='/web/sso?guid=' + this.form.guid.value;"
                value="logga in">
            </form>
          </div>
        </div>
      </div>

    </div>
  </div>

  <c:choose>
    <c:when test="${useMinifiedJavaScript == 'true'}">
      <script type="text/javascript" src="/web/webjars/jquery/1.9.0/jquery.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.4.10/angular.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.4.10/i18n/angular-locale_sv-se.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.4.10/angular-cookies.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.4.10/angular-route.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.4.10/angular-sanitize.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.4.10/angular-animate.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angular-ui-bootstrap/1.3.2/ui-bootstrap-tpls.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angular-ui-router/0.2.15/angular-ui-router.min.js"></script>
      <script type="text/javascript" src="/app/base/app.min.js?<spring:message code="buildNumber" />"></script>
    </c:when>
    <c:otherwise>
      <script type="text/javascript" src="/web/webjars/jquery/1.9.0/jquery.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.4.10/angular.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.4.10/i18n/angular-locale_sv-se.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.4.10/angular-cookies.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.4.10/angular-route.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.4.10/angular-sanitize.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.4.10/angular-animate.js"></script>
      <script type="text/javascript" src="/web/webjars/angular-ui-bootstrap/1.3.2/ui-bootstrap-tpls.js"></script>
      <script type="text/javascript" src="/web/webjars/angular-ui-router/0.2.15/angular-ui-router.js"></script>
      <script type="text/javascript" src="/app/base/app.js"></script>
    </c:otherwise>
  </c:choose>

</body>
</html>
<%}%>
