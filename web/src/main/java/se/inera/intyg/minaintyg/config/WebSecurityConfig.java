package se.inera.intyg.minaintyg.config;

import static org.springframework.security.config.Customizer.withDefaults;
import static se.inera.intyg.minaintyg.auth.AuthenticationConstants.PERSON_ID_ATTRIBUTE;

import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.session.DefaultCookieSerializerCustomizer;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.authentication.DefaultSaml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.authentication.OpenSaml4AuthenticationProvider;
import org.springframework.security.saml2.provider.service.authentication.Saml2Authentication;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import se.inera.intyg.minaintyg.auth.AuthenticationConstants;
import se.inera.intyg.minaintyg.auth.LoginMethod;
import se.inera.intyg.minaintyg.auth.MinaIntygUserDetailService;
import se.inera.intyg.minaintyg.auth.Saml2AuthenticationToken;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

  public static final String TESTABILITY_PROFILE = "testability";
  public static final String TESTABILITY_API = "/api/testability/**";
  public static final String HEALTH_CHECK_ENDPOINT = "/actuator/health";
  public static final String APP_BUNDLE_NAME = "app";
  private final MinaIntygUserDetailService minaIntygUserDetailService;
  private final Environment environment;
  @Value("${spring.ssl.bundle.jks.app.key.alias}")
  private String alias;
  @Value("${spring.ssl.bundle.jks.app.keystore.password}")
  private String password;
  @Value("${saml.idp.metadata.location}")
  private String samlIdpMetadataLocation;
  @Value("${saml.sp.assertion.consumer.service.location}")
  private String assertionConsumerServiceLocation;
  @Value("${saml.sp.single.logout.service.location}")
  private String singleLogoutServiceLocation;
  @Value("${saml.login.success.url}")
  private String samlLoginSuccessUrl;
  @Value("${saml.login.success.url.always.use}")
  private boolean samlLoginSuccessUrlAlwaysUse;

  @Bean
  public RelyingPartyRegistrationRepository relyingPartyRegistrationRepository(
      SslBundles sslBundles)
      throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {

    final var app = sslBundles.getBundle(APP_BUNDLE_NAME);

    final var appPrivateKey = (PrivateKey) app.getStores()
        .getKeyStore()
        .getKey(alias, password.toCharArray());

    final var appCertificate = (X509Certificate) app.getStores()
        .getKeyStore()
        .getCertificate(alias);

    final var registration = RelyingPartyRegistrations
        .fromMetadataLocation(samlIdpMetadataLocation)
        .registrationId(AuthenticationConstants.ELEG_PARTY_REGISTRATION_ID)
        .assertionConsumerServiceLocation(assertionConsumerServiceLocation)
        .singleLogoutServiceLocation(singleLogoutServiceLocation)
        .signingX509Credentials(signing ->
            signing.add(
                Saml2X509Credential.signing(appPrivateKey, appCertificate)
            )
        )
        .build();

    return new InMemoryRelyingPartyRegistrationRepository(registration);
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http,
      RelyingPartyRegistrationRepository relyingPartyRegistrationRepository) throws Exception {

    if (environment.acceptsProfiles(Profiles.of(TESTABILITY_PROFILE))) {
      configureTestability(http);
    }

    http
        .authorizeHttpRequests(request -> request.
            requestMatchers(HEALTH_CHECK_ENDPOINT).permitAll().
            anyRequest().fullyAuthenticated()
        )
        .saml2Login(saml2 -> saml2
            .relyingPartyRegistrationRepository(relyingPartyRegistrationRepository)
            .authenticationManager(
                new ProviderManager(
                    getOpenSaml4AuthenticationProvider()
                )
            )
            .defaultSuccessUrl(samlLoginSuccessUrl, samlLoginSuccessUrlAlwaysUse)
        )
        .csrf(csrfConfigurer -> csrfConfigurer
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
        )
        .saml2Logout(withDefaults())
        .saml2Metadata(withDefaults())
        .csrf(AbstractHttpConfigurer::disable);

    return http.build();
  }

  private void configureTestability(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(request -> request.
            requestMatchers(TESTABILITY_API).permitAll()
        )
        .csrf(csrfConfigurer -> csrfConfigurer
            .ignoringRequestMatchers(TESTABILITY_API)
        );
  }

  // https://stackoverflow.com/questions/72508155/spring-saml2-and-spring-session-savedrequest-not-retrieved-cannot-redirect-to
  @Bean
  public DefaultCookieSerializerCustomizer cookieSerializerCustomizer() {
    return cookieSerializer -> cookieSerializer.setSameSite(null);
  }

  private OpenSaml4AuthenticationProvider getOpenSaml4AuthenticationProvider() {
    final var authenticationProvider = new OpenSaml4AuthenticationProvider();
    authenticationProvider.setResponseAuthenticationConverter(responseToken -> {
      final var authentication = OpenSaml4AuthenticationProvider
          .createDefaultResponseAuthenticationConverter()
          .convert(responseToken);
      if (!(authentication != null && authentication.isAuthenticated())) {
        //TODO: Look into better error handling when working with Authentication-jira
        return null;
      }
      final var personId = getAttribute(authentication);
      final var principal = minaIntygUserDetailService.buildPrincipal(personId, LoginMethod.ELVA77);
      return new Saml2AuthenticationToken(principal, authentication);
    });
    return authenticationProvider;
  }

  private String getAttribute(Saml2Authentication samlCredential) {
    final var principal = (DefaultSaml2AuthenticatedPrincipal) samlCredential.getPrincipal();
    final var attributes = principal.getAttributes();
    if (attributes.containsKey(PERSON_ID_ATTRIBUTE)) {
      return (String) attributes.get(PERSON_ID_ATTRIBUTE).get(0);
    }
    throw new IllegalArgumentException(
        "Could not extract attribute '" + PERSON_ID_ATTRIBUTE + "' from Saml2Authentication.");
  }
}
