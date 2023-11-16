package se.inera.intyg.minaintyg.integration.webcert.converter.text;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateLinkDTO;

@ExtendWith(MockitoExtension.class)
class CertificateLinkConverterTest {

  private static final CertificateLinkDTO link = CertificateLinkDTO
      .builder()
      .url("URL")
      .id("ID")
      .name("NAME")
      .build();

  @InjectMocks
  CertificateLinkConverter certificateLinkConverter;

  @Test
  void shouldConvertId() {
    final var response = certificateLinkConverter.convert(link);

    assertEquals(link.getId(), response.getId());
  }

  @Test
  void shouldConvertName() {
    final var response = certificateLinkConverter.convert(link);

    assertEquals(link.getName(), response.getName());
  }

  @Test
  void shouldConvertUrl() {
    final var response = certificateLinkConverter.convert(link);

    assertEquals(link.getUrl(), response.getUrl());
  }
}
