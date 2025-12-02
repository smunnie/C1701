import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class UserView {

    private final ITTicketingSimpleApp app;

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
        setupUserTable(table);

        Button newTicketBtn = new Button("Raise Ticket");
        Button logoutBtn = new Button("Logout");
<<<<<<< HEAD
        Button decreasePriBtn = new Button("Decrease priority");

        logoutBtn.setOnAction(e -> app.logout());

        newTicketBtn.setOnAction(e -> {
            newTicketDialog();
        });

        decreasePriBtn.setOnAction(e -> {
//            Ticket t = table.getSelectionModel().getSelectedItem();
//            if (t == null) return;
            decreasePriDialog();
        });


        HBox buttons = new HBox(10, newTicketBtn,decreasePriBtn, logoutBtn);
=======


        logoutBtn.setOnAction(e -> app.logout());

        HBox buttons = new HBox(10, newTicketBtn, logoutBtn);
>>>>>>> 3b08bfcf34d6e912644f8bed49df162db43cdb04
        BorderPane.setMargin(buttons, new Insets(10, 10, 0, 0));

        root.setTop(header);
        root.setCenter(table);
        root.setBottom(buttons);

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
        resCol.setCellValueFactory(new PropertyValueFactory<>("resolution"));

        TableColumn<Ticket, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getCreatedDate().toString()));

<<<<<<< HEAD
        TableColumn<Ticket, String> RequestCol = new TableColumn<>("Request");
        RequestCol.setCellValueFactory(new PropertyValueFactory<>("RequestType"));

        table.getColumns().addAll(idCol, titleCol, statusCol, prioCol, resCol, dateCol, RequestCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }

    private void newTicketDialog() {
        Dialog<Void> dialog = new Dialog<>();
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

        dialog.showAndWait();
    }

    private void decreasePriDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Decrease Priority");

        Label prioLabel = new Label("Current Priority: ");
        Label currentValue = new Label("3");
//        Label currentValue = new Label(String.valueOf(ticket.getPriority()));

        Button minusBtn = new Button("-");
        minusBtn.setStyle("-fx-font-size: 18px; -fx-min-width: 30px;");

        minusBtn.setOnAction(e -> {
            int p = Integer.parseInt(currentValue.getText());
            if (p > 1) {
                p--;
                currentValue.setText(String.valueOf(p));
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(prioLabel, 0, 0);
        grid.add(currentValue, 1, 0);
        grid.add(minusBtn, 2, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait();
    }
=======
        TableColumn<Ticket, String> activityCol = new TableColumn<>("Activity");
        activityCol.setCellValueFactory(new PropertyValueFactory<>("activityType"));

        table.getColumns().addAll(idCol, titleCol, statusCol, prioCol, resCol, dateCol, activityCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }


>>>>>>> 3b08bfcf34d6e912644f8bed49df162db43cdb04
}
