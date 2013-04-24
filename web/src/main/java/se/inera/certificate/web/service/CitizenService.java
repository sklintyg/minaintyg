package se.inera.certificate.web.service;

import org.springframework.security.core.context.SecurityContextHolder;

import se.inera.certificate.web.security.Citizen;

public class CitizenService {

	public Citizen getCitizen() {
		return (Citizen) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

}
