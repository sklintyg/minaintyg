package se.inera.intyg.minaintyg.integration.webcert.converter.availablefunction;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunction;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.AvailableFunctionType;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.Information;
import se.inera.intyg.minaintyg.integration.api.certificate.model.common.InformationType;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.AvailableFunctionDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.availablefunction.InformationDTO;

class AvailableFunctionConverterTest {

  public static final String BODY = "BODY";
  public static final String TITLE = "TITLE";
  public static final String NAME = "NAME";
  public static final String DESCRIPTION = "DESCRIPTION";
  public static final String INFORMATION_ID = "INFORMATION_ID";
  public static final String INFORMATION_TEXT = "INFORMATION_TEXT";
  private final AvailableFunctionConverter availableFunctionConverter = new AvailableFunctionConverter();

  @Test
  void shallReturnEmptyListIfNull() {
    assertEquals(Collections.emptyList(), availableFunctionConverter.convert(null));
  }

  @Test
  void shallReturnEmptyListIfEmpty() {
    assertEquals(Collections.emptyList(),
        availableFunctionConverter.convert(Collections.emptyList())
    );
  }

  @Test
  void shallReturnBody() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .body(BODY)
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .body(BODY)
                .build()
        )
    );

    assertEquals(value(expected).getBody(), value(actual).getBody());
  }

  @Test
  void shallReturnTitle() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .title(TITLE)
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .title(TITLE)
                .build()
        )
    );

    assertEquals(value(expected).getTitle(), value(actual).getTitle());
  }

  @Test
  void shallReturnName() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .name(NAME)
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .name(NAME)
                .build()
        )
    );

    assertEquals(value(expected).getName(), value(actual).getName());
  }

  @Test
  void shallReturnDescription() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .description(DESCRIPTION)
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .description(DESCRIPTION)
                .build()
        )
    );

    assertEquals(value(expected).getDescription(), value(actual).getDescription());
  }

  @Test
  void shallReturnType() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .type(AvailableFunctionType.PRINT_CERTIFICATE)
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .type(AvailableFunctionType.PRINT_CERTIFICATE)
                .build()
        )
    );

    assertEquals(value(expected).getType(), value(actual).getType());
  }

  @Test
  void shallReturnEnabled() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .enabled(true)
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .enabled(true)
                .build()
        )
    );

    assertEquals(value(expected).isEnabled(), value(actual).isEnabled());
  }

  @Test
  void shallReturnDisbled() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .enabled(false)
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .enabled(false)
                .build()
        )
    );

    assertEquals(value(expected).isEnabled(), value(actual).isEnabled());
  }

  @Test
  void shallReturnInformationListEmptyWhenNull() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .information(Collections.emptyList())
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .build()
        )
    );

    assertEquals(value(expected).getInformation(), value(actual).getInformation());
  }

  @Test
  void shallReturnInformationListEmptyWhenEmpty() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .information(Collections.emptyList())
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .information(Collections.emptyList())
                .build()
        )
    );

    assertEquals(value(expected).getInformation(), value(actual).getInformation());
  }

  @Test
  void shallReturnInformationListWithId() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .information(
                information(Information.builder()
                    .id(INFORMATION_ID)
                    .build()
                )
            )
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .information(
                    informationDTOS(
                        InformationDTO.builder()
                            .id(INFORMATION_ID)
                            .build()
                    )
                )
                .build()
        )
    );

    assertEquals(informationValue(expected).getId(), informationValue(actual).getId());
  }

  @Test
  void shallReturnInformationListWithType() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .information(
                information(Information.builder()
                    .type(InformationType.OPTIONS)
                    .build()
                )
            )
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .information(
                    informationDTOS(
                        InformationDTO.builder()
                            .type(InformationType.OPTIONS)
                            .build()
                    )
                )
                .build()
        )
    );

    assertEquals(informationValue(expected).getType(), informationValue(actual).getType());
  }

  @Test
  void shallReturnInformationListWithText() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .information(
                information(Information.builder()
                    .text(INFORMATION_TEXT)
                    .build()
                )
            )
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .information(
                    informationDTOS(
                        InformationDTO.builder()
                            .text(INFORMATION_TEXT)
                            .build()
                    )
                )
                .build()
        )
    );

    assertEquals(informationValue(expected).getText(), informationValue(actual).getText());
  }

  @Test
  void shallReturnInformationListWithMultipleInformation() {
    final var expected = availableFunctions(
        AvailableFunction.builder()
            .information(
                information(
                    Information.builder().build(),
                    Information.builder().build(),
                    Information.builder().build()
                )
            )
            .build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder()
                .information(
                    informationDTOS(
                        InformationDTO.builder().build(),
                        InformationDTO.builder().build(),
                        InformationDTO.builder().build()
                    )
                )
                .build()
        )
    );

    assertEquals(informationValue(expected), informationValue(actual));
  }

  @Test
  void shallReturnMultipleAvailableFunctions() {
    final var expected = availableFunctions(
        AvailableFunction.builder().information(Collections.emptyList()).build(),
        AvailableFunction.builder().information(Collections.emptyList()).build(),
        AvailableFunction.builder().information(Collections.emptyList()).build()
    );

    final var actual = availableFunctionConverter.convert(
        availableFunctionDTOs(
            AvailableFunctionDTO.builder().build(),
            AvailableFunctionDTO.builder().build(),
            AvailableFunctionDTO.builder().build()
        )
    );

    assertEquals(value(expected), value(actual));
  }

  private static List<Information> information(Information... information) {
    return List.of(
        information
    );
  }

  private static List<InformationDTO> informationDTOS(InformationDTO... informationDTO) {
    return List.of(
        informationDTO
    );
  }

  private static List<AvailableFunction> availableFunctions(
      AvailableFunction... availableFunction) {
    return List.of(
        availableFunction
    );
  }

  private static List<AvailableFunctionDTO> availableFunctionDTOs(
      AvailableFunctionDTO... availableFunctionDTO) {
    return List.of(availableFunctionDTO);
  }

  private static AvailableFunction value(List<AvailableFunction> actual) {
    return actual.get(0);
  }

  private static Information informationValue(List<AvailableFunction> actual) {
    return value(actual).getInformation().get(0);
  }
}