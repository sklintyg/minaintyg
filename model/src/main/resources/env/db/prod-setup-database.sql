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

create table `CERTIFICATE` (
    `ID` varchar(255) not null,
    `DOCUMENT` blob not null,
    primary key (`ID`)
) ENGINE=InnoDB default CHARSET=UTF8;

create table `CERTIFICATE_META_DATA` (
    `ID` varchar(255) not null,
    `CIVIC_REGISTRATION_NUMBER` varchar(255),
    `TYPE` varchar(255) not null,
    `DELETED` TINYINT(1) not null,
    `CARE_UNIT_NAME` varchar(255) not null,
    `SIGNING_DOCTOR_NAME` varchar(255) not null,
    `SIGNED_DATE` timestamp not null,
    `VALID_FROM_DATE` timestamp,
    `VALID_TO_DATE` timestamp,
    primary key (`ID`),
    foreign key (`ID`) REFERENCES CERTIFICATE(`ID`)
) ENGINE=InnoDB default CHARSET=UTF8;

CREATE INDEX CERTIFICATE_META_DATA_INDEX ON `CERTIFICATE_META_DATA`(`CIVIC_REGISTRATION_NUMBER`);
CREATE INDEX CERTIFICATE_META_DATA_AND_TYPE_INDEX ON `CERTIFICATE_META_DATA`(`CIVIC_REGISTRATION_NUMBER`, `TYPE`);
