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
<%@ page language="java" isErrorPage="true" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags"%>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="ROBOTS" content="nofollow, noindex" />

<title><spring:message code="application.name" /></title>


<link rel="stylesheet" href="<c:url value="/mvk-topbar/css/styles.css"/>">
<link rel="stylesheet" href="<c:url value="/css/bootstrap.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera-certificate.css"/>">
</head>


<body ng-app="BaseApp">

  <mvk-top-bar></mvk-top-bar>

  <div class="container">

    <div id="content-container">
      <div class="content">
        <mi-header user-name="<sec:authentication property="principal.username" />"></mi-header>
        <div id="navigation-container"></div>

        <div class="row-fluid">
          <div id="content-body" class="span12">


            <c:choose>
              <c:when test="${param.reason eq \"logout\"}">
                <h1>
                  <spring:message code="info.loggedout.title" />
                </h1>
                <div id="loggedOut" class="alert alert-info">
                  <spring:message code="info.loggedout.text" />
                </div>
              </c:when>

              <c:when test="${param.reason eq \"denied\"}">
                <h1>
                  <spring:message code="error.noauth.title" />
                </h1>
                <div id="noAuth" class="alert alert-error">
                  <spring:message code="error.noauth.text" />
                </div>
              </c:when>

              <c:when test="${param.reason eq \"notfound\"}">
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


  <script type="text/javascript" src="<c:url value="/js/vendor/angular/angular.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/vendor/angular/i18n/angular-locale_sv-se.js"/>"></script>
  <script type="text/javascript" src='<c:url value="/js/vendor/ui-bootstrap/ui-bootstrap-tpls-0.3.0.js"/>'></script>

  <%-- Application files --%>
  <script type="text/javascript" src="<c:url value="/js/base/app.js"/>"></script>

  <%-- Dependencies to common components --%>
  <script type="text/javascript" src="<c:url value="/js/modules/message-module.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/modules/mi-header-directive.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/modules/common-message-resources.js"/>"></script>
  <!-- 
  Error:
  <c:catch>
   <c:out value="${pageContext.errorData.throwable.message}" />, 
   
   Stacktrace:
  <c:forEach items="${pageContext.errorData.throwable.stackTrace}" var="element">
    <c:out value="${element}" />, 
    </c:forEach>
   </c:catch>
-->
</body>
</html>
