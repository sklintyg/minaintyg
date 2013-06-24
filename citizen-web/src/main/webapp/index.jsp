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
<html lang="sv" xmlns:ng="http://angularjs.org" id="ng-app" ng-app="BaseApp">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="ROBOTS" content="nofollow, noindex" />

<title><spring:message code="application.name" /></title>

<link rel="icon" href="<c:url value="/favicon.ico" />" type="image/vnd.microsoft.icon" />

<link rel="stylesheet" href="<c:url value="/mvk-topbar/css/styles.css"/>">
<link rel="stylesheet" href="<c:url value="/css/bootstrap/2.3.2/bootstrap.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera-certificate.css"/>">
</head>
<body ng-app="BaseApp">

  <mvk-top-bar hide-logout="true"></mvk-top-bar>

  <div class="container">

    <div id="content-container">
      <div class="content">

        <div id="navigation-container">
          <mi-header user-name=""></mi-header>
        </div>

        <div id="content-body" class="row-fluid">
          <div class="span7">
  						<h1>Hej! Välkommen till Mina intyg.</h1>
  						<p class="ingress">Mina intyg är en säker webbtjänst där du kan hantera dina läkarintyg via internet.</p>
  						<p>I Mina intyg kan du läsa, skriva ut och spara dina intyg och du kan skicka intyg till Försäkringskassan. Hanteringen sköter du via ditt användarkonto och tjänsten är tillgänglig dygnet runt.</p>
  						<p>Första gången du loggar in i Mina intyg måste du ge ditt samtycke till att Mina intyg får hantera dina personuppgifter. Därefter kan du börja använda tjänsten omedelbart. Du kan bara använda tjänsten för dig själv. Barn och andra anhöriga måste ha egna användarkonton.</p>
  						<p>All informationsöverföring är skyddad (krypterad) och uppfyller vårdens krav på säkerhet och sekretess. Det kostar inget att använda tjänsten.</p>

  						<h2>Bakgrund</h2>
  						<p>Under 2011 infördes tjänsten läkarintyg i Sverige. Med tjänsten kan
  							läkarintyg skickas elektroniskt från vården till Försäkringskassan. Mina
  							intyg är en vidareutveckling av tjänsten som gör det möjligt för invånare
  							att ska kunna hantera sina läkarintyg och få inflytande över vilken
  							mottagare som ska få tillgång till läkarintyget och när. I dagsläget går det
  							att skicka läkarintyg vidare till Försäkringskassan, men tjänsten förbättras
  							hela tiden för att göra det möjligt att skicka till andra mottagare som
  							exempelvis till arbetsgivare och försäkringsbolag.</p>

              <p class="btn-row-desc">Observera att inloggningen sker hos Mina vårdkontakter.</p>
  						<div class="btn-row">
  							<a class="btn btn-success" href="http://www.minavardkontakter.se">Logga in</a>
  						</div>
  					</div>
  					<div class="span5">
  						<img id="welcome-image" src="<c:url value="/img/hand.jpg" />" />
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
  <script type="text/javascript" src='<c:url value="/js/vendor/ui-bootstrap/0.3.0/ui-bootstrap-tpls-0.3.0.js"/>'></script>

  <%-- Application files --%>
  <script type="text/javascript" src="<c:url value="/js/base/app.js"/>"></script>

  <%-- Dependencies to common components --%>
  <script type="text/javascript" src="<c:url value="/js/modules/message-module.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/modules/mi-header-directive.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/js/modules/common-message-resources.js"/>"></script>
</body>
</html>
