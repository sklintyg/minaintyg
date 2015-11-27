package se.inera.certificate.web.util;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDateTime;

import se.inera.certificate.web.service.dto.UtlatandeMetaData;
import se.inera.intyg.common.support.model.CertificateState;
import se.inera.intyg.common.support.model.Status;
import se.inera.intyg.common.support.model.common.internal.Utlatande;

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

    private String additionalInfo;

    private List<Status> statuses = new ArrayList<>();

    public UtlatandeMetaData build() {
        return new UtlatandeMetaData(id, type, issuerName, facilityName, signDate, available, additionalInfo, statuses);
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

    public UtlatandeMetaBuilder additionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;

        return this;
    }

    public UtlatandeMetaBuilder addStatus(CertificateState type, String target, LocalDateTime timestamp) {
        return addStatus(new Status(type, target, timestamp));
    }

    public UtlatandeMetaBuilder addStatus(Status status) {
        this.statuses.add(status);

        return this;
    }

    /**
     * Utility method for creating a {@link UtlatandeMetaBuilder} from a {@link Utlatande} object populated with.
     *
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
        String id = utlatande.getId();
        builder.id(id)
                .type(utlatande.getTyp())
                .issuerName(utlatande.getGrundData().getSkapadAv().getFullstandigtNamn())
                .facilityName(utlatande.getGrundData().getSkapadAv().getVardenhet().getEnhetsnamn())
                .signDate(utlatande.getGrundData().getSigneringsdatum());

        return builder;
    }
}
