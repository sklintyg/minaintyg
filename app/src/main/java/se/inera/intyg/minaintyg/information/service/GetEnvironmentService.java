package se.inera.intyg.minaintyg.information.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GetEnvironmentService {

  @Value("${environment:prod}")
  private String environmentType;


  public String get() {
    if (environmentType.isBlank()) {
      return null;
    } else {
      return environmentType;
    }
  }
}
