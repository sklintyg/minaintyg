package se.inera.intyg.minaintyg.integration.webcert.converter.data.value;

import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValue;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCodeList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;

@Component
public class CodeListValueConverter extends AbstractValueConverter {

  private static final String TRUE_BOOLEAN = "Ja";
  private static final String FALSE_BOOLEAN = "Nej";
  // TODO: Remove CODE_MAP and use the config of the element instead
  private static final Map<String, String> CODE_MAP = Map.ofEntries(
      Map.entry("NUVARANDE_ARBETE", "Nuvarande arbete"),
      Map.entry("ARBETSSOKANDE", "Arbetssökande"),
      Map.entry("FORALDRALEDIG", "Föräldraledighet för vård av barn"),
      Map.entry("STUDIER", "Studier"),
      Map.entry("EJ_AKTUELLT", "Inte aktuellt"),
      Map.entry("ARBETSTRANING", "Arbetsträning"),
      Map.entry("ARBETSANPASSNING", "Arbetsanpassning"),
      Map.entry("SOKA_NYTT_ARBETE", "Söka nytt arbete"),
      Map.entry("BESOK_ARBETSPLATS", "Besök på arbetsplatsen"),
      Map.entry("ERGONOMISK", "Ergonomisk bedömning"),
      Map.entry("HJALPMEDEL", "Hjälpmedel"),
      Map.entry("KONFLIKTHANTERING", "Konflikthantering"),
      Map.entry("KONTAKT_FHV", "Kontakt med företagshälsovård"),
      Map.entry("OMFORDELNING", "Omfördelning av arbetsuppgifter"),
      Map.entry("OVRIGA_ATGARDER", "Övrigt")
  );

  @Override
  public CertificateDataValueType getType() {
    return CertificateDataValueType.CODE_LIST;
  }

  @Override
  public CertificateQuestionValue convertToValue(CertificateDataElement element) {
    final var value = ((CertificateDataValueCodeList) element.getValue()).getList();
    if (value == null || value.isEmpty()) {
      return notProvidedValueList();
    }
    return CertificateQuestionValueList.builder()
        .values(
            value.stream()
                .map(CertificateDataValueCode::getCode)
                .map(CodeListValueConverter::codeToString)
                .toList()
        )
        .build();
  }

  private static CertificateQuestionValueList notProvidedValueList() {
    return CertificateQuestionValueList.builder()
        .values(
            List.of(NOT_PROVIDED)
        )
        .build();
  }

  private static String codeToString(String code) {
    return CODE_MAP.getOrDefault(code, null);
  }
}
