create database Irctc;
use Irctc;

######## Create PersonalDetail

Create table PersonalDetail(name varchar(50),aadhaarNo varchar(12), age int, mobileNo varchar(10));
alter table PersonalDetail Add Primary key(aadhaarNo);

######## Create Passenger

Create table Passenger(id int auto_increment primary key, source varchar(50), destination varchar(50), dateOfJourney datetime, trainNo int,
noOfSeats int,foodType varchar(10),aadhaarNo varchar(12));
ALTER TABLE Passenger ADD FOREIGN KEY (aadhaarNo) REFERENCES PersonalDetail(aadhaarNo);

######## Create Ticket

Create table Ticket(id int auto_increment primary key,fare decimal(7,2),pnr varchar(12),
bookingDate datetime,aadhaarNo varchar(12),passengerId int, FOREIGN KEY (passengerId) REFERENCES Passenger(id));

######## Create Train
create table Train(trainNo int primary key, type varchar(50),capacity int, fare decimal(10,2));


######## Insert Train meta data

insert into Train(trainNo,type,capacity,fare) values(120101,'Express',180,500);
insert into Train(trainNo,type,capacity,fare) values(120102,'Express',180,500);
insert into Train(trainNo,type,capacity,fare) values(120103,'Express',180,500);
insert into Train(trainNo,type,capacity,fare) values(120104,'Superfast',120,1000);
insert into Train(trainNo,type,capacity,fare) values(120105,'Superfast',120,1000);
insert into Train(trainNo,type,capacity,fare) values(120106,'Superfast',120,1000);
insert into Train(trainNo,type,capacity,fare) values(120107,'Superfast',120,1000);
insert into Train(trainNo,type,capacity,fare) values(120108,'Superfast',120,1000);
insert into Train(trainNo,type,capacity,fare) values(120109,'Rajdhani',100,1500);
insert into Train(trainNo,type,capacity,fare) values(120110,'Rajdhani',100,1500);