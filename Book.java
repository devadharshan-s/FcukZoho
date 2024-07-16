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

    //Methods Related to Booking Ticket starts Here
    void book(Ticket ticket){

        //Enters this method if only CNF/WL Available
        HashSet<Integer> alloted = new HashSet<>();

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
                            //We need the seats to be started at 1, so i + 1
                            curStation[i] = i + 1;
                            alloted.add(i + 1);
                        }
                    }
                }
            }

            ticket.seats = new ArrayList<>(alloted);
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
            //Station within bounds
            if(station >= From && station < To){
                //For each station check empty seats are available
                int emptySeats = 0;
                int[] curStation = masterList.get(station);
                for(int i : curStation){
                    if(i == 0) emptySeats++;
                }
                //If the no of seats < required go check for WL instead of CNF
                if(emptySeats < seatsRequired){
                    cnfrm = false;
                    break;
                }
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
//            System.out.println(WL);
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
            if(ticket.status == 2) {
                WL.remove(ticket);
                System.out.println("Ticket Moved From WL");
            }
            ticket.status = 1;
            book(ticket);
            printTicket(ticket);
        }

    }
    //Methods Related to Booking Ticket ends Here
    //----------------------------------------------------------

    //Methods Related to Cancel Ticket starts here ----->
    void updateWaitingList(){
        for(Ticket t : WL){
            checkAvailabilty(t);
        }
    }
    void removeFromMasterList(Ticket ticket, int noOfTickets){
        Character From = ticket.From, To = ticket.To;
        ArrayList<Integer> arr = ticket.seats;
        HashSet<Integer> cancelled = new HashSet<>();
        //Iterate and get seats to be cancelled
        for(Character k : masterList.keySet()){
            if(k == To) break;
            if(k >= From && k < To){
                int seats = noOfTickets;
                int[] cur = masterList.get(k);
                //Iterate the seats in ticket and cancel noOfTickets from MasterList
                for(int i = 0; seats > 0 && i < arr.size(); i++){
                    --seats;
                    //In array the element is stored in (element - 1) index
                    cur[arr.get(i) - 1] = 0;
                    //Remove from the ticket too for this add in a seat to avoid duplciation.
                    cancelled.add(arr.get(i));
                }
            }
        }
        System.out.println("Cancelled Tickets: "  + cancelled);
        System.out.println("-------------------------------------------------");
        //Remove seats from ticket too.
        for(Integer i: cancelled) arr.remove(i);
        //Update new tickets after removal of tickets.
        ticket.seats = arr;
        //Update the ticket in CNF after removal of tickets.
        CNF.remove(ticket);
        CNF.add(ticket);
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
            System.out.println("Your Seats Before Cancellation: " + cancel.seats);
            System.out.println("Ticket Cancelled Successfully");
            //If ticket is CNF remove from Map(MasterList)
            removeFromMasterList(cancel,tickets);
            //If any tickets in WL matches the number of seats available move to CNF(FIFO Order)
            updateWaitingList();
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
    //Methods Related to Cancel ends here -----
    //----------------------------------------

    //Methods Related to User Interaction starts here

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
            System.out.println("-------------------------------------------------");
        } else {
            System.out.println("Status: WL");
            System.out.println("-------------------------------------------------");
        }
    }

    //Methods Related to User Interaction starts here
    //----------------------------------------------
}
