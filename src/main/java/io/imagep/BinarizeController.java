package io.imagep;

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

public class BinarizeController implements Initializable {

    SimpleBooleanProperty closeProperty = new SimpleBooleanProperty(false);
    SimpleObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();

    @FXML
    private ImageView preview;

    @FXML
    private Slider high;
    @FXML
    private Slider low;
    @FXML
    private Button apply;
    @FXML
    private Button close;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        low.valueProperty().addListener((slider, prevValue, currentValue) -> {
            preview.setImage(ImageOperation.binarize(imageProperty.get(), currentValue.intValue(), (int)high.getValue()));
        });

        high.valueProperty().addListener((slider, prevValue, currentValue) -> {
            preview.setImage(ImageOperation.binarize(imageProperty.get(), (int)low.getValue(), currentValue.intValue()));
        });
    }

    void setImage(Image image) {
        preview.setImage(ImageOperation.binarize(image, (int)low.getValue(), (int)high.getValue()));
        imageProperty.set(image);
    }

    @FXML
    private void closeAction(ActionEvent e) {
        closeProperty.set(true);
    }

    @FXML
    private void applyAction(ActionEvent e) {
        imageProperty.set(ImageOperation.binarize(imageProperty.get(), (int)low.getValue(), (int)high.getValue()));
        closeProperty.set(true);
    }
}
