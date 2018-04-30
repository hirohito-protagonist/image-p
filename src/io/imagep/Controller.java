package io.imagep;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;

public class Controller {

    @FXML
    private BorderPane root;
    @FXML
    private ImageView imageView;
    @FXML
    private Label dimensionLabel;

    @FXML
    private void imageOpenAction(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg")
        );
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        Image image = null;
        if (selectedFile != null) {
            image = new Image("file:" + selectedFile.getAbsolutePath());
            imageView.setFitWidth(image.getWidth());
            imageView.setFitHeight(image.getHeight());
            imageView.setImage(image);
        }
        dimensionLabel.setText(dimensionInformation(image));
    }


    private String dimensionInformation(Image image) {
        return image == null ? "0 x 0px" : (int)image.getWidth() + " x " + (int)image.getHeight() + "px";
    }
}
