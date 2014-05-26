define([ 'text!directives/miMainNavigation.html' ], function(template) {
	'use strict';

	return [ '$rootScope', '$location', function($rootScope, $location) {
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
	                if ($rootScope.keepInboxTab == true) {
	                    var currentRoute = 'lista';
	                } else {
	                    var currentRoute = $location.path().substring(1) || 'lista';
	                }
	                return page === currentRoute ? 'active' : '';
	            };  
	        },
	        template : template
	    }
	} ];
});