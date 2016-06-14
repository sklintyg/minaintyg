/*globals JSON*/
'use strict';

var fs = require('fs');
var path = require('path');
var xml = require('simple-xml-dom')

var helpers = require('./../helpers/helpers.js');

var templateJsonObjLuaefs = require('./luae_fs-metadata.json');

module.exports = {

    getIntyg: function() {
        //generate a new GUID as certificate id
        templateJsonObjLuaefs.id = helpers.testdata.generateTestGuid();

        //cwd is expected to be minaintyg/test
        var fullPath = path.join(process.cwd(), 'minaintygTestTools/testdata/luae_fs-content.xml');

        //read xml doc into string variable
        var xmlString = fs.readFileSync(fullPath, 'utf8')
            .replace(/\r?\n|\r/g, '')
            .replace(/>\s+</g, '><')
            .replace("CERTIFICATE_ID", templateJsonObjLuaefs.id)
            .replace("SIGNED_DATE", templateJsonObjLuaefs.signedDate)
            .replace("SENT_DATE", templateJsonObjLuaefs.sentDate)
            .replace("PATIENT_CRN", templateJsonObjLuaefs.civicRegistrationNumber)
            .replace("DOCTOR_NAME", templateJsonObjLuaefs.signingDoctorName)
            .replace("CAREUNIT_ID", templateJsonObjLuaefs.careUnitId)
            .replace("CAREUNIT_NAME", templateJsonObjLuaefs.careUnitName)
            .replace("CAREGIVER_ID", templateJsonObjLuaefs.careGiverId)
            .replace("CAREGIVER_NAME", templateJsonObjLuaefs.careGiverName)
            .replace("UNDERSOKNING_DATE", templateJsonObjLuaefs.undersokningAvPatienten)
            .replace("JOURNALUPPGIFTER_DATE", templateJsonObjLuaefs.journaluppgifter)
            .replace("ANHORIG_DATE", templateJsonObjLuaefs.anhorigsBeskrivningAvPatienten)
            .replace("ANNAT_DATE", templateJsonObjLuaefs.annatGrundForMU)
            .replace("ANNAT_BESKRIVNING", templateJsonObjLuaefs.annatGrundForMUBeskrivning)
            .replace("KANNEDOMPATIENT_DATE", templateJsonObjLuaefs.kannedomOmPatient);

        return {
            id: templateJsonObjLuaefs.id,
            type: 'luae_fs',
            signedDate: templateJsonObjLuaefs.signedDate,
            signingDoctorName: templateJsonObjLuaefs.signingDoctorName,
            careUnitId: templateJsonObjLuaefs.careUnitId,
            careUnitName: templateJsonObjLuaefs.careUnitName,
            careGiverId: templateJsonObjLuaefs.careGiverId,
            careGiverName: templateJsonObjLuaefs.careGiverName,
            civicRegistrationNumber: templateJsonObjLuaefs.civicRegistrationNumber,
            validFromDate: null,
            validToDate: null,
            certificateStates: [
                {
                    target: templateJsonObjLuaefs.states[0].target,
                    state: templateJsonObjLuaefs.states[0].state,
                    timestamp: templateJsonObjLuaefs.states[0].timestamp
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

