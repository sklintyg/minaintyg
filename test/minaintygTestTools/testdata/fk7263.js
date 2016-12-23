/*globals JSON*/
'use strict';

var fs = require('fs');
var path = require('path');
var xml = require('simple-xml-dom')

var helpers = require('./../helpers/helpers.js');

var templateJsonObj = require('./fk7263-metadata.json');

module.exports = {

    getIntyg: function(userId) {
        //generate a new GUID as certificate id
        templateJsonObj.id = helpers.testdata.generateTestGuid();
        templateJsonObj.civicRegistrationNumber = userId || '191212121212';

        //cwd is expected to be minaintyg/test
        var fullPath = path.join(process.cwd(), 'minaintygTestTools/testdata/fk7263-content.xml');

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
            type: 'fk7263',
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


// Private functions

var xmlToString = function(xmlData) {
    // Whitespace collapsed
    return xml.serialize(xml.parse(xmlData))
};

