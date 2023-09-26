package se.inera.intyg.minaintyg.integration.webcert.client.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.Accordion;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCategory;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCauseOfDeath;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCauseOfDeathList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigCheckboxMultipleDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDateRange;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDiagnoses;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigHeader;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigIcf;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigInteger;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigMedicalInvestigation;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigMessage;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioBoolean;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioMultipleCode;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigRadioMultipleCodeOptionalDropdown;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigSickLeavePeriod;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextArea;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTextField;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTypeAhead;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigTypes;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigUncertainDate;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigViewList;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigViewTable;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigViewText;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigVisualAcuity;
import se.inera.intyg.minaintyg.integration.webcert.client.dto.config.CertificateDataConfigYear;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @Type(value = CertificateDataConfigCategory.class, name = "CATEGORY"),
    @Type(value = CertificateDataConfigTextArea.class, name = "UE_TEXTAREA"),
    @Type(value = CertificateDataConfigTextField.class, name = "UE_TEXTFIELD"),
    @Type(value = CertificateDataConfigTypeAhead.class, name = "UE_TYPE_AHEAD"),
    @Type(value = CertificateDataConfigDate.class, name = "UE_DATE"),
    @Type(value = CertificateDataConfigMessage.class, name = "UE_MESSAGE"),
    @Type(value = CertificateDataConfigHeader.class, name = "UE_HEADER"),
    @Type(value = CertificateDataConfigUncertainDate.class, name = "UE_UNCERTAIN_DATE"),
    @Type(value = CertificateDataConfigCheckboxBoolean.class, name = "UE_CHECKBOX_BOOLEAN"),
    @Type(value = CertificateDataConfigRadioBoolean.class, name = "UE_RADIO_BOOLEAN"),
    @Type(value = CertificateDataConfigRadioMultipleCode.class, name = "UE_RADIO_MULTIPLE_CODE"),
    @Type(value = CertificateDataConfigRadioMultipleCodeOptionalDropdown.class, name = "UE_RADIO_MULTIPLE_CODE_OPTIONAL_DROPDOWN"),
    @Type(value = CertificateDataConfigCheckboxMultipleDate.class, name = "UE_CHECKBOX_MULTIPLE_DATE"),
    @Type(value = CertificateDataConfigCheckboxMultipleCode.class, name = "UE_CHECKBOX_MULTIPLE_CODE"),
    @Type(value = CertificateDataConfigSickLeavePeriod.class, name = "UE_SICK_LEAVE_PERIOD"),
    @Type(value = CertificateDataConfigDiagnoses.class, name = "UE_DIAGNOSES"),
    @Type(value = CertificateDataConfigDropdown.class, name = "UE_DROPDOWN"),
    @Type(value = CertificateDataConfigIcf.class, name = "UE_ICF"),
    @Type(value = CertificateDataConfigCauseOfDeath.class, name = "UE_CAUSE_OF_DEATH"),
    @Type(value = CertificateDataConfigCauseOfDeathList.class, name = "UE_CAUSE_OF_DEATH_LIST"),
    @Type(value = CertificateDataConfigMedicalInvestigation.class, name = "UE_MEDICAL_INVESTIGATION"),
    @Type(value = CertificateDataConfigVisualAcuity.class, name = "UE_VISUAL_ACUITY"),
    @Type(value = CertificateDataConfigViewText.class, name = "UE_VIEW_TEXT"),
    @Type(value = CertificateDataConfigViewList.class, name = "UE_VIEW_LIST"),
    @Type(value = CertificateDataConfigViewTable.class, name = "UE_VIEW_TABLE"),
    @Type(value = CertificateDataConfigYear.class, name = "UE_YEAR"),
    @Type(value = CertificateDataConfigInteger.class, name = "UE_INTEGER"),
    @Type(value = CertificateDataConfigDateRange.class, name = "UE_DATE_RANGE")

})
public interface CertificateDataConfig {

  CertificateDataConfigTypes getType();

  String getHeader();

  String getLabel();

  String getIcon();

  String getText();

  String getDescription();

  Accordion getAccordion();

}
