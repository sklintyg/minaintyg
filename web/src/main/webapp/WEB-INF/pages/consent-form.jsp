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

<!DOCTYPE html>
<html lang="sv" id="ng-app">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="ROBOTS" content="nofollow, noindex" />
<meta name="viewport" content="width=320,initial-scale=1.0,target-densityDPI=320dpi" />

<title ng-bind="$root.page_title"><spring:message code="application.name" /></title>

<link rel="icon" href="<c:url value="/favicon.ico" />" type="image/vnd.microsoft.icon" />

<!-- bower:css -->
<!-- endbower -->

<!-- injector:css -->
<link rel="stylesheet" href="/app/app.css?_v=<spring:message code="buildNumber" />">
<link rel="stylesheet" href="/font/css/animation.css?_v=<spring:message code="buildNumber" />">
<link rel="stylesheet" href="/font/css/fontello.css?_v=<spring:message code="buildNumber" />">
<!-- endinjector -->

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
<body class="consent-background">

  <mvk-top-bar></mvk-top-bar>

  <mi-header user-name="<sec:authentication property="principal.username" />"></mi-header>

  <mi-cookie-banner></mi-cookie-banner>

  <div class="container container-narrow">
    <div class="content">
      <div class="row">
        <div id="content-body" class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
          <%-- No script to show at least something when javascript is off --%>
          <noscript>
            <h1>
              <span><spring:message code="error.noscript.title" /></span>
            </h1>
            <div class="alert alert-danger">
              <spring:message code="error.noscript.text" />
            </div>
          </noscript>
          <%-- ng-view that holds dynamic content managed by angular app --%>
          <div id="view" ui-view></div>
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
      <script type="text/javascript" src="/bower_components/bootstrap-sass/assets/javascripts/bootstrap.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/momentjs/min/moment.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/bower_components/ngstorage/ngStorage.min.js?<spring:message code="buildNumber" />"></script>
      <script type="text/javascript" src="/app/app.min.js?<spring:message code="buildNumber" />"></script>
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
      <script type="text/javascript" src="/bower_components/angular-breadcrumb/release/angular-breadcrumb.js"></script>
      <script type="text/javascript" src="/bower_components/momentjs/moment.js"></script>
      <script type="text/javascript" src="/bower_components/ngstorage/ngStorage.js"></script>
      <script type="text/javascript" src="/bower_components/bootstrap-sass/assets/javascripts/bootstrap.js"></script>
      <!-- endbower -->
      <script type="text/javascript" src="/app/app.js"></script>
    </c:otherwise>
  </c:choose>

</body>
</html>
