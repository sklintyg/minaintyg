package se.inera.intyg.minaintyg.testability;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TestPersonService {

  public List<TestPerson> getPersons() {
    return listOfTestPersons();
  }

  private List<TestPerson> listOfTestPersons() {
    return List.of(
        TestPerson.builder()
            .personId("194011306125")
            .personName("Athena React Andersson ")
            .build(),
        TestPerson.builder()
            .personId("198901192396")
            .personName("Agnarsson React Agnarsson")
            .build(),
        TestPerson.builder()
            .personId("200210282398")
            .personName("Albert React Albertsson")
            .build(),
        TestPerson.builder()
            .personId("200210292389")
            .personName("Albertina React Alison")
            .build(),
        TestPerson.builder()
            .personId("197901242391")
            .personName("Albin React Ander")
            .build(),
        TestPerson.builder()
            .personId("194110299221")
            .personName("Alexa React Valfridsson")
            .build(),
        TestPerson.builder()
            .personId("197901252382")
            .personName("Aline React Andersson")
            .build(),
        TestPerson.builder()
            .personId("199606282391")
            .personName("Allan React Allanson")
            .build(),
        TestPerson.builder()
            .personId("199606292382")
            .personName("Alma React Almarsson")
            .build(),
        TestPerson.builder()
            .personId("194112128154")
            .personName("Alve React Alfredsson")
            .build()
    );
  }
}