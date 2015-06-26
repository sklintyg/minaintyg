angular.module('minaintyg').run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('app/directives/miField.html',
    "<div class=\"body-row\">\n" +
    "   <h3 class=\"title\" ng-class=\"{ 'unfilled' : !filled}\"><span message key=\"{{ fieldLabel }}\"></span> <span ng-hide=\"filled\"><span message key=\"view.label.blank\"></span></span></h3>\n" +
    "   <span class=\"text\" ng-show=\"filled\">\n" +
    "       <span ng-transclude></span>\n" +
    "   </span>\n" +
    "</div>\n"
  );


  $templateCache.put('app/directives/miHeader.html',
    "<span class=\"mi-header\">\n" +
    "\t <div id=\"status\">\n" +
    "\t     <div class=\"status-row navbar-user-info\" ng-show=\"userName.length\">\n" +
    "\t         <message key=\"nav.label.loggedinas\"></message><br>\n" +
    "\t         <span class=\"logged-in\">{{userName}}</span><br>\n" +
    "\t         <a href=\"/web/logga-ut\"  id=\"mvklogoutLink\" class=\"mi-header-logout\"><message key=\"mvk.header.logouttext\"></message></a>\n" +
    "\t     </div>\n" +
    "\t     <div class=\"navbar-user-avatar\">\n" +
    "\t     \t<img class=\"user-avatar\" alt=\"\" ng-src=\"/img/avatar_retina.png\" />\n" +
    "\t     </div>\n" +
    "\t </div>\n" +
    "</span>"
  );


  $templateCache.put('app/directives/miMainNavigation.html',
    "<div>\n" +
    "\t<div class=\"navbar-header\">\n" +
    "\t\t<button class=\"btn btn-success navbar-toggle\" type=\"button\" data-toggle=\"collapse\" data-target=\".navbar-nav\" ng-click=\"isCollapsed = !isCollapsed\">\n" +
    "\t\t\t<span>Meny</span>\n" +
    "\t\t</button>\n" +
    "\t</div>\n" +
    "\t<div class=\"hidden-xs\" role=\"navigation\">\n" +
    "\t\t<ul class=\"nav navbar-nav\">\n" +
    "\t\t\t<li ng-class=\"navClass('lista')\"><a role=\"menuitem\" ui-sref=\"lista\" id=\"inboxTab\"><message key=\"nav.label.inbox\"></message></a></li>\n" +
    "\t\t    <li class=\"divider-vertical\" aria-hidden=\"true\"></li>\n" +
    "\t\t    <li ng-class=\"navClass('arkiverade')\"><a role=\"menuitem\" ui-sref=\"arkiverade\" id=\"archivedTab\"><message key=\"nav.label.archived\"></message></a></li>\n" +
    "\t\t    <li class=\"divider-vertical\" aria-hidden=\"true\"></li>\n" +
    "\t\t    <li ng-class=\"navClass('omminaintyg')\"><a role=\"menuitem\" ui-sref=\"omminaintyg\" id=\"aboutTab\"><message key=\"nav.label.aboutminaintyg\"></message></a></li>\n" +
    "\t\t    <li class=\"divider-vertical\" aria-hidden=\"true\"></li>\n" +
    "\t\t    <li ng-class=\"navClass('hjalp')\"><a role=\"menuitem\" ui-sref=\"hjalp\" id=\"helpTab\"><message key=\"nav.label.help\"></message></a></li>\n" +
    "\t\t</ul>\n" +
    "\t</div>\n" +
    "\t<div class=\"collapse\" collapse=\"!isCollapsed\">\n" +
    "\t\t<ul class=\"nav navbar-nav\">\n" +
    "\t\t\t<li ng-class=\"navClass('lista')\"><a role=\"menuitem\" ui-sref=\"lista\" id=\"inboxTab\" ng-click=\"isCollapsed = !isCollapsed\"><message key=\"nav.label.inbox\"></message></a></li>\n" +
    "\t\t    <li class=\"divider-vertical\" aria-hidden=\"true\"></li>\n" +
    "\t\t    <li ng-class=\"navClass('arkiverade')\"><a role=\"menuitem\" ui-sref=\"arkiverade\" id=\"archivedTab\" ng-click=\"isCollapsed = !isCollapsed\"><message key=\"nav.label.archived\"></message></a></li>\n" +
    "\t\t    <li class=\"divider-vertical\" aria-hidden=\"true\"></li>\n" +
    "\t\t    <li ng-class=\"navClass('omminaintyg')\"><a role=\"menuitem\" ui-sref=\"omminaintyg\" id=\"aboutTab\" ng-click=\"isCollapsed = !isCollapsed\"><message key=\"nav.label.aboutminaintyg\"></message></a></li>\n" +
    "\t\t    <li class=\"divider-vertical\" aria-hidden=\"true\"></li>\n" +
    "\t\t    <li ng-class=\"navClass('hjalp')\"><a role=\"menuitem\" ui-sref=\"hjalp\" id=\"helpTab\" ng-click=\"isCollapsed = !isCollapsed\"><message key=\"nav.label.help\"></message></a></li>\n" +
    "\t\t</ul>\n" +
    "\t</div>\n" +
    "</div>\n"
  );


  $templateCache.put('app/directives/miSpinner.html',
    "<div>\n" +
    "  <div ng-show=\"showSpinner\" style=\"text-align: center; padding: 20px;\">\n" +
    "    <img aria-labelledby=\"loading-message\" src=\"/img/ajax-loader.gif\" style=\"text-align: center;\" />\n" +
    "    <p id=\"loading-message\" style=\"text-align: center; color: #64ABC0; margin-top: 20px;\">\n" +
    "      <strong><span message key=\"{{ label }}\"></span></strong>\n" +
    "    </p>\n" +
    "  </div>\n" +
    "  <div ng-show=\"showContent\">\n" +
    "    <div ng-transclude></div>\n" +
    "  </div>\n" +
    "</div>\n"
  );


  $templateCache.put('app/directives/mvkTopBar.html',
    "<div id=\"headerContainer\" role=\"banner\" class=\"affix\">\n" +
    " <div id=\"header\">\n" +
    "  <div class=\"container-fluid\">\n" +
    "   <a href=\"/web/tillbaka-till-mvk\" class=\"backButton\" id=\"backToMvkLink\">\n" +
    "     <h1 class=\"assistiveText\"><message key=\"mvk.header.linktext\"></message></h1>\n" +
    "   </a>\n" +
    "<!--    <div class=\"functionRow\" ng-hide=\"hideLogout\"> -->\n" +
    "<!--     <a href=\"/web/logga-ut\"  id=\"mvklogoutLink\"><message key=\"mvk.header.logouttext\"></message></a> -->\n" +
    "<!--    </div> -->\n" +
    "   <div class=\"clear\"></div>\n" +
    "  </div>\n" +
    " </div>\n" +
    "</div>"
  );


  $templateCache.put('app/partials/common-dialog.html',
    "<div id=\"{{dialogId}}\" class=\"modal-header\">\n" +
    "  <button class=\"close\" ng-click=\"button2click()\">×</button>\n" +
    "  <h3 ng-focus=\"dialog.focus\" tabindex=\"-1\">\n" +
    "    <span message key=\"{{titleId}}\"></span>\n" +
    "  </h3>\n" +
    "</div>\n" +
    "<div class=\"modal-body\">\n" +
    "  <p><span message key=\"{{bodyTextId}}\"></span></p>\n" +
    "</div>\n" +
    "<div class=\"modal-footer\">\n" +
    "  <button id=\"{{button1id}}\" class=\"btn btn-success\" ng-disabled=\"!dialog.acceptprogressdone\" ng-click=\"button1click()\">\n" +
    "    <img ng-hide=\"dialog.acceptprogressdone\" ng-src=\"/img/ajax-loader-small-green.gif\" /> <span message key=\"{{button1text}}\"></span>\n" +
    "  </button>\n" +
    "  <button id=\"{{button2id}}\" class=\"btn btn-info\" ng-click=\"button2click()\">\n" +
    "    <span message key=\"{{button2text}}\"></span>\n" +
    "  </button>\n" +
    "  <button id=\"{{button3id}}\" ng-show=\"button3Visible\" class=\"btn btn-info\" ng-click=\"button3click()\">\n" +
    "    <span message key=\"{{button3text}}\"></span>\n" +
    "  </button>\n" +
    "</div>\n"
  );


  $templateCache.put('app/partials/error-dialog.html',
    "<div class=\"modal-header\">\n" +
    "    <h3><span message key=\"modal.tekniskt-fel.header\"></span></h3>\n" +
    "</div>\n" +
    "<div class=\"modal-body\">\n" +
    "  {{bodyText}}\n" +
    "</div>\n" +
    "<div class=\"modal-footer\">\n" +
    "    <button class=\"btn btn-success\" ng-click=\"$close()\"><span message key=\"modal.button.tekniskt-fel.ok\"></span></button>\n" +
    "</div>\n"
  );


  $templateCache.put('app/views/about/about-mina-intyg.html',
    "<div class=\"row\">\n" +
    "\t<div id=\"about-mina-intyg-root\" class=\"col-xs-12 col-sm-3 col-md-3 col-lg-3 margin-bottom-15px\" aria-hidden=\"{{shouldBeOpen}}\">\n" +
    "\t  <ul class=\"nav nav-pills nav-stacked\" role=\"menu\" ng-focus=\"pagefocus\" aria-labelledby=\"link-about-omminaintyg\">\n" +
    "\t    <li ng-class=\"{'active': visibility['omminaintyg'] }\"><a id=\"link-about-omminaintyg\" href role=\"menuitem\" aria-label=\"Länk Om mina intyg. Laddar information om mina intyg efter denna meny.\" aria-controls=\"about-content-omminaintyg\" ng-click=\"navigateTo('omminaintyg')\"><span message key=\"about.omminaintyghead\"></span><i class=\"glyphicon glyphicon-chevron-right pull-right\"></i></a></li>\n" +
    "\t    <li ng-class=\"{'active': visibility['samtycke'] }\"><a id=\"link-about-samtycke\" href role=\"menuitem\" aria-label=\"Länk Samtycke. Laddar information om samtycket till att anv��nda mina intyg efter denna meny.\" aria-controls=\"about-content-samtycke\" ng-click=\"navigateTo('samtycke')\"><span message key=\"about.consentheader\"></span><i class=\"glyphicon glyphicon-chevron-right pull-right\"></i></a></li>\n" +
    "\t    <li ng-class=\"{'active': visibility['juridik'] }\"><a id=\"link-about-juridik\" href role=\"menuitem\" aria-label=\"Länk Juridik. Laddar information om det juridiska efter denna meny.\" aria-controls=\"about-content-juridik\" ng-click=\"navigateTo('juridik')\"><span message key=\"about.juridikhead\"></span><i class=\"glyphicon glyphicon-chevron-right pull-right\"></i></a></li>\n" +
    "\t  </ul>\n" +
    "\t</div>\n" +
    "\t<div class=\"visible-xs margin-top-5px\"></div>\n" +
    "\t<div class=\"col-xs-12 col-sm-9 col-md-7 col-lg-7\" aria-hidden=\"{{shouldBeOpen}}\">\n" +
    "\t\n" +
    "\t  <div id=\"about-content-omminaintyg\" ng-show=\"visibility.omminaintyg\" aria-hidden=\"{{!visibility.omminaintyg}}\" aria-labelledby=\"link-about-omminaintyg\">\n" +
    "\t      <h1 ng-focus=\"subpagefocus.omminaintyg\" tabindex=\"-1\"><span message key=\"about.omminaintyghead\"></span></h1>\n" +
    "\t      <p></p>\n" +
    "\t      <span class=\"margin-top-5px\" message key=\"about.omminaintygtext\"></span>\n" +
    "\t  </div>\n" +
    "\t\n" +
    "\t  <div id=\"about-content-samtycke\" ng-show=\"visibility.samtycke\" aria-hidden=\"{{!visibility.samtycke}}\" aria-labelledby=\"link-about-samtycke\">\n" +
    "\t    <h1 ng-focus=\"subpagefocus.samtycke\" tabindex=\"-1\"><span message key=\"about.consentheader\"></span></h1>\n" +
    "\t\t<p></p>\n" +
    "\t    <div class=\"alert alert-info margin-top-5px\">\n" +
    "\t      <h2><span message key=\"about.revokeconsentheader\"></span></h2>\n" +
    "\t      <span message key=\"about.revokeconsenttext\"></span>\n" +
    "\t      <p>\n" +
    "\t      <input id=\"revokeConsentBtn\" type=\"button\" ng-click=\"openRevokeDialog()\" value=\"{{getMessage('about.revokeconsentheader')}}\" class=\"btn btn-success\" />\n" +
    "\t      </p>\n" +
    "\t    </div>\n" +
    "\t\n" +
    "\t    <span message key=\"about.consenttext\"></span>\n" +
    "\t\n" +
    "\t  </div>\n" +
    "\t\n" +
    "\t  <div id=\"about-content-juridik\" ng-show=\"visibility.juridik\" aria-hidden=\"{{!visibility.juridik}}\" aria-labelledby=\"link-about-juridik\">\n" +
    "\t      <h1 ng-focus=\"subpagefocus.juridik\" tabindex=\"-1\"><span message key=\"about.juridikhead\"></span></h1>\n" +
    "\t      <span message key=\"about.juridiktext\" class=\"margin-top-5px\"></span>\n" +
    "\t  </div>\n" +
    "\t</div>\n" +
    "</div>\n" +
    "\n"
  );


  $templateCache.put('app/views/consent/consent-given.html',
    "<h1 class=\"bottom-spacing\" ng-focus=\"pagefocus\" tabindex=\"-1\"><span message key=\"consent.givenhead\"></span></h1>\n" +
    "<div class=\"row\">\n" +
    "\t<div class=\"col-xs-12 col-sm-8 col-md-8 col-lg-8\" id=\"consent-given\">\n" +
    "\n" +
    "\t\t<span message key=\"consent.giventext\"></span>\n" +
    "\t\t<p>\n" +
    "\t\t   <button id=\"continueToMI\" ng-click=\"continueToMI()\" class=\"btn btn-success\"><span message key=\"consent.continue\"></span></button>\n" +
    "\t\t</p>\n" +
    "\n" +
    "\t</div>\n" +
    "</div>"
  );


  $templateCache.put('app/views/consent/consent-start.html',
    "<div id=\"consentTerms\" class=\"col-xs-12 col-sm-12 col-md-8 col-lg-8\">\n" +
    "  <h1><span message key=\"consent.consentpage.title\"></span></h1>\n" +
    "\n" +
    "  <div class=\"alert alert-info\">\n" +
    "\t  <span message key=\"consent.info\"></span>\n" +
    "  </div>\n" +
    "\n" +
    "  <h2><span message key=\"consent.aboutheader\"></span></h2>\n" +
    "  <p><span message key=\"consent.abouttext\"></span></p>\n" +
    "\n" +
    "  <span message key=\"consent.consentheader1\"></span>\n" +
    "  <span message key=\"consent.consenttext1\"></span>\n" +
    "\n" +
    "  <span message key=\"consent.consentheader2\"></span>\n" +
    "  <span message key=\"consent.consenttext2\"></span>\n" +
    "\n" +
    "  <span message key=\"consent.consentheader3\"></span>\n" +
    "  <span message key=\"consent.consenttext3\"></span>\n" +
    "\n" +
    "  <span message key=\"consent.consentheader4\"></span>\n" +
    "  <span message key=\"consent.consenttext4\"></span>\n" +
    "\n" +
    "  <span message key=\"consent.consentheader5\"></span>\n" +
    "  <span message key=\"consent.consenttext5\"></span>\n" +
    "\n" +
    "  <span message key=\"consent.consentheader6\"></span>\n" +
    "  <span message key=\"consent.consenttext6\"></span>\n" +
    "\n" +
    "  <p class=\"btn-row-desc\"><span message key=\"consent.confirmtext\"></span></p>\n" +
    "  <p>\n" +
    "    <button id=\"giveConsentButton\" ng-click=\"giveConsent()\" class=\"btn btn-success\"><span message key='consent.giveconsent'></span></button>\n" +
    "  </p>\n" +
    "\n" +
    "</div>"
  );


  $templateCache.put('app/views/error/error.html',
    "\n" +
    "<div id=\"backnav\" class=\"row-fluid\">\n" +
    "  <a class=\"backlink\" href=\"#start\"><span message key=\"common.goback\"></span></a>\n" +
    "</div>\n" +
    "<h1>\n" +
    "  <span message key=\"error.pagetitle\"></span>\n" +
    "</h1>\n" +
    "\n" +
    "<div class=\"row\">\n" +
    "\n" +
    "  <div id=\"errorMessage\" class=\"alert alert-error\">\n" +
    "    <span message key=\"error.{{errorCode}}\"></span> \n" +
    "  </div>\n" +
    "</div>\n"
  );


  $templateCache.put('app/views/help/help.html',
    "<div class=\"row\">\n" +
    "\t<div id=\"help-mina-intyg-root\" class=\"col-xs-12 col-sm-3 col-md-3 col-lg-3 margin-bottom-15px\" aria-hidden=\"{{shouldBeOpen}}\">\n" +
    "\t  <ul class=\"nav nav-pills nav-stacked\" role=\"menu\" ng-focus=\"pagefocus\" aria-labelledby=\"link-help-decription\">\n" +
    "\t    <li ng-class=\"{'active': visibility['helpdescription'] }\"><a id=\"link-help-decription\" href role=\"menuitem\" aria-label=\"Länk Hjälp beskrivning. Laddar information om hjälp efter denna meny.\" aria-controls=\"help-content-description\" ng-click=\"navigateTo('helpdescription')\"><span message key=\"help.helpdescriptionhead\"></span><i class=\"glyphicon glyphicon-chevron-right pull-right\"></i></a></li>\n" +
    "\t    <li ng-class=\"{'active': visibility['helpfaq'] }\"><a id=\"link-help-faq\" href role=\"menuitem\" aria-label=\"Länk om vanliga frågor och svar. Laddar vanliga frågor och svar efter denna meny.\" aria-controls=\"about-content-samtycke\" ng-click=\"navigateTo('helpfaq')\"><span message key=\"help.helpfaqhead\"></span><i class=\"glyphicon glyphicon-chevron-right pull-right\"></i></a></li>\n" +
    "\t  </ul>\n" +
    "\t</div>\n" +
    "\t<div class=\"visible-xs margin-top-5px\"></div>\n" +
    "\t<div class=\"col-xs-12 col-sm-9 col-md-7 col-lg-7\" aria-hidden=\"{{shouldBeOpen}}\">\n" +
    "\t\n" +
    "\t  <div id=\"help-content-description\" ng-show=\"visibility.helpdescription\" aria-hidden=\"{{!visibility.helpdescription}}\" aria-labelledby=\"link-help-decription\">\n" +
    "\t      <h1 ng-focus=\"subpagefocus.helpdescription\" tabindex=\"-1\">\n" +
    "\t\t\t<span id=\"helpDescriptionHeader\"><span message key=\"help.header\"></span></span>\n" +
    "\t  \t  </h1>\n" +
    "\t\t  <p></p>\n" +
    "\t\t  <span message key=\"help.description\"></span>\n" +
    "\n" +
    "\t\t  <h2><span message key=\"help.supportlinks\"></span></h2>\n" +
    "\t\t  <span message key=\"help.supportlinks.description\"></span>\n" +
    "\t  </div>\n" +
    "\t\n" +
    "\t  <div id=\"help-content-faq\" ng-show=\"visibility.helpfaq\" aria-hidden=\"{{!visibility.helpfaq}}\" aria-labelledby=\"link-help-faq\">\n" +
    "\t    <h1 ng-focus=\"subpagefocus.helpfaq\" tabindex=\"-1\">\n" +
    "\t    \t<span id=\"helpFaqHeader\"><span message key=\"help.faq.header\"></span></span>\n" +
    "\t    </h1>\n" +
    "\t    <p></p>\n" +
    "\t    <span message key=\"help.faq.description\"></span>\n" +
    "\t    <p></p>\n" +
    "      <div ng-repeat=\"faqsection in faqs\">\n" +
    "        <h2>{{faqsection.title}}</h2>\n" +
    "        <div ng-repeat=\"faq in faqsection.questions\" class=\"panel-group\" id=\"accordion\">\n" +
    "          <div class=\"panel panel-default\">\n" +
    "            <div class=\"panel-heading\">\n" +
    "              <h4 class=\"panel-title\">\n" +
    "                <span data-toggle=\"collapse\" data-parent=\"#accordion\" ng-bind-html=\"faq.question\"></span>\n" +
    "              </h4>\n" +
    "            </div>\n" +
    "            <div class=\"panel-collapse collapse in\">\n" +
    "              <div class=\"panel-body\" ng-bind-html=\"faq.answer\">\n" +
    "              </div>\n" +
    "            </div>\n" +
    "          </div>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "\t  </div>\n" +
    "  </div>\n" +
    "</div>"
  );


  $templateCache.put('app/views/list/archive/list-archived.html',
    "<div class=\"row\">\n" +
    "  <div class=\"col-xs-12 col-sm-7 col-md-7 col-lg-7\">\n" +
    "    <h1 ng-focus=\"pagefocus\" tabindex=\"0\">\n" +
    "      <span id=\"archivedHeader\" message key=\"archived.header\"></span>\n" +
    "    </h1>\n" +
    "    <p></p>\n" +
    "    <span message key=\"archived.description\"></span>\n" +
    "  </div>\n" +
    "</div>\n" +
    "<div class=\"row\">\n" +
    "  <div class=\"col-xs-12 col-sm-12 col-md-12 col-lg-12\">\n" +
    "    <div mi-spinner label=\"label.archivedcertificatesloading\" show-spinner=\"!doneLoading\"\n" +
    "         show-content=\"doneLoading && (certificates | archived).length>0\">\n" +
    "      <div class=\"table-responsive\">\n" +
    "        <table id=\"certTable\" class=\"table expandable archived-table\" aria-labelledby=\"archivedHeader\">\n" +
    "          <thead>\n" +
    "          <tr>\n" +
    "            <th scope=\"col\" id=\"issued\"><span message key=\"listtable.headers.issued\"></span></th>\n" +
    "            <th scope=\"col\" id=\"type\"><span message key=\"listtable.headers.type\"></span></th>\n" +
    "            <th scope=\"col\" id=\"certinfo\"><span message key=\"listtable.headers.certinfo\"></span></th>\n" +
    "            <th scope=\"col\" id=\"issuedby\"><span message key=\"listtable.headers.issuedby\"></span></th>\n" +
    "            <th scope=\"col\" id=\"latestEvent\"><span message key=\"listtable.headers.latestEvent\"></span></th>\n" +
    "            <th scope=\"col\" id=\"restore\" aria-hidden=\"true\" aria-label=\"Återställ intyg\">&nbsp;</th>\n" +
    "          </tr>\n" +
    "          </thead>\n" +
    "          <tbody>\n" +
    "          <tr ng-repeat=\"cert in certificates | archived\" class=\"{{cert.status}}\" ng-class=\"{'intyglist':1}\">\n" +
    "            <td scope=\"row\" headers=\"issued\">{{cert.sentDate | date:'longDate'}}</td>\n" +
    "            <td headers=\"type\">\n" +
    "              <span message key=\"certificatetypes.{{cert.type}}.commonname\"></span>\n" +
    "              <span class=\"cert-titlebar-type\" message key=\"certificatetypes.{{cert.type | lowercase}}.typename\"></span>\n" +
    "            </td>\n" +
    "            <td headers=\"certinfo\">{{cert.complementaryInfo}}</td>\n" +
    "            <td headers=\"issuedby\">{{cert.careunitName}}</td>\n" +
    "            <td headers=\"latestEvent\">\n" +
    "              <div ng-show=\"cert.statuses.length>0\">\n" +
    "                {{cert.statuses[0].timestamp | date:'short'}}\n" +
    "                <span message key=\"certificates.status.{{cert.statuses[0].type}}\"></span>\n" +
    "                <span ng-show=\"cert.statuses[0].type == 'SENT'\"><span message\n" +
    "                                                                      key=\"certificates.target.{{cert.statuses[0].target}}\"></span></span>\n" +
    "              </div>\n" +
    "              <span ng-show=\"!cert.statuses\">Ingen</span>\n" +
    "            </td>\n" +
    "            <td headers=\"restore\"><a id=\"restoreCertificate-{{cert.id}}\" href\n" +
    "                                     ng-click=\"openRestoreDialog(cert.id)\"><span message key=\"label.restore\"></span></a>\n" +
    "            </td>\n" +
    "          </tr>\n" +
    "          </tbody>\n" +
    "        </table>\n" +
    "      </div>\n" +
    "    </div>\n" +
    "    <!-- eof spinner -->\n" +
    "    <div class=\"table-bottom\" ng-hide=\"(certificates | archived).length<1\"></div>\n" +
    "\n" +
    "    <div id=\"noCerts\" class=\"alert alert-info\" ng-show=\"doneLoading && (certificates | archived).length<1\">\n" +
    "      <span message key=\"error.noarchivedcerts\"></span>\n" +
    "    </div>\n" +
    "  </div>\n" +
    "</div>\n" +
    "\n" +
    "\n"
  );


  $templateCache.put('app/views/list/list.html',
    "<!-- View start -->\n" +
    "<h1 ng-focus=\"pagefocus\" tabindex=\"0\" xmlns=\"http://www.w3.org/1999/html\">\n" +
    "  <span id=\"inboxHeader\" message key=\"inbox.header\"></span>\n" +
    "</h1>\n" +
    "\n" +
    "<div class=\"row\">\n" +
    "  <div class=\"col-xs-12 col-sm-8 col-md-7 col-lg-7\">\n" +
    "    <p>\n" +
    "      <div message key=\"inbox.description\" class=\"mi-text\"></div>\n" +
    "    </p>\n" +
    "  </div>\n" +
    "  <div class=\"col-xs-12 col-sm-8 col-md-8 col-lg-8\">\n" +
    "  \t<p>\n" +
    "  \t\t<div message key=\"inbox.description.archive\" class=\"mi-text\"></div>\n" +
    "  \t\t<a class=\"glyphicon glyphicon-question-sign more-info\" ng-click=\"isCollapsedArchive = !isCollapsedArchive\" title=\"{{messageService.getProperty('common.title.helptext.moreinfo')}}\"></a>\n" +
    "      \t<div collapse=\"!isCollapsedArchive\" class=\"mi-text well well-sm margin-top-5px\"><span message key=\"mi.helptext.arkivera\"></span></div>\n" +
    "  \t</p>\n" +
    "  </div>\n" +
    "</div>\n" +
    "<div class=\"row\">\n" +
    "  <div class=\"col-xs-12 col-sm-12 col-md-12 col-lg-12\">\n" +
    "\n" +
    "    <div mi-spinner label=\"label.certificatesloading\" show-spinner=\"!doneLoading\" show-content=\"doneLoading && (certificates | unarchived).length>0\">\n" +
    "      <div id=\"certTable\">\n" +
    "        <div ng-repeat=\"cert in certificates | unarchived\" class=\"{{cert.status}}\" ng-class=\"{'intyglist': 1}\">\n" +
    "          <div class=\"certificate\">\n" +
    "            <div class=\"row\">\n" +
    "              <div class=\"col-xs-12 col-sm-12 col-md-12 col-lg-12\">\n" +
    "                <div id=\"certificate-{{cert.id}}\" class=\"cert\">\n" +
    "                  <div class=\"cert-titlebar\" ng-class=\"{'cert-cancelled': cert.cancelled}\">\n" +
    "                    <a ng-show=\"!cert.cancelled\" ng-click=\"sendSelected(cert)\"><img class=\"cert-icon\" alt=\"\" ng-src=\"/web/webjars/{{cert.type | lowercase}}/minaintyg/img/{{cert.type | lowercase}}.png\" /></a>\n" +
    "                    <img ng-show=\"cert.cancelled\" class=\"cert-icon\" alt=\"\" ng-src=\"/web/webjars/{{cert.type | lowercase}}/minaintyg/img/{{cert.type | lowercase}}.png\" />\n" +
    "                    <div class=\"row\">\n" +
    "                      <div class=\"col-xs-12 col-sm-7 col-md-8 col-lg-8\">\n" +
    "                        <h2 class=\"cert-title\">\n" +
    "                          <button class=\"btn-link btn-link-link\" ng-click=\"sendSelected(cert)\" ng-show=\"!cert.cancelled\">\n" +
    "                          \t<span message key=\"certificatetypes.{{cert.type | lowercase}}.commonname\"></span><span class=\"text-nowrap\" message key=\"certificatetypes.{{cert.type | lowercase}}.typename\"></span>\n" +
    "                          </button>\n" +
    "                          <span ng-show=\"cert.cancelled\">\n" +
    "                            <span message key=\"certificatetypes.{{cert.type | lowercase}}.commonname\"></span><span message key=\"certificatetypes.{{cert.type | lowercase}}.typename\"></span> - <span message key=\"certificates.status.cancelledlc\"></span>\n" +
    "                          </span>\n" +
    "                        </h2>\n" +
    "                      </div>\n" +
    "                      <div class=\"col-xs-10 col-sm-4 col-md-3 col-lg-3 issue\">\n" +
    "                        <span class=\"cert-titlebar-value\">{{cert.sentDate | date:'shortDate'}}</span>&nbsp;<span message key=\"listtable.headers.issued\"></span>\n" +
    "                      </div>\n" +
    "                      <div class=\"col-xs-2 col-sm-1 col-md-1 col-lg-1 archive hidden-xs\">\n" +
    "                        <button class=\"archive-only btn-link\" id=\"archiveCertificateBtn-{{cert.id}}\" title=\"{{messageService.getProperty('button.alt.archive')}}\" ng-click=\"openArchiveDialog(cert)\">\n" +
    "                        \t<span class=\"glyphicon glyphicon-folder-close white\"></span>\n" +
    "                        </button>\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "                  </div>\n" +
    "                  <div class=\"cert-header\">\n" +
    "                    <div class=\"row\" ng-show=\"cert.cancelled\">\n" +
    "                      <div class=\"col-xs-12 col-sm-8 col-md-8 col-lg-8 header-row\">\n" +
    "                        <div class=\"alert alert-info\">\n" +
    "                          <span message key=\"inbox.revoked\"></span>\n" +
    "                          <span message key=\"inbox.revoked.archive\"></span>\n" +
    "                          <a class=\"glyphicon glyphicon-question-sign more-info\" ng-click=\"isCollapsedRevoked = !isCollapsedRevoked\" title=\"{{messageService.getProperty('common.title.helptext.moreinfo')}}\"></a>\n" +
    "                          <div collapse=\"!isCollapsedRevoked\" class=\"mi-text well well-sm margin-top-5px\"><span message key=\"inbox.revoked.helptext\"></span></div>\n" +
    "                        </div>\n" +
    "                      </div>\n" +
    "                    </div>\n" +
    "                    <div class=\"row\">\n" +
    "                      <div class=\"col-xs-12 col-sm-7 col-md-8 col-lg-8 header-row\">\n" +
    "                        <h3 class=\"tag\"><span message key=\"listtable.headers.issuedby\"></span></h3> {{cert.careunitName}}\n" +
    "                      </div>\n" +
    "                      <div class=\"col-xs-12 col-sm-5 col-md-3 col-lg-3\">\n" +
    "                        <h3 class=\"tag\"><span message key=\"{{cert.type | lowercase}}.inbox.complementaryinfo\"></span></h3><span id=\"certificate-period-{{cert.id}}\">{{cert.complementaryInfo}}</span>\n" +
    "                      </div>\n" +
    "                      <div class=\"col-xs-1 col-sm-1 col-md-1 col-lg-1\"></div>\n" +
    "                    </div>\n" +
    "                    <div class=\"row\">\n" +
    "                      <div class=\"col-xs-12 col-sm-7 col-md-8 col-lg-8 header-row\">\n" +
    "                        <h3 class=\"tag\"><span message key=\"listtable.headers.latestEvent\"></span></h3>\n" +
    "                        <div ng-show=\"cert.statuses.length>0\">\n" +
    "                          {{cert.statuses[0].timestamp | date:'short'}}\n" +
    "                          <span message key=\"certificates.status.{{cert.statuses[0].type}}\"></span>\n" +
    "                          <span ng-show=\"cert.statuses[0].type == 'SENT'\"><span message key=\"certificates.target.{{cert.statuses[0].target}}\"></span>s system</span>\n" +
    "                          <div collapse=\"isCollapsed\">\n" +
    "                            <div ng-repeat=\"status in cert.statuses\" ng-switch on=\"$index > 0\">\n" +
    "                              <div ng-switch-when=\"true\">\n" +
    "                                {{status.timestamp | date:'short'}}\n" +
    "                                <span message key=\"certificates.status.{{status.type}}\"></span>\n" +
    "                                <span ng-show=\"status.type == 'SENT'\"><span message key=\"certificates.target.{{status.target}}\"></span>s system</span><br>\n" +
    "                              </div>\n" +
    "                            </div>\n" +
    "                          </div>\n" +
    "                          <p>\n" +
    "                            <button class=\"btn-link btn-link-link\" ng-show=\"cert.statuses.length>1\" href=\"#status\" ng-click=\"isCollapsed = !isCollapsed\"><span message key=\"label.status.{{ !isCollapsed }}\"></span></button>\n" +
    "                          </p>\n" +
    "                        </div>\n" +
    "                        <span ng-show=\"cert.statuses.length==0\">Ingen</span>\n" +
    "                      </div>\n" +
    "                      <div class=\"col-xs-6 col-sm-3 col-md-3 col-lg-3 pull-left\">\n" +
    "                        <button id=\"viewCertificateBtn-{{cert.id}}\" class=\"btn btn-success\" ng-show=\"!cert.cancelled\" ng-disabled=\"cert.cancelled\" ng-click=\"sendSelected(cert)\"><span message key=\"button.show\"></span></button>\n" +
    "                      </div>\n" +
    "                      <div class=\"col-xs-6 hidden-sm hidden-md hidden-lg pull-right\">\n" +
    "                      \t<button class=\"btn btn-primary pull-right\" id=\"archiveCertificateBtn-{{cert.id}}\" title=\"{{messageService.getProperty('button.alt.archive')}}\" ng-click=\"openArchiveDialog(cert)\">\n" +
    "                      \t\t<span class=\"glyphicon glyphicon-folder-close white\"></span>\n" +
    "                      \t</button>\n" +
    "                      </div>\n" +
    "                      <div class=\"col-xs-1 col-sm-1 col-md-1 col-lg-1\"></div>\n" +
    "                    </div>\n" +
    "                  </div>\n" +
    "                </div>\n" +
    "              </div>\n" +
    "            </div>\n" +
    "            </div>\n" +
    "          <div class=\"table-bottom\" ng-show=\"(certificates | unarchived).length>0\"></div>\n" +
    "        </div>\n" +
    "      </div>\n" +
    "    </div>\n" +
    "  </div>\n" +
    "</div>\n" +
    "<div class=\"row\">\n" +
    "\t<div id=\"noCerts\" ng-show=\"doneLoading && (certificates | unarchived).length<1\" class=\"alert alert-info\">\n" +
    "\t\t<span message key=\"error.nocerts\"></span>\n" +
    "\t</div>\n" +
    "</div>\n" +
    "\n" +
    "<!-- Cookie information -->\n" +
    "<div class=\"row padding-bottom-30px\">\n" +
    "  <div class=\"col-xs-12 col-sm-12 col-md-7 col-lg-7\">\n" +
    "\t  <div>\n" +
    "\t    Mina intyg använder kakor. <a ng-click=\"isCollapsedCookieInfo = !isCollapsedCookieInfo\">Om Kakor (Cookies)</a>\n" +
    "\t  </div>\n" +
    "\t  <div collapse=\"!isCollapsedCookieInfo\" class=\"margin-top-5px\">\n" +
    "\t  \t<div class=\"mi-text well well-sm\">\n" +
    "\t  \t\t<span message key=\"about.cookies\"></span>\n" +
    "\t\t</div>\n" +
    "\t  </div>\n" +
    "\n" +
    "  </div>\n" +
    "</div>\n"
  );

}]);
