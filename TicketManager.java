import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class TicketManager {
    private final ObservableList<Ticket> allTickets = FXCollections.observableArrayList();
//    private final List<Ticket> allTickets = new ArrayList<>();

    public TicketManager(){
//        User admin = new User("admin", "admin", User.Role.admin);
        User sam = new User("sam", "sam12", User.Role.user);
        User newUser = new User("user", "user", User.Role.user);
        Ticket t = new Ticket(
                "Install Visual code",
                "Failed login attempt detected",
                sam,
                Ticket.JobRequest.software_app_installation);
//        set status to resolve
        t.setStatus(Ticket.Status.resolved);

        t.setResolution_note("new credentials created and login is successful");

        allTickets.add(t);
        allTickets.add(new Ticket(
                "malware",
                "virus affecting the computer's operation",
                newUser,
                Ticket.JobRequest.security_issues));
    }

    public void addTicket(Ticket t) {
        allTickets.add(t);
    }

    public ObservableList<Ticket> getAllTickets() {
        return allTickets;
    }

    public void removeTicket(Ticket ticket) {
        allTickets.remove(ticket);
    }
}

