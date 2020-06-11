-- FR5
-- Input Room = 'HBB'
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
), totalRevenue as (
    select Room, Roomname, month, Monthorder, sum(revenue) as total
    from combine C, lab7_rooms R
    where C.Room = R.RoomCode and C.Room = 'HBB'
    group by Room, Roomname, month, Monthorder
    order by Room, Roomname, Monthorder)

select Roomname, (select total from totalRevenue where Monthorder = 1 and Room = 'HBB') as Jan,
    (select total from totalRevenue where Monthorder = 2 and Room = 'HBB') as Feb,
    (select total from totalRevenue where Monthorder = 3 and Room = 'HBB') as Mar,
    (select total from totalRevenue where Monthorder = 4 and Room = 'HBB') as Apr,
    (select total from totalRevenue where Monthorder = 5 and Room = 'HBB') as May,
    (select total from totalRevenue where Monthorder = 6 and Room = 'HBB') as Jun,
    (select total from totalRevenue where Monthorder = 7 and Room = 'HBB') as Jul,
    (select total from totalRevenue where Monthorder = 8 and Room = 'HBB') as Aug,
    (select total from totalRevenue where Monthorder = 9 and Room = 'HBB') as Sep,
    (select total from totalRevenue where Monthorder = 10 and Room = 'HBB') as Oct,
    (select total from totalRevenue where Monthorder = 11 and Room = 'HBB') as Nov,
    (select total from totalRevenue where Monthorder = 12 and Room = 'HBB') as December
from totalRevenue where Room = 'HBB'
group by Roomname