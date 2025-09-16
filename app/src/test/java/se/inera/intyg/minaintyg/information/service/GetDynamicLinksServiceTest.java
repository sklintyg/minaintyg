package se.inera.intyg.minaintyg.information.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.information.dto.DynamicLinkDTO;
import se.inera.intyg.minaintyg.information.service.model.DynamicLink;

@ExtendWith(MockitoExtension.class)
class GetDynamicLinksServiceTest {

  @Mock
  private DynamicLinkRepository dynamicLinkRepository;
  @Mock
  private DynamicLinkConverter dynamicLinkConverter;

  @InjectMocks
  private GetDynamicLinksService service;

  @Test
  void shouldReturnDynamicLinksDTOWithProdLinksWhenEnvironmentIsProd() {

    final var link = DynamicLink.builder().build();
    final var expectedDTO = DynamicLinkDTO.builder().build();

    when(dynamicLinkRepository.get()).thenReturn(List.of(link));
    when(dynamicLinkConverter.convert(link)).thenReturn(expectedDTO);

    final var result = service.get();

    assertEquals(expectedDTO, result.getFirst());
  }
}
