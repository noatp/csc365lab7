--reservations
DROP TABLE IF EXISTS lab7_reservations;
CREATE TABLE lab7_reservations(Code INT(11), Room CHAR(5), CheckIn DATE, Checkout DATE, Rate FLOAT, LastName VARCHAR(15), FirstName VARCHAR(15), Adults INT(3), Kids INT(3),PRIMARY KEY (Code));
INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10105, 'HBB', '2020-05-01', '2020-06-11', 100, 'SLEBIG', 'CONRAD', 1, 0)
INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10183, 'AOB', '2020-06-11', '2020-06-13', 150, 'GABLER', 'DOLLIE', 2, 0)
INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10574, 'FNA', '2020-06-11', '2020-06-13', 150, 'GABLER', 'DOLLIE', 2, 0)
--rooms
DROP TABLE IF EXISTS lab7_rooms
CREATE TABLE lab7_rooms(RoomCode CHAR(5), RoomName VARCHAR(30), Beds INT(3), BedType VARCHAR(8), MaxOcc INT(3), BasePrice FLOAT, Decor VARCHAR(20));
INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('AOB', 'Abscond or bolster', 2, 'Queen', 4, 175, 'traditional')
INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('CAS', 'Convoke and sanguine', 2, 'King', 4, 175, 'traditional')
INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('FNA', 'Frugal not apropos', 2, 'King', 4, 250, 'traditional')	
INSERT INTO lab7_rooms (RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor) VALUES ('HBB', 'Harbinger but bequest', 1, 'Queen', 2, 100, 'modern')
select * from lab7_rooms
select * from lab7_reservations
--FR1

with list as (
    SELECT ROOM, MAX(CheckOut) as mostRecentCheckOut, MAX(CheckIn) as mostRecentCheckIn
	FROM lab7_reservations rs GROUP BY ROOM
), FR1_Today as (
    select distinct ROOMCODE, "Today" as nextAvailableCheckIn, "None" as nextReservation
    from list, lab7_rooms
    where (mostRecentCheckOut <= CURDATE()) or not exists (
        select ROOM 
        from list
        where ROOMCODE <> ROOM
    )
), FR1_CheckIn as (
    select distinct ROOMCODE, mostRecentCheckOut as nextAvailableCheckIn, "None" as NextReservation
    from list, lab7_rooms
    where mostRecentCheckOut > CURDATE() and roomcode = room
), FR1_FutureReservation as (
    select distinct ROOMCODE, "Today" as nextAvailableCheckIn, mostRecentCheckIn as NextReservation
    from list, lab7_rooms
    where mostRecentCheckIn > CURDATE() and roomcode = room
)

select RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor, nextAvailableCheckIn, NextReservation
from 
((select F0.RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor, nextAvailableCheckIn, NextReservation
from lab7_rooms F0, FR1_Today F1
where F0.RoomCode = F1.RoomCode ) 
union
(select F0.RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor, nextAvailableCheckIn, NextReservation
from lab7_rooms F0, FR1_CheckIn F2
where F0.RoomCode = F2.RoomCode)
union
(select F0.RoomCode, RoomName, Beds, BedType, MaxOcc, BasePrice, Decor, nextAvailableCheckIn, NextReservation
from lab7_rooms F0,  FR1_FutureReservation F3
where F0.RoomCode = F3.RoomCode)) as A
order by RoomCode
