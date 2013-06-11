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

<script type="text/javascript" src="<c:url value="/lib/jquery-1.9.1.js"/>"></script>
<script type="text/javascript" src="<c:url value="/lib/bootstrap.js"/>"></script>

</head>


<body>
  <div class="container">
    <div id="page-header-container">
      <div id="page-header">
        <div id="page-header-left"></div>
        <div id="page-header-right"></div>
        <img id="logo" src="<c:url value="/img/logo_mina_intyg.png" />" />
      </div>
    </div>
    <div id="content-container">
      <div class="content" style="padding-top: 0px;">
        <div class="row-fluid">
          <div id="content-body" class="span12" style="padding-top: 25px;">
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
              <input id="guid" type="text" class="span6" placeholder="ange guid">
              <input type="button" class="btn" onclick="location.href='/web/sso?guid=' + this.form.guid.value;" value="logga in">
            </form>



          </div>
        </div>
      </div>

    </div>
  </div>
</body>
</html>
