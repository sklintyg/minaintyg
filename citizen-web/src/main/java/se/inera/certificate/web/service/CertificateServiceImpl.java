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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import se.inera.certificate.api.CertificateMeta;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType;
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateStatusType;
import se.inera.ifv.insuranceprocess.certificate.v1.StatusType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificates.v1.rivtabp20.ListCertificatesResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatus.v1.rivtabp20.SetCertificateStatusResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusRequestType;
import se.inera.ifv.insuranceprocess.healthreporting.setcertificatestatusresponder.v1.SetCertificateStatusResponseType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum;

@Service
public class CertificateServiceImpl implements CertificateService {

    private static final Logger log = LoggerFactory.getLogger(CertificateServiceImpl.class);

    @Autowired
    private ListCertificatesResponderInterface listService;

    @Autowired
    private SetCertificateStatusResponderInterface statusService;

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

    public List<CertificateMeta> getCertificates(String civicRegistrationNumber) {
        final ListCertificatesRequestType params = new ListCertificatesRequestType();
        params.setNationalIdentityNumber(civicRegistrationNumber);

        return convert(listService.listCertificates(null, params));
    }

    private List<CertificateMeta> convert(final ListCertificatesResponseType response) {

        final List<CertificateMetaType> metas = response.getMeta();
        final List<CertificateMeta> dtos = new ArrayList<CertificateMeta>(metas.size());

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
        log.debug("{} is archived: {}", dto.getId(), dto.getArchived());

        final List<CertificateStatusType> stats = meta.getStatus();
        log.debug("Status length {}", stats.size());
        Collections.sort(stats, STATUS_COMPARATOR);
        for (CertificateStatusType stat : stats) {
            if (stat.getType().equals(StatusType.SENT) || stat.getType().equals(StatusType.CANCELLED)) {
                dto.setStatus(stat.getType().toString());
                dto.setTarget(stat.getTarget());
            }
        }

        return dto;
    }

    /**
     * Compare status newest first.
     *
     */
    private static final Comparator<CertificateStatusType> STATUS_COMPARATOR = new Comparator<CertificateStatusType>() {
        @Override
        public int compare(CertificateStatusType o1, CertificateStatusType o2) {
            return o2.getTimestamp().compareTo(o1.getTimestamp());
        }
    };

}
