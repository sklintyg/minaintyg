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
import java.util.Collections;
import java.util.Comparator;
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
import se.inera.certificate.api.StatusMeta;
import se.inera.certificate.integration.exception.ExternalWebServiceCallFailedException;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.integration.rest.dto.CertificateContentHolder;
import se.inera.certificate.integration.rest.dto.CertificateContentMeta;
import se.inera.certificate.integration.rest.dto.CertificateStatus;
import se.inera.certificate.model.Utlatande;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateStatusType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentRequest;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.getcertificatecontentresponder.v1.GetCertificateContentResponse;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificates.v1.rivtabp20.ListCertificatesResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesResponseType;
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
import com.google.common.base.Throwables;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final Logger LOG = LoggerFactory.getLogger(CertificateServiceImpl.class);

    private static final Comparator<? super CertificateMetaType> DESCENDING_DATE = new Comparator<CertificateMetaType>() {

        @Override
        public int compare(CertificateMetaType m1, CertificateMetaType m2) {
            return m2.getSignDate().compareTo(m1.getSignDate());
        }

    };

    @Autowired
    private ListCertificatesResponderInterface listService;

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
        enhet.setEnhetsnamn("MI");
        II enhetsId = new II();
        enhetsId.setExtension("MI");
        enhet.setEnhetsId(enhetsId);
        VardgivareType vardGivare = new VardgivareType();
        II vardGivarId = new II();
        vardGivarId.setExtension("MI");
        vardGivare.setVardgivareId(vardGivarId);
        vardGivare.setVardgivarnamn("MI");
        enhet.setVardgivare(vardGivare);
        hosPersonal.setEnhet(enhet);
        hosPersonal.setFullstandigtNamn("MI");
        II personalId = new II();
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
        patientIdHolder.setExtension(civicRegistrationNumber);
        patient.setPersonId(patientIdHolder);
        lakarutlatande.setPatient(patient);

        sendType.setLakarutlatande(lakarutlatande);
        req.setSend(sendType);
        final SendMedicalCertificateResponseType response = sendService.sendMedicalCertificate(null, req);
        if (response.getResult().getResultCode().equals(ResultCodeEnum.OK)) {
            return new ModuleAPIResponse("sent", "");
        } else {
            return new ModuleAPIResponse("error", "");
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
        GetCertificateContentRequest request = new GetCertificateContentRequest();
        request.setCertificateId(certificateId);
        request.setNationalIdentityNumber(civicRegistrationNumber);

        GetCertificateContentResponse response = getCertificateContentService.getCertificateContent(null, request);

        switch (response.getResult().getResultCode()) {
        case OK:
            return convert(response);
        default: {
            LOG.error("Failed to fetch utlatande #" + certificateId + " from Intygstjänsten. WS call result is " + response.getResult());
            throw new ExternalWebServiceCallFailedException(response.getResult());
        }
        }
    }

    private CertificateContentHolder convert(final GetCertificateContentResponse response) {

        CertificateContentHolder getCertificateContentHolder = new CertificateContentHolder();
        getCertificateContentHolder.setCertificateContent(response.getCertificate());
        List<CertificateStatusType> statuses = response.getStatuses();

        CertificateContentMeta contentMeta = new CertificateContentMeta();
        contentMeta.setStatuses(convertToCertificateStatus(statuses));

        //Deserialize certificate Json to get common properties
        Utlatande commonUtlatande;
        try {
            commonUtlatande = objectMapper.readValue(getCertificateContentHolder.getCertificateContent(), Utlatande.class);
        } catch (IOException e) {
            throw Throwables.propagate(e);
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
        final ListCertificatesRequestType params = new ListCertificatesRequestType();
        params.setNationalIdentityNumber(civicRegistrationNumber);

        ListCertificatesResponseType response = listService.listCertificates(null, params);

        switch (response.getResult().getResultCode()) {
        case OK:
            return convert(response);
        default: {
            LOG.error("Failed to fetch cert list for user #" + civicRegistrationNumber + " from Intygstjänsten. WS call result is " + response.getResult());
            throw new ExternalWebServiceCallFailedException(response.getResult());
        }
        }

    }

    private List<CertificateMeta> convert(final ListCertificatesResponseType response) {

        final List<CertificateMetaType> metas = response.getMeta();
        final List<CertificateMeta> dtos = new ArrayList<CertificateMeta>(metas.size());

        Collections.sort(metas, DESCENDING_DATE);

        for (CertificateMetaType meta : metas) {
            final CertificateMeta dto = convert(meta);
            dtos.add(dto);
        }

        return dtos;
    }

    private CertificateMeta convert(CertificateMetaType meta) {
        final CertificateMeta dto = new CertificateMeta();
        dto.setId(meta.getCertificateId());
        dto.setCaregiverName(meta.getIssuerName());
        dto.setCareunitName(meta.getFacilityName());
        dto.setFromDate(meta.getValidFrom().toString());
        dto.setTomDate(meta.getValidTo().toString());
        dto.setSentDate(meta.getSignDate().toString());
        dto.setType(meta.getCertificateType());
        dto.setArchived(!Boolean.parseBoolean(meta.getAvailable()));
        LOG.debug("{} is archived: {}", dto.getId(), dto.getArchived());

        final List<CertificateStatusType> stats = meta.getStatus();
        LOG.debug("Status length {}", stats.size());
        Collections.sort(stats, STATUS_COMPARATOR);
        dto.getStatuses().addAll(convertStatus(stats));
        dto.setCancelled(isCertificateCancelled(dto.getStatuses()));

        return dto;
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
    protected List<StatusMeta> convertStatus(List<CertificateStatusType> sourceList) {
        List<StatusMeta> statusList = new ArrayList<>();
        if (sourceList != null) {
            for (CertificateStatusType stat : sourceList) {
                if (stat.getType().equals(StatusType.SENT) || stat.getType().equals(StatusType.CANCELLED)) {
                    StatusMeta status = new StatusMeta();
                    status.setTarget(stat.getTarget());
                    status.setTimestamp(stat.getTimestamp());
                    status.setType(stat.getType().toString());
                    statusList.add(status);
                }
            }
        }
        return statusList;
    }

    protected Boolean isCertificateCancelled(List<StatusMeta> statuses) {
        if (statuses != null) {
            for (StatusMeta status : statuses) {
                if (status.getType().equals(StatusType.CANCELLED.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Compare status newest first.
     */
    private static final Comparator<CertificateStatusType> STATUS_COMPARATOR = new Comparator<CertificateStatusType>() {
        @Override
        public int compare(CertificateStatusType o1, CertificateStatusType o2) {
            return o2.getTimestamp().compareTo(o1.getTimestamp());
        }
    };

}
