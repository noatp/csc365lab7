-- FR4 
-- Input: reservation code ='10105'
-- check if reservation code is valid
select Room from lab7_reservations where Code = '10105'

-- if yes, delete the reservation
delete from lab7_reservations where Code = '10105'