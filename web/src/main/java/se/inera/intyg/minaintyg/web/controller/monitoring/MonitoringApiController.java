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
package se.inera.intyg.minaintyg.web.controller.monitoring;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.status;
import static se.inera.intyg.minaintyg.web.controller.monitoring.MonitoringRequest.HEIGHT;
import static se.inera.intyg.minaintyg.web.controller.monitoring.MonitoringRequest.QUESTION_TITLE;
import static se.inera.intyg.minaintyg.web.controller.monitoring.MonitoringRequest.QUESTION_ID;
import static se.inera.intyg.minaintyg.web.controller.monitoring.MonitoringRequest.WIDTH;
import static se.inera.intyg.minaintyg.web.controller.monitoring.MonitoringRequest.USER;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import se.inera.intyg.infra.monitoring.logging.UserAgentInfo;
import se.inera.intyg.infra.monitoring.logging.UserAgentParser;
import se.inera.intyg.minaintyg.web.service.MonitoringLogService;

/**
 * Created by marced on 2019-09-23.
 */
public class MonitoringApiController {

    @Autowired
    private MonitoringLogService monitoringService;

    @Autowired
    private UserAgentParser userAgentParser;

    @POST
    @Path("/")
    @Consumes(APPLICATION_JSON)
    public Response monitoring(MonitoringRequest request, @HeaderParam(HttpHeaders.USER_AGENT) String userAgent) {
        if (request == null || !request.isValid()) {
            return status(BAD_REQUEST).build();
        }

        switch (request.getEvent()) {
            case SCREEN_RESOLUTION:
                final UserAgentInfo userAgentInfo = userAgentParser.parse(userAgent);
                monitoringService
                    .logBrowserInfo(userAgentInfo.getBrowserName(),
                        userAgentInfo.getBrowserVersion(),
                        userAgentInfo.getOsFamily(),
                        userAgentInfo.getOsVersion(),
                        request.getInfo().get(WIDTH),
                        request.getInfo().get(HEIGHT));
                break;
            case OPENED_ABOUT:
                monitoringService.logOpenedAbout(request.getInfo().get(USER));
                break;
            case OPENED_FAQ:
                monitoringService.logOpenedFAQ(request.getInfo().get(USER));
                break;
            case OPENED_QUESTION:
                monitoringService.logOpenedQuestion(request.getInfo().get(QUESTION_ID),
                    request.getInfo().get(QUESTION_TITLE), request.getInfo().get(USER));
                break;
            default:
                return status(BAD_REQUEST).build();
        }
        return ok().build();
    }

}
