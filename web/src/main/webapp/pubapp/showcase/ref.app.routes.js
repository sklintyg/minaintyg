
angular.module('showcase').config(function($stateProvider, $urlRouterProvider, $httpProvider) {
    'use strict';

 var templateRoot = '/pubapp/showcase/'
    $stateProvider.

    state('showcase', {
        views: {
            'header@': {
                templateUrl: templateRoot +'header.html'
            }
        }
    }).

    state('showcase.bootstrap', {
        url: '/bootstrap-components',
        views: {
            'content@': {
                templateUrl: templateRoot + 'views/bootstrap.html',
                controller: 'showcase.BootstrapCtrl'
            }
        }
    }).
    state('showcase.navigation', {
        url: '/navigation',
        views: {
            'content@': {
                templateUrl: templateRoot + 'views/navigation.html',
                controller: 'showcase.navigationCtrl'
            }
        }
    }).
    state('showcase.intyg', {
        url: '/intyg',
        views: {
            'content@': {
                templateUrl: templateRoot + 'views/intyg.html',
                controller: 'showcase.intygCtrl'
            }
        }
    });


    $urlRouterProvider.when('', '/bootstrap-components');

});
