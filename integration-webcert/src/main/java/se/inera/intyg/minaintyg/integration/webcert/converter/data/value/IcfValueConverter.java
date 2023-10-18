package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigIcf;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataIcfValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class IcfValueConverter extends AbstractValueConverter {

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.ICF;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var icfCodes = ((CertificateDataIcfValue) element.getValue()).getIcfCodes();
    final var text = ((CertificateDataIcfValue) element.getValue()).getText();

    if (icfCodes != null) {
      final var collectionsLabel = ((CertificateDataConfigIcf) element.getConfig()).getCollectionsLabel();
      final var value = collectionsLabel + "\n" + icfCodes + "\n" + "\n" + text;

      return CertificateQuestionValueText.builder()
          .value(value)
          .build();
    }
    return getText(text);
  }

  private static CertificateQuestionValueText getText(String value) {
    return CertificateQuestionValueText.builder()
        .value(value != null ? value : NOT_PROVIDED)
        .build();
  }
}