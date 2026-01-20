package view;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
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
import ui.*;
import javafx.scene.layout.Priority;
import controller.Controllers;

import java.time.format.DateTimeFormatter;

public class UserView {

    private final ITTicketingSimpleApp app;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public UserView(ITTicketingSimpleApp app) {
        this.app = app;
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        Label header = new Label("User Dashboard - " + app.getLoggedIn().getUsername());
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        BorderPane.setMargin(header, new Insets(0, 0, 10, 0));
        BorderPane.setMargin(header, new Insets(0, 0, 10, 0));

        TableView<Ticket> table = new TableView<>();
        Controllers controller = new Controllers(app,table);
        table.setRowFactory(tv -> {
                    TableRow<Ticket> row = new TableRow<>();

                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 2 && !row.isEmpty()) {
                            controller.onRowDoubleClick(row.getItem());
                        }
                    });
                    return row;
                });

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

        //css
        logoutBtn.getStyleClass().add("primary");
        decreasePriBtn.getStyleClass().add("primary");
        deleteBtn.getStyleClass().add("primary");
        newTicketBtn.getStyleClass().add("primary");

        logoutBtn.setOnAction(e -> app.logout());

        newTicketBtn.setOnAction(controller::onClickNewTicket);

        decreasePriBtn.setOnAction(controller::onDecreasePriority);

        deleteBtn.setOnAction(controller::onDelete);

        // display greetings with loggedin Username
        Label greeting = new Label("👤 Hi, " + app.getLoggedIn().getUsername());
        greeting.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        // Spacers
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();

        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);


        ToolBar toolBar = new ToolBar(
                newTicketBtn,
                decreasePriBtn,
                deleteBtn,
                leftSpacer,
                rightSpacer,
                greeting
        );

        root.setTop(toolBar);
        root.setCenter(table);

        //Bottom Bar
        HBox bottomBar = new HBox(logoutBtn);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(5)); //can be changed to 10,0,0,0 for space;
        root.setBottom(bottomBar);

        Scene scene = new Scene(root, 1200, 700);
        ITTicketingSimpleApp.applyStyles(scene);
        return scene;
    }

    private void setupUserTable(TableView<Ticket> table) {
        TableColumn<Ticket, Integer> idCol = new TableColumn<>("ID");
        // serialize row numbers
        idCol.setCellValueFactory(cell ->
                new javafx.beans.property.ReadOnlyObjectWrapper<>(
                        table.getItems().indexOf(cell.getValue()) + 1
                )
        );

        TableColumn<Ticket, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Ticket, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        TableColumn<Ticket, Integer> prioCol = new TableColumn<>("Priority");
        prioCol.setCellValueFactory(new PropertyValueFactory<>("priority"));

        TableColumn<Ticket, String> resCol = getResCol(); // method for resolution note pop up

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
        TableColumn<Ticket, Void> attachCol = getTicketVoidTableColumn();

        table.getColumns().addAll(idCol, titleCol, RequestCol, prioCol, statusCol, dateCol, resCol, attachCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS); //changed column order
    }

    private static TableColumn<Ticket, Void> getTicketVoidTableColumn() {
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
            // function to say no attachments, instead of a view button when there's no attachment for the ticket
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
                        setText("None");
                    } else {
                        // Show the View button
                        setText(null);
                        setGraphic(btn);
                    }
                } }
        });
        return attachCol;
    }

    //Resolution column with pop-up window
    private TableColumn<Ticket, String> getResCol() {
        TableColumn<Ticket, String> resCol = new TableColumn<>("Resolution");
        resCol.setCellValueFactory(new PropertyValueFactory<>("resolution_note"));

        //Create a resolution dialog instance
        ResolutionNoteDialog resolutionNoteDialog = new ResolutionNoteDialog();
        //Popup window implementation
        resCol.setCellFactory(column -> new TableCell<>() {
            private final Button viewButton = new Button("view note");
            {
                viewButton.setOnAction(event ->{
                    Ticket t = getTableRow().getItem();
                    if (t != null){
                        resolutionNoteDialog.show(t); // call method to show pop-up window
                    }
                });
            }
            @Override
            protected void updateItem(String item, boolean empty){
                super.updateItem(item, empty);

                if (empty){
                    setGraphic(null);
                    setText(null);
                    return;
                }
                //simple check show button if resolution exists
                // this check does not work
                if (item == null || item.isBlank()) {
                    setGraphic(null);
                    setText("None");
                } else {
                    setText(null);
                    setGraphic(viewButton); //node
                }
            }
        });
        return resCol;
    }
}
