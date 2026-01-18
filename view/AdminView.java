package view;

import app.ITTicketingSimpleApp;
import controller.Controllers;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.*;
import model.*;
import ui.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx. collections.transformation.FilteredList;
import javafx. collections.transformation.SortedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import Charts.adminCharts;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminView {

    private final ITTicketingSimpleApp app ;


    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public final ResolutionNoteDialog resolutionNoteDialog;


    public AdminView(ITTicketingSimpleApp app) {
        this.app = app;
        this.resolutionNoteDialog = new ResolutionNoteDialog();
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Table Setup
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

        setupAdminTable(table);

        // Scroll pane
        ScrollPane tableScroll = new ScrollPane(table);
        tableScroll.setFitToWidth(true);
        tableScroll.setFitToHeight(true);
        tableScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        tableScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Get Tickets and Setup filtering
        ObservableList<Ticket> masterList = app.getTicketManager().getAllTickets();
        FilteredList<Ticket> filteredTickets = new FilteredList<>(masterList, t -> true); // replaced t parameter to _ coz it was showing t never used warning.
        SortedList<Ticket> sorted = new SortedList<>(filteredTickets);
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);

        // Action Buttons
        Button resolveBtn = new Button("Resolve");
        Button deleteBtn = new Button("Delete");
        Button changePriorityBtn = new Button("Change Priority");
        Button logoutBtn = new Button("Logout");



//      HBox buttons = new HBox(10, resolveBtn, deleteBtn, changePriorityBtn, logoutBtn);
        HBox buttons = new HBox(10, resolveBtn, deleteBtn, changePriorityBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setPadding(new Insets(10, 0, 10, 0));

        // Event handlers for buttons
        logoutBtn.setOnAction(controller::onLogout);
        deleteBtn.setOnAction(controller::onDelete);
        resolveBtn.setOnAction(controller::onResolve);
        changePriorityBtn.setOnAction(controller::onChangePriority);

        //Filter controls
        Label filterLabel = new Label("Filter:");
        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Any", "Open", "Resolved", "cancelled");
        statusBox.setValue("Any");

        // status filter listener
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


        // Date Filter controls
        DatePicker fromDatePicker = new DatePicker();
        fromDatePicker.setPromptText("From date");
        fromDatePicker.setEditable(false); //better UX
        fromDatePicker.setPrefWidth(120);

        DatePicker toDatePicker = new DatePicker();
        toDatePicker.setPromptText("To date");
        toDatePicker.setEditable(false);
        toDatePicker.setPrefWidth(120);

        // Request Type Filter - Dropdown implementation
        // Label and ComboBox for Request Type filter
        Label requestLabel = new Label("Request Type:");
        ComboBox<String> requestBox = new ComboBox<>();
        requestBox.setPromptText("All");
        requestBox.setPrefWidth(180);

        // Populate the dropdown with request types
        requestBox.getItems().addAll(
                "All",    //Default option to show all types
                "Security issues",
                "New computer configuration",
                "Software/app installation",
                "Network issue"
        );
        requestBox.setValue("All");   //Set default value

        // Apply Filter Button
        Button applyFilterBtn = new Button("Apply");

        //Apply Filter button Action
        applyFilterBtn.setOnAction(e -> TicketFilters.applyFilters(filteredTickets, fromDatePicker, toDatePicker, requestBox, statusBox.getValue()));

        //Add a "Clear Filters" button for better UX
        Button clearFilterBtn = new Button("Clear");
        clearFilterBtn.setOnAction(e ->{
            TicketFilters.clearFilters(filteredTickets,statusBox,fromDatePicker,toDatePicker,requestBox);
        });

        //Filter Bar Layout (changed from 10 to 8)
        HBox filterBar = new HBox(8, filterLabel, statusBox, fromDatePicker,
                toDatePicker, requestLabel, requestBox, applyFilterBtn);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(5, 0, 5, 0));

        // Add clear button to filter Bar
        filterBar.getChildren().add(clearFilterBtn);

        // display greetings with loggedin Username
        Label greeting = new Label("👤 Hi, " + app.getLoggedIn().getUsername());
        greeting.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox topRow = new HBox(spacer, greeting);
        topRow.setPadding(new Insets(5));


        // Top area -VBox
        VBox topArea = new VBox(topRow,filterBar, buttons);
        topArea.setSpacing(5);

        //Charts section
        adminCharts charts = new adminCharts(app);
        BarChart<String, Number> barChart = charts.getMonthlyTicketsBarChart();
        PieChart pieChart = charts.getMonthlyTicketsPieChart();
        BarChart<String, Number> requestTypeChart = charts.getRequestTypeOverTimeChart();

// setCenter(...) your existing dashboard content



        //Style charts to reduce congestion
        barChart.setLegendVisible(false);
        barChart.setTitle("Tickets Raised in " + LocalDate.now().getYear());
        barChart.setPrefHeight(250);

        pieChart.setTitle("Tickets per Month");
        pieChart.setPrefHeight(250);

        VBox chartsBox = new VBox(15, barChart,requestTypeChart, pieChart);
        chartsBox.setPadding(new Insets(10));

        //Split Pane - For table and charts
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(tableScroll, chartsBox);
        splitPane.setDividerPositions(0.90); // Give more space to the table
//        splitPane.setDividerPositions(0.60);  //repetition

        //Bottom Bar
        HBox bottomBar = new HBox(logoutBtn);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(5)); //can be changed to 10,0,0,0 for space

        root.setTop(topArea);
        root.setCenter(splitPane);
        root.setBottom(bottomBar);

        splitPane.setDividerPositions(0.90); // Give more space to the table


        return new Scene(root, 1200, 700); //Increased window from 1000, 550

    }

    // Setup table with Columns Tables
    private void setupAdminTable(TableView<Ticket> table) {
        TableColumn<Ticket, Integer> idCol = new TableColumn<>("ID");

        //ID Column
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(50); //reduce congestion

        //Title Column
        TableColumn<Ticket, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(100); //reduced to 100 from 150

        // Request Type Column
        TableColumn<Ticket, String> requestCol = new TableColumn<>("Request Type");
        requestCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        app.formatEnumName(cellData.getValue().getRequestType().name())
                )
        );
        requestCol.setPrefWidth(180); //request type column is still small increased from 150 to 180

        // Priority Column
        TableColumn<Ticket, Integer> prioCol = new TableColumn<>("Priority");
        prioCol.setCellValueFactory(new PropertyValueFactory<>("priority"));
        prioCol.setPrefWidth(70);

        //Status Column
        TableColumn<Ticket, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(80);

        //Date Column
        TableColumn<Ticket, String> creatorCol = new TableColumn<>("Created By");
        creatorCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        cellData.getValue().getCreatedBy().getUsername()
                )
        );
        creatorCol.setPrefWidth(100);

        //Created By Column
        TableColumn<Ticket, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell ->
                new SimpleStringProperty(
                        cell.getValue().getCreatedDate().format(dateFormatter)
                )
        );
        dateCol.setPrefWidth(140);

        //Resolution Column with pop-up button
        TableColumn<Ticket, String> resCol = getResCol();

        // attachment column
        TableColumn<Ticket, Void> attachCol = getTicketVoidTableColumn();

        //Column order
        table.getColumns().addAll(idCol, titleCol, requestCol, prioCol, statusCol,
                creatorCol, dateCol, resCol, attachCol);

        /* Column resize policy */
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
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


