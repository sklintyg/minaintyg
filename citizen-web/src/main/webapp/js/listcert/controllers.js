'use strict';

/* Controllers */
listCertApp.controller('ListCertCtrl', [ '$scope', 'listCertService', function ListCertCtrl($scope, listCertService) {
    listCertService.getCertificates(function(list) {
        $scope.certificates = list;
    });
} ]);

listCertApp.controller('ListArchivedCertCtrl', [ '$scope', 'listCertService', function ListCertCtrl($scope, listCertService) {
    listCertService.getCertificates(function(list) {
        $scope.certificates = list;
    });
} ]);
