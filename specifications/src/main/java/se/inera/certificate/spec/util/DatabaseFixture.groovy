package se.inera.certificate.spec.util

import groovy.sql.Sql

class DatabaseFixture {

	def db = [url:'jdbc:h2:tcp://localhost:9092/mem:dataSource', user:'sa', password:'', driver:'org.h2.Driver']
	def sql = Sql.newInstance(db.url, db.user, db.password, db.driver)

}
