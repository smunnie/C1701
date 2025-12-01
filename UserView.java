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


        logoutBtn.setOnAction(e -> app.logout());

        HBox buttons = new HBox(10, newTicketBtn, logoutBtn);
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

        TableColumn<Ticket, String> activityCol = new TableColumn<>("Activity");
        activityCol.setCellValueFactory(new PropertyValueFactory<>("activityType"));

        table.getColumns().addAll(idCol, titleCol, statusCol, prioCol, resCol, dateCol, activityCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
    }


}
