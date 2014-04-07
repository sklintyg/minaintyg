define([
    'angular',
    'filters/unarchivedFilter',
    'filters/archivedFilter'
], function (angular, unarchivedFilter, archivedFilter) {
    'use strict';

    var moduleName = 'intyg.app.filters';

    angular.module(moduleName, [])
        .filter('unarchived', unarchivedFilter)
        .filter('archived', archivedFilter);

    return moduleName;
});
