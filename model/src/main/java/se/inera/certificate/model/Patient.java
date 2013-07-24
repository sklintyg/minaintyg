package se.inera.certificate.model;

import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class Patient {

    private Id id;
    private List<String> fornamns;
    private List<String> mellannamns;
    private List<String> efternamns;

    private List<Sysselsattning> sysselsattnings;
    private List<Arbetsuppgift> arbetsuppgifts;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public List<String> getFornamns() {
        return fornamns;
    }

    public void setFornamns(List<String> fornamns) {
        this.fornamns = fornamns;
    }

    public List<String> getMellannamns() {
        return mellannamns;
    }

    public void setMellannamns(List<String> mellannamns) {
        this.mellannamns = mellannamns;
    }

    public List<String> getEfternamns() {
        return efternamns;
    }

    public void setEfternamns(List<String> efternamns) {
        this.efternamns = efternamns;
    }

    public List<Sysselsattning> getSysselsattnings() {
        return sysselsattnings;
    }

    public void setSysselsattnings(List<Sysselsattning> sysselsattnings) {
        this.sysselsattnings = sysselsattnings;
    }

    public List<Arbetsuppgift> getArbetsuppgifts() {
        return arbetsuppgifts;
    }

    public void setArbetsuppgifts(List<Arbetsuppgift> arbetsuppgifts) {
        this.arbetsuppgifts = arbetsuppgifts;
    }
}
