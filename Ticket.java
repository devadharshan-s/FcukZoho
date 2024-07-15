import java.util.*;

public class Ticket {
    Character From, To;
    int PNR, noOfSeats, status;
    ArrayList<Integer> seats;

    Ticket(Character From, Character To, int noOfSeats){
        this.From = From;
        this.To = To;
        this.noOfSeats = noOfSeats;
    }
}
