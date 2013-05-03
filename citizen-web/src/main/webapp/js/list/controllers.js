'use strict';

/* Controllers */
listCertApp.controller('ListCtrl', [ '$scope', 'listCertService', function ListCertCtrl($scope, listCertService) {
    listCertService.getCertificates(function (list) {
        $scope.certificates = list;
    });
} ]);

listCertApp.controller('ListArchivedCtrl', [ '$scope', 'listCertService', function ListCertCtrl($scope, listCertService) {
    listCertService.getCertificates(function (list) {
        $scope.certificates = list;
    });
} ]);
