/**
 * Common directives used in both MI as well as in modules. Since this js will be used/loaded from 
 * different contextpaths, all templates are inlined. PLEASE keep source formatting in this 
 * file as-is, otherwise the inline templates will be hard to follow. 
 */
angular.module('directives.mi', []);

angular.module('directives.mi').directive("miHeader", ['$rootScope', function($rootScope) {
    return {
        restrict : "E",
        replace : true,
        scope : {
          userName: "@"
        },
        template :
              '<span class="mi-header">' //directives must have a single root element.
            + ' <a href="/web/start"><img alt="Gå till inkorgen i Mina intyg. Logo Mina intyg" id="logo" src="/img/logo.png" /></a>'
            + ' <div id="status">'
            + '     <div class="status-row" ng-show="userName.length">'
            + '         <message key="nav.label.loggedinas"></message><br><span class="logged-in">{{userName}}</span>'
            + '     </div>'
            + ' </div>'
            + '</span>'
        
    }
} ]);

angular.module('directives.mi').directive("miMainNavigation", ['$rootScope', '$location' , function($rootScope, $location) {
    return {
        restrict : "E",
        replace : true,
        scope : {
            linkPrefix: "@",
            defaultActive: "@"
          },
        controller: function($scope, $element, $attrs) {
            $scope.navClass = function (page) {
                if (angular.isString($scope.defaultActive)) {
                    if (page == $scope.defaultActive) {
                        return 'active';
                    }
                }
                var currentRoute = $location.path().substring(1) || 'lista';
                return page === currentRoute ? 'active' : '';
            };  
        },
        template :
            '<div class="navbar mi-main-navigation">'
            + '<div class="navbar-inner">'
            + '  <ul class="nav" role="menubar">'
            + '    <li ng-class="navClass(\'lista\')"><a role="menuitem" ng-href="{{linkPrefix}}#/lista" id="inboxTab"><message key="nav.label.inbox"></message></a></li>'
		    + '    <li class="divider-vertical" aria-hidden="true"></li>'
            + '    <li ng-class="navClass(\'arkiverade\')"><a role="menuitem" ng-href="{{linkPrefix}}#/arkiverade" id="archivedTab"><message key="nav.label.archived"></message></a></li>'
            + '    <li class="divider-vertical" aria-hidden="true"></li>'
            + '    <li ng-class="navClass(\'omminaintyg\')"><a role="menuitem" ng-href="{{linkPrefix}}#/omminaintyg" id="aboutTab"><message key="nav.label.aboutminaintyg"></message></a></li>'
            + '    <li class="divider-vertical" aria-hidden="true"></li>'
            + '    <li ng-class="navClass(\'hjalp\')"><a role="menuitem" ng-href="{{linkPrefix}}#/hjalp" id="helpTab"><message key="nav.label.help"></message></a></li>'
            + '  </ul>'
            + ' </div>'
            + '</div>'
            
            
    }
} ]);

angular.module('directives.mi').directive("mvkTopBar", ['$rootScope', '$location' , function($rootScope, $location) {
    return {
        restrict : "E",
        replace : true,
        scope : {
            hideLogout: "@"
          },
        template :
              '<div id="headerContainer" role="banner">'
            + ' <div id="header">'
            + '  <div class="wrapper">'
            + '   <a href="/web/tillbaka-till-mvk" class="backButton" id="backToMvkLink">'
            + '     <h1 class="assistiveText"><message key="mvk.header.linktext"></message></h1>'
            + '   </a>'
            + '   <div class="functionRow" ng-hide="hideLogout">'
            + '    <a href="/web/logga-ut"  id="mvklogoutLink"><message key="mvk.header.logouttext"></message></a>'
            + '   </div>'
            + '   <div class="clear"></div>'
            + '  </div>'
            + ' </div>'
            + '</div>'
    }
} ]);

angular.module('directives.mi').directive('ngFocus',function($parse,$timeout){
  return function(scope,element,attrs){
    var ngFocusGet = $parse(attrs.ngFocus);
    var ngFocusSet = ngFocusGet.assign;
    if (!ngFocusSet) {
      throw Error("Non assignable expression");
    }

    var digesting = false;

    var abortFocusing = false;
    var unwatch = scope.$watch(attrs.ngFocus,function(newVal){
      if(newVal){
        $timeout(function(){
          element[0].focus();
        },0)
      }
      else {
        $timeout(function(){
          element[0].blur();
        },0);
      }
    });


    element.bind("blur",function(){

      if(abortFocusing) return;

      $timeout(function(){
        ngFocusSet(scope,false);
      },0);

    });


    var timerStarted = false;
    var focusCount = 0;

    function startTimer(){
      $timeout(function(){
        timerStarted = false;
        if(focusCount > 3){
          unwatch();
          abortFocusing = true;
          throw new Error("Aborting : ngFocus cannot be assigned to the same variable with multiple elements");
        }
      },200);
    }

    element.bind("focus",function(){

      if(abortFocusing) return;

      if(!timerStarted){
        timerStarted = true;
        focusCount = 0;
        startTimer();
      }
      focusCount++;

      $timeout(function(){
        ngFocusSet(scope,true);
      },0);

    });
  };
});