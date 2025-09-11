package se.inera.intyg.minaintyg.information.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GetEnvironmentService {

  @Value("${application.environment}")
  private String environmentType;

  public String get() {
    return environmentType;
  }
}
