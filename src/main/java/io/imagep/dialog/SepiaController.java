package io.imagep.dialog;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.effect.SepiaTone;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.net.URL;
import java.util.ResourceBundle;

public class SepiaController implements Initializable, Dialog {

    SimpleBooleanProperty closeProperty = new SimpleBooleanProperty(false);
    SimpleObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    SepiaTone sepiaTone = new SepiaTone();

    @FXML
    private ImageView preview;

    @FXML
    private Slider sepia;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sepia.valueProperty().addListener((slider, prevValue, currentValue) -> {
            sepiaTone.setLevel(currentValue.intValue() / 100d);
            preview.setEffect(sepiaTone);
        });
    }

    @Override
    public void setImage(Image image) {
        sepiaTone.setLevel(sepia.getValue() / 100d);
        preview.setImage(image);
        preview.setEffect(sepiaTone);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(596);
        imageView.setPreserveRatio(true);
        imageProperty.set(imageView.snapshot(null, null));
    }

    @Override
    public SimpleBooleanProperty closeProperty() {
        return closeProperty;
    }

    @Override
    public SimpleObjectProperty<Image> imageProperty() {
        return imageProperty;
    }

    @FXML
    private void closeAction(ActionEvent e) {
        closeProperty.set(true);
    }

    @FXML
    private void applyAction(ActionEvent e) {
        imageProperty.set(imageViewSnapshot(preview));
        closeProperty.set(true);
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
