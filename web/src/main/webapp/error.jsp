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
<%@ page language="java" isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<%
  String useMinifiedJavaScript = System.getProperty("minaintyg.useMinifiedJavaScript");
  if (useMinifiedJavaScript == null) {
    useMinifiedJavaScript = "true";
  }
  pageContext.setAttribute("useMinifiedJavaScript", useMinifiedJavaScript);
%>
<c:set var="elva77LoginUrl" value="${pageAttributes.elva77LoginUrl}"/>

<!DOCTYPE html>
<html lang="sv" id="ng-app">
<!-- error.jsp -->
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="ROBOTS" content="nofollow, noindex" />
<meta name="viewport" content="width=device-width">

<title><spring:message code="application.name" /></title>

<link rel="apple-touch-icon-precomposed" href="/img/touch-icon-small.png" />
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="/img/touch-icon-big.png" />

<!-- bower:css -->
<!-- endbower -->

<!-- injector:css -->
<link rel="stylesheet" href="/app/app.css?_v=<spring:message code="buildNumber" />">
<link rel="stylesheet" href="/font/css/animation.css?_v=<spring:message code="buildNumber" />">
<link rel="stylesheet" href="/font/css/fontello.css?_v=<spring:message code="buildNumber" />">
<!-- endinjector -->

</head>

<body>
  <noscript>
    <div class="content-wrapper container">
      <h3>
        <spring:message code="error.noscript.title" />
      </h3>
      <spring:message code="error.noscript.text" />
    </div>
  </noscript>

  <div class="content-wrapper" ng-cloak>
    <mvk-top-bar></mvk-top-bar>

    <mi-header></mi-header>

    <div id="navigation-container"></div>

    <div id="content-body" style="text-align: center;">


        <c:choose>
          <c:when test="${param.reason eq 'logout'}">
            <mi-page-message-icon
                id="loggedOut"
                img-path="/img/404.png"
                msg-key="info.loggedout.text">
            </mi-page-message-icon>
            <div class="btn-row">
              <a class="btn btn-primary" href="${elva77LoginUrl}"><i class="icon icon-login"></i> <spring:message code="info.loggedout.fk.loginagain" /></a>
            </div>
          </c:when>

          <c:when test="${param.reason eq 'denied'}">
            <mi-page-message-icon
                id="noAuth"
                img-path="/img/404.png"
                msg-key="error.noauth.text">
            </mi-page-message-icon>
          </c:when>

          <c:when test="${param.reason eq 'notfound'}">
              <mi-page-message-icon
                  id="notFound"
                  img-path="/img/404.png"
                  msg-key="error.notfound-loggedin.text">
              </mi-page-message-icon>
          </c:when>

          <c:when test="${param.reason eq 'login.failed'}">
            <mi-page-message-icon
                id="notFound"
                img-path="/img/404.png"
                msg-key="error.login.failed.text">
            </mi-page-message-icon>
          </c:when>

          <c:otherwise>
            <mi-page-message-icon
                id="genericTechProblem"
                img-path="/img/404.png"
                msg-key="error.generictechproblem.text">
            </mi-page-message-icon>
          </c:otherwise>
        </c:choose>
    </div>
  </div>
  <mi-footer ng-cloak></mi-footer>

  <c:choose>
    <c:when test="${useMinifiedJavaScript == 'true'}">
      <script type="text/javascript">
          var MINA_INTYG_DEBUG_MODE = false;
      </script>
      <script type="text/javascript" src="/bower_components/jquery/dist/jquery.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular/angular.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular-animate/angular-animate.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular-cookies/angular-cookies.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular-i18n/angular-locale_sv-se.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular-sanitize/angular-sanitize.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular-ui-router/release/angular-ui-router.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/bootstrap-sass/assets/javascripts/bootstrap.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/momentjs/min/moment.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/app/app.min.js?<spring:message code="buildNumber" />"></script>
    </c:when>
    <c:otherwise>
      <script type="text/javascript">
          var MINA_INTYG_DEBUG_MODE = true;
      </script>
      <!-- bower:js -->
      <script type="text/javascript" src="/bower_components/jquery/dist/jquery.js"></script>
      <script type="text/javascript" src="/bower_components/angular/angular.js"></script>
      <script type="text/javascript" src="/bower_components/angular-animate/angular-animate.js"></script>
      <script type="text/javascript" src="/bower_components/angular-cookies/angular-cookies.js"></script>
      <script type="text/javascript" src="/bower_components/angular-i18n/angular-locale_sv-se.js"></script>
      <script type="text/javascript" src="/bower_components/angular-sanitize/angular-sanitize.js"></script>
      <script type="text/javascript" src="/bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>
      <script type="text/javascript" src="/bower_components/angular-ui-router/release/angular-ui-router.js"></script>
      <script type="text/javascript" src="/bower_components/momentjs/moment.js"></script>
      <script type="text/javascript" src="/bower_components/bootstrap-sass/assets/javascripts/bootstrap.js"></script>
      <!-- endbower -->
      <script type="text/javascript" src="/app/app.js"></script>
    </c:otherwise>
  </c:choose>

</body>
</html>
