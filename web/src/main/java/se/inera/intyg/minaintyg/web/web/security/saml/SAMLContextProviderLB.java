/*
 * Copyright (C) 2017 Inera AB (http://www.inera.se)
 *
 * This file is part of sklintyg (https://github.com/sklintyg).
 *
 * sklintyg is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * sklintyg is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.intyg.minaintyg.web.web.security.saml;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.springframework.security.saml.context.SAMLContextProviderImpl;
import org.springframework.security.saml.context.SAMLMessageContext;
import org.springframework.util.Assert;

/**
 * Context provider which overrides request attributes with values of the load-balancer or reverse-proxy in front
 * of the local application. The settings help to provide correct redirect URls and verify destination URLs during
 * SAML processing.
 */
public class SAMLContextProviderLB extends SAMLContextProviderImpl {

    private String scheme;
    private String serverName;
    private boolean includeServerPortInRequestURL;
    private int serverPort;
    private String contextPath;

    /**
     * Method wraps the original request and provides values specified for load-balancer. The following methods
     * are overriden: getContextPath, getRequestURL, getRequestURI, getScheme, getServerName, getServerPort and isSecure.
     *
     * @param request  original request
     * @param response response object
     * @param context  context to populate values to
     * @throws MetadataProviderException
     */
    @Override
    protected void populateGenericContext(HttpServletRequest request, HttpServletResponse response, SAMLMessageContext context) throws MetadataProviderException {

        super.populateGenericContext(new LPRequestWrapper(request), response, context);

    }

    /**
     * Wrapper for original request which overrides values of scheme, server name, server port and contextPath.
     * Method isSecure returns value based on specified scheme.
     */
    private final class LPRequestWrapper extends HttpServletRequestWrapper {

        private LPRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getContextPath() {
            return contextPath;
        }

        @Override
        public String getScheme() {
            return scheme;
        }

        @Override
        public String getServerName() {
            return serverName;
        }

        @Override
        public int getServerPort() {
            return serverPort;
        }

        @Override
        public String getRequestURI() {
            StringBuilder sb = new StringBuilder(contextPath);
            sb.append(getServletPath());
            return sb.toString();
        }

        @Override
        public StringBuffer getRequestURL() {
            StringBuffer sb = new StringBuffer();
            sb.append(scheme).append("://").append(serverName);
            if (includeServerPortInRequestURL) {
                sb.append(":").append(serverPort);
            }
            sb.append(contextPath);
            sb.append(getServletPath());
            String pathInfo = getPathInfo();
            if (pathInfo != null) {
                sb.append(pathInfo);
            }
            return sb;
        }

        @Override
        public boolean isSecure() {
            return "https".equalsIgnoreCase(scheme);
        }

    }

    /**
     * Scheme of the LB server - either http or https.
     *
     * @param scheme scheme
     */
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    /**
     * Server name of the LB, e.g. www.myserver.com.
     *
     * @param serverName server name
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Port of the server, in case value is <= 0 port will not be included in the requestURL and port
     * from the original request will be used for getServerPort calls.
     *
     * @param serverPort server port
     */
    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    /**
     * When true serverPort will be used in construction of LB requestURL.
     *
     * @param includeServerPortInRequestURL true to include port
     */
    public void setIncludeServerPortInRequestURL(boolean includeServerPortInRequestURL) {
        this.includeServerPortInRequestURL = includeServerPortInRequestURL;
    }

    /**
     * Context path of the LB, must be starting with slash, e.g. /saml-extension
     *
     * @param contextPath context path
     */
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    /**
     * Verifies that required entities were autowired or set and initializes resolvers used to construct trust engines.
     *
     * @throws javax.servlet.ServletException
     */
    @Override
    public void afterPropertiesSet() throws ServletException {

        super.afterPropertiesSet();

        Assert.hasText(scheme, "Scheme must be set");
        Assert.hasText(serverName, "Server name must be set");
        Assert.notNull(contextPath, "Context path must be set");
        if (!"".equals(contextPath)) {
            Assert.isTrue(contextPath.startsWith("/"), "Context path must must start with a forward slash");
        }
    }

}
