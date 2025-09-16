package se.inera.intyg.minaintyg.information.service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Stream;
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

  @Value("${1177.menu.setting.name:Inställningar}")
  private String name;

  @Value("${1177.menu.setting.id:99}")
  private String id;

  private final Elva77LinkLoader elva77LinkLoader;

  private Elva77MenuConfig linkList;

  @PostConstruct
  public void init() {
    this.linkList = elva77LinkLoader.load(resource);
  }

  public List<DynamicLink> get(String environment, String settingUrl) {
    List<DynamicLink> links = linkList.getMenu().getItems().stream()
        .map(link -> DynamicLink.builder()
            .id(link.getId())
            .name(link.getName())
            .url(link.getUrl().get(environment))
            .build())
        .toList();

    return appendLink(settingUrl, links);
  }

  private List<DynamicLink> appendLink(String url, List<DynamicLink> dynamicLinks) {
    DynamicLink settingLink = DynamicLink.builder()
        .id(id)
        .name(name)
        .url(url)
        .build();
    return Stream.concat(dynamicLinks.stream(), Stream.of(settingLink)).toList();
  }
}
