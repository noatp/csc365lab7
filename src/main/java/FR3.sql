-- FR3
-- Input: reservation code = '10105', checkin ='2020-05-11', checkout = '2020-06-11'
-- Print nothing if there is no conflict
SELECT * 
from lab7_reservations R1, (select Room from lab7_reservations where Code = '10105') as R2
where R1.Room = R2.Room and R1.Code <> '10105' and (CheckIn <= '2020-05-11' and CheckOut > '2020-05-11') or (CheckIn < '2020-06-11' and CheckIn > '2020-05-11')

-- If there is no conflict, print out the room code
select Room from lab7_reservations where Code = '10105'
-- Use room code to check if if the requested person count exceeds the maximum capacity, print nothing if is none
SELECT *
from lab7_rooms
where RoomCode = 'HBB' and MaxOcc >= '3'
-- If there is no conflict, then allow to update
UPDATE lab7_reservations
SET FirstName = 'Phuong', LastName = 'Nguyen', CheckIn = '2020-09-01', CheckOut = '2020-09-04', Adults = 1, Kids = 1
WHERE Code = '10105';