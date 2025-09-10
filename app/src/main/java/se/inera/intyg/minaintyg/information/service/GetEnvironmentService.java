package se.inera.intyg.minaintyg.information.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GetEnvironmentService {

  @Value("${application.environment:prod}")
  private String environmentType;


  public String get() {
    if (environmentType.isBlank()) {
      return null;
    }
    return environmentType;
  }
}
