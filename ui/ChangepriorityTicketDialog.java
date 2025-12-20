package ui;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Ticket;

public class ChangepriorityTicketDialog {

    public void show(Ticket ticket, TableView<Ticket> table ) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Change Priority");
        Label lbl = new Label("New priority : ");
        ComboBox<Integer> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll(1, 2, 3, 4);
        priorityBox.setPromptText("Select priority");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10));

        grid.add(lbl, 0, 0);
        grid.add(priorityBox, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result -> {
            if(result == ButtonType.OK){
                int newpriority = priorityBox.getValue();
                ticket.setPriority(newpriority);
            }
            table.refresh();
        });
    }





}
