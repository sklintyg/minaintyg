package se.inera.certificate.integration;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import se.inera.certificate.integration.test.CertificateResource;
import se.inera.certificate.model.Certificate;

@RunWith(MockitoJUnitRunner.class)
public class CertificateResourceTest {

    @Mock
    private EntityManager entityManager = mock(EntityManager.class);

    @InjectMocks
    private CertificateResource certificateResource = new CertificateResource();

    @Test
    public void testGetCertificate() throws Exception {
        certificateResource.getCertificate("1");

        verify(entityManager).find(Certificate.class, "1");
    }

    @Test
    public void testDeleteCertificate() throws Exception {
        Certificate certificate = new Certificate("1", "");
        when(entityManager.find(Certificate.class, "1")).thenReturn(certificate);

        certificateResource.deleteCertificate("1");

        verify(entityManager).find(Certificate.class, "1");
        verify(entityManager).remove(certificate);
    }

    @Test
    public void testInsertCertificate() throws Exception {
        Certificate certificate = new Certificate("1", "");
        certificateResource.insertCertificate(certificate);

        verify(entityManager).persist(certificate);
    }
}
