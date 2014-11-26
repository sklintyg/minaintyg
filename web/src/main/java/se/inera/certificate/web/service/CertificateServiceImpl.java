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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import se.inera.certificate.exception.ExternalWebServiceCallFailedException;
import se.inera.certificate.exception.ResultTypeErrorException;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.integration.module.exception.CertificateAlreadyExistsException;
import se.inera.certificate.integration.module.exception.CertificateRevokedException;
import se.inera.certificate.integration.module.exception.InvalidCertificateException;
import se.inera.certificate.model.common.internal.Utlatande;
import se.inera.certificate.modules.support.api.CertificateHolder;
import se.inera.certificate.modules.support.api.ModuleContainerApi;
import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.inera.certificate.web.service.dto.UtlatandeRecipient;
import se.inera.certificate.web.service.dto.UtlatandeStatusType;
import se.inera.certificate.web.service.dto.UtlatandeWithMeta;
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
import se.inera.ifv.insuranceprocess.healthreporting.util.ModelConverter;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.webcert.medcertqa.v1.LakarutlatandeEnkelType;
import se.inera.webcert.medcertqa.v1.VardAdresseringsType;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CertificateServiceImpl implements CertificateService, ModuleContainerApi {

    private static final Logger LOG = LoggerFactory.getLogger(CertificateServiceImpl.class);

    @Autowired
    private ListCertificatesForCitizenResponderInterface listService;

    @Autowired
    private SetCertificateStatusResponderInterface statusService;

    @Autowired
    private SendMedicalCertificateResponderInterface sendService;

    @Autowired
    private GetCertificateContentResponderInterface getCertificateContentService;
    
    @Autowired
    private GetRecipientsForCertificateResponderInterface getRecipientsForCertificateService;

    /**
     * Mapper to serialize/deserialize Utlatanden.
     */
    private static ObjectMapper objectMapper = new CustomObjectMapper();

    /**
     * NOTE: This implementation only correctly the fields used by the SendMedicalCertificateResponderInterface
     * implementation. (The responserinterface used here now should be replaced with a custom
     * interface for this type of sendCertificate that is initiated by the citizen from MI)
     * 
     * @see se.inera.certificate.web.service.CertificateService#sendCertificate(java.lang.String, java.lang.String,
     *      java.lang.String)
     */
    @Override
    public ModuleAPIResponse sendCertificate(String civicRegistrationNumber, String id, String target) {
        LOG.debug("sendCertificate {} to {}", id, target);
        SendMedicalCertificateRequestType req = new SendMedicalCertificateRequestType();

        UtlatandeWithMeta utlatande = getUtlatande(civicRegistrationNumber, id);

        SendType sendType = new SendType();
        VardAdresseringsType vardAdresseringsType = ModelConverter.toVardAdresseringsType(utlatande.getUtlatande().getGrundData());

        sendType.setAdressVard(vardAdresseringsType);
        sendType.setAvsantTidpunkt(new LocalDateTime());
        sendType.setVardReferensId("MI");

        // Lakarutlatande
        LakarutlatandeEnkelType lakarutlatande = ModelConverter.toLakarutlatandeEnkelType(utlatande.getUtlatande());

        sendType.setLakarutlatande(lakarutlatande);
        req.setSend(sendType);

        AttributedURIType uri = new AttributedURIType();
        uri.setValue(target);

        final SendMedicalCertificateResponseType response = sendService.sendMedicalCertificate(uri, req);
        if (response.getResult().getResultCode().equals(ResultCodeEnum.ERROR)) {
            LOG.warn("SendCertificate error: {}", response.getResult().getErrorText());
            return new ModuleAPIResponse("error", "");
        } else {
            return new ModuleAPIResponse("sent", "");
        }
    }

    @Override
    public UtlatandeMetaData setCertificateStatus(String civicRegistrationNumber, String id, LocalDateTime timestamp, String target, StatusType type) {
        UtlatandeMetaData result = null;
        SetCertificateStatusRequestType req = new SetCertificateStatusRequestType();
        req.setCertificateId(id);
        req.setNationalIdentityNumber(civicRegistrationNumber);
        req.setStatus(type);
        req.setTarget(target);

        req.setTimestamp(new LocalDateTime(timestamp));

        final SetCertificateStatusResponseType response = statusService.setCertificateStatus(null, req);
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
        GetCertificateContentRequestType request = new GetCertificateContentRequestType();
        request.setCertificateId(certificateId);
        request.setNationalIdentityNumber(civicRegistrationNumber);

        GetCertificateContentResponseType response = getCertificateContentService.getCertificateContent(null, request);

        switch (response.getResult().getResultCode()) {
        case OK:
            return convert(response);
        default:
            LOG.error("Failed to fetch utlatande #" + certificateId + " from Intygstjänsten. WS call result is " + response.getResult());
            throw new ExternalWebServiceCallFailedException(response.getResult());
        }
    }

    private UtlatandeWithMeta convert(final GetCertificateContentResponseType response) {
        Utlatande utlatande;
        String document = response.getCertificate();

        try {
            utlatande = objectMapper.readValue(document, Utlatande.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<UtlatandeStatusType> statuses = new ArrayList<UtlatandeStatusType>();
        for (CertificateStatusType status : response.getStatuses()) {
            if (status.getType().equals(StatusType.SENT) || status.getType().equals(StatusType.CANCELLED)) {
                statuses.add(new UtlatandeStatusType(se.inera.certificate.web.service.dto.UtlatandeStatusType.StatusType.valueOf(status.getType().name()),
                        status.getTarget(), status.getTimestamp()));
            }
        }
        return new UtlatandeWithMeta(utlatande, document, statuses);
    }

    @Override
    public List<UtlatandeMetaData> getCertificates(String civicRegistrationNumber) {
        final ListCertificatesForCitizenType params = new ListCertificatesForCitizenType();
        params.setNationalIdentityNumber(civicRegistrationNumber);

        ListCertificatesForCitizenResponseType response = listService.listCertificatesForCitizen(null, params);

        switch (response.getResult().getResultCode()) {
        case OK:
            return ClinicalProcessMetaConverter.toUtlatandeMetaData(response.getMeta());
        default:
            LOG.error("Failed to fetch cert list for user #" + civicRegistrationNumber + " from Intygstjänsten. WS call result is "
                    + response.getResult());
            throw new ResultTypeErrorException(response.getResult());
        }
    }

    @Override 
    public List<UtlatandeRecipient> getRecipientsForCertificate(String type) {
        GetRecipientsForCertificateType params = new GetRecipientsForCertificateType();
        params.setCertificateType(type);

        GetRecipientsForCertificateResponseType response = getRecipientsForCertificateService.getRecipientsForCertificate(null,params);
        switch (response.getResult().getResultCode()) {
        case OK:
            List<UtlatandeRecipient> recipientList = new ArrayList<UtlatandeRecipient>();
            for (RecipientType recipientType : response.getRecipient()) {
                UtlatandeRecipient utlatandeRecipient = new UtlatandeRecipient(recipientType.getId(), recipientType.getName());
                recipientList.add(utlatandeRecipient);
            }
            return recipientList;

        default:
            LOG.error("Failed to fetch recipient list for cert type: " + type + " from Intygstjänsten. WS call result is "
                    + response.getResult());
            throw new ResultTypeErrorException(response.getResult());
        }
    }

    @Override
    public void certificateReceived(CertificateHolder certificate, boolean wireTapped) throws CertificateAlreadyExistsException,
            InvalidCertificateException {
        throw new RuntimeException("Not implemented in this context");
    }

    @Override
    public CertificateHolder getCertificate(String certificateId, String personId) throws InvalidCertificateException, CertificateRevokedException {
        throw new RuntimeException("Not implemented in this context");
    }
}
