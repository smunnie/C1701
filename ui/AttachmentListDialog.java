package ui;

import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Ticket;

import java.nio.file.Path;

public class AttachmentListDialog {

    public static void show(Ticket ticket) {

        Stage stage = new Stage();
        stage.setTitle("Attachments");
        stage.initModality(Modality.APPLICATION_MODAL);

        ListView<Path> listView = new ListView<>();
        listView.getItems().addAll(ticket.getAttachments());

        // Show only file names in the list
        listView.setCellFactory(lv -> new javafx.scene.control.ListCell<>() {
            @Override
            protected void updateItem(Path item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getFileName().toString());
            }
        });

        // When user double-clicks an attachment → open image preview
        listView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                Path selected = listView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    ImagePreviewDialog.show(selected);
                }
            }
        });

        stage.setScene(new Scene(listView, 200, 100));
        stage.showAndWait();
    }
}
