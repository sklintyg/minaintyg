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
package se.inera.intyg.minaintyg.web.web.controller.appconfig;

import java.io.Serializable;
import java.util.List;

import se.inera.intyg.minaintyg.web.web.service.dto.UtlatandeRecipient;

/**
 * DTO representing config metadata of a MI user session.
 *
 * Created by marced on 2017-05-11.
 */
@SuppressWarnings("serial")
public class ConfigResponse implements Serializable {

    // system properties
    private String buildNumber;
    private boolean useMinifiedJavascript;
    private String mvkMainUrl;
    private List<UtlatandeRecipient> knownRecipients;

    public ConfigResponse(String buildNumber, boolean useMinifiedJavascript, String mvkMainUrl, List<UtlatandeRecipient> knownRecipients) {
        this.buildNumber = buildNumber;
        this.useMinifiedJavascript = useMinifiedJavascript;
        this.mvkMainUrl = mvkMainUrl;
        this.knownRecipients = knownRecipients;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public boolean isUseMinifiedJavascript() {
        return useMinifiedJavascript;
    }

    public String getMvkMainUrl() {
        return mvkMainUrl;
    }

    public List<UtlatandeRecipient> getKnownRecipients() {
        return knownRecipients;
    }

}