package se.inera.intyg.minaintyg.web.web.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

public class JspPageAttributes {

    private static final String USE_MINIFIED_JAVA_SCRIPT = "minaintyg.useMinifiedJavaScript";

    @Autowired
    @Value("${mvk.url.main}")
    private String mvkMainUrl;

    @Autowired
    private Environment environment;

    public String getMvkMainUrl() {
        return mvkMainUrl;
    }

    public String getUseMinifiedJavaScript() {
        String useMinifiedJavaScript = System.getProperty(USE_MINIFIED_JAVA_SCRIPT);
        if (useMinifiedJavaScript == null) {
            return Boolean.TRUE.toString();
        }

        return useMinifiedJavaScript;
    }

}
