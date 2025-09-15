package se.inera.intyg.minaintyg.information.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import se.inera.intyg.minaintyg.information.service.model.Elva77MenuConfig;

@Component
public class Elva77LinkLoader {

  public Elva77MenuConfig load(Resource resource, ObjectMapper mapper) throws IOException {
    return mapper.readValue(resource.getInputStream(), Elva77MenuConfig.class);
  }
}
