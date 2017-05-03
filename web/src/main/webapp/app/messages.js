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

        'button.archive': 'Arkivera intyg',
        'button.archive.tooltip': 'Flyttar intyget till Arkiverade intyg.',
        'button.send': 'Skicka',
        'button.show': 'Visa intyg',
        'button.show.tooltip': 'Läs och hantera ditt intyg.',
        'button.goback': 'Tillbaka',

        'modal.tekniskt-fel.header': 'Tekniskt fel',
        'modal.button.tekniskt-fel.ok': 'OK',

        /* CONSENT */

        'consent.consentpage.title': 'Samtycke',
        'consent.info': 'Innan du kan använda tjänsten Mina intyg måste du ge ditt samtycke till att dina intyg får hanteras och visas där. Läs igenom texten och ge ditt samtycke längst ner på denna sida. Sedan kan du direkt börja använda Mina intyg.',
        'consent.aboutheader': 'Om tjänsten Mina intyg',
        'consent.abouttext': 'Mina intyg är en webbtjänst där du enkelt kan hantera intyg utfärdade av hälso- och sjukvården. Det är du själv som styr över allt som händer med intygen. All information är skyddad (krypterad) och uppfyller vårdens krav på säkerhet och sekretess.',


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
        'nav.label.loggedinas': 'Du är inloggad som:',

        /* INBOX */

        'inbox.header': 'Översikt över dina intyg',

        'inbox.description.1': 'Här listas alla dina intyg med det senast utfärdade intyget överst. Läkarintyg i Mina intyg är kopior från din patientjournal och har lämnats ut till dig från din vårdgivare. I Mina intyg kan du skicka dina intyg till olika mottagare, som t.ex. Försäkringskassan, anpassa dina intyg för arbetsgivaren eller ladda ner intygen som PDF. Klicka på knappen Visa intyg för att hantera dina intyg.',
        'inbox.description.archive.title': 'Arkivera intyg',
        'inbox.description.archive': '<p>Du kan inte ta bort enstaka intyg från Mina intyg, men du kan flytta dina gamla intyg till Arkiverade intyg. Ett arkiverat intyg kan alltid flyttas tillbaka till Inkorgen.</p><p><a href="#/arkiverade">Läs mer om arkivering av intyg under fliken Arkiverade intyg</a>.</p>',

        'inbox.tooltip.archive': '<b>Att arkivera intyg</b><br/>Ett läkarintyg innehåller information som hämtas från patientjournalen. Det innebär bland annat att du inte helt kan ta bort ditt intyg från Mina intyg. Däremot kan du flytta dina gamla intyg till Arkiverade intyg. Ett arkiverat intyg kan alltid flyttas tillbaka till inkorgen. Läs mer om arkivering av intyg under fliken <a href="#">Arkiverade intyg</a>.',

        'inbox.archivemodal.header': 'Arkivera intyg',
        'inbox.archivemodal.text': 'När du väljer att arkivera intyget kommer det att flyttas till <i>Arkiverade intyg</i>.<br><br> Du kan när som helst återställa intyget igen.',

        'inbox.helptext.arkivera': '<p><b>Att arkivera intyg</b></p>Läkarintyg i Mina intyg är kopior från din patientjournal och har lämnats ut till dig från din vårdgivare. Du kan inte ta bort enstaka intyg från Mina intyg, men du kan flytta dina gamla intyg till Arkiverade intyg. Ett arkiverat intyg kan alltid flyttas tillbaka till Inkorgen. Läs mer om arkivering av intyg under fliken Arkiverade intyg.',
        'inbox.message.no-more-certificates': 'Inga fler intyg',
        'inbox.list.issuer.label': 'Utfärdare:',

        /* ARCHIVED */

        'archived.header': 'Arkiverade intyg',

        'archived.description': '<p>Ett läkarintyg i Mina intyg innehåller information som lämnats ut från din patientjournal. Du kan inte ta bort ditt intyg från Mina intyg, men du kan flytta dina gamla intyg hit till Arkiverade intyg.</p><p>Om du väljer att ta bort ditt samtycke för att använda Mina intyg, kommer din kopia av intyget att raderas. Oavsett om du arkiverar ett intyg eller om du tar bort ditt samtycke, är din vårdgivare enligt lag alltid skyldig att spara uppgifterna i din patientjournal i minst 10 år.</p>',
        'archived.description.part2': '<p><a href="http://www.datainspektionen.se/lagar-och-regler/patientdatalagen" target="_blank">Läs mer om lagring av uppgifter och ändring av information hos Datainspektionen</a>.</p><p>Om någon uppgift i ditt intyg är fel eller du har medicinska frågor angående intyget, ska du kontakta den som utfärdade ditt intyg, eller den mottagning du besökte när du fick ditt intyg utfärdat.</p><p>Ett arkiverat intyg kan flyttas tillbaka till inkorgen. Det gör du genom att klicka på Återställ.</p>',

        'archived-cert-table.headers.issued': 'Intyg utfärdat',
        'archived-cert-table.headers.type': 'Typ av intyg',
        'archived-cert-table.headers.complementary-info': 'Intyg avser',
        'archived-cert-table.headers.issuedby': 'Intygsutfärdare',
        'btn-link.restore': 'Återställ',
        'btn-link.restore.tooltip': 'Flytta intyget tillbaka till Inkorgen.',

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
        'sendpage.dialog.label.somefailed': 'Intyget kunde inte tas emot av alla mottagare på grund av ett tekniskt fel. Försök igen senare. Om det fortfarande inte fungerar, kontakta <a href="http://www.inera.se/kontakt" target="_blank">Ineras kundservice</a>.',
        'sendpage.dialog.label.nonefailed': 'Intyget är nu inskickat och mottaget av:',
        'sendpage.dialog.btn.back-to-intyg': 'Tillbaka till intyget',



        /* ABOUT */

        'about.revokeconsent.button.label': 'Återta samtycke',
        'about.revokemodal.header': 'Du har angett att du vill återta ditt samtycke',
        'about.revokemodal.text': 'När du återtar samtycke innebär det att du inte längre kan använda tjänsten. Du kan när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.',

        'about.dina.rattigheter': '<h3>Dina rättigheter</h3><p> Som användare av tjänsten Mina intyg har du bland annat rätt att: <ul> <li>Varje år begära kostnadsfri information om hur dina personuppgifter behandlas av Inera AB.</li> <li>När som helst begära att dina uppgifter rättas, blockeras eller utplånas om sådana uppgifter är felaktiga eller inte har behandlats i enlighet med personuppgiftslagen (detta hindrar dock inte din vårdgivare från att fortsätta behandla uppgifterna i sina system  med stöd av patientdatalagen).</li><li>Begära att tredje man, till vilka personuppgifter lämnats ut, underrättas om personuppgifter som rättats,  blockerats eller utplånats.</li><li>Kräva skadestånd enligt <a href="http://www.riksdagen.se/sv/dokument-lagar/dokument/svensk-forfattningssamling/personuppgiftslag-1998204_sfs-1998-204" target="_blank">48 § PUL</a> vid behandling av personuppgifter i strid med lag. </li> </ul> </p>',
        'about.ge.aterta.samtycke': '<h3>Ge och återta samtycke</h3><p>När du ger ditt samtycke godkänner du att personuppgifterna som finns i intygen får lagras i en lagringstjänst kopplad till Mina intyg samt hanteras och visas i Mina intyg.</p><p>Du kan när som helst återta samtycket. Det innebär att de kopior av dina intyg som vården lämnat ut till Mina intyg    kommer att raderas, uppgifterna om dig kommer inte längre att visas eller på annat sätt behandlas i Mina intyg. När du återtar ditt samtycke kan du inte längre använda Mina intyg. Däremot kan du när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.</p>',
        /* HELP */

        'help.faq':[
            {title:'',questions:[
                {question: 'Varför kan jag inte se mitt intyg?', answer: 'I Mina intyg visas läkarintyg som är utfärdade efter 2013-11-21. Landsting och regioner ansluter sina journalsystem successivt till att skriva elektroniska läkarintyg, vilket kan innebära att det inte finns några läkarintyg att visa än.'},
                {question:'Jag har laddat ner mitt intyg på min dator men kan inte öppna filen.',answer:'Intyget laddas ner som en PDF-fil. PDF är ett filformat som används för att ett dokument ska se likadant ut i olika datorer. För att kunna öppna PDF-filer behöver du en PDF-läsare, exempelvis <a href="https://get.adobe.com/reader/?loc=se" target="_blank" title="Ladda ner Adobe Reader">Adobe Reader</a>.'},
                {question:'Vad betyder det när intyget är ersatt?', answer: 'Vårdgivaren kan ersätta ett intyg om till exempel intyget innehåller felaktig information, ny information tillkommit eller att Försäkringskassan har begärt en komplettering och vården har svarat med ett nytt intyg med de kompletterande uppgifterna. På ett ersatt intyg framgår vilken som är den nya versionen av intyget. Ett ersatt intyg går inte att skicka eller spara som PDF.'},
                {question:'Varför kan jag inte se makulerade intyg?',answer:'Vårdgivaren kan makulera ett intyg för att det innehåller ett allvarligt fel, till exempel om det är skrivit på fel patient.<p>Om en vårdgivare makulerar ett intyg är det inte tillgängligt i Mina intyg.</p>'}
            ]},
            {title:'Frågor och svar om samtycke',questions:[
                {question:'Varför måste jag ge samtycke?',answer:'För att kunna använda Mina intyg måste du först lämna ditt samtycke till att dina intyg får lagras i en lagringstjänst kopplad till Mina intyg, samt visas och hanteras i Mina intyg. Utan ditt samtycke till att dina personuppgifter i intygen hanteras i Mina intyg kan tjänsten inte användas.'},
                {question:'Vad innebär samtycke?',answer:'Läs mer om samtycke till att använda Mina intyg <a href="#/omminaintyg/samtycke">här</a>.'},
                {question:'Kan jag ta tillbaka mitt samtycke?',answer:'Du kan när som helst återta ditt samtycke. Det innebär att uppgifterna om dig inte längre kommer att visas eller på annat sätt behandlas i Mina intyg. Om du återtar ditt samtycke kan du inte längre använda Mina intyg. Däremot kan du när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.'}
            ]},
            {title:'Frågor och svar om säkerhet',questions:[
                {question:'Är det säkert att använda Mina intyg?',answer:'Ja, Mina intyg har en hög säkerhetsnivå och uppfyller Datainspektionens säkerhetskrav. Du kan bara logga in med e-legitimation (e-legitimation kan också heta bank-id – det är en typ av e-legitimation). E-legitimation är ett säkert sätt att logga in på.'},
                {question:'Hur skyddas mina uppgifter?',answer:'Hanteringen av uppgifter i Mina intyg följer de krav som ställs i Personuppgiftslagen (PuL), Patientdatalagen (PDL) och av Datainspektionen.'},
                {question:'Kan andra komma åt mina uppgifter?',answer:'Nej, i och med att du bara kan logga in med e-legitimation, vars inloggning baseras på ditt personnummer, kan endast information om dig som loggar in visas.'},
                {question:'Varför kan jag inte logga in med lösenord och sms?',answer:'Lösenord och SMS uppfyller inte de krav som gäller för hantering av medicinsk information, som till exempel kan finnas i ett läkarintyg.'},
                {question:'Är det säkert med e-legitimation?',answer:'Ja, en e-legitimation är ett säkert sätt att identifiera sig i Sverige, men glöm inte att även skydda din dator.'},
                {question:'Hur skyddar jag min dator?',answer:'<ol><li>Håll din dator uppdaterad. Ditt operativsystem och din webbläsare bör alltid ha de senaste säkerhetsuppdateringarna installerade. Se också till att du alltid använder senaste versionen av säkerhetsprogrammet för din e-legitimation.</li><li>Använd ett uppdaterat antivirusprogram. Vi rekommenderar att du använder ett uppdaterat antivirusprogram som skyddar din dator från skadlig kod och automatiskt kontrollerar de filer som kommer till din dator.</li><li>Logga ut när du är klar. Avsluta varje besök i Mina intyg med att logga ut och stänga webbläsaren.</li></ol>'}
            ]}
        ],

        /* LINKS */
        'links.inera.kundservice': '<a href="http://www.inera.se/kontakt" target="_blank">Ineras Kundservice</a>',

        'fkdialog.head': 'Du har gett ditt samtycke',
        'fkdialog.text': 'Du har gett ditt samtycke till att Försäkringskassan får hämta dina intyg från tjänsten Mina intyg. Nu kan du välja att logga ut och återgå till Försäkringskassans Mina sidor, eller gå vidare till tjänsten Mina intyg.',
        'fkdialog.button.returntofk': 'Tillbaka till Försäkringskassan',
        'fkdialog.button.continueuse': 'Gå vidare till Mina intyg',

        'listtable.headers.latestevent': 'Senaste händelser',

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
