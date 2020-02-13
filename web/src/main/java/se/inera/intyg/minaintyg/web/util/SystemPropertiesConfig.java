/*
 * Copyright (C) 2020 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by marced on 2017-05-11.
 */
@Component
public class SystemPropertiesConfig {

    public static final String USE_MINIFIED_JAVA_SCRIPT_ENV_KEY = "useMinifiedJavaScript";

    @Value("${project.version}")
    private String version;

    @Value("${buildNumber}")
    private String buildNumber;

    @Value("${elva77.url.main}")
    private String elva77MainUrl;

    @Value("${elva77.url.login}")
    private String elva77LoginUrl;

    @Value("${application.logout.url}")
    private String applicationLogoutUrl;

    @Value("${certificate.baseUrl}")
    private String intygstjanstBaseUrl;

    @Value("${mi.user.survey.url:}")
    private String miUserSurveyUrl;

    @Value("${mi.user.survey.version:}")
    private String miUserSurveyVersion;

    @Value("${mi.user.survey.date.to:}")
    private String miUserSurveyDateTo;

    @Value("${mi.user.survey.date.from:}")
    private String miUserSurveyDateFrom;

    @Autowired
    private Environment environment;

    // getters

    public boolean getUseMinifiedJavascript() {
        return environment.getProperty(USE_MINIFIED_JAVA_SCRIPT_ENV_KEY, Boolean.class, true);
    }

    public String getVersion() {
        return version;
    }

    public String getBuildNumber() {
        return buildNumber;
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

    public String getIntygstjanstBaseUrl() {
        return intygstjanstBaseUrl;
    }

    public String getMiUserSurveyUrl() {
        return miUserSurveyUrl;
    }

    public String getMiUserSurveyVersion() {
        return miUserSurveyVersion;
    }

    public String getMiUserSurveyDateTo() {
        return miUserSurveyDateTo;
    }

    public String getMiUserSurveyDateFrom() {
        return miUserSurveyDateFrom;
    }



}
