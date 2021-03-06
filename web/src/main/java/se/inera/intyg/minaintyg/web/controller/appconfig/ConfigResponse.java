/*
 * Copyright (C) 2021 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.controller.appconfig;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import se.inera.intyg.infra.driftbannerdto.Banner;
import se.inera.intyg.infra.dynamiclink.model.DynamicLink;
import se.inera.intyg.minaintyg.web.service.dto.UtlatandeRecipient;

/**
 * DTO representing config metadata of a MI user session.
 *
 * Created by marced on 2017-05-11.
 */
@SuppressWarnings("serial")
public class ConfigResponse implements Serializable {

    // system properties
    private boolean useMinifiedJavascript;

    private String version;
    private String buildNumber;
    private String elva77MainUrl;
    private String elva77LoginUrl;
    private String applicationLogoutUrl;

    private String miUserSurveyUrl;
    private String miUserSurveyVersion;
    private String miUserSurveyDateFrom;
    private String miUserSurveyDateTo;


    private List<UtlatandeRecipient> knownRecipients;

    private Map<String, DynamicLink> links;
    private List<Banner> banners;

    //CHECKSTYLE:OFF ParameterNumberCheck
    public ConfigResponse(String version, String buildNumber, boolean useMinifiedJavascript, String elva77MainUrl, String elva77LoginUrl,
        String applicationLogoutUrl, List<UtlatandeRecipient> knownRecipients, Map<String, DynamicLink> links,
        List<Banner> banners, String miUserSurveyDateFrom, String miUserSurveyDateTo, String miUserSurveyUrl, String miUserSurveyVersion) {

        this.version = version;
        this.buildNumber = buildNumber;
        this.useMinifiedJavascript = useMinifiedJavascript;
        this.elva77MainUrl = elva77MainUrl;
        this.elva77LoginUrl = elva77LoginUrl;
        this.applicationLogoutUrl = applicationLogoutUrl;
        this.knownRecipients = knownRecipients;
        this.links = links;
        this.banners = banners;
        this.miUserSurveyDateFrom = miUserSurveyDateFrom;
        this.miUserSurveyDateTo = miUserSurveyDateTo;
        this.miUserSurveyUrl = miUserSurveyUrl;
        this.miUserSurveyVersion = miUserSurveyVersion;
    }
    //CHECKSTYLE:ON ParameterNumberCheck

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildNumber() {
        return buildNumber;
    }

    public boolean isUseMinifiedJavascript() {
        return useMinifiedJavascript;
    }

    public String getElva77MainUrl() {
        return elva77MainUrl;
    }

    public String getElva77LoginUrl() {
        return elva77LoginUrl;
    }

    public String getApplicationLogoutUrl() {
        return applicationLogoutUrl;
    }

    public List<UtlatandeRecipient> getKnownRecipients() {
        return knownRecipients;
    }

    public Map<String, DynamicLink> getLinks() {
        return links;
    }

    public List<Banner> getBanners() {
        return banners;
    }

    public String getMiUserSurveyUrl() {
        return miUserSurveyUrl;
    }

    public String getMiUserSurveyDateFrom() {
        return miUserSurveyDateFrom;
    }

    public String getMiUserSurveyDateTo() {
        return miUserSurveyDateTo;
    }

    public String getMiUserSurveyVersion() {
        return miUserSurveyVersion;
    }
}
