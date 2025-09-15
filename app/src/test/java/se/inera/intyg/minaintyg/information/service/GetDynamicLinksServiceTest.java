package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import se.inera.intyg.minaintyg.information.dto.DynamicLinkDTO;
import se.inera.intyg.minaintyg.information.service.model.DynamicLink;
import se.inera.intyg.minaintyg.information.service.model.Elva77MenuConfig;

@ExtendWith(MockitoExtension.class)
class GetDynamicLinksServiceTest {

  @Mock
  private DynamicLinkRepository dynamicLinkRepository;
  @Mock
  private DynamicLinkConverter dynamicLinkConverter;

  public static final Elva77MenuConfig EXPECTED_MENU = Elva77MenuConfig.builder().build();

  @InjectMocks
  private GetDynamicLinksService service;

  @Test
  void shouldReturnMenuStructureWhenNoEnvironmentIsProvided() {

    when(dynamicLinkRepository.get()).thenReturn(EXPECTED_MENU);
    final var menu = service.getMenuConfig();
    assertNotNull(menu);
    assertEquals(EXPECTED_MENU, menu);
  }

  @Test
  void shouldReturnDynamicLinksDTOWithProdLinksWhenEnvironmentIsProd() {

    DynamicLink link = DynamicLink.builder().build();
    DynamicLinkDTO expectedDTO = DynamicLinkDTO.builder().build();

    ReflectionTestUtils.setField(service, "environmentType", "prod");
    when(dynamicLinkRepository.get("prod")).thenReturn(List.of(link));
    when(dynamicLinkConverter.convert(link, "prod")).thenReturn(expectedDTO);

    List<DynamicLinkDTO> result = service.get();

    assertEquals(1, result.size());
    assertEquals(expectedDTO, result.getFirst());
  }
}
