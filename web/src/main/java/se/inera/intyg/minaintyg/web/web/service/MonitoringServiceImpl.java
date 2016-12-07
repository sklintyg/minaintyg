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

package se.inera.intyg.minaintyg.web.web.service;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.*;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import se.inera.intyg.minaintyg.web.web.service.dto.HealthStatus;
import se.riv.itintegration.monitoring.rivtabp21.v1.PingForConfigurationResponderInterface;
import se.riv.itintegration.monitoring.v1.PingForConfigurationResponseType;
import se.riv.itintegration.monitoring.v1.PingForConfigurationType;

@Service
public class MonitoringServiceImpl implements MonitoringService {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringServiceImpl.class);

    @Value("${project.version}")
    private String applicationVersion;

    @Value("${buildNumber}")
    private String buildNumberString;

    @Value("${buildTime}")
    private String buildTimeString;

    @Value("${intygstjanst.logicaladdress}")
    private String intygstjanstLogicalAddress;

    @Autowired
    @Qualifier("pingIntygstjanstForConfigurationClient")
    private PingForConfigurationResponderInterface intygstjanstPingForConfiguration;

    @Autowired
    private SessionRegistry sessionRegistry;

    /*
     * (non-Javadoc)
     *
     * @see se.inera.intyg.minaintyg.web.monitoring.MonitoringService#checkIntygstjanst()
     */
    @Override
    public HealthStatus checkIntygstjanst() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean ok = pingIntygstjanst();
        stopWatch.stop();
        HealthStatus status = createStatusWithTiming(ok, stopWatch);
        logStatus("pingIntygstjanst", status);
        return status;
    }

    public boolean pingIntygstjanst() {
        try {
            PingForConfigurationType parameters = new PingForConfigurationType();
            PingForConfigurationResponseType pingResponse = intygstjanstPingForConfiguration.pingForConfiguration(intygstjanstLogicalAddress,
                    parameters);
            return pingResponse != null;
        } catch (Exception e) {
            LOG.error("pingIntygstjanst failed with exception: " + e.getMessage());
            return false;
        }
    }

    @Override
    public String getApplicationVersion() {
        return applicationVersion;
    }

    @Override
    public String getApplicationBuildNumber() {
        return buildNumberString;
    }

    @Override
    public String getApplicationBuildTime() {
        return buildTimeString;
    }

    @Override
    public HealthStatus getNbrOfLoggedInUsers() {
        int nbrOfPrincipals = sessionRegistry.getAllPrincipals().size();
        LOG.debug("{} users are currently logged in", nbrOfPrincipals);
        return new HealthStatus(nbrOfPrincipals, true);
    }

    private void logStatus(String operation, HealthStatus status) {
        String result = status.isOk() ? "OK" : "FAIL";
        LOG.info("Operation {} completed with result {} in {} ms", operation, result, status.getMeasurement());
    }

    private HealthStatus createStatusWithTiming(boolean ok, StopWatch stopWatch) {
        if (!ok) {
            return new HealthStatus(-1, ok);
        }

        return new HealthStatus(stopWatch.getTime(), ok);
    }

}
