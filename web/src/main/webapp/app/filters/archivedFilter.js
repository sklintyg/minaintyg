angular.module('minaintyg').filter('archived',
    function() {
        'use strict';

        return function(certificates) {
            var out = [];

            angular.forEach(certificates, function(value) {
                if (value.archived) {
                    this.push(value);
                }
            }, out);
            return out;
        };
    });
