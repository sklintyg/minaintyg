package se.inera.certificate.model;

import java.util.List;

/**
 * @author andreaskaltenbach
 */
public class Arbetsformaga {

    private String motivering;

    private String arbetsuppgift;

    private List<ArbetsformagaNedsattning> arbetsformagaNedsattningar;

    private List<Sysselsattning> sysselsattningar;

    public String getMotivering() {
        return motivering;
    }

    public void setMotivering(String motivering) {
        this.motivering = motivering;
    }

    public String getArbetsuppgift() {
        return arbetsuppgift;
    }

    public void setArbetsuppgift(String arbetsuppgift) {
        this.arbetsuppgift = arbetsuppgift;
    }

    public List<ArbetsformagaNedsattning> getArbetsformagaNedsattningar() {
        return arbetsformagaNedsattningar;
    }

    public void setArbetsformagaNedsattningar(List<ArbetsformagaNedsattning> arbetsformagaNedsattningar) {
        this.arbetsformagaNedsattningar = arbetsformagaNedsattningar;
    }

    public List<Sysselsattning> getSysselsattningar() {
        return sysselsattningar;
    }

    public void setSysselsattningar(List<Sysselsattning> sysselsattningar) {
        this.sysselsattningar = sysselsattningar;
    }
}
