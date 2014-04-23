define([
    'angular',
    'services/listCertService',
    'services/dialogService',
    'services/consentService',
    'services/sendCertService'
], function (angular, listCertService, dialogService, consentService, sendCertService) {
    'use strict';

    var moduleName = 'intyg.app.services';

    angular.module(moduleName, [])
        .factory('listCertService', listCertService)
        .factory('dialogService', dialogService)
        .factory('consentService', consentService)
        .factory('sendCertService', sendCertService);

    return moduleName;
});
