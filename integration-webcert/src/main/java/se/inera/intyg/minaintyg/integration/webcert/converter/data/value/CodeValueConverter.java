package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.DropdownItem;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.RadioMultipleCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.RadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class CodeValueConverter extends AbstractValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.CODE;
  }

  @Override
  public boolean includeSubquestions() {
    return true;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    return convert(element, Collections.emptyList());
  }

  @Override
  public CertificateQuestionValue convert(CertificateDataElement element,
      List<CertificateDataElement> subQuestions) {
    final var codeValue = getCodeValue(element.getValue());
    if (codeValue.isEmpty() || codeValue.get().getId() == null) {
      return NOT_PROVIDED_VALUE;
    }

    return codeValue.map(value ->
            switch (element.getConfig().getType()) {
              case UE_DROPDOWN -> convertDropdown(
                  value,
                  (CertificateDataConfigDropdown) element.getConfig()
              );
              case UE_RADIO_MULTIPLE_CODE_OPTIONAL_DROPDOWN -> convertRadioMultipleCodeOptionalDropdown(
                  value,
                  (CertificateDataConfigRadioMultipleCodeOptionalDropdown) element.getConfig(),
                  subQuestions
              );
              case UE_RADIO_MULTIPLE_CODE -> convertRadioMultipleCode(
                  value,
                  (CertificateDataConfigRadioMultipleCode) element.getConfig()
              );
              default -> createTextValue(value.getId());
            }
        )
        .orElse(NOT_PROVIDED_VALUE);
  }

  private CertificateQuestionValueText convertRadioMultipleCode(CertificateDataValueCode value,
      CertificateDataConfigRadioMultipleCode config) {
    return convertMultipleCode(
        value,
        config.getList(),
        RadioMultipleCode::getId,
        RadioMultipleCode::getLabel
    );
  }

  private static CertificateQuestionValueText convertDropdown(
      CertificateDataValueCode value, CertificateDataConfigDropdown config) {
    return convertMultipleCode(
        value,
        config.getList(),
        DropdownItem::getId,
        DropdownItem::getLabel
    );
  }

  private static <T> CertificateQuestionValueText convertMultipleCode(
      CertificateDataValueCode value,
      List<T> list,
      Function<T, String> getId,
      Function<T, String> getLabel) {
    final var idToLabelMap = convertIdToLabelMap(list, getId, getLabel);
    return createTextValue(
        idToLabelMap.getOrDefault(value.getId(), value.getId())
    );
  }

  private static <T> Map<String, String> convertIdToLabelMap(List<T> list,
      Function<T, String> getId, Function<T, String> getLabel) {
    return list
        .stream()
        .collect(
            Collectors.toMap(
                getId,
                getLabel
            )
        );
  }

  private CertificateQuestionValueText convertRadioMultipleCodeOptionalDropdown(
      CertificateDataValueCode value,
      CertificateDataConfigRadioMultipleCodeOptionalDropdown dataConfig,
      List<CertificateDataElement> subQuestions) {

    final var radioMultipleCodeOptionalDropdown = getIdToLabelMap(dataConfig).get(value.getId());
    if (radioMultipleCodeOptionalDropdown == null) {
      return createTextValue(
          value.getId()
      );
    }

    if (radioMultipleCodeOptionalDropdown.getDropdownQuestionId() == null) {
      return createTextValue(
          radioMultipleCodeOptionalDropdown.getLabel()
      );
    }

    final var subQuestionLabel = subQuestions.stream()
        .filter(subQuestion -> subQuestion.getId().equalsIgnoreCase(
            radioMultipleCodeOptionalDropdown.getDropdownQuestionId()))
        .findAny()
        .map(this::convertToValue)
        .map(textValue -> ((CertificateQuestionValueText) textValue).getValue())
        .orElse(radioMultipleCodeOptionalDropdown.getDropdownQuestionId());

    return createTextValue(
        "%s %s".formatted(radioMultipleCodeOptionalDropdown.getLabel(), subQuestionLabel)
    );
  }

  private static Map<String, RadioMultipleCodeOptionalDropdown> getIdToLabelMap(
      CertificateDataConfigRadioMultipleCodeOptionalDropdown dataConfig) {
    return dataConfig.getList().stream()
        .collect(
            Collectors.toMap(
                RadioMultipleCodeOptionalDropdown::getId,
                Function.identity()
            )
        );
  }

  private static CertificateQuestionValueText createTextValue(String value) {
    return CertificateQuestionValueText.builder()
        .value(value)
        .build();
  }

  private Optional<CertificateDataValueCode> getCodeValue(CertificateDataValue value) {
    if (value instanceof CertificateDataValueCode codeValue) {
      return Optional.of(codeValue);
    }
    return Optional.empty();
  }
}