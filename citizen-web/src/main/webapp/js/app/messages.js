'use strict';
var minaIntygResources = {
    "sv" : {
        "label.showall" : "Visa alla",
        "label.showfewer" : "Visa färre",
        "label.status.false" : "Visa alla händelser",
        "label.status.true" : "Visa färre händelser",
        "label.restore" : "Återställ",
        "label.certificatesloading" : "Dina intyg laddas. Vänligen vänta...",
        "label.archivedcertificatesloading" : "Arkiverade intyg laddas. Vänligen vänta... ",
        "button.archive" : "Arkivera",
        "button.alt.archive" : "Arkivera intyget. Flyttar intyget till Arkiverade intyg",
        "button.send" : "Skicka",
        "button.show" : "Visa intyget",

        "inbox.header" : "Inkorgen",
        "inbox.description" : "Här listas alla dina intyg med det senast inkomna intyget överst. För att hantera intyget väljer du <i>Visa intyget</i>. Då kan du: <br><br> <ul><li>läsa hela intyget</li><li>spara det till din dator</li><li>skriva ut det</li><li>skicka det till Försäkringskassan</li></ul><br>Om du vill arkivera intyget väljer du <i>Arkivera intyg.</i>",
        "inbox.revoked" : "Intyget är rättat av din vårdgivare. Det går därför inte att visa eller hantera. Kontakta din vårdgivare för mer information.",
        "inbox.archivemodal.header" : "Arkivera intyg",
        "inbox.archivemodal.text" : "När du väljer att arkivera intyget kommer det att flyttas till <i>Arkiverade intyg</i>.<br><br> Du kan när som helst återställa intyget igen från <i>Arkiverade intyg</i>.",

        "archived.header" : "Arkiverade intyg",
        "archived.description" : "<p>Ett läkarintyg innehåller information som hämtas från din patientjournal. Därför gäller samma lagar och regler för ditt intyg som för journalen. Det innebär att: <ul><li>Du kan inte helt ta bort ditt intyg från intygstjänsten. Däremot kan du flytta dina gamla intyg hit till <i>Arkiverade intyg</i>.</li><li>Uppgifter i patientjournalen måste lagras i minst 10 år enligt lag. Ofta lagras de längre, i vissa fall livet ut.</li><li>Om någon uppgift är fel i ditt intyg ska du vända dig till din vårdgivare. De kan då rätta uppgiften i journalen.</li></ul></p><p><a href=\"http://www.datainspektionen.se/lagar-och-regler/patientdatalagen\" target=\"_blank\">Läs mer om lagring av uppgifter och ändring av information hos Datainspektionen</a>.</p><p>Du kan flytta tillbaka ett arkiverat intyg till inkorgen genom att välja <i>Återställ</i>.</p>",
        "archived.restoremodal.header" : "Återställ intyg",
        "archived.restoremodal.text" : "När du väljer att återställa intyget kommer det att flyttas till <i>Inkorgen</i>.<br><br>Du kan när som helst arkivera intyget igen från <i>Inkorgen</i>.",

        "about.header" : "Om Mina intyg",

        "error.nocerts" : "Du har för närvarande inga intyg i din inkorg.",
        "error.noarchivedcerts" : "Du har för närvarande inga arkiverade intyg",

        "about.omminaintyghead" : "Om Mina intyg",
        "about.omminaintygtext" : "<p>Mina intyg är en säker webbtjänst där du kan hantera dina intyg via internet. För att kunna använda tjänsten behöver du ha en e-legitimation och ett användarkonto hos Mina vårdkontakter. Det går även att nå tjänsten från Försäkringskassans Mina sidor om du har ett användarkonto där.</p> <h3>Säkerhet</h3> <p>All informationsöverföring är skyddad (krypterad) och uppfyller vårdens krav på säkerhet och sekretess. Det kostar inget att använda tjänsten. (kanske bör stå något mer om säkerheten här?)</p> <h3>Så använder du Mina intyg</h3> <p>I Mina intyg kan du enkelt hantera de intyg som vården utfärdat för dig. Du kan läsa, skriva ut och spara dina intyg. Du kan även skicka intyg vidare till Försäkringskassan, arbetsgivare eller någon annan registrerad mottagare. Hanteringen sköter du via ditt användarkonto och tjänsten är tillgänglig dygnet runt.</p> <h3>Ny användare</h3> <p>Första gången du loggar in i Mina intyg måste du ge ditt samtycke till att Mina intyg får hantera dina personuppgifter. Därefter kan du börja använda tjänsten omedelbart. Du kan bara använda tjänsten för dig själv. Barn och andra anhöriga måste ha egna användarkonton.</p><h3>Ansvarig för tjänsten</h3> <p>[kort text om Inera AB – ska kompletteras med]</p> <h3>Bakgrund</h3> <p>Under 2011 infördes e-tjänsten läkarintyg i Sverige. Det innebär att läkaren kan skicka läkarintyg elektroniskt från sitt journalsystem till Försäkringskassan. Mina intyg är en utveckling av tjänsten så att invånarna själva ska kunna hantera sina läkarintyg och få inflytande över vilken mottagare som ska få tillgång till intygen. I dag går det att skicka läkarintyg till Försäkringskassan och arbete pågår så att det ska bli möjligt att skicka läkarintyg till andra mottagare, som till exempel arbetsgivare och försäkringsbolag.</p> <p>Om du vill veta mer:</p> <p><a href=\"http://www.minavardkontakter.se/C125755F00329208/p/startpage?opendocument\" target=\"_blank\">Mer information om Mina Vårdkontakter</a></p> <p><a href=\"http://www.minavardkontakter.se/C125755F00329208/p/KONT-8ZSGV8?opendocument\" target=\"_blank\">Information om e-legitimation på Mina vårdkontakter</a></p> <p><a href=\"http://www.forsakringskassan.se/privatpers/om_sjalvbetjaning/!ut/p/b1/hY7BCoJAGISfpRfYf_5Vd9ejW5AWSlmQ7SUsQoRWO0TR26fRtZrbwDcfQ44qcl19b5v61vZdfRm7UwcLILacYF4ajSyY5tKUkQR4APYDgC9J8G-_INcevXicvIDgUMoAOgw4VrEGK9qRexuMRG5Hg0kUI-N0y-vClrNl9AF-PCjS3p_p6qvnZtVMXingJIY!/dl4/d5/L2dJQSEvUUt3QS80SmtFL1o2XzgyME1CQjFBMDhTMDgwSTlIOUVGOTJHQTMx/\" target=\"_blank\">Försäkringskassan information om Mina sidor</a></p> <div class=\"alert alert-info extra-margin-top\"> <h3>Tekniska krav och tillgänglighet</h3> <p>Mina intyg följer i största mån samma riktlinjer som Mina vårdkontakter.</p> <p><a href=\"https://www.minavardkontakter.se/C125755F00329208/p/OSAL-7PBG4J?opendocument\" target=\"_blank\">Se informationen under menyvalet Om Mina vårdkontakter</a> </div>",
        "about.consentheader" : "Samtycke",
        "about.consenttext" : " <h3>Om tjänsten Mina intyg</h3><p>Mina intyg är en webbtjänst där du enkelt kan hantera olika vårdintyg som är utfärdade till dig eller till någon du är ombud för. Du kan läsa intygen, spara ner dem på din dator och skriva ut dem. Du kan också skicka intygen vidare till Försäkringskassan. Det är du själv som styr över allt som händer med intygen.</p><h3>Personuppgifterna i Mina intyg</h3><p>Din vårdgivare är enligt patientdatalagen skyldig att föra patientjournal. De intyg som vårdgivaren utfärdar innehåller uppgifter från patientjournalen. Personuppgifterna som finns i intygen är samma personuppgifter som hanteras i tjänsten Mina intyg. Det är: <ul><li>namn</li><li>personnummer</li><li>adress</li><li>diagnos</li><li>sjukdomsförlopp</li><li>funktionsnedsättning</li><li>aktivitetsbegränsning</li><li>rekommendationer</li><li>planerad eller pågående behandling</li><li>arbetsförmåga</li><li>prognos</li><li>vårdbesök</li></ul></p><p>Personuppgifterna finns i Mina intyg för att underlätta för dig. Du ska enkelt kunna hantera dina intyg och välja när och till vem du vill skicka dem. Uppgifterna kommer också att användas för att ta fram statistik om sjukskrivningar, men statistiken går inte att härleda till dig eller någon annan person.</p><h3>Ansvarig för Mina intyg</h3><p>Tjänsten Mina intyg drivs av Inera AB som på uppdrag av hälso- och sjukvården fungerar som personuppgiftsbiträde. Det innebär att Inera AB behandlar personuppgifter om enskilda personer.</p><h3>Dina rättigheter</h3><p>Som användare av tjänsten Mina intyg har du bland annat rätt att:</p><ul><li>varje år begära kostnadsfri information om hur dina personuppgifter behandlas av Inera AB</li><li>när som helst begära att dina uppgifter rättas</li><li>när som helst begära att dina personuppgifter spärras</li><li>kräva skadestånd enligt <a href=\"http://www.riksdagen.se/sv/Dokument-Lagar/Lagar/Svenskforfattningssamling/Patientdatalag-2008355_sfs-2008-355/?bet=2008:355%22%20\l%20%22K10#K10\" target=\"_blank\">10 kap. 1 § PDL</a> eller <a href=\"http://www.riksdagen.se/sv/Dokument-Lagar/Lagar/Svenskforfattningssamling/Personuppgiftslag-1998204_sfs-1998-204/?bet=1998:204\" target=\"_blank\">48 § PUL</a> vid behandling av personuppgifter i strid med lag.</li></ul></p><p>Du har också rätt att begära att Inera AB lämnar ut uppgifter till tredje man  och underrättar om uppgifternas felaktighet, samt att få information om hur tredje man kan söka och få tillgång till dina uppgifter.</p><i>Kontaktuppgifter</i><p><a href=\"http://www.inera.se/\" target=\"_blank\">Inera AB</a><br>Postadress: Box 17703, 118 93 Stockholm<br>Organisationsnummer: 556559-4230<br>Telefon: 0771-25 10 10 (nationell kundservice), eller 08-452 71 60 (växel)<br>E-post: info@inera.se</p><h3>Ge och återta samtycke</h3><p>När du ger ditt samtycke godkänner du att:<ul><li>personuppgifterna som finns i intygen <u>får hanteras och visas</u> i Mina intyg</li><li>personuppgifterna som finns i intygen <u>får hanteras</u> för statistiska ändamål.</li></ul></p><p>Du kan när som helst återta samtycket. Det innebär att uppgifterna om dig inte längre kommer att visas eller på annat sätt behandlas i Mina intyg. När du återtar ditt samtycke kan du inte längre använda Mina intyg. Däremot kan du när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.</p>",
        "about.juridikhead" : "Juridik",
        "about.juridiktext" : "<h3>Lagring av personuppgifter</h3><p>Din vårdgivare är skyldig att föra patientjournal enligt patientdatalagen. Eftersom läkarintygen är en del av din journal kommer dina personuppgifter att finnas lagrade lika länge som de finns i din patientjournal. Uppgifterna sparas i minst 10 år. Den här personuppgiftsbehandlingen är inte frivillig. Det betyder att personuppgifterna och intygen kommer att lagras även om du motsätter dig det.</p><p>Alla elektroniska läkarintyg lagras i en särskild databas. Därifrån hämtar webbtjänsten Mina intyg sin information.</p><h3>Skillnaden mellan hantering och lagring</h3><p>Din vårdgivare är skyldig att <u>lagra</u> information från patientjournalen. Däremot får de uppgifter som finns i intygen endast <u>hanteras</u> av behöriga personer, det vill säga hälso- och sjukvårdspersonal som deltar i vården av dig eller behöver uppgifterna för att utföra sina arbetsuppgifter. Det kan vara läkarsekreterare eller sjuksköterska, du själv eller den du väljer ska få tillgång till intyget, till exempel Försäkringskassan.</p><h3>Sekretess</h3><p>Personuppgifter inom hälso- och sjukvården lyder under sekretess. Uppgifter om ditt hälsotillstånd eller andra personliga förhållanden får alltså inte röjas – om det inte är tydligt att de kan röjas utan att du eller någon närstående till dig lider men. Det är bara den som behöver informationen för att kunna vårda dig som får ta del av den.</p><p>Sekretess kan ibland även gälla för uppgifter om ditt hälsotillstånd i förhållande till dig själv, om det på grund av ändamålet med vården är av synnerlig vikt att uppgiften inte lämnas till dig.</p><p>Sekretessen hindrar inte att uppgifter lämnas</p><ul><li>till en enskild vårdgivare enligt vad som föreskrivs om <a href=\"http://www.1177.se/Regler-och-rattigheter/Patientjournalen/#section-3\" target=\"_blank\"> sammanhållen journalföring i patientdatalagen</a></li><li>till ett nationellt eller regionalt kvalitetsregister enligt patientdatalagen</li><li>till andra myndigheter i vissa fall</li><li>om det finns lagstiftning som kräver det.</li></ul><h3>Radering av information i din patientjournal</h3><p>Om du vill att hela eller delar av din patientjournal ska raderas kan du begära detta hos <a href=\"http://www.ivo.se/Sidor/default.aspx\" target=\"_blank\">Inspektionen för vård och omsorg, IVO</a>. För att IVO ska besluta att radera en journaluppgift eller en hel patientjournal krävs det att du har godtagbara skäl för det, att det är uppenbart att uppgifterna inte behövs för din vård samt att det ur allmän synpunkt är uppenbart att det inte finns skäl att bevara journalen.</p><p>Ett exempel på när det från allmän synpunkt finns skäl att bevara journalen är att uppgifterna ligger till grund för olika beslut och åtgärder hos andra myndigheter eller att de behövs för forskning eller statistik.</p><h3>Lagar som styr hantering av personuppgifter</h3><p><a href=\"http://www.riksdagen.se/sv/Dokument-Lagar/Lagar/Svenskforfattningssamling/Personuppgiftslag-1998204_sfs-1998-204/?bet=1998:204\" target=\"_blank\">Personuppgiftslagen (PUL)</a> och <a href=\"http://www.riksdagen.se/sv/Dokument-Lagar/Lagar/Svenskforfattningssamling/Patientdatalag-2008355_sfs-2008-355/?bet=2008:355\" target=\"_blank\">Patientdatalagen (PDL)</a> är de lagar som reglerar hur personuppgifter behandlas.</p><p><a href=\"https://kontakt.minavardkontakter.se/mvk/wrapper.xhtml?title=Hj%C3%A4lp&url=https%3A%2F%2Fwww.minavardkontakter.se%2Fsupport\" target=\"_blank\">Mer information om hantering av personuppgifter hos Mina vårdkontakter</a>.</p><p><a href=\"http://www.1177.se/Regler-och-rattigheter/Patientjournalen/\" target=\"_blank\">Mer information om patientjournalen hos 1177.se</a>.</p>",
        "about.revokeconsentheader" : "Återkalla samtycke",
        "about.revokeconsenttext" : "Du har gett ditt samtycke till att Mina intyg får hantera dina intyg elektroniskt. Du kan när som helst återkalla ditt samtycke. Det innebär att uppgifter om dig inte längre kommer att visas eller på annat sätt behandlas i Mina intyg. När du återkallar ditt samtycke kan du inte längre använda tjänsten. Du kan när som helst återaktivera tjänsten genom att lämna ett nytt samtycke.",
        "about.revokemodal.header" : "Du har angett att du vill ta tillbaka samtycke",
        "about.revokemodal.text" : "Går du vidare innebär det att du inte kommer att kunna använda tjänsten förrän du har gett ditt samtycke igen. Vill du fortsätta?",

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
        "listtable.headers.latestevent" : "Senaste händelsen",

        "fkdialog.head" : "Samtycke givet",
        "fkdialog.text" : "Du har nu givit det samtycke som krävs för att dina intyg kan hämtas av Försäkringskassan. Nu kan du antingen logga ut för att återgå till Försäkringskassans sidor eller gå vidare in i tjänsten Mina Intyg.",
        "fkdialog.button.returntofk" : "Tillbaka till Försäkringskassan",
        "fkdialog.button.continueuse" : "Gå vidare till Mina Intyg"

    },
    "en" : {
        "label.showall" : "Show all"

    }
};
