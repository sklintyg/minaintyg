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

<link rel="stylesheet" href='<c:url value="/mvk-topbar/css/styles.css"/>' />
<link rel="stylesheet" href="<c:url value="/css/bootstrap.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera.css"/>">
<link rel="stylesheet" href="<c:url value="/css/inera-certificate.css"/>">

<script type="text/javascript" src="<c:url value="/js/vendor/jquery-1.9.1.js"/>"></script>
<script type="text/javascript" src="<c:url value="/js/vendor/bootstrap.js"/>"></script>
<script type="text/javascript">
	$(document).ready(function() {
		//Show consentterms
		$("#consentTerms").show();

		$("#noconsentBtn").on("click", function(e) {
			$("#consentTerms").hide(1);
			$("#refuseConsentMessage").fadeIn(400);
			e.preventDefault();
		});
	});
</script>

</head>


<!--BODY -->
<body>
	<div id="headerContainer">
	   <div id="header">
		 <div class="wrapper">
			<a href="####" class="backButton">
				<h1 class="assistiveText">Mina vårdkontakter</h1>
			</a>

			<div class="functionRow">
			<a href="####">Logga ut</a>
			</div>
			<div class="clear"></div>
		 </div>
	   </div>
	</div>

	  <div class="container">
		<div id="content-container">
		  <div class="content">
			<a href="<c:url value="/web/start" />"><img id="logo" src="<c:url value="/img/logo.png" />" /></a>

			<div id="status">
				<div class="status-row">
					<spring:message code="header.loggedInAs" /><br><span class="logged-in"><sec:authentication property="principal.username" /></span>
				</div>
			</div>
			<!-- (tabs) -->
			<div class="row-fluid content-consent">
			  <div id="content-body" class="span12">
				<noscript>
				  <h1>
					<span><spring:message code="error.noscript.title" /></span>
				  </h1>
				  <div class="alert alert-error">
					<spring:message code="error.noscript.text" />
				  </div>
				</noscript>

				<div id="refuseConsentMessage" class="hide span8">
				  <h1>Du har valt att ej ge samtycke för användning</h1>

				  <p>Kanske en text som förklarar innebörden av detta beslut - hur man gör om man i framtiden ångrar sitt beslut etc...</p>

				  <p>Vilka navigeringsmöjligheter skall presenteras? Stäng tjänst eller länk till MVK?</p>
				  <input id="noconsent" type="button" value="Stäng sidan?" class="btn btn-info" />
				</div>
				<!--CONTENT  -->
				<div id="consentTerms" class="hide offset1 span8">
				  <h1>Samtycke för användning</h1>

				  <h2>Personuppgifter från patientjournalen</h2>

				  <p>Intygen som du hanterar i Mina intyg är utfärdade av vården och uppgifterna är en del av din patientjournal. När du ger ditt samtycke godkänner du:
				  <ul class="italic">
					<li>att dina personuppgifter i intygen får hanteras och visas i Mina intyg.</li>
					<li>att dina personuppgifter i intygen får hanteras för statistiska ändamål.</li>
				  </ul>

				  <h2>Personuppgifter som hanteras i Mina intyg</h2>

				  <p>
					De personuppgifter som hanteras i Mina intyg är de uppgifter som finns i intyget.<br /> Det är: <em>namn, personnummer, adress, diagnos, sjukdomsförlopp, funktionsnedsättning,
					  aktivitetsbegränsning, rekommendationer och planerad eller pågående behandling, arbetsförmåga, prognos och vårdbesök.</em>
				  </p>

				  <h2>Syfte med hanteringen av personuppgifter</h2>

				  <p>Personuppgifterna finns i Mina intyg som en service till invånaren. Du ska enkelt kunna hantera dina intyg och välja när och till vem du vill skicka ett intyg. Uppgifterna kommer
					också att användas för att ta fram statistik om sjukskrivningar. Statistiken som tas fram går inte att härleda till dig eller någon viss person.</p>

				  <h2>Aktiviteter i intygstjänsten</h2>

				  <p>I Mina intyg hanteras och visas dina intyg. All aktivitet som sker, styr du själv över, som till exempel att överföra intyg till Försäkringskassan.</p>

				  <h2>Ansvarig</h2>

				  <p>Intygstjänsten drivs av Inera AB. I intygstjänsten behandlar Inera AB personuppgifter om enskilda personer i egenskap av personuppgiftsbiträde på uppdrag av vården.
				  <h2>Dina rättigheter</h2>

				  <p>Som användare av intygstjänsten har du bland annat rätt att:</p>
				  <ul class="italic">
					<li>varje år begära kostnadsfri information om personuppgifter som Inera AB behandlar</li>
					<li>när som helst begära rättelse av uppgifter</li>
					<li>när som helst begära spärrning eller radering av personuppgifter förutsatt att det finns godtagbara skäl, att uppgifterna uppenbarligen inte behövs för din vård eller att det
					  ur allmän synpunkt uppenbarligen inte finns skäl att bevara uppgifterna</li>
					<li>kräva skadestånd enligt 10 kap. 1 § PDL eller 48 § PUL vid behandling av personuppgifter i strid med lag. Du har också rätt att begära att Inera AB underrättar tredje man,
					  till vilken uppgifter lämnas ut, om uppgifternas felaktighet samt att få information om sökbegrepp, direktåtkomst och utlämnande av dina uppgifter som förekommit.</li>
				  </ul>

				  <h2>Återta samtycke</h2>

				  <p>Du kan när som helst återkalla ditt samtycke. Det innebär att uppgifter om dig inte längre kommer att visas eller på annat sätt behandlas i Mina intyg. När du återkallar ditt
					samtycke kan du inte längre använda Mina intyg.</p>

				  <p>Du kan när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.</p>
				  <br />

				  <p>Jag har läst och förstått ovanstående och lämnar mitt samtycke till hantering av mina personuppgifter i Mina intyg (krävs för att använda webbtjänsten).</p>

				  <form action='<c:url value="/web/ge-samtycke"/>' method="post">
					<p class="btn-row">
					  <input id="giveconsent" type="submit" value="Jag ger mitt samtycke" class="btn btn-success" />
					</p>
				  </form>
				</div>
				<!--/ CONTENT -->

			  </div>
			</div>
		  </div>
		</div>
	  </div>

</body>
</html>
