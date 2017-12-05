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

        'label.event.false': 'Visa alla händelser',
        'label.event.true': 'Visa färre händelser',

        'button.archive': 'Arkivera intyg',
        'button.archive.tooltip': 'Flyttar intyget till Arkiverade intyg.',
        'button.modal.archive.tooltip': 'Flytta intyget till Arkiverade intyg.',
        'button.show': 'Visa intyg',
        'button.show.tooltip': 'Läs och hantera ditt intyg.',
        'button.goback': 'Tillbaka',

        /* CONSENT */

        'consent.info': 'Innan du kan använda tjänsten Mina intyg måste du ge ditt samtycke till att dina intyg får hanteras och visas där. Läs igenom texten och ge ditt samtycke längst ner på denna sida. Sedan kan du direkt börja använda Mina intyg.',
        'consent.aboutheader': 'Om tjänsten Mina intyg',
        'consent.abouttext': 'Mina intyg är en webbtjänst där du enkelt kan hantera olika intyg som hälso- och sjukvården utfärdat till dig. Du kan läsa intygen, spara ner dem på din dator och skriva ut dem. Du kan även välja att skicka intygen vidare till de mottagare som är anslutna till Mina intyg, till exempel Försäkringskassan och Transportstyrelsen. Det är du själv som styr över allt som händer med intygen. All information är skyddad (krypterad) och uppfyller vårdens krav på säkerhet och sekretess.',


        'consent.confirmtext': 'Jag har läst och förstått ovanstående och lämnar mitt samtycke till hantering av mina personuppgifter i Mina intyg.',
        'consent.giveconsent': 'Jag ger mitt samtycke',
        'consent.continue': 'Fortsätt',
        'consent.givenhead': 'Du har nu gett samtycke - välj hur du vill fortsätta',
        'consent.giventext': '<h2>Kom du ifrån annan aktör än 1177 Vårdguiden? (tex Försäkringskassan)</h2> <p>Du kan nu börja bifoga intyg hos din aktör och <strong>stänga detta fönster.</strong></p> <p><span style="font-style:italic">Exempel:</span> För att bifoga ditt intyg till ett ärende i Försäkringskassans Mina sidor behöver du endast ge ditt samtycke, vilket du gjorde på föregående sida. Därför behöver du inte gå vidare till tjänsten Mina intyg utan kan stänga detta fönster.</p> <h2>Kom du till Mina intyg ifrån 1177 Vårdguiden?</h2> <p>Om du kom till Mina intyg från 1177 Vårdguiden, <strong>välj fortsätt</strong>.</p>',

        /* NAVIGATION */

        'nav.label.inbox': 'Inkorgen',
        'nav.label.archived': 'Arkiverade intyg',
        'nav.label.aboutminaintyg': 'Om Mina intyg',
        'nav.label.loggedinas': 'Du är inloggad som:',

        'user.label.sekrettessmarkering': 'Du har en sekretessmarkering',
        'user.sekretessmarkeringmodal.header': 'Vad innebär sekretessmarkering?',
        'user.sekretessmarkeringmodal.button': '<i class="icon icon-ok"></i> Ok, jag förstår',

        'send.sekretessmarkeringmodal.header': 'Observera!',
        'send.sekretessmarkeringmodal.body': '<p>Mina intyg kan aldrig garantera hur de tillgängliga mottagarna för ett intyg hanterar sekretessmarkerade personuppgifter eller om de följer Skatteverkets vägledning för hur sekretessmarkerade personuppgifter ska hanteras. Myndigheter ska ha särskilda rutiner för hantering av sekretessmarkerade personuppgifter.</p><p>Vill du veta mer om hur en specifik mottagare hanterar inkomna handlingar för dig med sekretessmarkering, hänvisar vi till den aktuella mottagaren</p>',
        'send.sekretessmarkeringmodal.button1': '<i class="icon icon-ok"></i> Skicka',
        'send.sekretessmarkeringmodal.button2': '<i class="icon icon-cancel"></i> Avbryt',

        'pdf.sekretessmarkeringmodal.header': 'Ladda ner intyg som PDF',
        'pdf.sekretessmarkeringmodal.body': '<p>OBS! Tänk på att ditt intyg innehåller personuppgifter om dig.</p><p>När du laddar ner ditt intyg som PDF kommer det att sparas till den dator/enhet du använder. Om du till exempel använder en offentlig dator kan det vara bra att radera det nerladdade intyget innan du lämnar datorn/enheten.</p>',
        'pdf.sekretessmarkeringmodal.button1': '<i class="icon icon-download-1"></i> Ladda ner intyg som PDF',
        'pdf.sekretessmarkeringmodal.button2': '<i class="icon icon-cancel"></i> Avbryt',

        /* INBOX */
        'inbox.label.certificatesloading': 'Dina intyg laddas. Vänligen vänta...',
        'inbox.header': 'Översikt över dina intyg',

        'inbox.description.1': 'Här listas alla dina intyg med det senast utfärdade intyget överst. Läkarintyg i Mina intyg är kopior från din patientjournal och har lämnats ut till dig från din vårdgivare. I Mina intyg kan du skicka dina intyg till olika mottagare, som t.ex. Försäkringskassan, anpassa dina intyg för arbetsgivaren eller ladda ner intygen som PDF. Klicka på knappen Visa intyg för att hantera dina intyg.',
        'inbox.description.archive.title': 'Arkivera intyg',
        'inbox.description.archive': '<p>Du kan inte ta bort enstaka intyg från Mina intyg, men du kan flytta dina gamla intyg till Arkiverade intyg. Ett arkiverat intyg kan alltid flyttas tillbaka till Inkorgen.</p><p><a href="#/arkiverade">Läs mer om arkivering av intyg under fliken Arkiverade intyg</a>.</p>',

        'inbox.tooltip.archive': '<b>Att arkivera intyg</b><br/>Ett läkarintyg innehåller information som hämtas från patientjournalen. Det innebär bland annat att du inte helt kan ta bort ditt intyg från Mina intyg. Däremot kan du flytta dina gamla intyg till Arkiverade intyg. Ett arkiverat intyg kan alltid flyttas tillbaka till inkorgen. Läs mer om arkivering av intyg under fliken <a href="#">Arkiverade intyg</a>.',

        'inbox.archivemodal.header': 'Arkivera intyg',
        'inbox.archivemodal.text': 'När du väljer att arkivera intyget kommer det att flyttas till <i>Arkiverade intyg</i>.<br>Du kan när som helst återställa intyget igen.',

        'inbox.helptext.arkivera': '<p><b>Att arkivera intyg</b></p>Läkarintyg i Mina intyg är kopior från din patientjournal och har lämnats ut till dig från din vårdgivare. Du kan inte ta bort enstaka intyg från Mina intyg, men du kan flytta dina gamla intyg till Arkiverade intyg. Ett arkiverat intyg kan alltid flyttas tillbaka till Inkorgen. Läs mer om arkivering av intyg under fliken Arkiverade intyg.',
        'inbox.message.no-more-certificates': 'Inga fler intyg',
        'inbox.message.nocerts': 'Du har inga intyg.',
        'inbox.list.issuer.label': 'Utfärdare',

        /* ARCHIVED */
        'archived.label.archivedcertificatesloading': 'Arkiverade intyg laddas. Vänligen vänta... ',
        'archived.header': 'Arkiverade intyg',


        'archived.description': '<p>Ett läkarintyg i Mina intyg innehåller information som lämnats ut från din patientjournal. Du kan inte ta bort ditt intyg från Mina intyg, men du kan flytta dina gamla intyg hit till <i>Arkiverade intyg</i>.</p><p>Om du väljer att ta bort ditt samtycke för att använda Mina intyg, kommer din kopia av intyget att raderas. Oavsett om du arkiverar ett intyg eller om du tar bort ditt samtycke, är din vårdgivare enligt lag alltid skyldig att spara uppgifterna i din patientjournal i minst 10 år.</p>',
        'archived.description.part2': '<p><LINK:diPatientDataLagen>.</p><p>Om någon uppgift i ditt intyg är fel eller du har medicinska frågor angående intyget, ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.</p><p>Ett arkiverat intyg kan flyttas tillbaka till inkorgen. Det gör du genom att klicka på Återställ intyg.</p>',
        'archived.message.nocerts': 'Du har inga arkiverade intyg.',

        'archived-cert-table.headers.issued': 'Intyg utfärdat',
        'archived-cert-table.headers.type': 'Typ av intyg',
        'archived-cert-table.headers.complementary-info': 'Intyg avser',
        'archived-cert-table.headers.issuedby': 'Intygsutfärdare',
        'btn-link.restore': 'Återställ intyg',
        'btn-link.restore.tooltip': 'Flytta tillbaka intyget till Inkorgen.',

        /* SEND */
        'sendpage.label.single-recipient.heading': 'Klicka på knappen om du vill skicka ditt intyg till nedanstående mottagare:',
        'sendpage.label.single-recipient.sent.heading': 'Intyget har redan skickats till nedanstående mottagare.',
        'sendpage.label.select-recipients.heading': 'Välj mottagare du vill skicka intyget till:',
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
        'sendpage.dialog.label.somefailed.title': 'Tekniskt fel',
        'sendpage.dialog.label.somefailed.body': 'Intyget kunde inte tas emot av alla mottagare på grund av ett tekniskt fel. Försök igen om några minuter.<br>Om felet kvarstår kontakta Ineras kundservice. Ring 0771-25 10 10 eller besök <LINK:ineraKontakt>',
        'sendpage.dialog.label.nonefailed.title': 'Intyget skickat',
        'sendpage.dialog.label.nonefailed.body': 'Intyget är nu inskickat och mottaget av:',
        'sendpage.dialog.btn.back-to-intyg': 'Tillbaka till intyget',

        'info.loggedout.fk.title': 'Du är utloggad ur Mina intyg',
        'info.loggedout.fk.text': 'Stäng detta webbfönster för att fortsätta till Försäkringskassans Mina sidor.',

        /* ABOUT */

        'about.revokeconsent.button.label': 'Återta samtycke',
        'about.revokemodal.header': 'Du har angett att du vill återta ditt samtycke',
        'about.revokemodal.text': 'När du återtar samtycke innebär det att du inte längre kan använda tjänsten. Du kan när som helst aktivera tjänsten igen genom att lämna ett nytt samtycke.',

        'about.dina.rattigheter': '<h3>Dina rättigheter</h3><p> Som användare av tjänsten Mina intyg har du bland annat rätt att: <ul> <li>varje år begära kostnadsfri information om hur dina personuppgifter behandlas av Inera AB.</li> <li>när som helst begära att dina uppgifter rättas, blockeras eller utplånas om sådana uppgifter är felaktiga eller inte har behandlats i enlighet med personuppgiftslagen (detta hindrar dock inte din vårdgivare från att fortsätta behandla uppgifterna i sina system  med stöd av patientdatalagen).</li><li>begära att tredje man till vilka personuppgifter lämnats ut underrättas om personuppgifter som rättats, blockerats eller utplånats. Någon sådan underrättelse behöver dock inte lämnas om detta visar sig vara omöjligt eller skulle innebära en oproportionerligt hög arbetsinsats.</li><li>kräva skadestånd enligt <LINK:pul48> vid behandling av personuppgifter i strid med lag.</li></ul></p>',
        'about.ge.aterta.samtycke': '<h3>Ge och återta samtycke</h3><p>När du ger ditt samtycke godkänner du att personuppgifterna som finns i intygen får lagras i en lagringstjänst kopplad till Mina intyg samt hanteras och visas i Mina intyg.</p><p>Du kan när som helst återta samtycket. Det innebär att de kopior av dina intyg som vården lämnat ut till Mina intyg    kommer att raderas, uppgifterna om dig kommer inte längre att visas eller på annat sätt behandlas i Mina intyg. När du återtar ditt samtycke kan du inte längre använda Mina intyg. Däremot kan du när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.</p>',
        'about.samtycke.om.tjansten': '<h3>Om tjänsten Mina Intyg</h3><p>Mina intyg är en webbtjänst där du enkelt kan hantera intyg utfärdade av hälso- och sjukvården. Det är du själv som styr över allt som händer med intygen. All information är skyddad (krypterad) och uppfyller vårdens krav på säkerhet och sekretess.</p>',

        /* HELP */

        // FAQ
        'faq.intyg.1.title' : 'Varför kan jag inte se mitt intyg?',
        'faq.intyg.1.body' : 'I Mina intyg visas läkarintyg som är utfärdade efter 2013-11-21. Landsting och regioner ansluter sina journalsystem successivt till att skriva elektroniska läkarintyg, vilket kan innebära att det inte finns några läkarintyg att visa än.',
        'faq.intyg.2.title' : 'Jag har laddat ner mitt intyg på min dator men kan inte öppna filen.',
        'faq.intyg.2.body' : 'Intyget laddas ner som en PDF-fil. PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna PDF-filer behöver du en PDF-läsare, exempelvis <LINK:adobeReader>.',
        'faq.intyg.3.title' : 'Vad betyder det när intyget är ersatt?',
        'faq.intyg.3.body' : 'Vårdgivaren kan ersätta ett intyg om till exempel intyget innehåller felaktig information, ny information tillkommit eller att Försäkringskassan har begärt en komplettering och vården har svarat med ett nytt intyg med de kompletterande uppgifterna. På ett ersatt intyg framgår vilken som är den nya versionen av intyget. Ett ersatt intyg går inte att skicka eller spara som PDF.',
        'faq.intyg.4.title' : 'Varför kan jag inte se makulerade intyg?',
        'faq.intyg.4.body' : 'Vårdgivaren kan makulera ett intyg för att det innehåller ett allvarligt fel, till exempel om det är skrivit på fel patient. Om en vårdgivare makulerar ett intyg är det inte tillgängligt i Mina intyg.',

        'faq.samtycke.1.title' : 'Varför måste jag ge samtycke?',
        'faq.samtycke.1.body': 'För att kunna använda Mina intyg måste du först lämna ditt samtycke till att dina intyg får lagras i en lagringstjänst kopplad till Mina intyg, samt visas och hanteras i Mina intyg. Utan ditt samtycke till att dina personuppgifter i intygen hanteras i Mina intyg kan tjänsten inte användas.',
        'faq.samtycke.2.title' : 'Vad innebär samtycke?',
        'faq.samtycke.2.body': 'Läs mer om samtycke till att använda Mina intyg <a href="#/omminaintyg/samtycke">här.</a>',
        'faq.samtycke.3.title' : 'Kan jag ta tillbaka mitt samtycke?',
        'faq.samtycke.3.body': 'Du kan när som helst återta ditt samtycke. Det innebär att uppgifterna om dig inte längre kommer att visas eller på annat sätt behandlas i Mina intyg. Om du återtar ditt samtycke kan du inte längre använda Mina intyg. Däremot kan du när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.',

        'faq.sakerhet.1.title' : 'Är det säkert att använda Mina intyg?',
        'faq.sakerhet.1.body': 'Ja, Mina intyg har en hög säkerhetsnivå och uppfyller Datainspektionens säkerhetskrav. Du kan bara logga in med e-legitimation (e-legitimation kan också heta bank-id – det är en typ av e-legitimation). E-legitimation är ett säkert sätt att logga in på.',
        'faq.sakerhet.2.title' : 'Hur skyddas mina uppgifter?',
        'faq.sakerhet.2.body': 'Hanteringen av uppgifter i Mina intyg följer de krav som ställs i Personuppgiftslagen (PuL), Patientdatalagen (PDL) och av Datainspektionen.',
        'faq.sakerhet.3.title' : 'Kan andra komma åt mina uppgifter?',
        'faq.sakerhet.3.body': 'Nej, i och med att du bara kan logga in med e-legitimation, vars inloggning baseras på ditt personnummer, kan endast information om dig som loggar in visas.',
        'faq.sakerhet.4.title' : 'Varför kan jag inte logga in med lösenord och sms?',
        'faq.sakerhet.4.body': 'Lösenord och SMS uppfyller inte de krav som gäller för hantering av medicinsk information, som till exempel kan finnas i ett läkarintyg.',
        'faq.sakerhet.5.title' : 'Är det säkert med e-legitimation?',
        'faq.sakerhet.5.body': 'Ja, en e-legitimation är ett säkert sätt att identifiera sig i Sverige, men glöm inte att även skydda din dator.',
        'faq.sakerhet.6.title' : 'Hur skyddar jag min dator?',
        'faq.sakerhet.6.body': '<ol><li>Håll din dator uppdaterad. Ditt operativsystem och din webbläsare bör alltid ha de senaste säkerhetsuppdateringarna installerade. Se också till att du alltid använder senaste versionen av säkerhetsprogrammet för din e-legitimation.</li><li>Använd ett uppdaterat antivirusprogram. Vi rekommenderar att du använder ett uppdaterat antivirusprogram som skyddar din dator från skadlig kod och automatiskt kontrollerar de filer som kommer till din dator.</li><li>Logga ut när du är klar. Avsluta varje besök i Mina intyg med att logga ut och stänga webbläsaren.</li></ol>',

        /* FOOTER */
        'footer.cookies.modal.title': 'Om kakor (cookies)',
        'footer.cookies.modal.body': '<p>En kaka är en textfil som webbsidan begär att få spara på din dator för att möjliggöra automatisk inloggning i olika e-tjänster under 60 minuter efter det du identifierat dig med en e-legitimation.</p><p>Den här webbsidan innehåller två så kallade kakor (cookies):</p><ul><li>En sessionskaka som innehåller ett unikt slumptal för att identifiera just din inloggning och försvinner när webbläsaren stängs av.</li><li>En informationskaka om vilken av servrarna på vår webbplats som hanterade din inloggning och den kakan finns kvar i webbläsaren även efter omstart.</li></ul><p>Dessa två kakor behövs för att på ett säkert sätt låta dig komma åt olika e-tjänster som en inloggad och identifierad användare. Kakorna i sig innehåller inga personliga uppgifter och används inte av någon annan.</p><h3>Undvika kakor</h3><p>Vill du inte acceptera kakor kan din webbläsare ställas in så att du automatiskt nekar till lagring av kakor eller informeras varje gång en webbplats begär att få lagra en kaka. Genom webbläsaren kan också tidigare lagrade kakor raderas. Se webbläsarens hjälpsidor för mer information.</p><p>Väljer du att inte acceptera kakor så kan du inte identifiera dig med e-legitimation i denna e-tjänst.</p><p>Mer information om kakor kan du finna på <LINK:ptsCookiesModal>.</p>',

        /* LINKS */
        'links.inera.kundservice': '<LINK:ineraKundserviceKontakt>',

        'fkdialog.head': 'Du har gett ditt samtycke',
        'fkdialog.text': 'Du har gett ditt samtycke till att Försäkringskassan får hämta dina intyg från tjänsten Mina intyg. Nu kan du välja att logga ut och återgå till Försäkringskassans Mina sidor, eller gå vidare till tjänsten Mina intyg.',
        'fkdialog.button.returntofk': 'Tillbaka till Försäkringskassan',
        'fkdialog.button.continueuse': 'Gå vidare till Mina intyg',

        'listtable.headers.latestevent': 'Senaste händelser',

        'mvk.header.linktext': 'E-tjänster',
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


        'certificates.label.replaced': 'ERSATT INTYG',

        // Errors in main app (boot-app.jsp)
        //'error.pagetitle': 'Tekniskt fel',
        'error.generictechproblem.title': 'Tekniskt fel',
        'error.couldnotloadcertlist': '<p>Intygen i inkorgen kunde inte visas på grund av ett tekniskt fel. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta <LINK:ineraKundserviceKontakt>.<br><br>Om du inte kan nå ditt intyg i Mina intyg, kontakta din läkare för att få en kopia av intyget.</p>',
        'error.couldnotloadarchivedlist': '<p>Dina arkiverade intyg kunde inte visas på grund av ett tekniskt fel. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta <LINK:ineraKundserviceKontakt>.</p>',
        'error.certnotfound': '<p>Intyget kunde inte visas på grund av ett tekniskt fel. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta <LINK:ineraKundserviceKontakt>.<br><br>Om du inte kan nå ditt intyg i Mina intyg, kontakta din läkare för att få en kopia av intyget.</p>',
        'info.certrevoked': '<p>Intyget är makulerat och kan därför inte visas.</p><a href="/web/start/#/inkorg">Gå till inkorgen</a>',
        'error.certarchived': '<p>Intyget är arkiverat och kan därför inte visas. Återställ intyget till inkorgen för att kunna läsa det.</p>',
        'error.modal.couldnotarchivecert': '<p>Intyget kunde inte flyttas från Inkorgen till Arkiverade intyg just nu. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta <LINK:ineraKundserviceKontakt>.</p>',
        'error.modal.couldnotrestorecert': '<p>Intyget kunde inte flyttas från Arkiverade intyg till Inkorgen just nu. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta <LINK:ineraKundserviceKontakt>.</p>',
        'error.modal.couldnotrevokeconsent': '<p>Det gick inte att återta ditt samtycke för tillfället. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta <LINK:ineraKundserviceKontakt></p>',
        'error.modal.couldnotgiveconsent': '<p>Ditt samtycke kunde inte registreras för tillfället. Det innebär att du inte kan börja använda tjänsten Mina intyg just nu. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta <LINK:ineraKundserviceKontakt>.<br><br>Om du inte kan nå ditt intyg i Mina intyg, kontakta din läkare för att få en kopia av intyget.</p>',
        'error.generic': '<p>Tekniskt fel</p><p>På grund av ett tekniskt fel går det inte att visa Mina intyg just nu. Försök igen om några minuter.</p><p>Om felet kvarstår kontakta <LINK:ineraKundserviceKontakt>.</p><p><a href="/web/start/#/inkorg">Gå till inkorgen</a></p>',
        'error.modal.btn.back-to-inkorg': 'Tillbaka till Inkorgen',
        'error.modal.btn.back-to-archive-cert': 'Tillbaka till Arkiverade intyg',
        'error.modal.btn.back-to-about': 'Tillbaka till Om Mina intyg',

        // Errors in error app (error.jsp)
        'info.loggedout.text': 'För att använda Mina intyg måste du logga in igen.',
        'error.noauth.text': '<p>Du har blivit utloggad från tjänsten på grund av inaktivitet, eller så försöker du nå en sida som kräver inloggning. Gå till minaintyg.se för att logga in.</p><p><a href="/">Gå till minaintyg.se</a></p>',
        'error.notfound-loggedin.text': '<p>Sidan du försöker nå finns inte.</p><p>Kontrollera om du stavat fel i webbläsaren eller gå till tillbaka till inkorgen.</p><p> <a href="/web/start/#/inkorg">Gå till inkorgen</a></p>',

        'error.generictechproblem.text': '<p>Tekniskt fel.</p><p>På grund av ett tekniskt fel går det inte att visa Mina intyg just nu. Försök igen om några minuter.</p><p>Om felet kvarstår kontakta <LINK:ineraKundserviceKontakt>.</p>',
        'error.login.failed.text': '<p>Tekniskt fel.</p><p>På grund av ett tekniskt fel går det inte att logga in just nu. Försök igen om några minuter.</p><p>Om felet kvarstår kontakta <LINK:ineraKundserviceKontakt>.<br><a href="/">Gå till minaintyg.se</a></p>'
    },
    'en': {
        'label.showall': 'Show all',
        'label.inbox': 'The INBOX',
        'label.archived': 'Archived'
    }
});
