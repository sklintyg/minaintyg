package se.inera.certificate.web.service.dto;

import static org.springframework.util.Assert.hasText;

public class UtlatandeRecipient {

    private final String id;
    private final String name;

    public UtlatandeRecipient(String id, String name) {
        hasText(id, "recipient id must not be empty");
        hasText(name, "recipient name must not be empty");
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
