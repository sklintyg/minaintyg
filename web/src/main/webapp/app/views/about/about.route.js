angular.module('minaintyg').config(function($stateProvider) {
  'use strict';
  $stateProvider
  .state('omminaintyg', {
    url: '/omminaintyg',
    templateUrl: '/app/views/about/about-mina-intyg-page.html',
    controller: 'minaintyg.AboutCtrl',
    data: {title: 'Om Mina intyg', keepInboxTabActive: false, breadcrumb: ['om']}
  }).state('omminaintyg.info', {
    url: '/info',
    views: {
      'content@omminaintyg': {
        templateUrl: '/app/views/about/section-info.html'
      }
    }
  }).state('omminaintyg.help-info', {
    url: '/help-info',
    views: {
      'content@omminaintyg': {
        templateUrl: '/app/views/about/section-help-and-support.html'
      }
    }
  }).state('omminaintyg.faq', {
    url: '/faq',
    views: {
      'content@omminaintyg': {
        templateUrl: '/app/views/about/faq/faq.html',
        controller: 'AboutFaqPageCtrl'
      }
    }
  }).state('omminaintyg.juridik', {
    url: '/juridik',
    views: {
      'content@omminaintyg': {
        templateUrl: '/app/views/about/section-juridik.html'
      }
    }
  }).state('omminaintyg.personuppgifter-info', {
    url: '/personuppgifter',
    views: {
      'content@omminaintyg': {
        templateUrl: '/app/views/about/section-personuppgifter.html'
      }
    }
  });
});
