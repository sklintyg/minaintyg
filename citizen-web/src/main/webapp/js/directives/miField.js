define([ 'text!directives/miField.html' ], function(template) {
	'use strict';

	return [ '$rootScope', function($rootScope) {
	    return {
	        restrict : "A",
	        transclude : true,
	        replace : true,
	        scope : {
	            fieldLabel: "@",
	            filled: "=?"
	        },
	        template : template
	    }
	} ];
});