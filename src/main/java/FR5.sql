-- FR5
-- print out Room, Roomname, month name, revenue each month
with sameMonth as (select Room, Monthname(CheckIn) as month, Month(Checkin) as Monthorder, sum(rate*(datediff(checkout, checkin))) as revenue
from lab7_reservations
where Monthname(CheckIn) = Monthname(Checkout)
group by Room, month, Monthorder
order by Room),
listDifferentMonth as (select * from lab7_reservations where Monthname(CheckIn) <> Monthname(Checkout)),
combine as(
    (select Room, Monthname(CheckIn) as month, Month(Checkin) as Monthorder, sum(rate*(datediff(last_day(checkin), checkin)+1)) as revenue
    from listDifferentMonth
    group by Room, month, Monthorder)
    union
    (select Room, Monthname(Checkout) as month, Month(Checkout) as Monthorder, sum(rate*(datediff(checkout, date_add(checkout, INTERVAL(1 - DAYOFMonth(checkout)) day))+1)) as revenue
    from listDifferentMonth
    group by Room, month, Monthorder)
    union
    (select * from sameMonth)
)
select Room, Roomname, month, sum(revenue)
from combine C, lab7_rooms R
where C.Room = R.RoomCode
group by Room, Roomname, month, Monthorder
order by Room, Roomname, Monthorder