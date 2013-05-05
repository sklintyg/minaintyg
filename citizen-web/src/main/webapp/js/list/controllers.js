'use strict';

/* Controllers */
listCertApp.controller('ListCtrl', [ '$scope', 'listCertService', function ListCertCtrl($scope, listCertService) {
    $scope.certificates = [];

    $scope.initialDisplaySize = 10;
    $scope.currentDisplaySize = 10;

    listCertService.getCertificates(function(list) {
        $scope.certificates = list;
    });
} ]);

listCertApp.controller('ListArchivedCtrl', [ '$scope', 'listCertService', function ListCertCtrl($scope, listCertService) {
    listCertService.getCertificates(function(list) {
        $scope.certificates = list;
    });
} ]);
