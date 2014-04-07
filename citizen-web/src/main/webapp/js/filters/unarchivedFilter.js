define([
    'angular'
], function (angular) {
    'use strict';

    return function () {
        return function(certificates) {
            var out = [];

            angular.forEach(certificates, function(value, key) {
                if (!value.archived) {
                    this.push(value);
                }
            }, out);
            return out;
        };
    };
});
