package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
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

  public static final String EMPTY = "";

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
    final var optionalConfig = getConfig(config);

    if (optionalConfig.isEmpty()) {
      return List.of(MISSING_LABEL);
    }

    final var emptyHeadingList = List.of(EMPTY);

    return Stream.concat(
            emptyHeadingList.stream(),
            optionalConfig.get()
                .getColumns()
                .stream()
                .map(ViewColumn::getText)
        )
        .toList();
  }
}
