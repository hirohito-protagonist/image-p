package io.imagep.dialog;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;

public interface Dialog {
    void setImage(Image image);
    SimpleBooleanProperty closeProperty();
    SimpleObjectProperty<Image> imageProperty();
}
