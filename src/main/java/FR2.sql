-- FR2
-- Input: checkin ='2020-05-11', checkout = '2020-06-11', roomCode = 'HBB'
-- print nothing if there is not checkin/checkout overlap
SELECT *
FROM lab7_reservations
where Room = 'HBB' and (CheckIn <= '2020-05-11' and CheckOut > '2020-05-11') or (CheckIn < '2020-06-11' and CheckIn > '2020-05-11')
-- Input: number of people = '3', roomCode = 'HBB'
-- print nothing if the requested person count exceeds the maximum capacity
-- else print a room with roomCode = 'HBB' and MaxOcc <= 3
SELECT *
from lab7_rooms
where RoomCode = 'HBB' and MaxOcc <= '3'

-- If both print nothing, then add a row to lab7_reservations for the new reservation