/*globals JSON*/
'use strict';

var testdataHelper = require('./../helpers/testdataHelper.js');

var templateJsonObjLuaefs = require('./luae_fs.json');

module.exports = {

    getIntyg: function() {

        templateJsonObjLuaefs.id = testdataHelper.generateTestGuid();

        return {
            id: templateJsonObjLuaefs.id,
            document: JSON.stringify(templateJsonObjLuaefs),
            originalCertificate: '',
            type: templateJsonObjLuaefs.typ,
            signingDoctorName: templateJsonObjLuaefs.grundData.skapadAv.fullstandigtNamn,
            careUnitId: templateJsonObjLuaefs.grundData.skapadAv.vardenhet.enhetsid,
            careUnitName: templateJsonObjLuaefs.grundData.skapadAv.vardenhet.enhetsnamn,
            careGiverId: templateJsonObjLuaefs.grundData.skapadAv.vardenhet.vardgivare.vardgivarid,
            civicRegistrationNumber: templateJsonObjLuaefs.grundData.patient.personId,
            signedDate: templateJsonObjLuaefs.grundData.signeringsdatum,
            validFromDate: null,
            validToDate: null,
            additionalInfo: '',
            deleted: false,
            deletedByCareGiver: false,
            certificateStates: [
                {
                    target: 'HV',
                    state: 'RECEIVED',
                    timestamp: '2016-05-26T10:00:00.000'
                }
            ],
            revoked: false
        };
    }

};
