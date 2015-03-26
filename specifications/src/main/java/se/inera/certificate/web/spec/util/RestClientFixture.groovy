package se.inera.certificate.web.spec.util

import groovyx.net.http.RESTClient

/**
 *
 * @author andreaskaltenbach
 */
class RestClientFixture extends se.inera.certificate.spec.util.RestClientFixture {

    String baseUrl = System.getProperty("geb.build.baseUrl")

}
