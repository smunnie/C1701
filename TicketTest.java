import java.util.ArrayList;
import java.util.List;

public class TicketTest {
    public void run(){
        User newUser = new User("user", "user", User.Role.user);

        TicketManager ticketManager = new TicketManager();

        ticketManager.addTicket(
            new Ticket("malware","virus affecting the computer's operation",newUser, Ticket.JobRequest.security_issues));

        System.out.println(ticketManager.getAllTickets());

        List<Ticket> tickets = ticketManager.getAllTickets();

        System.out.println(tickets.getFirst().description);
        System.out.println(tickets.getFirst().id);
        System.out.println(tickets.getFirst().title);
        System.out.println(tickets.getFirst().getRequestType());
        System.out.println(tickets.getFirst().getRequestType().getPriority());

    }
    public static void main(String[] args){
        TicketTest test = new TicketTest();
        test.run();
    }
}
