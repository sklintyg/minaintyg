package se.inera.intyg.minaintyg.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import org.springframework.session.web.http.CookieSerializer;

public class MinaIntygCookieSerializerTest {

  private final HttpServletRequest req = mock(HttpServletRequest.class);
  private final HttpServletResponse resp = mock(HttpServletResponse.class);

  @ParameterizedTest
  @MethodSource("userAgents")
  void writeCookieValue_samesite_exclusion(String userAgent, boolean isSameSiteNone) {

    final var minaIntygCookieSerializer = new MinaIntygCookieSerializer(true);

    final var cookieValue = new CookieSerializer.CookieValue(req, resp, "");
    when(req.isSecure()).thenReturn(true);
    when(req.getHeader(HttpHeaders.USER_AGENT)).thenReturn(userAgent);

    minaIntygCookieSerializer.writeCookieValue(cookieValue);

    final var stringCaptor = ArgumentCaptor.forClass(String.class);
    verify(resp).addHeader(anyString(), stringCaptor.capture());

    assertEquals(isSameSiteNone, stringCaptor.getValue().contains("SameSite=None"),
        () -> "Erroneous samesite attribut for: %s".formatted(userAgent)
    );
  }

  @ParameterizedTest
  @MethodSource("userAgents")
  void writeCookieValueSamesiteNoExclusion(String userAgent, boolean isSameSiteNone) {
    final var minaIntygCookieSerializer = new MinaIntygCookieSerializer(isSameSiteNone);

    final var cookieValue = new CookieSerializer.CookieValue(req, resp, "");
    when(req.isSecure()).thenReturn(true);
    when(req.getHeader(HttpHeaders.USER_AGENT)).thenReturn(userAgent);

    minaIntygCookieSerializer.writeCookieValue(cookieValue);

    final var stringCaptor = ArgumentCaptor.forClass(String.class);
    verify(resp).addHeader(anyString(), stringCaptor.capture());

    assertTrue(stringCaptor.getValue().contains("SameSite=None"),
        () -> "Erroneous samesite attribut for: %s".formatted(userAgent)
    );
  }

  public static Stream<Arguments> userAgents() {
    return Stream.of(
        Arguments.of(
            "Mozilla/5.0 doogiePIM/1.0.4.2 AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.84 Safari/537.36",
            false),
        Arguments.of(
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/52.0.2743.82 Safari/537.36",
            false),
        Arguments.of(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2883.87 Safari/537.36",
            false),
        Arguments.of("Mozilla/5.0 Chrome/54.0.2840.99 Safari/537.36", false),
        Arguments.of(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_2) AppleWebKit/537.36 (KHTML, like Gecko) "
                + "Chrome/55.0.2883.95 Safari/537.36", false),
        Arguments.of(
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.76 Safari/537.36",
            false),
        Arguments.of(
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.133 Safari/537.36",
            false),
        Arguments.of("Mozilla/5.0 (Linux; Android 8.0.0) AppleWebKit/537.36 (KHTML, like Gecko) "
            + "Version/4.0 Klar/1.0 Chrome/58.0.3029.121 Mobile Safari/537.36", false),
        Arguments.of(
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36",
            false),
        Arguments.of(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.78 Safari/537.36",
            false),
        Arguments.of(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.79 Safari/537.36",
            false),
        Arguments.of(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/62.0.3165.0 Safari/537.36",
            false),
        Arguments.of(
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3213.3 Safari/537.36",
            false),
        Arguments.of(
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.119 Safari/537.36",
            false),
        Arguments.of(
            "Mozilla/5.0 (Win) AppleWebKit/1000.0 (KHTML, like Gecko) Chrome/65.663 Safari/1000.01",
            false),
        Arguments.of(
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3334.0 Safari/537.36",
            false),
        Arguments.of(
            "Mozilla/5.0 (Linux; Android 4.4.4; One Build/KTU84L.H4) AppleWebKit/537.36 (KHTML, like Gecko) "
                + "Version/4.0 Chrome/66.0.0.0 Mobile Safari/537.36 [FB_IAB/FB4A;FBAV/28.0.0.20.16;]",
            false),
        Arguments.of(
            "UCWEB/2.0 (MIDP-2.0; U; Adr 4.0.4; en-US; ZTE_U795) U2/1.0.0 UCBrowser/10.7.6.805 U2/1.0.0 Mobile",
            false),
        Arguments.of(
            "Mozilla/5.0 (Linux; U; Android 7.1.1; en-US; Lenovo K8 Note Build/NMB26.54-74) AppleWebKit/537.36 (KHTML, like Gecko) "
                + "Version/4.0 Chrome/57.0.2987.108 UCBrowser/12.0.0.1088 Mobile Safari/537.36",
            false),
        Arguments.of(
            "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X; zh-CN) AppleWebKit/537.51.1 (KHTML, like Gecko) "
                + "Mobile/15A5304i UCBrowser/11.5.7.986 Mobile AliApp(TUnionSDK/0.1.15)", false),
        Arguments.of(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_1) AppleWebKit/605.1.15 (KHTML, like Gecko) "
                + "Version/12.0 Safari/605.1.15", false),
        Arguments.of(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_2) AppleWebKit/537.36 (KHTML, like Gecko)",
            false),
        Arguments.of(
            "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/ 604.1.21 (KHTML, like Gecko) "
                + "Version/ 12.0 Mobile/17A6278a Safari/602.1.26", false),
        Arguments.of(
            "Mozilla/5.0 (iPhone; CPU iPhone OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) "
                + "CriOS/70.0.3538.75 Mobile/15E148 Safari/605.1", false),
        Arguments.of(
            "Mozilla/5.0 (iPad; CPU OS 12_0 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) "
                + "FxiOS/13.2b11866 Mobile/16A366 Safari/605.1.15", false),
        Arguments.of(
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.2661.102 Safari/537.36",
            true),
        Arguments.of(
            "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.2526.73 Safari/537.36",
            true),
        Arguments.of(
            "Mozilla/5.0 (iPad; CPU OS 11_0 like Mac OS X) AppleWebKit/603.1.30 (KHTML, like Gecko) "
                + "CriOS/60.0.3112.72 Mobile/15A5327g Safari/602.1", true),
        Arguments.of(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/79.0.3945.29 Safari/537.36",
            true),
        Arguments.of(
            "Mozilla/5.0 (Linux; U; Android 8.0.0; en-US; Pixel XL Build/OPR3.170623.007) AppleWebKit/534.30 (KHTML, like Gecko) "
                + "Version/4.0 UCBrowser/12.13.2.1005 U3/0.8.0 Mobile Safari/534.30", true),
        Arguments.of(
            "Mozilla/5.0 (Linux; U; Android 8.0.0; en-US; Pixel XL Build/OPR3.170623.007) AppleWebKit/534.30 (KHTML, like Gecko) "
                + "Version/4.0 UCBrowser/12.13.4.1005 U3/0.8.0 Mobile Safari/534.30", true),
        Arguments.of(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_1) AppleWebKit/605.1.15 (KHTML, like Gecko) "
                + "Version/13.0.3 Safari/605.1.15", true),
        Arguments.of(
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15) AppleWebKit/601.1.39 (KHTML, like Gecko) Version/10.1.2 Safari/601.1.39",
            true),
        Arguments.of(
            "Mozilla/5.0 (iPhone; CPU iPhone OS 13_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) "
                + "Version/66.6 Mobile/14A5297c Safari/602.1", true)
    );
  }
}