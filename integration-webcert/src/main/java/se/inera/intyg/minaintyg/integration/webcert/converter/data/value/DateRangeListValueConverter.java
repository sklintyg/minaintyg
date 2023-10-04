package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class DateRangeListValueConverter extends AbstractValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.DATE_RANGE_LIST;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    // TODO: Implementation is left to do
    return CertificateQuestionValueTable.builder()
        .headings(List.of(
            "Not yet implemented",
            "Not yet implemented"
        ))
        .values(
            List.of(
                List.of(
                    "Not yet implemented",
                    "Not yet implemented"
                )
            ))
        .build();
  }
}
