package ui;
import app.ITTicketingSimpleApp;
import javafx.scene.control.*;
import model.Ticket;
import model.User;


public class DeleteTicketDialog {

    private final ITTicketingSimpleApp app;

    public DeleteTicketDialog(ITTicketingSimpleApp app) {
        this.app = app;
    }

    public void show(Ticket ticket, TableView<Ticket> table) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete Ticket");
        alert.setContentText("Are you sure you want to delete this ticket ?");

        ButtonType okBtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okBtn);

        alert.showAndWait().ifPresent(result -> {
            if (result == okBtn) {
                //                check if user is an admin
                String role = app.getLoggedIn().getRole().name();
                if (role.equals("admin")) {
                    //                    delete ticket
                    app.getTicketManager().removeTicket(ticket);
                } else if (role.equals("user")) {
                    //                    check if ticket belongs to user before delete
                    User user = ticket.getCreatedBy();
                    if (user.getUsername().equals(app.getLoggedIn().getUsername())) {
                        app.getTicketManager().removeTicket(ticket);
                    } else {
                        new Alert(Alert.AlertType.ERROR, "Unable to delete other user's ticket.") // incase of multiple users
                                .showAndWait();
                    }

                }
                table.refresh();
            }
        });
    }


}
