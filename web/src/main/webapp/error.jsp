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

<!DOCTYPE html>
<html lang="sv" id="ng-app" ng-app="minaintyg">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="ROBOTS" content="nofollow, noindex" />
<meta name="viewport" content="width=320,initial-scale=1.0,target-densityDPI=320dpi">

<title><spring:message code="application.name" /></title>

<link rel="icon" href="<c:url value="/favicon.ico" />" type="image/vnd.microsoft.icon" />
<link rel="apple-touch-icon-precomposed" href="/img/touch-icon-small.png" />
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="/img/touch-icon-big.png" />

<link rel="stylesheet" href="<c:url value="/mvk-topbar/css/styles.css"/>?<spring:message code="buildNumber" />">
<link rel="stylesheet" href="<c:url value="/web/webjars/bootstrap/3.1.1/css/bootstrap.min.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera.css"/>?<spring:message code="buildNumber" />">
<link rel="stylesheet" href="<c:url value="/css/inera-certificate.css"/>?<spring:message code="buildNumber" />">
<link rel="stylesheet" href="<c:url value="/css/inera-certificate-responsive.css"/>?<spring:message code="buildNumber" />">

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
  <div class="container" id="mi-logo-header">
    <div class="content-container">
      <div class="row">
        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 pull-left">
          <a href="/web/start" class="navbar-brand"><img class="page-logo" alt="Gå till inkorgen i Mina intyg. Logo Mina intyg" id="logo" src="/img/logo-minaintyg-white-retina.png" /></a>
        </div>
        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 pull-right">
          <mi-header user-name="<sec:authorize access="isAuthenticated()"><sec:authentication property="principal.username" /></sec:authorize>"></mi-header>
        </div>
      </div>
    </div>
  </div>

  <div class="container">

    <div id="content-container">
      <div class="content">
        <%--         <mi-header user-name="<c:catch><sec:authentication property="principal.username" /></c:catch>"></mi-header> --%>
        <div id="navigation-container"></div>

        <div class="row">
          <div id="content-body" class="col-xs-12 col-sm-7 col-md-7 col-lg-7">

            <c:choose>
              <c:when test="${param.reason eq 'logout'}">
                <h1>
                  <spring:message code="info.loggedout.title" />
                </h1>
                <div id="loggedOut" class="alert alert-info">
                  <spring:message code="info.loggedout.text" />
                </div>
                <p class="btn-row-desc"><spring:message code="info.loggedout.fk.mvkinfo" /></p>
                <div class="btn-row">
                  <a class="btn btn-success" href="http://www.minavardkontakter.se"><spring:message
                      code="info.loggedout.fk.loginagain" /></a>
                </div>
              </c:when>

              <c:when test="${param.reason eq 'denied'}">
                <h1>
                  <spring:message code="error.noauth.title" />
                </h1>
                <div id="noAuth" class="alert alert-info">
                  <spring:message code="error.noauth.text" />
                </div>
                <p class="btn-row-desc"><spring:message code="info.loggedout.fk.mvkinfo" /></p>
                <div class="btn-row">
                  <a class="btn btn-success" href="http://www.minavardkontakter.se"><spring:message
                      code="info.loggedout.fk.loginagain" /></a>
                </div>
              </c:when>

              <c:when test="${param.reason eq 'notfound'}">
                <h1>
                  <spring:message code="error.notfound.title" />
                </h1>
                <div id="notFound" class="alert alert-error">
                  <spring:message code="error.notfound.text" />
                </div>
              </c:when>

              <c:otherwise>
                <h1>
                  <spring:message code="error.generictechproblem.title" />
                </h1>
                <div id="genericTechProblem" class="alert alert-error">
                  <spring:message code="error.generictechproblem.text" />
                </div>
              </c:otherwise>
            </c:choose>
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
