package view;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import model.*;
import app.ITTicketingSimpleApp;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import ui.TicketDialogs;
import javafx.scene.layout.Priority;


public class UserView {

    private final ITTicketingSimpleApp app;
    private final TicketDialogs ticketDialogs;

    public UserView(ITTicketingSimpleApp app) {
        this.app = app;
        this.ticketDialogs = new TicketDialogs(app);
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label header = new Label("User Dashboard - " + app.getLoggedIn().getUsername());
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        BorderPane.setMargin(header, new Insets(0, 0, 10, 0));
        BorderPane.setMargin(header, new Insets(0, 0, 10, 0));

        TableView<Ticket> table = new TableView<>();
        setupUserTable(table);
        var allTickets = app.getAllTickets();
        User current = app.getLoggedIn();

        // Filter: only tickets created by this user
        FilteredList<Ticket> userTickets = new FilteredList<>(allTickets, t ->
                t.getCreatedBy().getUsername().equals(current.getUsername())
        );
        SortedList<Ticket> sortedTickets = new SortedList<>(userTickets);

        sortedTickets.setComparator((t1, t2) ->
                t2.getCreatedDate().compareTo(t1.getCreatedDate()));
        sortedTickets.comparatorProperty().bind(table.comparatorProperty());

        table.setItems(sortedTickets);

        Button newTicketBtn = new Button("Raise Ticket");
        Button logoutBtn = new Button("Logout");
        Button decreasePriBtn = new Button("Decrease priority");
        Button deleteBtn = new Button("Delete");


        logoutBtn.setOnAction(e -> app.logout());

        newTicketBtn.setOnAction(e -> {
            ticketDialogs.newTicketDialog();
        });

        decreasePriBtn.setOnAction(e -> {
            Ticket t = table.getSelectionModel().getSelectedItem();
            ticketDialogs.decreasePriDialog(t, table);
        });

        deleteBtn.setOnAction(e -> {
            Ticket t = table.getSelectionModel().getSelectedItem();
            if (t == null) return;
            ticketDialogs.deleteWarningDialog(t,table);
        });


        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        ToolBar toolBar = new ToolBar(
                newTicketBtn,
                decreasePriBtn,
                deleteBtn,
                spacer,
                logoutBtn
        );

        root.setTop(toolBar);
        root.setCenter(table);

        return new Scene(root, 800, 450);
    }

    private void setupUserTable(TableView<Ticket> table) {
        TableColumn<Ticket, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Ticket, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Ticket, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Ticket, Integer> prioCol = new TableColumn<>("Priority");
        prioCol.setCellValueFactory(new PropertyValueFactory<>("priority"));

        TableColumn<Ticket, String> resCol = new TableColumn<>("Resolution");
        resCol.setCellValueFactory(new PropertyValueFactory<>("resolution_note"));

        TableColumn<Ticket, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCreatedDate().toString()));

        TableColumn<Ticket, String> RequestCol = new TableColumn<>("Request");
        RequestCol.setCellValueFactory(new PropertyValueFactory<>("RequestType"));

        table.getColumns().addAll(idCol, titleCol, RequestCol, prioCol, statusCol, dateCol, resCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); //changed column order
    }

}
