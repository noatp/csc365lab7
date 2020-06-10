--reservations
DROP TABLE IF EXISTS lab7_reservations;
CREATE TABLE lab7_reservations(Code INT(11), Room CHAR(5), CheckIn DATE, Checkout DATE, Rate FLOAT, LastName VARCHAR(15), FirstName VARCHAR(15), Adults INT(3), Kids INT(3),PRIMARY KEY (Code));
INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10105, 'HBB', '2020-05-01', '2020-06-11', 100, 'SLEBIG', 'CONRAD', 1, 0)
INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10183, 'AOB', '2020-06-11', '2020-06-7', 150, 'GABLER', 'DOLLIE', 2, 0)
INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10574, 'FNA', '2020-06-11', '2020-06-13', 150, 'GABLER', 'DOLLIE', 2, 0)
INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (10500, 'HBB', '2020-06-13', '2020-06-15', 150, 'GABLER', 'DOLLIE', 2, 0)
INSERT INTO lab7_reservations (Code, Room, CheckIn, Checkout, Rate, LastName, FirstName, Adults, Kids) VALUES (12686, 'HBB', '2020-06-17', '2020-06-19', 150, 'GABLER', 'DOLLIE', 2, 0)
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

--- FR1
-- Input: roomcode ='HBB', today = CURDATE()
-- list room that checkIn <= today < checkout
select Room, CheckIn, Checkout
from lab7_reservations
where Room = 'HBB' and CheckIn <= CURDATE() and Checkout > CURDATE()

-- Input: checkout day = '2020-06-11', roomcode = 'HBB'
-- list room that checkIn > checkOut
select Room, CheckIn, Checkout
from lab7_reservations
where Room = 'HBB' and CheckIn > '2020-06-11'