package se.inera.certificate.spec;

import se.inera.certificate.spec.util.DatabaseFixture;
import groovy.sql.Sql


public class TaBortIntyg extends DatabaseFixture {

	private def delete_cert = "DELETE FROM CERTIFICATE WHERE ID = ?"
	private def delete_cert_meta = "DELETE FROM CERTIFICATE_META_DATA WHERE ID = ?"
	
	String id
	
	public void execute() {
		sql.execute delete_cert_meta, [id]
		sql.execute delete_cert, [id]
	}

}
