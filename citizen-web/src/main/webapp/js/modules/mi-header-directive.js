angular.module('directives.mi.header', []).directive("miHeader", ['$rootScope', function($rootScope) {
    return {
        restrict : "E",
        replace : true,
        scope : {
          proxyPrefix: "@",
          userName: "@"
        },
        template :
          "<div id='page-header'>"
        + "<div id='page-header-left'></div>"
        + "<div id='page-header-right'></div>"
        + "<a href='/web/start'><img id='logo' alt='logo' ng-src='{{proxyPrefix}}/img/logo_mina_intyg.png' /></a>"
        + "<div id='status'>"
        + "  <div class='status-row'>"
        + "    <span class='logged-in'><message key='view.label.loggedinas'/></span>&nbsp;<strong>{{userName}}</strong>"
        + "  </div>"
        + "</div>"
    }
} ]);