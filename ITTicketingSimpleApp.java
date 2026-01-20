package app;

import javafx.stage.Window;
import model.*;
import view.*;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class ITTicketingSimpleApp extends Application {

    private Stage stage;
    private final TicketManager ticketManager = new TicketManager();

    private final List<User> userList = Arrays.asList(
            new User("user", "user", model.User.Role.user),
            new User("anita", "ann123", model.User.Role.user),
            new User("admin", "admin", model.User.Role.admin),
            new User("sam", "sm678", model.User.Role.user),
            new User("edwin", "ed456", model.User.Role.user),
            new User("muni", "mn419", model.User.Role.user)
    );


    private User loggedIn;

    public static void main(String[] args) {
        AttachmentStorage.init(); //clear the temp folder of temporary attachments.
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
        loginBtn.getStyleClass().add("primary");

        loginBtn.setOnAction(e -> {
            String username = userField.getText().trim();
            String password = passField.getText().trim();

            boolean loginSuccess = false;

            for (User user : userList) {
                if (user.getUsername().equals(username) &&
                        user.getPassword().equals(password)) {
                    loggedIn = user;
                    loginSuccess = true;

                    if (user.getRole() == model.User.Role.admin) {
                        stage.setScene(new AdminView(this).createScene());
                    } else {
                        stage.setScene(new UserView(this).createScene());
                    }
                    stage.setMaximized(true);
                    break;
                }
            }

            if (!loginSuccess) {
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

    public String formatEnumName(String raw) {
        // replace underscores with spaces
        String cleaned = raw.replace("_", " ");
        // capitalize first letter only
        return cleaned.substring(0, 1).toUpperCase() + cleaned.substring(1).toLowerCase();
    }

    public Window getPrimaryStage() {
        return null;
    }
    public static void applyStyles(Scene scene) {
        URL css = ITTicketingSimpleApp.class.getResource("/style.css");
        if (css != null) {
            scene.getStylesheets().add(css.toExternalForm());
        }
    }
}
