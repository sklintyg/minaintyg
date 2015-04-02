angular.module('minaintyg').provider('minaintyg.path',
    function() {
        'use strict';

        /**
         * Object that holds config and default values.
         */

        this.$get = [ '$window', function( $window ) {
            return {
                currentPath :function(){
                    return $window.location.href;
                }
            };
        }];

    });
