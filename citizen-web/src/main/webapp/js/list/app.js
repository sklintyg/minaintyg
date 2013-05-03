'use strict';

/* App Module */

var listCertApp = angular.module('ListCertApp', []).config([ '$routeProvider', function ($routeProvider) {
    $routeProvider.when('/lista', {
        templateUrl: '/views/list.html',
        controller: 'ListCtrl'
    }).when('/arkiverade', {
            templateUrl: '/views/list-archived.html',
            controller: 'ListArchivedCtrl'
        }).otherwise({
            redirectTo: '/lista'
        });
} ]);
