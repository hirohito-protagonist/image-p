package io.imagep;

import io.imagep.core.Utils;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;

import java.util.Arrays;
import java.util.function.Function;

class Histogram {

    private static int[] collectRGBSeries(int[] pixels) {

        int[] series = new int[256];
        Arrays.fill(series, 0);
        for (int i = 0; i < 256; i++) {
            series[i] = 0;
        }
        for (int argb : pixels) {
            int r = (argb >> 16) & 0xff;
            int g = (argb >> 8) & 0xff;
            int b = argb & 0xff;
            if (r == g && r == b) {
                series[r]++;
            } else {
                series[r]++;
                series[g]++;
                series[b]++;
            }
        }
        return series;
    }

    private static int[] collectRedSeries(int[] pixels) {
        int[] series = new int[256];
        Arrays.fill(series, 0);
        for (int argb : pixels) {
            int r = (argb >> 16) & 0xff;
            series[r]++;
        }
        return series;
    }

    private static int[] collectGreenSeries(int[] pixels) {

        int[] series = new int[256];
        Arrays.fill(series, 0);
        for (int argb : pixels) {
            int g = (argb >> 8) & 0xff;
            series[g]++;
        }
        return series;
    }

    private static int[] collectBlueSeries(int[] pixels) {

        int[] series = new int[256];
        Arrays.fill(series, 0);
        for (int argb : pixels) {
            int b = argb & 0xff;
            series[b]++;
        }
        return series;
    }

    static XYChart.Series rgb(final Image image) {

        return Histogram.generateSeries(image, Histogram::collectRGBSeries);
    }

    static XYChart.Series red(final Image image) {

        return Histogram.generateSeries(image, Histogram::collectRedSeries);
    }

    static XYChart.Series green(final Image image) {

        return Histogram.generateSeries(image, Histogram::collectGreenSeries);
    }

    static XYChart.Series blue(final Image image) {

        return Histogram.generateSeries(image, Histogram::collectBlueSeries);
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
