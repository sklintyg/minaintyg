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

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3.wsaddressing10.AttributedURIType;
import se.inera.certificate.api.ModuleAPIResponse;
import se.inera.certificate.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v1.GetRecipientsForCertificateResponderInterface;
import se.inera.certificate.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v1.GetRecipientsForCertificateResponseType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v1.GetRecipientsForCertificateType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v1.RecipientType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenResponderInterface;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenResponseType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.sendcertificateforcitizen.v1.SendCertificateForCitizenResponderInterface;
import se.inera.certificate.clinicalprocess.healthcond.certificate.sendcertificateforcitizen.v1.SendCertificateForCitizenResponseType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.sendcertificateforcitizen.v1.SendCertificateForCitizenType;
import se.inera.certificate.exception.ExternalWebServiceCallFailedException;
import se.inera.certificate.exception.ResultTypeErrorException;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.CertificateState;
import se.inera.certificate.model.Status;
import se.inera.certificate.model.common.internal.Utlatande;
import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.inera.certificate.web.service.dto.UtlatandeRecipient;
import se.inera.certificate.web.service.dto.UtlatandeWithMeta;
import se.inera.certificate.web.util.ClinicalProcessMetaConverter;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateStatusType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatearchived.v1.rivtabp20.SetCertificateArchivedResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatearchivedresponder.v1.SetCertificateArchivedRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatearchivedresponder.v1.SetCertificateArchivedResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatus.v1.rivtabp20.SetCertificateStatusResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificateServiceImpl.class);

    /* Mapper to serialize/deserialize Utlatanden. */
    private static ObjectMapper objectMapper = new CustomObjectMapper();

    @Autowired
    private ListCertificatesForCitizenResponderInterface listService;

    @Autowired
    private SetCertificateStatusResponderInterface setStatusService;

    @Autowired
    private SendCertificateForCitizenResponderInterface sendService;

    @Autowired
    private GetCertificateContentResponderInterface getContentService;

    @Autowired
    private GetRecipientsForCertificateResponderInterface getRecipientsService;

    @Autowired
    private SetCertificateArchivedResponderInterface setArchivedService;

    // These values are injected by their setter methods
    private String vardReferensId;
    private String logicalAddress;


    // - - - - - Public methods - - - - - //

    /**
     * NOTE: This implementation only correctly the fields used by the SendMedicalCertificateResponderInterface
     * implementation. (The responserinterface used here now should be replaced with a custom
     * interface for this type of sendCertificate that is initiated by the citizen from MI)
     *
     * @see se.inera.certificate.web.service.CertificateService#sendCertificate(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public ModuleAPIResponse sendCertificate(String civicRegistrationNumber, String certificateId, String recipientId) {
        LOGGER.debug("sendCertificate {} to {}", certificateId, recipientId);

        /*
        UtlatandeWithMeta utlatande = getUtlatande(civicRegistrationNumber, certificateId);
        VardAdresseringsType vardAdresseringsType = ModelConverter.toVardAdresseringsType(utlatande.getUtlatande().getGrundData());

        // Läkarutlatande
        LakarutlatandeEnkelType lakarutlatande = ModelConverter.toLakarutlatandeEnkelType(utlatande.getUtlatande());

        SendType sendType = new SendType();
        sendType.setAdressVard(vardAdresseringsType);
        sendType.setAvsantTidpunkt(new LocalDateTime());
        sendType.setVardReferensId(vardReferensId);
        sendType.setLakarutlatande(lakarutlatande);

        SendMedicalCertificateRequestType req = new SendMedicalCertificateRequestType();
        req.setSend(sendType);

        AttributedURIType uri = new AttributedURIType();
        uri.setValue(logicalAddress);

        //final SendMedicalCertificateResponseType response = sendService.sendMedicalCertificate(uri, req);
        */

        SendCertificateForCitizenType request = new SendCertificateForCitizenType();
        request.setPersonId(civicRegistrationNumber);
        request.setUtlatandeId(certificateId);
        request.setMottagareId(recipientId);

        final SendCertificateForCitizenResponseType response = sendService.sendCertificateForCitizen(logicalAddress, request);

        if (response.getResult().getResultCode().equals(ResultCodeEnum.ERROR)) {
            LOGGER.warn("SendCertificate error: {}", response.getResult().getResultText());
            return new ModuleAPIResponse("error", "");
        } else {
            return new ModuleAPIResponse("sent", "");
        }
    }

    @Override
    public UtlatandeMetaData setCertificateStatus(String civicRegistrationNumber, String id, LocalDateTime timestamp, String recipientId, StatusType type) {
        UtlatandeMetaData result = null;
        SetCertificateStatusRequestType req = new SetCertificateStatusRequestType();
        req.setCertificateId(id);
        req.setNationalIdentityNumber(civicRegistrationNumber);
        req.setStatus(type);
        req.setTarget(recipientId);
        req.setTimestamp(new LocalDateTime(timestamp));

        final SetCertificateStatusResponseType response = setStatusService.setCertificateStatus(null, req);

        if (response.getResult().getResultCode().equals(ResultCodeEnum.OK)) {
            List<UtlatandeMetaData> updatedList = this.getCertificates(civicRegistrationNumber);
            for (UtlatandeMetaData meta : updatedList) {
                if (meta.getId().equals(id)) {
                    result = meta;
                    break;
                }
            }

        }
        return result;
    }

    @Override
    public UtlatandeWithMeta getUtlatande(String civicRegistrationNumber, String certificateId) {

        AttributedURIType uri = new AttributedURIType();
        uri.setValue(logicalAddress);

        GetCertificateContentRequestType request = new GetCertificateContentRequestType();
        request.setCertificateId(certificateId);
        request.setNationalIdentityNumber(civicRegistrationNumber);

        GetCertificateContentResponseType response = getContentService.getCertificateContent(uri, request);

        switch (response.getResult().getResultCode()) {
        case OK:
            return convert(response);
        default:
            LOGGER.error("Failed to fetch utlatande #" + certificateId + " from Intygstjänsten. WS call result is " + response.getResult());
            throw new ExternalWebServiceCallFailedException(response.getResult());
        }
    }

    @Override
    public List<UtlatandeMetaData> getCertificates(String civicRegistrationNumber) {
        final ListCertificatesForCitizenType params = new ListCertificatesForCitizenType();
        params.setPersonId(civicRegistrationNumber);

        ListCertificatesForCitizenResponseType response = listService.listCertificatesForCitizen(null, params);

        switch (response.getResult().getResultCode()) {
        case OK:
            return ClinicalProcessMetaConverter.toUtlatandeMetaData(response.getMeta());
        default:
            LOGGER.error("Failed to fetch cert list for user #" + civicRegistrationNumber + " from Intygstjänsten. WS call result is "
                    + response.getResult());
            throw new ResultTypeErrorException(response.getResult());
        }
    }

    @Override
    public List<UtlatandeRecipient> getRecipientsForCertificate(String certificateType) {
        // Setup request
        GetRecipientsForCertificateType request = new GetRecipientsForCertificateType();
        request.setCertificateType(certificateType);

        // Call service and get recipients
        GetRecipientsForCertificateResponseType response = getRecipientsService.getRecipientsForCertificate(logicalAddress, request);

        switch (response.getResult().getResultCode()) {
        case OK:
            List<UtlatandeRecipient> recipientList = new ArrayList<UtlatandeRecipient>();
            for (RecipientType recipientType : response.getRecipient()) {
                UtlatandeRecipient utlatandeRecipient = new UtlatandeRecipient(recipientType.getId(), recipientType.getName());
                recipientList.add(utlatandeRecipient);
            }
            return recipientList;

        default:
            LOGGER.error("Failed to fetch recipient list for cert type: {} from Intygstjänsten. WS call result is {}", certificateType, response.getResult());
            throw new ResultTypeErrorException(response.getResult());
        }
    }

    @Override
    public UtlatandeMetaData setArchived(String id, String civicRegistrationNumber, String archivedState) {
        SetCertificateArchivedRequestType parameters = new SetCertificateArchivedRequestType();
        parameters.setArchivedState(archivedState);
        parameters.setCertificateId(id);
        parameters.setNationalIdentityNumber(civicRegistrationNumber);

        UtlatandeMetaData result = null;
        SetCertificateArchivedResponseType response = setArchivedService.setCertificateArchived(null, parameters);
        if (response.getResult().getResultCode().equals(ResultCodeEnum.OK)) {
            List<UtlatandeMetaData> updatedList = this.getCertificates(civicRegistrationNumber);
            for (UtlatandeMetaData meta : updatedList) {
                if (meta.getId().equals(id)) {
                    meta.setAvailable(archivedState.equalsIgnoreCase("true") ? "false" : "true");
                    result = meta;
                    break;
                }
            }
        }

        return result;
    }


    // - - - - - Package methods - - - - - //

    @Value("${application.ID}")
    void setVardReferensId(final String vardReferensId) {
        this.vardReferensId = vardReferensId;
    }

    @Value("${intygstjanst.logicaladdress}")
    void setLogicalAddress(final String logicalAddress) {
        this.logicalAddress = logicalAddress;
    }


    // - - - - - Private methods - - - - - //

    private UtlatandeWithMeta convert(final GetCertificateContentResponseType response) {
        Utlatande utlatande;
        String document = response.getCertificate();

        try {
            utlatande = objectMapper.readValue(document, Utlatande.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<Status> statuses = new ArrayList<Status>();
        for (CertificateStatusType status : response.getStatuses()) {
            if (status.getType().equals(StatusType.SENT) || status.getType().equals(StatusType.CANCELLED)) {
                statuses.add(new Status(CertificateState.valueOf(status.getType().name()), status.getTarget(),
                        status.getTimestamp()));
            }
        }
        return new UtlatandeWithMeta(utlatande, document, statuses);
    }

}
