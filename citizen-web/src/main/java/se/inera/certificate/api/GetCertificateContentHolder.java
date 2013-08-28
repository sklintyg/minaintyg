package se.inera.certificate.api;

import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * Wrapper class for holding the external format of a certificate as well as metadata about the certificate, such as status
 * @author marced
 */
public class GetCertificateContentHolder {
    
    @JsonRawValue
    private String certificateContent;
    
    private CertificateContentMeta certificateContentMeta;


    public CertificateContentMeta getCertificateContentMeta() {
        return certificateContentMeta;
    }

    public void setCertificateContentMeta(CertificateContentMeta certificateContentMeta) {
        this.certificateContentMeta = certificateContentMeta;
    }

    public String getCertificateContent() {
        return certificateContent;
    }

    public void setCertificateContent(String certificateContent) {
        this.certificateContent = certificateContent;
    }



}
