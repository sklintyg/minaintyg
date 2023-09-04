package se.inera.intyg.minaintyg.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.DEV_PROFILE;

import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.opensaml.security.x509.X509Support;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.Saml2MetadataFilter;
import org.springframework.security.saml2.provider.service.web.authentication.Saml2WebSsoAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.web.http.DefaultCookieSerializer;
import se.inera.intyg.minaintyg.auth.FakeAuthenticationProvider;
import se.inera.intyg.minaintyg.auth.MinaIntygLoggingSessionRegistryImpl;
import se.inera.intyg.minaintyg.auth.Saml2AuthenticationToken;
import se.inera.intyg.minaintyg.filter.FakeAuthenticationFilter;
import se.inera.intyg.minaintyg.service.monitoring.MonitoringLogService;


@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final MonitoringLogService monitoringLogService;
    private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;
    private static final String ELEG = "eleg";
    private static List<String> profiles;
    @Value("${private.key}")
    private RSAPrivateKey key;
    @Value("${public.certificate}")
    private String certificate;
    @Value("${intyg.metadata.url}")
    private String metadataUrl;
    @Value("${slo.url}")
    private String singleLogoutServiceLocation;

    public WebSecurityConfig(MonitoringLogService monitoringLogService,
        FindByIndexNameSessionRepository<? extends Session> sessionRepository, Environment environment) {
        this.monitoringLogService = monitoringLogService;
        this.sessionRepository = sessionRepository;
        this.profiles = Arrays.asList(environment.getActiveProfiles());
    }

    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository() throws CertificateException {
        final var decodeCertificate = getX509Certificate();
        final var credential = Saml2X509Credential.signing(key, decodeCertificate);
        final var registration = RelyingPartyRegistrations
            .fromMetadataLocation(metadataUrl)
            .registrationId(ELEG)
            .singleLogoutServiceLocation(singleLogoutServiceLocation)
            .signingX509Credentials((signing) -> signing.add(credential))
            .build();
        return new InMemoryRelyingPartyRegistrationRepository(registration);
    }

    private X509Certificate getX509Certificate() throws CertificateException {
        final var classLoader = getClass().getClassLoader();
        final var verificationKey = new File(Objects.requireNonNull(classLoader.getResource(certificate)).getFile());
        final var decodeCertificate = X509Support.decodeCertificate(verificationKey);
        if (decodeCertificate == null) {
            log.error("Unable to extract X509Certificat from string representation. Provided path: {}", certificate);
            throw new CertificateException();
        }
        return decodeCertificate;
    }

    @Bean
    public Http403ForbiddenEntryPoint http403ForbiddenEntryPoint() {
        return new Http403ForbiddenEntryPoint();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public RegisterSessionAuthenticationStrategy registerSessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(sessionAuthenticationStrategy());
    }
    @Bean
    public MinaIntygLoggingSessionRegistryImpl<? extends Session> sessionAuthenticationStrategy() {
        return new MinaIntygLoggingSessionRegistryImpl<>(sessionRepository, monitoringLogService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(filterSecurity -> filterSecurity.authenticationEntryPoint(loginAuthenticationEntryPoint()))
            .authorizeHttpRequests(WebSecurityConfig::configureAlwaysPermitted)
            .authorizeHttpRequests(request -> request.requestMatchers("/**").fullyAuthenticated())
            .exceptionHandling(exceptionHandler -> exceptionHandler.accessDeniedPage("/error"))
            .saml2Login(saml2 -> saml2.authenticationManager(new ProviderManager(getOpenSaml4AuthenticationProvider())))
            .saml2Logout(saml2 -> saml2.logoutUrl("/"))
            .addFilterBefore(getSaml2MetadataFilter(relyingPartyRegistrationRepository()), Saml2WebSsoAuthenticationFilter.class)
            .sessionManagement(securitySessionManagementConfigurer -> securitySessionManagementConfigurer.sessionAuthenticationStrategy(registerSessionAuthenticationStrategy()))
            .csrf(AbstractHttpConfigurer::disable)
            .cors(withDefaults());

        if (profiles.contains(DEV_PROFILE)) {
            configureFake(http);
        }

        return http.authorizeHttpRequests(request -> request.anyRequest().authenticated()).build();
    }

    private void configureFake(HttpSecurity http) throws Exception {
        http.
            authorizeHttpRequests((request) -> {
                    request.requestMatchers("/fake/sso").permitAll();
                    request.requestMatchers("/welcome").permitAll();
                }
            )
            .addFilterAt(fakeAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class)
            .securityContext(context -> context.requireExplicitSave(false));
    }

    private static Saml2MetadataFilter getSaml2MetadataFilter(RelyingPartyRegistrationRepository relyingPartyRegistrationRepository) {
        final var relyingPartyRegistrationResolver = new DefaultRelyingPartyRegistrationResolver(relyingPartyRegistrationRepository);
        return new Saml2MetadataFilter(relyingPartyRegistrationResolver, new OpenSamlMetadataResolver());
    }


    private static void configureAlwaysPermitted(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry request) {
        request.requestMatchers("/").permitAll();
        request.requestMatchers("/ping").permitAll();
        request.requestMatchers("/links").permitAll();
        request.requestMatchers("/config").permitAll();
        request.requestMatchers("/error/**").permitAll();
    }

    @Bean
    public AuthenticationEntryPoint loginAuthenticationEntryPoint(){
        return new LoginUrlAuthenticationEntryPoint("/");
    }

    // https://stackoverflow.com/questions/72508155/spring-saml2-and-spring-session-savedrequest-not-retrieved-cannot-redirect-to
    @Bean
    public DefaultCookieSerializer cookieSerializerCustomizer() {
//        return cookieSerializer -> {
//            cookieSerializer.setSameSite(null);
//        };
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setCookiePath("/");
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        serializer.setSameSite(null);
        return serializer;
    }
    @Bean
    public FakeAuthenticationFilter fakeAuthenticationFilter() {
        final var fakeAuthenticationFilter = new FakeAuthenticationFilter();
        fakeAuthenticationFilter.setAuthenticationManager(new ProviderManager(getFakeAuthenticationProvider()));
        fakeAuthenticationFilter.setSessionAuthenticationStrategy(registerSessionAuthenticationStrategy());
        return fakeAuthenticationFilter;
    }


    private FakeAuthenticationProvider getFakeAuthenticationProvider() {
        return new FakeAuthenticationProvider();
    }

    private OpenSaml4AuthenticationProvider getOpenSaml4AuthenticationProvider() {
        final var authenticationProvider = new OpenSaml4AuthenticationProvider();
        authenticationProvider.setResponseAuthenticationConverter(responseToken -> {
            final var authentication = OpenSaml4AuthenticationProvider
                .createDefaultResponseAuthenticationConverter()
                .convert(responseToken);
            assert authentication != null;
            return new Saml2AuthenticationToken(authentication);
        });
        return authenticationProvider;
    }
}
