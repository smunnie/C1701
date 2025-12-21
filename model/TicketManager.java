package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

public class TicketManager {
    private final ObservableList<Ticket> allTickets = FXCollections.observableArrayList();
//    private final List<Ticket> allTickets = new ArrayList<>();

    public TicketManager(){

        // adding sample ticket 1
        User newUser = new User("user", "user", User.Role.user);
        Ticket t = new Ticket(
                "Install Visual code",
                "Failed login attempt detected",
                newUser,
                Ticket.JobRequest.software_app_installation);
//        set status to resolve
        t.setStatus(Ticket.Status.resolved);
        t.setCreatedDate(LocalDateTime.parse("2025-11-07T10:45"));
        t.setResolution_note("new credentials created and login is successful");
        Path source = Paths.get("attachments/network.png").toAbsolutePath();

        t.addAttachment(source);
        allTickets.add(t);

        // adding sample ticket 2
        allTickets.add(new Ticket(
                "malware",
                "virus affecting the computer's operation",
                newUser,
                Ticket.JobRequest.security_issues
        ));
    }

    public void addTicket(Ticket t) {
        System.out.println("Creating new Ticket " + t.title);
        allTickets.add(t);
    }

    public ObservableList<Ticket> getAllTickets() {
        return allTickets;
    }

    public void removeTicket(Ticket t) {
        System.out.println("Deleting Ticket " + t.title);
        allTickets.remove(t);
    }
}

