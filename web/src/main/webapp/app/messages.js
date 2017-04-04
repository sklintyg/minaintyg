/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/* jshint maxlen: false */
angular.module('minaintyg').constant('minaintyg.messages', {
    'sv': {
        'label.showall': 'Visa alla',
        'label.showfewer': 'Visa färre',
        'label.status.false': 'Visa alla händelser',
        'label.status.true': 'Visa färre händelser',
        'label.restore': 'Återställ',
        'label.certificatesloading': 'Dina intyg laddas. Vänligen vänta...',
        'label.archivedcertificatesloading': 'Arkiverade intyg laddas. Vänligen vänta... ',

        'button.archive': 'Arkivera',
        'button.archive.tooltip': 'Flytta intyget till Arkiverade intyg',
        'button.send': 'Skicka',
        'button.show': 'Visa intyget',

        'modal.tekniskt-fel.header': 'Tekniskt fel',
        'modal.button.tekniskt-fel.ok': 'OK',

        /* CONSENT */

        'consent.consentpage.title': 'Ditt samtycke',
        'consent.info': 'Innan du kan använda tjänsten Mina intyg måste du ge ditt samtycke till att dina intyg får hanteras och visas där. Läs igenom texten och ge ditt samtycke längst ner på denna sida. Sedan kan du direkt börja använda Mina intyg.',
        'consent.aboutheader': 'Om tjänsten Mina intyg',
        'consent.abouttext': 'Mina intyg är en webbtjänst där du enkelt kan hantera olika intyg som hälso- och sjukvården utfärdat till dig. Du kan läsa intygen, spara ner dem på din dator och skriva ut dem. Du kan även välja att skicka intygen vidare till de mottagare som är anslutna till Mina intyg, till exempel Försäkringskassan och Transportstyrelsen. Det är du själv som styr över allt som händer med intygen. All information är skyddad (krypterad) och uppfyller vårdens krav på säkerhet och sekretess.',

        'consent.consentheader1': '<h3>Personuppgifter i Mina intyg</h3>',
        'consent.consenttext1': '<p>Din vårdgivare är enligt patientdatalagen skyldig att föra patientjournal. De intyg som vårdgivaren utfärdar innehåller uppgifter från patientjournalen. När du ger ditt samtycke kommer din vårdgivare att lämna ut kopior av dina intyg till Mina intyg och du kan direkt börja hantera dina intyg i tjänsten. Mina intyg hanterar endast personuppgifter som finns i intygen.</p><p>Personuppgifterna finns i Mina intyg för att underlätta för dig. Du ska enkelt kunna hantera dina intyg och välja när och till vem du vill skicka dem.</p>',

        'consent.consentheader2': '<h3>Ansvarig för Mina intyg</h3>',
        'consent.consenttext2': '<p>Tjänsten Mina intyg drivs av <a href="http://www.inera.se/" target="_blank">Inera AB</a> som är personuppgiftsansvarig för den behandling av personuppgifter om enskilda personer som sker i tjänsten. </p><p>Postadress: Box 17703, 118 93 Stockholm<br>Organisationsnummer: 556559-4230</p><p>Om du har frågor eller har problem med att använda Mina intyg kan du kontakta <a href="http://www.inera.se/felanmalan" target="_blank">Nationell kundservice</a>.</p>',

        'consent.consentheader3': '<h3>Dina rättigheter</h3>',
        'consent.consenttext3': '<p>Som användare av tjänsten Mina intyg har du bland annat rätt att:</p><ul><li>varje år begära kostnadsfri information om hur dina personuppgifter behandlas av Inera AB</li><li>när som helst begära att dina uppgifter rättas, blockeras eller utplånas om sådana uppgifter är felaktiga eller inte har behandlats i enlighet med personuppgiftslagen (detta hindrar dock inte din vårdgivare från att fortsätta behandla uppgifterna i sina system med stöd av patientdatalagen)</li><li>begära att tredje man till vilka personuppgifter lämnats ut underrättas om personuppgifter som rättats, blockerats eller utplånats. Någon sådan underrättelse behöver dock inte lämnas om detta visar sig vara omöjligt eller skulle innebära en oproportionerligt hög arbetsinsats.</li><li>kräva skadestånd enligt <a href="http://www.riksdagen.se/sv/Dokument-Lagar/Lagar/Svenskforfattningssamling/Personuppgiftslag-1998204_sfs-1998-204/?bet=1998:204" target="_blank">48 § PUL</a> vid behandling av personuppgifter i strid med lag.</li></ul></p>',

        'consent.consentheader4': '',
        'consent.consenttext4': '',

        'consent.consentheader5': '',
        'consent.consenttext5': '',

        'consent.consentheader6': '<h3>Ge och återta samtycke</h3>',
        'consent.consenttext6': '<p>När du ger ditt samtycke godkänner du att personuppgifterna som finns i intygen får lagras i en lagringstjänst kopplad till Mina intyg samt hanteras och visas i Mina intyg.</p><p>Du kan när som helst återta ditt samtycke. Det innebär att de kopior av dina intyg som vården lämnat ut till Mina intyg kommer att raderas, uppgifterna om dig kommer inte längre att visas eller på annat sätt behandlas i Mina intyg. När du återtar ditt samtycke kan du inte längre använda Mina intyg. Däremot kan du när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.</p>',

        'consent.confirmtext': 'Jag har läst och förstått ovanstående och lämnar mitt samtycke till hantering av mina personuppgifter i Mina intyg.',
        'consent.giveconsent': 'Jag ger mitt samtycke',
        'consent.continue': 'Fortsätt',
        'consent.givenhead': 'Du har nu gett samtycke - välj hur du vill fortsätta',
        'consent.giventext': '<h2>Kom du ifrån annan aktör än 1177 Vårdguiden? (tex Försäkringskassan)</h2> <p>Du kan nu börja bifoga intyg hos din aktör och <strong>stänga detta fönster.</strong></p> <p><span style="font-style:italic">Exempel:</span> För att bifoga ditt intyg till ett ärende i Försäkringskassans Mina sidor behöver du endast ge ditt samtycke, vilket du gjorde på föregående sida. Därför behöver du inte gå vidare till tjänsten Mina intyg utan kan stänga detta fönster.</p> <h2>Kom du till Mina intyg ifrån 1177 Vårdguiden?</h2> <p>Om du kom till Mina intyg från 1177 Vårdguiden, <strong>välj fortsätt</strong>.</p>',

        /* NAVIGATION */

        'nav.label.inbox': 'Inkorgen',
        'nav.label.archived': 'Arkiverade intyg',
        'nav.label.aboutminaintyg': 'Om Mina intyg',
        'nav.label.help': 'Hjälp och support',
        'nav.label.loggedinas': 'Du är inloggad som',

        /* INBOX */

        'inbox.header': 'Inkorgen',

        'inbox.description.1': 'Här listas alla dina intyg med det senast inkomna intyget överst. Läkarintyg i Mina intyg är kopior från din patientjournal och har lämnats ut till dig från din vårdgivare. För att hantera intyget väljer du <i>Visa intyget</i>, då kan du:',
        'inbox.description.2': '<ul><li>Läsa intyget</li><li>Ladda ner intyget som en PDF</li><li>Skriva ut intyget</li><li>Skicka intyget elektroniskt till olika mottagare</li><li>Anpassa intyget till din arbetsgivare (endast sjukpenningintyg)</li></ul>',
        'inbox.description.3': 'Du kan inte ta bort enstaka intyg från Mina intyg, men du kan flytta dina gamla intyg till Arkiverade intyg. Ett arkiverat intyg kan alltid flyttas tillbaka till Inkorgen. Läs mer om arkivering av intyg under fliken Arkiverade intyg.',

        'inbox.tooltip.archive': '<b>Att arkivera intyg</b><br/>Ett läkarintyg innehåller information som hämtas från patientjournalen. Det innebär bland annat att du inte helt kan ta bort ditt intyg från Mina intyg. Däremot kan du flytta dina gamla intyg till Arkiverade intyg. Ett arkiverat intyg kan alltid flyttas tillbaka till inkorgen. Läs mer om arkivering av intyg under fliken <a href="#">Arkiverade intyg</a>.',

        'inbox.revoked': 'Intyget är makulerat av din vårdgivare. Det går därför inte att visa eller hantera. Kontakta din vårdgivare för mer information.',
        'inbox.revoked.archive': 'Vill du inte längre se detta intyg i Inkorgen kan du arkivera det.',
        'inbox.revoked.helptext': 'Vårdgivaren kan makulera ett intyg för att det är felaktigt. Vid makuleringen meddelas de som har tagit emot intyget att det inte längre är giltigt. Du kan se ett makulerat intyg i inkorgen, men det är "utgråat" och du kan inte öppna eller titta på intyget. Ett makulerat intyg går inte heller att skriva ut eller skicka. Om du har begärt rättelse av ett felaktigt intyg hos din vårdgivare så visas det i Mina intyg som ett makulerat intyg.',

        'inbox.archivemodal.header': 'Arkivera intyg',
        'inbox.archivemodal.text': 'När du väljer att arkivera intyget kommer det att flyttas till <i>Arkiverade intyg</i>.<br><br> Du kan när som helst återställa intyget igen.',

        'inbox.title.helptext.arkivera': 'om att arkivera intyg',
        'inbox.title.helptext.makulera': 'om makulerat intyg',

        'inbox.helptext.arkivera': '<p><b>Att arkivera intyg</b></p>Läkarintyg i Mina intyg är kopior från din patientjournal och har lämnats ut till dig från din vårdgivare. Du kan inte ta bort enstaka intyg från Mina intyg, men du kan flytta dina gamla intyg till Arkiverade intyg. Ett arkiverat intyg kan alltid flyttas tillbaka till Inkorgen. Läs mer om arkivering av intyg under fliken Arkiverade intyg.',

        /* ARCHIVED */

        'archived.header': 'Arkiverade intyg',

        'archived.description': '<p>Ett läkarintyg i Mina intyg innehåller information som lämnats ut från din patientjournal. Du kan inte helt ta bort ditt intyg från tjänsten. Däremot kan du flytta dina gamla intyg hit till <i>Arkiverade intyg</i>.</p><p>Om du väljer att återkalla ditt samtycke kommer dina intyg att tas bort från Mina intyg.</p><p>Oavsett om du arkiverar ett intyg eller om du återkallar ditt samtycke, är din vårdgivare enligt lag alltid skyldig att lagra uppgifterna i din patientjournal i minst 10 år. Ofta lagras de längre, i vissa fall livet ut. Denna lagring sker inte i Mina intyg.</p><p>Om någon uppgift i ditt intyg är fel ska du vända dig till din vårdgivare. De kan då rätta uppgiften i patientjournalen och göra ett nytt och korrekt intyg tillgängligt i Mina intyg.</p><p><a href="http://www.datainspektionen.se/lagar-och-regler/patientdatalagen" target="_blank">Läs mer om lagring av uppgifter och ändring av information hos Datainspektionen</a>.</p><p>Du kan flytta tillbaka ett arkiverat intyg till inkorgen genom att klicka på <i>Återställ</i>.</p>',

        'archived.restoremodal.header': 'Återställ intyg',
        'archived.restoremodal.text': 'När du väljer att återställa intyget kommer det att flyttas till <i>Inkorgen</i>.<br><br>Du kan när som helst arkivera intyget igen.',

        /* SEND */
        'sendpage.label.select-recipients.heading': 'Välj mottagare du vill skicka intyget till.',
        'sendpage.label.none-selected': 'Du har inte valt någon mottagare att skicka intyget till.',
        'sendpage.label.some-selected': 'Du har valt att skicka intyget till följande mottagare:',
        'sendpage.btn.send': 'Skicka intyg',
        'sendpage.btn.clear': 'Rensa val',
        'sendpage.btn.remove-one': 'Ta bort',
        'sendpage.btn.select': 'Välj',

        'sendpage.label.loading': 'Laddar...',
        'sendpage.link.goback': 'Tillbaka',
        'sendpage.dialog.label.sending': 'Dina intyg skickas nu!<br>Det kan ta några sekunder.',
        'sendpage.dialog.label.received-by': 'Mottaget av',
        'sendpage.dialog.label.not-received-by': 'Ej mottaget av',
        'sendpage.dialog.label.somefailed': 'Intyget kunde inte tas emot av alla mottagare på grund av ett tekniskt fel. Försök igen senare. Om det fortfarande inte fungerar, kontakta <a href="http://www.inera.se/felanmalan" target="_blank">Ineras nationella kundservice</a>.',
        'sendpage.dialog.label.nonefailed': 'Intyget är nu inskickat och mottaget av:',
        'sendpage.dialog.btn.back-to-intyg': 'Tillbaka till intyget',



        /* ABOUT */

        'about.header': 'Om Mina intyg',

        'about.omminaintyghead': 'Om Mina intyg',
        'about.omminaintygtext': '<p>Mina intyg är en säker webbtjänst där du kan hantera dina läkarintyg.</p><p>I Mina intyg kan du läsa, skriva ut och spara dina intyg och du kan skicka intyg till Försäkringskassan och Transportstyrelsen. Det enda du behöver är en e-legitimation för att kunna logga in och använda tjänsten.</p> <p>Tjänsten nås från <a href="http://www.1177.se/e-tjanster" target="_blank">1177.se/e-tjanster</a>. Det går även att nå tjänsten från Försäkringskassans Mina sidor.</p><h3>Så använder du Mina intyg</h3><p>Första gången du loggar in i Mina intyg måste du ge ditt samtycke till att dina personuppgifter hanteras i tjänsten. Därefter kan du börja använda Mina intyg omedelbart. Du kan bara använda tjänsten för din egen person. Tjänsten är tillgänglig dygnet runt.</p><h3>Säkerhet</h3><p>All informationsöverföring är skyddad (krypterad) och Mina intyg uppfyller vårdens krav på säkerhet och sekretess. Det kostar inget att använda tjänsten och du kan hantera dina intyg när och var det passar dig.</p><h3>Ansvarig för Mina intyg</h3><p>Tjänsten Mina intyg drivs av <a href="http://www.inera.se/" target="_blank">Inera AB</a> som är personuppgiftsansvarig för den behandling av personuppgifter om enskilda personer som sker i tjänsten.</p><p>Postadress: Box 17703, 118 93 Stockholm<br/>Organisationsnummer: 556559-4230</p><p>Om du har frågor eller har problem med att använda Mina intyg kan du kontakta <a href="http://www.inera.se/felanmalan" target="_blank">Nationell kundservice</a>.</p><h3>Tekniska krav och tillgänglighet</h3><p>Mina intyg följer 1177 Vårdguidens riktlinjer för e-tjänster.</p><p>Information om tekniska krav för att använda Mina intyg, tillgänglighet, support och information om andra e-tjänster finns på <a href="http://www.1177.se/e-tjanster" target="_blank">1177.se/e-tjanster</a>.</p><h3>Försäkringskassans Mina sidor</h3><p>Mina sidor är en tjänst från Försäkringskassans där du kan bifoga ditt intyg i samband med att du gör din ansökan om exempelvis sjukpenning. För frågor och mer information om den processen hänvisar vi till <a href="http://www.forsakringskassan.se" target="_blank">Försäkringskassan</a>.</p> <h3>Bakgrund</h3><p>Under 2011 infördes e-tjänsten Läkarintyg i Sverige. Det innebär att läkaren kan skicka läkarintyg elektroniskt från journalsystemet till Försäkringskassan. Webbtjänsten Mina intyg är en utveckling av e-tjänsten Läkarintyg, som gör det möjligt för invånaren att själv hantera sina intyg och få inflytande över vilken mottagare som ska få tillgång till intygen.</p><p>Idag går det att skicka sina läkarintyg till Försäkringskassan och Transportstyrelsen. Framöver blir det även möjligt att skicka läkarintyg till andra mottagare, som till exempel försäkringsbolag och fler myndigheter.</p>',

        'about.consentheader': 'Samtycke',

        'about.revokeconsentheader': 'Återta samtycke',
        'about.revokeconsent.button.label': 'Återta samtycke',
        'about.revokeconsenttext': '<p>Du kan när som helst återta ditt samtycke.</p><p>Om du återtar ditt samtycke upphör Inera att behandla dina personuppgifter, inklusive intyg, i Mina intyg. Om du därefter lämnar ett nytt samtycke kan du återigen se dina intyg.</p><p>Även om du lämnar ett nytt samtycke kan det i vissa fall finnas intyg som inte går att visa i Mina intyg igen. Det beror på att den vårdgivare som har utfärdat intygen upphört med sin verksamhet, eller slutat att använda tjänsten av annan anledning, under den tid som du har återtagit ditt samtycke. De intyg som inte längre kan visas i Mina intyg finns alltid hos vårdgivaren eller hos den vårdgivare eller myndighet som övertagit verksamheten.</p> <p>Om du vill behålla intygen så kan du ladda ner intygen på din dator innan du återtar ditt samtycke. Gå till inkorgen och välj "Visa intyget", klicka sen på "Spara som PDF".<p>Om du inte återtar ditt samtycke finns dina intyg kvar i tjänsten.</p>',
        'about.revokemodal.header': 'Du har angett att du vill återta ditt samtycke',
        'about.revokemodal.text': 'När du återtar samtycke innebär det att du inte längre kan använda tjänsten. Du kan när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.',

        'about.juridikhead': 'Lagring av personuppgifter, sekretess och lagar',
        'about.juridiktext': '<h3>Lagring av personuppgifter</h3> <p>Din vårdgivare är skyldig att föra patientjournal enligt patientdatalagen. Eftersom läkarintygen är en del av din journal kommer dina personuppgifter att finnas lagrade lika länge som de finns i din patientjournal. Uppgifterna sparas i minst 10 år. Den här personuppgiftsbehandlingen är inte frivillig. Det betyder att personuppgifterna och intygen kommer att lagras även om du motsätter dig det.</p> <p>Alla elektroniska läkarintyg lagras i en särskild databas. Därifrån hämtar webbtjänsten Mina intyg sin information. För att personuppgifterna i intygen ska få hanteras i tjänsten Mina intyg krävs att du samtycker till det.</p> <h3>Skillnaden mellan hantering och lagring</h3> <p>Din vårdgivare är skyldig att <u>lagra</u> information från patientjournalen. Däremot får de uppgifter som finns i intygen endast <u>hanteras</u> av behöriga personer, det vill säga hälso- och sjukvårdspersonal som deltar i vården av dig eller behöver uppgifterna för att utföra sina arbetsuppgifter. Det kan vara läkarsekreterare eller sjuksköterska, du själv eller den du väljer ska få tillgång till intyget, till exempel Försäkringskassan.</p> <h3>Sekretess</h3> <p>Personuppgifter inom hälso- och sjukvården lyder under sekretess. Det innebär att uppgifter om ditt hälsotillstånd eller andra personliga förhållanden inte får röjas – om det inte är tydligt att de kan röjas utan att du eller någon närstående till dig lider men. Det är bara den som behöver informationen för att kunna vårda dig som får ta del av den.</p> <p>Sekretess kan ibland även gälla för uppgifter om ditt hälsotillstånd i förhållande till dig själv, om det på grund av ändamålet med vården är av synnerlig vikt att uppgiften inte lämnas till dig.</p> <p>Sekretessen hindrar inte att uppgifter lämnas</p> <ul> <li>till en enskild vårdgivare enligt vad som föreskrivs om <a href="http://www.1177.se/Regler-och-rattigheter/Patientjournalen/#section-3" target="_blank">sammanhållen journalföring i patientdatalagen</a></li> <li>till ett nationellt eller regionalt kvalitetsregister enligt patientdatalagen</li> <li>till andra myndigheter i vissa fall</li> <li>om det finns lagstiftning som kräver det.</li> </ul> <h3>Radering av information i din patientjournal</h3> <p>Om du vill att hela eller delar av din patientjournal ska raderas kan du begära detta hos <a href="http://www.ivo.se/Sidor/default.aspx" target="_blank">Inspektionen för vård och omsorg, IVO</a>. För att IVO ska besluta att radera en journaluppgift eller en hel patientjournal krävs det att du har godtagbara skäl för det, att det är uppenbart att uppgifterna inte behövs för din vård samt att det ur allmän synpunkt är uppenbart att det inte finns skäl att bevara journalen.</p> <p>Ett exempel på skäl att bevara journalen är att uppgifterna ligger till grund för olika beslut och åtgärder hos andra myndigheter eller att de behövs för forskning eller statistik.</p> <h3>Lagar som styr hantering av personuppgifter</h3> <p><a href="http://www.riksdagen.se/sv/Dokument-Lagar/Lagar/Svenskforfattningssamling/Personuppgiftslag-1998204_sfs-1998-204/?bet=1998:204" target="_blank">Personuppgiftslagen (PUL)</a> och <a href="http://www.riksdagen.se/sv/Dokument-Lagar/Lagar/Svenskforfattningssamling/Patientdatalag-2008355_sfs-2008-355/?bet=2008:355" target="_blank">Patientdatalagen (PDL)</a> är de lagar som reglerar hur personuppgifter behandlas.</p> <p><a href="http://www.1177.se/Tema/E-tjanster/Om-1177-Vardguidens-e-tjanster/Sa-skyddas-och-hanteras-dina-uppgifter-i-e-tjansterna/" target="_blank">Mer information om hantering av personuppgifter hos 1177 Vårdguiden</a>.</p> <p><a href="http://www.1177.se/Regler-och-rattigheter/Patientjournalen/" target="_blank">Mer information om patientjournalen hos 1177.se</a>.</p><h3>Kakor (Cookies)</h3><p>Så kallade kakor (cookies) används för att underlätta för besökaren på webbplatsen. En kaka är en textfil som lagras på din dator och som innehåller information. Denna webbplats använder så kallade sessionskakor. Sessionskakor lagras temporärt i din dators minne under tiden du är inne på en webbsida. Sessionskakor försvinner när du stänger din webbläsare. Ingen personlig information om dig sparas vid användning av sessionskakor. Om du inte accepterar användandet av kakor kan du stänga av det via din webbläsares säkerhetsinställningar. Du kan även ställa in webbläsaren så att du får en varning varje gång webbplatsen försöker sätta en kaka på din dator.</p><p><strong>Observera!</strong> Om du stänger av kakor i din webbläsare kan du inte logga in i Mina intyg.</p><p>Allmän information om kakor (cookies) och lagen om elektronisk kommunikation finns på Post- och telestyrelsens webbplats.</p> <p><a href="https://www.pts.se/sv/Privat/Internet/Skydd-av-uppgifter/Fragor-och-svar-om-kakor-for-anvandare1/" target="_blank">Mer om kakor (cookies) på Post- och telestyrelsens webbplats</a></p>',

        'about.cookies': '<h3>Kakor (Cookies)</h3><p>Så kallade kakor (cookies) används för att underlätta för besökaren på webbplatsen. En kaka är en textfil som lagras på din dator och som innehåller information. Denna webbplats använder så kallade sessionskakor. Sessionskakor lagras temporärt i din dators minne under tiden du är inne på en webbsida. Sessionskakor försvinner när du stänger din webbläsare. Ingen personlig information om dig sparas vid användning av sessionskakor. Om du inte accepterar användandet av kakor kan du stänga av det via din webbläsares säkerhetsinställningar. Du kan även ställa in webbläsaren så att du får en varning varje gång webbplatsen försöker sätta en kaka på din dator. </p><p><strong>Observera!</strong> Om du stänger av kakor i din webbläsare kan du inte logga in i Mina Intyg.</p><p>Allmän information om kakor (cookies) och lagen om elektronisk kommunikation finns på Post- och telestyrelsens webbplats.</p> <p><a href="https://www.pts.se/sv/Privat/Internet/Skydd-av-uppgifter/Fragor-och-svar-om-kakor-for-anvandare1/" target="_blank">Mer om kakor (cookies) på Post- och telestyrelsens webbplats</a></p>',

        /* HELP */

        'help.header': 'Hjälp och support',
        'help.helpdescriptionhead': 'Hjälp och support',
        'help.description1': 'Har du några frågor, synpunkter eller problem med att använda Mina intyg kan du kontakta Ineras nationella kundservice. Vanliga frågor om Mina intyg finns besvarade i ',
        'help.description2': '<p>Om du inte hittar svar på dina frågor kan du kontakta kundservice via telefon. För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.</p><p>Ineras nationella kundservice nås på telefonnummer: 0771-25 10 10 alla vardagar klockan 7-22. Om ditt ärende inte är akut kan du också att fylla i formuläret på <a href="http://www.inera.se/felanmalan" target="_blank">Nationell kundservice</a> webbplats.</p><p>Det är bra om du kan beskriva ditt problem så noga som möjligt. Det kan till exempel vara hur du har loggat in i tjänsten och vilken webbläsare du använder.</p><p>Observera att detta endast är teknisk support för Mina intyg. Om ditt ärende gäller andra tjänster hos exempelvis 1177 Vårdguiden eller Försäkringskassan så ska du vända dig direkt till deras support.</p>',
        'help.helpfaqhead': 'Vanliga frågor och svar',
        'help.faq.header': 'Vanliga frågor och svar',
        'help.faq.description': '<p>Här kan du läsa vanliga frågor och svar om tjänsten Mina intyg.<br/>Om du inte hittar svar på din fråga kan du ringa Ineras Nationella kundservice på telefonnummer: 0771-25 10 10 alla vardagar klockan 7-22. Om ditt ärende inte är akut kan du också att fylla i formuläret på <a href="http://www.inera.se/felanmalan" target="_blank">Nationell kundservice webbplats</a>.</p><p>För medicinska frågor som rör ditt intyg ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.</p>',

        'help.faq':[
            {title:'',questions:[
                {question: 'Varför kan jag inte se mitt intyg?', answer: 'I Mina intyg visas läkarintyg som är utfärdade efter 2013-11-21. Landsting och regioner ansluter sina journalsystem successivt till att skriva elektroniska läkarintyg, vilket kan innebära att det inte finns några läkarintyg att visa än.'},
                {question:'Jag har laddat ner mitt intyg på min dator men kan inte öppna filen.',answer:'Intyget laddas ner som en PDF-fil. PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna PDF-filer behöver du en PDF-läsare exempelvis. <a href="http://get.adobe.com/se/reader/" target="_blank" title="Ladda ner Adobe Reader">Adobe Reader</a>.'},
                {question:'Vad betyder det när intyget är ersatt?', answer: '<p>Vårdgivaren kan ersätta ett intyg om till exempel intyget innehåller felaktig information, ny information tillkommit eller att försäkringskassan begärt en komplettering och vården svarat med ett nytt intyg med de kompletterande uppgifterna.</p><p>På ett ersatt intyg framgår vilken som är den nya versionen av intyget. Ett ersatt intyg går inte att skicka eller spara som PDF.</p>'},
                {question:'Varför kan jag inte se makulerade intyg?',answer:'<p>Vårdgivaren kan makulera ett intyg för att det innehåller ett allvarligt fel, till exempel om det är skrivit på fel patient.</p><p>Om en vårdgivare makulerar ett intyg, är det inte längre tillgängliga i Mina intyg.</p>'}]},
            {title:'Frågor och svar om samtycke',questions:[
                {question:'Varför måste jag ge samtycke?',answer:'För att kunna använda tjänsten Mina intyg måste du först lämna ditt samtycke till att dina intyg får lagras i lagringstjänst kopplad till Mina intyg samt visas och hanteras i Mina intyg. Utan ditt samtycke till att dina personuppgifter i intygen hanteras i Mina intyg kan tjänsten inte användas.'},
                {question:'Vad innebär samtycke?',answer:'Samtycke är en frivillig, otvetydig och särskild viljeyttring genom vilken du godkänner den personuppgiftsbehandling som sker genom att dina intyg hanteras i Mina intyg. Samtycket innebär att intyg som utfärdats inom hälso- och sjukvården kan synliggöras och hanteras i Mina intyg. I Mina intyg kan du själv läsa, spara ner på din dator och skriva ut dina intyg samt skicka intyg vidare till exempelvis Försäkringskassan.'},
                {question:'Vad samtycker jag till?',answer:'Genom att samtycka till behandlingen av personuppgifter i Mina intyg godkänner du att dina personuppgifter som finns i intygen lagras i lagringstjänst kopplad till Mina intyg, hanteras och visas i Mina intyg. Personuppgifterna som hanteras är:<ul><li>namn</li><li>personnummer</li><li>adress</li><li>diagnos</li><li>sjukdomsförlopp</li><li>funktionsnedsättning</li><li>aktivitetsbegränsning</li><li>rekommendationer</li><li>planerad eller pågående behandling</li><li>arbetsförmåga</li><li>prognos</li><li>vårdbesök</li></ul>'},
                {question:'Kan jag ta tillbaka mitt samtycke?',answer:'Du kan när som helst återta ditt samtycke. Det innebär att uppgifterna om dig inte längre kommer att visas eller på annat sätt behandlas i Mina intyg. Om du återtar ditt samtycke kan du inte längre använda Mina intyg. Däremot kan du när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.'},
                {question:'Var registreras mitt samtycke?',answer:'Inera AB förvaltar Mina intyg och registrerar ditt samtycke.'},
                {question:'Hur registreras samtycket?',answer:'Första gången du loggar in till Mina intyg måste du lämna ditt samtycke för att kunna använda tjänsten. Genom att trycka på knappen "Jag ger mitt samtycke" registreras ditt samtycke och du kan börja använda tjänsten. Samtycket fortsätter gälla fram till dess att du återkallar det.'},
                {question:'Kan andra parter få ta del av min information via mitt samtycke, utan att jag känner till det?',answer:'Någon annan kan inte få del av uppgifterna i dina intyg i och med att du lämnar ditt samtycke. Det är du själv som styr över den aktivitet som sker i Mina intyg. Ditt samtycke innebär endast att uppgifterna får hanteras och visas i tjänsten, men inte att intyg får skickas vidare eller tillgängliggöras för andra utan din vetskap.'}
            ]},
            {title:'Frågor och svar om säkerhet',questions:[
                {question:'Är det säkert att använda Mina intyg?',answer:'Ja, Mina intyg har en hög säkerhetsnivå och uppfyller Datainspektionens säkerhetskrav. Du kan bara logga in med e-legitimation (e-legitimation kan också heta bank-id – det är en typ av e-legitimation), E-legitimation är ett säkert sätt att logga in på.'},
                {question:'Hur skyddas mina uppgifter?',answer:'All data i Mina intyg lagras på ett säkert sätt. Hanteringen av uppgifter i Mina intyg följer de krav som ställs i Personuppgiftslagen (PuL), Patientdatalagen (PDL) och av Datainspektionen.'},
                {question:'Kan andra komma åt mina uppgifter?',answer:'Nej, i och med att du bara kan logga in med e-legitimation, vars inloggning baseras på ditt personnummer, kan endast information om dig som loggar in visas.'},
                {question:'Varför kan jag inte logga in med lösenord och sms?',answer:'Lösenord och SMS uppfyller inte de krav som gäller för hantering av medicinsk information, som till exempel kan finnas i ett läkarintyg.'},
                {question:'Om jag loggat in med e-legitimation på Mina intyg, måste jag logga in med e-legitimation på nytt om jag vill gå in på Försäkringskassans Mina sidor?',answer:'Ja för tillfället behöver du göra det. Detta kan dock komma att ändras framöver.'},
                {question:'Hur skickas informationen från journalsystemet till Mina intyg?',answer:'Informationen skickas på ett säkert sätt via Hälso- och Sjukvårdens Nationella tjänsteplattform.<p>Så kan du skydda din dator:<ol><li>Håll din dator uppdaterad. Ditt operativsystem och din webbläsare bör alltid ha de senaste säkerhetsuppdateringarna installerade. Se också till att du alltid använder senaste versionen av säkerhetsprogrammet för din e-legitimation.</li><li>Använd ett uppdaterat antivirusprogram. Vi rekommenderar att du använder ett uppdaterat antivirusprogram som skyddar din dator från skadlig kod och automatiskt kontrollerar de filer som kommer till din dator.</li><li>Logga ut när du är klar. Avsluta varje besök i Mina intyg med att logga ut och stänga webbläsaren.</li></ol></p>'}
            ]}
        ],

        'listtable.headers.issued': 'Inkom till Mina intyg',
        'listtable.headers.type': 'Typ av intyg',
        'listtable.headers.certinfo': 'Intygsinformation',
        'listtable.headers.issuedby': 'Enhet',
        'listtable.headers.latestevent': 'Senaste händelser',

        'fkdialog.head': 'Du har gett ditt samtycke',
        'fkdialog.text': 'Du har gett ditt samtycke till att Försäkringskassan får hämta dina intyg från tjänsten Mina intyg. Nu kan du välja att logga ut och återgå till Försäkringskassans Mina sidor, eller gå vidare till tjänsten Mina intyg.',
        'fkdialog.button.returntofk': 'Tillbaka till Försäkringskassan',
        'fkdialog.button.continueuse': 'Gå vidare till Mina intyg',

        'mvk.header.linktext': '1177 Vårdguiden',
        'mvk.header.logouttext': 'Logga ut',

        'common.close': 'Stäng',
        'common.ok': 'OK',
        'common.yes': 'Ja',
        'common.no': 'Nej',
        'common.cancel': 'Avbryt',
        'common.goback': 'Tillbaka',
        'common.nodontask': 'Nej, och fråga inte igen',

        'common.title.helptext.moreinfo': 'Mer information',
        'common.title.helptext.lessinfo': 'Mindre information',

        'common.module.message.sendingcertificate': 'Skickar intyg...',

        'certificates.status.cancelled': 'Intyget är makulerat',
        'certificates.status.cancelled.title': 'Makulerat',
        'certificates.status.received': 'Mottagits av {0}',
        'certificates.status.sent': 'Skickat till {0}',
        'certificates.status.noevents': 'Inga händelser',
        'certificates.status.unknowndatetime': 'Okänd tid',
        'certificates.status.statusesshown': '(Visar {0} av {1})',

        'certificates.target.fk': 'Försäkringskassan',
        'certificates.target.ts': 'Transportstyrelsen',

        'certificates.send.label.patientname': 'Patientens namn:',
        'certificates.send.label.issued': 'Utfärdat:',
        'certificates.send.label.civicnumber': 'Personnummer:',
        'certificates.send.label.issuer': 'Utfärdare',
        'certificates.send.label.period': 'Period:',
        'certificates.send.label.unit': 'Enhet:',

        'error.pagetitle': 'Tekniskt fel',
        'error.couldnotloadcertlist': '<p>Intygen i Inkorgen kunde inte visas. Om felet kvarstår kan du kontakta Nationell kundservice.<br><b>Telefonnummer: 0771-25 10 10</b><br></p><p>Om du inte kan komma åt intyget på Mina intyg just nu så kan du kontakta din läkare för att få en kopia.<br><br><a href="/web/start">Gå till Inkorgen och försök igen</a>.</p>',
        'error.couldnotarchivecert': '<p>Intyget kunde inte flyttas från Inkorgen till Arkiverade intyg.</p><p><ul><li><a href="/web/start">Gå till Inkorgen och försök igen</a></li><li>Kontakta <a href="/web/start/#/hjalp">support</a></li></ul></p>',
        'error.couldnotrestorecert': '<p>Intyget kunde inte flyttas från Arkiverade intyg till Inkorgen.</p><p><ul><li><a href="/web/start/#/arkiverade">Gå till Arkiverade intyg och försök igen</a></li><li>Kontakta <a href="/web/start/#/hjalp">support</a></li></ul></p>',
        'error.couldnotrevokeconsent': '<p>Det gick inte att återta ditt samtycke för tillfället. Vänta några minuter och försök igen.</p><p><ul><li><a href="/web/start/#/omminaintyg">Gå till Om mina sidor och försök igen</a></li><li><a href="/web/start/#/hjalp">Kontakta support</a></li></ul></p>',
        'error.couldnotgiveconsent': '<p>Ditt samtycke kunde inte registreras för tillfället. Det innebär att du inte kan börja använda tjänsten Mina intyg just nu. Vänta några minuter och försök att ge ditt samtycke igen.</p><p><ul><li><a href="/web/visa-ge-samtycke#/start">Försök igen</a></li><li>Om felet kvarstår kan du kontakta support på telefon 0771-25 10 10</li><li>Om du inte kan nå ditt intyg i Mina intyg så kan du kontakta din läkare för att få en kopia av intyget.</li></ul></p>',
        'error.generic': '<p>Tekniskt fel</p>Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>',
        'error.certnotfound': 'Intyget kunde inta hämtas. Om felet kvarstår kan du kontakta <a href="/web/start/#/hjalp">support</a>.',

        'error.nocerts': 'Du har inga intyg i inkorgen.',
        'error.noarchivedcerts': 'Du har inga arkiverade intyg.'
    },
    'en': {
        'consent.consentpage.title': 'Consent of use',
        'label.showall': 'Show all',
        'label.inbox': 'The INBOX',
        'label.archived': 'Archived'
    }
});
