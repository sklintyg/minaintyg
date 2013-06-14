/**
 * Copyright (C) 2013 Inera AB (http://www.inera.se)
 *
 * This file is part of Inera Certificate (http://code.google.com/p/inera-certificate).
 *
 * Inera Certificate is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Inera Certificate is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.inera.certificate.service.impl;

import java.util.List;

import iso.v21090.dt.v1.CD;
import iso.v21090.dt.v1.II;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3.wsaddressing10.AttributedURIType;

import se.inera.certificate.model.Aktivitet;
import se.inera.certificate.model.BedomtTillstand;
import se.inera.certificate.model.Certificate;
import se.inera.certificate.model.HosPersonal;
import se.inera.certificate.model.Lakarutlatande;
import se.inera.certificate.model.Patient;
import se.inera.certificate.model.Referens;
import se.inera.certificate.model.Vardenhet;
import se.inera.certificate.model.Vardgivare;
import se.inera.certificate.model.Vardkontakt;
import se.inera.certificate.service.CertificateSenderService;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.AktivitetType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Aktivitetskod;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.BedomtTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.LakarutlatandeType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.MedicinsktTillstandType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.ReferensType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Referenstyp;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.VardkontaktType;
import se.inera.ifv.insuranceprocess.healthreporting.mu7263.v3.Vardkontakttyp;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificate.v3.rivtabp20.RegisterMedicalCertificateResponderInterface;
import se.inera.ifv.insuranceprocess.healthreporting.registermedicalcertificateresponder.v3.RegisterMedicalCertificateType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.EnhetType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.HosPersonalType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.PatientType;
import se.inera.ifv.insuranceprocess.healthreporting.v2.VardgivareType;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author andreaskaltenbach
 */
@Service
public class CertificateSenderServiceImpl implements CertificateSenderService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RegisterMedicalCertificateResponderInterface registerMedicalCertificateResponder;

    private String logicalAddress = "logicaladdress";

    public void sendCertificate(Certificate certificate, String target) {
        if (certificate.getType().equalsIgnoreCase("fk7263")) {
            AttributedURIType address = new AttributedURIType();
            address.setValue(logicalAddress);
            registerMedicalCertificateResponder.registerMedicalCertificate(address , getJaxbObject(certificate));
        } else {
            throw new IllegalArgumentException("Can not send certificate of type " + certificate.getType() + " to " + target);
        }
    }

    private RegisterMedicalCertificateType getJaxbObject(Certificate certificate) {
        try {
            Lakarutlatande lakarutlatande = objectMapper.readValue(certificate.getDocument(), Lakarutlatande.class);

            RegisterMedicalCertificateType register = new RegisterMedicalCertificateType();
            register.setLakarutlatande(new LakarutlatandeType());
            register.getLakarutlatande().setLakarutlatandeId(lakarutlatande.getId());
            register.getLakarutlatande().setTypAvUtlatande("Läkarintyg enligt 3 kap, 8 § lagen (1962:381) om allmän försäkring");
            register.getLakarutlatande().setKommentar(lakarutlatande.getKommentar());
            register.getLakarutlatande().setSigneringsdatum(lakarutlatande.getSigneringsDatum());
            register.getLakarutlatande().setSkickatDatum(lakarutlatande.getSkickatDatum());
            register.getLakarutlatande().setPatient(toJaxb(lakarutlatande.getPatient()));
            register.getLakarutlatande().setSkapadAvHosPersonal(toJaxb(lakarutlatande.getSkapadAv()));
            register.getLakarutlatande().getSkapadAvHosPersonal().setEnhet(toJaxb(lakarutlatande.getVardenhet()));
            register.getLakarutlatande().setBedomtTillstand(sjukdomsforloppToJaxb(lakarutlatande.getSjukdomsforlopp()));
            register.getLakarutlatande().setMedicinsktTillstand(toJaxb(lakarutlatande.getBedomtTillstand()));
            addAktivitet(register.getLakarutlatande().getAktivitet(), lakarutlatande.getAktiviteter());
            addReferens(register.getLakarutlatande().getReferens(), lakarutlatande.getReferenser());
            addVardkontakt(register.getLakarutlatande().getVardkontakt(), lakarutlatande.getVardkontakter());
            return register;
        } catch (Exception e) {
            // TODO: Kasta annat undantag! /PW
            throw new RuntimeException(e);
        }
    }

    private void addVardkontakt(List<VardkontaktType> target, List<Vardkontakt> source) {
        if (isNull(source)) {
            return;
        }
        for (Vardkontakt vardkontakt: source) {
            target.add(toJaxb(vardkontakt));
        }
    }

    private VardkontaktType toJaxb(Vardkontakt source) {
        VardkontaktType vardkontaktType = new VardkontaktType();
        vardkontaktType.setVardkontaktstid(source.getVardkontaktstid());
        switch (source.getVardkontakttyp()) {
        case MIN_TELEFONKONTAKT_MED_PATIENTEN: vardkontaktType.setVardkontakttyp(Vardkontakttyp.MIN_TELEFONKONTAKT_MED_PATIENTEN); break;
        case MIN_UNDERSOKNING_AV_PATIENTEN: vardkontaktType.setVardkontakttyp(Vardkontakttyp.MIN_UNDERSOKNING_AV_PATIENTEN); break;
        default: throw new IllegalArgumentException("Can not convert " + source.getVardkontakttyp());
        }
        return vardkontaktType;
    }

    private void addReferens(List<ReferensType> target, List<Referens> source) {
        if (isNull(source)) {
            return;
        }
        for (Referens referens: source) {
            target.add(toJaxb(referens));
        }
    }

    private ReferensType toJaxb(Referens source) {
        ReferensType referensType = new ReferensType();
        referensType.setDatum(source.getDatum());
        switch (source.getReferenstyp()) {
        case JOURNALUPPGIFTER: referensType.setReferenstyp(Referenstyp.JOURNALUPPGIFTER); break;
        case ANNAT: referensType.setReferenstyp(Referenstyp.ANNAT); break;
        default: throw new IllegalArgumentException("Can not convert " + source.getReferenstyp());
        }
        return referensType;
    }

    private void addAktivitet(List<AktivitetType> target, List<Aktivitet> source) {
        if (isNull(source)) {
            return;
        }
        for (Aktivitet aktivitet: source) {
            target.add(toJaxb(aktivitet));
        }
        
    }

    private boolean isNull(Object o) {
        return o == null;
    }

    private AktivitetType toJaxb(Aktivitet source) {
        AktivitetType aktivitet;
        switch (source.getAktivitetskod()) {
        case ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL: aktivitet = createAktivitetType(Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL); break;
        case ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL: aktivitet = createAktivitetType(Aktivitetskod.ARBETSLIVSINRIKTAD_REHABILITERING_AR_EJ_AKTUELL); break;
        case AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA: aktivitet = createAktivitetType(Aktivitetskod.AVSTANGNING_ENLIGT_SM_L_PGA_SMITTA); break;
        case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT: aktivitet = createAktivitetType(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_AKTUELLT); break;
        case FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT: aktivitet = createAktivitetType(Aktivitetskod.FORANDRAT_RESSATT_TILL_ARBETSPLATSEN_AR_EJ_AKTUELLT); break;
        case GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL: aktivitet = createAktivitetType(Aktivitetskod.GAR_EJ_ATT_BEDOMMA_OM_ARBETSLIVSINRIKTAD_REHABILITERING_AR_AKTUELL); break;
        case KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL: aktivitet = createAktivitetType(Aktivitetskod.KONTAKT_MED_FORSAKRINGSKASSAN_AR_AKTUELL); break;
        case OVRIGT: aktivitet = createAktivitetType(Aktivitetskod.OVRIGT, source.getBeskrivning()); break;
        case PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN: aktivitet = createAktivitetType(Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_ARBETSFORMEDLINGEN); break;
        case PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN: aktivitet = createAktivitetType(Aktivitetskod.PATIENTEN_BEHOVER_FA_KONTAKT_MED_FORETAGSHALSOVARDEN); break;
        case PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD: aktivitet = createAktivitetType(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_ANNAN_ATGARD, source.getBeskrivning()); break;
        case PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN: aktivitet = createAktivitetType(Aktivitetskod.PLANERAD_ELLER_PAGAENDE_BEHANDLING_ELLER_ATGARD_INOM_SJUKVARDEN, source.getBeskrivning()); break;
        default:
            throw new IllegalArgumentException("Can not convert " + source.getAktivitetskod());
        }
        return aktivitet;
    }

    private AktivitetType createAktivitetType(Aktivitetskod kod, String beskrivning) {
        AktivitetType aktivitet = new AktivitetType();
        aktivitet.setAktivitetskod(kod);
        aktivitet.setBeskrivning(beskrivning);
        return aktivitet;
    }

    private AktivitetType createAktivitetType(Aktivitetskod kod) {
        AktivitetType aktivitet = new AktivitetType();
        aktivitet.setAktivitetskod(kod);
        return aktivitet;
    }

    private MedicinsktTillstandType toJaxb(BedomtTillstand bedomtTillstand) {
        MedicinsktTillstandType tillstand = new MedicinsktTillstandType();
        tillstand.setBeskrivning(bedomtTillstand.getBeskrivning());
        CD cd = new CD();
        cd.setCode(bedomtTillstand.getTillstandskod());
        cd.setCodeSystem("ICD-10");
        return tillstand;
    }


    private BedomtTillstandType sjukdomsforloppToJaxb(String source) {
        BedomtTillstandType tillstand = new BedomtTillstandType();
        tillstand.setBeskrivning(source);
        return tillstand;
    }


    private EnhetType toJaxb(Vardenhet source) {
        EnhetType enhet = new EnhetType();
        enhet.setEnhetsnamn(source.getNamn());
        enhet.setPostadress(source.getPostadress());
        enhet.setPostnummer(source.getPostnummer());
        enhet.setPostort(source.getPostort());
        enhet.setTelefonnummer(source.getTelefonnummer());
        enhet.setEpost(source.getEpost());

        enhet.setArbetsplatskod(arbetsplatskodToJaxb(source.getArbetsplatskod()));
        enhet.setEnhetsId(enhetsidToJaxb(source.getId()));

        enhet.setVardgivare(toJaxb(source.getVardgivare()));
        return enhet;
    }

    private VardgivareType toJaxb(Vardgivare source) {
        VardgivareType vardgivare = new VardgivareType();
        vardgivare.setVardgivarnamn(source.getNamn());
        vardgivare.setVardgivareId(vardgivareidToJaxb(source.getId()));
        return vardgivare;
    }

    private II vardgivareidToJaxb(String id) {
        return ii("1.2.752.129.2.1.4.1", id);
    }

    private II enhetsidToJaxb(String id) {
        return ii("1.2.752.129.2.1.4.1", id);
    }

    private II arbetsplatskodToJaxb(String arbetsplatskod) {
        return ii("1.2.752.29.4.71", arbetsplatskod);
    }

    private HosPersonalType toJaxb(HosPersonal skapadAv) {
        HosPersonalType personal = new HosPersonalType();
        personal.setForskrivarkod(skapadAv.getForskrivarkod());
        personal.setFullstandigtNamn(skapadAv.getNamn());
        personal.setPersonalId(hosPersonalIdToJaxb(skapadAv.getId()));
        return personal;
    }

    private II hosPersonalIdToJaxb(String id) {
        return ii("1.2.752.129.2.1.4.1", id);
    }

    private PatientType toJaxb(Patient source) {
        PatientType patient = new PatientType();
        patient.setFullstandigtNamn(extractFullstandigtNamn(source));
        patient.setPersonId(personnummerToII(source.getId()));
        return patient;
    }

    private II ii(String root, String extension) {
        II ii = new II();
        ii.setRoot(root);
        ii.setExtension(extension);
        return ii;
    }
    private II personnummerToII(String id) {
        // TODO Always sets as personnummer, inte samordningsnummer
        return ii("1.2.752.129.2.1.3.1", id);
    }

    private String extractFullstandigtNamn(Patient source) {
        if (notEmpty(source.getFullstandigtNamn())) {
            return source.getFullstandigtNamn();
        }
        return source.getFornamn() + " " + source.getEfternamn();
    }

    private boolean notEmpty(String source) {
        return source != null && source.length() > 0;
    }
}
