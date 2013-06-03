angular.module('directives.mi.header', []);

angular.module('directives.mi.header').directive("miHeader", ['$rootScope', function($rootScope) {
    return {
        restrict : "E",
        replace : true,
        scope : {
          userName: "@"
        },
        template :
          "<div id='page-header'>"
        + "<div id='page-header-left'></div>"
        + "<div id='page-header-right'></div>"
        + "<a href='/web/start'><img id='logo' alt='logo' ng-src='/img/logo_mina_intyg.png' /></a>"
        + "<div id='status'>"
        + "  <div class='status-row'>"
        + "    <span class='logged-in'><message key='view.label.loggedinas'/></span>&nbsp;<strong>{{userName}}</strong>"
        + "  </div>"
        + "</div>"
    }
} ]);

angular.module('directives.mi.header').directive("miMainNavigation", ['$rootScope', '$location' , function($rootScope, $location) {
    return {
        restrict : "E",
        replace : true,
        controller: function($scope, $element, $attrs) {
            $scope.navClass = function (page) {
                var currentRoute = $location.path().substring(1) || 'lista';
                return page === currentRoute ? 'active' : '';
            };  
        },
        template :
            '<div class="navbar">'
            + '<div class="navbar-inner">'
            + '  <ul class="nav">'
            + '    <li ng-class="navClass(\'lista\')"><a href="#/lista" id="inboxTab"><message key="label.inbox" /></a></li>'
            + '    <li ng-class="navClass(\'arkiverade\')"><a href="#/arkiverade" id="archivedTab">Arkiverade Intyg</a></li>'
            + '    <li ng-class="navClass(\'omminaintyg\')"><a href="#/omminaintyg">Om Mina Intyg</a></li>'
            + '    <li ng-class="navClass(\'hjalp\')"><a href="#/hjalp">Hjälp</a></li>'
            + '  </ul>'
            + ' </div>'
            + '</div>'
            
            
    }
} ]);