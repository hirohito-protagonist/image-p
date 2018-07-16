package io.imagep;

import io.imagep.core.Utils;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;

import java.util.function.Function;

class Histogram {


    static XYChart.Series rgb(final Image image) {

        return Histogram.generateSeries(image, Utils::collectRGBSeries);
    }

    static XYChart.Series red(final Image image) {

        return Histogram.generateSeries(image, Utils::collectRedSeries);
    }

    static XYChart.Series green(final Image image) {

        return Histogram.generateSeries(image, Utils::collectGreenSeries);
    }

    static XYChart.Series blue(final Image image) {

        return Histogram.generateSeries(image, Utils::collectBlueSeries);
    }

    private static XYChart.Series generateSeries(final Image image, Function<int[], int[]> channelCollection) {

        int[] buffer = Utils.pixelsFromImage(image);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        int[] bufferSeries = channelCollection.apply(buffer);

        for (int i = 0; i < 256; i++) {
            series.getData().add(new XYChart.Data<String, Number>(Integer.toString(i), bufferSeries[i]));
        }
        return series;
    }
}
