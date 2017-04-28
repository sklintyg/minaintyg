angular.module('minaintyg').config(function($stateProvider) {
    'use strict';
    $stateProvider.state('help', {
        url: '/hjalp',
        templateUrl: '/app/views/help/help-page.html',
        controller: 'minaintyg.HelpCtrl',
        data:{title: 'Hjälp', keepInboxTabActive: false,
            breadcrumb: ['hjalp']}
    }).state('help.info', {
        url: '/info',
        views: {
            'content@help': {
                templateUrl: '/app/views/help/section-helpinfo.html'
            }
        }
    }).state('help.faq', {
        url: '/faq',
        views: {
            'content@help': {
                templateUrl: '/app/views/help/section-faq.html'
            }
        }
    });
});
