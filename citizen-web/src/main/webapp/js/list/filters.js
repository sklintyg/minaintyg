'use strict';

/* Filters */

// Certification list filter that filters based on supplied statues
listCertApp.filter('bycertstatus', function () {
    return function (certificates, statuses) {
        var items = {
                statuses: statuses,
            out: []
        };
        
        angular.forEach(certificates, function (value, key) {
            if (this.statuses[value.status] === true) {
                this.out.push(value);
            }
        }, items);
        return items.out;
    };
});

