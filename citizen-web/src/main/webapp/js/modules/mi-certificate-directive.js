/** Directives only used in certificate modules */

angular.module('directives.mi').directive("miField", ['$rootScope', function($rootScope) {
    return {
        restrict : "A",
	    transclude : true,
        replace : true,
        scope : {
          fieldLabel: "@",
	      filled: "=?"
        },
        template :
			'<div class="body-row">'
           +'   <h3 class="title" ng-class="{ \'unfilled\' : !filled}"><span message key="{{ fieldLabel }}"></span> <span ng-hide="filled"><span message key="view.label.blank"></span></span></h3>'
           +'   <span class="text" ng-show="filled">'
           +'       <span ng-transclude></span>'
           +'   </span>'
           +'</div>'


/*
		//TODO: fix this to use default value for filled if it is omitted (it is already optional but default value is undefined =?)
		compile: function(element,attrs)
	    {
		    var filled = attrs.filled || true;
		    var htmlText = '<div class="body-row">'
		               +'   <h3 class="title" ng-class="{ \'unfilled\' : !'+filled+'}"><span message key="{{ title }}"></span> <span ng-hide="'+filled+'"><span message key="view.label.blank"></span></span></h3>'
		               +'   <span class="text" ng-show="'+filled+'">'
		               +'       <span ng-transclude></span>'
		               +'   </span>'
		               +'</div>';
		    element.replaceWith(htmlText);
	    }*/
    }
} ]);