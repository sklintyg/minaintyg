package se.inera.intyg.minaintyg.information.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.information.service.model.Elva77MenuConfig;

@Component
@RequiredArgsConstructor
public class Elva77LinkLoader {

  private final ObjectMapper mapper;

  public Elva77MenuConfig load(Resource resource) throws IllegalStateException {
    try (var is = resource.getInputStream()) {
      return mapper.readValue(is, Elva77MenuConfig.class);
    } catch (IOException e) {
      throw new IllegalStateException("Failed to load or parse resource: " + resource, e);
    }
  }
}
