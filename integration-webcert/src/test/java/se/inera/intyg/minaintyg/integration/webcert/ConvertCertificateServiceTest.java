package se.inera.intyg.minaintyg.integration.webcert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueList;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateResponseDTO;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfig;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCategory;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueCodeList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueDateList;

@ExtendWith(MockitoExtension.class)
class ConvertCertificateServiceTest {

  private static final String TITLE = "title";
  private static final String TEXT_VALUE = "textValue";
  private static final String LABEL_VALUE = "labelValue";
  private static final String NOT_PROVIDED = "Ej angivet";
  private static final String TRUE_BOOLEAN = "Ja";
  private static final String FALSE_BOOLEAN = "Nej";
  private static final String CONFIG_ID = "configId";
  private static final String CODE = "NUVARANDE_ARBETE";
  private static final String CODE_MATCH = "Nuvarande arbete";
  private static final CertificateResponseDTO CERTIFICATE_RESPONSE_DTO = CertificateResponseDTO.builder()
      .certificate(
          CertificateDTO.builder()
              .data(Collections.emptyMap())
              .build())
      .build();
  @Mock
  private CategoryQuestionOrganizer categoryQuestionOrganizer;
  @InjectMocks
  private ConvertCertificateService convertCertificateService;

  @Nested
  class CategoryTitle {

    @Test
    void shouldAddCategoryTitle() {
      final var elements = List.of(
          List.of(
              createElement(CertificateDataConfigCategory.builder()
                  .text(TITLE)
                  .build(), null)));
      when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
      final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
      assertEquals(TITLE, result.get(0).getTitle());
    }

    @Test
    void shouldAddMultipleCategoryTitle() {
      final var elements = List.of(
          List.of(
              createElement(CertificateDataConfigCategory.builder()
                  .text(TITLE)
                  .build(), null)),
          List.of(
              createElement(CertificateDataConfigCategory.builder()
                  .text(TITLE)
                  .build(), null)),
          List.of(
              createElement(CertificateDataConfigCategory.builder()
                  .text(TITLE)
                  .build(), null))
      );
      when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
      final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
      assertEquals(TITLE, result.get(0).getTitle());
      assertEquals(TITLE, result.get(1).getTitle());
      assertEquals(TITLE, result.get(2).getTitle());
    }

    @Test
    void shouldHandleNullTitle() {
      final var elements = List.of(
          List.of(createElement(CertificateDataConfigCategory.builder().build(), null)
          )
      );
      when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
      final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
      assertNull(result.get(0).getTitle());
    }
  }


  @Nested
  class ToCertificateQuestion {

    @Nested
    class Title {

      @Test
      void shouldIncludeTitleIfPresent() {
        final var elements = List.of(
            List.of(
                createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(
                    CertificateDataConfigTextArea.builder()
                        .text(TEXT_VALUE)
                        .build(), null
                )
            )
        );
        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertEquals(TEXT_VALUE, result.get(0).getQuestions().get(0).getTitle());
      }

      @Test
      void shouldNotIncludeTitleIfNotPresent() {
        final var elements = List.of(
            List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(
                    CertificateDataConfigTextArea.builder()
                        .build(), null
                )
            )
        );
        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertNull(result.get(0).getQuestions().get(0).getTitle());
      }
    }

    @Nested
    class Label {

      @Test
      void shouldIncludeLabelIfPresent() {
        final var elements = List.of(
            List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(
                    CertificateDataConfigTextArea.builder()
                        .label(LABEL_VALUE)
                        .build(), null
                )
            )
        );
        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertEquals(LABEL_VALUE, result.get(0).getQuestions().get(0).getLabel());
      }

      @Test
      void shouldNotIncludeLabelIfNotPresent() {
        final var elements = List.of(
            List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(
                    CertificateDataConfigTextArea.builder()
                        .build(), null
                )
            )
        );
        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertNull(result.get(0).getQuestions().get(0).getLabel());
      }
    }

    @Nested
    class TextValue {

      @Test
      void shouldConvertCertificateDataTextValue() {
        final var elements = List.of(
            List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(
                    CertificateDataConfigTextArea.builder().build(),
                    CertificateDataTextValue.builder()
                        .text(TEXT_VALUE)
                        .build()
                )
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .value(CertificateQuestionValueText.builder()
                .value(TEXT_VALUE)
                .build())
            .build();

        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }

      @Test
      void shouldConvertCertificateDataTextValueWithNoValue() {
        final var elements = List.of(
            List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(
                    CertificateDataConfigTextArea.builder().build(),
                    CertificateDataTextValue.builder()
                        .build()
                )
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .value(CertificateQuestionValueText.builder()
                .value(NOT_PROVIDED)
                .build())
            .build();

        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }
    }

    @Nested
    class RadioBoolean {

      @Test
      void shouldConvertCertificateDataValueRadioBooleanWithValueTrue() {
        final var elements = List.of(
            List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(CertificateDataConfigRadioBoolean.builder().build(),
                    CertificateDataValueBoolean.builder()
                        .selected(true)
                        .build())
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueText.builder()
                    .value(TRUE_BOOLEAN)
                    .build())
            .build();
        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }

      @Test
      void shouldConvertCertificateDataValueRadioBooleanWithValueFalse() {
        final var elements = List.of(
            List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(CertificateDataConfigRadioBoolean.builder().build(),
                    CertificateDataValueBoolean.builder()
                        .selected(false)
                        .build())
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueText.builder()
                    .value(FALSE_BOOLEAN)
                    .build())
            .build();
        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }

      @Test
      void shouldConvertCertificateDataValueRadioBooleanWithNoValue() {
        final var elements = List.of(
            List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(CertificateDataConfigRadioBoolean.builder().build(),
                    CertificateDataValueBoolean.builder()
                        .build())
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueText.builder()
                    .value(NOT_PROVIDED)
                    .build())
            .build();
        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }
    }

    @Nested
    class DateList {

      @Test
      void shouldConvertCertificateDataDateList() {
        final var elements = List.of(
            List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(
                    CertificateDataConfigCheckboxMultipleDate.builder()
                        .label(LABEL_VALUE)
                        .list(
                            List.of(
                                CheckboxMultipleDate.builder()
                                    .id(CONFIG_ID)
                                    .build()
                            )
                        )
                        .build(),
                    CertificateDataValueDateList.builder()
                        .list(
                            List.of(
                                CertificateDataValueDate.builder()
                                    .id(CONFIG_ID)
                                    .date(LocalDate.now())
                                    .build()
                            )
                        )
                        .build()
                )
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .label(LABEL_VALUE)
            .subQuestions(
                List.of(
                    CertificateQuestion.builder()
                        .value(CertificateQuestionValueText.builder()
                            .value(LocalDate.now().toString())
                            .build())
                        .build()
                )
            ).build();

        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }

      @Test
      void shouldConvertCertificateDataDateListWithNoValue() {
        final var elements = List.of(
            List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(
                    CertificateDataConfigCheckboxMultipleDate.builder()
                        .label(LABEL_VALUE)
                        .list(
                            List.of(
                                CheckboxMultipleDate.builder()
                                    .id(CONFIG_ID)
                                    .build()
                            )
                        )
                        .build(),
                    CertificateDataValueDateList.builder()
                        .build()
                )
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .label(LABEL_VALUE)
            .subQuestions(
                List.of(
                    CertificateQuestion.builder()
                        .value(CertificateQuestionValueText.builder()
                            .value(NOT_PROVIDED)
                            .build())
                        .build()
                )
            ).build();

        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }
    }

    @Nested
    class CodeList {

      @Test
      void shouldConvertCertificateDataCodeList() {
        final var elements = List.of(
            List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(CertificateDataConfigTextArea.builder().build(),
                    CertificateDataValueCodeList.builder()
                        .list(
                            List.of(
                                CertificateDataValueCode.builder()
                                    .code(CODE)
                                    .build()
                            )
                        )
                        .build())
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueList.builder()
                    .values(
                        List.of(
                            CODE_MATCH
                        )
                    )
                    .build())
            .build();
        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }

      @Test
      void shouldConvertCertificateDataCodeListIfNoValue() {
        final var elements = List.of(
            List.of(createElement(CertificateDataConfigCategory.builder().build(), null),
                createElement(CertificateDataConfigTextArea.builder().build(),
                    CertificateDataValueCodeList.builder()
                        .build())
            )
        );

        final var expectedResult = CertificateQuestion.builder()
            .value(
                CertificateQuestionValueList.builder()
                    .values(
                        List.of(
                            NOT_PROVIDED
                        )
                    )
                    .build())
            .build();
        when(categoryQuestionOrganizer.organize(any())).thenReturn(elements);
        final var result = convertCertificateService.convert(CERTIFICATE_RESPONSE_DTO);
        assertEquals(expectedResult, result.get(0).getQuestions().get(0));
      }
    }
  }


  private static CertificateDataElement createElement(CertificateDataConfig config,
      CertificateDataValue value) {
    return CertificateDataElement.builder()
        .config(config)
        .value(value)
        .build();
  }
}
