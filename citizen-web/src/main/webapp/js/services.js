define([
    'angular',
    'services/listCertService',
    'services/consentService'
], function (angular, listCertService, consentService) {
    'use strict';

    var moduleName = 'intyg.app.services';

    angular.module(moduleName, [])
        .factory('listCertService', listCertService)
        .factory('consentService', consentService);

    return moduleName;
});
