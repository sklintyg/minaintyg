angular.module('minaintyg').directive('miHeader',
    function() {
        'use strict';

        return {
            restrict: 'E',
            replace: true,
            scope: {
                userName: '@'
            },
            templateUrl: '/app/views/miHeader.directive.html'
        };
    });
