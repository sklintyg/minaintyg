angular.module('showcase').controller('showcase.intygCtrl',
    ['$scope', 'common.messageService',
        function($scope, messageService) {
            'use strict';

            $scope.cert = {
                'id':'1a0ddc45-0972-4f10-81ee-9aced504d370',
                'grundData':{
                    'signeringsdatum':'2016-06-15T13:40:59.000',
                    'skapadAv':{'personId':'IFV1239877878-1049','fullstandigtNamn':'Jan Nilsson','forskrivarKod':'0000000','befattningar':[],'specialiteter':['Kod1'],
                    'vardenhet':{'enhetsid':'IFV1239877878-1042','enhetsnamn':'WebCert-Enhet1','postadress':'Storgatan 1','postnummer':'12345','postort':'Småmåla','telefonnummer':'0101234567890','epost':'enhet1@webcert.invalid.se',
                    'vardgivare':{'vardgivarid':'IFV1239877878-1041','vardgivarnamn':'WebCert-Vårdgivare1'},'arbetsplatsKod':'1234567890'}},
                    'patient':{'personId':'191212121212','fullstandigtNamn':'Tolvan Tolvansson','fornamn':'Tolvan','efternamn':'Tolvansson','postadress':'Svensson, Storgatan 1, PL 1234','postnummer':'12345','postort':'Småmåla','samordningsNummer':false}
                },
                'typ':'luse',
                'meta':{'cancelled':false,'statuses':[{'type':'SENT','target':'FK','timestamp':'2016-06-15T13:41:59.000'}]}
            };
            
        }]);
