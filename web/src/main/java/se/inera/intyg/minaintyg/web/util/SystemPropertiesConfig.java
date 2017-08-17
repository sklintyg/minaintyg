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

    public static final String USE_MINIFIED_JAVA_SCRIPT_ENV_KEY = "minaintyg.useMinifiedJavaScript";

    @Value("${buildNumber}")
    private String buildNumber;

    @Value("${mvk.url.main}")
    private String mvkMainUrl;

    @Autowired
    private Environment environment;

    public String getBuildNumber() {
        return buildNumber;
    }

    public String getMvkMainUrl() {
        return mvkMainUrl;
    }

    public boolean getUseMinifiedJavascript() {
        return environment.getProperty(USE_MINIFIED_JAVA_SCRIPT_ENV_KEY, Boolean.class, true);
    }
}