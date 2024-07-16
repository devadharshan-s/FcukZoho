This is a LLD Question asked in Zoho L3 Round.

Design a Train Ticket Booking System with Stations(A->B->C->D->E) and in this problem a train has only one coach and 8 seats

-> PNR must be unique for every ticket

-> One Ticket can have multiple seats

-> Ticket Cancellation Must allow full and partial cancellation of seats

-> Implement Waiting List(WL) of 2 Seats

-> If the number of Requested seats is higher than the available seats, Do not book ticket(Book ticket when the requested number of seats are available).

-> Print Chart (ROW, COL Format) (ROW -> (A-E) number of stations COL -> (1 - 8) seats)

-> If someone cancels their ticket search for suitable ticket in WL to move to Confirmed Ticket(FIFO Order)
