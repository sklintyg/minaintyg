package se.inera.intyg.minaintyg.information.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import se.inera.intyg.minaintyg.information.service.model.DynamicLink;
import se.inera.intyg.minaintyg.information.service.model.Elva77MenuConfig;

@RequiredArgsConstructor
@Repository
public class DynamicLinkRepository {

  @Value("${1177.menu.links.file}")
  private Resource resource;

  private final ObjectMapper objectMapper;

  private final Elva77LinkLoader elva77LinkLoader;

  private Elva77MenuConfig linkList;

  @PostConstruct
  public void init() {
    try {
      this.linkList = elva77LinkLoader.load(resource, objectMapper);
    } catch (Exception e) {
      throw new IllegalStateException(
          "Failed to load dynamic links for environment: " + resource.getFilename(), e);
    }
  }

  public Elva77MenuConfig get() {
    return linkList;
  }

  public List<DynamicLink> get(String environmentType) {
    return linkList.getMenu().getItems().stream()
        .filter(link -> link.getUrl().containsKey(environmentType))
        .toList();
  }
}
