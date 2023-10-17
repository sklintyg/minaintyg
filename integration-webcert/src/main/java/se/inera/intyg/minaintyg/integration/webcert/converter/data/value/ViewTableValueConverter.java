package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigViewTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.ViewColumn;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueViewTable;

@Component
public class ViewTableValueConverter extends AbstractValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.VIEW_TABLE;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    return getValue(element.getValue())
        .map(value -> mapTable(value, element.getConfig()))
        .orElse(NOT_PROVIDED_VALUE);
  }

  private Optional<CertificateDataValueViewTable> getValue(
      CertificateDataValue value) {
    if (value instanceof CertificateDataValueViewTable viewTable) {
      return Optional.of(viewTable);
    }
    return Optional.empty();
  }

  private Optional<CertificateDataConfigViewTable> getConfig(CertificateDataConfig config) {
    if (config instanceof CertificateDataConfigViewTable viewTable) {
      return Optional.of(viewTable);
    }
    return Optional.empty();
  }

  private CertificateQuestionValue mapTable(
      CertificateDataValueViewTable value,
      CertificateDataConfig config) {
    if (value.getRows() == null || value.getRows().isEmpty()) {
      return NOT_PROVIDED_VALUE;
    }

    return CertificateQuestionValueTable.builder()
        .headings(getHeadings(config))
        .values(
            value.getRows()
                .stream()
                .map(row -> row.getColumns()
                    .stream()
                    .map(CertificateDataTextValue::getText)
                    .toList()
                )
                .toList()
        )
        .build();
  }

  private List<String> getHeadings(CertificateDataConfig config) {
    final var tableViewConfig = getConfig(config);

    return tableViewConfig.map(c -> c
        .getColumns()
        .stream()
        .map(ViewColumn::getText)
        .toList()).orElseGet(() -> List.of(MISSING_LABEL));
  }
}
