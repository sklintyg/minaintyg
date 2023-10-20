package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.List;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigIcf;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataIcfValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class IcfValueConverter extends AbstractValueConverter {

  public static final String LINE_BREAK = "\n";
  public static final String DOUBLE_LINE_BREAK = "\n\n";

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.ICF;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var value = ((CertificateDataIcfValue) element.getValue());
    final var icfCodes = value.getIcfCodes();
    final var text = value.getText();

    return hasIcfCodes(icfCodes) ? getTextWithIcfCodes(element, icfCodes, text) : getText(text);
  }

  private static boolean hasIcfCodes(List<String> icfCodes) {
    return icfCodes != null && !icfCodes.isEmpty();
  }

  private static CertificateQuestionValueText getTextWithIcfCodes(
      CertificateDataElement element, List<String> icfCodes, String text) {
    final var collectionsLabel = ((CertificateDataConfigIcf) element.getConfig()).getCollectionsLabel();
    final var formattedString = buildFormattedString(icfCodes, text, collectionsLabel);

    return CertificateQuestionValueText.builder()
        .value(formattedString)
        .build();
  }

  private static String buildFormattedString(List<String> icfCodes, String text,
      String collectionsLabel) {
    final var stringOfIcfCodes = String.join(" - ", icfCodes);
    return collectionsLabel + LINE_BREAK + stringOfIcfCodes + DOUBLE_LINE_BREAK + text;
  }

  private static CertificateQuestionValueText getText(String value) {
    return CertificateQuestionValueText.builder()
        .value(value != null ? value : NOT_PROVIDED)
        .build();
  }
}