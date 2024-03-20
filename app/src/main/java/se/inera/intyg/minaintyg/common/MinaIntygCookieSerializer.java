package se.inera.intyg.minaintyg.common;

import java.util.regex.Pattern;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpHeaders;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * This is needed to make IdP functionality work. This will not satisfy all browsers, but it works
 * for IE, Chrome and Edge. Reference:
 * <a href="https://auth0.com/blog/browser-behavior-changes-what-developers-need-to-know/">...</a>
 */
public class MinaIntygCookieSerializer extends DefaultCookieSerializer {

  private final boolean useSameSiteNoneExclusion;
  private final Pattern ucBrowserPattern = Pattern.compile("UCBrowser/(\\d+)\\.(\\d+)\\.(\\d+)\\.");

  public MinaIntygCookieSerializer(boolean useSameSiteNoneExclusion) {
    super();
    this.useSameSiteNoneExclusion = useSameSiteNoneExclusion;
  }

  @Override
  public void writeCookieValue(CookieValue cookieValue) {
    final var request = cookieValue.getRequest();

    if (useSameSiteNoneExclusion && exludeSameSiteNone(request.getHeader(HttpHeaders.USER_AGENT))) {
      setSameSite(null);
    } else {
      setUseSecureCookie(true);
      setSameSite(SameSite.NONE.attributeValue());
    }

    super.writeCookieValue(cookieValue);
  }

  /**
   * Som older browser/OS doesn't handle samesite=none. These must be excluded when this attribute
   * is set for session cookies.
   * <ul>
   * <li>Based on <a href="https://catchjs.com/Blog/SameSiteCookies">...</a></li>
   * <li>Information on incompatible browsers: https://www.chromium.org/updates/same-site/incompatible-clients</li>
   * </ul>
   *
   * @param userAgent User Agent
   * @return true if samesite=none should not be set.
   */
  private boolean exludeSameSiteNone(String userAgent) {
    //iOS 12
    return userAgent.contains("iPhone OS 12_") || userAgent.contains("iPad; CPU OS 12_")
        //UC Browser < 12.13.2
        || (userAgent.contains("UCBrowser/") ? isOlderUcBrowser(userAgent)
        //Chrome
        : (userAgent.contains("Chrome/5") || userAgent.contains("Chrome/6")))
        //Chromium
        || userAgent.contains("Chromium/5") || userAgent.contains("Chromium/6")
        //Safari on MacOS 10.14
        || (userAgent.contains(" OS X 10_14_")
        && ((userAgent.contains("Version/") && userAgent.contains("Safari"))
        //Embedded browser on macOS 10.14
        || userAgent.endsWith("(KHTML, like Gecko)")));
  }

  private boolean isOlderUcBrowser(String userAgent) {
    // CHECKSTYLE:OFF MagicNumber
    final var uaMatcher = ucBrowserPattern.matcher(userAgent);

    if (!uaMatcher.find()) {
      return false;
    }

    final var major = Integer.parseInt(uaMatcher.group(1));
    final var minor = Integer.parseInt(uaMatcher.group(2));
    final var build = Integer.parseInt(uaMatcher.group(3));

    if (major != 12) {
      return major < 12;
    }

    if (minor != 13) {
      return minor < 13;
    }
    return build < 2;
    // CHECKSTYLE:ON MagicNumber
  }
}