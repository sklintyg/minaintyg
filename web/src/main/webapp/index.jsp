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

<c:set var="useMinifiedJavaScript" value="${pageAttributes.useMinifiedJavaScript}"/>
<c:set var="mvkMainUrl" value="${pageAttributes.mvkMainUrl}"/>

<!DOCTYPE html>
<html lang="sv" id="ng-app" ng-app="minaintyg">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="ROBOTS" content="nofollow, noindex" />
<meta name="viewport" content="width=320,initial-scale=1.0,target-densityDPI=320dpi">

<title><spring:message code="application.name" /></title>

<link rel="icon" href="<c:url value="/favicon.ico" />" type="image/vnd.microsoft.icon" />

<link rel="stylesheet" href="<c:url value="/mvk-topbar/css/styles.css"/>">
<!-- bower:css -->
<link rel="stylesheet" href="/bower_components/bootstrap/dist/css/bootstrap.css" />
<!-- endbower -->

<!-- injector:css -->
<link rel="stylesheet" href="/app/app.css?_v=<spring:message code="buildNumber" />">
<!-- endinjector -->

<script type="text/javascript">
  /**
   Global JS config/constants for this app, to be used by scripts
   **/
  var MI_CONFIG = {
    BUILD_NUMBER: '<spring:message code="buildNumber" />',
    USE_MINIFIED_JAVASCRIPT: '${useMinifiedJavaScript}'
  }
</script>

</head>

<body ng-app="minaintyg">

  <mvk-top-bar hide-logout="true"></mvk-top-bar>
  <div class="container" id="mi-logo-header">
    <div class="content-container">
      <a href="/web/start" class="navbar-brand"><img alt="Gå till inkorgen i Mina intyg. Logo Mina intyg" id="logo" src="/img/logo-minaintyg-white.png" /></a>
    </div>
  </div>

  <mi-cookie-banner></mi-cookie-banner>

  <div class="container">

    <div id="content-container">
      <div class="content">

        <!--         <div id="navigation-container"> -->
        <!--           <mi-header user-name=""></mi-header> -->
        <!--         </div> -->

        <div id="content-body" class="row">
          <div class="col-xs-12 col-sm-7 col-md-7 col-lg-7">
            <h1>Välkommen till Mina intyg</h1>
            <p class="ingress">Mina intyg är en säker webbtjänst där du kan hantera dina läkarintyg</p>
            <p>I Mina intyg kan du läsa, skriva ut och spara dina intyg och du kan skicka intyg till Försäkringskassan.
              Det enda du behöver är en e-legitimation för att kunna logga in och använda tjänsten.</p>
            <p>All informationsöverföring är skyddad (krypterad) och uppfyller vårdens krav på säkerhet och sekretess.
              Det kostar inget att använda tjänsten och du kan hantera dina intyg när och var det passar dig.</p>
            <p>Första gången du loggar in i Mina intyg måste du ge ditt samtycke till att dina personuppgifter hanteras
              i tjänsten. Därefter kan du börja använda Mina intyg omedelbart. Du kan bara använda tjänsten för din egen
              person.</p>
            <p>
              <a href="${mvkMainUrl}/C125755F00329208/p/KONT-8ZSGV8?opendocument">Mer information om
                hur du skaffar en e-legitimation</a>
            </p>
            <p class="btn-row-desc">Inloggningen sker via 1177 Vårdguiden</p>
            <div class="btn-row">
              <a class="btn btn-success" href="${mvkMainUrl}">Logga in</a>
            </div>

          </div>
          <div class="hidden-xs col-sm-5 col-md-5 col-lg-5">
            <img id="welcome-image" src="<c:url value="/img/hand.jpg" />" />
          </div>
        </div>

      </div>
    </div>
  </div>

  <c:choose>
    <c:when test="${useMinifiedJavaScript == 'true'}">
      <script type="text/javascript" src="/bower_components/jquery/jquery.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular/angular.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular-animate/angular-animate.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular-cookies/angular-cookies.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular-i18n/angular-locale_sv-se.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular-sanitize/angular-sanitize.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular-bootstrap/ui-bootstrap-tpls.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/angular-ui-router/release/angular-ui-router.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/bootstrap/dist/js/bootstrap.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/momentjs/min/moment.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/app/base/app.min.js?<spring:message code="buildNumber" />"></script>
    </c:when>
    <c:otherwise>
      <!-- bower:js -->
      <script type="text/javascript" src="/bower_components/jquery/jquery.js"></script>
      <script type="text/javascript" src="/bower_components/angular/angular.js"></script>
      <script type="text/javascript" src="/bower_components/angular-animate/angular-animate.js"></script>
      <script type="text/javascript" src="/bower_components/angular-cookies/angular-cookies.js"></script>
      <script type="text/javascript" src="/bower_components/angular-i18n/angular-locale_sv-se.js"></script>
      <script type="text/javascript" src="/bower_components/angular-sanitize/angular-sanitize.js"></script>
      <script type="text/javascript" src="/bower_components/angular-bootstrap/ui-bootstrap-tpls.js"></script>
      <script type="text/javascript" src="/bower_components/angular-ui-router/release/angular-ui-router.js"></script>
      <script type="text/javascript" src="/bower_components/bootstrap/dist/js/bootstrap.js"></script>
      <script type="text/javascript" src="/bower_components/momentjs/moment.js"></script>
      <!-- endbower -->
      <script type="text/javascript" src="/app/base/app.js"></script>
    </c:otherwise>
  </c:choose>

</body>
</html>
