import java.util.*;

public class Book {
    static int PNR = 0;
    static HashMap<Character,int[]> masterList = new HashMap<>();
    //8 + 1 => (8 - 8 = 0) 0 seats available => 9
    static int totalStations = 5;
    static int totalSeats = 8;
    static int totalWL = 2;
    //CNF Tickets
    static Set<Ticket> CNF = new HashSet<>();
    //WL Queue
    static Deque<Ticket> WL = new LinkedList<>();

    static Scanner sc = new Scanner(System.in);

    void initMasterList(){
        for(char i = 'A'; i <= 'E'; i++)
            masterList.put(i, new int[totalSeats]);
    }

    HashMap<Character,int[]> getMasterList(){
        return masterList;
    }
    int getTotalStations(){
        return totalStations;
    }
    int getTotalSeats(){
        return totalSeats;
    }
    void printTicket(Ticket ticket){

        if(ticket.status == -1){
            System.out.println("Ticket Not Available");
            return;
        }
        System.out.println("Ticket Booked");
        System.out.println("PNR: " + ticket.PNR);
        System.out.println("From: " + ticket.From + "\n" + "To: " + ticket.To);
        for(Integer seat : ticket.seats) System.out.print(seat + " ");
        System.out.println();
        int status = ticket.status;
        if(status == 1){
            System.out.println("Status: CNF");
        } else {
            System.out.println("Status: WL");
        }
    }
    void book(Ticket ticket){

        //Enters this method if only CNF/WL Available
        ArrayList<Integer> st = new ArrayList<>();
        int status = ticket.status;
        if(status == 1) {
            Character From = ticket.From, To = ticket.To;

            for (Character station : masterList.keySet()) {
                if (station == To) break;
                if (station >= From && station < To) {
                    int seatsRequired = ticket.noOfSeats;
                    int[] curStation = masterList.get(station);
                    //Iterate the array fill empty sets
                    for(int i = 0; seatsRequired > 0 && i < totalSeats; i++){
                        if(curStation[i] == 0){
                            --seatsRequired;
                            curStation[i] = i + 1;
                            st.add(i + 1);
                        }
                    }
                }
            }
            ticket.seats = st;
            ticket.PNR = ++PNR;
            CNF.add(ticket);
        } else{
            ticket.PNR = ++PNR;
            WL.add(ticket);
        }

    }

    void checkAvailabilty(Ticket ticket){
        int seatsRequired = ticket.noOfSeats;
        Character From = ticket.From, To = ticket.To;
        boolean cnfrm = true;

        for(Character station : masterList.keySet()){
            if(station == To) break;
            //We need to Check for every valid station
            int emptySeats = 0;
            if(station >= From && station < To){
                int[] curStation = masterList.get(station);
                for(int i : curStation){
                    if(i == 0) emptySeats++;
                }
            }
            //If the no of seats < required go check for WL instead of CNF
            if(emptySeats < seatsRequired){
                cnfrm = false;
                break;
            }
        }
        //Check WL and if ticket is already in WL(while checking) dont add in WL
        if(ticket.status != 2 && !cnfrm){
            //WL Max 2
            if(seatsRequired > 2) {
                ticket.status = -1;
                printTicket(ticket);
                return;
            }
            System.out.println(WL);
            int wlSeats = 0;
            //Check if WL Queue if Full
            for(Ticket t : WL){
                wlSeats += t.noOfSeats;
                if(wlSeats >= totalWL){
                    ticket.status = -1;
                    printTicket(ticket);
                    return;
                }
            }
            ArrayList<Integer> st = new ArrayList<>();
            for(int i = 0; i < seatsRequired; i++) st.add(i + 1);
            ticket.seats = st;
            //If not book ticket in WL with '2' status
            ticket.status = 2;
            book(ticket);
            printTicket(ticket);
        }
        //If CNF available book
        else if(cnfrm) {
            //If ticket is already in WL remove from WL
            if(ticket.status == 2)
                WL.remove(ticket);
            ticket.status = 1;
            book(ticket);
            printTicket(ticket);
        }

    }
    //Methods Related to Booking Ticket ends Here
    //----------------------------------------------------------

    void updateWaitingList(){

        for(Ticket t : WL){
            checkAvailabilty(t);
        }
    }
    void removeFromMasterList(Ticket ticket, int noOfTickets){
        Character From = ticket.From, To = ticket.To;
        ArrayList<Integer> arr = ticket.seats;
        //Iterate and get seats to be cancelled
        for(Character k : masterList.keySet()){
            if(k == To) break;
            if(k >= From && k < To){
                int[] cur = masterList.get(k);
                //Iterate the seats in ticket and cancel noOfTickets from MasterList
                for(int i = 0; noOfTickets > 0 && i < arr.size(); i++){
                    --noOfTickets;
                    cur[arr.get(i)] = 0;
                    //Remove from the ticket too.
                    arr.remove(i);
                }
            }
        }
        ticket.seats = arr;
        CNF.remove(ticket);
        CNF.add(ticket);
        updateWaitingList();
    }
    void identifyAndCancel(int cancelPNR, int tickets){
        Ticket cancel = null;
        //Search for Ticket in CNF
        for(Ticket ticket : CNF){
            if(ticket.PNR == cancelPNR) {
                cancel = ticket;
                break;
            }
        }
        //If the ticket is confirmed, Cancel from CNF and MasterList
        if(cancel != null){
            removeFromMasterList(cancel,tickets);
            System.out.println(cancel.seats);
            System.out.println("Ticket Cancelled Successfully");
            return;
        }
        //Ticket is in WL
        if(cancel == null){
            for(Ticket ticket : WL){
                if(ticket.PNR == cancelPNR) {
                    cancel = ticket;
                    break;
                }
            }
        }
        //If the ticket in WL
        ArrayList<Integer> seats = cancel.seats;
        for(int i = 0; tickets > 0 && i < seats.size(); i++){
            --tickets;
            //Remove required seats
            seats.remove(i);
        }
        //Reassign the updated seats to ticket
        cancel.seats = seats;
        //Remove and update Ticket in Queue
        WL.remove(seats);
        WL.addFirst(cancel);
        System.out.println(cancel.seats);
        System.out.println("Ticket Cancelled Successfully(WL Tickets)");

    }
    //Ticket Cancel Details
    void getCancelDetails(){
        //Get PNR to identify Ticket to be cancelled
        System.out.println("Enter PNR: ");
        int cancelPNR = sc.nextInt();
        //Get No of tickets to be cancelled
        System.out.println("Enter Number of tickets to be cancelled: ");
        int noOfCancelTickets = sc.nextInt();
        identifyAndCancel(cancelPNR,noOfCancelTickets);
    }
    void getBookDetails(){
        System.out.println("Enter Boarding Station:");
        Character From = sc.next().charAt(0);
        System.out.println("Enter Dropping Station:");
        Character To = sc.next().charAt(0);
        System.out.println("Enter Number of Passengers");
        int noOfSeats = sc.nextInt();
        Ticket check = new Ticket(From,To,noOfSeats);
        checkAvailabilty(check);
    }
}
