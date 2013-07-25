package se.inera.certificate.model;

import org.joda.time.Partial;

/**
 * @author andreaskaltenbach
 */
public class PartialInterval {

    private Partial from;
    private Partial tom;

    public PartialInterval() {
    }

    public PartialInterval(Partial from, Partial tom) {
        this.from = from;
        this.tom = tom;
    }

    public Partial getFrom() {
        return from;
    }

    public void setFrom(Partial from) {
        this.from = from;
    }

    public Partial getTom() {
        return tom;
    }

    public void setTom(Partial tom) {
        this.tom = tom;
    }
}
