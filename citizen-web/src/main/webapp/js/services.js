define([
    'angular',
    'services/listCertService',
    'services/dialogService',
    'services/consentService'
], function (angular, listCertService, dialogService, consentService) {
    'use strict';

    var moduleName = 'intyg.app.services';

    angular.module(moduleName, [])
        .factory('listCertService', listCertService)
        .factory('dialogService', dialogService)
        .factory('consentService', consentService);

    return moduleName;
});
