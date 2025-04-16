package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateRange;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class DateRangeValueConverter extends AbstractValueConverter {

  private static final String FROM_HEADING = "Från och med";
  private static final String TO_HEADING = "Till och med";

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.DATE_RANGE;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    return getDateRange(element.getValue())
        .map(dateRange -> {
              if (isEmpty(dateRange)) {
                return NOT_PROVIDED_VALUE;
              }
              return CertificateQuestionValueTable.builder()
                  .headings(List.of(FROM_HEADING, TO_HEADING))
                  .values(
                      List.of(
                          List.of(dateRange.getFrom().toString(), dateRange.getTo().toString())
                      )
                  )
                  .build();
            }
        )
        .orElse(NOT_PROVIDED_VALUE);
  }

  private static boolean isEmpty(CertificateDataValueDateRange dateRange) {
    return dateRange.getFrom() == null || dateRange.getTo() == null;
  }

  private Optional<CertificateDataValueDateRange> getDateRange(CertificateDataValue element) {
    if (element instanceof CertificateDataValueDateRange dateRange) {
      return Optional.of(dateRange);
    }
    return Optional.empty();
  }
}