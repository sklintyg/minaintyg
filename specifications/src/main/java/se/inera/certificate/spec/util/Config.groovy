package se.inera.certificate.spec.util

class Config {

	String property
	String value
	
	void execute() {
		System.setProperty(property, value)
	}
}
