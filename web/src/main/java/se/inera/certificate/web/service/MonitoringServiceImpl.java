package se.inera.certificate.web.service;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import se.inera.certificate.web.service.dto.HealthStatus;
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

    /* (non-Javadoc)
     * @see se.inera.certificate.monitoring.MonitoringService#checkIntygstjanst()
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
            PingForConfigurationResponseType pingResponse = intygstjanstPingForConfiguration.pingForConfiguration(intygstjanstLogicalAddress, parameters);
            return (pingResponse != null);
        } catch (Exception e) {
            LOG.error("pingIntygstjanst failed with exception: " + e.getMessage());
            return false;
        }
    }
    
    public String getApplicationVersion() {
        return applicationVersion;
    }

    public String getApplicationBuildNumber() {
        return buildNumberString;
    }

    public String getApplicationBuildTime() {
        return buildTimeString;
    }
    
    private void logStatus(String operation, HealthStatus status) {
        String result = status.isOk() ? "OK" : "FAIL";
        LOG.info("Operation {} completed with result {} in {} ms", new Object[] { operation, result, status.getMeasurement() });
    }

    private HealthStatus createStatusWithTiming(boolean ok, StopWatch stopWatch) {
        if (!ok) {
            return new HealthStatus(-1, ok);
        }
        
        return new HealthStatus(stopWatch.getTime(), ok);
    }
}
