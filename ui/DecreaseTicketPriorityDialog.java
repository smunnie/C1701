package ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Ticket;

public class DecreaseTicketPriorityDialog {


    public void show(Ticket ticket, TableView<Ticket> table) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Decrease Priority");

        Label prioLabel = new Label("Priority: ");
        Label currentValue = new Label(String.valueOf(ticket.getPriority()));

        Button minusBtn = new Button("-");
        minusBtn.setStyle("-fx-font-size: 18px; -fx-min-width: 30px;");

        minusBtn.setOnAction(e -> {
            int p = Integer.parseInt(currentValue.getText());
            if (p < 4) {
                p++;
                currentValue.setText(String.valueOf(p));
            }
        });

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(20);
        grid.setVgap(10);
        grid.setPrefWidth(400);

        grid.add(prioLabel, 0, 0);
        grid.add(currentValue, 1, 0);
        grid.add(minusBtn, 2, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                int newPriority = Integer.parseInt(currentValue.getText());
                ticket.setPriority(newPriority);
            }
            table.refresh();
        } );
    }
}
