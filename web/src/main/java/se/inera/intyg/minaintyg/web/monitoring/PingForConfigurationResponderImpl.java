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
package se.inera.intyg.minaintyg.web.monitoring;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.minaintyg.web.service.MonitoringService;
import se.inera.intyg.minaintyg.web.service.dto.HealthStatus;
import se.riv.itintegration.monitoring.rivtabp21.v1.PingForConfigurationResponderInterface;
import se.riv.itintegration.monitoring.v1.ConfigurationType;
import se.riv.itintegration.monitoring.v1.PingForConfigurationResponseType;
import se.riv.itintegration.monitoring.v1.PingForConfigurationType;

public class PingForConfigurationResponderImpl implements PingForConfigurationResponderInterface {

    private static final Logger LOG = LoggerFactory.getLogger(PingForConfigurationResponderImpl.class);

    private static final String INTYGTJANST = "intygstjanst";
    private static final String NBR_OF_USERS = "nbrOfUsers";
    private static final String BUILD_NUMBER = "buildNumber";
    private static final String BUILD_TIME = "buildTime";

    @Autowired
    private MonitoringService monitoringService;

    @Override
    public PingForConfigurationResponseType pingForConfiguration(String logicalAddress, PingForConfigurationType parameters) {

        LOG.debug("Calling pingForConfiguration");

        PingForConfigurationResponseType response = new PingForConfigurationResponseType();
        response.setPingDateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        response.setVersion(monitoringService.getApplicationVersion());

        addConfiguration(response, BUILD_NUMBER, monitoringService.getApplicationBuildNumber());
        addConfiguration(response, BUILD_TIME, monitoringService.getApplicationBuildTime());

        HealthStatus checkIntygstjanst = monitoringService.checkIntygstjanst();
        addConfiguration(response, INTYGTJANST, checkIntygstjanst);

        HealthStatus nbrOfUsers = monitoringService.getNbrOfLoggedInUsers();
        addConfiguration(response, NBR_OF_USERS, nbrOfUsers);

        return response;
    }

    private void addConfiguration(PingForConfigurationResponseType response, String name, HealthStatus status) {
        ConfigurationType conf = new ConfigurationType();
        conf.setName(name);
        conf.setValue(Long.toString(status.getMeasurement()));
        response.getConfiguration().add(conf);
    }

    private void addConfiguration(PingForConfigurationResponseType response, String name, String value) {
        ConfigurationType conf = new ConfigurationType();
        conf.setName(name);
        conf.setValue(value);
        response.getConfiguration().add(conf);
    }

}
