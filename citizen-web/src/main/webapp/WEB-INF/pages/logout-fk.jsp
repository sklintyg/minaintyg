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
<%@ page language="java" isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
         trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>


<!DOCTYPE html>
<html lang="sv" id="ng-app" ng-app="minaintyg">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="ROBOTS" content="nofollow, noindex" />
<meta name="viewport" content="width=320,initial-scale=1.0,target-densityDPI=320dpi">

<title><spring:message code="application.name" /></title>


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

  <mvk-top-bar hide-logout="true"></mvk-top-bar>

  <div class="container">

    <div id="content-container">
      <div class="content">
        <mi-header user-name=""></mi-header>
        <div id="navigation-container"></div>

        <div class="row">
          <div id="content-body" class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
            <h1>
              <spring:message code="info.loggedout.fk.title" />
            </h1>
            <div id="genericTechProblem" class="alert alert-info">
              <spring:message code="info.loggedout.fk.text" />
            </div>
          </div>
          <p class="btn-row-desc"><spring:message code="info.loggedout.fk.mvkinfo" /></p>
          <div class="btn-row">
            <a class="btn btn-success" href="http://www.minavardkontakter.se"><spring:message
                code="info.loggedout.fk.loginagain" /></a>
          </div>
        </div>
      </div>

    </div>
  </div>

  <!--[if lte IE 8]>
  <script>
    window.myCustomTags = [ 'miHeader', 'mvkTopBar', 'message' ]; // optional
  </script>
  <script type="text/javascript" src="<c:url value="/js/ie/ie-angular-shiv.js"/>"></script>
  <![endif]-->

  <c:choose>
    <c:when test="${useMinifiedJavaScript == 'true'}">
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/angular.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/i18n/angular-locale_sv-se.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/angular-cookies.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/angular-route.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/angular-sanitize.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angular-ui-bootstrap/0.10.0/ui-bootstrap-tpls.min.js"></script>
      <script type="text/javascript" src="/web/webjars/jquery/1.9.0/jquery.min.js"></script>
      <script type="text/javascript" src="/js/base/app.min.js?<spring:message code="buildNumber" />"></script>
    </c:when>
    <c:otherwise>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/angular.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/i18n/angular-locale_sv-se.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/angular-cookies.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/angular-route.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.14/angular-sanitize.js"></script>
      <script type="text/javascript" src="/web/webjars/angular-ui-bootstrap/0.10.0/ui-bootstrap-tpls.js"></script>
      <script type="text/javascript" src="/web/webjars/jquery/1.9.0/jquery.js"></script>
      <script type="text/javascript" src="/js/base/app.js"></script>
    </c:otherwise>
  </c:choose>

</body>
</html>
