define([ 'text!directives/miSpinner.html' ], function(template) {
	'use strict';

	return [ '$rootScope', function($rootScope) {
	    return {
	        restrict : "A",
	        transclude : true,
	        replace : true,
	        scope : {
	          label: "@",
	          showSpinner: "=",
	          showContent: "="
	        },
	        template : template
	    }
	} ];
});