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

insert into CERTIFICATE (ID, DOCUMENT) values ('1', X'54686973206973206120646f63756d656e74');
insert into CERTIFICATE_META_DATA (ID, CIVIC_REGISTRATION_NUMBER, DELETED, SIGNED_DATE, TYPE, VALID_FROM_DATE, VALID_TO_DATE) values ('1', '121212-1212', 0, '2013-04-24','fk7263', '2013-04-25', '2013-05-25');
