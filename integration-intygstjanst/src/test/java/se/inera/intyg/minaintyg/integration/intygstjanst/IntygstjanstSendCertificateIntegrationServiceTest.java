package se.inera.intyg.minaintyg.integration.intygstjanst;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.SendCertificateIntegrationRequest;
import se.inera.intyg.minaintyg.integration.intygstjanst.client.SendCertificateUsingIntygstjanstService;

@ExtendWith(MockitoExtension.class)
class IntygstjanstSendCertificateIntegrationServiceTest {

  private static final String PERSON_ID = "191212121212";
  private static final String RECIPIENT = "recipient";
  private static final String CERTIFICATE_ID = "certificateId";
  @Mock
  private SendCertificateUsingIntygstjanstService sendCertificateUsingIntygstjanstService;
  @InjectMocks
  private IntygstjanstSendCertificateIntegrationService intygstjanstSendCertificateIntegrationService;

  private SendCertificateIntegrationRequest getRequest(String patientId, String certificateId,
      String recipient) {
    return SendCertificateIntegrationRequest
        .builder()
        .patientId(patientId)
        .certificateId(certificateId)
        .recipient(recipient)
        .build();
  }

  @Test
  void shouldCallSendServiceWithRequest() {
    SendCertificateIntegrationRequest request = getRequest(PERSON_ID, CERTIFICATE_ID, RECIPIENT);
    intygstjanstSendCertificateIntegrationService.send(request);

    final var captor = ArgumentCaptor.forClass(SendCertificateIntegrationRequest.class);

    verify(sendCertificateUsingIntygstjanstService).send(captor.capture());
    assertEquals(request, captor.getValue());
  }

  @Nested
  class ErrorHandling {

    @Test
    void shouldThrowExceptionIfRequestIsNull() {
      assertThrows(IllegalArgumentException.class,
          () -> intygstjanstSendCertificateIntegrationService.send(null));
    }

    @Test
    void shouldThrowExceptionIfNullPatientId() {
      final var request = getRequest(null, CERTIFICATE_ID, RECIPIENT);
      assertThrows(IllegalArgumentException.class,
          () -> intygstjanstSendCertificateIntegrationService.send(request));
    }

    @Test
    void shouldThrowExceptionIfNullCertificateId() {
      final var request = getRequest(PERSON_ID, null, RECIPIENT);
      assertThrows(IllegalArgumentException.class,
          () -> intygstjanstSendCertificateIntegrationService.send(request));
    }

    @Test
    void shouldThrowExceptionIfNullRecipient() {
      final var request = getRequest(PERSON_ID, CERTIFICATE_ID, null);
      assertThrows(IllegalArgumentException.class,
          () -> intygstjanstSendCertificateIntegrationService.send(request));
    }

    @Test
    void shouldThrowExceptionIfEmptyRecipient() {
      final var request = getRequest(PERSON_ID, CERTIFICATE_ID, "");
      assertThrows(IllegalArgumentException.class,
          () -> intygstjanstSendCertificateIntegrationService.send(request));
    }

    @Test
    void shouldThrowExceptionIfEmptyCertificateId() {
      final var request = getRequest(PERSON_ID, "", RECIPIENT);
      assertThrows(IllegalArgumentException.class,
          () -> intygstjanstSendCertificateIntegrationService.send(request));
    }

    @Test
    void shouldThrowExceptionIfEmptyPatientId() {
      final var request = getRequest("", CERTIFICATE_ID, RECIPIENT);
      assertThrows(IllegalArgumentException.class,
          () -> intygstjanstSendCertificateIntegrationService.send(request));
    }
  }
}
