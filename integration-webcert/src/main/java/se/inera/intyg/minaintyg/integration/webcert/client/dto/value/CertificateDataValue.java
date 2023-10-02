package se.inera.intyg.minaintyg.integration.webcert.client.dto.value;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
    @Type(value = CertificateDataTextValue.class, name = "TEXT"),
    @Type(value = CertificateDataValueBoolean.class, name = "BOOLEAN"),
    @Type(value = CertificateDataValueDateList.class, name = "DATE_LIST"),
    @Type(value = CertificateDataValueDate.class, name = "DATE"),
    @Type(value = CertificateDataUncertainDateValue.class, name = "UNCERTAIN_DATE"),
    @Type(value = CertificateDataValueDateRangeList.class, name = "DATE_RANGE_LIST"),
    @Type(value = CertificateDataValueDateRange.class, name = "DATE_RANGE"),
    @Type(value = CertificateDataValueDiagnosisList.class, name = "DIAGNOSIS_LIST"),
    @Type(value = CertificateDataValueDiagnosis.class, name = "DIAGNOSIS"),
    @Type(value = CertificateDataValueCodeList.class, name = "CODE_LIST"),
    @Type(value = CertificateDataValueCode.class, name = "CODE"),
    @Type(value = CertificateDataIcfValue.class, name = "ICF"),
    @Type(value = CertificateDataValueCauseOfDeath.class, name = "CAUSE_OF_DEATH"),
    @Type(value = CertificateDataValueCauseOfDeathList.class, name = "CAUSE_OF_DEATH_LIST"),
    @Type(value = CertificateDataValueMedicalInvestigation.class, name = "MEDICAL_INVESTIGATION"),
    @Type(value = CertificateDataValueMedicalInvestigationList.class, name = "MEDICAL_INVESTIGATION_LIST"),
    @Type(value = CertificateDataValueVisualAcuities.class, name = "VISUAL_ACUITIES"),
    @Type(value = CertificateDataValueVisualAcuity.class, name = "VISUAL_ACUITY"),
    @Type(value = CertificateDataValueDouble.class, name = "DOUBLE"),
    @Type(value = CertificateDataValueViewText.class, name = "VIEW_TEXT"),
    @Type(value = CertificateDataValueViewList.class, name = "VIEW_LIST"),
    @Type(value = CertificateDataValueViewTable.class, name = "VIEW_TABLE"),
    @Type(value = CertificateDataValueViewRow.class, name = "VIEW_ROW"),
    @Type(value = CertificateDataValueYear.class, name = "YEAR"),
    @Type(value = CertificateDataValueInteger.class, name = "INTEGER")
})
public interface CertificateDataValue {

  CertificateDataValueType getType();
}
