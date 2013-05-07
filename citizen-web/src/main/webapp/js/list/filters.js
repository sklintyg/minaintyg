'use strict';

/* Filters */

//Filter items that should be listed in inbox
listCertApp.filter('inboxfilter', function() {
    return function(certificates) {
        var items = {
            statuses : {
                "UNHANDLED" : true,
                "DELETED" : false, // only one to exclude in inbox list?
                "RESTORED" : true,
                "CANCELLED" : true,
                "SENT" : true,
                "RECEIVED" : true,
                "IN_PROGRESS" : true,
                "PROCESSED" : true
            },
            out : []
        };

        angular.forEach(certificates, function(value, key) {
            if (this.statuses[value.status] === true) {
                this.out.push(value);
            }
        }, items);
        return items.out;
    };
});

//Filter items that should be listed in inbox-archived
listCertApp.filter('archivedfilter', function() {
    return function(certificates) {
        var items = {
            statuses : {

                "DELETED" : true
            // only one to include in archive list?
            },
            out : []
        };

        angular.forEach(certificates, function(value, key) {
            if (this.statuses[value.status] === true) {
                this.out.push(value);
            }
        }, items);
        return items.out;
    };
});
