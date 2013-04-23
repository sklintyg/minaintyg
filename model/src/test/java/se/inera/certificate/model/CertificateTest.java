package se.inera.certificate.model;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:persistence-config.xml"})
@ActiveProfiles("dev")
public class CertificateTest {

  @PersistenceContext
  private EntityManager entityManager;

  @Autowired
  private DataSource dataSource;
    
  @Test
  public void testFind() {
      List<String> types = Arrays.asList(new String[]{"INTYG"});
      
      int size = entityManager.createNamedQuery("CertificateMetaData.findByCivicRegistrationNumberAndType")
          .setParameter("civicRegistrationNumber", "121212-1212")
          .setParameter("types", types)
          .getResultList()
          .size();
      
      assertEquals(1, size);
  }
  
  @Test
  public void testFindNone() {
      List<String> types = Arrays.asList(new String[]{"INTYG1"});
      
      int size = entityManager.createNamedQuery("CertificateMetaData.findByCivicRegistrationNumberAndType")
          .setParameter("civicRegistrationNumber", "121212-1212")
          .setParameter("types", types)
          .getResultList()
          .size();
      
      assertEquals(0, size);
  }

}