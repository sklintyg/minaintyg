define([ 'text!directives/miHeader.html' ], function(template) {
	'use strict';

	return [ '$rootScope', function($rootScope) {
	    return {
	        restrict : "E",
	        replace : true,
	        scope : {
	          userName: "@"
	        },
	        template : template
	        
	    }
	} ];
});