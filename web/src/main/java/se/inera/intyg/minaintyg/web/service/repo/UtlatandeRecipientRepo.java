package se.inera.intyg.minaintyg.web.service.repo;

import se.inera.intyg.minaintyg.web.service.dto.UtlatandeRecipient;

import java.util.List;

public interface UtlatandeRecipientRepo {
    List<UtlatandeRecipient> getAllRecipients();
}
