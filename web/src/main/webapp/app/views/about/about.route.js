angular.module('minaintyg')
    .config(function($stateProvider) {
        'use strict';
        $stateProvider
            .state('omminaintyg', {
                url: '/omminaintyg',
                templateUrl: '/app/views/about/about-mina-intyg-page.html',
                controller: 'minaintyg.AboutCtrl',
                data:{title: 'Om Mina intyg', keepInboxTabActive: false,
                    breadcrumb: ['om']}
            })
            .state('omminaintyg.info', {
                url: '/info',
                views: {
                    'content@omminaintyg': {
                        templateUrl: '/app/views/about/section-info.html'
                    }
                }
            })
            .state('omminaintyg.samtycke', {
                url: '/samtycke',
                views: {
                    'content@omminaintyg': {
                        templateUrl: '/app/views/about/section-samtycke.html'
                    }
                },
            })
            .state('omminaintyg.juridik', {
                url: '/juridik',
                views: {
                    'content@omminaintyg': {
                        templateUrl: '/app/views/about/section-juridik.html'
                    }
                }
            });
    });
