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


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="ROBOTS" content="nofollow, noindex" />

<title><spring:message code="application.name" /></title>

<link rel="icon" href="<c:url value="/favicon.ico" />" type="image/vnd.microsoft.icon" />

<link rel="stylesheet" href="<c:url value="/css/bootstrap.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera-certificate.css"/>">

</head>

<body ng-app="ListCertApp">
  <div class="container">
    <div id="page-header-container">
      <div id="page-header">
        <div id="page-header-left"></div>
        <div id="page-header-right"></div>
        <a href="<c:url value="/web/start" />"><img id="logo" src="<c:url value="/img/logo_mina_intyg.png" />" /></a>
        <div id="status">
          <div class="status-row">
            <a href="<c:url value="/web/settings" />"><spring:message code="label.settings" /></a> | <a href="<c:url value="/web/logout" />"><spring:message code="label.logout" /></a>
          </div>
          <div class="status-row">
            <span class="logged-in"><spring:message code="header.loggedInAs" /></span>&nbsp;<strong><sec:authentication property="principal.username" /></strong>
          </div>
        </div>
      </div>
    </div>
    <div id="content-container">
      <div class="content">

        <div class="row-fluid">
          <div id="content-body" class="span12">


                
                <div ng-view></div>
          </div>
        </div>
      </div>
    </div>
  </div>

  <script type="text/javascript" src="<c:url value="/js/vendor/jquery-1.9.1.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/vendor/bootstrap.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/vendor/angular/angular.js"/>"></script>

  <script type="text/javascript" src="<c:url value="/js/list/app.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/list/controllers.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/list/directives.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/list/filters.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/list/services.js"/>"></script>


</body>
</html>
