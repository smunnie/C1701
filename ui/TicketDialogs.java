package ui;
import app.ITTicketingSimpleApp;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Window;
import model.Ticket;
import model.User;

public class TicketDialogs {

    private final ITTicketingSimpleApp app;

    public TicketDialogs(ITTicketingSimpleApp app) {
        this.app = app;
    }

    public void deleteWarningDialog(Ticket ticket, TableView<Ticket> table) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete Ticket");
        alert.setContentText("Are you sure you want to delete this ticket ?");

        ButtonType okBtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okBtn);

        alert.showAndWait().ifPresent(result ->{
            if (result == okBtn){
//                check if user is an admin
                String role = app.getLoggedIn().getRole().name();
                if(role.equals("admin")){
//                    delete ticket
                    app.getTicketManager().removeTicket(ticket);
                }
                table.refresh();
            }
        });
    }

    public void resolveDialog(Ticket ticket, TableView<Ticket> table) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Resolve Ticket");

        Label lbl = new Label("Enter resolution notes:");
        TextArea area = new TextArea();
        area.setPrefRowCount(2);

        GridPane grid1 = new GridPane();
        grid1.setHgap(10);
        grid1.setVgap(10);
        grid1.setPadding(new Insets(10));

        grid1.add(lbl, 0, 0);
        grid1.add(area, 0, 1);

        dialog.getDialogPane().setContent(grid1);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result ->{
            if (result == ButtonType.OK){
                String notes = area.getText();
                ticket.setResolution_note(notes);
        }
            table.refresh();
        });
    }

    public void priorityDialog(Ticket ticket, TableView<Ticket> table) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Priority");
        Label lbl = new Label("New priority : ");
        ComboBox<Integer> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll(1, 2, 3, 4);
        priorityBox.setPromptText("Select priority");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(lbl, 0, 0);
        grid.add(priorityBox, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait();
    }

    public void newTicketDialog() {
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
    public void decreasePriDialog(Ticket ticket, TableView<Ticket> table) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Decrease Priority");

        Label prioLabel = new Label("Priority: ");
        Label currentValue = new Label(String.valueOf(ticket.getPriority()));

        Label reqLabel = new Label("Request type: ");
        Label reqValue = new Label(ticket.getRequestType().name());

        Button minusBtn = new Button("-");
        minusBtn.setStyle("-fx-font-size: 18px; -fx-min-width: 30px;");

        minusBtn.setOnAction(e -> {
            int p = Integer.parseInt(currentValue.getText());
            if (p < 4) {
                p++;
                currentValue.setText(String.valueOf(p));
                Ticket.JobRequest newReq = Ticket.JobRequest.fromPriority(p);
                if (newReq != null){
                    reqValue.setText(formatEnumName(newReq.name()));
                }

            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPrefWidth(400);

        grid.add(prioLabel, 0, 0);
        grid.add(currentValue, 1, 0);
        grid.add(minusBtn, 2, 0);

        grid.add(reqLabel, 0, 1);    // column 0, row 1
        grid.add(reqValue, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                int newPriority = Integer.parseInt(currentValue.getText());
                Ticket.JobRequest newJobrequest = Ticket.JobRequest.fromPriority(newPriority);
                ticket.setPriority(newPriority);
                ticket.setRequestType(newJobrequest);
            }
            table.refresh();
        } );
    }

    public String formatEnumName(String raw) {
        // replace underscores with spaces
        String cleaned = raw.replace("_", " ");

        // capitalize first letter only
        return cleaned.substring(0, 1).toUpperCase() + cleaned.substring(1).toLowerCase();
    }
}