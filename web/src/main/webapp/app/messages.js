/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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

    /* NAVIGATION */

    'nav.label.inbox': 'Inkorgen',
    'nav.label.archived': 'Arkiverade intyg',
    'nav.label.aboutminaintyg': 'Om Mina intyg',
    'nav.label.loggedinas': 'Du är inloggad som:',

    'user.label.sekrettessmarkering': 'Du har skyddade personuppgifter',
    'user.sekretessmarkeringmodal.header': 'Vad innebär skyddade personuppgifter?',
    'user.sekretessmarkeringmodal.button': '<i aria-hidden="true" class="icon icon-ok"></i> Ok, jag förstår',

    'send.sekretessmarkeringmodal.header': 'Observera!',
    'send.sekretessmarkeringmodal.body': '<p>Mina intyg kan aldrig garantera hur de tillgängliga mottagarna för ett intyg hanterar skyddade personuppgifter eller om de följer Skatteverkets vägledning för hur skyddade personuppgifter ska hanteras. Myndigheter ska ha särskilda rutiner för hantering av skyddade personuppgifter.</p><p>Vill du veta mer om hur en specifik mottagare hanterar inkomna handlingar för dig med skyddade personuppgifter, hänvisar vi till den aktuella mottagaren</p>',
    'send.sekretessmarkeringmodal.button1': '<i aria-hidden="true" class="icon icon-ok"></i> Skicka',
    'send.sekretessmarkeringmodal.button2': '<i aria-hidden="true" class="icon icon-cancel"></i> Avbryt',

    'pdf.sekretessmarkeringmodal.header': 'Ladda ner intyg som PDF',
    'pdf.sekretessmarkeringmodal.body': '<p>OBS! Tänk på att ditt intyg innehåller personuppgifter om dig.</p><p>När du laddar ner ditt intyg som PDF kommer det att sparas till den dator/enhet du använder. Om du till exempel använder en offentlig dator måste du själv se till att radera det nerladdade intyget innan du lämnar datorn/enheten.</p>',
    'pdf.sekretessmarkeringmodal.button1': '<i aria-hidden="true" class="icon icon-download-1"></i> Ladda ner intyg som PDF',
    'pdf.sekretessmarkeringmodal.button2': '<i aria-hidden="true" class="icon icon-cancel"></i> Avbryt',

    /* INBOX */
    'inbox.label.certificatesloading': 'Dina intyg laddas. Vänligen vänta...',
    'inbox.header': 'Översikt över dina intyg',

    'inbox.description.1': '<p>Här listas dina läkarintyg som vården utfärdat digitalt. Det senast utfärdade intyget visas överst.</p><p>Du kan skicka intyg digitalt till Försäkringskassan och Transportstyrelsen. Läkarintyg om arbetsförmåga kan inte skickas digitalt till din arbetsgivare, däremot kan du skriva ut intyget.</p><p>För att läsa ditt intyg klicka på knappen Visa intyg.</p>',
    'inbox.description.2': '<p>Du har inte tillgång till någon annan persons intyg. Det finns ingen ombudsfunktion i Mina intyg.</p><p>Intyg kan inte "laddas upp" eller tillföras av dig till Mina intyg. Saknar du ett intyg ska du kontakta vården.</p><p>Hittar du inte ditt intyg i inkorgen, alternativt i ditt arkiv, vänd dig till din läkare/vårdgivare.</p><p>Du hittar mer information i <a href="#/omminaintyg/faq">Vanliga frågor och svar</a>.</p>',

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

    'archived.description': '<p>Ett läkarintyg i Mina intyg innehåller information som lämnats ut från din patientjournal. Du kan inte ta bort ditt intyg från Mina intyg, men du kan flytta dina gamla intyg hit till <i>Arkiverade intyg</i>.</p><p><LINK:diPatientDataLagen>.</p>',
    'archived.description.part2': '<p>Om någon uppgift i ditt intyg är fel eller du har medicinska frågor angående intyget, ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.</p><p>Ett arkiverat intyg kan flyttas tillbaka till inkorgen. Det gör du genom att klicka på Återställ intyg.</p>',
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
    'sendpage.dialog.label.somefailed.body': 'Intyget kunde inte tas emot av alla mottagare just nu. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta Support. Ring 0770-72 00 00, klockan 06.00 - 22.00 alla dagar eller besök  <LINK:1177eServicesHelpAndSupport>',
    'sendpage.dialog.label.nonefailed.title': 'Intyget skickat',
    'sendpage.dialog.label.nonefailed.body': 'Intyget är nu inskickat och mottaget av:',
    'sendpage.dialog.btn.back-to-intyg': 'Tillbaka till intyget',

    'info.loggedout.fk.title': 'Du är utloggad ur Mina intyg',
    'info.loggedout.fk.text': 'Stäng detta webbfönster för att fortsätta till Försäkringskassans Mina sidor.',

    /* HELP */

    // FAQ
    'faq.intyg.1.title': 'Varför kan jag inte se mitt intyg?',
    'faq.intyg.1.body': '<p>Om din läkare har signerat ditt intyg digitalt ska det finnas i Mina intyg. Hittar du inte ditt intyg i inkorgen, alternativt i ditt arkiv, vänd dig till din läkare/vårdgivare.</p><p>I Mina intyg visas läkarintyg som är utfärdade efter 2013-11-21.</p>',
    'faq.intyg.2.title': 'Varför kan jag inte skicka mitt intyg?',
    'faq.intyg.2.body': '<p>En del intyg har ingen digital mottagare. Det gäller till exempel intyg till arbetsgivare. Dessa kan du istället skriva ut och ge till din arbetsgivare.</p><p>Det finns också intyg som skickas av vården direkt till mottagaren. Dessa är synliga i Mina intyg för att du ska kunna se att intyget är utfärdat och skickat.</p><p>Du har fått ett intyg från din läkare som inte går att skicka digitalt. Om du vill skicka intyget till en intygsmottagare behöver du skicka intyget med post.</p>',
    'faq.intyg.3.title': 'Kan jag göra någon ändring i intygen som riktar sig till min arbetsgivare?',
    'faq.intyg.3.body': '<p>De två nya intygen Läkarintyg om arbetsförmåga – arbetsgivaren och Läkarintyg om arbetsförmåga – sjuklöneperioden är framtagna för att säkerställa att arbetsgivarna får tillräcklig information för att kunna bevilja sjuklön och erbjuda rehabilitering. I dessa intyg kan information om din diagnos väljas bort innan du skriver ut och lämnar intyget till din arbetsgivare. All övrig intygsinformation är obligatorisk och kan därför inte väljas bort. Om du redan vid läkarbesöket sagt till din läkare att utelämna diagnos så är detta redan genomfört och funktionen i Mina intyg är borttagen. </p>' +
                        '<p>Observera att det tidigare gick att anpassa Försäkringskassans Läkarintyg för sjukpenningintyg för att ge till sin arbetsgivare. Denna möjlighet är numera borttagen.</p>',
    'faq.intyg.4.title': 'Jag har laddat ner mitt intyg på min enhet men kan inte öppna filen.',
    'faq.intyg.4.body': 'Intyget laddas ner som en PDF-fil. PDF är ett filformat som används för att ett dokument ska se likadant ut i olika enheter. För att kunna öppna PDF-filer behöver du en PDF-läsare, exempelvis <LINK:adobeReader>.',
    'faq.intyg.5.title': 'Vad betyder det när intyget är ersatt?',
    'faq.intyg.5.body': 'Vårdgivaren kan ersätta ett intyg om till exempel intyget innehåller felaktig information, ny information tillkommit eller att Försäkringskassan har begärt en komplettering och vården har svarat med ett nytt intyg med de kompletterande uppgifterna. På ett ersatt intyg framgår vilken som är den nya versionen av intyget. Ett ersatt intyg går inte att skicka eller spara som PDF.',
    'faq.intyg.6.title': 'Varför kan jag inte se makulerade intyg?',
    'faq.intyg.6.body': '<p>Vårdgivaren kan makulera ett intyg för att det innehåller ett allvarligt fel, till exempel om det är skrivit på fel patient.</p>Om en vårdgivare makulerar ett intyg är det inte tillgängligt i Mina intyg.',

    'faq.sakerhet.1.title': 'Är det säkert att använda Mina intyg?',
    'faq.sakerhet.1.body': 'Ja, Mina intyg har en hög säkerhetsnivå och uppfyller Integritetsskyddsmyndighetens säkerhetskrav. Du kan bara logga in med e-legitimation (e-legitimation kan också heta bank-id – det är en typ av e-legitimation). E-legitimation är ett säkert sätt att logga in på.',
    'faq.sakerhet.2.title': 'Hur skyddas mina uppgifter?',
    'faq.sakerhet.2.body': 'Hanteringen av uppgifter i Mina intyg följer de krav som ställs i Dataskyddsförordningen (GDPR), Patientdatalagen (PDL) och av Integritetsskyddsmyndigheten.',
    'faq.sakerhet.3.title': 'Kan andra komma åt mina uppgifter?',
    'faq.sakerhet.3.body': '<p>Nej, i och med att du loggar in med din personliga e-legitimation, kan endast information om dig visas.</p>' +
                           '<p>OBS! Tänk på att ditt intyg innehåller personuppgifter om dig.</p>' +
                           '<p>När du laddar ner ditt intyg som PDF kommer det att sparas till den dator/enhet du använder. Om du till exempel använder en offentlig dator måste du själv se till att radera det nerladdade intyget innan du lämnar datorn/enheten.</p>',
    'faq.sakerhet.4.title': 'Varför kan jag inte logga in med lösenord och sms?',
    'faq.sakerhet.4.body': 'Lösenord och SMS uppfyller inte de krav som gäller för hantering av medicinsk information, som till exempel kan finnas i ett läkarintyg.',
    'faq.sakerhet.5.title': 'Är det säkert med e-legitimation?',
    'faq.sakerhet.5.body': 'Ja, en e-legitimation är ett säkert sätt att identifiera sig i Sverige, men glöm inte att även skydda din enhet.',
    'faq.sakerhet.6.title': 'Hur skyddar jag min enhet?',
    'faq.sakerhet.6.body': '<ol><li>Håll din enhet uppdaterad. Ditt operativsystem och din webbläsare bör alltid ha de senaste säkerhetsuppdateringarna installerade. Se också till att du alltid använder senaste versionen av säkerhetsprogrammet för din e-legitimation.</li><li>Använd ett uppdaterat antivirusprogram. Vi rekommenderar att du använder ett uppdaterat antivirusprogram som skyddar från skadlig kod och automatiskt kontrollerar de filer tas emot av din enhet eller som du laddar ner.</li><li>Logga ut när du är klar. Avsluta varje besök i Mina intyg med att logga ut och stänga webbläsaren.</li></ol>',

    /* FOOTER */
    'footer.cookies.modal.title': 'Om kakor (cookies)',
    'footer.cookies.modal.body': '<p>Vi använder kakor (cookies) för att den här webbplatsen ska fungera på ett bra sätt för dig. Genom att logga in accepterar du vår användning av kakor.</p><h3>Så här använder vi kakor</h3><p>Den typ av kakor som används på den här webbplatsen kallas för sessionskakor. De lagras temporärt i din dators minne under tiden du är inne på webbplatsen. Sessionskakor sparar ingen personlig information om dig, och de försvinner när du stänger din webbläsare.</p><p>I Mina intyg används sessionskakor för att du ska kunna navigera i tjänsten utan att behöva logga in på nytt varje gång du går till en ny sida. De används också för att de filterinställningar du gör ska finnas kvar under hela tiden du är inloggad. För att vara säker på att kakorna inte sparas i din dator efter avslutad session måste du stänga webbläsaren när du har loggat ut.</p><h3>Undvika kakor</h3><p>Vill du inte acceptera kakor kan din webbläsare ställas in så att du automatiskt nekar till lagring av kakor eller informeras varje gång en webbplats begär att få lagra en kaka. Genom webbläsaren kan också tidigare lagrade kakor raderas. Se webbläsarens hjälpsidor för mer information.</p><p>Väljer du att inte acceptera kakor så kan du inte identifiera dig med e-legitimation i denna e-tjänst.</p><p>Mer information om kakor kan du finna på <LINK:ptsCookiesModal>.</p>',

    /* LINKS */
    'links.inera.kundservice': '<LINK:ineraKundserviceKontakt>',

    'listtable.headers.latestevent': 'Senaste händelser',

    'mvk.header.linktext': 'E-tjänster',
    'mvk.header.logouttext': 'Logga ut',

    'common.close': 'Stäng',
    'common.ok': 'OK',
    'common.yes': 'Ja',
    'common.no': 'Nej',
    'common.cancel': 'Avbryt',
    'common.goback': 'Tillbaka',
    'common.iaccept': 'Jag godkänner',
    'common.nodontask': 'Nej, och fråga inte igen',

    'common.title.helptext.moreinfo': 'Mer information',
    'common.title.helptext.lessinfo': 'Mindre information',

    'certificates.label.replaced': 'ERSATT INTYG',
    'certificates.label.completed': 'KOMPLETTERAT INTYG',
    'certificates.label.dash': '-',

    /* USER SURVEY */

    'mi.survey.modal.text': 'Vi genomför en användarundersökning för tjänsten Mina Intyg och vore mycket tacksamma om du kan tänka dig att dela dina synpunkter med oss. Undersökningen tar ett par minuter att fylla i och är helt anonym. Du kan närsomhelst avbryta undersökningen om så önskas. \n' +
        'Vill du hjälpa oss att förbättra tjänsten genom ditt deltagande?',
    'mi.survey.modal.title': 'Användarundersökning',

    // Errors in main app (boot-app.jsp)
    //'error.pagetitle': 'Tekniskt fel',
    'error.generictechproblem.title': 'Tekniskt fel',
    'error.couldnotloadcertlist': '<p>Intygen i inkorgen kunde inte visas på grund av ett tekniskt fel. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta <LINK:1177eServicesSupport>.<br><br>Om du inte kan nå ditt intyg i Mina intyg, kontakta din läkare för att få en kopia av intyget.</p>',
    'error.couldnotloadarchivedlist': '<p>Dina arkiverade intyg kunde inte visas på grund av ett tekniskt fel. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta <LINK:1177eServicesSupport>.</p>',
    'error.certnotfound': '<p>Intyget kunde inte visas på grund av ett tekniskt fel. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta <LINK:1177eServicesSupport> och ange intygs-id: {intygsId}.<br><br>Om du inte kan nå ditt intyg i Mina intyg, kontakta din läkare för att få en kopia av intyget.</p>',
    'info.certrevoked': '<p>Intyget är makulerat och kan därför inte visas.</p><a href="/web/start/#/inkorg">Gå till inkorgen</a>',
    'error.certarchived': '<p>Intyget är arkiverat och kan därför inte visas. Återställ intyget till inkorgen för att kunna läsa det.</p>',
    'error.modal.couldnotarchivecert': '<p>Intyget kunde inte flyttas från Inkorgen till Arkiverade intyg just nu. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta <LINK:1177eServicesSupport> och ange intygs-id: {intygsId}.</p>',
    'error.modal.couldnotrestorecert': '<p>Intyget kunde inte flyttas från Arkiverade intyg till Inkorgen just nu. Försök igen om några minuter.<br><br>Om felet kvarstår kontakta <LINK:1177eServicesSupport> och ange intygs-id: {intygsId}.</p>',
    'error.generic': '<p>Tekniskt fel</p><p>På grund av ett tekniskt fel går det inte att visa Mina intyg just nu. Försök igen om några minuter.</p><p>Om felet kvarstår kontakta <LINK:ineraKundserviceKontakt>.</p><p><a href="/web/start/#/inkorg">Gå till inkorgen</a></p>',
    'error.modal.btn.back-to-inkorg': 'Tillbaka till Inkorgen',
    'error.modal.btn.back-to-archive-cert': 'Tillbaka till Arkiverade intyg',
    'error.modal.btn.back-to-about': 'Tillbaka till Om Mina intyg',

    // Errors in error app (error.jsp)
    'info.loggedout.header': 'Utloggad',
    'info.loggedout.text1': 'Din webbläsare kan hålla kvar information även efter utloggning.',
    'info.loggedout.text2': 'Du bör därför stänga samtliga öppna webbläsarfönster.',
    'info.loggedout.text3': 'För att logga in igen, klicka på nedanstående knapp.',
    'error.noauth.text': '<p>Du har blivit utloggad från tjänsten på grund av inaktivitet, eller så försöker du nå en sida som kräver inloggning. Gå till minaintyg.se för att logga in.</p><p><a href="/">Gå till minaintyg.se</a></p>',
    'error.notfound-loggedin.text': '<p>Sidan du försöker nå finns inte.</p><p>Kontrollera om du stavat fel i webbläsaren eller gå till tillbaka till inkorgen.</p><p> <a href="/web/start/#/inkorg">Gå till inkorgen</a></p>',

    'error.generictechproblem.text': '<p>Tekniskt fel.</p><p>På grund av ett tekniskt fel går det inte att visa Mina intyg just nu. Försök igen om några minuter.</p><p>Om felet kvarstår kontakta <LINK:1177eServicesSupport>.</p>',
    'error.login.failed.text': '<p>Tekniskt fel.</p><p>På grund av ett tekniskt fel går det inte att logga in just nu. Försök igen om några minuter.</p><p>Om felet kvarstår kontakta <LINK:1177eServicesSupport>.<br><a href="/">Gå till minaintyg.se</a></p>'
  },
  'en': {
    'label.showall': 'Show all',
    'label.inbox': 'The INBOX',
    'label.archived': 'Archived'
  }
});
