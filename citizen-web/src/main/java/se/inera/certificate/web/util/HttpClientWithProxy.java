package se.inera.certificate.web.util;

import org.apache.commons.httpclient.HttpClient;

public class HttpClientWithProxy {

    public static HttpClient create() {
        HttpClient httpClient = new HttpClient();
        
        String proxyHost = System.getProperty("https.proxyHost");
        if (proxyHost != null) {
            String proxyPort = System.getProperty("https.proxyPort", "-1");
            httpClient.getHostConfiguration().setProxy(proxyHost, Integer.parseInt(proxyPort));
        }
        return httpClient;
    }
}
