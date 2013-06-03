angular.module('directives.mi', []);

angular.module('directives.mi').directive("miHeader", ['$rootScope', function($rootScope) {
    return {
        restrict : "E",
        replace : true,
        scope : {
          userName: "@"
        },
        template :
              '<span class="mi-header">' //directives must ha a single root element.
            + ' <a href="/web/start"><img id="logo" src="/img/logo.png" /></a>'
            + ' <div id="status">'
            + '     <div class="status-row">'
            + '         <message key="view.label.loggedinas"></message><br><span class="logged-in">{{userName}}</span>'
            + '     </div>'
            + ' </div>'
            + '</span>'
        
    }
} ]);

angular.module('directives.mi').directive("miMainNavigation", ['$rootScope', '$location' , function($rootScope, $location) {
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
            '<div class="navbar mi-main-navigation">'
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

angular.module('directives.mi').directive("mvkTopBar", ['$rootScope', '$location' , function($rootScope, $location) {
    return {
        restrict : "E",
        replace : true,
        template :
              '<div id="headerContainer">'
            + ' <div id="header">'
            + '  <div class="wrapper">'
            + '   <a href="####" class="backButton">'
            + '     <h1 class="assistiveText">Mina vårdkontakter</h1>'
            + '   </a>'
            + '   <div class="functionRow">'
            + '    <a href="####">Logga ut</a>'
            + '   </div>'
            + '   <div class="clear"></div>'
            + '  </div>'
            + ' </div>'
            + '</div>'
    }
} ]);

