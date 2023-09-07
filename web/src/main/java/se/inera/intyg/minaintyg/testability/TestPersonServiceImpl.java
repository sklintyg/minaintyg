package se.inera.intyg.minaintyg.testability;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TestPersonServiceImpl implements TestPersonService {

  @Override
  public List<TestPerson> getPersons() {
    return listOfTestPersons();
  }

  private List<TestPerson> listOfTestPersons() {
    return List.of(
        TestPerson.builder()
            .personId("198901192396")
            .personName("Agnarsson Agnarsson")
            .build(),
        TestPerson.builder()
            .personId("200210282398")
            .personName("Albert Albertsson")
            .build(),
        TestPerson.builder()
            .personId("200210292389")
            .personName("Albertina Alison")
            .build(),
        TestPerson.builder()
            .personId("197901242391")
            .personName("Albin Ander")
            .build(),
        TestPerson.builder()
            .personId("194110299221")
            .personName("Alexa Valfridsson")
            .build(),
        TestPerson.builder()
            .personId("197901252382")
            .personName("Aline Andersson")
            .build(),
        TestPerson.builder()
            .personId("199606282391")
            .personName("Allan Allanson")
            .build(),
        TestPerson.builder()
            .personId("199606292382")
            .personName("Alma Almarsson")
            .build(),
        TestPerson.builder()
            .personId("194112128154")
            .personName("Alve Alfredsson")
            .build()
    );
  }
}