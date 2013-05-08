package se.inera.certificate.response;

import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultOfCall;

/**
 * Common interface for all web service responses within the inera certificate project.
 *
 * @author andreaskaltenbach
 */
public interface WebServiceResponse {

    void setResult(ResultOfCall value);
}
