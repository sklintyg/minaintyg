package se.inera.certificate.monitoring;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import se.riv.itintegration.monitoring.rivtabp21.v1.PingForConfigurationResponderInterface;
import se.riv.itintegration.monitoring.v1.ConfigurationType;
import se.riv.itintegration.monitoring.v1.PingForConfigurationResponseType;
import se.riv.itintegration.monitoring.v1.PingForConfigurationType;

public class PingForConfigurationResponderImpl implements PingForConfigurationResponderInterface {

    private static final Logger LOG = LoggerFactory.getLogger(PingForConfigurationResponderImpl.class);
    
    @Value("${project.version}")
    private String projectVersion;

    @Value("${buildNumber}")
    private String buildNumberString;

    @Value("${buildTime}")
    private String buildTimeString;

    @Override
    public PingForConfigurationResponseType pingForConfiguration(String logicalAddress, PingForConfigurationType parameters) {

        PingForConfigurationResponseType response = new PingForConfigurationResponseType();
        response.setPingDateTime(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        response.setVersion(projectVersion);
        
        addConfiguration(response, "buildNumber", buildNumberString);
        addConfiguration(response, "buildTime", buildTimeString);

        return response;
    }
    
    private void addConfiguration(PingForConfigurationResponseType response, String name, String value) {
        ConfigurationType conf = new ConfigurationType();
        conf.setName(name);
        conf.setValue(value);
        response.getConfiguration().add(conf);
    }

    @PostConstruct
    public void init() {
        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }

}
