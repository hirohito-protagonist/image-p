package io.imagep.dialog;

import io.imagep.operation.ImageOperation;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class PosterizeController implements Initializable, Dialog {

    SimpleBooleanProperty closeProperty = new SimpleBooleanProperty(false);
    SimpleObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    @FXML
    private ImageView preview;

    @FXML
    private Slider posterize;
    @FXML
    private Button apply;
    @FXML
    private Button close;

    private Image originalImage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        posterize.valueProperty().addListener((slider, prevValue, currentValue) -> {
            preview.setImage(ImageOperation.posterize(imageProperty.get(), currentValue.intValue()));
        });
    }

    @Override
    public void setImage(Image image) {
        preview.setImage(ImageOperation.posterize(image, (int)posterize.getValue()));
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(596);
        imageView.setPreserveRatio(true);
        imageProperty.set(imageView.snapshot(null, null));
        originalImage = image;
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
        imageProperty.set(ImageOperation.posterize(originalImage, (int)posterize.getValue()));
        closeProperty.set(true);
    }
}
