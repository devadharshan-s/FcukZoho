import java.util.Scanner;

public class Main {
    static Train train = new Train();
    static Chart chart = new Chart();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int ch = 0;
        train.initMasterList();
        while(ch != 4){
            System.out.println("-------------------------------------------------");
            System.out.println("1.Book Ticket \n2.Cancel Ticket \n3.Print Chart\n4.Exit ");
            System.out.println("Enter Choice");
            ch = sc.nextInt();
            switch (ch){
                case 1:
                    train.getBookDetails();
                    break;

                case 2:
                    train.getCancelDetails();
                    break;
                case 3:
                    chart.printChart();
                    break;
                case 4:
                    System.out.println("Exited Sucessfully");
                    break;
                default:
                    System.out.println("Select Correct Choice");
                    break;
            }
        }
    }
}
