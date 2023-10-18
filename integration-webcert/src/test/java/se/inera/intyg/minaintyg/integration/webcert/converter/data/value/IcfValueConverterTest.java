package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigIcf;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataIcfValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;

class IcfValueConverterTest {

  private static final String TEXT_VALUE = "textValue";
  private static final String COLLECTIONS_LABEL = "collectionsLabel";
  private static final String NOT_PROVIDED = "Ej angivet";
  private static final String ICF_CODE_1 = "icfCode1";
  private static final String ICF_CODE_2 = "icfCode2";

  private ValueConverter icfValueConverter = new IcfValueConverter();

  @Test
  void shouldConvertCertificateIcfValue() {
    final var value = CertificateDataIcfValue.builder()
        .text(TEXT_VALUE)
        .build();
    final var config = createConfig();
    final var element = createElement(config, value);
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(TEXT_VALUE)
        .build();
    final var result = icfValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  @Test
  void shouldConvertCertificateIcfNoValue() {
    final var value = CertificateDataIcfValue.builder().build();
    final var config = createConfig();
    final var element = createElement(config, value);
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();
    final var result = icfValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  @Test
  void shouldReturnCollectionsLabelIfOneIcfCodeIsProvided() {
    final var value = CertificateDataIcfValue.builder()
        .text(TEXT_VALUE)
        .icfCodes(List.of(ICF_CODE_1))
        .build();
    final var config = createConfig();
    final var element = createElement(config, value);
    final var valueString =
        COLLECTIONS_LABEL + "\n" + List.of(ICF_CODE_1) + "\n" + "\n" + TEXT_VALUE;
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(valueString)
        .build();
    final var result = icfValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  @Test
  void shouldReturnCollectionsLabelIfMultipleIcfCodesAreProvided() {
    final var value = CertificateDataIcfValue.builder()
        .text(TEXT_VALUE)
        .icfCodes(List.of(ICF_CODE_1, ICF_CODE_2))
        .build();
    final var config = createConfig();
    final var element = createElement(config, value);
    final var valueString =
        COLLECTIONS_LABEL + "\n" + List.of(ICF_CODE_1, ICF_CODE_2) + "\n" + "\n" + TEXT_VALUE;
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(valueString)
        .build();
    final var result = icfValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  @Test
  void shouldNotReturnCollectionsLabelIfNoIcfCodeIsProvided() {
    final var value = CertificateDataIcfValue.builder()
        .text(TEXT_VALUE)
        .build();
    final var config = createConfig();
    final var element = createElement(config, value);
    final var expectedValue = CertificateQuestionValueText.builder()
        .value(TEXT_VALUE)
        .build();
    final var result = icfValueConverter.convert(element);
    assertEquals(expectedValue, result);
  }

  private static CertificateDataConfigIcf createConfig() {
    return CertificateDataConfigIcf.builder()
        .collectionsLabel(COLLECTIONS_LABEL)
        .build();
  }

  private static CertificateDataElement createElement(CertificateDataConfig config,
      CertificateDataValue value) {
    return CertificateDataElement.builder()
        .config(config)
        .value(value)
        .build();
  }
}