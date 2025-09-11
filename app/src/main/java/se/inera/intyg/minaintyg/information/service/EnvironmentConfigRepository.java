package se.inera.intyg.minaintyg.information.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
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

  private Map<String, DynamicLink> linkMap;

  @PostConstruct
  public void init() {
    try {
      var inputStream = resourceLoader.getResource(location).getInputStream();
      var root = objectMapper.readTree(inputStream);
      var items = root.path("menu").path("items");
      List<DynamicLink> dynamicLinks = objectMapper.readValue(
          objectMapper.treeAsTokens(items),
          new TypeReference<>() {
          }
      );
      linkMap = dynamicLinks.stream()
          .collect(Collectors.toMap(DynamicLink::getId, Function.identity()));
    } catch (Exception e) {
      throw new IllegalStateException("Failed to load dynamic links for environment: " + location,
          e);
    }
  }

  public Map<String, DynamicLink> get() {
    return linkMap;
  }
}
