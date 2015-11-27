package se.inera.certificate.web.security;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.JAXRSInvoker;
import org.apache.cxf.jaxrs.model.OperationResourceInfo;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.MessageContentsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import se.inera.certificate.web.service.CitizenService;
import se.inera.certificate.web.service.ConsentService;
import se.inera.intyg.common.support.modules.support.api.dto.Personnummer;

import com.google.common.collect.ImmutableSet;

/**
 * Created by orjan on 14-08-19(34).
 */
public class VerifyConsentJAXRSInvoker extends JAXRSInvoker {

    private static final Logger LOG = LoggerFactory.getLogger(VerifyConsentJAXRSInvoker.class);

    @Autowired
    private ConsentService consentService;

    @Autowired
    private CitizenService citizenService;

    private AtomicLong consentCounter = new AtomicLong();

    private static final ImmutableSet<String> ALLOWED_METHODS = ImmutableSet.of("getModulesMap", "onbeforeunload", "giveConsent");

    @Override
    public Object invoke(Exchange exchange, Object requestParams, Object resourceObject) {
        LOG.debug("invoke(...)");

        LOG.debug("Verifying citizen consent...");
        // Get Citizen instance from context
        Citizen citizen = citizenService.getCitizen();
        if (citizen == null) {
            LOG.error("State of consent not known -citizen was null");
            return new MessageContentsList(Response.status(Response.Status.FORBIDDEN).build());
        }

        LOG.debug("Login from {} consentIsKnown: {} ", citizen.getLoginMethod().toString(), citizen.consentIsKnown());

        final Personnummer citizenPersonnummer = new Personnummer(citizen.getUsername());
        if (!citizen.consentIsKnown()) {
            LOG.debug("State of consent not known - fetching consent status...");
            boolean consentResult = consentService.fetchConsent(citizenPersonnummer);
            LOG.debug("Consent result is {}", consentResult);
            // set the consent result so that we don't have to fetch it next tinime around
            citizen.setConsent(consentResult);
        }

        // Check consent state of citizen
        if (!citizen.hasConsent()) {
            LOG.warn("User: {} does not have consent", citizenPersonnummer.getPnrHash());
            final String methodName;
            if (exchange != null) {
                OperationResourceInfo ori = exchange.get(OperationResourceInfo.class);
                Method m = ori.getMethodToInvoke();
                LOG.debug("Method called is {} ", m.getName());
                methodName = m.getName();
            } else {
                methodName = "";
            }

            if (ALLOWED_METHODS.contains(methodName)) {
                LOG.debug("Allowing method {}", methodName);
            } else {
                try {
                    return new MessageContentsList(Response.status(Response.Status.FORBIDDEN)
                            .contentLocation(new URI("\\web\\visa-ge-samtycke#\\consent")).build());
                } catch (URISyntaxException e) {
                    return new MessageContentsList(Response.status(Response.Status.FORBIDDEN).build());
                }
            }
        }
        // We have a consent, let the request continue processing
        consentCounter.incrementAndGet();

        return super.invoke(exchange, requestParams, resourceObject);
    }

    public Long getConsentCounter() {
        return consentCounter.longValue();
    }

}

// Servlet filters -> CXF interceptors.
//
// If you mix JAX-RS 2.0 filters & CXF interceptors, then
//
// Servlet filters -> CXF interceptors before UNMARSHAL phase ->
// PreMatch Container Request Filter -> CXF interceptors after UNMARSHAL
// phase -> per-method specific Container Request Filters
