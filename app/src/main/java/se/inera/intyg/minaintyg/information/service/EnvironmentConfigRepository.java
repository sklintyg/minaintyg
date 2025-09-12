package se.inera.intyg.minaintyg.information.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import se.inera.intyg.minaintyg.information.dto.DynamicLink;

@RequiredArgsConstructor
@Service
public class EnvironmentConfigRepository {

  @Value("${application.environment.links.file}")
  private String location;

  private final ObjectMapper objectMapper;
  private final ResourceLoader resourceLoader;

  private List<DynamicLink> linkList;

  @PostConstruct
  public void init() {
    try {
      var inputStream = resourceLoader.getResource(location).getInputStream();
      var root = objectMapper.readTree(inputStream);
      var items = root.path("menu").path("items");
      linkList = objectMapper.readValue(
          objectMapper.treeAsTokens(items),
          new TypeReference<>() {
          }
      );
    } catch (Exception e) {
      throw new IllegalStateException("Failed to load dynamic links for environment: " + location,
          e);
    }
  }

  public List<DynamicLink> get() {
    return linkList;
  }
}
