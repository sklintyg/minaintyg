angular.module('minaintyg').directive('miField',
    function() {
        'use strict';

        return {
            restrict: 'A',
            transclude: true,
            replace: true,
            scope: {
                fieldLabel: '@',
                filled: '=?'
            },
            templateUrl: '/app/directives/miField.html'
        };
    });
