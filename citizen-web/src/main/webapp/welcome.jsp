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
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>

<%
String profile = System.getProperty("spring.profiles.active");
if ("prod".equals(profile)) {
    response.sendError(HttpServletResponse.SC_NOT_FOUND);
} else {
%>
<!DOCTYPE html>
<html lang="sv" xmlns:ng="http://angularjs.org" id="ng-app" ng-app="BaseApp">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="ROBOTS" content="nofollow, noindex" />

<title><spring:message code="application.name" />-<spring:message code="project.version"/>-<%= profile %></title>

<link rel="icon" href="<c:url value="/favicon.ico" />" type="image/vnd.microsoft.icon" />

<link rel="stylesheet" href="<c:url value="/mvk-topbar/css/styles.css"/>">
<link rel="stylesheet" href="<c:url value="/css/bootstrap/2.3.2/bootstrap.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera-certificate.css"/>">
</head>
<body ng-app="BaseApp">

  <mvk-top-bar></mvk-top-bar>

  <div class="container">

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
              <input id="guid" type="text" class="col-xs-6 col-sm-6 col-md-6 col-lg-6" placeholder="ange guid"> <input type="button" class="btn" onclick="location.href='/web/sso?guid=' + this.form.guid.value;"
                value="logga in">
            </form>
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

  <script type="text/javascript" src="<c:url value="/js/vendor/angular/1.1.5/angular.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/vendor/angular/1.1.5/i18n/angular-locale_sv-se.js"/>"></script>
  <script type="text/javascript" src='<c:url value="/js/vendor/ui-bootstrap/0.7.0/ui-bootstrap-tpls-0.7.0.js"/>'></script><!-- Please notice that this is a modified version with the bottom part of the datepicker template commented out (it couldn't be done in a nicer way unfortunately, customization is limited) -->

  <%-- Application files --%>
  <script type="text/javascript" src="<c:url value="/js/base/app.js"/>"></script>

  <%-- Dependencies to common components --%>
  <script type="text/javascript" src="<c:url value="/js/modules/message-module.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/modules/mi-header-directive.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/modules/common-message-resources.js"/>"></script>

</body>
</html>
<%
}
%>
