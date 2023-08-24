package se.inera.intyg.minaintyg.auth.config;

import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.DEV_PROFILE;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.HttpClient;
import org.apache.velocity.app.VelocityEngine;
import org.opensaml.saml2.metadata.provider.FilesystemMetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProvider;
import org.opensaml.saml2.metadata.provider.MetadataProviderException;
import org.opensaml.xml.parse.StaticBasicParserPool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.security.saml.SAMLAuthenticationProvider;
import org.springframework.security.saml.SAMLBootstrap;
import org.springframework.security.saml.SAMLEntryPoint;
import org.springframework.security.saml.SAMLLogoutFilter;
import org.springframework.security.saml.SAMLLogoutProcessingFilter;
import org.springframework.security.saml.context.SAMLContextProviderLB;
import org.springframework.security.saml.key.JKSKeyManager;
import org.springframework.security.saml.key.KeyManager;
import org.springframework.security.saml.log.SAMLDefaultLogger;
import org.springframework.security.saml.metadata.CachingMetadataManager;
import org.springframework.security.saml.metadata.ExtendedMetadata;
import org.springframework.security.saml.metadata.ExtendedMetadataDelegate;
import org.springframework.security.saml.processor.HTTPArtifactBinding;
import org.springframework.security.saml.processor.HTTPPostBinding;
import org.springframework.security.saml.processor.HTTPRedirectDeflateBinding;
import org.springframework.security.saml.processor.HTTPSOAP11Binding;
import org.springframework.security.saml.processor.SAMLBinding;
import org.springframework.security.saml.processor.SAMLProcessorImpl;
import org.springframework.security.saml.storage.EmptyStorageFactory;
import org.springframework.security.saml.util.VelocityFactory;
import org.springframework.security.saml.websso.ArtifactResolutionProfileImpl;
import org.springframework.security.saml.websso.SingleLogoutProfile;
import org.springframework.security.saml.websso.SingleLogoutProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfile;
import org.springframework.security.saml.websso.WebSSOProfileConsumer;
import org.springframework.security.saml.websso.WebSSOProfileConsumerHoKImpl;
import org.springframework.security.saml.websso.WebSSOProfileConsumerImpl;
import org.springframework.security.saml.websso.WebSSOProfileECPImpl;
import org.springframework.security.saml.websso.WebSSOProfileImpl;
import org.springframework.security.saml.websso.WebSSOProfileOptions;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import se.inera.intyg.minaintyg.auth.MinaIntygAuthenticationProvider;
import se.inera.intyg.minaintyg.auth.MinaIntygSAMLEntryPoint;
import se.inera.intyg.minaintyg.auth.MinaIntygUserDetailsService;

@Profile("!" + DEV_PROFILE)
@Configuration
public class SAMLSecurityConfig {

    @Value("${mina.intyg.server.name}")
    private String serverName;
    @Value("${saml.keystore.file}")
    private String samlKeystoreLocation;
    @Value("${saml.response.skew}")
    private int samlResponseSkew;

    @Value("${saml.keystore.password}")
    private String samlKeystorePassword;

    @Value("${saml.keystore.alias}")
    private String samlKeystoreAlias;

    @Value("${application.dir}")
    private String applicationDir;

    @Value("${mina.intyg.default.target.url}")
    private String targetUrl;

    @Value("${mina.intyg.base.url}")
    private String baseUrl;
    @Value("${saml.idp.entity.id}")
    private String samlIdpEntityId;

    @Value("${saml.sp.entity.id}")
    private String samlSpEntityId;
    public static final String HTTPS = "https";
    public static final String ROOT_URL = "/";

    public static final int REFRESH_CHECK_INTERVAL = 86400000;
    @Bean
    public MinaIntygUserDetailsService samlUserDetailsService() {
        return new MinaIntygUserDetailsService();
    }
    @Bean
    public SAMLAuthenticationProvider samlAuthenticationProvider() {
        final var minaIntygAuthenticationProvider = new MinaIntygAuthenticationProvider();
        minaIntygAuthenticationProvider.setUserDetails(samlUserDetailsService());
        minaIntygAuthenticationProvider.setForcePrincipalAsString(false);
        return minaIntygAuthenticationProvider;
    }

    @Bean
    public WebSSOProfileOptions defaultWebSSOProfileOptions() {
        WebSSOProfileOptions webSSOProfileOptions = new WebSSOProfileOptions();
        webSSOProfileOptions.setIncludeScoping(false);
        List<String> authnContexts = new ArrayList<>();
        authnContexts.add("urn:oasis:names:tc:SAML:2.0:ac:classes:TLSClient");
        authnContexts.add("urn:oasis:names:tc:SAML:2.0:ac:classes:SoftwarePKI");
        webSSOProfileOptions.setAuthnContexts(authnContexts);
        return webSSOProfileOptions;
    }

    @Bean
    public SAMLEntryPoint samlEntryPoint() {
        SAMLEntryPoint samlEntryPoint = new MinaIntygSAMLEntryPoint();
        samlEntryPoint.setDefaultProfileOptions(defaultWebSSOProfileOptions());
        return samlEntryPoint;
    }

    @Bean
    public SAMLContextProviderLB contextProvider() {
        SAMLContextProviderLB samlContextProviderLB = new SAMLContextProviderLB();
        samlContextProviderLB.setStorageFactory(new EmptyStorageFactory());
        samlContextProviderLB.setScheme(HTTPS);
        samlContextProviderLB.setServerName(serverName);
        samlContextProviderLB.setContextPath(ROOT_URL);
        return samlContextProviderLB;
    }

    @Bean
    public static SAMLBootstrap samlBootstrap() {
        return new SAMLBootstrap();
    }

    @Bean
    public SAMLDefaultLogger samlLogger() {
        return new SAMLDefaultLogger();
    }

    @Bean
    public WebSSOProfileConsumer webSSOprofileConsumer() {
        WebSSOProfileConsumerImpl webSSOProfileConsumer = new WebSSOProfileConsumerImpl();
        webSSOProfileConsumer.setResponseSkew(samlResponseSkew);
        return webSSOProfileConsumer;
    }

    @Bean
    @Qualifier("hokWebSSOprofileConsumer")
    public WebSSOProfileConsumerHoKImpl hokWebSSOProfileConsumer() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    @Bean
    public WebSSOProfile webSSOprofile() {
        return new WebSSOProfileImpl();
    }

    @Bean
    public WebSSOProfileConsumerHoKImpl hokWebSSOProfile() {
        return new WebSSOProfileConsumerHoKImpl();
    }

    @Bean
    public WebSSOProfileECPImpl ecpProfile() {
        return new WebSSOProfileECPImpl();
    }

    @Bean
    public SingleLogoutProfile logoutProfile() {
        return new SingleLogoutProfileImpl();
    }

    @Bean
    public KeyManager keyManager() {
        DefaultResourceLoader loader = new DefaultResourceLoader();
        Resource storeFile = loader.getResource(samlKeystoreLocation);
        Map<String, String> passwords = new HashMap<>();
        passwords.put(samlKeystoreAlias, samlKeystorePassword);
        return new JKSKeyManager(storeFile, samlKeystorePassword, passwords, samlKeystoreAlias);
    }

    @Bean
    public ExtendedMetadata spExtendedMetadata() {
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setAlias("eleg");
        extendedMetadata.setLocal(true);
        extendedMetadata.setSecurityProfile("metaiop");
        extendedMetadata.setSslSecurityProfile("metaiop");
        extendedMetadata.setSignMetadata(true);
        extendedMetadata.setSigningKey(samlKeystoreAlias);
        extendedMetadata.setEncryptionKey(samlKeystoreAlias);
        extendedMetadata.setRequireArtifactResolveSigned(true);
        extendedMetadata.setRequireLogoutRequestSigned(true);
        extendedMetadata.setRequireLogoutResponseSigned(true);
        return extendedMetadata;
    }

    @Bean(initMethod = "initialize")
    public StaticBasicParserPool parserPool() {
        return new StaticBasicParserPool();
    }
    @Bean
    public FilesystemMetadataProvider spFilesystemMetadataProvider() throws MetadataProviderException {
        File spMetadataFile = new File(applicationDir + "/config/sp-eleg.xml");
        FilesystemMetadataProvider filesystemMetadataProvider = new FilesystemMetadataProvider(spMetadataFile);
        filesystemMetadataProvider.setParserPool(parserPool());
        return filesystemMetadataProvider;
    }

    @Bean
    public ExtendedMetadataDelegate spExtendedMetadataDelegate() throws MetadataProviderException {
        ExtendedMetadataDelegate extendedMetadataDelegate = new ExtendedMetadataDelegate(spFilesystemMetadataProvider(),
            spExtendedMetadata());
        extendedMetadataDelegate.setMetadataTrustCheck(true);
        return extendedMetadataDelegate;
    }

    @Bean
    public ExtendedMetadata idpExtendedMetadata() {
        ExtendedMetadata extendedMetadata = new ExtendedMetadata();
        extendedMetadata.setAlias("eleg");
        return extendedMetadata;
    }

    @Bean
    public FilesystemMetadataProvider idpFilesystemMetadataProvider() throws MetadataProviderException {
        File spMetadataFile = new File(applicationDir + "/config/idp-eleg.xml");
        FilesystemMetadataProvider filesystemMetadataProvider = new FilesystemMetadataProvider(spMetadataFile);
        filesystemMetadataProvider.setParserPool(parserPool());
        return filesystemMetadataProvider;
    }

    @Bean
    public ExtendedMetadataDelegate idpExtendedMetadataDelegate() throws MetadataProviderException {
        ExtendedMetadataDelegate extendedMetadataDelegate = new ExtendedMetadataDelegate(idpFilesystemMetadataProvider(),
            idpExtendedMetadata());
        extendedMetadataDelegate.setMetadataTrustCheck(false);
        return extendedMetadataDelegate;
    }
    @Bean
    @Qualifier("metadata")
    public CachingMetadataManager metadata() throws MetadataProviderException {
        List<MetadataProvider> delegates = new ArrayList<>();
        delegates.add(spExtendedMetadataDelegate());
        delegates.add(idpExtendedMetadataDelegate());
        CachingMetadataManager metadataManager = new CachingMetadataManager(delegates);
        metadataManager.setRefreshCheckInterval(REFRESH_CHECK_INTERVAL);
        metadataManager.setDefaultIDP(samlIdpEntityId);
        metadataManager.setHostedSPName(samlSpEntityId);
        return metadataManager;
    }
    @Bean
    public RegexRequestMatcher saveRequestMatcher() {
        return new RegexRequestMatcher("\\/organizations", "GET");
    }

    @Bean
    public HttpSessionRequestCache httpSessionRequestCache() {
        HttpSessionRequestCache httpSessionRequestCache = new HttpSessionRequestCache();
        httpSessionRequestCache.setRequestMatcher(saveRequestMatcher());
        return new HttpSessionRequestCache();
    }
    @Bean
    @Qualifier("saml")
    public SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler() {
        SavedRequestAwareAuthenticationSuccessHandler successRedirectHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successRedirectHandler.setDefaultTargetUrl(targetUrl);
        successRedirectHandler.setRequestCache(httpSessionRequestCache());
        return successRedirectHandler;
    }

    @Bean
    @Qualifier("saml")
    public SimpleUrlAuthenticationFailureHandler authenticationFailureHandler() {
        SimpleUrlAuthenticationFailureHandler failureHandler = new SimpleUrlAuthenticationFailureHandler();
        failureHandler.setUseForward(true);
        failureHandler.setDefaultFailureUrl("/error");
        return failureHandler;
    }

    @Bean
    public SimpleUrlLogoutSuccessHandler successLogoutHandler() {
        SimpleUrlLogoutSuccessHandler successLogoutHandler = new SimpleUrlLogoutSuccessHandler();
        successLogoutHandler.setDefaultTargetUrl(baseUrl);
        return successLogoutHandler;
    }

    @Bean
    public SecurityContextLogoutHandler logoutHandler() {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.setInvalidateHttpSession(true);
        securityContextLogoutHandler.setClearAuthentication(true);
        return securityContextLogoutHandler;
    }

    @Bean
    public SAMLLogoutProcessingFilter samlLogoutProcessingFilter() {
        return new SAMLLogoutProcessingFilter(successLogoutHandler(), logoutHandler());
    }

    @Bean
    public VelocityEngine velocityEngine() {
        return VelocityFactory.getEngine();
    }

    @Bean
    public SAMLLogoutFilter samlLogoutFilter() {
        return new SAMLLogoutFilter(successLogoutHandler(),
            new LogoutHandler[] { logoutHandler() },
            new LogoutHandler[] { logoutHandler() });
    }

    @Bean
    public HTTPPostBinding httpPostBinding() {
        return new HTTPPostBinding(parserPool(), velocityEngine());
    }

    @Bean
    public HTTPRedirectDeflateBinding httpRedirectDeflateBinding() {
        return new HTTPRedirectDeflateBinding(parserPool());
    }

    @Bean
    public HttpClient httpClient() {
        return new HttpClient();
    }

    @Bean
    public HTTPSOAP11Binding soapBinding() {
        return new HTTPSOAP11Binding(parserPool());
    }

    private ArtifactResolutionProfileImpl artifactResolutionProfile() {
        final ArtifactResolutionProfileImpl artifactResolutionProfile = new ArtifactResolutionProfileImpl(httpClient());
        artifactResolutionProfile.setProcessor(new SAMLProcessorImpl(soapBinding()));
        return artifactResolutionProfile;
    }

    @Bean
    public HTTPArtifactBinding httpArtifactBinding() {
        return new HTTPArtifactBinding(parserPool(), velocityEngine(), artifactResolutionProfile());
    }

    @Bean
    public SAMLProcessorImpl processor() {
        ArrayList<SAMLBinding> bindings = new ArrayList<>();
        bindings.add(httpRedirectDeflateBinding());
        bindings.add(httpPostBinding());
        bindings.add(httpArtifactBinding());
        return new SAMLProcessorImpl(bindings);
    }

}
