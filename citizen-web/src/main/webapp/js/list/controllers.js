'use strict';

/* Controllers */
listCertApp.controller('ListCtrl', [ '$scope', '$filter','listCertService', function ListCertCtrl($scope, $filter, listCertService) {
    $scope.certificates = [];

    $scope.initialDisplaySize = 10;
    $scope.currentDisplaySize = 10;

    listCertService.getCertificates(function(list) {
        //$scope.certificates = list;
        //filter and just keep those with right status
        $scope.certificates = $filter('bycertstatus')(list, {
            'UNHANDLED':true, 'DELETED':false});
    });
} ]);

listCertApp.controller('ListArchivedCtrl', [ '$scope', 'listCertService', function ListCertCtrl($scope, listCertService) {
    listCertService.getCertificates(function(list) {
        $scope.certificates = list;
    });
} ]);
