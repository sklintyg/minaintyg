package se.inera.intyg.minaintyg.integration.pu;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import se.inera.intyg.minaintyg.integration.api.PUIntegrationService;
import se.inera.intyg.minaintyg.integration.dto.PersonRequest;
import se.inera.intyg.minaintyg.integration.dto.PersonResponse;

@Component
public class PUIntegrationServiceImpl implements PUIntegrationService {
    private final WebClient webClient;
    private final String scheme;
    private final String baseUrl;
    private final int port;
    private final String puEndpoint;

    public PUIntegrationServiceImpl(
        @Qualifier(value = "intygProxyWebClient") WebClient webClient,
        @Value("${integration.intygProxy.scheme}") String scheme,
        @Value("${integration.intygProxy.baseurl}") String baseUrl,
        @Value("${integration.intygProxy.port}") int port,
        @Value("${integration.intygProxy.pu.endpoint}") String puEndpoint) {
        this.webClient = webClient;
        this.scheme = scheme;
        this.baseUrl = baseUrl;
        this.port = port;
        this.puEndpoint = puEndpoint;
    }

    @Override
    public PersonResponse getPersonResponse(PersonRequest personRequest) {
        return webClient.post().uri(uriBuilder -> uriBuilder
                .scheme(scheme)
                .host(baseUrl)
                .port(port)
                .path(puEndpoint)
                .build())
            .body(Mono.just(personRequest), PersonRequest.class)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(PersonResponse.class)
            .share()
            .block();
    }
}
