package se.inera.certificate.web.spec;

public class Browser {

    private static geb.Browser browser
    
    static void öppna() {
        if (browser) throw new IllegalStateException("Browser already initialized")
        browser = new geb.Browser()
    }
    
    public void stäng() {
        if (!browser) throw new IllegalStateException("Browser not initialized")
        browser.quit()
        browser = null
    }
    
    static geb.Browser drive(Closure script) {
        if (!browser) throw new IllegalStateException("Browser not initialized")
        script.delegate = browser
        script()
        browser
    }

}
