package io.imagep.dialog;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Slider;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;

import java.net.URL;
import java.util.ResourceBundle;

public class BlurController implements Initializable, Dialog {

    SimpleBooleanProperty closeProperty = new SimpleBooleanProperty(false);
    SimpleObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    GaussianBlur gaussianBlur = new GaussianBlur();

    @FXML
    private ImageView preview;

    @FXML
    private Slider blur;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        blur.valueProperty().addListener((slider, prevValue, currentValue) -> {
            gaussianBlur.setRadius(currentValue.intValue());
            preview.setEffect(gaussianBlur);
        });
    }

    @Override
    public void setImage(Image image) {
        gaussianBlur.setRadius(blur.getValue());
        preview.setImage(image);
        preview.setEffect(gaussianBlur);
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
