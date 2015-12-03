package se.inera.intyg.minaintyg.web.monitoring;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.intyg.minaintyg.web.web.service.MonitoringService;
import se.inera.intyg.minaintyg.web.web.service.dto.HealthStatus;
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
