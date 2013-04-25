package se.inera.certificate.web.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import se.inera.certificate.web.security.Citizen;

@Service
public class CitizenService {

    public Citizen getCitizen() {
        return (Citizen) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
