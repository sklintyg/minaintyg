/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate Web (http://code.google.com/p/inera-certificate-web).
 *
 * Inera Certificate Web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * Inera Certificate Web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.web.service;

import iso.v21090.dt.v1.II;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import riv.insuranceprocess.healthreporting.medcertqa._1.LakarutlatandeEnkelType;
import riv.insuranceprocess.healthreporting.medcertqa._1.VardAdresseringsType;
import se.inera.certificate.api.CertificateMeta;
import se.inera.certificate.api.ModuleAPIResponse;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenResponderInterface;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenResponseType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenType;
import se.inera.certificate.integration.exception.ExternalWebServiceCallFailedException;
import se.inera.certificate.integration.exception.ResultTypeErrorException;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.integration.module.dto.CertificateContentHolder;
import se.inera.certificate.integration.module.dto.CertificateContentMeta;
import se.inera.certificate.integration.module.dto.CertificateStatus;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.model.common.MinimalUtlatande;
import se.inera.certificate.web.util.ClinicalProcessMetaConverter;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateStatusType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificate.v1.rivtabp20.SendMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendMedicalCertificateResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.sendmedicalcertificateresponder.v1.SendType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatus.v1.rivtabp20.SetCertificateStatusResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final Logger LOG = LoggerFactory.getLogger(CertificateServiceImpl.class);

    private static final String PATIENT_ID_OID = "1.2.752.129.2.1.3.1";
    private static final String HOS_PERSONAL_OID = "1.2.752.129.2.1.4.1";
    private static final String ENHET_OID = "1.2.752.129.2.1.4.1";
    private static final String ARBETSPLATS_CODE_OID = "1.2.752.29.4.71";

    @Autowired
    private ListCertificatesForCitizenResponderInterface listService;

    @Autowired
    private SetCertificateStatusResponderInterface statusService;

    @Autowired
    private SendMedicalCertificateResponderInterface sendService;

    @Autowired
    private GetCertificateContentResponderInterface getCertificateContentService;

    /**
     * Mapper to serialize/deserialize Utlatanden
     */
    private static ObjectMapper objectMapper = new CustomObjectMapper();

    /**
     * NOTE: This implementation only correctly the fields used by the SendMedicalCertificateResponderInterface implementation. (The responserinterface used here now should be replaced with a custom
     * interface for this type of sendCertificate that is initiated by the citizen from MI)
     * @see se.inera.certificate.web.service.CertificateService#sendCertificate(java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public ModuleAPIResponse sendCertificate(String civicRegistrationNumber, String id, String target) {
        LOG.debug("sendCertificate {} to {}", id, target);
        SendMedicalCertificateRequestType req = new SendMedicalCertificateRequestType();

        SendType sendType = new SendType();
        VardAdresseringsType vardAdresseringsType = new VardAdresseringsType();
        HosPersonalType hosPersonal = new HosPersonalType();

        // Enhet
        EnhetType enhet = new EnhetType();
        enhet.setEnhetsnamn("enhetsnamn");

        II enhetsId = new II();
        enhetsId.setRoot(ENHET_OID);
        enhetsId.setExtension("enhetsid");
        enhet.setEnhetsId(enhetsId);

        II arbetsplatsKod = new II();
        arbetsplatsKod.setRoot(ARBETSPLATS_CODE_OID);
        arbetsplatsKod.setExtension("arbetsplatskod");
        enhet.setArbetsplatskod(arbetsplatsKod);

        VardgivareType vardGivare = new VardgivareType();

        II vardGivarId = new II();
        vardGivarId.setRoot(ENHET_OID);
        vardGivarId.setExtension("vardgivarid");
        vardGivare.setVardgivareId(vardGivarId);

        vardGivare.setVardgivarnamn("MI");
        enhet.setVardgivare(vardGivare);

        hosPersonal.setEnhet(enhet);
        hosPersonal.setFullstandigtNamn("MI");

        II personalId = new II();
        personalId.setRoot(HOS_PERSONAL_OID);
        personalId.setExtension("MI");
        hosPersonal.setPersonalId(personalId);

        vardAdresseringsType.setHosPersonal(hosPersonal);

        sendType.setAdressVard(vardAdresseringsType);
        sendType.setAvsantTidpunkt(new LocalDateTime());
        sendType.setVardReferensId("MI");

        // Lakarutlatande
        LakarutlatandeEnkelType lakarutlatande = new LakarutlatandeEnkelType();

        lakarutlatande.setLakarutlatandeId(id);
        lakarutlatande.setSigneringsTidpunkt(new LocalDateTime());
        PatientType patient = new PatientType();
        II patientIdHolder = new II();
        patientIdHolder.setRoot(PATIENT_ID_OID);
        patientIdHolder.setExtension(civicRegistrationNumber);
        patient.setPersonId(patientIdHolder);
        patient.setFullstandigtNamn("patientnamn");
        lakarutlatande.setPatient(patient);

        sendType.setLakarutlatande(lakarutlatande);
        req.setSend(sendType);
        final SendMedicalCertificateResponseType response = sendService.sendMedicalCertificate(null, req);
        if (response.getResult().getResultCode().equals(ResultCodeEnum.ERROR)) {
            LOG.warn("SendCertificate error: {}",response.getResult().getErrorText()  );
            return new ModuleAPIResponse("error", "");
        } else {
            return new ModuleAPIResponse("sent", "");
        }
    }

    @Override
    public CertificateMeta setCertificateStatus(String civicRegistrationNumber, String id, LocalDateTime timestamp, String target, StatusType type) {
        CertificateMeta result = null;
        SetCertificateStatusRequestType req = new SetCertificateStatusRequestType();
        req.setCertificateId(id);
        req.setNationalIdentityNumber(civicRegistrationNumber);
        req.setStatus(type);
        req.setTarget(target);

        req.setTimestamp(new LocalDateTime(timestamp));

        final SetCertificateStatusResponseType response = statusService.setCertificateStatus(null, req);
        if (response.getResult().getResultCode().equals(ResultCodeEnum.OK)) {
            List<CertificateMeta> updatedList = this.getCertificates(civicRegistrationNumber);
            for (CertificateMeta meta : updatedList) {
                if (meta.getId().equals(id)) {
                    result = meta;
                    break;
                }
            }

        }
        return result;
    }

    @Override
    public CertificateContentHolder getUtlatande(String civicRegistrationNumber, String certificateId) {
        GetCertificateContentRequestType request = new GetCertificateContentRequestType();
        request.setCertificateId(certificateId);
        request.setNationalIdentityNumber(civicRegistrationNumber);

        GetCertificateContentResponseType response = getCertificateContentService.getCertificateContent(null, request);

        switch (response.getResult().getResultCode()) {
        case OK:
            return convert(response);
        default: {
            LOG.error("Failed to fetch utlatande #" + certificateId + " from Intygstjänsten. WS call result is " + response.getResult());
            throw new ExternalWebServiceCallFailedException(response.getResult());
        }
        }
    }

    private CertificateContentHolder convert(final GetCertificateContentResponseType response) {

        CertificateContentHolder getCertificateContentHolder = new CertificateContentHolder();
        getCertificateContentHolder.setCertificateContent(response.getCertificate());
        List<CertificateStatusType> statuses = response.getStatuses();

        CertificateContentMeta contentMeta = new CertificateContentMeta();
        contentMeta.setStatuses(convertToCertificateStatus(statuses));

        //Deserialize certificate Json to get common properties
        Utlatande commonUtlatande;
        try {
            commonUtlatande = objectMapper.readValue(getCertificateContentHolder.getCertificateContent(), MinimalUtlatande.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        // Set metadata based on common properties found on any certificate (regardless of type).
        contentMeta.setId(commonUtlatande.getId().getExtension());
        contentMeta.setType(commonUtlatande.getTyp().getCode().toLowerCase());
        contentMeta.setPatientId(commonUtlatande.getPatient().getId().getExtension());
        contentMeta.setFromDate(commonUtlatande.getValidFromDate());
        contentMeta.setTomDate(commonUtlatande.getValidToDate());

        getCertificateContentHolder.setCertificateContentMeta(contentMeta);

        return getCertificateContentHolder;
    }

    public List<CertificateMeta> getCertificates(String civicRegistrationNumber) {
        final ListCertificatesForCitizenType params = new ListCertificatesForCitizenType();
        params.setNationalIdentityNumber(civicRegistrationNumber);

        ListCertificatesForCitizenResponseType response = listService.listCertificatesForCitizen(null, params);

        switch (response.getResult().getResultCode()) {
        case OK:
            return ClinicalProcessMetaConverter.toCertificateMeta(response.getMeta());
        default:
            LOG.error("Failed to fetch cert list for user #" + civicRegistrationNumber + " from Intygstjänsten. WS call result is "
                    + response.getResult());
            throw new ResultTypeErrorException(response.getResult());
        }
    }

    protected List<CertificateStatus> convertToCertificateStatus(List<CertificateStatusType> sourceList) {
        List<CertificateStatus> statusList = new ArrayList<>();
        if (sourceList != null) {
            for (CertificateStatusType stat : sourceList) {
                if (stat.getType().equals(StatusType.SENT) || stat.getType().equals(StatusType.CANCELLED)) {
                    CertificateStatus status = new CertificateStatus();
                    status.setTarget(stat.getTarget());
                    status.setTimestamp(stat.getTimestamp());
                    status.setType(stat.getType().toString());
                    statusList.add(status);
                }
            }
        }
        return statusList;
    }
}
