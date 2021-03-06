/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*globals JSON*/
'use strict';

var fs = require('fs');
var path = require('path');
var xml = require('simple-xml-dom')

var helpers = require('./../helpers/helpers.js');

var templateJsonObjTemplate = require('./generic-metadata.json');

module.exports = {

  getFk7263: function(userId, signDate) {
    return this.getIntyg('fk7263', '1.0', 'fk7263', userId, signDate);
  },

  getFk7263Smittskydd: function(userId, signDate) {
    return this.getIntyg('fk7263', '1.0', 'fk7263-smittskydd', userId, signDate);
  },

  getTsBas: function(userId, signDate) {
    return this.getIntyg('ts-bas', '6.8', 'ts-bas', userId, signDate);
  },

  getTsDiabetes: function(userId, version) {
    version = version || '2.8';
    return this.getIntyg('ts-diabetes', version, 'ts-diabetes', userId);
  },

  getTstrk1062: function(userId, version) {
    return this.getIntyg('tstrk1062', version, 'tstrk1062', userId);
  },

  getLuse: function(userId) {
    return this.getIntyg('luse', '1.0', 'luse', userId);
  },

  getLisjp: function(userId) {
    return this.getIntyg('lisjp', '1.0', 'lisjp', userId);
  },

  getLuaena: function(userId) {
    return this.getIntyg('luae_na', '1.0', 'luae_na', userId);
  },

  getLuaefs: function(userId) {
    return this.getIntyg('luae_fs', '1.0', 'luae_fs', userId);
  },

  getLisjpSmittskydd: function(userId) {
    return this.getIntyg('lisjp', '1.0', 'lisjp-smittskydd', userId);
  },

  getLisjpFull: function(userId) {
    return this.getIntyg('lisjp', '1.0', 'lisjp-full', userId);
  },

  getLisjpSmittskyddOvrigt: function(userId) {
    return this.getIntyg('lisjp', '1.0', 'lisjp-smittskydd-ovrigt', userId);
  },

  getAg114: function(userId) {
    return this.getIntyg('ag114', '1.0', 'ag114-full', userId);
  },
  getAF00251: function(userId) {
    return this.getIntyg('af00251', '1.0', 'af00251-full', userId);
  },

  getAg7804: function(userId) {
    return this.getIntyg('ag7804', '1.0', 'ag7804-full', userId);
  },

  getAg7804Smittskydd: function(userId) {
    return this.getIntyg('ag7804', '1.0', 'ag7804-smittskydd', userId);
  },

  getIntyg: function(type, typeVersion, file, userId, signedDate) {

    //Create a local copy that we are free to mutate
    var templateJsonObj = JSON.parse(JSON.stringify(templateJsonObjTemplate));

    //generate a new GUID as certificate id
    templateJsonObj.id = helpers.testdata.generateTestGuid();
    templateJsonObj.civicRegistrationNumber = userId || '191212121212';
    templateJsonObj.signedDate = signedDate || templateJsonObj.signedDate;

    var fullPath = path.join(process.cwd(), 'minaintygTestTools/testdata/intyg-' + file + '-' + typeVersion + '-content.xml');

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
      typeVersion: typeVersion,
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
