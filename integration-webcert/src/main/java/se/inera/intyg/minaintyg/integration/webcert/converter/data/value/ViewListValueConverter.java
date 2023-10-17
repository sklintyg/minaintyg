package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueViewList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueViewText;

@Component
public class ViewListValueConverter extends AbstractValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.VIEW_LIST;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var valueList = ((CertificateDataValueViewList) element.getValue()).getList();
    if (valueList == null || valueList.isEmpty()) {
      return notProvidedValue();
    }
    return CertificateQuestionValueList.builder()
        .values(
            valueList.stream()
                .map(CertificateDataValueViewText::getText)
                .toList()
        )
        .build();
  }

  private CertificateQuestionValueText notProvidedValue() {
    return CertificateQuestionValueText.builder()
        .value(NOT_PROVIDED)
        .build();
  }
}
