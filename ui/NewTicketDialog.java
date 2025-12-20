package ui;

import app.ITTicketingSimpleApp;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Ticket;
import model.User;

public class NewTicketDialog {
    private final ITTicketingSimpleApp app;

    public NewTicketDialog(ITTicketingSimpleApp app) {
        this.app = app;
    }

    public void show() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("New Ticket");

        GridPane grid = new GridPane();

        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();
        grid.add(titleLabel, 0, 0);
        grid.add(titleField, 1, 0);

        Label descLabel = new Label("Description:");
        TextArea descArea = new TextArea();
        descArea.setPrefRowCount(3);
        grid.add(descLabel, 0, 1);
        grid.add(descArea, 1, 1);

        Label requestLabel = new Label("Request Type:");
        ComboBox<String> requestBox = new ComboBox<>();
        requestBox.getItems().addAll(
                "Security issues",
                "New computer configuration",
                "Software/app installation",
                "Network issue"
        );
        requestBox.setPromptText("Select request");
        grid.add(requestLabel, 0, 2);
        grid.add(requestBox, 1, 2);

        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result ->{
            if (result == ButtonType.OK) {
                String title = titleField.getText();
                String desc = descArea.getText();
                String reqStr = requestBox.getValue();

//            basic validation
                if (title == null || title.isBlank()
                        || desc == null || desc.isBlank()
                        || reqStr == null) {

                    new Alert(Alert.AlertType.WARNING,
                            "Please fill in the title, description, and request type.")
                            .showAndWait();
                    return;
                }
                Ticket.JobRequest requestType;
                switch(reqStr){
                    case "Security issues" ->
                            requestType = Ticket.JobRequest.security_issues;
                    case "New computer configuration" ->
                            requestType = Ticket.JobRequest.new_computer_configuration;
                    case "Network issue" ->
                            requestType = Ticket.JobRequest.network_issue;
                    case "Software/app installation" ->
                            requestType = Ticket.JobRequest.software_app_installation;
                    default -> {
                        new Alert(Alert.AlertType.ERROR, "Unknown request type selected.")
                                .showAndWait();
                        return;
                    }
                }

//                creator = currently logged-in user
                User loggedinUser = app.getLoggedIn();

//                create ticket
//                System.out.println("Creating new Ticket");
                Ticket ticket = new Ticket(title, desc,loggedinUser, requestType);

//                app.getAllTickets().addAll(ticket);
                app.getTicketManager().addTicket(ticket);
            }
        });
    }



}
