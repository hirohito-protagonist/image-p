package io.imagep;

import io.imagep.operation.ImageOperation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private BorderPane root;
    @FXML
    private ImageView imageView;
    @FXML
    private Label dimensionLabel;
    @FXML
    private Slider contrast;
    @FXML
    private Slider hue;
    @FXML
    private Slider brightness;
    @FXML
    private Slider saturation;
    @FXML
    private Slider zoom;
    @FXML
    private Label zoomInformation;


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
            zoom.setDisable(false);
        }
        dimensionLabel.setText(dimensionInformation(image));
    }


    private String dimensionInformation(Image image) {

        return image == null ? "0 x 0px" : (int)image.getWidth() + " x " + (int)image.getHeight() + "px";
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ColorAdjust colorAdjust = new ColorAdjust();

        contrast.valueProperty().addListener((slider, prevValue, currentValue) -> {
            colorAdjust.setContrast((double)currentValue);
            imageView.setEffect(colorAdjust);
        });

        hue.valueProperty().addListener((slider, prevValue, currentValue) -> {
            colorAdjust.setHue((double)currentValue);
            imageView.setEffect(colorAdjust);
        });

        brightness.valueProperty().addListener((slider, prevValue, currentValue) -> {
            colorAdjust.setBrightness((double)currentValue);
            imageView.setEffect(colorAdjust);
        });

        saturation.valueProperty().addListener((slider, prevValue, currentValue) -> {
            colorAdjust.setSaturation((double)currentValue);
            imageView.setEffect(colorAdjust);
        });

        zoom.valueProperty().addListener((slider, prevValue, currentValue) -> {
            Image image = imageView.getImage();
            double scale = (double) currentValue;
            double w = image.getWidth() * scale;
            double h = image.getHeight() * scale;
            imageView.setFitWidth(w);
            imageView.setFitHeight(h);
            zoomInformation.setText((int)(scale * 100) + "%");
        });
    }


    @FXML
    private void grayScaleAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(ImageOperation.grayScale(image));
        }
    }

    @FXML
    private void invertAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(ImageOperation.invert(image));
        }
    }

    @FXML
    private void darkenAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(ImageOperation.darker(image));
        }
    }

    @FXML
    private void lightenAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(ImageOperation.lighten(image));
        }
    }
}
