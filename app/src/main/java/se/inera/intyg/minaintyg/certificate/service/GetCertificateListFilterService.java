package se.inera.intyg.minaintyg.certificate.service;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.certificate.service.dto.GetCertificateFilterResponse;
import se.inera.intyg.minaintyg.certificate.service.dto.ListCertificatesRequest;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateListItem;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateStatusType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateTypeFilter;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateUnit;

@Service
@RequiredArgsConstructor
public class GetCertificateListFilterService {

  private final ListCertificatesService listCertificatesService;

  public GetCertificateFilterResponse get() {

    final var response = listCertificatesService.get(ListCertificatesRequest.builder().build());
    final var certificates = response.getContent();

    return GetCertificateFilterResponse
        .builder()
        .total(certificates.size())
        .certificateTypes(getCertificateTypes(certificates))
        .years(getYears(certificates))
        .units(getUnits(certificates))
        .statuses(getStatuses())
        .build();
  }

  private List<String> getYears(List<CertificateListItem> certificateListItems) {
    return getList(
        certificateListItems,
        certificate -> certificate.getIssued().getYear()
    ).stream()
        .sorted(Comparator.reverseOrder())
        .map(String::valueOf)
        .toList();
  }

  private List<CertificateTypeFilter> getCertificateTypes(
      List<CertificateListItem> certificateListItems) {
    return getList(certificateListItems, CertificateListItem::getType)
        .stream()
        .map(this::convertCertificateTypeFilter)
        .distinct()
        .sorted(Comparator.comparing(CertificateTypeFilter::getName))
        .toList();
  }

  private CertificateTypeFilter convertCertificateTypeFilter(CertificateType type) {
    return CertificateTypeFilter
        .builder()
        .id(type.getId())
        .name(type.getName())
        .build();
  }

  private List<CertificateUnit> getUnits(List<CertificateListItem> certificateListItems) {
    return getList(certificateListItems, CertificateListItem::getUnit)
        .stream()
        .sorted(Comparator.comparing(CertificateUnit::getName))
        .toList();
  }

  private List<CertificateStatusType> getStatuses() {
    return List.of(CertificateStatusType.NOT_SENT, CertificateStatusType.SENT);
  }

  private <T> List<T> getList(List<CertificateListItem> certificateListItems,
      Function<CertificateListItem, T> getValueFunction) {
    return certificateListItems
        .stream()
        .map(getValueFunction)
        .distinct()
        .toList();
  }
}
