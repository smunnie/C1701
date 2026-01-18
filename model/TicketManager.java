package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

public class TicketManager {
    private final ObservableList<Ticket> allTickets = FXCollections.observableArrayList();

    public TicketManager() {

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
        Path path = Paths.get("attachments/network.png");//
        Path img1 = path.toAbsolutePath(); // first instance of same image for testing

        t.addAttachment(img1);
        Path path2 = Paths.get("attachments/failed_login.jpeg");
        Path img2 = path2.toAbsolutePath(); // second instance of same image for testing
        t.addAttachment(img2); //
        allTickets.add(t);

        // adding sample ticket 2
        allTickets.add(new Ticket(
                "malware",
                "virus affecting the computer's operation",
                newUser,
                Ticket.JobRequest.security_issues
        ));

        // add ticket from anita
        User anita = new User("anita", "ann123", User.Role.user);
        //add sample ticket 2
        allTickets.add(new Ticket(
                "malware",
                "virus affecting the computer's operation",
                anita,
                Ticket.JobRequest.software_app_installation
        ));
        Ticket t1 = new Ticket(
                "connection issues",
                " cannot connect ot my wifi on my laptop",
                anita,
                Ticket.JobRequest.network_issue);
//      set status to resolve
        t1.setStatus(Ticket.Status.cancelled);
        t1.setCreatedDate(LocalDateTime.parse("2026-01-15T10:45"));
        t1.setResolution_note("resolved offline");
        Path path3 = Paths.get("attachments/Connection_issue.jpg");//
        Path img3 = path3.toAbsolutePath(); // first instance of same image for testing
        t1.addAttachment(img3);
        allTickets.add(t1);


        // add ticket from sam
        User sam = new User("sam", "sm678", model.User.Role.user);
        //add sample ticket 2
        allTickets.add(new Ticket(
                "malware",
                "virus affecting the computer's operation",
                sam,
                Ticket.JobRequest.security_issues
        ));
        Ticket t2 = new Ticket(
                "connection issues",
                " cannot connect ot my wifi on my laptop",
                sam,
                Ticket.JobRequest.network_issue);
//      set status to resolve
        t2.setStatus(Ticket.Status.cancelled);
        t2.setCreatedDate(LocalDateTime.parse("2026-01-15T10:45"));
        t2.setResolution_note("resolved offline");
        t2.addAttachment(img3);
        allTickets.add(t2);

        // add ticket from edwin
        User edwin = new User("edwin", "ed456", model.User.Role.user);
        //add sample ticket 2
        allTickets.add(new Ticket(
                "malware",
                "virus affecting the computer's operation",
                edwin,
                Ticket.JobRequest.security_issues
        ));
        Ticket t3 = new Ticket(
                "connection issues",
                " cannot connect ot my wifi on my laptop",
                edwin,
                Ticket.JobRequest.network_issue);
        //update some ticket metadata
        t3.setStatus(Ticket.Status.resolved);
        t3.setCreatedDate(LocalDateTime.parse("2026-01-15T10:45"));
        t3.setResolution_note("connection restore");
        t3.addAttachment(img3);
        allTickets.add(t3);

        // adding tickets from muni
        User muni = new User("muni", "mn419", model.User.Role.user);
        allTickets.add(new Ticket(
                "New Computer Configuration Program",
                "Request to develop a program for configuring new computers with required hardware and software settings.",
                muni,
                Ticket.JobRequest.new_computer_configuration));

        Ticket tx = new Ticket(
                "Computer Configuration Tool Setup",
                "Request to create a tool for setting up and configuring new computers.",
                muni,
                Ticket.JobRequest.new_computer_configuration);
        tx.setStatus(Ticket.Status.resolved);
        allTickets.add(tx);

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