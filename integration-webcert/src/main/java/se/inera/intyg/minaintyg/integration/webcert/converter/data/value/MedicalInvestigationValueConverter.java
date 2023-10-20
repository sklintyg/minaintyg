package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigMedicalInvestigation;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CodeItem;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueMedicalInvestigation;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueMedicalInvestigationList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class MedicalInvestigationValueConverter extends AbstractValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.MEDICAL_INVESTIGATION_LIST;
  }

  @Override
  protected CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var value = getValue(element.getValue());
    final var config = getConfig(element.getConfig());
    return createTableValue(value, config);
  }

  private Optional<CertificateDataConfigMedicalInvestigation> getConfig(
      CertificateDataConfig config) {
    if (config instanceof CertificateDataConfigMedicalInvestigation configMedicalInvestigation) {
      return Optional.of(configMedicalInvestigation);
    }
    return Optional.empty();
  }

  private Optional<List<CertificateDataValueMedicalInvestigation>> getValue(
      CertificateDataValue value) {
    if (value instanceof CertificateDataValueMedicalInvestigationList investigationListValue) {
      return Optional.ofNullable(investigationListValue.getList());
    }
    return Optional.empty();
  }

  private static CertificateQuestionValue createTableValue(
      Optional<List<CertificateDataValueMedicalInvestigation>> values,
      Optional<CertificateDataConfigMedicalInvestigation> config) {
    if (isEmpty(values)) {
      return NOT_PROVIDED_VALUE;
    }

    return CertificateQuestionValueTable.builder()
        .headings(
            row(
                header(config, CertificateDataConfigMedicalInvestigation::getTypeText),
                header(config, CertificateDataConfigMedicalInvestigation::getDateText),
                header(config, CertificateDataConfigMedicalInvestigation::getInformationSourceText)
            )
        )
        .values(
            rows(values, config)
        )
        .build();
  }

  private static List<List<String>> rows(
      Optional<List<CertificateDataValueMedicalInvestigation>> values,
      Optional<CertificateDataConfigMedicalInvestigation> config) {
    return values.map(investigationList ->
            investigationList.stream()
                .filter(investigation -> !isValueEmpty(investigation))
                .map(investigation ->
                    List.of(
                        convertInvestigationCode(investigation, config),
                        getDate(investigation),
                        getInformationSource(investigation)
                    ))
                .toList()
        )
        .orElse(Collections.emptyList());
  }

  private static String getInformationSource(
      CertificateDataValueMedicalInvestigation investigation) {
    return investigation.getInformationSource() != null
        && investigation.getInformationSource().getText() != null
        ? investigation.getInformationSource().getText() : MISSING_LABEL;
  }

  private static String getDate(CertificateDataValueMedicalInvestigation investigation) {
    return investigation.getDate() != null
        && investigation.getDate().getDate() != null
        ? investigation.getDate().getDate().toString() : MISSING_LABEL;
  }

  private static String convertInvestigationCode(
      CertificateDataValueMedicalInvestigation investigation,
      Optional<CertificateDataConfigMedicalInvestigation> config) {
    if (investigation.getInvestigationType() == null
        || investigation.getInvestigationType().getCode() == null) {
      return MISSING_LABEL;
    }

    return config.flatMap(investigationConfig -> investigationConfig.getList().stream().findAny())
        .map(medicalInvestigation ->
            medicalInvestigation.getTypeOptions().stream()
                .filter(
                    item -> item.getCode().equals(investigation.getInvestigationType().getCode()))
                .findAny()
                .map(CodeItem::getLabel)
                .orElse(investigation.getInvestigationType().getCode())
        )
        .orElse(investigation.getInvestigationType().getCode());

  }

  private static boolean isEmpty(Optional<List<CertificateDataValueMedicalInvestigation>> values) {
    return values.map(MedicalInvestigationValueConverter::isValueListEmpty).orElse(true);
  }

  private static boolean isValueListEmpty(List<CertificateDataValueMedicalInvestigation> values) {
    return values.isEmpty()
        || values.stream().allMatch(MedicalInvestigationValueConverter::isValueEmpty);
  }

  private static boolean isValueEmpty(
      CertificateDataValueMedicalInvestigation medicalInvestigation) {
    return (medicalInvestigation.getDate() == null
        || medicalInvestigation.getDate().getDate() == null)
        && (medicalInvestigation.getInvestigationType() == null
        || medicalInvestigation.getInvestigationType().getCode() == null)
        && (medicalInvestigation.getInformationSource() == null
        || medicalInvestigation.getInformationSource().getText() == null);
  }

  private static String header(Optional<CertificateDataConfigMedicalInvestigation> config,
      Function<CertificateDataConfigMedicalInvestigation, String> getLabel) {
    return config.map(getLabel).orElse(MISSING_LABEL);
  }

  private static List<String> row(String... headers) {
    return List.of(headers);
  }
}
