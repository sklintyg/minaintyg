package se.inera.certificate.model;

/**
 * @author andreaskaltenbach
 */
public enum CertificateState {
    UNHANDLED,
    DELETED,
    RESTORED,
    CANCELLED,
    SENT,
    RECEIVED,
    IN_PROGRESS,
    PROCESSED
}
