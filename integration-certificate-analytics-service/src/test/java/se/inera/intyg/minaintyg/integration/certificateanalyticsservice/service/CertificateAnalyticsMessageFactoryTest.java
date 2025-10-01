package se.inera.intyg.minaintyg.integration.certificateanalyticsservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.lenient;

import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.MDC;
import se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessage;
import se.inera.intyg.minaintyg.integration.api.analytics.model.CertificateAnalyticsMessageType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.Certificate;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateMetadata;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;
import se.inera.intyg.minaintyg.integration.api.user.LoggedInMinaIntygUserService;
import se.inera.intyg.minaintyg.integration.api.user.model.LoggedInMinaIntygUser;
import se.inera.intyg.minaintyg.logging.MdcLogConstants;

@ExtendWith(MockitoExtension.class)
class CertificateAnalyticsMessageFactoryTest {

  @Mock
  private LoggedInMinaIntygUserService loggedInMinaIntygUserService;

  @InjectMocks
  private static CertificateAnalyticsMessageFactory factory;

  private static final String LOGGED_IN_PATIENT_ID = "19121212-1212";
  private static final String CERTIFICATE_ID = "certificate-id";
  private static final String CERTIFICATE_TYPE = "certiticate-type";
  private static final String CERTIFICATE_TYPE_VERSION = "certiticate-type-version";
  private static final String CERTIFICATE_UNIT_ID = "certificate-unit-id";
  private static final String CERTIFICATE_CARE_PROVIDER_ID = "certificate-care-provider-id";

  private static final String EVENT_SESSION_ID = "event-session-id";

  private Certificate certificate;
  private LoggedInMinaIntygUser loggedInMinaIntygUser;

  @BeforeEach
  void setUp() {
    certificate = Certificate.builder()
        .metadata(
            CertificateMetadata.builder()
                .id(CERTIFICATE_ID)
                .type(
                    CertificateType.builder()
                        .id(CERTIFICATE_TYPE)
                        .version(CERTIFICATE_TYPE_VERSION)
                        .build()
                )
                .unit(
                    CertificateUnit.builder()
                        .id(CERTIFICATE_UNIT_ID)
                        .build()
                )
                .careProvider(
                    CertificateUnit.builder()
                        .id(CERTIFICATE_CARE_PROVIDER_ID)
                        .build()
                )
                .build()
        )
        .build();

    loggedInMinaIntygUser = LoggedInMinaIntygUser.builder()
        .personId(LOGGED_IN_PATIENT_ID)
        .build();

    // Make this lenient to enable mocking to work in parameterized tests
    lenient().when(loggedInMinaIntygUserService.getLoggedInMinaIntygUser())
        .thenReturn(loggedInMinaIntygUser);

    MDC.put(MdcLogConstants.SESSION_ID_KEY, EVENT_SESSION_ID);
  }

  @ParameterizedTest(name = "{index} => {1}")
  @MethodSource("analyticsMessagesBasedOnCertificate")
  void shallReturnCorrectEventTimestamp(Function<Certificate, CertificateAnalyticsMessage> test,
      CertificateAnalyticsMessageType messageType) {
    final var actual = test.apply(certificate);
    assertNotNull(actual.getEvent().getTimestamp());
  }

  @ParameterizedTest(name = "{index} => {1}")
  @MethodSource("analyticsMessagesBasedOnCertificate")
  void shallReturnCorrectEventMessageType(Function<Certificate, CertificateAnalyticsMessage> test,
      CertificateAnalyticsMessageType messageType) {
    final var actual = test.apply(certificate);
    assertEquals(messageType, actual.getEvent().getMessageType());
  }

  @ParameterizedTest(name = "{index} => {1}")
  @MethodSource("analyticsMessagesBasedOnCertificate")
  void shallReturnCorrectEventStaffId(Function<Certificate, CertificateAnalyticsMessage> test,
      CertificateAnalyticsMessageType messageType) {
    final var actual = test.apply(certificate);
    assertEquals(LOGGED_IN_PATIENT_ID, actual.getEvent().getUserId());
  }

  @ParameterizedTest(name = "{index} => {1}")
  @MethodSource("analyticsMessagesBasedOnCertificate")
  void shallReturnCorrectEventSessionId(Function<Certificate, CertificateAnalyticsMessage> test,
      CertificateAnalyticsMessageType messageType) {
    final var actual = test.apply(certificate);
    assertEquals(EVENT_SESSION_ID, actual.getEvent().getSessionId());
  }

  @ParameterizedTest(name = "{index} => {1}")
  @MethodSource("analyticsMessagesBasedOnCertificate")
  void shallReturnCorrectCertificateId(Function<Certificate, CertificateAnalyticsMessage> test,
      CertificateAnalyticsMessageType messageType) {
    final var actual = test.apply(certificate);
    assertEquals(CERTIFICATE_ID, actual.getCertificate().getId());
  }

  @ParameterizedTest(name = "{index} => {1}")
  @MethodSource("analyticsMessagesBasedOnCertificate")
  void shallReturnCorrectCertificateType(Function<Certificate, CertificateAnalyticsMessage> test,
      CertificateAnalyticsMessageType messageType) {
    final var actual = test.apply(certificate);
    assertEquals(CERTIFICATE_TYPE, actual.getCertificate().getType());
  }

  @ParameterizedTest(name = "{index} => {1}")
  @MethodSource("analyticsMessagesBasedOnCertificate")
  void shallReturnCorrectCertificateTypeVersion(
      Function<Certificate, CertificateAnalyticsMessage> test,
      CertificateAnalyticsMessageType messageType) {
    final var actual = test.apply(certificate);
    assertEquals(CERTIFICATE_TYPE_VERSION, actual.getCertificate().getTypeVersion());
  }

  @ParameterizedTest(name = "{index} => {1}")
  @MethodSource("analyticsMessagesBasedOnCertificate")
  void shallReturnCorrectCertificatePatientId(
      Function<Certificate, CertificateAnalyticsMessage> test,
      CertificateAnalyticsMessageType messageType) {
    final var actual = test.apply(certificate);
    assertEquals(LOGGED_IN_PATIENT_ID, actual.getCertificate().getPatientId());
  }

  @ParameterizedTest(name = "{index} => {1}")
  @MethodSource("analyticsMessagesBasedOnCertificate")
  void shallReturnCorrectCertificateUnitId(
      Function<Certificate, CertificateAnalyticsMessage> test,
      CertificateAnalyticsMessageType messageType) {
    final var actual = test.apply(certificate);
    assertEquals(CERTIFICATE_UNIT_ID, actual.getCertificate().getUnitId());
  }

  @ParameterizedTest(name = "{index} => {1}")
  @MethodSource("analyticsMessagesBasedOnCertificate")
  void shallReturnCorrectCertificateCareProviderId(
      Function<Certificate, CertificateAnalyticsMessage> test,
      CertificateAnalyticsMessageType messageType) {
    final var actual = test.apply(certificate);
    assertEquals(CERTIFICATE_CARE_PROVIDER_ID, actual.getCertificate().getCareProviderId());
  }

  static Stream<Arguments> analyticsMessagesBasedOnCertificate() {
    return Stream.of(
        Arguments.of(
            (Function<Certificate, CertificateAnalyticsMessage>) certificate -> factory.certificatePrinted(
                certificate),
            CertificateAnalyticsMessageType.CERTIFICATE_PRINTED_BY_CITIZEN
        ),
        Arguments.of(
            (Function<Certificate, CertificateAnalyticsMessage>) certificate -> factory.certificateSent(
                certificate),
            CertificateAnalyticsMessageType.CERTIFICATE_SENT_BY_CITIZEN
        )
    );
  }
}