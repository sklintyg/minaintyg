/** Directives only used in certificate modules */

angular.module('directives.mi').directive("miField", ['$rootScope', function($rootScope) {
    return {
        restrict : "E",
	    transclude : true,
        replace : true,
        scope : {
          title: "@",
	      filled: "="
        },
        template :
			'<div class="body-row">'
           +'   <h3 class="title" ng-class="{ \'unfilled\' : !filled}"><span message key="{{ title }}"></span><span ng-hide="filled"><span message key="view.label.blank"></span></span></h3>'
           +'   <span class="text" ng-show="filled">'
           +'       <span ng-transclude></span>'
           +'   </span>'
           +'</div>'
    }
} ]);