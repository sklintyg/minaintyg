package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigSickLeavePeriod;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxDateRange;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateRangeList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class DateRangeListValueConverter extends AbstractValueConverter {

  private static final String LABEL_HEADING = "Nedsättningsgrad";
  private static final String FROM_HEADING = "Från och med";
  private static final String TO_HEADING = "Till och med";

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.DATE_RANGE_LIST;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    return getDateRangeListValue(element.getValue())
        .map(dateRangeList ->
            mapDateRangeList(
                dateRangeList,
                getIdLabelHash(element.getConfig())
            )
        )
        .orElse(NOT_PROVIDED_VALUE);
  }

  private static CertificateQuestionValue mapDateRangeList(
      CertificateDataValueDateRangeList dateRangeList, Map<String, String> idLabelHash) {
    if (isEmpty(dateRangeList)) {
      return NOT_PROVIDED_VALUE;
    }

    return CertificateQuestionValueTable.builder()
        .headings(
            createHeadings()
        )
        .values(
            createValues(dateRangeList, idLabelHash)
        )
        .build();
  }

  private Map<String, String> getIdLabelHash(CertificateDataConfig config) {
    final var sickLeavePeriodConfig = getSickLeavePeriodConfig(config);
    return sickLeavePeriodConfig.map(configSickLeavePeriod ->
            configSickLeavePeriod.getList().stream()
                .collect(
                    Collectors.toMap(CheckboxDateRange::getId, CheckboxDateRange::getLabel)
                )
        )
        .orElse(Collections.emptyMap());
  }

  private Optional<CertificateDataConfigSickLeavePeriod> getSickLeavePeriodConfig(
      CertificateDataConfig config) {
    if (config instanceof CertificateDataConfigSickLeavePeriod configSickLeavePeriod) {
      return Optional.of(configSickLeavePeriod);
    }
    return Optional.empty();
  }

  private Optional<CertificateDataValueDateRangeList> getDateRangeListValue(
      CertificateDataValue value) {
    if (value instanceof CertificateDataValueDateRangeList dateRangeList) {
      return Optional.of(dateRangeList);
    }
    return Optional.empty();
  }

  private static boolean isEmpty(CertificateDataValueDateRangeList dateRangeList) {
    return dateRangeList.getList() == null || dateRangeList.getList().isEmpty();
  }

  private static List<String> createHeadings() {
    return List.of(LABEL_HEADING, FROM_HEADING, TO_HEADING);
  }

  private static List<List<String>> createValues(CertificateDataValueDateRangeList dateRangeList,
      Map<String, String> idLabelHash) {
    return dateRangeList.getList()
        .stream()
        .map(dateRange ->
            List.of(
                idLabelHash.getOrDefault(dateRange.getId(), dateRange.getId()),
                dateRange.getFrom().toString(),
                dateRange.getTo().toString()
            )
        )
        .collect(
            Collectors.collectingAndThen(
                Collectors.toList(),
                DateRangeListValueConverter::reverseList
            )
        );
  }

  private static List<List<String>> reverseList(List<List<String>> list) {
    Collections.reverse(list);
    return list;
  }
}
