import javafx.beans.property.SimpleStringProperty;
import javafx. collections.FXCollections;
import javafx.collections.ObservableList;
import javafx. collections.transformation.FilteredList;
import javafx. collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;


import java.time.LocalDate;

public class AdminView {

    private final ITTicketingSimpleApp app;
    private FilteredList<Ticket> filteredTickets;

    public AdminView(ITTicketingSimpleApp app) {
        this.app = app;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label header = new Label("Admin Dashboard - " + app.getLoggedIn().getUsername());
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        TableView<Ticket> table = new TableView<>();
        setupAdminTable(table);

        // Get Tickets and Setup filtering
        ObservableList<Ticket> masterList = app.getTicketManager().getAllTickets();
        FilteredList<Ticket> filteredTickets = new FilteredList<>(masterList, t -> true);

        SortedList<Ticket> sorted = new SortedList<>(filteredTickets);

        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);

        //Buttons
        Button resolveBtn = new Button("Resolve");
        Button deleteBtn = new Button("Delete");
        Button changePriorityBtn = new Button("Change Priority");
        Button newTicketBtn = new Button("Raise Ticket");
        Button logoutBtn = new Button("Logout");

//        HBox buttons = new HBox(10, resolveBtn, deleteBtn, changePriorityBtn, logoutBtn);
        HBox buttons = new HBox(10, resolveBtn, deleteBtn, changePriorityBtn,newTicketBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setPadding(new Insets(10, 0, 10, 0));

        logoutBtn.setOnAction(e -> app.logout());

        deleteBtn.setOnAction(e -> {
            Ticket t = table.getSelectionModel().getSelectedItem();
            if (t == null) return;
            deleteWarningDialog(t);
        });

        resolveBtn.setOnAction(e -> {
            Ticket t = table.getSelectionModel().getSelectedItem();
            if (t == null) return;
            resolveDialog(t);
        });

        changePriorityBtn.setOnAction(e -> {
            Ticket t = table.getSelectionModel().getSelectedItem();
            if (t == null) return;
            priorityDialog(t);
        });

        newTicketBtn.setOnAction(e -> {
            newTicketDialog();
        });


        //Filter controls
        Label filterLabel = new Label("Filter:");
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Any", "Open", "Resolved", "cancelled");
        statusBox.setValue("Any");

        // status listener
        statusBox.valueProperty().addListener(
                (observable, oldStatus, newStatus) -> {
                    System.out.println(filteredTickets + newStatus.toLowerCase());
                    if (newStatus.equals("Any")){
                        filteredTickets.setPredicate(ticket -> true);
                    } else {
                        filteredTickets.setPredicate(Ticket ->
                                Ticket.getStatus().name().equalsIgnoreCase(newStatus));
                    }
                });


        DatePicker fromDatePicker = new DatePicker();
        fromDatePicker.setPromptText("From date");

        DatePicker toDatePicker = new DatePicker();
        toDatePicker.setPromptText("To date");

        TextField activityField = new TextField();
        activityField.setPromptText("Activity type");

        Button applyFilterBtn = new Button("Apply");
        //Button clearFilterBtn = new Button("clear")

        //setup filter button actions
        HBox filterBar = new HBox(10, filterLabel, statusBox, fromDatePicker,
                toDatePicker, activityField, applyFilterBtn);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(5, 0, 5, 0));

        VBox topArea = new VBox(header, filterBar, buttons);
        topArea.setSpacing(5);

        root.setTop(topArea);
        root.setCenter(table);
        root.setBottom(logoutBtn);

        BorderPane.setMargin(header, new Insets(0, 0, 5, 0));

        return new Scene(root, 1000, 550);
    }

    private void setupAdminTable(TableView<Ticket> table) {
        TableColumn<Ticket, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Ticket, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Ticket, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Ticket, Integer> prioCol = new TableColumn<>("Priority");
        prioCol.setCellValueFactory(new PropertyValueFactory<>("priority"));

        TableColumn<Ticket, String> activityCol = new TableColumn<>("Request Type");
        activityCol.setCellValueFactory(new PropertyValueFactory<>("requestType"));

        TableColumn<Ticket, String> creatorCol = new TableColumn<>("Created By");
        creatorCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCreatedBy().getUsername()
                )
        );

        TableColumn<Ticket, String> resCol = new TableColumn<>("Resolution");
        resCol.setCellValueFactory(new PropertyValueFactory<>("resolution"));

        TableColumn<Ticket, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCreatedDate().toString()));


        table.getColumns().addAll(idCol, titleCol, statusCol, prioCol,
                creatorCol, resCol, dateCol, activityCol);

        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void deleteWarningDialog(Ticket ticket) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Delete Ticket");
        alert.setContentText("Are you sure you want to delete this ticket ?");

        ButtonType okBtn = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okBtn);

        alert.showAndWait();
    }


//    this is a comment
//    this is a comment
//    this is a comment

    private void resolveDialog(Ticket ticket) {
        Dialog<Void> dialog = new Dialog<>();
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

        dialog.showAndWait();
    }

    private void priorityDialog(Ticket ticket) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Priority");
        Label lbl = new Label("New priority (1–4):");
        TextField field = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(lbl, 0, 0);
        grid.add(field, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait();
    }

    private void newTicketDialog() {
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
                    case "Security issue" ->
                        requestType = Ticket.JobRequest.security_issues;
                    case "New computer configuration" ->
                        requestType = Ticket.JobRequest.new_computer_configuration;
                    case "Network issue" ->
                        requestType = Ticket.JobRequest.network_issue;
                    case "Software/app installation" ->
                        requestType = Ticket.JobRequest.software_app_installation
                    default -> {
                        new Alert(Alert.AlertType.ERROR, "Unknown request type selected.")
                                .showAndWait();
                        return;
                    }
                }

//                creator = currently logged-in user
                User loggedinUser = app.getLoggedIn();

//                create ticket
                Ticket ticket = new Ticket(title, desc,loggedinUser, requestType);

                app.getAllTickets().addAll(ticket);
            }
        });
    }
}
