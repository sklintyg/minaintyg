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
<link rel="stylesheet" href="<c:url value="/web/webjars/bootstrap/3.1.1/css/bootstrap.min.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera-certificate.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera-certificate-responsive.css"/>">

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
              <a href="http://www.minavardkontakter.se/C125755F00329208/p/KONT-8ZSGV8?opendocument">Mer information om
                hur du skaffar en e-legitimation</a>
            </p>
            <p class="btn-row-desc">Inloggningen sker via Mina vårdkontakter</p>
            <div class="btn-row">
              <a class="btn btn-success" href="${mvkMainUrl}">Logga in</a>
            </div>

            <div style="padding-top: 50px">
              <p>Mina intyg använder cookies. <a href="#" onclick="toggle(); return false;">Läs mer om Kakor
                (cookies)</a></p>
              <div class="bluebox" id="cookiejar">
                <h3>Om Kakor (cookies)</h3>
                <p>Så kallade kakor (cookies) används för att underlätta för besökaren på webbplatsen. En kaka är en
                  textfil som lagras på din dator och som innehåller information. Denna webbplats använder så kallade
                  sessionskakor. Sessionskakor lagras temporärt i din dators minne under tiden du är inne på en
                  webbsida. Sessionskakor försvinner när du stänger din webbläsare. Ingen personlig information om dig
                  sparas vid användning av sessionskakor. Om du inte accepterar användandet av kakor kan du stänga av
                  det via din webbläsares säkerhetsinställningar. Du kan även ställa in webbläsaren så att du får en
                  varning varje gång webbplatsen försöker sätta en kaka på din dator. </p><p><strong>Observera!</strong>
                Om du stänger av kakor i din webbläsare kan du inte logga in i Mina Intyg.</p><p>Allmän information om
                kakor (cookies) och lagen om elektronisk kommunikation finns på Post- och telestyrelsens webbplats.</p>
                <p>
                  <a href='http://www.pts.se/sv/Privat/Internet/Integritet1/Fragor-och-svar-om-kakor-for-anvandare/'
                     target='_blank'>Mer om kakor (cookies) på Post- och telestyrelsens webbplats</a>
                </p>
              </div>
            </div>

            <script>
              var cookiejar = document.getElementById('cookiejar');
              cookiejar.style.display = 'none';

              function toggle() {
                if (cookiejar.style.display == 'none')
                  cookiejar.style.display = 'block';
                else
                  cookiejar.style.display = 'none';
              }
            </script>
          </div>
          <div class="hidden-xs col-sm-5 col-md-5 col-lg-5">
            <img id="welcome-image" src="<c:url value="/img/hand.jpg" />" />
          </div>
        </div>

      </div>
    </div>
  </div>

  <!--[if lte IE 8]>
  <script>
    window.myCustomTags = [ 'miHeader', 'mvkTopBar', 'message', 'miField', 'miSpinner', 'ngLocale' ]; // optional
  </script>
  <script type="text/javascript" src="<c:url value="/app/ie/ie-angular-shiv.js"/>"></script>
  <script type = "text/javascript" src="<c:url value="/web/webjars/respond/1.4.2/src/matchmedia.polyfill.js"/>"></script>
  <script type="text/javascript" src="<c:url value="/web/webjars/respond/1.4.2/src/respond.js"/>"></script>
  <![endif]-->

  <c:choose>
    <c:when test="${useMinifiedJavaScript == 'true'}">
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/i18n/angular-locale_sv-se.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular-cookies.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular-sanitize.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angular-ui-bootstrap/0.11.2/ui-bootstrap-tpls.min.js"></script>
      <script type="text/javascript" src="/web/webjars/angular-ui-router/0.2.13/angular-ui-router.min.js"></script>
      <script type="text/javascript" src="/web/webjars/jquery/1.9.0/jquery.min.js"></script>
      <script type="text/javascript" src="/app/base/app.min.js?<spring:message code="buildNumber" />"></script>
    </c:when>
    <c:otherwise>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/i18n/angular-locale_sv-se.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular-cookies.js"></script>
      <script type="text/javascript" src="/web/webjars/angularjs/1.2.27/angular-sanitize.js"></script>
      <script type="text/javascript" src="/web/webjars/angular-ui-bootstrap/0.11.2/ui-bootstrap-tpls.js"></script>
      <script type="text/javascript" src="/web/webjars/angular-ui-router/0.2.13/angular-ui-router.js"></script>
      <script type="text/javascript" src="/web/webjars/jquery/1.9.0/jquery.js"></script>
      <script type="text/javascript" src="/app/base/app.js"></script>
    </c:otherwise>
  </c:choose>

</body>
</html>
