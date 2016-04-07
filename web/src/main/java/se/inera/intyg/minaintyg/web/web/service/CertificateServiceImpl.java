/*
 * Copyright (C) 2016 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.web.service;

import java.util.*;

import javax.xml.ws.soap.SOAPFaultException;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3.wsaddressing10.AttributedURIType;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.ifv.insuranceprocess.certificate.v1.CertificateStatusType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatus.rivtabp20.v1.SetCertificateStatusResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v1.*;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.*;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.*;
import se.inera.intyg.common.support.model.common.internal.Utlatande;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.insuranceprocess.healthreporting.getcertificatecontent.rivtabp20.v1.GetCertificateContentResponderInterface;
import se.inera.intyg.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentRequestType;
import se.inera.intyg.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentResponseType;
import se.inera.intyg.insuranceprocess.healthreporting.setcertificatearchived.rivtabp20.v1.SetCertificateArchivedResponderInterface;
import se.inera.intyg.insuranceprocess.healthreporting.setcertificatearchivedresponder.v1.SetCertificateArchivedRequestType;
import se.inera.intyg.insuranceprocess.healthreporting.setcertificatearchivedresponder.v1.SetCertificateArchivedResponseType;
import se.inera.intyg.minaintyg.web.api.ModuleAPIResponse;
import se.inera.intyg.minaintyg.web.exception.ExternalWebServiceCallFailedException;
import se.inera.intyg.minaintyg.web.exception.ResultTypeErrorException;
import se.inera.intyg.minaintyg.web.web.service.dto.*;
import se.inera.intyg.minaintyg.web.web.util.*;
import se.riv.clinicalprocess.healthcond.certificate.getCertificate.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.IntygsStatus;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificateServiceImpl.class);

    /* Mapper to serialize/deserialize Utlatanden. */
    protected static ObjectMapper objectMapper = new CustomObjectMapper();

    @Autowired
    private ListCertificatesForCitizenResponderInterface listService;

    @Autowired
    private SetCertificateStatusResponderInterface setStatusService;

    @Autowired
    private SendCertificateToRecipientResponderInterface sendService;

    @Autowired
    private GetCertificateContentResponderInterface getContentService;

    @Autowired
    private GetCertificateResponderInterface getCertificateClient;

    @Autowired
    private GetRecipientsForCertificateResponderInterface getRecipientsService;

    @Autowired
    private SetCertificateArchivedResponderInterface setArchivedService;

    @Autowired
    private MonitoringLogService monitoringService;

    @Autowired
    private IntygModuleRegistry moduleRegistry;

    @Autowired
    private IntygTextsService intygTextsService;

    @Autowired
    private CitizenService citizenService;

    // These values are injected by their setter methods
    private String vardReferensId;
    private String logicalAddress;

    private String[] useLegacyGetCertificate = { CertificateTypes.FK7263.toString(), CertificateTypes.TSBAS.toString(),
            CertificateTypes.TSDIABETES.toString() };

    private enum ArchivedState {
        ARCHIVED("true"),
        RESTORED("false");
        ArchivedState(String state) {
            this.state = state;
        }

        private String state;

        public String getState() {
            return state;
        }
    }

    /**
     * NOTE: This implementation only correctly the fields used by the SendMedicalCertificateResponderInterface
     * implementation. (The responserinterface used here now should be replaced with a custom
     * interface for this type of sendCertificate that is initiated by the citizen from MI)
     *
     * @see se.inera.intyg.minaintyg.web.web.service.CertificateService#sendCertificate(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public ModuleAPIResponse sendCertificate(Personnummer civicRegistrationNumber, String certificateId, String recipientId) {
        LOGGER.debug("sendCertificate {} to {}", certificateId, recipientId);

        SendCertificateToRecipientType request = SendCertificateToRecipientTypeConverter.convert(certificateId,
                civicRegistrationNumber.getPersonnummer(), citizenService.getCitizen().getUsername(), recipientId);

        final SendCertificateToRecipientResponseType response = sendService.sendCertificateToRecipient(logicalAddress, request);

        if (response.getResult().getResultCode().equals(se.riv.clinicalprocess.healthcond.certificate.v2.ResultCodeType.ERROR)) {
            LOGGER.warn("SendCertificate error: {}", response.getResult().getResultText());
            return new ModuleAPIResponse("error", "");
        }

        monitoringService.logCertificateSend(certificateId, recipientId);
        return new ModuleAPIResponse("sent", "");
    }

    @Override
    public UtlatandeMetaData setCertificateStatus(Personnummer civicRegistrationNumber, String id, LocalDateTime timestamp, String recipientId,
            StatusType type) {
        UtlatandeMetaData result = null;
        SetCertificateStatusRequestType req = new SetCertificateStatusRequestType();
        req.setCertificateId(id);
        req.setNationalIdentityNumber(civicRegistrationNumber.getPersonnummer());
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
    public Optional<UtlatandeWithMeta> getUtlatande(String type, Personnummer civicRegistrationNumber, String certificateId) {
        if (!moduleRegistry.moduleExists(type)) {
            LOGGER.error("The specified module {} does not exist", type);
            throw new RuntimeException(String.format("The specified module %s does not exist", type));
        }
        // Handle legacy get requests to Intygstjänsten
        if (Arrays.asList(useLegacyGetCertificate).contains(type)) {
            AttributedURIType uri = new AttributedURIType();
            uri.setValue(logicalAddress);
            return Optional.of(getUtlatandeLegacy(civicRegistrationNumber, certificateId, uri));
        } else {
            return getUtlatande(civicRegistrationNumber, certificateId, logicalAddress);
        }

    }

    @Override
    public List<UtlatandeMetaData> getCertificates(Personnummer civicRegistrationNumber) {
        final ListCertificatesForCitizenType params = new ListCertificatesForCitizenType();
        params.setPersonId(civicRegistrationNumber.getPersonnummer());

        ListCertificatesForCitizenResponseType response = listService.listCertificatesForCitizen(null, params);

        switch (response.getResult().getResultCode()) {
        case OK:
            return ClinicalProcessMetaConverter.toUtlatandeMetaData(response.getMeta());
        default:
            LOGGER.error("Failed to fetch cert list for user #" + civicRegistrationNumber.getPnrHash() + " from Intygstjänsten. WS call result is "
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
            LOGGER.error("Failed to fetch recipient list for cert type: {} from Intygstjänsten. WS call result is {}", certificateType,
                    response.getResult());
            throw new ResultTypeErrorException(response.getResult());
        }
    }

    @Override
    public UtlatandeMetaData archiveCertificate(String certificateId, Personnummer civicRegistrationNumber) {
        UtlatandeMetaData result = setArchived(certificateId, civicRegistrationNumber, ArchivedState.ARCHIVED);
        monitoringService.logCertificateArchived(certificateId);
        return result;
    }

    @Override
    public UtlatandeMetaData restoreCertificate(String certificateId, Personnummer civicRegistrationNumber) {
        UtlatandeMetaData result = setArchived(certificateId, civicRegistrationNumber, ArchivedState.RESTORED);
        monitoringService.logCertificateRestored(certificateId);
        return result;
    }

    @Value("${application.ID}")
    void setVardReferensId(final String vardReferensId) {
        this.vardReferensId = vardReferensId;
    }

    @Value("${intygstjanst.logicaladdress}")
    void setLogicalAddress(final String logicalAddress) {
        this.logicalAddress = logicalAddress;
    }

    private Optional<UtlatandeWithMeta> getUtlatande(Personnummer civicRegistrationNumber, String certificateId, String logicalAddress) {
        GetCertificateType getCertificateType = new GetCertificateType();
        IntygId intygId = new IntygId();
        intygId.setRoot("SE5565594230-B31");
        intygId.setExtension(certificateId);
        getCertificateType.setIntygsId(intygId);
        try {
            GetCertificateResponseType response = getCertificateClient.getCertificate(logicalAddress, getCertificateType);
            if (response.getIntyg().getStatus().stream().anyMatch(status -> StatusKod.CANCEL.name().equals(status.getStatus().getCode()))) {
                LOGGER.info("Certificate {} is revoked", certificateId);
                return Optional.empty();
            }
            UtlatandeWithMeta utlatandeWithMeta = convert(response);
            if (utlatandeWithMeta != null && utlatandeWithMeta.getUtlatande() != null) {
                monitoringService.logCertificateRead(utlatandeWithMeta.getUtlatande().getId(), utlatandeWithMeta.getUtlatande().getTyp());
            }
            return Optional.of(utlatandeWithMeta);
        } catch (SOAPFaultException e) {
            LOGGER.warn("Failed to fetch utlatande #{} from Intygstjänsten. WS call result is {}", certificateId, e.getMessage());
            return Optional.empty();
        }
    }

    private UtlatandeWithMeta getUtlatandeLegacy(Personnummer civicRegistrationNumber, String certificateId, AttributedURIType uri) {
        GetCertificateContentRequestType request = new GetCertificateContentRequestType();
        request.setCertificateId(certificateId);
        request.setNationalIdentityNumber(civicRegistrationNumber.getPersonnummer());

        GetCertificateContentResponseType response = getContentService.getCertificateContent(uri, request);

        switch (response.getResult().getResultCode()) {
        case OK:
            UtlatandeWithMeta utlatandeWithMeta = convertLegacy(response);
            if (utlatandeWithMeta != null && utlatandeWithMeta.getUtlatande() != null) {
                monitoringService.logCertificateRead(utlatandeWithMeta.getUtlatande().getId(), utlatandeWithMeta.getUtlatande().getTyp());
            }
            return utlatandeWithMeta;
        default:
            LOGGER.error("Failed to fetch utlatande #" + certificateId + " from Intygstjänsten. WS call result is " + response.getResult());
            throw new ExternalWebServiceCallFailedException(response.getResult().getInfoText(), response.getResult().getErrorId().name());
        }
    }

    private UtlatandeMetaData setArchived(String certificateId, Personnummer civicRegistrationNumber, ArchivedState archivedState) {
        SetCertificateArchivedRequestType parameters = new SetCertificateArchivedRequestType();
        parameters.setArchivedState(archivedState.getState());
        parameters.setCertificateId(certificateId);
        parameters.setNationalIdentityNumber(civicRegistrationNumber.getPersonnummer());

        UtlatandeMetaData result = null;
        SetCertificateArchivedResponseType response = setArchivedService.setCertificateArchived(null, parameters);

        switch (response.getResult().getResultCode()) {
        case ERROR:
            LOGGER.error("Failed to set certifiate '{}' as {}. WS call result is {}", new Object[] { certificateId, archivedState,
                    response.getResult().getErrorText() });
            throw new ExternalWebServiceCallFailedException(response.getResult().getInfoText(), response.getResult().getErrorId().name());
        default:
            List<UtlatandeMetaData> updatedList = this.getCertificates(civicRegistrationNumber);
            for (UtlatandeMetaData meta : updatedList) {
                if (meta.getId().equals(certificateId)) {
                    result = meta;
                    break;
                }
            }
        }

        return result;
    }

    private UtlatandeWithMeta convert(final GetCertificateResponseType response) {
        Intyg intyg = response.getIntyg();
        String json;
        Utlatande utlatande;
        try {
            ModuleApi moduleApi = moduleRegistry.getModuleApi(intyg.getTyp().getCode().toLowerCase());
            utlatande = moduleApi.getUtlatandeFromIntyg(intyg);
            json = objectMapper.writeValueAsString(utlatande);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Status> statusList = new ArrayList<>();
        for (IntygsStatus intygStatus : response.getIntyg().getStatus()) {
            StatusKod sk = StatusKod.valueOf(intygStatus.getStatus().getCode());
            if (sk == null) {
                continue;
            }
            CertificateState certificateState = sk.toCertificateState();
            if (CertificateState.CANCELLED.equals(certificateState) || CertificateState.SENT.equals(certificateState)) {
                statusList.add(new Status(certificateState, intygStatus.getPart().getCode(), intygStatus.getTidpunkt()));
            }
        }
        return new UtlatandeWithMeta(utlatande, json, statusList);
    }

    private UtlatandeWithMeta convertLegacy(final GetCertificateContentResponseType response) {
        Utlatande utlatande;
        String document = response.getCertificate();

        try {
            ModuleApi moduleApi = moduleRegistry.getModuleApi(response.getType());
            utlatande = moduleApi.getUtlatandeFromJson(document);
        } catch (Exception e) {
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

    @Override
    public String getQuestions(String intygsTyp, String version) {
        String questionsAsJson = intygTextsService.getIntygTexts(intygsTyp, version);

        LOGGER.debug("Got questions of {} chars from module '{}'", getSafeLength(questionsAsJson), intygsTyp);

        return questionsAsJson;
    }

    private int getSafeLength(String str) {
        return (StringUtils.isNotBlank(str)) ? str.length() : 0;
    }
}
