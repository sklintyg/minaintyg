create table CERTIFICATE_META_DATA (
	id varchar(255) not null, 
	careUnitName varchar(255), 
	certificate varchar(255),
	civicRegistrationNumber varchar(255), 
	deleted TINYINT(1) not null,
    signedDate timestamp,
    signingDoctorName varchar(255),
    type varchar(255),
    validFromDate timestamp,
    validToDate timestamp,
    primary key (id)
);
insert into CERTIFICATE_META_DATA (id, civicRegistrationNumber, deleted, type) values ('1', '121212-1212', 0, 'INTYG');

