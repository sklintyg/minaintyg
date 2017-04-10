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
<link rel="apple-touch-icon-precomposed" href="/img/touch-icon-small.png" />
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="/img/touch-icon-big.png" />

<!-- bower:css -->
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
    USE_MINIFIED_JAVASCRIPT: '<c:out value="${useMinifiedJavaScript}"/>'
  }
</script>
</head>

<body ng-app="minaintyg">

  <mvk-top-bar></mvk-top-bar>

  <mi-header user-name="<sec:authorize access="isAuthenticated()"><sec:authentication property="principal.username" /></sec:authorize>"></mi-header>

  <div class="container">
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
                <a class="btn btn-primary" href="${mvkMainUrl}"><spring:message
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
                <a class="btn btn-primary" href="${mvkMainUrl}"><spring:message
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
      <script type="text/javascript" src="/bower_components/momentjs/moment.js"></script>
      <script type="text/javascript" src="/bower_components/bootstrap-sass/assets/javascripts/bootstrap.js"></script>
      <!-- endbower -->
      <script type="text/javascript" src="/app/base/app.js"></script>
    </c:otherwise>
  </c:choose>

</body>
</html>
