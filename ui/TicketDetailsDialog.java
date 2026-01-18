package ui;

import app.ITTicketingSimpleApp;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Ticket;

import java.time.format.DateTimeFormatter;

public class TicketDetailsDialog {

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void show (Ticket ticket, ITTicketingSimpleApp app) {
        Stage stage = new Stage();
        stage.setTitle("Ticket Details- #" + ticket.getId() + " - " + ticket.getTitle());
        stage.initModality(Modality.APPLICATION_MODAL);

        //Main container with padding
        VBox mainContainer = new VBox(20);
        mainContainer.setPadding(new Insets(20, 20, 20, 20));
        mainContainer.setStyle("-fx-background-color: #f8f9fa;");
        mainContainer.setAlignment(Pos.CENTER); // Extras
        mainContainer.setSpacing(20); // Extras

        //Header section with ticket ID and title
        HBox headerBox = createHeaderSection(ticket);

        // Information sections
        VBox infoSection = new VBox(10);
        infoSection.setAlignment(Pos.CENTER); // an addition

        // Basic Info Section
        GridPane basicInfoSection = createBasicInfoSection(ticket);

        mainContainer.getChildren().addAll(headerBox, infoSection, basicInfoSection);

        Scene scene = new Scene(mainContainer, 450, 400);
        stage.setScene(scene);
        stage.showAndWait();
    }
    private static HBox createHeaderSection(Ticket ticket) {
        Label title = new Label("Ticket Title");
        title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
        title.setAlignment(Pos.CENTER);

        HBox box = new HBox(title);
        box.setAlignment(Pos.CENTER);
        box.setSpacing(20);
        return box;
    }
    private static GridPane createBasicInfoSection(Ticket ticket) {
        GridPane InfoSection = new GridPane();
        InfoSection.setAlignment(Pos.CENTER);
        InfoSection.setHgap(10);
        InfoSection.setVgap(10);
        InfoSection.setPadding(new Insets(20, 20, 20, 20));

        int row = 0;

        InfoSection.add(new Label("Ticket Id: "), 0, row);
        InfoSection.add(new Label(String.valueOf(ticket.getId())), 1, row++);

        InfoSection.add(new Label("Title: "), 0, row);
        InfoSection.add(new Label(ticket.getTitle()), 1, row++);

        InfoSection.add(new Label("Description: "), 0, row);
        InfoSection.add(new Label(ticket.getdescription()), 1, row++);

        InfoSection.add(new Label("Request Type: "), 0, row);
        InfoSection.add(new Label(ticket.getRequestType().toString()), 1, row++);

        InfoSection.add(new Label("Priority:"), 0, row);
        InfoSection.add(new Label(String.valueOf(ticket.getPriority())), 1, row++);

        InfoSection.add(new Label("Status:"), 0, row);
        InfoSection.add(new Label(ticket.getStatus().toString()), 1, row++);

        InfoSection.add(new Label("Created Date:"), 0, row);
        InfoSection.add(new Label(ticket.getCreatedDate().format(dateFormatter)), 1,row++);

        InfoSection.add(new Label("Created By:"), 0, row);
        InfoSection.add(new Label(ticket.getCreatedBy().getUsername()), 1,row++);

        return InfoSection;
    }
}


