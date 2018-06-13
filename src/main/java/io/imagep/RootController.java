package io.imagep;

import io.imagep.dialog.Dialog;
import io.imagep.core.Color;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class RootController implements Initializable {

    @FXML
    private VBox root;
    @FXML
    private ImageView imageView;
    @FXML
    private ImageView previewImage;
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
    private Button applyEffects;
    @FXML
    private AreaChart histogram;
    @FXML
    private AreaChart redHistogram;
    @FXML
    private AreaChart greenHistogram;
    @FXML
    private AreaChart blueHistogram;

    @FXML
    private void imageOpenAction(ActionEvent e) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.jpe", "*.bmp")
        );
        File selectedFile = fileChooser.showOpenDialog(root.getScene().getWindow());
        Image image = null;
        if (selectedFile != null) {
            image = new Image("file:" + selectedFile.getAbsolutePath());
            imageView.setFitWidth(image.getWidth());
            imageView.setFitHeight(image.getHeight());
            imageView.setImage(image);
            zoom.setDisable(false);
            contrast.setDisable(false);
            hue.setDisable(false);
            saturation.setDisable(false);
            brightness.setDisable(false);
            applyEffects.setDisable(false);
        }
        dimensionLabel.setText(dimensionInformation(image));
    }


    private String dimensionInformation(Image image) {

        return image == null ? "0 x 0px" : (int)image.getWidth() + " x " + (int)image.getHeight() + "px";
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ColorAdjust colorAdjust = new ColorAdjust();

        previewImage.effectProperty().bind(imageView.effectProperty());
        previewImage.imageProperty().bind(imageView.imageProperty());
        imageView.imageProperty().addListener((image) -> {
            histogram.getData().setAll(Histogram.rgb(imageView.getImage()));
            redHistogram.getData().setAll(Histogram.red(imageView.getImage()));
            greenHistogram.getData().setAll(Histogram.green(imageView.getImage()));
            blueHistogram.getData().setAll(Histogram.blue(imageView.getImage()));
        });

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
            imageView.setImage(Color.grayScale(image));
        }
    }

    @FXML
    private void invertAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(Color.invert(image));
        }
    }

    @FXML
    private void darkenAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(Color.darker(image));
        }
    }

    @FXML
    private void lightenAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(Color.lighten(image));
        }
    }

    @FXML
    private void saturateAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(Color.saturate(image));
        }
    }

    @FXML
    private void desaturateAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(Color.desaturate(image));
        }
    }

    @FXML
    private void binarizeAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            createDialog("/fxml/binarize.fxml", "Binarize core");
        }
    }

    @FXML
    private void posterizeAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            createDialog("/fxml/posterize.fxml", "Posterize core");
        }
    }

    @FXML
    private void gammaCorrectionAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(Color.gammaCorrection(image, .5));
        }
    }

    @FXML
    private void thresholdAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            createDialog("/fxml/threshold.fxml", "Threshold core");
        }
    }

    private void createDialog(String templatePath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(templatePath));
            Parent root = loader.load();
            Dialog dialog = loader.getController();
            dialog.setImage(imageView.getImage());
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.show();
            dialog.closeProperty().addListener((prop, prev, current) -> {
                if (current) {
                    stage.close();
                }
            });
            dialog.imageProperty().addListener((prop, prev, current) -> {
                imageView.setImage(current);
                stage.close();
            });
        } catch (IOException ignore) {
            ignore.printStackTrace();
        }
    }

    @FXML
    private void applyEffectsAction(ActionEvent e) {

        Image image = imageView.getImage();
        if (image != null) {
            imageView.setImage(imageViewSnapshot(imageView));
            contrast.setValue(0);
            hue.setValue(0);
            saturation.setValue(0);
            brightness.setValue(0);
        }
    }

    private WritableImage imageViewSnapshot(ImageView view) {

        Image image = view.getImage();
        WritableImage writableImage = new WritableImage((int) image.getWidth(), (int) image.getHeight());
        double width = view.getFitWidth();
        double height = view.getFitHeight();
        view.setFitWidth(image.getWidth());
        view.setFitHeight(image.getHeight());
        view.snapshot(null, writableImage);
        view.setFitWidth(width);
        view.setFitHeight(height);

        return  writableImage;
    }
}
