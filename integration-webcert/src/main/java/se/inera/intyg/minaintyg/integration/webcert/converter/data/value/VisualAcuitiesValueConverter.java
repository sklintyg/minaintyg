package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueGeneralTable;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.TableElement;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.TableElementType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigVisualAcuity;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.VisualAcuity;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDouble;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueVisualAcuities;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueVisualAcuity;

@Component
public class VisualAcuitiesValueConverter extends AbstractValueConverter {

  public static final String MISSING_LABEL = "Saknas";
  public static final String EMPTY = "";
  public static final String EMPTY_VALUE = "-";
  public static final String TRUE_LABEL = "Ja";
  public static final String FALSE_LABEL = "Nej";

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.VISUAL_ACUITIES;
  }

  @Override
  protected CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var value = getValue(element.getValue());
    final var config = getConfig(element.getConfig());
    return createTableValue(value, config);
  }

  private static TableElement headingElement(String value) {
    return TableElement.builder()
        .type(TableElementType.HEADING)
        .value(value)
        .build();
  }

  private static TableElement dataElement(String value) {
    return TableElement.builder()
        .type(TableElementType.DATA)
        .value(value)
        .build();
  }

  private static List<TableElement> dataElements(List<String> values) {
    return values.stream()
        .map(VisualAcuitiesValueConverter::dataElement)
        .toList();
  }

  private static CertificateQuestionValue createTableValue(
      Optional<CertificateDataValueVisualAcuities> values,
      Optional<CertificateDataConfigVisualAcuity> config) {
    if (isEmpty(values)) {
      return NOT_PROVIDED_VALUE;
    }

    return CertificateQuestionValueGeneralTable.builder()
        .headings(
            List.of(
                dataElement(EMPTY),
                headingElement(headerLabel(config,
                    CertificateDataConfigVisualAcuity::getWithoutCorrectionLabel)
                ),
                headingElement(
                    headerLabel(config,
                        CertificateDataConfigVisualAcuity::getWithCorrectionLabel)
                ),
                headingElement(
                    headerLabel(config,
                        CertificateDataConfigVisualAcuity::getContactLensesLabel)
                )
            )
        )
        .values(
            List.of(
                row(
                    headingElement(label(config, CertificateDataConfigVisualAcuity::getRightEye)),
                    dataElements(value(values, CertificateDataValueVisualAcuities::getRightEye))),
                row(
                    headingElement(label(config, CertificateDataConfigVisualAcuity::getLeftEye)),
                    dataElements(value(values, CertificateDataValueVisualAcuities::getLeftEye))
                ),
                row(
                    headingElement(label(config, CertificateDataConfigVisualAcuity::getBinocular)),
                    dataElements(
                        value(values, CertificateDataValueVisualAcuities::getBinocular, false))
                )
            )
        )
        .build();
  }

  private static boolean isEmpty(Optional<CertificateDataValueVisualAcuities> values) {
    return values.isEmpty() || values.get().getRightEye() == null;
  }

  private static String headerLabel(Optional<CertificateDataConfigVisualAcuity> config,
      Function<CertificateDataConfigVisualAcuity, String> getLabel) {
    return config.map(getLabel)
        .orElse(MISSING_LABEL);
  }

  private static List<TableElement> row(TableElement label, List<TableElement> value) {
    return Stream.concat(Stream.of(label), value.stream()).toList();
  }

  private static String label(Optional<CertificateDataConfigVisualAcuity> config,
      Function<CertificateDataConfigVisualAcuity, VisualAcuity> getVisualAcuity) {
    return config.map(getVisualAcuity)
        .map(VisualAcuity::getLabel)
        .orElse(MISSING_LABEL);
  }

  private static List<String> value(Optional<CertificateDataValueVisualAcuities> values,
      Function<CertificateDataValueVisualAcuities, CertificateDataValueVisualAcuity> getVisualActuity) {
    return value(values, getVisualActuity, true);
  }

  private static List<String> value(Optional<CertificateDataValueVisualAcuities> values,
      Function<CertificateDataValueVisualAcuities, CertificateDataValueVisualAcuity> getVisualActuity,
      boolean includeContactLenses) {
    return List.of(
        value(values, getVisualActuity, CertificateDataValueVisualAcuity::getWithoutCorrection),
        value(values, getVisualActuity, CertificateDataValueVisualAcuity::getWithCorrection),
        includeContactLenses ?
            booleanValue(values, getVisualActuity,
                CertificateDataValueVisualAcuity::getContactLenses) :
            EMPTY_VALUE
    );
  }

  private static String value(Optional<CertificateDataValueVisualAcuities> visualAcuities,
      Function<CertificateDataValueVisualAcuities, CertificateDataValueVisualAcuity> getVisualAcuity,
      Function<CertificateDataValueVisualAcuity, CertificateDataValueDouble> getDouble) {
    return visualAcuities.map(getVisualAcuity)
        .map(getDouble)
        .map(CertificateDataValueDouble::getValue)
        .map(value -> value.toString().replace(".", ","))
        .orElse(EMPTY_VALUE);
  }

  private static String booleanValue(Optional<CertificateDataValueVisualAcuities> visualAcuities,
      Function<CertificateDataValueVisualAcuities, CertificateDataValueVisualAcuity> getVisualAcuity,
      Function<CertificateDataValueVisualAcuity, CertificateDataValueBoolean> getBoolean) {
    return visualAcuities.map(getVisualAcuity)
        .map(getBoolean)
        .map(CertificateDataValueBoolean::getSelected)
        .map(value -> Boolean.TRUE.equals(value) ? TRUE_LABEL : FALSE_LABEL)
        .orElse(FALSE_LABEL);
  }

  private Optional<CertificateDataConfigVisualAcuity> getConfig(CertificateDataConfig config) {
    if (config instanceof CertificateDataConfigVisualAcuity acuitiesConfig) {
      return Optional.of(acuitiesConfig);
    }
    return Optional.empty();
  }

  private Optional<CertificateDataValueVisualAcuities> getValue(CertificateDataValue value) {
    if (value instanceof CertificateDataValueVisualAcuities acuitiesValue) {
      return Optional.of(acuitiesValue);
    }
    return Optional.empty();
  }
}
