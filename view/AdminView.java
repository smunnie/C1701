package view;

import app.ITTicketingSimpleApp;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import Charts.adminCharts;

import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime; // in the date column you have date and time
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AdminView {

    private final ITTicketingSimpleApp app;
//    private final TicketDialogs ticketDialogs;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final DeleteTicketDialog deleteDialogs;
    private final ResolveTicketDialog resolveDialog;
    private final ChangepriorityTicketDialog changepriorityDialog;
    private final ResolutionNoteDialog resolutionNoteDialog;

    public AdminView(ITTicketingSimpleApp app) {
        this.app = app;
        this.deleteDialogs = new DeleteTicketDialog(app);
        this.resolveDialog = new ResolveTicketDialog();
        this.changepriorityDialog = new ChangepriorityTicketDialog();
        this.resolutionNoteDialog = new ResolutionNoteDialog();
    }

    public Scene createScene() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Header
        Label header = new Label("Admin Dashboard - " + app.getLoggedIn().getUsername());
        header.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Table Setup
        TableView<Ticket> table = new TableView<>();
        setupAdminTable(table);

        // Apply custom CSS to reduce congestion
        table.setStyle("-fx-font-size : 12px");

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
//        Button newTicketBtn = new Button("Raise Ticket");
        Button logoutBtn = new Button("Logout");

//        HBox buttons = new HBox(10, resolveBtn, deleteBtn, changePriorityBtn, logoutBtn);
        HBox buttons = new HBox(10, resolveBtn, deleteBtn, changePriorityBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);
        buttons.setPadding(new Insets(10, 0, 10, 0));

        // Event handlers for buttons
        logoutBtn.setOnAction(e -> app.logout());
        deleteBtn.setOnAction(e -> {
            Ticket t = table.getSelectionModel().getSelectedItem();
            if (t == null) return;
            deleteDialogs.show(t,table);
        });
        resolveBtn.setOnAction(e -> {
            Ticket t = table.getSelectionModel().getSelectedItem();
            if (t == null) return;
            resolveDialog.show(t, table);
        });
        changePriorityBtn.setOnAction(e -> {
            Ticket t = table.getSelectionModel().getSelectedItem();
            if (t == null) return;
            changepriorityDialog.show(t,table);
        });

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
        applyFilterBtn.setOnAction(e ->{
            applyFilters(filteredTickets, fromDatePicker, toDatePicker, requestBox, statusBox.getValue());
        });

        //Add a "Clear Filters" button for better UX
        Button clearFilterBtn = new Button("Clear");
        clearFilterBtn.setOnAction(e ->{
            // clear all filter controls
            statusBox.setValue("Any");
            fromDatePicker.setValue(null);
            toDatePicker.setValue(null);
            requestBox.setValue("All");

            // Reset all filters
            filteredTickets.setPredicate(ticket -> true);
            System.out.println("All filters cleared");
        });

        //Filter Bar Layout (changed from 10 to 8)
        HBox filterBar = new HBox(8, filterLabel, statusBox, fromDatePicker,
                toDatePicker, requestLabel, requestBox, applyFilterBtn);
        filterBar.setAlignment(Pos.CENTER_LEFT);
        filterBar.setPadding(new Insets(5, 0, 5, 0));

        // Add clear button to filter Bar
        filterBar.getChildren().add(clearFilterBtn);

        // Top area -VBox
        VBox topArea = new VBox(header, filterBar, buttons);
        topArea.setSpacing(5);

        //Charts section
        adminCharts charts = new adminCharts(app);
        BarChart<String, Number> barChart = charts.getMonthlyTicketsBarChart();
        PieChart pieChart = charts.getMonthlyTicketsPieChart();

        //Style charts to reduce congestion
        barChart.setLegendVisible(false);
        barChart.setTitle("Tickets Raised in " + LocalDate.now().getYear());
        barChart.setPrefHeight(250);

        pieChart.setTitle("Tickets per Month");
        pieChart.setPrefHeight(250);

        VBox chartsBox = new VBox(15, barChart, pieChart);
        chartsBox.setPadding(new Insets(10));

        //Split Pane - For table and charts
        SplitPane splitPane = new SplitPane();
        splitPane.getItems().addAll(tableScroll, chartsBox);
        splitPane.setDividerPositions(0.65); // Give more space to the table
//        splitPane.setDividerPositions(0.60);  //repetition

        //Bottom Bar
        HBox bottomBar = new HBox(logoutBtn);
        bottomBar.setAlignment(Pos.CENTER_LEFT);
        bottomBar.setPadding(new Insets(5)); //can be changed to 10,0,0,0 for space

        root.setTop(topArea);
        root.setCenter(splitPane);
        root.setBottom(bottomBar);

        BorderPane.setMargin(header, new Insets(0, 0, 5, 0));

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
        titleCol.setPrefWidth(150);

        // Request Type Column
        TableColumn<Ticket, String> requestCol = new TableColumn<>("Request Type");
        requestCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(
                        app.formatEnumName(cellData.getValue().getRequestType().name())
                )
        );
        requestCol.setPrefWidth(150);

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

        //Column order
        table.getColumns().addAll(idCol, titleCol, requestCol, prioCol, statusCol,
                creatorCol, dateCol, resCol, attachCol);

        /* Column resize policy */
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
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
                    Ticket t = getTableView().getItems().get(getIndex());
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
                } else {
                    Ticket t = getTableView().getItems().get(getIndex());
                   //simple check show button if resolution exists
                  // this check does not work
                    if (t.getResolution_note() != null && !t.getResolution_note().isEmpty()){
                        setGraphic(viewButton);
                        setText(null);
                    } else {
                        setGraphic(null); //node
                        setText("No resolution note");
                    }
                }
            }
        });
        return resCol;
    }

    //Apply all filters when "Apply" button is pressed
    //Combines status, date range and request type filters
    private void applyFilters(FilteredList<Ticket> filteredTickets,
                              DatePicker fromDatePicker,
                              DatePicker toDatePicker,
                              ComboBox<String> requestBox,
                              String selectedStatus) {
        filteredTickets.setPredicate(ticket -> {
            boolean passesFilters = true;

            //Status filter (existing functionality)
            if (!selectedStatus.equals("Any")) {
                //convert both strings
                String ticketStatus = ticket.getStatus().name().toLowerCase();
                String filterStatus = selectedStatus.toLowerCase();
                passesFilters = ticketStatus.equals(filterStatus);
            }

            //Date range filter - LocalDateTime comparisons
            LocalDate fromDate = fromDatePicker.getValue();
            LocalDate toDate = toDatePicker.getValue();
            LocalDateTime ticketDateTime = ticket.getCreatedDate();

            if (fromDate != null) {
                //convert LocalDate to LocalDateTime
                //method chaining demonstrated here
                LocalDateTime fromDateTime = fromDate.atStartOfDay();
                passesFilters = passesFilters && !ticketDateTime.isBefore(fromDateTime);
            }

            if (toDate != null) {
                LocalDateTime toDateTime = toDate.atTime(LocalTime.MAX);
                passesFilters = passesFilters && !ticketDateTime.isAfter(toDateTime);
            }

            // request type filter
            String selectedRequestType = requestBox.getValue();
            //map UI display names to actual enum values as might differ from enum names
            String enumEquivalent = mapRequestTypeToEnum(selectedRequestType);
            if (enumEquivalent != null){
                //Compare ticket's request type with selected type
                String ticketRequestType = ticket.getRequestType().name();
                passesFilters = passesFilters && ticketRequestType.equalsIgnoreCase(enumEquivalent);
            }
            return passesFilters; // Ticket passes all filters
        });
    }

    //Helper method to map UI display names to actual enum values
    private String mapRequestTypeToEnum (String uiDisplayName){
        if (uiDisplayName== null || uiDisplayName.equals("All")) {
            return null;
        }
        return switch (uiDisplayName.toLowerCase()) {
            case "security issues" -> "security_issues"; //Matches enum value
            case "new computer configuration" -> "new_computer_configuration";
            case "software/app installation" -> "software_app_installation";
            case "network issue" -> "network_issue";
            default -> {
                System.err.println("unknown request type: " + uiDisplayName);
                yield null;
            }
        };
    }
}
