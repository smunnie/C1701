package ui;

import javafx.geometry.Insets;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import model.Ticket;

public class ResolutionNoteDialog {

    //Shows resolution note in pop up dialog window
    public void show(Ticket ticket) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Resolution Details"); //create dialog window
        dialog.setHeaderText("Ticket #" + ticket.getId() + ": " + ticket.getTitle()); // set header
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK); //add button to dialogue
        Label resolutionLabel = new Label("Resolution Note:"); // create content for popup
        TextArea resolutionArea = new TextArea(ticket.getResolution_note());
        resolutionArea.setEditable(false); //read-only
        resolutionArea.setWrapText(true); //word wrapping

        //create layout
        VBox content = new VBox(10, resolutionLabel, resolutionArea);
        content.setPadding(new Insets(15));

        //Set content and Show dialog
        dialog.getDialogPane().setContent(content);
        dialog.showAndWait(); //optional void, Blocks until closed
    }
}
