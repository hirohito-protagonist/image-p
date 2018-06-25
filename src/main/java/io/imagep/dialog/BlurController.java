package io.imagep.dialog;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Slider;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class BlurController implements Initializable, Dialog {

    SimpleBooleanProperty closeProperty = new SimpleBooleanProperty(false);
    SimpleObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
    BoxBlur boxBlur = new BoxBlur();

    @FXML
    private ImageView preview;

    @FXML
    private Slider blur;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        blur.valueProperty().addListener((slider, prevValue, currentValue) -> {
            boxBlur.setWidth(currentValue.intValue());
            boxBlur.setHeight(currentValue.intValue());
            preview.setEffect(boxBlur);
        });
    }

    @Override
    public void setImage(Image image) {
        boxBlur.setWidth(blur.getValue());
        boxBlur.setHeight(blur.getValue());
        preview.setImage(image);
        preview.setEffect(boxBlur);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);;
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
        ImageView imageView = new ImageView(image);
        imageView.setEffect(boxBlur);
        imageView.setFitWidth(image.getWidth());
        imageView.setFitHeight(image.getHeight());
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);
        imageView.snapshot(params, writableImage);

        return  writableImage;
    }
}
