/*
 * Copyright (C) 2019 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.service.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Repository;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listknownrecipients.v1.ListKnownRecipientsResponderInterface;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listknownrecipients.v1.ListKnownRecipientsResponseType;
import se.inera.intyg.clinicalprocess.healthcond.certificate.listknownrecipients.v1.ListKnownRecipientsType;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeRecipient;
import se.riv.clinicalprocess.healthcond.certificate.v1.ResultCodeType;

import javax.annotation.PostConstruct;
import javax.xml.ws.WebServiceException;
import java.util.ArrayList;
import java.util.List;

@Repository
@EnableScheduling
public class UtlatandeRecipientRepoImpl implements UtlatandeRecipientRepo {

    private static final Logger LOG = LoggerFactory.getLogger(UtlatandeRecipientRepoImpl.class);

    private List<UtlatandeRecipient> recipients;

    private String logicalAddress;

    @Autowired
    private ListKnownRecipientsResponderInterface listKnownRecipientsClient;

    @Value("${intygstjanst.logicaladdress}")
    void setLogicalAddress(final String logicalAddress) {
        this.logicalAddress = logicalAddress;
    }

    @PostConstruct
    public void init() {
        recipients = new ArrayList<>();
        update();
    }

    @Scheduled(cron = "${recipient.update.cron}")
    public void update() {
        try {
            ListKnownRecipientsType request = new ListKnownRecipientsType();

            ListKnownRecipientsResponseType response =
                    listKnownRecipientsClient.listKnownRecipients(logicalAddress, request);

            // Always use intygstjansten as master
            if (response.getResult().getResultCode().equals(ResultCodeType.OK)) {
                recipients.clear();
                response.getRecipient().forEach(r -> recipients.add(new UtlatandeRecipient(r.getId(), r.getName(), r.isTrusted())));
            } else {
                LOG.error("Got error: {} when updating recipients from Intygstjansten.",
                        response.getResult().getResultText());
            }
        } catch (WebServiceException we) {
            LOG.error("Unable to contact Intygstjänsten while attempting to update recipients: {}", we.getMessage());
        }
    }

    @Override
    public List<UtlatandeRecipient> getAllRecipients() {
        if (recipients.isEmpty()) {
            LOG.warn("No recipients found, attempting to sync with Intygstjansten before proceeding");
            update();
        }
        return recipients;
    }
}
