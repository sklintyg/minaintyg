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
import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3.wsaddressing10.AttributedURIType;

import riv.insuranceprocess.healthreporting.medcertqa._1.LakarutlatandeEnkelType;
import riv.insuranceprocess.healthreporting.medcertqa._1.VardAdresseringsType;
import se.inera.certificate.api.ModuleAPIResponse;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenResponderInterface;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenResponseType;
import se.inera.certificate.clinicalprocess.healthcond.certificate.listcertificatesforcitizen.v1.ListCertificatesForCitizenType;
import se.inera.certificate.integration.exception.ExternalWebServiceCallFailedException;
import se.inera.certificate.integration.exception.ResultTypeErrorException;
import se.inera.certificate.integration.json.CustomObjectMapper;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Utlatande;
import se.inera.certificate.model.common.MinimalUtlatande;
import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.inera.certificate.web.service.dto.UtlatandeWithMeta;
import se.inera.certificate.web.util.ClinicalProcessMetaConverter;
import se.inera.certificate.web.util.UtlatandeMetaBuilder;
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

    @Autowired
    private ListCertificatesForCitizenResponderInterface listService;

    @Autowired
    private SetCertificateStatusResponderInterface statusService;

    @Autowired
    private SendMedicalCertificateResponderInterface sendService;

    @Autowired
    private GetCertificateContentResponderInterface getCertificateContentService;

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

        Utlatande utlatande;

        try {
            utlatande = objectMapper.readValue(getUtlatande(civicRegistrationNumber, id).getUtlatande(),
                    MinimalUtlatande.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        SendType sendType = new SendType();
        VardAdresseringsType vardAdresseringsType = new VardAdresseringsType();

        // Enhet
        EnhetType enhet = buildEnhetFromUtlatande(utlatande);

        HosPersonalType hosPersonal = new HosPersonalType();
        hosPersonal.setEnhet(enhet);
        hosPersonal.setFullstandigtNamn(utlatande.getSkapadAv().getNamn());

        II personalId = new II();
        Id personalIdSource = utlatande.getSkapadAv().getId();
        personalId.setRoot(personalIdSource.getRoot());
        personalId.setExtension(personalIdSource.getExtension());
        hosPersonal.setPersonalId(personalId);

        vardAdresseringsType.setHosPersonal(hosPersonal);

        sendType.setAdressVard(vardAdresseringsType);
        sendType.setAvsantTidpunkt(new LocalDateTime());
        sendType.setVardReferensId("MI");

        // Lakarutlatande
        LakarutlatandeEnkelType lakarutlatande = buildLakarutlatandeTypeFromUtlatande(utlatande);

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

    /**
     * Build a {@link LakarutlatandeEnkelType} from the source {@link Utlatande}.
     *
     * @param utlatande
     *            a {@link Utlatande}
     * @return {@link LakarutlatandeEnkelType}
     */
    private LakarutlatandeEnkelType buildLakarutlatandeTypeFromUtlatande(Utlatande utlatande) {
        LakarutlatandeEnkelType lakarutlatande = new LakarutlatandeEnkelType();

        lakarutlatande.setLakarutlatandeId(utlatande.getId().getExtension());

        lakarutlatande.setSigneringsTidpunkt(utlatande.getSigneringsdatum());

        PatientType patient = new PatientType();

        II patientIdHolder = new II();
        patientIdHolder.setRoot(utlatande.getPatient().getId().getRoot());
        patientIdHolder.setExtension(utlatande.getPatient().getId().getExtension());
        patient.setPersonId(patientIdHolder);

        patient.setFullstandigtNamn(utlatande.getPatient().getFullstandigtNamn());
        lakarutlatande.setPatient(patient);

        return lakarutlatande;
    }

    /**
     * Build an EnhetType object from an Utlatande.
     *
     * @param utlatande
     *            the source {@link Utlatande}
     * @return {@link EnhetType}
     */
    private EnhetType buildEnhetFromUtlatande(Utlatande utlatande) {
        EnhetType enhet = new EnhetType();
        enhet.setEnhetsnamn(utlatande.getSkapadAv().getVardenhet().getNamn());

        II enhetsId = new II();
        Id sourceEnhetsId = utlatande.getSkapadAv().getVardenhet().getId();
        enhetsId.setRoot(sourceEnhetsId.getRoot());
        enhetsId.setExtension(sourceEnhetsId.getExtension());
        enhet.setEnhetsId(enhetsId);

        if (utlatande.getSkapadAv().getVardenhet().getArbetsplatskod() != null) {
            II arbetsplatsKod = new II();
            Id arbetsplatskodSource = utlatande.getSkapadAv().getVardenhet().getArbetsplatskod();
            arbetsplatsKod.setRoot(arbetsplatskodSource.getRoot());
            arbetsplatsKod.setExtension(arbetsplatskodSource.getExtension());
            enhet.setArbetsplatskod(arbetsplatsKod);
        }

        VardgivareType vardGivare = new VardgivareType();

        II vardGivarId = new II();
        Id vardgivarIdSource = utlatande.getSkapadAv().getVardenhet().getVardgivare().getId();
        vardGivarId.setRoot(vardgivarIdSource.getRoot());
        vardGivarId.setExtension(vardgivarIdSource.getExtension());
        vardGivare.setVardgivareId(vardGivarId);

        vardGivare.setVardgivarnamn(utlatande.getSkapadAv().getVardenhet().getVardgivare().getNamn());
        enhet.setVardgivare(vardGivare);

        return enhet;
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
        // Deserialize certificate Json to get common properties
        Utlatande commonUtlatande;
        try {
            commonUtlatande = objectMapper.readValue(response.getCertificate(), MinimalUtlatande.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UtlatandeMetaBuilder builder = UtlatandeMetaBuilder.fromUtlatande(commonUtlatande);
        for (CertificateStatusType status : response.getStatuses()) {
            if (status.getType().equals(StatusType.SENT) || status.getType().equals(StatusType.CANCELLED)) {
                builder.addStatus(se.inera.certificate.web.service.dto.UtlatandeStatusType.StatusType.valueOf(status.getType().name()),
                        status.getTarget(), status.getTimestamp());
            }
        }

        return new UtlatandeWithMeta(response.getCertificate(), builder.build());
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
}
