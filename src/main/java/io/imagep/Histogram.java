package io.imagep;

import com.sun.istack.internal.NotNull;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;

import java.nio.IntBuffer;

public class Histogram {

    private static int[] getImagePixels(@NotNull final Image image) {
        int width = (int)image.getWidth();
        int height = (int)image.getHeight();
        int[] buffer = new int[width * height];
        PixelReader pixelReader = image.getPixelReader();
        WritablePixelFormat<IntBuffer> pixelFormat = PixelFormat.getIntArgbInstance();
        pixelReader.getPixels(0, 0, width, height, pixelFormat, buffer, 0, width);
        return buffer;
    }

    private static int[] collectRGBSeries(int[] pixels) {

        int[] series = new int[256];
        for (int i = 0; i < 256; i++) {
            series[i] = 0;
        }
        for (int i = 0; i < pixels.length; i++) {
            int argb = pixels[i];
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

    public static XYChart.Series rgb(@NotNull final Image image) {

        int[] buffer = Histogram.getImagePixels(image);
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        int[] rgbSeries = Histogram.collectRGBSeries(buffer);

        for (int i = 0; i < 256; i++) {
            series.getData().add(new XYChart.Data<String, Number>(Integer.toString(i), rgbSeries[i]));
        }
        return series;
    }
}
