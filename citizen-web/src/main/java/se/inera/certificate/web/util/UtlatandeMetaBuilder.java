package se.inera.certificate.web.util;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import se.inera.certificate.model.Utlatande;
import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.inera.certificate.web.service.dto.UtlatandeStatusType;
import se.inera.certificate.web.service.dto.UtlatandeStatusType.StatusType;

/**
 * Builder object for creating immutable {@link UtlatandeMetaData} objects.
 */
public class UtlatandeMetaBuilder {

    private String id;

    private String type;

    private String issuerName;

    private String facilityName;

    private LocalDateTime signDate;

    private String available;

    private String complemantaryInfo;

    private List<UtlatandeStatusType> statuses = new ArrayList<>();

    public UtlatandeMetaData build() {
        return new UtlatandeMetaData(id, type, issuerName, facilityName, signDate, available, complemantaryInfo, statuses);
    }

    public UtlatandeMetaBuilder id(String id) {
        this.id = id;

        return this;
    }

    public UtlatandeMetaBuilder type(String type) {
        this.type = type;

        return this;
    }

    public UtlatandeMetaBuilder issuerName(String issuerName) {
        this.issuerName = issuerName;

        return this;
    }

    public UtlatandeMetaBuilder facilityName(String facilityName) {
        this.facilityName = facilityName;

        return this;
    }

    public UtlatandeMetaBuilder signDate(LocalDateTime signDate) {
        this.signDate = signDate;

        return this;
    }

    public UtlatandeMetaBuilder available(String available) {
        this.available = available;

        return this;
    }

    public UtlatandeMetaBuilder complementaryInfo(String complemantaryInfo) {
        this.complemantaryInfo = complemantaryInfo;

        return this;
    }

    public UtlatandeMetaBuilder addStatus(StatusType type, String target, LocalDateTime timestamp) {
        return addStatus(new UtlatandeStatusType(type, target, timestamp));
    }

    public UtlatandeMetaBuilder addStatus(UtlatandeStatusType status) {
        this.statuses.add(status);

        return this;
    }

    /**
     * Utility method for creating a {@link UtlatandeMetaBuilder} from a {@link Utlatande} object populated with:
     * <ul>
     * <li>id
     * <li>type
     * <li>issuerName
     * <li>facilityName
     * <li>signDate
     * </ul>
     * 
     * @param utlatande
     *            The utlatande to extract meta data from.
     * 
     * @return A prepopulated builder.
     */
    public static UtlatandeMetaBuilder fromUtlatande(Utlatande utlatande) {
        UtlatandeMetaBuilder builder = new UtlatandeMetaBuilder();
        builder.id(utlatande.getId().getExtension())
                .type(utlatande.getTyp().getCode().toLowerCase())
                .issuerName(utlatande.getSkapadAv().getNamn())
                .facilityName(utlatande.getSkapadAv().getVardenhet().getNamn())
                .signDate(utlatande.getSigneringsdatum());

        return builder;
    }
}
