define([ 'text!directives/mvkTopBar.html' ], function(template) {
    'use strict';

    return [ '$rootScope', '$location', function($rootScope, $location) {
        return {
            restrict : "E",
            replace : true,
            scope : {
                hideLogout: "@"
            },
            template : template
        }
    } ];
});