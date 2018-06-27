package io.imagep.dialog;

import io.imagep.core.filter.BoxBlur;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class BlurController implements Initializable, Dialog {

    SimpleBooleanProperty closeProperty = new SimpleBooleanProperty(false);
    SimpleObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    @FXML
    private ImageView preview;

    @FXML
    private Slider blur;

    private Image originalImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        blur.valueProperty().addListener((slider, prevValue, currentValue) -> {
            preview.setImage(BoxBlur.apply(imageProperty.get(), currentValue.intValue()));
        });
    }

    @Override
    public void setImage(Image image) {
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1024);
        imageView.setPreserveRatio(true);
        imageProperty.set(imageView.snapshot(null, null));
        originalImage = image;
        preview.setImage(BoxBlur.apply(imageProperty.get(), (int)blur.getValue()));
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
        imageProperty.set(BoxBlur.apply(originalImage, (int)blur.getValue()));
        closeProperty.set(true);
    }
}
