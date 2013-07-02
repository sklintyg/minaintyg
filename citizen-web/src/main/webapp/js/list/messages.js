'use strict';
var minaIntygResources = {
    "sv" : {
        "label.showall" : "Visa alla",
        "label.showfewer" : "Visa färre",
        "label.restore" : "Återställ",
        "label.certificatesloading" : "Dina intyg laddas. Vänligen vänta...",
        "label.archivedcertificatesloading" : "Arkiverade intyg laddas. Vänligen vänta... ",
        "button.archive" : "Arkivera",
        "button.alt.archive" : "Arkivera intyget. Flyttar intyget till Arkiverade intyg",
        "button.send" : "Skicka",
        "button.show" : "Visa intyget",
        
        "inbox.header" : "Inkorgen",
        "inbox.description" : "Här listas dina intyg med det senast inkomna intyget överst. Välj Visa intyget för att titta på det, skriva ut det eller spara det till din dator.",
	    "inbox.revoked" : "Vården har dragit tillbaka detta intyg. Det kan därför inte längre visas eller skickas. Kontakta vården om du har några frågor.",
       
        "archived.header" : "Arkiverade intyg",
        "archived.description" : "<p>Ett läkarintyg innehåller information som hämtas från din patientjournal. Därför gäller samma lagar och regler för ditt intyg som för journalen. Det innebär att: <ul><li>Du kan inte helt ta bort ditt intyg från intygstjänsten. Däremot kan du flytta dina gamla intyg till mappen \"borttagna intyg\".</li><li>Uppgifter i patientjournalen måste lagras i minst 10 år enligt lag. Ofta lagras de längre, och i vissa fall livet ut.</li><li>Om någon uppgift är fel i ditt intyg ska du vända dig till din vårdgivare. De kan då ändra uppgiften i journalen.</li></ul></p><p><a href=\"http://www.datainspektionen.se/lagar-och-regler/patientdatalagen\" target=\"_blank\">Läs mer om lagring av uppgifter och ändring av information hos Datainspektionen</a>.</p>    <p>För att återställa ett borttaget intyg välj Återställ.</p>",

        "about.header": "Om Mina intyg",

        "error.nocerts" : "Du har för närvarande inga intyg i din inkorg.",
        "error.noarchivedcerts" : "Du har för närvarande inga arkiverade intyg",

	    "about.omminaintyghead": "Om Mina intyg",
	    "about.omminaintygtext": "<p>Mina intyg är en säker webbtjänst där du kan hantera dina intyg via internet. För att kunna använda tjänsten behöver du ha en e-legitimation och ett användarkonto hos Mina vårdkontakter. Det går även att nå tjänsten från Försäkringskassans Mina sidor om du har ett användarkonto där.</p> <h3>Säkerhet</h3> <p>All informationsöverföring är skyddad (krypterad) och uppfyller vårdens krav på säkerhet och sekretess. Det kostar inget att använda tjänsten. (kanske bör stå något mer om säkerheten här?)</p> <h3>Så använder du Mina intyg</h3> <p>I Mina intyg kan du enkelt hantera de intyg som vården utfärdat för dig. Du kan läsa, skriva ut och spara dina intyg. Du kan även skicka intyg vidare till Försäkringskassan, arbetsgivare eller någon annan registrerad mottagare. Hanteringen sköter du via ditt användarkonto och tjänsten är tillgänglig dygnet runt.</p> <h3>Ny användare</h3> <p>Första gången du loggar in i Mina intyg måste du ge ditt samtycke till att Mina intyg får hantera dina personuppgifter. Därefter kan du börja använda tjänsten omedelbart. Du kan bara använda tjänsten för dig själv. Barn och andra anhöriga måste ha egna användarkonton.</p> <p><a href ng-click=\"navigateTo('samtycke')\">Läs mer om samtycke</a></p> <h3>Ansvarig för tjänsten</h3> <p>[kort text om Inera AB – ska kompletteras med]</p> <h3>Bakgrund</h3> <p>Under 2011 infördes e-tjänsten läkarintyg i Sverige. Det innebär att läkaren kan skicka läkarintyg elektroniskt från sitt journalsystem till Försäkringskassan. Mina intyg är en utveckling av tjänsten så att invånarna själva ska kunna hantera sina läkarintyg och få inflytande över vilken mottagare som ska få tillgång till intygen. I dag går det att skicka läkarintyg till Försäkringskassan och arbete pågår så att det ska bli möjligt att skicka läkarintyg till andra mottagare, som till exempel arbetsgivare och försäkringsbolag.</p> <p>Om du vill veta mer:</p> <p><a href=\"http://www.minavardkontakter.se/C125755F00329208/p/startpage?opendocument\" target=\"_blank\">Mer information om Mina Vårdkontakter</a></p> <p><a href=\"http://www.minavardkontakter.se/C125755F00329208/p/KONT-8ZSGV8?opendocument\" target=\"_blank\">Information om e-legitimation på Mina vårdkontakter</a></p> <p><a href=\"http://www.forsakringskassan.se/privatpers/om_sjalvbetjaning/!ut/p/b1/hY7BCoJAGISfpRfYf_5Vd9ejW5AWSlmQ7SUsQoRWO0TR26fRtZrbwDcfQ44qcl19b5v61vZdfRm7UwcLILacYF4ajSyY5tKUkQR4APYDgC9J8G-_INcevXicvIDgUMoAOgw4VrEGK9qRexuMRG5Hg0kUI-N0y-vClrNl9AF-PCjS3p_p6qvnZtVMXingJIY!/dl4/d5/L2dJQSEvUUt3QS80SmtFL1o2XzgyME1CQjFBMDhTMDgwSTlIOUVGOTJHQTMx/\" target=\"_blank\">Försäkringskassan information om Mina sidor</a></p> <div class=\"alert alert-info extra-margin-top\"> <h3>Tekniska krav och tillgänglighet</h3> <p>Mina intyg följer i största mån samma riktlinjer som Mina vårdkontakter.</p> <p><a href=\"https://www.minavardkontakter.se/C125755F00329208/p/OSAL-7PBG4J?opendocument\" target=\"_blank\">Se informationen under menyvalet Om Mina vårdkontakter</a> </div>",
		"about.consentheader":"Samtycke",
	    "about.consenttext": " <h2>Här följer samtycket som du tidigare har accepterat</h3> <p>Innan du kan använda Mina intyg måste du ge ditt samtycke till att dina intyg får hanteras och visas i webbtjänsten. Du ger ditt samtycke genom att kryssa i rutan längst ner på sidan när du läst igenom texten. Därefter kan du börja använda Mina intyg direkt.</p> <h3>Om Mina intyg</h3> <p>Mina intyg är en webbtjänst där du enkelt kan hantera intyg som vården utfärdat för dig eller den du är ombud för. Du kan läsa, spara ner på din dator och skriva ut dina intyg och du kan skicka intyg vidare till Försäkringskassan och till andra mottagare.</p> <h3>Personuppgifter från patientjournalen</h3> <p>Intygen som du hanterar i Mina intyg är utfärdade av vården och uppgifterna är en del av din patientjournal. När du ger ditt samtycke godkänner du:</p> <ul> <li>att dina personuppgifter i intygen får hanteras och visas i Mina intyg.</li> <li>att dina personuppgifter i intygen får hanteras för statistiska ändamål.</li> </ul> <h3>Personuppgifter som hanteras i Mina intyg</h3> <p>De personuppgifter som hanteras i Mina intyg är de uppgifter som finns i intyget. Det är: namn, personnummer, adress, diagnos, sjukdomsförlopp, funktionsnedsättning, aktivitetsbegränsning, rekommendationer och planerad eller pågående behandling, arbetsförmåga, prognos och vårdbesök.</p> <h3>Syfte med hanteringen av personuppgifter</h3> <p>Personuppgifterna finns i Mina intyg som en service till invånaren. Du ska enkelt kunna hantera dina intyg och välja när och till vem du vill skicka ett intyg. Uppgifterna kommer också att användas för att ta fram statistik om sjukskrivningar. Statistiken som tas fram går inte att härleda till dig eller någon viss person.</p> <h3>Aktiviteter i intygstjänsten</h3> <p>I Mina intyg hanteras och visas dina intyg. All aktivitet som sker, styr du själv över, som till exempel att överföra intyg till Försäkringskassan.</p> <h3>Ansvarig</h3> <p>Intygstjänsten drivs av Inera AB. I intygstjänsten behandlar Inera AB personuppgifter om enskilda personer i egenskap av personuppgiftsbiträde på uppdrag av vården.</p> <h3>Dina rättigheter</h3> <p>Som användare av intygstjänsten har du bland annat rätt att:</p> <ul> <li>varje år begära kostnadsfri information om personuppgifter som Inera AB behandlar</li> <li>när som helst begära rättelse av uppgifter</li> <li>när som helst begära spärrning eller radering av personuppgifter förutsatt att det finns godtagbara skäl, att uppgifterna uppenbarligen inte behövs för din vård eller att det ur allmän synpunkt uppenbarligen inte finns skäl att bevara uppgifterna</li> <li>kräva skadestånd enligt 10 kap. 1 § PDL eller 48 § PUL vid behandling av personuppgifter i strid med lag.</li> </ul> <p>Du har också rätt att begära att Inera AB underrättar tredje man, till vilken uppgifter lämnas ut, om uppgifternas felaktighet samt att få information om sökbegrepp, direktåtkomst och utlämnande av dina uppgifter som förekommit.</p> <h3>Återta samtycke</h3> <p>Du kan när som helst återkalla ditt samtycke. Det innebär att uppgifter om dig inte längre kommer att visas eller på annat sätt behandlas i Mina intyg. När du återkallar ditt samtycke kan du inte längre använda Mina intyg. Du kan när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.</p>",
		"about.juridikhead": "Juridik",
	    "about.juridiktext": "<h3>Lagring av personuppgifter i intygstjänsten</h3> <p>Din vårdgivare är skyldig att föra patientjournal enligt patientdatalagen. Då intygen är en del av din journal så kommer personuppgifterna att finnas lagrade i intygstjänsten lika länge som de finns i din patientjournal. Uppgifterna sparas i minst 10 år, ibland hela livet. Den här personuppgiftsbehandlingen är inte frivillig. Det betyder att intygen kommer att samlas i databasen även om du motsätter dig sådan lagring.</p> <h3>Sekretess</h3> <p>Personuppgifter inom hälso- och sjukvården lyder under sekretess. Det gäller för uppgifter om ditt hälsotillstånd eller andra personliga förhållanden - om det inte är tydligt att uppgiften kan röjas utan att du eller någon närstående till dig lider men. Endast den som behöver informationen för att kunna vårda dig, får ta del av den. Sekretess kan ibland även gälla för uppgifter om ditt eget hälsotillstånd i förhållande till dig själv, om det med hänsyn till ändamålet med vården eller behandlingen är av synnerlig vikt att uppgiften inte lämnas till dig.</p> <p>Sekretessen hindrar inte att uppgift lämnas:</p> <ul> <li>till en enskild vårdgivare enligt vad som föreskrivs om sammanhållen journalföring i patientdatalagen</li> <li>till ett nationellt eller regionalt kvalitetsregister enligt patientdatalagen</li> <li>i vissa fall till andra myndigheter</li> <li>av lag tvingande skäl.</li> </ul> <h3>Lagar som styr hantering av personuppgifter</h3> <p>Personuppgiftslagen (PUL) och Patientdatalagen (PDL) är lagar som reglerar behandling av personuppgifter.</p> <p><a href=\"http://www.vardguiden.se/Sa-funkar-det/Lagar--rattigheter/Lagar-i-halso--och-sjukvard/Sa-hanteras-dina-personuppgifter/\" target=\"_blank\">Läs mer om hantering av personuppgifter</a></p>",
	    "about.revokeconsentheader": "Återkalla samtycke",
	    "about.revokeconsenttext": "Du har gett ditt samtycke till att Mina intyg får hantera dina intyg elektroniskt. Du kan när som helst återkalla ditt samtycke. Det innebär att uppgifter om dig inte längre kommer att visas eller på annat sätt behandlas i Mina intyg. När du återkallar ditt samtycke kan du inte längre använda tjänsten. Du kan när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.",
	    "about.revokemodal.header": "Du har angett att du vill ta tillbaka samtycke",
	    "about.revokemodal.text": "Går du vidare innebär det att du inte kommer att kunna använda tjänsten förrän du har gett ditt samtycke igen. Vill du fortsätta?",

        "help.header" : "Hjälp",
        "help.description" : "<p>Vid problem, frågor eller synpunkter vänder du dig till den tekniska supporten för Mina vårdkontakter.</p><p>Har du problem med inloggning, glöm inte ange inloggningssätt (e-legitimation eller engångskod) samt tekniska specifikationer såsom vilken webbläsare du använder dig av.</p><p>OBS! Mina vårdkontakter besvarar inga medicinska frågor, detta är endast teknisk support för systemet Mina intyg eller Mina vårdkontakter.</p>",
        "help.phone" : "Telefon",
        "help.phone.description" : "<p>08-123 135 00. Öppet alla dagar 8-21.</p>",
        "help.email" : "E-post",
        "help.email.description" : "<p>Mina intyg använder sig av Mina vårdkontakters support:</p><p><a href='https://www.minavardkontakter.se/C125755F00329208/p/VGDO-7PSG9V' target='_blank'>Till Mina vårdkontakters kontaktformulär</a></p>",

        "listtable.headers.issued" : "Utfärdat",
        "listtable.headers.type" : "Typ",
        "listtable.headers.certperiod" : "Intygsperiod",
        "listtable.headers.issuedby" : "Utfärdat av",
        "listtable.headers.latestevent" : "Senaste händelsen"
    },
    "en" : {
        "label.showall" : "Show all"
        
    }
};
