package se.inera.intyg.minaintyg.information.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import se.inera.intyg.minaintyg.information.service.model.DynamicLink;
import se.inera.intyg.minaintyg.information.service.model.Elva77MenuConfig;

@Slf4j
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
    } catch (IOException e) {
      log.error("Failed to load dynamic links from file: {}", resource, e);
      throw new IllegalStateException(e);
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
