package ui;

import app.AttachmentStorage;
import app.ITTicketingSimpleApp;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import model.Ticket;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

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

        // add attachments
        Button attachBtn = new Button("Add Attachment");
        ListView<String> attachmentList = new ListView<>();
        // Store selected files temporarily (before ticket is created)
        List<File> selectedFiles = new ArrayList<>();
        attachBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Select attachment(s)");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Images & Docs", "*.png", "*.jpg", "*.jpeg", "*.pdf", "*.txt"
                    )
            );

            List<File> files = chooser.showOpenMultipleDialog(null);
            if (files != null) {
                for (File f: files) {
                    selectedFiles.add(f);
                    attachmentList.getItems().add(f.getName());
                }
            }
        });

        grid.add(attachBtn, 0, 3);
        grid.add(attachmentList, 1, 3);

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

                // Copy attachments into temp attachments folder
                for (File f: selectedFiles) {
                    try {
                        Path target = AttachmentStorage.ATTACHMENTS_DIR.resolve(
                                "ticket_" + ticket.getId() + "_" + f.getName()
                        );

                        Files.copy(
                                f.toPath(),
                                target,
                                StandardCopyOption.REPLACE_EXISTING
                        );

                        ticket.addAttachment(target);

                    } catch (IOException ex) {
                        new Alert(Alert.AlertType.ERROR,
                                "Failed to attach file: " + f.getName())
                                .showAndWait();
                    }
                }

//                app.getAllTickets().addAll(ticket);
                app.getTicketManager().addTicket(ticket);
            }
        });
    }



}
