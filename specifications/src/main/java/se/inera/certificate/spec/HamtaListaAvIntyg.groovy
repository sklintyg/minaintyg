package se.inera.certificate.spec
import org.joda.time.LocalDate

import se.inera.certificate.spec.util.WsClientFixture
import se.inera.ifv.insuranceprocess.certificate.v1.CertificateMetaType
import se.inera.ifv.insuranceprocess.healthreporting.getconsent.v1.rivtabp20.GetConsentResponderInterface
import se.inera.ifv.insuranceprocess.healthreporting.getconsent.v1.rivtabp20.GetConsentResponderService
import se.inera.ifv.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentRequestType
import se.inera.ifv.insuranceprocess.healthreporting.getconsentresponder.v1.GetConsentResponseType
import se.inera.ifv.insuranceprocess.healthreporting.listcertificates.v1.rivtabp20.ListCertificatesResponderInterface
import se.inera.ifv.insuranceprocess.healthreporting.listcertificates.v1.rivtabp20.ListCertificatesResponderService
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesRequestType
import se.inera.ifv.insuranceprocess.healthreporting.listcertificatesresponder.v1.ListCertificatesResponseType
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultCodeEnum
import se.inera.ifv.insuranceprocess.healthreporting.v2.ResultOfCall

public class HamtaListaAvIntyg extends WsClientFixture {

	private ListCertificatesResponderService listCertificatesService = new ListCertificatesResponderService();
	private ListCertificatesResponderInterface listCertificatesResponder = listCertificatesService.listCertificatesResponderPort
	private GetConsentResponderService getConsentService = new GetConsentResponderService();
	private GetConsentResponderInterface getConsentResponder = getConsentService.getConsentResponderPort

	public HamtaListaAvIntyg() {
		setEndpoint(listCertificatesResponder, "list-certificates/v1.0")
		setEndpoint(getConsentResponder, "get-consent/v1.0")
	}
	
	String personnr
	private String[] typ
	private LocalDate från
	private LocalDate till
	String kommentar
	
	public void setTyp(String[] array) {
		if (isEmpty(array)) {
			typ = []
		} else {
			typ = array
		}
	}
	public void setFrån(String datum) {
		this.från = LocalDate.parse(datum)
	}
	
	public void setTill(String datum) {
		this.till = LocalDate.parse(datum)
	}

	private String svar
	private String intyg
	
    public void execute() {
		svar = null
		intyg = null
		GetConsentRequestType getConsentParameters = new GetConsentRequestType()
		getConsentParameters.personnummer = personnr
		GetConsentResponseType getConsentResponse = getConsentResponder.getConsent(logicalAddress, getConsentParameters)
		if (!getConsentResponse.consentGiven) {
			svar = "samtycke saknas"
			return 
		}
		ListCertificatesRequestType parameters = new ListCertificatesRequestType();
		parameters.nationalIdentityNumber = personnr
		parameters.fromDate = från
		parameters.toDate = till
		if (typ) {
			parameters.certificateType = typ
		}
		ListCertificatesResponseType response = listCertificatesResponder.listCertificates(logicalAddress, parameters)
		ResultOfCall result = response.result
		if (result.resultCode == ResultCodeEnum.OK) {
			def fk_intygs_id = []
			def fk_intyg = []
			List allaIntyg = response.meta
			allaIntyg.each {CertificateMetaType metaType ->
				fk_intygs_id << metaType.certificateId
				fk_intyg << metaType.certificateId + ":" + metaType.certificateType + ":" + metaType.signDate + ":" + metaType.validFrom + ":" + metaType.validTo + ":" + metaType.issuerName + ":" + metaType.facilityName
 			}
			svar = fk_intygs_id.sort().toString()	
			intyg = fk_intyg.sort().toString()	
		} else {
			svar = result.errorText
		}
	}

	public String svar() {
		svar
	}
	
	public String intyg() {
		intyg
	}
	
	private boolean isEmpty(String[] array) {
		switch (array.length) {
			case 0: return true;
			case 1: return array[0].isEmpty();
			default: return false;
		}
	}

}
