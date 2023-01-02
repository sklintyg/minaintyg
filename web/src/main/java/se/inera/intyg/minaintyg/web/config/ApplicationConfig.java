/*
 * Copyright (C) 2023 Inera AB (http://www.inera.se)
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
package se.inera.intyg.minaintyg.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.CookieSerializer;
import se.inera.intyg.infra.security.common.cookie.IneraCookieSerializer;

@Configuration
public class ApplicationConfig {

    @Bean
    public CookieSerializer cookieSerializer() {
        /*
        This is needed to make IdP functionality work.
        This will not satisfy all browsers, but it works for IE, Chrome and Edge.
        Reference: https://auth0.com/blog/browser-behavior-changes-what-developers-need-to-know/
         */
        return new IneraCookieSerializer(true);
    }
}
