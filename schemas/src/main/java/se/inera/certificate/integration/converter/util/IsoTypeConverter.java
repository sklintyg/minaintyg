package se.inera.certificate.integration.converter.util;

import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;

/**
 * @author andreaskaltenbach
 */
public class IsoTypeConverter {

    private IsoTypeConverter() {
    }

    public static Id toId(II ii) {
        if (ii == null) return null;
        return new Id(ii.getRoot(), ii.getExtension());
    }

    public static II toII(Id id) {
        if (id == null) return null;

        II ii = new II();
        ii.setRoot(id.getRoot());
        ii.setExtension(id.getExtension());
        return ii;
    }

    public static Kod toKod(CD cd) {
        if (cd == null) return null;
        return new Kod(cd.getCodeSystem(), cd.getCodeSystemName(), cd.getCodeSystemVersion(), cd.getCode());
    }

    public static CD toCD(Kod kod) {
        if (kod == null) return null;
        CD cd = new CD();
        cd.setCode(kod.getCode());
        cd.setCodeSystem(kod.getCodeSystem());
        cd.setCodeSystemName(kod.getCodeSystemName());
        if(kod.getCodeSystemVersion() != null) {
            cd.setCodeSystemVersion(kod.getCodeSystemVersion());
        }
        return cd;
    }
}
