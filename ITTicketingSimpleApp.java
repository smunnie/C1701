import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.List;


public class ITTicketingSimpleApp extends Application {

    private Stage stage;
    private final TicketManager ticketManager = new TicketManager();
//    private final ObservableList<Ticket> allTickets = FXCollections.observableArrayList();
    private final User newUser = new User( "user", "user", User.Role.user);
    private final User admin  = new User( "admin", "admin", User.Role.admin);
    private User loggedIn;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.stage = primaryStage;
        stage.setTitle("IT Ticketing System ");
        stage.setScene(createLoginScene());   // login is here
        stage.show();
    }

    private Scene createLoginScene() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        Label title = new Label("Login");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        grid.add(title, 0, 0, 2, 1);

        Label userLabel = new Label("Username:");
        TextField userField = new TextField();
        grid.add(userLabel, 0, 1);
        grid.add(userField, 1, 1);


        Label passLabel = new Label("Password:");
        PasswordField passField = new PasswordField();
        grid.add(passLabel, 0, 2);
        grid.add(passField, 1, 2);

        Button loginBtn = new Button("Login");
        Label errorMsg = new Label();
        grid.add(loginBtn, 1, 3);
        grid.add(errorMsg, 0, 4, 2, 1);

        GridPane.setMargin(title, new Insets(0, 0, 15, 0));


        loginBtn.setOnAction(e -> {
            String u1 = userField.getText().trim();
            String p1 = passField.getText().trim();

            if (u1.equals(newUser.getUsername()) && p1.equals(newUser.getPassword())) {
                loggedIn = newUser;
                stage.setScene(new UserView(this).createScene());
            } else if (u1.equals(admin.getUsername()) && p1.equals(admin.getPassword())) {
                loggedIn = admin;
                stage.setScene(new AdminView(this).createScene());
            } else {
                errorMsg.setText("Wrong username or password.");
            }
        });

        return new Scene(grid, 400, 250);
    }

    public void logout() {
        loggedIn = null;
        stage.setScene(createLoginScene());
    }

    public TicketManager getTicketManager() {
        return ticketManager;
    }

    public ObservableList<Ticket> getAllTickets() {
        return ticketManager.getAllTickets();
    }
    public User getLoggedIn() { return loggedIn; }


}
