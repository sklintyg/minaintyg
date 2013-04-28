package se.inera.certificate.spec;

import se.inera.certificate.spec.util.WsClientFixture
import se.inera.ifv.insuranceprocess.healthreporting.setconsent.v1.rivtabp20.SetConsentResponderInterface
import se.inera.ifv.insuranceprocess.healthreporting.setconsent.v1.rivtabp20.SetConsentResponderService
import se.inera.ifv.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentRequestType
import se.inera.ifv.insuranceprocess.healthreporting.setconsentresponder.v1.SetConsentResponseType

public class Samtycke extends WsClientFixture {

	private SetConsentResponderService setConsentService = new SetConsentResponderService();
	private SetConsentResponderInterface setConsentResponder = setConsentService.setConsentResponderPort

	public Samtycke() {
		setEndpoint(setConsentResponder, "set-consent-v1")
	}

	String personnr
	private boolean samtycke
	
	public void setSamtycke(String value) {
		if (value != null && value.equalsIgnoreCase("ja")) {
			samtycke = true
		} else {
			samtycke = false
		}
	}

	public void execute() {
		SetConsentRequestType setConsentParameters = new SetConsentRequestType()
		setConsentParameters.personnummer = personnr
		setConsentParameters.consentGiven = samtycke
		SetConsentResponseType setConsentResponse = setConsentResponder.setConsent(logicalAddress, setConsentParameters)
	}

}
