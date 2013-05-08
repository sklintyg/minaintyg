'use strict';

/* Filters */

// Filter items that should be listed in inbox
listCertApp.filter('inboxfilter', function() {
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
listCertApp.filter('archivedfilter', function() {
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
