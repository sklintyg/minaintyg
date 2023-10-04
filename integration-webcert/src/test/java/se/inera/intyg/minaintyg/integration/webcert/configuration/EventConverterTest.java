package se.inera.intyg.minaintyg.integration.webcert.configuration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.CertificateEvent;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateMetadataDTO;

class EventConverterTest {

    //inget event (om inte skickat/förlängt = inget event) - KLART
    //ett event - KLART
    //flera event

    //ersätter
    //kompletterar = ersätter
    //ersatt
    //kompletterat = ersatt
    //skickat till recipient - KLART

    private final EventConverter eventConverter = new EventConverter();

    @Test
    void shallConvertSentCertificateToSentEvent() {
        final var expectedEvent = List.of(
            CertificateEvent.builder()
                .description("Skickat till recipientName")
                .build()
        );

        final var metadataDTO = CertificateMetadataDTO.builder()
            .sent(true)
            .sentTo("recipientName")
            .build();

        final var actualEvents = eventConverter.convert(metadataDTO);
        assertEquals(expectedEvent, actualEvents);
    }

    @Test
    void shallReturnEmptyListIfUnsentCertificate() {
        final var metadataDTO = CertificateMetadataDTO.builder()
            .sent(false)
            .sentTo("recipientName")
            .build();

        final var actualEvents = eventConverter.convert(metadataDTO);
        assertEquals(Collections.emptyList(), actualEvents);
    }

    //Intyg som är ersatt/kompletterat
    // Skickat intyg som ersätter/kompletterar ett annat intyg

}