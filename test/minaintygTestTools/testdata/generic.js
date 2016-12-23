/*globals JSON*/
'use strict';

var fs = require('fs');
var path = require('path');
var xml = require('simple-xml-dom')

var helpers = require('./../helpers/helpers.js');

var templateJsonObj = require('./generic-metadata.json');

module.exports = {

    getFk7263: function(userId) {
        return this.getIntyg('fk7263', userId);
    },

    getTsBas: function(userId) {
        return this.getIntyg('ts-bas', userId);
    },

    getTsDiabetes: function(userId) {
        return this.getIntyg('ts-diabetes', userId);
    },

    getLuse: function(userId) {
        return this.getIntyg('luse', userId);
    },

    getLisjp: function(userId) {
        return this.getIntyg('lisjp', userId);
    },

    getLuaena: function(userId) {
        return this.getIntyg('luae_na', userId);
    },

    getLuaefs: function(userId) {
        return this.getIntyg('luae_fs', userId);
    },

    getIntyg: function(type, userId) {
        //generate a new GUID as certificate id
        templateJsonObj.id = helpers.testdata.generateTestGuid();
        templateJsonObj.civicRegistrationNumber = userId || '191212121212';

        return {
            id: templateJsonObj.id,
            type: type,
            civicRegistrationNumber: templateJsonObj.civicRegistrationNumber,
            signedDate: templateJsonObj.signedDate,
            signingDoctorName: templateJsonObj.signingDoctorName,
            careUnitId: templateJsonObj.careUnitId,
            careUnitName: templateJsonObj.careUnitName,
            careGiverId: templateJsonObj.careGiverId,
            careGiverName: templateJsonObj.careGiverName,
            validFromDate: null,
            validToDate: null,
            certificateStates: [
                {
                    target: templateJsonObj.states[0].target,
                    state: templateJsonObj.states[0].state,
                    timestamp: templateJsonObj.states[0].timestamp
                }
            ],
            deleted: false,
            deletedByCareGiver: false,
            revoked: false,
            additionalInfo: ''
        };
    }

};
