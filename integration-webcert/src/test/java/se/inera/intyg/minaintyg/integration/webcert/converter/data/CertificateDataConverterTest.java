package se.inera.intyg.minaintyg.integration.webcert.converter.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateCategory;
import se.inera.intyg.minaintyg.integration.api.certificate.model.CertificateQuestion;
import se.inera.intyg.minaintyg.integration.api.certificate.model.value.CertificateQuestionValueText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.CertificateDataElement;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCategory;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataTextValue;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.value.CertificateDataValueType;
import se.inera.intyg.minaintyg.integration.webcert.converter.data.value.ValueConverter;

class CertificateDataConverterTest {

  public static final String CAT_ONE_ID = "CAT_ONE_ID";
  public static final String CAT_ONE_TEXT = "CAT_ONE_TEXT";
  public static final String CAT_TWO_ID = "CAT_TWO_ID";
  public static final String CAT_TWO_TEXT = "CAT_TWO_TEXT";
  private static final String QN_ONE_ID = "QN_ONE_ID";
  public static final String QN_ONE_TEXT = "QN_ONE_TEXT";
  private static final String QN_ONE_LABEL = "QN_ONE_LABEL";
  private static final String QN_ONE_VALUE = "QN_ONE_VALUE";
  private static final String QN_TWO_ID = "QN_TWO_ID";
  public static final String QN_TWO_TEXT = "QN_TWO_TEXT";
  private static final String QN_TWO_LABEL = "QN_TWO_LABEL";
  private static final String QN_THREE_ID = "QN_THREE_ID";
  public static final String QN_THREE_TEXT = "QN_THREE_TEXT";
  private static final String QN_THREE_LABEL = "QN_THREE_LABEL";
  public static final String QN_TEXT_VALUE = "Ej angivet";
  public static final String QN_ERROR_VALUE = "Kan inte visa värdet pga tekniskt fel";
  private ValueConverter textValueConverter = mock(ValueConverter.class);
  private CertificateDataConverter certificateDataConverter;

  @BeforeEach
  void setUp() {
    doReturn(CertificateDataValueType.TEXT).when(textValueConverter).getType();
    certificateDataConverter = new CertificateDataConverter(List.of(textValueConverter));
  }

  @Test
  void shallConvertCategory() {
    final var expectedCategories = List.of(
        createCertificateCategory(CAT_ONE_TEXT)
    );

    final var elements = List.of(
        createCategoryElement(CAT_ONE_TEXT, CAT_ONE_ID, 0)
    );

    final var actualCategories = certificateDataConverter.convert(elements);

    assertEquals(expectedCategories, actualCategories);
  }

  @Test
  void shallConvertCategoriesInCorrectOrder() {
    final var expectedCategories = List.of(
        createCertificateCategory(CAT_ONE_TEXT),
        createCertificateCategory(CAT_TWO_TEXT)
    );

    final var elements = List.of(
        createCategoryElement(CAT_TWO_TEXT, CAT_TWO_ID, 1),
        createCategoryElement(CAT_ONE_TEXT, CAT_ONE_ID, 0)
    );

    final var actualCategories = certificateDataConverter.convert(elements);

    assertEquals(expectedCategories, actualCategories);
  }

  @Test
  void shallConvertCategoryWithQuestion() {
    final var expectedCategories = List.of(
        createCertificateCategory(CAT_ONE_TEXT,
            createCertificateQuestion(QN_ONE_TEXT, QN_ONE_LABEL)
        )
    );

    final var elements = List.of(
        createCategoryElement(CAT_ONE_TEXT, CAT_ONE_ID, 0),
        createQuestionElement(QN_ONE_TEXT, QN_ONE_LABEL, QN_ONE_ID, 1, CAT_ONE_ID)
    );

    final var actualCategories = certificateDataConverter.convert(elements);

    assertEquals(expectedCategories, actualCategories);
  }

  @Test
  void shallConvertCategoryWithQuestionsInCorrectOrder() {
    final var expectedCategories = List.of(
        createCertificateCategory(CAT_ONE_TEXT,
            createCertificateQuestion(QN_ONE_TEXT, QN_ONE_LABEL),
            createCertificateQuestion(QN_TWO_TEXT, QN_TWO_LABEL)
        )
    );

    final var elements = List.of(
        createCategoryElement(CAT_ONE_TEXT, CAT_ONE_ID, 0),
        createQuestionElement(QN_TWO_TEXT, QN_TWO_LABEL, QN_TWO_ID, 2, CAT_ONE_ID),
        createQuestionElement(QN_ONE_TEXT, QN_ONE_LABEL, QN_ONE_ID, 1, CAT_ONE_ID)
    );

    final var actualCategories = certificateDataConverter.convert(elements);

    assertEquals(expectedCategories, actualCategories);
  }

  @Test
  void shallConvertCategoryWithQuestionWithSubQuestion() {
    final var expectedCategories = List.of(
        createCertificateCategory(CAT_ONE_TEXT,
            createCertificateQuestion(QN_ONE_TEXT, QN_ONE_LABEL,
                createCertificateQuestion(QN_TWO_TEXT, QN_TWO_LABEL))
        )
    );

    final var elements = List.of(
        createCategoryElement(CAT_ONE_TEXT, CAT_ONE_ID, 0),
        createQuestionElement(QN_ONE_TEXT, QN_ONE_LABEL, QN_ONE_ID, 1, CAT_ONE_ID),
        createQuestionElement(QN_TWO_TEXT, QN_TWO_LABEL, QN_TWO_ID, 2, QN_ONE_ID)
    );

    final var actualCategories = certificateDataConverter.convert(elements);

    assertEquals(expectedCategories, actualCategories);
  }

  @Test
  void shallConvertCategoryWithQuestionWithSubQuestionInCorrectOrder() {
    final var expectedCategories = List.of(
        createCertificateCategory(CAT_ONE_TEXT,
            createCertificateQuestion(QN_ONE_TEXT, QN_ONE_LABEL,
                createCertificateQuestion(QN_TWO_TEXT, QN_TWO_LABEL),
                createCertificateQuestion(QN_THREE_TEXT, QN_THREE_LABEL)
            )
        )
    );

    final var elements = List.of(
        createCategoryElement(CAT_ONE_TEXT, CAT_ONE_ID, 0),
        createQuestionElement(QN_ONE_TEXT, QN_ONE_LABEL, QN_ONE_ID, 1, CAT_ONE_ID),
        createQuestionElement(QN_THREE_TEXT, QN_THREE_LABEL, QN_THREE_ID, 3, QN_ONE_ID),
        createQuestionElement(QN_TWO_TEXT, QN_TWO_LABEL, QN_TWO_ID, 2, QN_ONE_ID)
    );

    final var actualCategories = certificateDataConverter.convert(elements);

    assertEquals(expectedCategories, actualCategories);
  }

  @Test
  void shallReturnTitleNullIfTextIsNull() {
    final var expectedCategories = List.of(
        createCertificateCategory(null)
    );

    final var elements = List.of(
        createCategoryElement(null, CAT_ONE_ID, 0)
    );

    final var actualCategories = certificateDataConverter.convert(elements);

    assertEquals(expectedCategories, actualCategories);
  }

  @Test
  void shallReturnTitleNullIfTextIsEmpty() {
    final var expectedCategories = List.of(
        createCertificateCategory(null)
    );

    final var elements = List.of(
        createCategoryElement("", CAT_ONE_ID, 0)
    );

    final var actualCategories = certificateDataConverter.convert(elements);

    assertEquals(expectedCategories, actualCategories);
  }

  @Test
  void shallReturnLabelNullIfLabelIsNull() {
    final var expectedCategories = List.of(
        createCertificateCategory(CAT_ONE_TEXT,
            createCertificateQuestion(QN_ONE_TEXT, null)
        )
    );

    final var elements = List.of(
        createCategoryElement(CAT_ONE_TEXT, CAT_ONE_ID, 0),
        createQuestionElement(QN_ONE_TEXT, null, QN_ONE_ID, 1, CAT_ONE_ID)
    );

    final var actualCategories = certificateDataConverter.convert(elements);

    assertEquals(expectedCategories, actualCategories);
  }

  @Test
  void shallReturnLabelNullIfLabelIsEmpty() {
    final var expectedCategories = List.of(
        createCertificateCategory(CAT_ONE_TEXT,
            createCertificateQuestion(QN_ONE_TEXT, null)
        )
    );

    final var elements = List.of(
        createCategoryElement(CAT_ONE_TEXT, CAT_ONE_ID, 0),
        createQuestionElement(QN_ONE_TEXT, "", QN_ONE_ID, 1, CAT_ONE_ID)
    );

    final var actualCategories = certificateDataConverter.convert(elements);

    assertEquals(expectedCategories, actualCategories);
  }

  @Test
  void shallConvertValueWhenValueConverterExists() {
    final var expectedCategories = List.of(
        createCertificateCategory(CAT_ONE_TEXT,
            CertificateQuestion.builder()
                .title(QN_ONE_TEXT)
                .value(
                    CertificateQuestionValueText.builder()
                        .value(QN_ONE_VALUE)
                        .build()
                )
                .build()
        )
    );

    final var elements = List.of(
        createCategoryElement(CAT_ONE_TEXT, CAT_ONE_ID, 0),
        CertificateDataElement.builder()
            .id(QN_ONE_ID)
            .index(1)
            .parent(CAT_ONE_ID)
            .config(
                CertificateDataConfigTextArea.builder()
                    .text(QN_ONE_TEXT)
                    .build()
            )
            .value(
                CertificateDataTextValue.builder()
                    .text(QN_ONE_VALUE)
                    .build()
            )
            .build()
    );

    doReturn(expectedCategories.get(0).getQuestions().get(0).getValue())
        .when(textValueConverter).convert(elements.get(1));

    final var actualCategories = certificateDataConverter.convert(elements);

    assertEquals(expectedCategories, actualCategories);
  }

  @Test
  void shallReturnErrorValueWhenValueConverterDoesntExists() {
    final var expectedCategories = List.of(
        createCertificateCategory(CAT_ONE_TEXT,
            CertificateQuestion.builder()
                .title(QN_ONE_TEXT)
                .value(
                    CertificateQuestionValueText.builder()
                        .value(QN_ERROR_VALUE)
                        .build()
                )
                .build()
        )
    );

    final var elements = List.of(
        createCategoryElement(CAT_ONE_TEXT, CAT_ONE_ID, 0),
        CertificateDataElement.builder()
            .id(QN_ONE_ID)
            .index(1)
            .parent(CAT_ONE_ID)
            .config(
                CertificateDataConfigRadioBoolean.builder()
                    .text(QN_ONE_TEXT)
                    .build()
            )
            .value(
                CertificateDataValueBoolean.builder().build()
            )
            .build()
    );

    final var actualCategories = certificateDataConverter.convert(elements);

    assertEquals(expectedCategories, actualCategories);
  }

  private static CertificateCategory createCertificateCategory(String title,
      CertificateQuestion... question) {
    return CertificateCategory.builder()
        .title(title)
        .questions(List.of(question))
        .build();
  }

  private static CertificateQuestion createCertificateQuestion(String title,
      String label, CertificateQuestion... subQuestion) {
    return CertificateQuestion.builder()
        .title(title)
        .label(label)
        .value(
            CertificateQuestionValueText.builder()
                .value(QN_TEXT_VALUE)
                .build()
        )
        .subQuestions(List.of(subQuestion))
        .build();
  }

  private static CertificateDataElement createCategoryElement(
      String title, String id, int index) {
    return CertificateDataElement.builder()
        .id(id)
        .index(index)
        .config(
            CertificateDataConfigCategory.builder()
                .text(title)
                .build()
        )
        .build();
  }

  private static CertificateDataElement createQuestionElement(String text, String label, String id,
      int index, String parent) {
    return CertificateDataElement.builder()
        .id(id)
        .index(index)
        .parent(parent)
        .config(
            CertificateDataConfigTextArea.builder()
                .text(text)
                .label(label)
                .build()
        )
        .build();
  }
}
