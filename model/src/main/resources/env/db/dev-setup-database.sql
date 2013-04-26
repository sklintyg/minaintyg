--
-- Copyright (C) 2013 Inera AB (http://www.inera.se)
--
-- This file is part of Inera Certificate (http://code.google.com/p/inera-certificate).
--
-- Inera Certificate is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- Inera Certificate is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with this program.  If not, see <http://www.gnu.org/licenses/>.
--

create table CERTIFICATE (
    ID varchar(255) not null,
    DOCUMENT blob,
    primary key (ID)
);
create table CERTIFICATE_META_DATA (
    ID varchar(255) not null,
    CARE_UNIT_NAME varchar(255),
    CIVIC_REGISTRATION_NUMBER varchar(255),
    DELETED TINYINT(1) not null,
    SIGNED_DATE timestamp,
    SIGNING_DOCTOR_NAME varchar(255),
    TYPE varchar(255),
    VALID_FROM_DATE timestamp,
    VALID_TO_DATE timestamp,
    primary key (ID)
);
alter table CERTIFICATE_META_DATA
    add constraint SAME_ID
    foreign key (ID)
    references CERTIFICATE;

insert into CERTIFICATE (ID, DOCUMENT) values ('1', STRINGTOUTF8('<RegisterMedicalCertificate xmlns="urn:riv:insuranceprocess:healthreporting:RegisterMedicalCertificateResponder:3"><lakarutlatande></lakarutlatande></RegisterMedicalCertificate>'));

insert into CERTIFICATE_META_DATA (ID, CIVIC_REGISTRATION_NUMBER, DELETED, SIGNED_DATE, TYPE, VALID_FROM_DATE, VALID_TO_DATE) values ('1', '121212-1212', 0, '2013-04-24','fk7263', '2013-04-25', '2013-05-25');
