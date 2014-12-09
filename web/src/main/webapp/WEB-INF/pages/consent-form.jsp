<%--

    Copyright (C) 2013 Inera AB (http://www.inera.se)

    This file is part of Inera Certificate Web (http://code.google.com/p/inera-certificate-web).

    Inera Certificate Web is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    Inera Certificate Web is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html lang="sv" id="ng-app" ng-app="minaintyg">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="ROBOTS" content="nofollow, noindex" />
<meta name="viewport" content="width=320,initial-scale=1.0,target-densityDPI=320dpi" />

<title ng-bind="$root.page_title"><spring:message code="application.name" /></title>

<link rel="icon" href="<c:url value="/favicon.ico" />" type="image/vnd.microsoft.icon" />

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
    LOGIN_METHOD: '<sec:authentication property="principal.loginMethod" />',
    USE_MINIFIED_JAVASCRIPT: '<c:out value="${useMinifiedJavaScript}"/>'
  }
</script>

</head>
<body>

  <mvk-top-bar></mvk-top-bar>
  <div class="container" id="mi-logo-header">
    <div class="content-container">
      <div class="row">
        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 pull-left">
          <a href="/web/start" class="navbar-brand"><img class="page-logo" alt="Gå till inkorgen i Mina intyg. Logo Mina intyg" id="logo" src="/img/logo-minaintyg-white-retina.png" /></a>
        </div>
        <div class="col-xs-6 col-sm-6 col-md-6 col-lg-6 pull-right">
          <mi-header user-name="<sec:authentication property="principal.username" />"></mi-header>
        </div>
      </div>
    </div>
  </div>

  <div class="container">

    <div id="content-container">
      <div class="content">

        <div class="row">
          <div id="content-body" class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <%-- No script to show at least something when javascript is off --%>
            <noscript>
              <h1>
                <span><spring:message code="error.noscript.title" /></span>
              </h1>
              <div class="alert alert-error">
                <spring:message code="error.noscript.text" />
              </div>
            </noscript>
            <%-- ng-view that holds dynamic content managed by angular app --%>
            <div id="view" ng-view></div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!--[if lte IE 8]>
  <script>
    window.myCustomTags = [ 'miHeader', 'mvkTopBar', 'message', 'miField', 'miSpinner', 'ngLocale' ]; // optional
  </script>
  <script type="text/javascript" src="<c:url value="/js/ie/ie-angular-shiv.js"/>"></script>
  <![endif]-->

  <c:choose>
    <c:when test="${useMinifiedJavaScript == 'true'}">
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/i18n/angular-locale_sv-se.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular-cookies.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular-route.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular-sanitize.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular-animate.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angular-ui-bootstrap/0.11.2/ui-bootstrap-tpls.min.js"></script>
      <script type="text/javascript" src="/web/webjars/jquery/1.9.0/jquery.min.js"></script>
      <script type="text/javascript" src="/js/app.min.js?<spring:message code="buildNumber" />"></script>
    </c:when>
    <c:otherwise>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/i18n/angular-locale_sv-se.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular-cookies.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular-route.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular-sanitize.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular-animate.js"></script>
      <script type="text/javascript" src="/web/webjars/angular-ui-bootstrap/0.11.2/ui-bootstrap-tpls.js"></script>
      <script type="text/javascript" src="/web/webjars/jquery/1.9.0/jquery.js"></script>
      <script type="text/javascript" src="/js/app.js"></script>
    </c:otherwise>
  </c:choose>

</body>
</html>
