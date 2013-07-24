package se.inera.certificate.integration.converter.util;

import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;
import iso.v21090.dt.v1.IVLTS;
import iso.v21090.dt.v1.TS;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import se.inera.certificate.model.Id;
import se.inera.certificate.model.Kod;
import se.inera.certificate.model.LocalDateTimeInterval;

/**
 * @author andreaskaltenbach
 */
public class IsoTypeConverter {

    private static final String DATE_FORMAT = "yyyyddMM";
    private static final String DATE_TIME_FORMAT = "yyyyddMMHHmmss";

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_FORMAT);

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
        cd.setCodeSystemVersion(kod.getCodeSystemVersion());
        return cd;
    }

    public static LocalDate toLocalDate(TS timestamp) {
        if (timestamp == null) return null;
        return LocalDate.parse(timestamp.getValue(), DATE_TIME_FORMATTER);
    }

    public static LocalDateTime toLocalDateTime(TS timestamp) {
        if (timestamp == null) return null;
        return LocalDateTime.parse(timestamp.getValue(), DATE_TIME_FORMATTER);
    }

    public static LocalDateTimeInterval toLocalDateTimeInterval(IVLTS interval) {
        if (interval == null) return null;
        return new LocalDateTimeInterval(toLocalDateTime(interval.getLow()), toLocalDateTime(interval.getHigh()));
    }
}
