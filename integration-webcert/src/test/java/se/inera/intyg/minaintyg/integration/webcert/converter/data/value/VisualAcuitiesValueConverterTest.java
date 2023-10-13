package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter.NOT_PROVIDED_VALUE;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigVisualAcuity;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.VisualAcuity;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDouble;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueVisualAcuities;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueVisualAcuity;

class VisualAcuitiesValueConverterTest {

  public static final String WITHOUT_CORRECTION_LABEL = "WITHOUT_CORRECTION_LABEL";
  public static final String WITHOUT_CORRECTION_ID = "WITHOUT_CORRECTION_ID";
  public static final String WITH_CORRECTION_LABEL = "WITH_CORRECTION_LABEL";
  public static final String WITH_CORRECTION_ID = "WITH_CORRECTION_ID";
  public static final String CONTACT_LENSES_ID = "CONTACT_LENSES_ID";
  public static final String CONTACT_LENSES_LABEL = "CONTACT_LENSES_LABEL";
  public static final String RIGHT_EYE_LABEL = "RIGHT_EYE_LABEL";
  public static final String LEFT_EYE_LABEL = "LEFT_EYE_LABEL";
  public static final String BINOCULAR_LABEL = "BINOCULAR_LABEL";
  private final ValueConverter visualAcuitiesValueConverter = new VisualAcuitiesValueConverter();

  @Test
  void shallReturnYearValueType() {
    assertEquals(CertificateDataValueType.VISUAL_ACUITIES, visualAcuitiesValueConverter.getType());
  }

  @Test
  void shallReturnNotProvidedValueIfNull() {
    final var element = CertificateDataElement.builder()
        .value(
            CertificateDataValueVisualAcuities.builder()
                .build()
        )
        .build();

    final var actualValue = visualAcuitiesValueConverter.convert(element);
    assertEquals(NOT_PROVIDED_VALUE, actualValue);
  }

  @Test
  void shallReturnWithoutCorrectionAsTable() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings("", WITHOUT_CORRECTION_LABEL, WITH_CORRECTION_LABEL,
                CONTACT_LENSES_LABEL)
        )
        .values(
            createValues(
                createValue(RIGHT_EYE_LABEL, "0,1", "-", "Nej"),
                createValue(LEFT_EYE_LABEL, "1,1", "-", "Nej"),
                createValue(BINOCULAR_LABEL, "2,0", "-", "-")
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createVisualActuityConfiguration()
        )
        .value(
            CertificateDataValueVisualAcuities.builder()
                .rightEye(
                    createVisualActuityValue("0,1", null, null)
                )
                .leftEye(
                    createVisualActuityValue("1,1", null, null)
                )
                .binocular(
                    createVisualActuityValue("2,0", null, null)
                )
                .build()
        )
        .build();

    final var actualValue = visualAcuitiesValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnWithCorrectionAsTable() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings("", WITHOUT_CORRECTION_LABEL, WITH_CORRECTION_LABEL,
                CONTACT_LENSES_LABEL)
        )
        .values(
            createValues(
                createValue(RIGHT_EYE_LABEL, "-", "0,1", "Nej"),
                createValue(LEFT_EYE_LABEL, "-", "1,1", "Nej"),
                createValue(BINOCULAR_LABEL, "-", "2,0", "-")
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createVisualActuityConfiguration()
        )
        .value(
            CertificateDataValueVisualAcuities.builder()
                .rightEye(
                    createVisualActuityValue(null, "0,1", null)
                )
                .leftEye(
                    createVisualActuityValue(null, "1,1", null)
                )
                .binocular(
                    createVisualActuityValue(null, "2,0", null)
                )
                .build()
        )
        .build();

    final var actualValue = visualAcuitiesValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnContactLensesAsTable() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings("", WITHOUT_CORRECTION_LABEL, WITH_CORRECTION_LABEL,
                CONTACT_LENSES_LABEL)
        )
        .values(
            createValues(
                createValue(RIGHT_EYE_LABEL, "0,1", "2,0", "Nej"),
                createValue(LEFT_EYE_LABEL, "1,1", "0,1", "Ja"),
                createValue(BINOCULAR_LABEL, "2,0", "1,1", "-")
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createVisualActuityConfiguration()
        )
        .value(
            CertificateDataValueVisualAcuities.builder()
                .rightEye(
                    createVisualActuityValue("0,1", "2,0", "Nej")
                )
                .leftEye(
                    createVisualActuityValue("1,1", "0,1", "Ja")
                )
                .binocular(
                    createVisualActuityValue("2,0", "1,1", null)
                )
                .build()
        )
        .build();

    final var actualValue = visualAcuitiesValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnWithoutCorrectionWithCorrectionContactLensesAsTable() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings("", WITHOUT_CORRECTION_LABEL, WITH_CORRECTION_LABEL,
                CONTACT_LENSES_LABEL)
        )
        .values(
            createValues(
                createValue(RIGHT_EYE_LABEL, "-", "-", "Ja"),
                createValue(LEFT_EYE_LABEL, "-", "-", "Nej"),
                createValue(BINOCULAR_LABEL, "-", "-", "-")
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createVisualActuityConfiguration()
        )
        .value(
            CertificateDataValueVisualAcuities.builder()
                .rightEye(
                    createVisualActuityValue(null, null, "Ja")
                )
                .leftEye(
                    createVisualActuityValue(null, null, "Nej")
                )
                .binocular(
                    createVisualActuityValue(null, null, null)
                )
                .build()
        )
        .build();

    final var actualValue = visualAcuitiesValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnLeftAndRightWithoutBinocularsAsTable() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings("", WITHOUT_CORRECTION_LABEL, WITH_CORRECTION_LABEL,
                CONTACT_LENSES_LABEL)
        )
        .values(
            createValues(
                createValue(RIGHT_EYE_LABEL, "0,1", "-", "Nej"),
                createValue(LEFT_EYE_LABEL, "1,1", "-", "Nej"),
                createValue(BINOCULAR_LABEL, "-", "-", "-")
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createVisualActuityConfiguration()
        )
        .value(
            CertificateDataValueVisualAcuities.builder()
                .rightEye(
                    createVisualActuityValue("0,1", null, null)
                )
                .leftEye(
                    createVisualActuityValue("1,1", null, null)
                )
                .build()
        )
        .build();

    final var actualValue = visualAcuitiesValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  @Test
  void shallReturnConsiderNullContactLensesAsFalse() {
    final var expectedValue = CertificateQuestionValueTable.builder()
        .headings(
            createHeadings("", WITHOUT_CORRECTION_LABEL, WITH_CORRECTION_LABEL,
                CONTACT_LENSES_LABEL)
        )
        .values(
            createValues(
                createValue(RIGHT_EYE_LABEL, "0,1", "-", "Nej"),
                createValue(LEFT_EYE_LABEL, "1,1", "-", "Nej"),
                createValue(BINOCULAR_LABEL, "-", "-", "-")
            )
        )
        .build();

    final var element = CertificateDataElement.builder()
        .config(
            createVisualActuityConfiguration()
        )
        .value(
            CertificateDataValueVisualAcuities.builder()
                .rightEye(
                    createVisualActuityValue("0,1", null, null)
                )
                .leftEye(
                    createVisualActuityValue("1,1", null, null)
                )
                .build()
        )
        .build();

    final var actualValue = visualAcuitiesValueConverter.convert(element);
    assertEquals(expectedValue, actualValue);
  }

  private static CertificateDataConfigVisualAcuity createVisualActuityConfiguration() {
    return CertificateDataConfigVisualAcuity.builder()
        .withoutCorrectionLabel(WITHOUT_CORRECTION_LABEL)
        .withCorrectionLabel(WITH_CORRECTION_LABEL)
        .contactLensesLabel(CONTACT_LENSES_LABEL)
        .rightEye(
            VisualAcuity.builder()
                .label(RIGHT_EYE_LABEL)
                .withoutCorrectionId(WITHOUT_CORRECTION_ID)
                .withCorrectionId(WITH_CORRECTION_ID)
                .contactLensesId(CONTACT_LENSES_ID)
                .build()
        )
        .leftEye(
            VisualAcuity.builder()
                .label(LEFT_EYE_LABEL)
                .withoutCorrectionId(WITHOUT_CORRECTION_ID)
                .withCorrectionId(WITH_CORRECTION_ID)
                .contactLensesId(CONTACT_LENSES_ID)
                .build()
        )
        .binocular(
            VisualAcuity.builder()
                .label(BINOCULAR_LABEL)
                .withoutCorrectionId(WITHOUT_CORRECTION_ID)
                .withCorrectionId(WITH_CORRECTION_ID)
                .contactLensesId(CONTACT_LENSES_ID)
                .build()
        )
        .build();
  }

  private static CertificateDataValueVisualAcuity createVisualActuityValue(String without,
      String with, String contacts) {
    final var builder = CertificateDataValueVisualAcuity.builder();
    if (without != null) {
      builder.withoutCorrection(
          CertificateDataValueDouble.builder()
              .id(WITHOUT_CORRECTION_ID)
              .value(Double.parseDouble(without.replace(",", ".")))
              .build()
      );
    }
    if (with != null) {
      builder.withCorrection(
          CertificateDataValueDouble.builder()
              .id(WITH_CORRECTION_ID)
              .value(Double.parseDouble(with.replace(",", ".")))
              .build()
      );
    }
    if (contacts != null) {
      builder.contactLenses(
          CertificateDataValueBoolean.builder()
              .id(CONTACT_LENSES_ID)
              .selected("Ja".equals(contacts))
              .build()
      );
    }
    return builder.build();
  }

  private static List<String> createHeadings(String... headings) {
    return List.of(headings);
  }

  @SafeVarargs
  private static List<List<String>> createValues(List<String>... values) {
    return List.of(values);
  }

  private static List<String> createValue(String... values) {
    return List.of(values);
  }
}