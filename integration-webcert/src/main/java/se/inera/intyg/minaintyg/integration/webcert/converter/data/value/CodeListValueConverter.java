package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCodeList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class CodeListValueConverter extends AbstractValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.CODE_LIST;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var value = ((CertificateDataValueCodeList) element.getValue()).getList();

    if (value == null || value.isEmpty()) {
      return NOT_PROVIDED_VALUE;
    }

    return CertificateQuestionValueList.builder()
        .values(
            value.stream()
                .map(CertificateDataValueCode::getCode)
                .map(code -> codeToString(code, element.getConfig()))
                .toList()
        )
        .build();
  }

  private String codeToString(String code, CertificateDataConfig config) {
    if (!(config instanceof final CertificateDataConfigCheckboxMultipleCode codeConfig)) {
      return code;
    }

    return codeConfig.getList()
        .stream()
        .filter(configItem -> configItem.getId().equals(code))
        .findFirst()
        .map(CheckboxMultipleCode::getLabel).orElse(code);
  }
}
