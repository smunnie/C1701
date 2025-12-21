package ui;

import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.nio.file.Path;

public class ImagePreviewDialog {

    public static void show(Path imagePath) {

        Stage stage = new Stage();
        stage.setTitle(imagePath.getFileName().toString());
        stage.initModality(Modality.APPLICATION_MODAL);

        Image image = new Image(imagePath.toUri().toString());

        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(600);
        imageView.setFitHeight(400);

        BorderPane root = new BorderPane(imageView);

        stage.setScene(new Scene(root, 600, 400));
        stage.showAndWait();
    }
}
