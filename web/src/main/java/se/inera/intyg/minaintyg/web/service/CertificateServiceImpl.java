/*
 * Copyright (C) 2018 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.service;

import static se.inera.intyg.common.support.Constants.KV_PART_CODE_SYSTEM;
import static se.inera.intyg.common.support.Constants.KV_STATUS_CODE_SYSTEM;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;

import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v11.GetRecipientsForCertificateResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v11.GetRecipientsForCertificateResponseType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v11.GetRecipientsForCertificateType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.getrecipientsforcertificate.v11.RecipientType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listrelationsforcertificate.v1.IntygRelations;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listrelationsforcertificate.v1.ListRelationsForCertificateResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listrelationsforcertificate.v1.ListRelationsForCertificateResponseType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listrelationsforcertificate.v1.ListRelationsForCertificateType;
import se.inera.intyg.common.services.texts.IntygTextsService;
import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.modules.converter.InternalConverterUtil;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.registry.ModuleNotFoundException;
import se.inera.intyg.common.support.modules.support.api.dto.CertificateResponse;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.common.util.integration.json.CustomObjectMapper;
import se.inera.intyg.minaintyg.web.api.SendToRecipientResult;
import se.inera.intyg.minaintyg.web.exception.ExternalWebServiceCallFailedException;
import se.inera.intyg.minaintyg.web.exception.ResultTypeErrorException;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeMetaData;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeRecipient;
import se.inera.intyg.minaintyg.web.service.repo.UtlatandeRecipientRepo;
import se.inera.intyg.minaintyg.web.util.SendCertificateToRecipientTypeConverter;
import se.inera.intyg.minaintyg.web.util.UtlatandeMetaDataConverter;
import se.inera.intyg.schemas.contract.Personnummer;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v3.ListCertificatesForCitizenResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v3.ListCertificatesForCitizenResponseType;
import se.riv.clinicalprocess.healthcond.certificate.listCertificatesForCitizen.v3.ListCertificatesForCitizenType;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v2.SendCertificateToRecipientResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v2.SendCertificateToRecipientResponseType;
import se.riv.clinicalprocess.healthcond.certificate.sendCertificateToRecipient.v2.SendCertificateToRecipientType;
import se.riv.clinicalprocess.healthcond.certificate.setCertificateStatus.v2.SetCertificateStatusResponderInterface;
import se.riv.clinicalprocess.healthcond.certificate.setCertificateStatus.v2.SetCertificateStatusResponseType;
import se.riv.clinicalprocess.healthcond.certificate.setCertificateStatus.v2.SetCertificateStatusType;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.IntygId;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Part;
import se.riv.clinicalprocess.healthcond.certificate.types.v3.Statuskod;
import se.riv.clinicalprocess.healthcond.certificate.v3.Intyg;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CertificateServiceImpl.class);
    private static final String RECIPIENT_INVANA = "INVANA";

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
    private ListRelationsForCertificateResponderInterface listRelationsService;

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

    @Autowired
    private UtlatandeRecipientRepo recipientRepo;

    // This value is injected by the setter method
    private String logicalAddress;

    @Override
    public List<SendToRecipientResult> sendCertificate(Personnummer civicRegistrationNumber, String certificateId,
            List<String> recipients) {
        List<SendToRecipientResult> batchResult = new ArrayList<>();
        for (String recipientId : recipients) {
            LOGGER.debug("sendCertificate {} to {}", certificateId, recipientId);
            try {
                SendCertificateToRecipientType request = SendCertificateToRecipientTypeConverter.convert(
                        certificateId,
                        civicRegistrationNumber,
                        Personnummer.createPersonnummer(citizenService.getCitizen().getUsername()).get(),
                        recipientId);

                final SendCertificateToRecipientResponseType response = sendService.sendCertificateToRecipient(logicalAddress, request);

                if (response.getResult().getResultCode().equals(se.riv.clinicalprocess.healthcond.certificate.v3.ResultCodeType.ERROR)) {
                    LOGGER.warn(String.format("SendCertificate error when sending certificate %s to recipient %s, errortext was '%s'",
                            certificateId, recipientId, response.getResult().getResultText()));
                    batchResult.add(new SendToRecipientResult(recipientId, false, null));
                } else {
                    batchResult.add(new SendToRecipientResult(recipientId, true, LocalDateTime.now()));
                    monitoringService.logCertificateSend(certificateId, recipientId);
                }
            } catch (Exception e) {
                LOGGER.error(
                        String.format("SendCertificate exception when sending certificate %s to recipient %s", certificateId, recipientId),
                        e);
                batchResult.add(new SendToRecipientResult(recipientId, false, null));
            }

        }
        return batchResult;

    }

    @Override
    public Optional<CertificateResponse> getUtlatande(String type, String intygTypeVersion, Personnummer civicRegistrationNumber,
            String certificateId) {
        CertificateResponse certificate;
        try {
            certificate = moduleRegistry.getModuleApi(type, intygTypeVersion).getCertificate(certificateId, logicalAddress, RECIPIENT_INVANA
            );
        } catch (ModuleException | ModuleNotFoundException e) {
            LOGGER.error("Failed to fetch utlatande '{}' from Intygstjänsten: {}", certificateId, e.getMessage());
            throw new ExternalWebServiceCallFailedException(e.getMessage(), null);
        }

        Personnummer certificateCivicRegistrationNumber = certificate.getUtlatande().getGrundData().getPatient().getPersonId();
        if (!civicRegistrationNumber.equals(certificateCivicRegistrationNumber)) {
            LOGGER.warn("Certificate {} does not match user {}", certificateId, civicRegistrationNumber.getPersonnummerHash());
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
        Part part = toPart(RECIPIENT_INVANA);
        params.setPart(part);

        ListCertificatesForCitizenResponseType response = listService.listCertificatesForCitizen(null, params);

        switch (response.getResult().getResultCode()) {
        case OK:
            List<Intyg> intygList = response.getIntygsLista().getIntyg();
            List<IntygRelations> intygRelations = getRelationsForCertificates(intygList.stream()
                    .map(intyg -> intyg.getIntygsId().getExtension())
                    .collect(Collectors.toList())
            );
            return utlatandeMetaDataConverter.convert(intygList, intygRelations, arkiverade);
        default:
            LOGGER.error("Failed to fetch cert list for user #" + civicRegistrationNumber.getPersonnummerHash()
                    + " from Intygstjänsten. WS call result is "
                    + response.getResult());
            throw new ExternalWebServiceCallFailedException(response.getResult().getResultText(),
                    response.getResult().getErrorId() != null ? response.getResult().getErrorId().name() : "");
        }
    }

    @Override
    public List<IntygRelations> getRelationsForCertificates(List<String> intygsId) {
        if (intygsId == null || intygsId.size() == 0) {
            return Collections.emptyList();
        }
        ListRelationsForCertificateType request = new ListRelationsForCertificateType();
        request.getIntygsId().addAll(intygsId);
        ListRelationsForCertificateResponseType response = listRelationsService.listRelationsForCertificate(logicalAddress, request);

        return response.getIntygRelation();

    }

    @Override
    public List<UtlatandeRecipient> getRecipientsForCertificate(String certificateId) {
        // Setup request
        GetRecipientsForCertificateType request = new GetRecipientsForCertificateType();
        request.setCertificateId(certificateId);

        // Call service and get recipients
        GetRecipientsForCertificateResponseType response = getRecipientsService.getRecipientsForCertificate(logicalAddress, request);

        switch (response.getResult().getResultCode()) {
        case OK:
            List<UtlatandeRecipient> recipientList = new ArrayList<>();
            for (RecipientType recipientType : response.getRecipient()) {
                UtlatandeRecipient utlatandeRecipient = new UtlatandeRecipient(
                        recipientType.getId(),
                        recipientType.getName(),
                        recipientType.isTrusted());
                recipientList.add(utlatandeRecipient);
            }
            return recipientList;
        default:
            LOGGER.error("Failed to fetch recipient list for certificate-id: {} from Intygstjänsten. WS call result is {}",
                    certificateId, response.getResult());
            throw new ResultTypeErrorException(response.getResult());
        }
    }

    @Override
    public List<UtlatandeRecipient> getAllRecipients() {
        return recipientRepo.getAllRecipients();
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
                .filter(c -> certificateId.equals(c.getId())).findAny()
                .orElseThrow(() -> new IllegalArgumentException("Invalid certificate for user"));
        SetCertificateStatusType parameters = new SetCertificateStatusType();
        parameters.setIntygsId(toIntygsId(certificateId));
        parameters.setPart(toPart(RECIPIENT_INVANA));
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

    private Part toPart(String id) {
        Part part = new Part();
        part.setCode(id);
        part.setCodeSystem(KV_PART_CODE_SYSTEM);
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

        LOGGER.debug("Got questions of {} chars from module '{}'", Strings.nullToEmpty(questionsAsJson).trim().length(), intygsTyp);

        return questionsAsJson;
    }

}
