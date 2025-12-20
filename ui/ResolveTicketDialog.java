package ui;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import model.Ticket;

public class ResolveTicketDialog {

    public void show(Ticket ticket, TableView<Ticket> table) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Resolve Ticket");

        Label lbl = new Label("Enter resolution notes:");
        TextArea area = new TextArea();
        area.setPrefRowCount(2);

        GridPane grid1 = new GridPane();
        grid1.setHgap(10);
        grid1.setVgap(10);
        grid1.setPadding(new Insets(10));

        grid1.add(lbl, 0, 0);
        grid1.add(area, 0, 1);

        dialog.getDialogPane().setContent(grid1);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(result ->{
            if (result == ButtonType.OK){
                String notes = area.getText();
                ticket.setResolution_note(notes);
                ticket.setStatus(Ticket.Status.resolved);
            }
            table.refresh();
        });
    }

}
