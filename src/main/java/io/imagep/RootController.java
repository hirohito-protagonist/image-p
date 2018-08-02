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
import java.util.Arrays;
import java.util.Optional;
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
            resetEffects.setDisable(false);
            zoom.setValue(1.0);
        }
        dimensionLabel.setText(dimensionInformation(image));
    }

    @FXML
    private void imageSaveAsAction() {
        String[] extensions = ImageIO.getWriterFileSuffixes();
        String[] filterExtension = Arrays.stream(extensions).map((v) -> "*." + v).toArray(String[]::new);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image As");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image Files", filterExtension)
        );
        Optional<File> file = Optional.ofNullable(fileChooser.showSaveDialog(root.getScene().getWindow()));
        Optional<Image> image = Optional.ofNullable(imageView.getImage());
        file.ifPresent((f) -> {
            image.ifPresent((i) -> {
                String fileName = f.getName();
                Arrays.stream(extensions).filter(fileName::contains).forEach((extension) -> {
                    try {
                        System.out.println(extension);
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
        imageView.imageProperty().addListener((image) -> {
            histogram.getData().setAll(
                Histogram.red(imageView.getImage()),
                Histogram.green(imageView.getImage()),
                Histogram.blue(imageView.getImage())
            );
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
    private void menuItemImageAction(ActionEvent e) {
        MenuItem node = (MenuItem) e.getSource();
        String operationType = (String) node.getUserData();
        getImage().ifPresent((image) -> {
            switch (operationType) {
                case "invert":
                    imageView.setImage(Color.invert(image));
                    break;
                case "grayscale":
                    imageView.setImage(Color.grayScale(image));
                    break;
                case "darken":
                    imageView.setImage(Color.darker(image));
                    break;
                case "lighten":
                    imageView.setImage(Color.lighten(image));
                    break;
                case "saturate":
                    imageView.setImage(Color.saturate(image));
                    break;
                case "desaturate":
                    imageView.setImage(Color.desaturate(image));
                    break;
                case "histogramAverage":
                    imageView.setImage(HistogramOperation.average(image));
                    break;
                case "histogramRandom":
                    imageView.setImage(HistogramOperation.random(image));
                    break;
                case "histogramStretch":
                    imageView.setImage(HistogramOperation.stretch(image));
                    break;
                case "edgeDetection":
                    imageView.setImage(Sobel.apply(image));
                    break;
                case "pixelize":
                    imageView.setImage(Pixelize.apply(image, 9));
                    break;
                case "blurGaussian":
                    imageView.setImage(io.imagep.core.filter.GaussianBlur.apply(image, 100));
                    break;
                case "binarize":
                    createDialog("/fxml/binarize.fxml", "Binarize core");
                    break;
                case "posterize":
                    createDialog("/fxml/posterize.fxml", "Posterize core");
                    break;
                case "treshold":
                    createDialog("/fxml/threshold.fxml", "Threshold core");
                    break;
                case "gammaCorrection":
                    createDialog("/fxml/gamma-correction.fxml", "Gamma correction");
                    break;
                case "sepia":
                    createDialog("/fxml/sepia.fxml", "Sepia");
                    break;
                case "blur":
                    createDialog("/fxml/blur.fxml", "Blur");
                    break;
            }
        });
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
