package se.inera.intyg.minaintyg.integration.api.analytics;

import se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessage;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;

public interface AnalyticsMessageFactory {

  CertificateAnalyticsMessage certificatePrinted(Certificate certificate);

  CertificateAnalyticsMessage certificatePrintedCustomized(Certificate certificate);

  CertificateAnalyticsMessage certificateSent(Certificate certificate, String recipient);
}
