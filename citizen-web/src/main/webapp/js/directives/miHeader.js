angular.module('minaintyg').directive('miHeader',
    function() {
        'use strict';

        return {
            restrict: 'E',
            replace: true,
            scope: {
                userName: '@'
            },
            templateUrl: '/js/directives/miHeader.html'
        };
    });
