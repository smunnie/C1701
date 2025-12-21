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
import ui.AttachmentListDialog;
import ui.DecreaseTicketPriorityDialog;
import ui.DeleteTicketDialog;
import ui.NewTicketDialog;
import javafx.scene.layout.Priority;

import java.time.format.DateTimeFormatter;


public class UserView {

    private final ITTicketingSimpleApp app;
//    private final TicketDialogs ticketDialogs;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final NewTicketDialog newTicketDialog;
    private final DeleteTicketDialog deleteDialogs;
    private final DecreaseTicketPriorityDialog decreasePriDialog;

    public UserView(ITTicketingSimpleApp app) {
        this.app = app;
        this.newTicketDialog = new NewTicketDialog(app);
        this.decreasePriDialog = new DecreaseTicketPriorityDialog();
        this.deleteDialogs = new DeleteTicketDialog(app);
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
            newTicketDialog.show();
        });

        decreasePriBtn.setOnAction(e -> {
            Ticket t = table.getSelectionModel().getSelectedItem();
            decreasePriDialog.show(t, table);
        });

        deleteBtn.setOnAction(e -> {
            Ticket t = table.getSelectionModel().getSelectedItem();
            if (t == null) return;
            deleteDialogs.show(t,table);
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
                new SimpleStringProperty(String.format(cell.getValue().getCreatedDate().format(dateFormatter))));

        TableColumn<Ticket, String> RequestCol = new TableColumn<>("Request");
        RequestCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        app.formatEnumName(cellData.getValue().getRequestType().name())
                )
        );

        // attachment column
        TableColumn<Ticket, Void> attachCol = new TableColumn<>("Attachment");
        attachCol.setCellFactory(col -> new TableCell<>() {
            // view button that opens up the list of attachments available
            private final Button btn = new Button("View");

            {
                btn.setOnAction(e -> {
                    Ticket ticket = getTableView().getItems().get(getIndex());
                    AttachmentListDialog.show(ticket);
                });
            }
            // function to say no attachments, instead of a view button when there's no attahment for the ticket
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Ticket ticket = getTableView().getItems().get(getIndex());
                    if (ticket.getAttachments().isEmpty()) {
                        // Show text instead of button
                        setGraphic(null);
                        setText("No attachments");
                    } else {
                        // Show the View button
                        setText(null);
                        setGraphic(btn);
                    }
                } }
        });

//        RequestCol.setCellValueFactory(new PropertyValueFactory<>("RequestType"));

        table.getColumns().addAll(idCol, titleCol, RequestCol, prioCol, statusCol, dateCol, resCol, attachCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); //changed column order
    }

}
