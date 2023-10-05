package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueItemList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificationQuestionValueItem;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class DateListValueConverter extends AbstractValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.DATE_LIST;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var configList = getConfigList(element);
    final var valueList = getValueList(element);
    return CertificateQuestionValueItemList.builder()
        .values(
            configList.stream()
                .map(checkboxMultipleDate ->
                    valueList.stream()
                        .filter(
                            matchingId(checkboxMultipleDate)
                        )
                        .findFirst()
                        .map(
                            toValueItem(checkboxMultipleDate.getLabel())
                        )
                        .orElse(
                            notProvidedValueItem(checkboxMultipleDate.getLabel())
                        )
                )
                .toList()
        )
        .build();
  }

  private static List<CheckboxMultipleDate> getConfigList(CertificateDataElement element) {
    return ((CertificateDataConfigCheckboxMultipleDate) element.getConfig()).getList();
  }

  private static List<CertificateDataValueDate> getValueList(CertificateDataElement element) {
    final var list = ((CertificateDataValueDateList) element.getValue()).getList();
    return list != null ? list : Collections.emptyList();
  }

  private static Predicate<CertificateDataValueDate> matchingId(
      CheckboxMultipleDate checkboxMultipleDate) {
    return dateValue -> checkboxMultipleDate.getId().equalsIgnoreCase(dateValue.getId());
  }

  private static Function<CertificateDataValueDate, CertificationQuestionValueItem> toValueItem(
      String label) {
    return certificateDataValueDate -> CertificationQuestionValueItem.builder()
        .label(label)
        .value(certificateDataValueDate.getDate().toString())
        .build();
  }

  private static CertificationQuestionValueItem notProvidedValueItem(String label) {
    return CertificationQuestionValueItem.builder()
        .label(label)
        .value(NOT_PROVIDED)
        .build();
  }
}
