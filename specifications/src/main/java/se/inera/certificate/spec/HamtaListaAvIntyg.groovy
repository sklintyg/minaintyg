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
		setEndpoint(listCertificatesResponder, "list-certificates")
		setEndpoint(getConsentResponder, "get-consent-v1")
	}
	
	String personnr
	private LocalDate från
	private LocalDate till
	String kommentar
	
	public void setFrån(String datum) {
		this.från = LocalDate.parse(datum)
	}
	
	public void setTill(String datum) {
		this.till = LocalDate.parse(datum)
	}
	
	public String svar() {
		GetConsentRequestType getConsentParameters = new GetConsentRequestType()
		getConsentParameters.personnummer = personnr
		GetConsentResponseType getConsentResponse = getConsentResponder.getConsent(logicalAddress, getConsentParameters)
		if (!getConsentResponse.consentGiven) {
			return "samtycke saknas"
		}
		ListCertificatesRequestType parameters = new ListCertificatesRequestType();
		parameters.nationalIdentityNumber = personnr
		parameters.fromDate = från
		parameters.toDate = till
        parameters.certificateType = ["FK7263"]
		ListCertificatesResponseType response = listCertificatesResponder.listCertificates(logicalAddress, parameters)
		ResultOfCall result = response.result
		if (result.resultCode == ResultCodeEnum.OK) {
			def fk_intyg = []
			List allaIntyg = response.meta
			allaIntyg.each {CertificateMetaType metaType ->
				fk_intyg << metaType.certificateId
			}
			return fk_intyg.toString()	
		} else {
			return result.errorText
		}
	}

}
