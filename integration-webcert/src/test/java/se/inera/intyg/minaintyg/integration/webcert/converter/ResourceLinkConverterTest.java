package se.inera.intyg.minaintyg.integration.webcert.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.ResourceLinkType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.ResourceLinkDTO;

@ExtendWith(MockitoExtension.class)
class ResourceLinkConverterTest {

  private static final ResourceLinkDTO ORIGINAL = ResourceLinkDTO
      .builder()
      .name("NAME")
      .description("DESCRIPTION")
      .enabled(true)
      .type(ResourceLinkType.ADJUST_CERTIFICATE)
      .build();

  @InjectMocks
  ResourceLinkConverter resourceLinkConverter;

  @Test
  void shouldConvertName() {
    final var response = resourceLinkConverter.convert(ORIGINAL);

    assertEquals(ORIGINAL.getName(), response.getName());
  }

  @Test
  void shouldConvertType() {
    final var response = resourceLinkConverter.convert(ORIGINAL);

    assertEquals(ORIGINAL.getType(), response.getType());
  }

  @Test
  void shouldConvertDescription() {
    final var response = resourceLinkConverter.convert(ORIGINAL);

    assertEquals(ORIGINAL.getDescription(), response.getDescription());
  }

  @Test
  void shouldConvertEnabled() {
    final var response = resourceLinkConverter.convert(ORIGINAL);

    assertEquals(ORIGINAL.isEnabled(), response.isEnabled());
  }
}