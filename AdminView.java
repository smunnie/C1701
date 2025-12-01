import javafx.beans.property.SimpleStringProperty;
import javafx. collections.FXCollections;
import javafx.collections.ObservableList;
import javafx. collections.transformation.FilteredList;
import javafx. collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
        Button logoutBtn = new Button("Logout");

        HBox buttons = new HBox(10, resolveBtn, deleteBtn, changePriorityBtn, logoutBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setPadding(new Insets(10, 0, 10, 0));

        logoutBtn.setOnAction(e -> app.logout());

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
}
