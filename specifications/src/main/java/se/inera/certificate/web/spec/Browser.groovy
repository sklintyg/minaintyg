package se.inera.certificate.web.spec;

public class Browser {

    public void stäng() {
        geb.Browser.drive {
        }.quit()
    }
}
