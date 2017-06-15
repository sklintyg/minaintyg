/*globals JSON*/
'use strict';

var fs = require('fs');
var path = require('path');
var xml = require('simple-xml-dom')

var helpers = require('./../helpers/helpers.js');

var templateJsonObjTemplate = require('./generic-metadata.json');

module.exports = {

    getFk7263: function(userId, signDate) {
        return this.getIntyg('fk7263', 'fk7263', userId, signDate);
    },

    getFk7263Smittskydd: function(userId, signDate) {
        return this.getIntyg('fk7263', 'fk7263-smittskydd', userId, signDate);
    },

    getTsBas: function(userId, signDate) {
        return this.getIntyg('ts-bas','ts-bas', userId, signDate);
    },

    getTsDiabetes: function(userId, signDate) {
        return this.getIntyg('ts-diabetes', 'ts-diabetes', userId, signDate);
    },

    getLuse: function(userId) {
        return this.getIntyg('luse', 'luse', userId);
    },

    getLisjp: function(userId) {
        return this.getIntyg('lisjp', 'lisjp', userId);
    },

    getLuaena: function(userId) {
        return this.getIntyg('luae_na', 'luae_na', userId);
    },

    getLuaefs: function(userId) {
        return this.getIntyg('luae_fs', 'luae_fs', userId);
    },

    getLisjpSmittskydd: function(userId) {
        return this.getIntyg('lisjp', 'lisjp-smittskydd', userId);
    },

    getLisjpFull: function(userId) {
        return this.getIntyg('lisjp', 'lisjp-full', userId);
    },

    getLisjpSmittskyddOvrigt: function(userId) {
        return this.getIntyg('lisjp', 'lisjp-smittskydd-ovrigt', userId);
    },

    getIntyg: function(type, file, userId, signedDate) {

        //Create a local copy that we are free to mutate
        var templateJsonObj = JSON.parse(JSON.stringify(templateJsonObjTemplate));
        //generate a new GUID as certificate id
        templateJsonObj.id = helpers.testdata.generateTestGuid();
        templateJsonObj.civicRegistrationNumber = userId || '191212121212';
        templateJsonObj.signedDate = signedDate || templateJsonObj.signedDate;
        var fullPath = path.join(process.cwd(), 'minaintygTestTools/testdata/intyg-' + file + '-content.xml');

        //read xml doc into string variable
        var xmlString = fs.readFileSync(fullPath, 'utf8')
            .replace(/\r?\n|\r/g, '')
            .replace(/>\s+</g, '><')
            .replace("CERTIFICATE_ID", templateJsonObj.id)
            .replace("SIGNED_DATE", templateJsonObj.signedDate)
            .replace("SENT_DATE", templateJsonObj.sentDate)
            .replace("PATIENT_CRN", templateJsonObj.civicRegistrationNumber)
            .replace("DOCTOR_NAME", templateJsonObj.signingDoctorName)
            .replace("CAREUNIT_ID", templateJsonObj.careUnitId)
            .replace("CAREUNIT_NAME", templateJsonObj.careUnitName)
            .replace("CAREGIVER_ID", templateJsonObj.careGiverId)
            .replace("CAREGIVER_NAME", templateJsonObj.careGiverName);

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
            additionalInfo: '',
            originalCertificate: xmlToString(xmlString)
        };
    }

};

var xmlToString = function(xmlData) {
    // Whitespace collapsed
    return xml.serialize(xml.parse(xmlData));
};
