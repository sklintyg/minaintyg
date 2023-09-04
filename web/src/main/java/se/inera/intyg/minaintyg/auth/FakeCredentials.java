package se.inera.intyg.minaintyg.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FakeCredentials {
    private String personId;

    private String personName;
    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    @Override
    public String toString() {
        return "FakeCredentials{" +
            "personId='" + personId + '\'' +
            ", personName='" + personName + '\'' +
            '}';
    }
}
