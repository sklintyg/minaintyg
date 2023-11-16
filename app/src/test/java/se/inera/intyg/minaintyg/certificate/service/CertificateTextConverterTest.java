package se.inera.intyg.minaintyg.certificate.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateLink;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateText;

@ExtendWith(MockitoExtension.class)
class CertificateTextConverterTest {

  private static final CertificateText TEXT_NO_LINKS = CertificateText.builder()
      .text("TEXT_NO_LINKS").build();
  private static final CertificateText TEXT_WITH_LINK = CertificateText
      .builder()
      .text("Text {L1} with link")
      .links(
          List.of(
              CertificateLink.builder().id("L1").url("https://test.com").name("Länknamn").build()
          )
      )
      .build();

  private static final CertificateText TEXT_WITH_SAME_LINKS = CertificateText
      .builder()
      .text("Text {L1} with link {L1}")
      .links(
          List.of(
              CertificateLink.builder().id("L1").url("https://test.com").name("Länknamn").build()
          )
      )
      .build();

  private static final CertificateText TEXT_WITH_LINKS = CertificateText
      .builder()
      .text("Text {L1} with links {L2}")
      .links(
          List.of(
              CertificateLink.builder().id("L1").url("https://test.com").name("Länknamn").build(),
              CertificateLink.builder().id("L2").url("https://test2.com").name("Länknamn 2").build()
          )
      )
      .build();


  @InjectMocks
  private CertificateTextConverter certificateTextConverter;

  @Test
  void shouldReturnTextWithoutLink() {
    final var response = certificateTextConverter.convert(TEXT_NO_LINKS);

    assertEquals(TEXT_NO_LINKS.getText(), response);
  }

  @Test
  void shouldReturnTextWithFormattedLink() {
    final var response = certificateTextConverter.convert(TEXT_WITH_LINK);

    assertEquals(
        "Text <IDSLink href=\"https://test.com\">Länknamn</IDSLink> with link",
        response);
  }

  @Test
  void shouldReturnTextWithFormattedLinks() {
    final var response = certificateTextConverter.convert(TEXT_WITH_LINKS);

    assertEquals(
        "Text <IDSLink href=\"https://test.com\">Länknamn</IDSLink> with links <IDSLink href=\"https://test2.com\">Länknamn 2</IDSLink>",
        response);
  }

  @Test
  void shouldReturnTextWithFormattedLinkTwice() {
    final var response = certificateTextConverter.convert(TEXT_WITH_SAME_LINKS);

    assertEquals(
        "Text <IDSLink href=\"https://test.com\">Länknamn</IDSLink> with link <IDSLink href=\"https://test.com\">Länknamn</IDSLink>",
        response);
  }
}