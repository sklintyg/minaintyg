'use strict';

/* Filters */

// Filter items that should be listed in inbox
angular.module('filters.inboxfilter', []);
angular.module('filters.inboxfilter').filter('inboxfilter', function() {
    return function(certificates) {
        var out = [];

        angular.forEach(certificates, function(value, key) {
            if (!value.archived) {
                this.push(value);
            }
        }, out);
        return out;
    };
});

// Filter items that should be listed in inbox-archived
angular.module('filters.archivedfilter', []);
angular.module('filters.archivedfilter').filter('archivedfilter', function() {
    return function(certificates) {
        var out = [];

        angular.forEach(certificates, function(value, key) {
            if (value.archived) {
                this.push(value);
            }
        }, out);
        return out;
    };
});
