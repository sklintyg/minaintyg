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

import static se.inera.intyg.common.support.Constants.KV_PART_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_STATUS_CODE_SYSTEM;

import java.time.LocalDateTime;
import java.util.*;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v1.*;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.common.enumerations.PartKod;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.registry.ModuleNotFoundException;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.integration.json.CustomObjectMapper;
import se.inera.intyg.minaintyg.web.api.ModuleAPIResponse;
import se.inera.intyg.minaintyg.web.exception.ExternalWebServiceCallFailedException;
import se.inera.intyg.minaintyg.web.exception.ResultTypeErrorException;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeMetaData;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeRecipient;
import se.inera.intyg.minaintyg.web.web.util.SendCertificateToRecipientTypeConverter;
import se.inera.intyg.minaintyg.web.web.util.UtlatandeMetaDataConverter;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v2.*;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.setCertificateStatus.v1.*;
import se.riv.clinicalprocess.healthcond.certificate.types.v2.*;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificateServiceImpl.class);

    /* Mapper to serialize/deserialize Utlatanden. */
    protected static ObjectMapper objectMapper = new CustomObjectMapper();

    @Autowired
    private ListCertificatesForCitizenResponderInterface listService;

    @Autowired
    private SendCertificateToRecipientResponderInterface sendService;

    @Autowired
    private GetRecipientsForCertificateResponderInterface getRecipientsService;

    @Autowired
    private SetCertificateStatusResponderInterface setCertificateStatusService;

    @Autowired
    private MonitoringLogService monitoringService;

    @Autowired
    private IntygModuleRegistry moduleRegistry;

    @Autowired
    private IntygTextsService intygTextsService;

    @Autowired
    private CitizenService citizenService;

    @Autowired
    private UtlatandeMetaDataConverter utlatandeMetaDataConverter;

    // This value is injected by the setter method
    private String logicalAddress;

    @Override
    public ModuleAPIResponse sendCertificate(Personnummer civicRegistrationNumber, String certificateId, String recipientId) {
        LOGGER.debug("sendCertificate {} to {}", certificateId, recipientId);

        SendCertificateToRecipientType request = SendCertificateToRecipientTypeConverter.convert(certificateId,
                civicRegistrationNumber, new Personnummer(citizenService.getCitizen().getUsername()), recipientId);

        final SendCertificateToRecipientResponseType response = sendService.sendCertificateToRecipient(logicalAddress, request);

        if (response.getResult().getResultCode().equals(se.riv.clinicalprocess.healthcond.certificate.v2.ResultCodeType.ERROR)) {
            LOGGER.warn("SendCertificate error: {}", response.getResult().getResultText());
            return new ModuleAPIResponse("error", "");
        }

        monitoringService.logCertificateSend(certificateId, recipientId);
        return new ModuleAPIResponse("sent", "");
    }

    @Override
    public Optional<CertificateResponse> getUtlatande(String type, Personnummer civicRegistrationNumber, String certificateId) {
        CertificateResponse certificate;
        try {
            certificate = moduleRegistry.getModuleApi(type).getCertificate(certificateId, logicalAddress);
        } catch (ModuleException | ModuleNotFoundException e) {
            LOGGER.error("Failed to fetch utlatande '{}' from Intygstjänsten: {}", certificateId, e.getMessage());
            throw new ExternalWebServiceCallFailedException(e.getMessage(), null);
        }

        Personnummer certificateCivicRegistrationNumber = certificate.getUtlatande().getGrundData().getPatient().getPersonId();
        if (!civicRegistrationNumber.equals(certificateCivicRegistrationNumber)) {
            LOGGER.warn("Certificate {} does not match user {}", certificateId, civicRegistrationNumber.getPnrHash());
            return Optional.empty();
        }
        if (certificate.getMetaData().getStatus().stream().anyMatch(status -> CertificateState.CANCELLED.equals(status.getType()))) {
            LOGGER.info("Certificate {} is revoked", certificateId);
            return Optional.empty();
        }
        monitoringService.logCertificateRead(certificate.getUtlatande().getId(), certificate.getUtlatande().getTyp());
        return Optional.of(certificate);
    }

    @Override
    public List<UtlatandeMetaData> getCertificates(Personnummer civicRegistrationNumber, boolean arkiverade) {
        final ListCertificatesForCitizenType params = new ListCertificatesForCitizenType();
        params.setPersonId(InternalConverterUtil.getPersonId(civicRegistrationNumber));
        params.setArkiverade(arkiverade);

        ListCertificatesForCitizenResponseType response = listService.listCertificatesForCitizen(null, params);

        switch (response.getResult().getResultCode()) {
        case OK:
            return utlatandeMetaDataConverter.convert(response.getIntygsLista().getIntyg(), arkiverade);
        default:
            LOGGER.error("Failed to fetch cert list for user #" + civicRegistrationNumber.getPnrHash() + " from Intygstjänsten. WS call result is "
                    + response.getResult());
            throw new ExternalWebServiceCallFailedException(response.getResult().getResultText(),
                    response.getResult().getErrorId() != null ? response.getResult().getErrorId().name() : "");
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
            List<UtlatandeRecipient> recipientList = new ArrayList<>();
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
        UtlatandeMetaData result = setCertificateState(certificateId, civicRegistrationNumber, StatusKod.DELETE);
        monitoringService.logCertificateArchived(certificateId);
        return result;
    }

    @Override
    public UtlatandeMetaData restoreCertificate(String certificateId, Personnummer civicRegistrationNumber) {
        UtlatandeMetaData result = setCertificateState(certificateId, civicRegistrationNumber, StatusKod.RESTOR);
        monitoringService.logCertificateRestored(certificateId);
        return result;
    }

    @Value("${intygstjanst.logicaladdress}")
    void setLogicalAddress(final String logicalAddress) {
        this.logicalAddress = logicalAddress;
    }

    private IntygId toIntygsId(String certificateId) {
        IntygId intygId = new IntygId();
        intygId.setRoot("SE5565594230-B31");
        intygId.setExtension(certificateId);
        return intygId;
    }

    private UtlatandeMetaData setCertificateState(String certificateId, Personnummer civicRegistrationNumber, StatusKod status) {
        boolean arkiverade = !StatusKod.DELETE.equals(status);
        // first assert the certificate belongs to the user
        UtlatandeMetaData utlatande = getCertificates(civicRegistrationNumber, arkiverade).stream()
                .filter(c -> certificateId.equals(c.getId())).findAny().orElseThrow(() -> new IllegalArgumentException("Invalid certificate for user"));
        SetCertificateStatusType parameters = new SetCertificateStatusType();
        parameters.setIntygsId(toIntygsId(certificateId));
        parameters.setPart(toPart(PartKod.INVANA));
        parameters.setStatus(toStatus(status));
        parameters.setTidpunkt(LocalDateTime.now());

        SetCertificateStatusResponseType response = setCertificateStatusService.setCertificateStatus(logicalAddress, parameters);

        switch (response.getResult().getResultCode()) {
        case ERROR:
            LOGGER.error("Failed to set certifiate '{}' as {}. WS call result is {}", certificateId, status.name(),
                    response.getResult().getResultText());
            throw new ExternalWebServiceCallFailedException(response.getResult().getResultText(), response.getResult().getErrorId().name());
        default:
            // negate availability
            utlatande.setAvailable(String.valueOf(arkiverade));
            return utlatande;
        }
    }

    private Part toPart(PartKod partkod) {
        Part part = new Part();
        part.setCode(partkod.name());
        part.setCodeSystem(KV_PART_CODE_SYSTEM);
        part.setDisplayName(partkod.getDisplayName());
        return part;
    }

    private Statuskod toStatus(StatusKod statuskod) {
        Statuskod status = new Statuskod();
        status.setCode(statuskod.name());
        status.setCodeSystem(KV_STATUS_CODE_SYSTEM);
        status.setDisplayName(statuskod.getDisplayName());
        return status;
    }

    @Override
    public String getQuestions(String intygsTyp, String version) {
        String questionsAsJson = intygTextsService.getIntygTexts(intygsTyp, version);

        LOGGER.debug("Got questions of {} chars from module '{}'", getSafeLength(questionsAsJson), intygsTyp);

        return questionsAsJson;
    }

    private int getSafeLength(String str) {
        return StringUtils.isNotBlank(str) ? str.length() : 0;
    }
}
