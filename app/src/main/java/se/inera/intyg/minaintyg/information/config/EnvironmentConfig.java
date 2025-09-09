package se.inera.intyg.minaintyg.information.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class EnvironmentConfig {

  @Value("${environment}")
  public String EnvironmentType;
}
