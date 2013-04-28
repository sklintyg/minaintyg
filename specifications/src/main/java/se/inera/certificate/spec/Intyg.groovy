package se.inera.certificate.spec;

import groovy.sql.Sql

import org.joda.time.LocalDate

import se.inera.certificate.spec.util.DatabaseFixture

public class Intyg extends DatabaseFixture {

	private def insert_cert = "INSERT INTO CERTIFICATE (ID) values (?)"
	private def insert_meta_data = "INSERT INTO CERTIFICATE_META_DATA (ID, TYPE, SIGNING_DOCTOR_NAME, CIVIC_REGISTRATION_NUMBER, SIGNED_DATE, VALID_FROM_DATE, VALID_TO_DATE, DELETED) values (?,?,'Doctor Ruth',?,?,?,?,false)"
	
	String personnr
	LocalDate datum
	String typ
	String id
	
	public void setDatum(String datum) {
		this.datum = LocalDate.parse(datum)
	}
	public void execute() {
		sql.execute insert_cert, [id]
		sql.execute insert_meta_data, [id, typ, personnr, datum.toString("yyyy-MM-dd 00:00:00"), datum.toString("yyyy-MM-dd 00:00:00"), datum.toString("yyyy-MM-dd 00:00:00")]
	}

}
