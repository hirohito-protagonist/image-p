package io.imagep;

import io.imagep.core.HistogramOperation;
import io.imagep.core.filter.Pixelize;
import io.imagep.core.filter.Sobel;
import io.imagep.dialog.Dialog;
import io.imagep.core.Color;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

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
    private Button resetEffects;
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
        String[] fileSuffixes = ImageIO.getReaderFileSuffixes();
        String[] fileExtensions = Arrays.stream(fileSuffixes).map((v) -> "*." + v).toArray(String[]::new);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", fileExtensions)
        );

        dimensionLabel.setText(dimensionInformation(null));

        Optional<File> selectedFile = Optional.ofNullable(fileChooser.showOpenDialog(root.getScene().getWindow()));
        selectedFile.ifPresent((file) -> {
            Image image = new Image("file:" + file.getAbsolutePath());
            imageView.setFitWidth(image.getWidth());
            imageView.setFitHeight(image.getHeight());
            imageView.setImage(image);
            zoom.setDisable(false);
            contrast.setDisable(false);
            hue.setDisable(false);
            saturation.setDisable(false);
            brightness.setDisable(false);
            applyEffects.setDisable(false);
            resetEffects.setDisable(false);
            zoom.setValue(1.0);
            dimensionLabel.setText(dimensionInformation(image));
        });
    }

    @FXML
    private void imageSaveAsAction() {
        String[] extensions = ImageIO.getWriterFileSuffixes();
        String[] fileExtensions = Arrays.stream(extensions).map((v) -> "*." + v).toArray(String[]::new);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image As");
        Arrays.stream(fileExtensions).forEach((e) -> {
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(e, e)
            );
        });
        Optional<File> file = Optional.ofNullable(fileChooser.showSaveDialog(root.getScene().getWindow()));
        file.ifPresent((f) -> {
            getImage().ifPresent((i) -> {
                String fileName = f.getName();
                Arrays.stream(extensions).filter(fileName::contains).forEach((extension) -> {
                    try {
                        ImageIO.write(SwingFXUtils.fromFXImage(i, null), extension, f);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage());
                    }
                });
            });
        });
    }


    private String dimensionInformation(Image image) {

        return image == null ? "0 x 0px" : (int)image.getWidth() + " x " + (int)image.getHeight() + "px";
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ColorAdjust colorAdjust = new ColorAdjust();

        histogram.getYAxis().setTickLabelsVisible(false);
        histogram.getXAxis().setTickLabelsVisible(false);
        histogram.getXAxis().setTickMarkVisible(false);
        histogram.getYAxis().setTickMarkVisible(false);
        histogram.getYAxis().setTickLength(0);
        histogram.getXAxis().setTickLength(0);
        redHistogram.getYAxis().setTickLabelsVisible(false);
        redHistogram.getXAxis().setTickLabelsVisible(false);
        redHistogram.getXAxis().setTickMarkVisible(false);
        redHistogram.getYAxis().setTickMarkVisible(false);
        redHistogram.getYAxis().setTickLength(0);
        redHistogram.getXAxis().setTickLength(0);
        greenHistogram.getYAxis().setTickLabelsVisible(false);
        greenHistogram.getXAxis().setTickLabelsVisible(false);
        greenHistogram.getXAxis().setTickMarkVisible(false);
        greenHistogram.getYAxis().setTickMarkVisible(false);
        greenHistogram.getYAxis().setTickLength(0);
        greenHistogram.getXAxis().setTickLength(0);
        blueHistogram.getYAxis().setTickLabelsVisible(false);
        blueHistogram.getXAxis().setTickLabelsVisible(false);
        blueHistogram.getXAxis().setTickMarkVisible(false);
        blueHistogram.getYAxis().setTickMarkVisible(false);
        blueHistogram.getYAxis().setTickLength(0);
        blueHistogram.getXAxis().setTickLength(0);
        previewImage.effectProperty().bind(imageView.effectProperty());
        previewImage.imageProperty().bind(imageView.imageProperty());
        imageView.imageProperty().addListener((img) -> {
            getImage().ifPresent((image) -> {
                histogram.getData().setAll(
                    Histogram.red(image),
                    Histogram.green(image),
                    Histogram.blue(image)
                );
                redHistogram.getData().setAll(Histogram.red(image));
                greenHistogram.getData().setAll(Histogram.green(image));
                blueHistogram.getData().setAll(Histogram.blue(image));
            });
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
            getImage().ifPresent((image) -> {
                double scale = (double) currentValue;
                double w = image.getWidth() * scale;
                double h = image.getHeight() * scale;
                imageView.setFitWidth(w);
                imageView.setFitHeight(h);
                zoomInformation.setText((int)(scale * 100) + "%");
            });
        });
    }

    @FXML
    private void menuItemImageAction(ActionEvent e) {
        MenuItem node = (MenuItem) e.getSource();
        String operationType = (String) node.getUserData();
        getImage().ifPresent((image) -> {

            Map<String, Function<Image, Image>> operations = unaryImageOperation();
            if (operations.containsKey(operationType)) {
                imageView.setImage(operations.get(operationType).apply(image));
            }
            Map<String, Consumer<Void>> dialogs = imageDialogs();
            if (dialogs.containsKey(operationType)) {
                dialogs.get(operationType).accept(null);
            }
            switch (operationType) {
                case "pixelize":
                    imageView.setImage(Pixelize.apply(image, 9));
                    break;
                case "blurGaussian":
                    imageView.setImage(io.imagep.core.filter.GaussianBlur.apply(image, 100));
                    break;
            }
        });
    }


    private Map<String, Consumer<Void>> imageDialogs() {

        Map<String, Consumer<Void>> dialogs = new HashMap<>();

        dialogs.putIfAbsent("binarize", (e) -> createDialog("/fxml/binarize.fxml", "Binarize core"));
        dialogs.putIfAbsent("posterize", (e) -> createDialog("/fxml/posterize.fxml", "Posterize core"));
        dialogs.putIfAbsent("treshold", (e) -> createDialog("/fxml/threshold.fxml", "Threshold core"));
        dialogs.putIfAbsent("gammaCorrection", (e) -> createDialog("/fxml/gamma-correction.fxml", "Gamma correction"));
        dialogs.putIfAbsent("sepia", (e) -> createDialog("/fxml/sepia.fxml", "Sepia"));
        dialogs.putIfAbsent("blur", (e) -> createDialog("/fxml/blur.fxml", "Blur"));
        return dialogs;
    }

    private Map<String, Function<Image, Image>> unaryImageOperation() {

        Map<String, Function<Image, Image>> operations = new HashMap<>();

        operations.putIfAbsent("invert", Color::invert);
        operations.putIfAbsent("grayscale", Color::grayScale);
        operations.putIfAbsent("darken", Color::darker);
        operations.putIfAbsent("lighten", Color::lighten);
        operations.putIfAbsent("saturate", Color::saturate);
        operations.putIfAbsent("desaturate", Color::desaturate);
        operations.putIfAbsent("histogramAverage", HistogramOperation::average);
        operations.putIfAbsent("histogramRandom", HistogramOperation::random);
        operations.putIfAbsent("histogramStretch", HistogramOperation::stretch);
        operations.putIfAbsent("edgeDetection", Sobel::apply);
        return operations;
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

        getImage().ifPresent((image) -> {
            imageView.setImage(imageViewSnapshot(imageView));
            contrast.setValue(0);
            hue.setValue(0);
            saturation.setValue(0);
            brightness.setValue(0);
        });
    }

    @FXML
    private void resetEffectsAction(ActionEvent e) {

        contrast.setValue(0);
        hue.setValue(0);
        saturation.setValue(0);
        brightness.setValue(0);
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

    private Optional<Image> getImage() {

        return Optional.ofNullable(imageView.getImage());
    }
}
