create database StitchWorks;
use StitchWorks;


create table cus_Info(
	mobile VARCHAR(15) NOT NULL PRIMARY KEY,
    name VARCHAR(200),
    address VARCHAR(200),
    city VARCHAR(100),
    dob DATE NOT NULL,
    gender VARCHAR(200) ,
    pic_filename VARCHAR(200),
    doEnroll DATE DEFAULT (CURRENT_TIMESTAMP)
);
select * from cus_Info;


create table worker(
	wname VARCHAR(50) NOT NULL PRIMARY KEY,
    mobile VARCHAR(15) NOT NULL,
    wadd VARCHAR(200) NOT NULL,
    splz VARCHAR(300) NOT NULL,
    avail integer(2) NOT NULL
);
select * from worker;

CREATE TABLE measurements (
    orderId INT(10) AUTO_INCREMENT PRIMARY KEY,
    mobile VARCHAR(15) NOT NULL,
    dress VARCHAR(100) NOT NULL,
    dod DATE NOT NULL,
    design_filename VARCHAR(100),
    qty INT(10) NOT NULL,
    ppu INT(10) NOT NULL,
    bill INT(10) NOT NULL,
    msrmnts VARCHAR(500) NOT NULL,
    workerass VARCHAR(150) NOT NULL,
    status INT(10) NOT NULL
);
select * from measurements;

create table owner(
ownerId varchar(100),
ownerPwd varchar(100));
insert into owner values("owner6007@thapar.edu" , "Owner@1234");
select * from owner;

