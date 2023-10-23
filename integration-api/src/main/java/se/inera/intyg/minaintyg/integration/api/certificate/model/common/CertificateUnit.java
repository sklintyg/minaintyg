package se.inera.intyg.minaintyg.integration.api.certificate.model.common;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CertificateUnit {

  String id;
  String name;
  String address;
  String zipCode;
  String city;
  String phoneNumber;
  String email;
}
