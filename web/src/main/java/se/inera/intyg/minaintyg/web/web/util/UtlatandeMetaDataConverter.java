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

package se.inera.intyg.minaintyg.web.web.util;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import se.inera.intyg.common.support.model.StatusKod;
import se.inera.intyg.common.support.modules.converter.TransportConverterUtil;
import se.inera.intyg.common.support.modules.registry.IntygModuleRegistry;
import se.inera.intyg.common.support.modules.registry.ModuleNotFoundException;
import se.inera.intyg.common.support.modules.support.api.ModuleApi;
import se.inera.intyg.common.support.modules.support.api.exception.ModuleException;
import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeMetaData;
import se.riv.clinicalprocess.healthcond.certificate.v2.Intyg;
import se.riv.clinicalprocess.healthcond.certificate.v2.IntygsStatus;

@Component
public class UtlatandeMetaDataConverter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UtlatandeMetaDataConverter.class);

    private static final Comparator<? super Intyg> DESCENDING_DATE = Comparator.comparing(Intyg::getSigneringstidpunkt, Comparator.reverseOrder());

    @Autowired
    private IntygModuleRegistry moduleRegistry;

    public UtlatandeMetaData convert(Intyg intyg, boolean arkiverade) {
        UtlatandeMetaBuilder builder = new UtlatandeMetaBuilder();

        builder.id(intyg.getIntygsId().getExtension())
                .type(intyg.getTyp().getCode().toLowerCase())
                .issuerName(intyg.getSkapadAv().getFullstandigtNamn())
                .facilityName(intyg.getSkapadAv().getEnhet().getEnhetsnamn())
                .signDate(intyg.getSigneringstidpunkt())
                .available(String.valueOf(!arkiverade))
                .additionalInfo(getAdditionalInfo(intyg));

        if (intyg.getStatus() != null) {
            for (IntygsStatus statusType : intyg.getStatus()) {
                StatusKod statuskod = StatusKod.valueOf(statusType.getStatus().getCode());
                if (StatusKod.SENTTO.equals(statuskod) || StatusKod.CANCEL.equals(statuskod)) {
                    builder.addStatus(TransportConverterUtil.getStatus(statusType));
                }
            }
        }

        return builder.build();
    }

    private String getAdditionalInfo(Intyg intyg) {
        try {
            ModuleApi moduleApi = moduleRegistry.getModuleApi(intyg.getTyp().getCode().toLowerCase());
            return moduleApi.getAdditionalInfo(intyg);
        } catch (ModuleNotFoundException | ModuleException e) {
            LOGGER.error("Error retrieving additional info from module registry: {}", e.getMessage());
        }
        return null;
    }

    public List<UtlatandeMetaData> convert(List<Intyg> intygList, boolean arkiverade) {
        List<UtlatandeMetaData> result = new ArrayList<>();

        // Copy and sort
        List<Intyg> input = new ArrayList<>(intygList);
        Collections.sort(input, DESCENDING_DATE);

        for (Intyg intyg : input) {
            result.add(convert(intyg, arkiverade));
        }

        return result;
    }

}
