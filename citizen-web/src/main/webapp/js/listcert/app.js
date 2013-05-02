'use strict';

/* App Module */

var listCertApp = angular.module('ListCertApp', []).config([ '$routeProvider', function($routeProvider) {
    $routeProvider.when('/lista', {
        templateUrl : '/partials/cert-list.html',
        controller : 'ListCertCtrl'
    }).when('/arkiverade', {
        templateUrl : '/partials/cert-list-archived.html',
        controller : 'ListArchivedCertCtrl'
    }).otherwise({
        redirectTo : '/lista'
    });
} ]);
